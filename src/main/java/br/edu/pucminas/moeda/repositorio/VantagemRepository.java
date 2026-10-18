package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.Vantagem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface VantagemRepository extends JpaRepository<Vantagem,Long> {
    List<Vantagem> findByAtivaTrueAndEmpresaAtivoTrueOrderByIdDesc();
    List<Vantagem> findByEmpresaIdAndAtivaTrueOrderByIdDesc(Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select v from Vantagem v where v.id=:id")
    Optional<Vantagem> bloquear(@Param("id") Long id);
}
