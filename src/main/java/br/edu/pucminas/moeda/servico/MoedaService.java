package br.edu.pucminas.moeda.servico;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.*;
@Service @Transactional
public class MoedaService {
    private final AcessoService acesso;
    private final ProfessorRepository professores;
    private final EnvioMoedaRepository envios;
    private final TrocaVantagemRepository trocas;
    private final CreditoSemestralRepository creditos;
    private final NotificacaoRepository notificacoes;
    public MoedaService(AcessoService acesso, ProfessorRepository professores,
        EnvioMoedaRepository envios, TrocaVantagemRepository trocas, CreditoSemestralRepository creditos, NotificacaoRepository notificacoes) {
        this.acesso=acesso; this.professores=professores; this.envios=envios;
        this.trocas=trocas; this.creditos=creditos; this.notificacoes=notificacoes;
    }
    public void creditarSemestre(LocalDate data) {
        String semestre=data.getYear()+"-"+(data.getMonthValue()<=6?"1":"2");
        for (Professor cadastro: professores.findByAtivoTrueOrderById()) {
            Professor professor=(Professor)acesso.bloquearId(cadastro.getId());
            if (!creditos.existsByProfessorIdAndSemestre(professor.getId(),semestre)) {
                professor.creditar(1000); creditos.save(new CreditoSemestral(professor,semestre));
            }
        }
    }
    public EnvioMoeda enviar(String email, Long alunoId, int quantidade, String motivo) {
        Professor professor=acesso.exigir(acesso.bloquear(email),Professor.class);
        if (motivo==null || motivo.isBlank() || motivo.length()>1000) throw new IllegalArgumentException("Informe o motivo do reconhecimento (até 1.000 caracteres).");
        Usuario destino=acesso.bloquearId(alunoId);
        if (!destino.isAtivo()) throw new IllegalArgumentException("Selecione um aluno ativo.");
        if (!(destino instanceof Aluno aluno)) throw new IllegalArgumentException("Selecione um aluno ativo.");
        professor.debitar(quantidade); aluno.creditar(quantidade);
        EnvioMoeda envio=envios.save(new EnvioMoeda(professor,aluno,quantidade,motivo.strip()));
        notificacoes.save(new Notificacao(envio,aluno.getEmail(),"Você recebeu moedas estudantis",
            professor.getNome()+" enviou "+quantidade+" moedas. Motivo: "+motivo.strip()));
        return envio;
    }
    @Transactional(readOnly=true)
    public List<Transacao> extrato(String email) {
        Usuario usuario=acesso.atual(email); List<Transacao> resultado=new ArrayList<>();
        if (usuario instanceof Professor) {
            resultado.addAll(envios.findByProfessorIdOrderByDataDesc(usuario.getId()));
            resultado.addAll(creditos.findByProfessorIdOrderByDataDesc(usuario.getId()));
        } else if (usuario instanceof Aluno) {
            resultado.addAll(envios.findByAlunoIdOrderByDataDesc(usuario.getId()));
            resultado.addAll(trocas.findByAlunoIdOrderByDataDesc(usuario.getId()));
        } else throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        resultado.sort(Comparator.comparing(Transacao::getData).reversed()); return resultado;
    }
}
