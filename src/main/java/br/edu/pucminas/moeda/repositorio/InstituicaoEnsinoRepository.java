package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.InstituicaoEnsino;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface InstituicaoEnsinoRepository extends JpaRepository<InstituicaoEnsino,Long> {
    
}
