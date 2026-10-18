package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity
public class Aluno extends Usuario {
    @Column(nullable=false, length=11, unique=true) private String cpf;
    @Column(nullable=false, length=30) private String rg;
    @Column(nullable=false, length=250) private String endereco;
    @Column(nullable=false, length=120) private String curso;
    @ManyToOne(optional=false) @JoinColumn(name="instituicao_id") private InstituicaoEnsino instituicao;
    @Column(nullable=false) private int saldo;

    protected Aluno() {}
    public Aluno(String nome, String email, String senhaHash, String cpf, String rg, String endereco, String curso, InstituicaoEnsino instituicao) {
        super(nome,email,senhaHash); this.cpf=cpf; this.rg=rg; this.endereco=endereco; this.curso=curso; this.instituicao=instituicao;
    }
    public String getCpf() { return cpf; }
    public String getRg() { return rg; }
    public String getEndereco() { return endereco; }
    public String getCurso() { return curso; }
    public InstituicaoEnsino getInstituicao() { return instituicao; }
    public int getSaldo() { return saldo; }
    public String getPerfil() { return "ALUNO"; }
    public void creditar(int quantidade) {
        if (quantidade<=0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        saldo=Math.addExact(saldo,quantidade);
    }
    public void debitar(int quantidade) {
        if (quantidade<=0) throw new IllegalArgumentException("A quantidade deve ser positiva.");
        if (saldo<quantidade) throw new IllegalArgumentException("Saldo de moedas insuficiente.");
        saldo-=quantidade;
    }
    public void atualizarDados(String nome, String rg, String endereco, String curso, InstituicaoEnsino instituicao) {
        atualizar(nome); this.rg=rg; this.endereco=endereco; this.curso=curso; this.instituicao=instituicao;
    }
}
