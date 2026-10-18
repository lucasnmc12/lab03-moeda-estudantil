package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Entity
public class TrocaVantagem extends Transacao {
    @ManyToOne(optional=false) @JoinColumn(name="aluno_id") private Aluno aluno;
    @ManyToOne(optional=false) @JoinColumn(name="vantagem_id") private Vantagem vantagem;
    @Column(nullable=false, unique=true, length=36) private String codigo=UUID.randomUUID().toString();
    @Column(nullable=false, length=500) private String descricaoVantagem;
    private LocalDateTime utilizadaEm;
    protected TrocaVantagem() {}
    public TrocaVantagem(Aluno aluno, Vantagem vantagem) {
        super(vantagem.getCusto()); this.aluno=aluno; this.vantagem=vantagem; this.descricaoVantagem=vantagem.getDescricao();
    }
    public Aluno getAluno() { return aluno; }
    public Vantagem getVantagem() { return vantagem; }
    public String getCodigo() { return codigo; }
    public String getDescricaoVantagem() { return descricaoVantagem; }
    public LocalDateTime getUtilizadaEm() { return utilizadaEm; }
    public String getTipo() { return "RESGATE"; }
    public String getDescricao() { return "Resgate: "+descricaoVantagem; }
    public void confirmarUso() {
        if (utilizadaEm!=null) throw new IllegalArgumentException("Este cupom já foi utilizado.");
        utilizadaEm=LocalDateTime.now();
    }
}
