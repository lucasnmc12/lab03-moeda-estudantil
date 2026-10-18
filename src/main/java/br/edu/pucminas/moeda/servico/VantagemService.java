package br.edu.pucminas.moeda.servico;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.repositorio.*;
import br.edu.pucminas.moeda.dto.VantagemForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
@Service @Transactional
public class VantagemService {
    private final AcessoService acesso;
    private final VantagemRepository vantagens;
    private final TrocaVantagemRepository trocas;
    private final NotificacaoRepository notificacoes;
    public VantagemService(AcessoService acesso, VantagemRepository vantagens, TrocaVantagemRepository trocas, NotificacaoRepository notificacoes) {
        this.acesso=acesso; this.vantagens=vantagens; this.trocas=trocas; this.notificacoes=notificacoes;
    }
    public Vantagem salvar(String email, Long id, VantagemForm form) {
        EmpresaParceira empresa=acesso.exigir(acesso.bloquear(email),EmpresaParceira.class);
        if (form.getDescricao()==null || form.getDescricao().isBlank() || form.getDescricao().length()>500 || form.getCusto()==null || form.getCusto()<=0)
            throw new IllegalArgumentException("Informe descrição (até 500 caracteres) e custo positivo.");
        Vantagem atual=id==null?null:propriaBloqueada(empresa,id);
        MultipartFile arquivo=form.getFoto();
        byte[] foto=atual==null?null:atual.getFoto(); String tipo=atual==null?null:atual.getFotoTipo();
        if (arquivo!=null && !arquivo.isEmpty()) {
            if (arquivo.getSize()>2*1024*1024) throw new IllegalArgumentException("A foto deve ter até 2 MB.");
            try {
                foto=arquivo.getBytes();
                boolean png=foto.length>8 && foto[0]==(byte)137 && foto[1]==80 && foto[2]==78 && foto[3]==71;
                boolean jpeg=foto.length>3 && foto[0]==(byte)255 && foto[1]==(byte)216 && foto[2]==(byte)255;
                if ((!png && !jpeg) || ImageIO.read(new ByteArrayInputStream(foto))==null) throw new IllegalArgumentException("Envie uma foto PNG ou JPEG válida.");
                tipo=png?"image/png":"image/jpeg";
            } catch (IOException exception) { throw new IllegalArgumentException("Não foi possível ler a foto."); }
        }
        if (foto==null) throw new IllegalArgumentException("Adicione uma foto PNG ou JPEG.");
        if (atual==null) return vantagens.save(new Vantagem(empresa,form.getDescricao().strip(),form.getCusto(),foto,tipo));
        atual.atualizar(form.getDescricao().strip(),form.getCusto(),foto,tipo); return atual;
    }
    private Vantagem propriaBloqueada(EmpresaParceira empresa, Long id) {
        Vantagem vantagem=vantagens.bloquear(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!vantagem.getEmpresa().getId().equals(empresa.getId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return vantagem;
    }
    @Transactional(readOnly=true)
    public Vantagem propria(String email, Long id) {
        EmpresaParceira empresa=acesso.exigir(acesso.atual(email),EmpresaParceira.class);
        Vantagem vantagem=vantagens.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!vantagem.getEmpresa().getId().equals(empresa.getId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return vantagem;
    }
    public void remover(String email, Long id) {
        EmpresaParceira empresa=acesso.exigir(acesso.bloquear(email),EmpresaParceira.class);
        propriaBloqueada(empresa,id).desativar();
    }
    public TrocaVantagem resgatar(String email, Long id) {
        Aluno aluno=acesso.exigir(acesso.bloquear(email),Aluno.class);
        Vantagem vantagem=vantagens.bloquear(id).filter(v -> v.isAtiva() && v.getEmpresa().isAtivo())
            .orElseThrow(() -> new IllegalArgumentException("Esta vantagem não está mais disponível."));
        aluno.debitar(vantagem.getCusto());
        TrocaVantagem troca=trocas.save(new TrocaVantagem(aluno,vantagem));
        String texto="Cupom: "+troca.getCodigo()+"\nVantagem: "+troca.getDescricaoVantagem()+"\nCusto: "+troca.getQuantidade()+" moedas. Apresente o código na troca presencial.";
        notificacoes.save(new Notificacao(troca,aluno.getEmail(),"Seu cupom de vantagem",texto));
        notificacoes.save(new Notificacao(troca,vantagem.getEmpresa().getEmail(),"Cupom emitido para conferência",texto));
        return troca;
    }
    public void utilizar(String email, String codigo) {
        EmpresaParceira empresa=acesso.exigir(acesso.bloquear(email),EmpresaParceira.class);
        TrocaVantagem troca=trocas.bloquearPorCodigo(codigo).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!troca.getVantagem().getEmpresa().getId().equals(empresa.getId())) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        troca.confirmarUso();
    }
    @Transactional(readOnly=true)
    public List<TrocaVantagem> cupons(String email) {
        Usuario usuario=acesso.atual(email);
        if (usuario instanceof Aluno) return trocas.findByAlunoIdOrderByDataDesc(usuario.getId());
        acesso.exigir(usuario,EmpresaParceira.class); return trocas.findByVantagemEmpresaIdOrderByDataDesc(usuario.getId());
    }
}
