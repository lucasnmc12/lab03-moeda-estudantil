package br.edu.pucminas.moeda;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
class CadastroMvcTest extends BaseCadastro {
    @Test void alunoCadastraConsultaEditaERemovePelaInterface() throws Exception {
        mvc.perform(get("/alunos/novo")).andExpect(status().isOk()).andExpect(content().string(containsString("Instituição de ensino")));
        mvc.perform(post("/alunos").with(csrf()).param("nome","Carla").param("email","carla@teste.local").param("senha","Senha123!")
            .param("cpf","390.533.447-05").param("rg","RG-3").param("endereco","Rua C").param("curso","Direito").param("instituicaoId",instituicao.getId().toString()))
            .andExpect(redirectedUrl("/entrar"));
        var carla=usuarios.findByEmail("carla@teste.local").orElseThrow();
        assertTrue(encoder.matches("Senha123!",carla.getSenhaHash()));
        mvc.perform(get("/alunos").with(user(carla.getEmail()).roles("ALUNO"))).andExpect(status().isOk()).andExpect(content().string(containsString("Carla")));
        mvc.perform(get("/alunos/{id}",carla.getId()).with(user(carla.getEmail()).roles("ALUNO"))).andExpect(status().isOk());
        mvc.perform(get("/alunos/{id}/editar",carla.getId()).with(user(carla.getEmail()).roles("ALUNO"))).andExpect(status().isOk());
        mvc.perform(post("/alunos/{id}",carla.getId()).with(user(carla.getEmail()).roles("ALUNO")).with(csrf()).param("nome","Carla Silva")
            .param("rg","RG-3").param("endereco","Rua Nova").param("curso","Direito").param("instituicaoId",instituicao.getId().toString()).param("saldo","99999"))
            .andExpect(redirectedUrl("/alunos/"+carla.getId()));
        assertEquals("Carla Silva",usuarios.findById(carla.getId()).orElseThrow().getNome());
        assertEquals(0,alunos.findById(carla.getId()).orElseThrow().getSaldo());
        mvc.perform(post("/alunos/{id}/excluir",carla.getId()).with(user(carla.getEmail()).roles("ALUNO")).with(csrf()))
            .andExpect(redirectedUrl("/entrar?removida"));
        assertFalse(usuarios.findById(carla.getId()).orElseThrow().isAtivo());
    }
    @Test void empresaCadastraListaConsultaEditaERemove() throws Exception {
        mvc.perform(get("/empresas/novo")).andExpect(status().isOk());
        mvc.perform(post("/empresas").with(csrf()).param("nome","Restaurante").param("email","restaurante@teste.local").param("senha","Senha123!"))
            .andExpect(redirectedUrl("/entrar"));
        var conta=usuarios.findByEmail("restaurante@teste.local").orElseThrow();
        mvc.perform(get("/empresas").with(user(conta.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
        mvc.perform(get("/empresas/{id}",conta.getId()).with(user(conta.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
        mvc.perform(get("/empresas/{id}/editar",conta.getId()).with(user(conta.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
        mvc.perform(post("/empresas/{id}",conta.getId()).with(user(conta.getEmail()).roles("EMPRESA")).with(csrf()).param("nome","Restaurante Novo"))
            .andExpect(redirectedUrl("/empresas/"+conta.getId()));
        assertEquals("Restaurante Novo",usuarios.findById(conta.getId()).orElseThrow().getNome());
        mvc.perform(post("/empresas/{id}/excluir",conta.getId()).with(user(conta.getEmail()).roles("EMPRESA")).with(csrf())).andExpect(status().is3xxRedirection());
        assertFalse(usuarios.findById(conta.getId()).orElseThrow().isAtivo());
    }
    @Test void rejeitaCamposInvalidosCpfInvalidoInstituicaoEDuplicidade() throws Exception {
        mvc.perform(post("/alunos").with(csrf()).param("nome","").param("email","email-invalido").param("senha","curta").param("cpf","123"))
            .andExpect(status().isOk()).andExpect(model().attributeHasFieldErrors("form","nome","email","senha","cpf","rg","endereco","curso","instituicaoId"));
        mvc.perform(post("/alunos").with(csrf()).param("nome","Teste").param("email","novo@teste.local").param("senha","Senha123!")
            .param("cpf",ana.getCpf()).param("rg","RG").param("endereco","Rua").param("curso","Curso").param("instituicaoId",instituicao.getId().toString()))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Já existe um aluno com este CPF")));
        mvc.perform(post("/alunos").with(csrf()).param("nome","Teste").param("email","novo@teste.local").param("senha","Senha123!")
            .param("cpf","39053344705").param("rg","RG").param("endereco","Rua").param("curso","Curso").param("instituicaoId","999999"))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Selecione uma instituição participante")));
        mvc.perform(post("/empresas").with(csrf()).param("nome","Teste").param("email",ana.getEmail()).param("senha","Senha123!"))
            .andExpect(status().isOk()).andExpect(content().string(containsString("Já existe uma conta com este email")));
        mvc.perform(post("/empresas").with(csrf()).param("nome","").param("email","ruim").param("senha","1"))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("form"));
        assertEquals(5,usuarios.count());
    }
    @Test void edicaoInvalidaPreservaDados() throws Exception {
        mvc.perform(post("/alunos/{id}",ana.getId()).with(user(ana.getEmail()).roles("ALUNO")).with(csrf()).param("nome","").param("rg","").param("endereco","").param("curso",""))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("form"));
        mvc.perform(post("/empresas/{id}",parceiro.getId()).with(user(parceiro.getEmail()).roles("EMPRESA")).with(csrf()).param("nome",""))
            .andExpect(status().isOk()).andExpect(model().attributeHasErrors("form"));
        assertEquals("Ana",usuarios.findById(ana.getId()).orElseThrow().getNome());
    }
}
