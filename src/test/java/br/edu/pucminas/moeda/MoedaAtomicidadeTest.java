package br.edu.pucminas.moeda;

import br.edu.pucminas.moeda.modelo.EnvioMoeda;
import br.edu.pucminas.moeda.servico.EmailReconhecimento;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class MoedaAtomicidadeTest extends BaseIntegracao {
    @MockitoSpyBean EmailReconhecimento emails;

    @Test void falhaNoSegundoTemplateDesfazSaldosTransacaoEPrimeiraNotificacao() {
        saldoProfessor(100);
        doThrow(new IllegalStateException("Falha simulada no segundo template"))
            .when(emails).recebimentoAluno(any(EnvioMoeda.class));
        assertThrows(IllegalStateException.class,
            () -> moedas.enviar(professor.getEmail(), ana.getId(), 30, "Participação"));
        assertEquals(100, professores.findById(professor.getId()).orElseThrow().getSaldo());
        assertEquals(0, alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(0, envios.count());
        assertEquals(0, notificacoes.count());
    }
}
