package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity @Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, length=120) private String nome;
    @Column(nullable=false, unique=true, length=180) private String email;
    @Column(nullable=false, length=100) private String senhaHash;
    @Column(nullable=false) private boolean ativo = true;
    protected Usuario() {}
    protected Usuario(String nome, String email, String senhaHash) {
        this.nome=nome; this.email=email; this.senhaHash=senhaHash;
    }
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getSenhaHash() { return senhaHash; }
    public boolean isAtivo() { return ativo; }
    public void atualizar(String nome) { this.nome=nome; }
    public void desativar() { ativo=false; }
    public abstract String getPerfil();
}
