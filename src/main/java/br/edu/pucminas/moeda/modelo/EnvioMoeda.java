package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity
public class EnvioMoeda extends Transacao {
    @ManyToOne(optional=false) @JoinColumn(name="professor_id") private Professor professor;
    @ManyToOne(optional=false) @JoinColumn(name="aluno_id") private Aluno aluno;
    @Column(nullable=false, length=1000) private String motivo;
    protected EnvioMoeda() {}
    public EnvioMoeda(Professor professor, Aluno aluno, int quantidade, String motivo) {
        super(quantidade); this.professor=professor; this.aluno=aluno; this.motivo=motivo;
    }
    public Professor getProfessor() { return professor; }
    public Aluno getAluno() { return aluno; }
    public String getMotivo() { return motivo; }
    public String getTipo() { return "ENVIO"; }
    public String getDescricao() { return "Reconhecimento: "+motivo; }
}
