package br.edu.pucminas.moeda.dto;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.br.CPF;
public class AlunoForm extends ContaForm {
    @NotBlank @CPF private String cpf;
    @NotBlank @Size(max=30) private String rg;
    @NotBlank @Size(max=250) private String endereco;
    @NotBlank @Size(max=120) private String curso;
    @NotNull private Long instituicaoId;
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf=cpf; }
    public String getRg() { return rg; }
    public void setRg(String rg) { this.rg=rg; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco=endereco; }
    public String getCurso() { return curso; }
    public void setCurso(String curso) { this.curso=curso; }
    public Long getInstituicaoId() { return instituicaoId; }
    public void setInstituicaoId(Long id) { this.instituicaoId=id; }
}
