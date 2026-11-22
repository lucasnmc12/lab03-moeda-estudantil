package br.edu.pucminas.moeda;

import br.edu.pucminas.moeda.modelo.TrocaVantagem;
import br.edu.pucminas.moeda.servico.EmailReconhecimento;
import br.edu.pucminas.moeda.servico.VantagemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

class TrocaAtomicidadeTest extends BaseIntegracao {
    @Autowired VantagemService beneficios;
    @MockitoSpyBean EmailReconhecimento emails;

    @Test void falhaNoEmailDaEmpresaDesfazDebitoCupomEEmailDoAluno() {
        saldoAluno(100);
        var vantagem = oferta(40);
        doThrow(new IllegalStateException("Falha simulada no template da empresa"))
            .when(emails).confirmacaoEmpresa(any(TrocaVantagem.class));

        assertThrows(IllegalStateException.class,
            () -> beneficios.resgatar(ana.getEmail(), vantagem.getId()));

        assertEquals(100, alunos.findById(ana.getId()).orElseThrow().getSaldo());
        assertEquals(0, trocas.count());
        assertEquals(0, notificacoes.count());
    }
}
