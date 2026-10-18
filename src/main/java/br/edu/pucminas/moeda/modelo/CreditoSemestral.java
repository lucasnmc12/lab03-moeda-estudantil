package br.edu.pucminas.moeda.modelo;
import jakarta.persistence.*;
@Entity @Table(uniqueConstraints=@UniqueConstraint(columnNames={"professor_id","semestre"}))
public class CreditoSemestral extends Transacao {
    @ManyToOne(optional=false) @JoinColumn(name="professor_id") private Professor professor;
    @Column(nullable=false, length=6) private String semestre;
    protected CreditoSemestral() {}
    public CreditoSemestral(Professor professor, String semestre) { super(1000); this.professor=professor; this.semestre=semestre; }
    public Professor getProfessor() { return professor; }
    public String getSemestre() { return semestre; }
    public String getTipo() { return "CREDITO"; }
    public String getDescricao() { return "Crédito do semestre "+semestre; }
}
