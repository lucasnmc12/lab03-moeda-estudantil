package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.Notificacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface NotificacaoRepository extends JpaRepository<Notificacao,Long> {
    List<Notificacao> findTop20ByEnviadaEmIsNullOrderById();
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select n from Notificacao n where n.id=:id")
    Optional<Notificacao> bloquear(@Param("id") Long id);
}
