package br.edu.pucminas.moeda;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
class CadastroAcessoTest extends BaseCadastro {
    @Test void contaAlheiaEPapelIncorretoSaoNegados() throws Exception {
        mvc.perform(get("/alunos/{id}",ana.getId()).with(user(bruno.getEmail()).roles("ALUNO"))).andExpect(status().isNotFound());
        mvc.perform(post("/alunos/{id}/excluir",ana.getId()).with(user(bruno.getEmail()).roles("ALUNO")).with(csrf())).andExpect(status().isNotFound());
        mvc.perform(get("/empresas/{id}",parceiro.getId()).with(user(outroParceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isNotFound());
        mvc.perform(get("/empresas").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isForbidden());
    }
    @Test void loginRealECsrfProtegemCadastro() throws Exception {
        mvc.perform(post("/entrar").with(csrf()).param("username",ana.getEmail()).param("password","Senha123!"))
            .andExpect(redirectedUrl("/painel")).andExpect(authenticated().withUsername(ana.getEmail()));
        mvc.perform(post("/entrar").with(csrf()).param("username",ana.getEmail()).param("password","incorreta"))
            .andExpect(unauthenticated());
        mvc.perform(post("/empresas").param("nome","Teste").param("email","teste@local").param("senha","Senha123!"))
            .andExpect(status().isForbidden());
    }
}
