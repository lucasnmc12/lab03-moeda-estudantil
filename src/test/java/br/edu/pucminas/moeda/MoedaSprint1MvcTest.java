package br.edu.pucminas.moeda;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MoedaSprint1MvcTest extends BaseIntegracao {
    private MockHttpSession entrar(String email) throws Exception {
        return (MockHttpSession) mvc.perform(post("/entrar").with(csrf())
                .param("username", email).param("password", "Senha123!"))
            .andExpect(authenticated().withUsername(email))
            .andReturn().getRequest().getSession(false);
    }

    @Test void formularioEnviaMoedasEExtratosMostramSaidaEEntradaSomenteDosEnvolvidos() throws Exception {
        saldoProfessor(1000);
        var sessaoProfessor = entrar(professor.getEmail());
        mvc.perform(get("/moedas/enviar").session(sessaoProfessor))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Valorize uma conquista")));
        mvc.perform(post("/moedas/enviar").session(sessaoProfessor).with(csrf())
                .param("alunoId", ana.getId().toString()).param("quantidade", "120")
                .param("motivo", "Participação na atividade de modelagem"))
            .andExpect(redirectedUrl("/extrato"))
            .andExpect(flash().attribute("sucesso", containsString("confirmação e recebimento")));
        mvc.perform(get("/extrato").session(sessaoProfessor))
            .andExpect(status().isOk()).andExpect(content().string(containsString("880")))
            .andExpect(content().string(containsString("−120")));
        mvc.perform(get("/extrato").session(entrar(ana.getEmail())))
            .andExpect(status().isOk()).andExpect(content().string(containsString("+120")))
            .andExpect(content().string(containsString("Participação na atividade de modelagem")));
        mvc.perform(get("/extrato").session(entrar(bruno.getEmail())))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Seu extrato começa")));
        assertEquals(1, envios.count());
        assertEquals(2, notificacoes.count());
    }

    @Test void saldoInsuficienteExibeErroSemMovimentacaoOuEmails() throws Exception {
        saldoProfessor(100);
        mvc.perform(post("/moedas/enviar").session(entrar(professor.getEmail())).with(csrf())
                .param("alunoId", ana.getId().toString()).param("quantidade", "101").param("motivo", "Participação"))
            .andExpect(status().isOk()).andExpect(view().name("moedas/enviar"))
            .andExpect(content().string(containsString("Saldo de moedas insuficiente")));
        assertEquals(100, professores.findById(professor.getId()).orElseThrow().getSaldo());
        assertEquals(0, alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(0, envios.count());
        assertEquals(0, notificacoes.count());
    }

    @Test void templatesPreservamMotivoLiteralComAcentosESemInterpretarExpressoes() {
        saldoProfessor(100);
        String motivo = "Colaboração <especial> & dedicação: ${7*7}";
        moedas.enviar(professor.getEmail(), ana.getId(), 10, motivo);
        assertEquals(2, notificacoes.count());
        for (var mensagem : notificacoes.findAll()) {
            assertTrue(mensagem.getTexto().contains(motivo));
            assertFalse(mensagem.getTexto().contains("&lt;especial&gt;"));
        }
    }
}
