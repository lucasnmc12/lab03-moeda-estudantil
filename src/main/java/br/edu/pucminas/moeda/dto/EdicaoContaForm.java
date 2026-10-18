package br.edu.pucminas.moeda.dto;
import jakarta.validation.constraints.*;
public class EdicaoContaForm {
    @NotBlank @Size(max=120) private String nome;
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome=nome; }
}
