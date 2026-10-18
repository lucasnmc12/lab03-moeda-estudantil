package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity
public class Vantagem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="empresa_id") private EmpresaParceira empresa;
    @Column(nullable=false, length=500) private String descricao;
    @Column(nullable=false) private int custo;
    @Lob @Column(nullable=false) private byte[] foto;
    @Column(nullable=false, length=30) private String fotoTipo;
    @Column(nullable=false) private boolean ativa=true;
    protected Vantagem() {}
    public Vantagem(EmpresaParceira empresa, String descricao, int custo, byte[] foto, String fotoTipo) {
        this.empresa=empresa; atualizar(descricao,custo,foto,fotoTipo);
    }
    public void atualizar(String descricao, int custo, byte[] foto, String fotoTipo) {
        if (descricao==null || descricao.isBlank() || custo<=0) throw new IllegalArgumentException("Informe descrição e custo positivo.");
        this.descricao=descricao; this.custo=custo; this.foto=foto; this.fotoTipo=fotoTipo;
    }
    public void desativar() { ativa=false; }
    public Long getId() { return id; }
    public EmpresaParceira getEmpresa() { return empresa; }
    public String getDescricao() { return descricao; }
    public int getCusto() { return custo; }
    public byte[] getFoto() { return foto; }
    public String getFotoTipo() { return fotoTipo; }
    public boolean isAtiva() { return ativa; }
}
