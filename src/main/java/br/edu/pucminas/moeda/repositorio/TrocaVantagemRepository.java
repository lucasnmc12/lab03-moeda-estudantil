package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.TrocaVantagem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface TrocaVantagemRepository extends JpaRepository<TrocaVantagem,Long> {
    List<TrocaVantagem> findByAlunoIdOrderByDataDesc(Long id);
    List<TrocaVantagem> findByVantagemEmpresaIdOrderByDataDesc(Long id);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select t from TrocaVantagem t where t.codigo=:codigo")
    Optional<TrocaVantagem> bloquearPorCodigo(@Param("codigo") String codigo);
}
