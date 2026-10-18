package br.edu.pucminas.moeda.servico;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.repositorio.UsuarioRepository;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
@Service
public class AcessoService {
    private final UsuarioRepository usuarios;
    private final EntityManager entityManager;
    public AcessoService(UsuarioRepository usuarios, EntityManager entityManager) { this.usuarios=usuarios; this.entityManager=entityManager; }
    public Usuario atual(String email) {
        return usuarios.findByEmail(email).filter(Usuario::isAtivo)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
    public Usuario bloquear(String email) {
        Long id=usuarios.travarPorEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        Usuario usuario=carregarAtualizado(id);
        if (!usuario.isAtivo()) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return usuario;
    }
    public Usuario bloquearId(Long id) {
        usuarios.travarPorId(id).orElseThrow(() -> new IllegalArgumentException("Selecione um usuário existente."));
        return carregarAtualizado(id);
    }
    private Usuario carregarAtualizado(Long id) {
        // Em JOINED, o lock deve anteceder a leitura das tabelas de saldo.
        Usuario usuario=usuarios.findById(id).orElseThrow();
        entityManager.refresh(usuario);
        return usuario;
    }
    public <T extends Usuario> T exigir(Usuario usuario, Class<T> tipo) {
        if (!tipo.isInstance(usuario)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        return tipo.cast(usuario);
    }
    public void exigirDono(Usuario usuario, Long id) {
        if (!usuario.getId().equals(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
