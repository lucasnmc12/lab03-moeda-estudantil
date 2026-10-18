package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.Entity;
@Entity
public class EmpresaParceira extends Usuario {
    protected EmpresaParceira() {}
    public EmpresaParceira(String nome, String email, String senhaHash) { super(nome,email,senhaHash); }
    public String getPerfil() { return "EMPRESA"; }
}
