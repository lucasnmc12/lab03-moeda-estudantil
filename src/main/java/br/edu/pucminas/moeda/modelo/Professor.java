package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity
public class Professor extends Usuario {
    @Column(nullable=false, length=11, unique=true) private String cpf;
    @Column(nullable=false, length=120) private String departamento;
    @ManyToOne(optional=false) @JoinColumn(name="instituicao_id") private InstituicaoEnsino instituicao;
    @Column(nullable=false) private int saldo;

    protected Professor() {}
    public Professor(String nome, String email, String senhaHash, String cpf, String departamento, InstituicaoEnsino instituicao) {
        super(nome,email,senhaHash); this.cpf=cpf; this.departamento=departamento; this.instituicao=instituicao;
    }
    public String getCpf() { return cpf; }
    public String getDepartamento() { return departamento; }
    public InstituicaoEnsino getInstituicao() { return instituicao; }
    public int getSaldo() { return saldo; }
    public String getPerfil() { return "PROFESSOR"; }
    public void creditar(int quantidade) {
        if (quantidade<=0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        saldo=Math.addExact(saldo,quantidade);
    }
    public void debitar(int quantidade) {
        if (quantidade<=0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        if (saldo<quantidade) throw new IllegalArgumentException("Saldo de moedas insuficiente.");
        saldo-=quantidade;
    }
    
}
