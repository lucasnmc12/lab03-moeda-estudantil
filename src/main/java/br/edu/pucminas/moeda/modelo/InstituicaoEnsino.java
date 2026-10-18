package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity
public class InstituicaoEnsino {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=150) private String nome;
    protected InstituicaoEnsino() {}
    public InstituicaoEnsino(String nome) { this.nome=nome; }
    public Long getId() { return id; }
    public String getNome() { return nome; }
}
