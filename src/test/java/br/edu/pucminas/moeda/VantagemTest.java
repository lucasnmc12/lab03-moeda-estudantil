package br.edu.pucminas.moeda;
import br.edu.pucminas.moeda.dto.VantagemForm;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
class VantagemTest extends BaseIntegracao {
    @org.springframework.beans.factory.annotation.Autowired br.edu.pucminas.moeda.servico.VantagemService beneficios;
    static final byte[] PNG=Base64.getDecoder().decode("iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+aD1sAAAAASUVORK5CYII=");
    VantagemForm form() { var f=new VantagemForm();f.setDescricao("Desconto de 10%");f.setCusto(20);f.setFoto(new MockMultipartFile("foto","foto.png","image/png",PNG));return f; }
    @Test void parceiroCadastraEditaERetiraVantagemComFoto() throws Exception {
        mvc.perform(get("/vantagens/nova").with(user(parceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
        mvc.perform(multipart("/vantagens/nova").file((MockMultipartFile)form().getFoto()).with(user(parceiro.getEmail()).roles("EMPRESA")).with(csrf()).param("descricao","Desconto de 10%").param("custo","20"))
            .andExpect(redirectedUrl("/vantagens"));
        var v=vantagens.findAll().getFirst();
        mvc.perform(get("/vantagens/{id}/foto",v.getId()).with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isOk()).andExpect(content().contentType("image/png"));
        mvc.perform(get("/vantagens/{id}/editar",v.getId()).with(user(parceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
        var editar=form();editar.setDescricao("Nova descrição");editar.setFoto(null);beneficios.salvar(parceiro.getEmail(),v.getId(),editar);
        assertArrayEquals(PNG,vantagens.findById(v.getId()).orElseThrow().getFoto());
        beneficios.remover(parceiro.getEmail(),v.getId()); assertFalse(vantagens.findById(v.getId()).orElseThrow().isAtiva());
    }
    @Test void rejeitaFotoFalsaCustoInvalidoEVantagemAlheia() {
        var f=form();f.setFoto(new MockMultipartFile("foto","falso.png","image/png",new byte[]{1,2}));
        assertThrows(IllegalArgumentException.class,()->beneficios.salvar(parceiro.getEmail(),null,f));
        var semFoto=form();semFoto.setFoto(null);assertThrows(IllegalArgumentException.class,()->beneficios.salvar(parceiro.getEmail(),null,semFoto));
        var custo=form();custo.setCusto(0);assertThrows(IllegalArgumentException.class,()->beneficios.salvar(parceiro.getEmail(),null,custo));
        var v=oferta(20);assertThrows(ResponseStatusException.class,()->beneficios.salvar(outroParceiro.getEmail(),v.getId(),form()));
        assertThrows(ResponseStatusException.class,()->beneficios.remover(outroParceiro.getEmail(),v.getId()));
    }
    @Test void resgateDebitaEGeraMesmoCodigoNosDoisEmails() throws Exception {
        saldoAluno(100);var v=oferta(40);
        mvc.perform(get("/vantagens").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isOk());
        mvc.perform(post("/vantagens/{id}/resgatar",v.getId()).with(user(ana.getEmail()).roles("ALUNO")).with(csrf())).andExpect(redirectedUrl("/cupons"));
        var troca=trocas.findAll().getFirst();assertEquals(60,alunos.findById(ana.getId()).orElseThrow().getSaldo());assertEquals(2,notificacoes.count());
        assertTrue(notificacoes.findAll().stream().allMatch(n->n.getTexto().contains(troca.getCodigo())));
        assertEquals(1,beneficios.cupons(ana.getEmail()).size());assertEquals(0,beneficios.cupons(bruno.getEmail()).size());
        assertEquals(0,beneficios.cupons(outroParceiro.getEmail()).size());
        mvc.perform(get("/cupons").with(user(ana.getEmail()).roles("ALUNO"))).andExpect(status().isOk());
        mvc.perform(get("/cupons").with(user(parceiro.getEmail()).roles("EMPRESA"))).andExpect(status().isOk());
    }
    @Test void saldoInsuficienteOuOfertaRetiradaNaoProduzCupom() {
        var v=oferta(40);saldoAluno(30);assertThrows(IllegalArgumentException.class,()->beneficios.resgatar(ana.getEmail(),v.getId()));
        saldoAluno(100);beneficios.remover(parceiro.getEmail(),v.getId());assertThrows(IllegalArgumentException.class,()->beneficios.resgatar(ana.getEmail(),v.getId()));
        assertEquals(100,alunos.findById(ana.getId()).orElseThrow().getSaldo());assertEquals(0,trocas.count());assertEquals(0,notificacoes.count());
    }
    @Test void somenteDonoConfirmaCupomUmaVezEHistoricoSobreviveRemocao() {
        saldoAluno(100);var troca=beneficios.resgatar(ana.getEmail(),oferta(40).getId());
        assertThrows(ResponseStatusException.class,()->beneficios.utilizar(outroParceiro.getEmail(),troca.getCodigo()));
        assertThrows(ResponseStatusException.class,()->beneficios.utilizar(ana.getEmail(),troca.getCodigo()));
        beneficios.utilizar(parceiro.getEmail(),troca.getCodigo());assertThrows(IllegalArgumentException.class,()->beneficios.utilizar(parceiro.getEmail(),troca.getCodigo()));
        cadastro.remover(ana.getEmail(),ana.getId(),br.edu.pucminas.moeda.modelo.Aluno.class);
        assertEquals(1,beneficios.cupons(parceiro.getEmail()).size());assertNotNull(trocas.findById(troca.getId()).orElseThrow().getUtilizadaEm());
    }
    @Test void resgatesConcorrentesNaoGeramSaldoNegativo() throws Exception {
        saldoAluno(100);var v=oferta(60);
        var pool=java.util.concurrent.Executors.newFixedThreadPool(2);
        var inicio=new java.util.concurrent.CountDownLatch(1);
        java.util.concurrent.Callable<Boolean> tarefa=()->{inicio.await();try{beneficios.resgatar(ana.getEmail(),v.getId());return true;}catch(IllegalArgumentException e){return false;}};
        try {
            var a=pool.submit(tarefa);var b=pool.submit(tarefa);inicio.countDown();
            assertNotEquals(a.get(15,java.util.concurrent.TimeUnit.SECONDS),b.get(15,java.util.concurrent.TimeUnit.SECONDS));
        } finally {pool.shutdownNow();}
        assertEquals(40,alunos.findById(ana.getId()).orElseThrow().getSaldo());assertEquals(1,trocas.count());assertEquals(2,notificacoes.count());
    }
}
