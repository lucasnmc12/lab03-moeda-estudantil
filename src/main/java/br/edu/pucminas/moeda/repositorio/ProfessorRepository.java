package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.Professor;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface ProfessorRepository extends JpaRepository<Professor,Long> {
    List<Professor> findByAtivoTrueOrderById();
}
