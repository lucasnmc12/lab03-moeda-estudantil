package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.EnvioMoeda;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface EnvioMoedaRepository extends JpaRepository<EnvioMoeda,Long> {
    List<EnvioMoeda> findByProfessorIdOrderByDataDesc(Long id);
    List<EnvioMoeda> findByAlunoIdOrderByDataDesc(Long id);
}
