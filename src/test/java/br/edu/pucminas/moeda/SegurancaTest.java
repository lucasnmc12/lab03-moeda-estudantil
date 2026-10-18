package br.edu.pucminas.moeda;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
class SegurancaTest extends BaseIntegracao {
    @org.springframework.beans.factory.annotation.Autowired br.edu.pucminas.moeda.servico.VantagemService beneficios;
    @Test void loginRealDosTresPerfisESenhaIncorreta() throws Exception {
        for (var email:new String[]{ana.getEmail(),professor.getEmail(),parceiro.getEmail()})
            mvc.perform(post("/entrar").with(csrf()).param("username",email).param("password","Senha123!"))
                .andExpect(redirectedUrl("/painel")).andExpect(authenticated().withUsername(email));
        mvc.perform(post("/entrar").with(csrf()).param("username",ana.getEmail()).param("password","errada"))
            .andExpect(redirectedUrl("/entrar?erro")).andExpect(unauthenticated());
    }
    @Test void contaAlheiaNaoPodeSerVistaEditadaOuRemovida() throws Exception {
        mvc.perform(get("/alunos/{id}",ana.getId()).with(user(bruno.getEmail()).roles("ALUNO"))).andExpect(status().isNotFound());
        mvc.perform(post("/alunos/{id}/excluir",ana.getId()).with(user(bruno.getEmail()).roles("ALUNO")).with(csrf())).andExpect(status().isNotFound());
        mvc.perform(post("/alunos/{id}",ana.getId()).with(user(bruno.getEmail()).roles("ALUNO")).with(csrf()).param("nome","Invasor"))
            .andExpect(status().isNotFound());
        mvc.perform(get("/empresas/{id}",parceiro.getId()).with(user(outroParceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isNotFound());
        mvc.perform(post("/empresas/{id}/excluir",parceiro.getId()).with(user(outroParceiro.getEmail()).roles("EMPRESA")).with(csrf())).andExpect(status().isNotFound());
        assertThrows(ResponseStatusException.class,()->acesso.exigirDono(bruno,ana.getId()));
        assertTrue(usuarios.findById(ana.getId()).orElseThrow().isAtivo());
    }
    @Test void rotasPrivadasPapeisECsrfSaoProtegidos() throws Exception {
        mvc.perform(get("/painel")).andExpect(status().is3xxRedirection());
        mvc.perform(get("/moedas/enviar").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isForbidden());
        mvc.perform(get("/extrato").with(user(parceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isForbidden());
        mvc.perform(get("/vantagens/nova").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isForbidden());
        mvc.perform(post("/empresas").param("nome","Teste").param("email","teste@teste.local").param("senha","Senha123!"))
            .andExpect(status().isForbidden());
        assertThrows(ResponseStatusException.class,()->moedas.enviar(ana.getEmail(),bruno.getId(),1,"teste"));
        assertThrows(ResponseStatusException.class,()->beneficios.salvar(ana.getEmail(),null,new br.edu.pucminas.moeda.dto.VantagemForm()));
    }
    @Test void contaDesativadaNaoEntraNemUsaSessaoAnterior() throws Exception {
        cadastro.remover(ana.getEmail(),ana.getId(),br.edu.pucminas.moeda.modelo.Aluno.class);
        mvc.perform(post("/entrar").with(csrf()).param("username",ana.getEmail()).param("password","Senha123!"))
            .andExpect(unauthenticated());
        mvc.perform(get("/painel").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isForbidden());
        assertThrows(ResponseStatusException.class,()->moedas.extrato(ana.getEmail()));
    }
    @Test void formulariosRenderizadosIncluemCsrf() throws Exception {
        for (String rota:new String[]{"/entrar","/alunos/novo","/empresas/novo"})
            mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"_csrf\"")));
    }
}
