package br.edu.pucminas.moeda.repositorio;
import br.edu.pucminas.moeda.modelo.Usuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query(value="select id from usuario where email=:email for update", nativeQuery=true)
    Optional<Long> travarPorEmail(@Param("email") String email);
    @Query(value="select id from usuario where id=:id for update", nativeQuery=true)
    Optional<Long> travarPorId(@Param("id") Long id);
}
