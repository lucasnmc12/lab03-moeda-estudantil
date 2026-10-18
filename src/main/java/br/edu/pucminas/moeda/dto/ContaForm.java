package br.edu.pucminas.moeda.dto;
import jakarta.validation.constraints.*;
public class ContaForm {
    @NotBlank @Size(max=120) private String nome;
    @NotBlank @Email @Size(max=180) private String email;
    @NotBlank @Size(min=8,max=60) private String senha;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome=nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha=senha; }
}
