package br.edu.pucminas.moeda;

import br.edu.pucminas.moeda.modelo.Professor;
import br.edu.pucminas.moeda.modelo.Transacao;
import br.edu.pucminas.moeda.servico.VantagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ExtratoMvcTest extends BaseIntegracao {
    @Autowired VantagemService beneficios;

    private MockHttpSession entrar(String email) throws Exception {
        return (MockHttpSession) mvc.perform(post("/entrar").with(csrf())
                .param("username", email).param("password", "Senha123!"))
            .andExpect(authenticated().withUsername(email))
            .andReturn().getRequest().getSession(false);
    }

    private void datar(Transacao transacao, int hora) {
        // Datas distintas tornam a ordenação verificável sem depender da velocidade do teste.
        jdbc.update("UPDATE transacao SET data=? WHERE id=?",
            LocalDateTime.of(2026, 11, 9, hora, 0), transacao.getId());
    }

    @Test void alunoVeRecebimentosEResgatesPropriosEmOrdemDecrescente() throws Exception {
        saldoProfessor(1000);
        var primeiro = moedas.enviar(professor.getEmail(), ana.getId(), 120, "Primeiro reconhecimento");
        var ultimo = moedas.enviar(professor.getEmail(), ana.getId(), 30, "Reconhecimento mais recente");
        var resgate = beneficios.resgatar(ana.getEmail(), oferta(40).getId());
        var outroAluno = moedas.enviar(professor.getEmail(), bruno.getId(), 99, "Reconhecimento exclusivo do Bruno");
        datar(primeiro, 9);
        datar(resgate, 10);
        datar(ultimo, 11);
        datar(outroAluno, 12);

        assertEquals(List.of(ultimo.getId(), resgate.getId(), primeiro.getId()),
            moedas.extrato(ana.getEmail()).stream().map(Transacao::getId).toList());
        mvc.perform(get("/extrato").session(entrar(ana.getEmail())))
            .andExpect(status().isOk())
            .andExpect(extrato(110, List.of(
                List.of("09/11/2026 11:00", "Reconhecimento: Reconhecimento mais recente", "+30"),
                List.of("09/11/2026 10:00", "Resgate: Desconto em livro", "−40"),
                List.of("09/11/2026 09:00", "Reconhecimento: Primeiro reconhecimento", "+120"))));
        mvc.perform(get("/extrato").session(entrar(bruno.getEmail())))
            .andExpect(status().isOk())
            .andExpect(extrato(99, List.of(
                List.of("09/11/2026 12:00", "Reconhecimento: Reconhecimento exclusivo do Bruno", "+99"))));
    }

    @Test void professoresVeemApenasSeusCreditosEEnvios() throws Exception {
        var outraProfessora = professores.save(new Professor("Outra professora", "outra@teste.local",
            encoder.encode("Senha123!"), "11144477735", "Engenharia", instituicao));
        moedas.creditarSemestre(LocalDate.of(2026, 11, 9));
        var primeiroEnvio = moedas.enviar(professor.getEmail(), ana.getId(), 100, "Atividade de computação");
        var segundoEnvio = moedas.enviar(outraProfessora.getEmail(), bruno.getId(), 70, "Atividade de engenharia");
        var primeiroCredito = creditos.findByProfessorIdOrderByDataDesc(professor.getId()).getFirst();
        var segundoCredito = creditos.findByProfessorIdOrderByDataDesc(outraProfessora.getId()).getFirst();
        datar(primeiroCredito, 8);
        datar(segundoCredito, 8);
        datar(primeiroEnvio, 9);
        datar(segundoEnvio, 10);

        assertEquals(List.of(primeiroEnvio.getId(), primeiroCredito.getId()),
            moedas.extrato(professor.getEmail()).stream().map(Transacao::getId).toList());
        assertEquals(List.of(segundoEnvio.getId(), segundoCredito.getId()),
            moedas.extrato(outraProfessora.getEmail()).stream().map(Transacao::getId).toList());
        mvc.perform(get("/extrato").session(entrar(professor.getEmail())))
            .andExpect(status().isOk())
            .andExpect(extrato(900, List.of(
                List.of("09/11/2026 09:00", "Reconhecimento: Atividade de computação", "−100"),
                List.of("09/11/2026 08:00", "Crédito do semestre 2026-2", "+1000"))));
        mvc.perform(get("/extrato").session(entrar(outraProfessora.getEmail())))
            .andExpect(status().isOk())
            .andExpect(extrato(930, List.of(
                List.of("09/11/2026 10:00", "Reconhecimento: Atividade de engenharia", "−70"),
                List.of("09/11/2026 08:00", "Crédito do semestre 2026-2", "+1000"))));
    }

    private ResultMatcher extrato(int saldo, List<List<String>> linhasEsperadas) {
        return resultado -> {
            String html = resultado.getResponse().getContentAsString();
            var saldoRenderizado = Pattern.compile("<p class=\"saldo-linha\">\\s*<strong>(.*?)</strong>", Pattern.DOTALL)
                .matcher(html);
            assertTrue(saldoRenderizado.find(), "Saldo deve aparecer no extrato");
            assertEquals(Integer.toString(saldo), saldoRenderizado.group(1).strip());
            var linhas = Pattern.compile("<tr>(.*?)</tr>", Pattern.DOTALL).matcher(html).results()
                .filter(linha -> linha.group(1).contains("<td"))
                .map(linha -> Pattern.compile("<td[^>]*>(.*?)</td>", Pattern.DOTALL)
                    .matcher(linha.group(1)).results().map(celula -> celula.group(1).strip()).toList())
                .toList();
            assertEquals(linhasEsperadas, linhas, "Data, descrição, sinal e ordem das movimentações");
        };
    }
}
