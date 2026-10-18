package br.edu.pucminas.moeda.dto;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
public class VantagemForm {
    @NotBlank @Size(max=500) private String descricao;
    @NotNull @Positive private Integer custo;
    private MultipartFile foto;
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao=descricao; }
    public Integer getCusto() { return custo; }
    public void setCusto(Integer custo) { this.custo=custo; }
    public MultipartFile getFoto() { return foto; }
    public void setFoto(MultipartFile foto) { this.foto=foto; }
}
