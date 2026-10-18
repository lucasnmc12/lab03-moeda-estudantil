package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity @Inheritance(strategy=InheritanceType.JOINED)
public abstract class Transacao {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false) private LocalDateTime data=LocalDateTime.now();
    @Column(nullable=false) private int quantidade;
    protected Transacao() {}
    protected Transacao(int quantidade) {
        if (quantidade<=0) throw new IllegalArgumentException("Quantidade deve ser positiva.");
        this.quantidade=quantidade;
    }
    public Long getId() { return id; }
    public LocalDateTime getData() { return data; }
    public int getQuantidade() { return quantidade; }
    public abstract String getDescricao();
    public abstract String getTipo();
}
