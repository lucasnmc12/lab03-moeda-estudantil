package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.EmpresaParceira;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira,Long> {
    
}
