package br.edu.pucminas.moeda.config;
import br.edu.pucminas.moeda.servico.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.*;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import java.time.*;
@Configuration @EnableScheduling
@ConditionalOnProperty(name="moeda.jobs.ativos",havingValue="true",matchIfMissing=true)
public class RotinasConfig {
    private final MoedaService moedas;
    private final NotificacaoService notificacoes;
    public RotinasConfig(MoedaService moedas, NotificacaoService notificacoes) { this.moedas=moedas; this.notificacoes=notificacoes; }
    @EventListener(ApplicationReadyEvent.class)
    public void iniciar() { creditar(); }
    @Scheduled(cron="0 0 0 * * *",zone="America/Sao_Paulo")
    public void creditar() { moedas.creditarSemestre(LocalDate.now(ZoneId.of("America/Sao_Paulo"))); }
    @Scheduled(fixedDelay=15000,initialDelay=15000)
    public void enviarEmails() { notificacoes.entregarPendentes(); }
}
