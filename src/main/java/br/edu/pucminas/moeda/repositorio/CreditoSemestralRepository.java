package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.CreditoSemestral;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface CreditoSemestralRepository extends JpaRepository<CreditoSemestral,Long> {
    boolean existsByProfessorIdAndSemestre(Long id, String semestre);
    List<CreditoSemestral> findByProfessorIdOrderByDataDesc(Long id);
}
