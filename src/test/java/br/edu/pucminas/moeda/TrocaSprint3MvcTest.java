package br.edu.pucminas.moeda;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TrocaSprint3MvcTest extends BaseIntegracao {
    @org.springframework.beans.factory.annotation.Autowired
    br.edu.pucminas.moeda.servico.VantagemService beneficios;
    private MockHttpSession entrar(String email) throws Exception {
        return (MockHttpSession) mvc.perform(post("/entrar").with(csrf())
                .param("username", email).param("password", "Senha123!"))
            .andExpect(authenticated().withUsername(email))
            .andReturn().getRequest().getSession(false);
    }

    @Test void alunoGanhaConsultaTrocaEEmpresaConfirmaMesmoCupom() throws Exception {
        saldoProfessor(1000);
        var vantagem = oferta(40);
        mvc.perform(post("/moedas/enviar").session(entrar(professor.getEmail())).with(csrf())
                .param("alunoId", ana.getId().toString()).param("quantidade", "120")
                .param("motivo", "Colaboração na modelagem"))
            .andExpect(redirectedUrl("/extrato"));
        var aluno = entrar(ana.getEmail());
        mvc.perform(get("/vantagens").session(aluno))
            .andExpect(content().string(containsString("Desconto em livro")));
        mvc.perform(post("/vantagens/{id}/resgatar", vantagem.getId()).session(aluno).with(csrf()))
            .andExpect(redirectedUrl("/cupons"));
        var troca = trocas.findAll().getFirst();
        assertEquals(80, alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(880, professores.findById(professor.getId()).orElseThrow().getSaldo());
        mvc.perform(get("/cupons").session(aluno))
            .andExpect(content().string(containsString(troca.getCodigo())));
        mvc.perform(get("/extrato").session(aluno))
            .andExpect(content().string(containsString("+120")))
            .andExpect(content().string(containsString("−40")));
        mvc.perform(get("/cupons").session(entrar(bruno.getEmail())))
            .andExpect(content().string(org.hamcrest.Matchers.not(containsString(troca.getCodigo()))));
        var mensagens = notificacoes.findAll().stream()
            .filter(n -> n.getTexto().contains(troca.getCodigo())).toList();
        assertEquals(2, mensagens.size());
        var cupom = mensagens.stream().filter(n -> n.getDestinatario().equals(ana.getEmail())).findFirst().orElseThrow();
        var confirmacao = mensagens.stream().filter(n -> n.getDestinatario().equals(parceiro.getEmail())).findFirst().orElseThrow();
        assertTrue(cupom.getTexto().contains("Você trocou 40 moedas"));
        assertTrue(confirmacao.getTexto().contains("Ana resgatou a vantagem"));
        assertNotEquals(cupom.getTexto(), confirmacao.getTexto());
        var empresa = entrar(parceiro.getEmail());
        mvc.perform(post("/cupons/{codigo}/utilizar", troca.getCodigo()).session(empresa).with(csrf()))
            .andExpect(redirectedUrl("/cupons"));
        mvc.perform(post("/cupons/{codigo}/utilizar", troca.getCodigo()).session(empresa).with(csrf()))
            .andExpect(flash().attribute("erro", "Este cupom já foi utilizado."));
        assertNotNull(trocas.findById(troca.getId()).orElseThrow().getUtilizadaEm());
        assertEquals(1, trocas.count());
        assertEquals(4, notificacoes.count());
    }

    @Test void saldoInsuficienteMostraErroSemAlterarSaldoHistoricoOuEmails() throws Exception {
        saldoAluno(30);
        mvc.perform(post("/vantagens/{id}/resgatar", oferta(40).getId())
                .session(entrar(ana.getEmail())).with(csrf()))
            .andExpect(redirectedUrl("/vantagens"))
            .andExpect(flash().attribute("erro", containsString("Saldo de moedas insuficiente")));
        assertEquals(30, alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(0, trocas.count());
        assertEquals(0, notificacoes.count());
    }

    @Test void descricaoDoCupomPreservaAcentosEExpressoesLiterais() {
        saldoAluno(100);
        var vantagem = oferta(40);
        vantagem.atualizar("Café <especial> & pão ${7*7}", 40, vantagem.getFoto(), vantagem.getFotoTipo());
        vantagens.save(vantagem);
        beneficios.resgatar(ana.getEmail(), vantagem.getId());
        for (var mensagem : notificacoes.findAll()) {
            assertTrue(mensagem.getTexto().contains("Café <especial> & pão ${7*7}"));
        }
    }
}
