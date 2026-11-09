package br.edu.pucminas.moeda;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.concurrent.*;
import static org.junit.jupiter.api.Assertions.*;
class MoedaServiceTest extends BaseIntegracao {
    @Test void creditoSemestralAcumulaSemDuplicar() {
        moedas.creditarSemestre(LocalDate.of(2026,1,1)); moedas.creditarSemestre(LocalDate.of(2026,6,30));
        assertEquals(1000,professores.findById(professor.getId()).orElseThrow().getSaldo());
        moedas.enviar(professor.getEmail(),ana.getId(),150,"Participação em aula");
        moedas.creditarSemestre(LocalDate.of(2026,7,1));
        assertEquals(1850,professores.findById(professor.getId()).orElseThrow().getSaldo()); assertEquals(2,creditos.count());
    }
    @Test void extratoRenderizaLancamentosDoAlunoEDoProfessor() throws Exception {
        saldoProfessor(1000);moedas.enviar(professor.getEmail(),ana.getId(),120,"Participação");
        for (var email:new String[]{ana.getEmail(),professor.getEmail()})
            mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/extrato")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user(email).roles(email.equals(ana.getEmail())?"ALUNO":"PROFESSOR")))
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.content().string(org.hamcrest.Matchers.containsString("Participação")));
    }
    @Test void envioDebitaCreditaRegistraMotivoEDuasNotificacoes() {
        saldoProfessor(1000); moedas.enviar(professor.getEmail(),ana.getId(),125,"Bom trabalho");
        assertEquals(875,professores.findById(professor.getId()).orElseThrow().getSaldo());
        assertEquals(125,alunos.findById(ana.getId()).orElseThrow().getSaldo()); assertEquals(2,notificacoes.count());
        assertEquals("Bom trabalho",envios.findAll().getFirst().getMotivo());
        var mensagens=notificacoes.findAll();
        var confirmacao=mensagens.stream().filter(n->n.getDestinatario().equals(professor.getEmail())).findFirst().orElseThrow();
        var recebimento=mensagens.stream().filter(n->n.getDestinatario().equals(ana.getEmail())).findFirst().orElseThrow();
        assertTrue(confirmacao.getTexto().contains("Você enviou 125 moedas para Ana"));
        assertTrue(recebimento.getTexto().contains("Você recebeu 125 moedas de Professor"));
        assertTrue(mensagens.stream().allMatch(n->n.getTexto().contains("Bom trabalho")));
        assertEquals(1,moedas.extrato(ana.getEmail()).size()); assertEquals(0,moedas.extrato(bruno.getEmail()).size());
    }
    @Test void envioInvalidoNaoAlteraSaldoNemCriaLancamento() {
        saldoProfessor(100);
        assertThrows(IllegalArgumentException.class,()->moedas.enviar(professor.getEmail(),ana.getId(),101,"Motivo"));
        assertThrows(IllegalArgumentException.class,()->moedas.enviar(professor.getEmail(),ana.getId(),-1,"Motivo"));
        assertThrows(IllegalArgumentException.class,()->moedas.enviar(professor.getEmail(),ana.getId(),0,"Motivo"));
        assertThrows(IllegalArgumentException.class,()->moedas.enviar(professor.getEmail(),ana.getId(),1," "));
        assertThrows(IllegalArgumentException.class,()->moedas.enviar(professor.getEmail(),999999L,1,"Motivo"));
        assertEquals(100,professores.findById(professor.getId()).orElseThrow().getSaldo()); assertEquals(0,alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(0,envios.count()); assertEquals(0,notificacoes.count());
    }
    @Test void enviosConcorrentesNaoGastamAcimaDoSaldo() throws Exception {
        saldoProfessor(1000); ExecutorService pool=Executors.newFixedThreadPool(2); CountDownLatch inicio=new CountDownLatch(1);
        Callable<Boolean> enviar=()->{ inicio.await(); try { moedas.enviar(professor.getEmail(),ana.getId(),600,"Reconhecimento"); return true; } catch(IllegalArgumentException e) { return false; } };
        try { var a=pool.submit(enviar);var b=pool.submit(enviar);inicio.countDown();assertNotEquals(a.get(15,TimeUnit.SECONDS),b.get(15,TimeUnit.SECONDS)); }
        finally { pool.shutdownNow(); }
        assertEquals(400,professores.findById(professor.getId()).orElseThrow().getSaldo()); assertEquals(600,alunos.findById(ana.getId()).orElseThrow().getSaldo());assertEquals(1,envios.count());
    }
}
