package br.edu.pucminas.moeda.dto;
import jakarta.validation.constraints.*;
public class EnvioForm {
    @NotNull private Long alunoId;
    @NotNull @Positive private Integer quantidade;
    @NotBlank @Size(max=1000) private String motivo;
    public Long getAlunoId() { return alunoId; }
    public void setAlunoId(Long id) { alunoId=id; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer q) { quantidade=q; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String m) { motivo=m; }
}
