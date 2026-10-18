package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.Aluno;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface AlunoRepository extends JpaRepository<Aluno,Long> {
    boolean existsByCpf(String cpf);
    List<Aluno> findByAtivoTrueOrderByNome();
}
