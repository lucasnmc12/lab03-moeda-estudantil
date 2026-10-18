package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Notificacao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="transacao_id") private Transacao transacao;
    @Column(nullable=false, length=180) private String destinatario;
    @Column(nullable=false, length=200) private String assunto;
    @Column(nullable=false, length=3000) private String texto;
    private LocalDateTime enviadaEm;
    @Column(nullable=false) private int tentativas;
    protected Notificacao() {}
    public Notificacao(Transacao transacao, String destinatario, String assunto, String texto) {
        this.transacao=transacao; this.destinatario=destinatario; this.assunto=assunto; this.texto=texto;
    }
    public Long getId() { return id; }
    public String getDestinatario() { return destinatario; }
    public String getAssunto() { return assunto; }
    public String getTexto() { return texto; }
    public LocalDateTime getEnviadaEm() { return enviadaEm; }
    public int getTentativas() { return tentativas; }
    public void registrarEnvio() { tentativas++; enviadaEm=LocalDateTime.now(); }
    public void registrarFalha() { tentativas++; }
}
