package br.edu.pucminas.moeda.servico;
import br.edu.pucminas.moeda.modelo.Notificacao;
import br.edu.pucminas.moeda.repositorio.NotificacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class NotificacaoService {
    private static final Logger LOG=LoggerFactory.getLogger(NotificacaoService.class);
    private final NotificacaoRepository notificacoes;
    private final JavaMailSender sender;
    private final TransactionTemplate transacao;
    private final String remetente;
    public NotificacaoService(NotificacaoRepository notificacoes, JavaMailSender sender, PlatformTransactionManager manager,
            @Value("${moeda.email.remetente}") String remetente) {
        this.notificacoes=notificacoes; this.sender=sender; this.transacao=new TransactionTemplate(manager); this.remetente=remetente;
    }
    public void entregarPendentes() {
        for (Notificacao pendente:notificacoes.findTop20ByEnviadaEmIsNullOrderByTentativasAscIdAsc()) {
            transacao.executeWithoutResult(status -> {
                Notificacao mensagem=notificacoes.bloquear(pendente.getId()).orElseThrow();
                if (mensagem.getEnviadaEm()!=null) return;
                SimpleMailMessage email=new SimpleMailMessage();
                email.setFrom(remetente); email.setTo(mensagem.getDestinatario());
                email.setSubject(mensagem.getAssunto()); email.setText(mensagem.getTexto());
                try { sender.send(email); mensagem.registrarEnvio(); }
                catch (MailException exception) {
                    mensagem.registrarFalha();
                    LOG.warn("SMTP indisponível. Notificação {} permanece pendente (tentativa {}).",mensagem.getId(),mensagem.getTentativas());
                }
            });
        }
    }
}
