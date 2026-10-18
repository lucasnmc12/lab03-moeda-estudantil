package br.edu.pucminas.moeda.servico;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.dto.*;
import br.edu.pucminas.moeda.repositorio.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service @Transactional
public class CadastroService {
    private final UsuarioRepository usuarios;
    private final AlunoRepository alunos;
    private final EmpresaParceiraRepository empresas;
    private final InstituicaoEnsinoRepository instituicoes;
    private final PasswordEncoder encoder;
    private final AcessoService acesso;
    public CadastroService(UsuarioRepository usuarios, AlunoRepository alunos, EmpresaParceiraRepository empresas,
            InstituicaoEnsinoRepository instituicoes, PasswordEncoder encoder, AcessoService acesso) {
        this.usuarios=usuarios; this.alunos=alunos; this.empresas=empresas;
        this.instituicoes=instituicoes; this.encoder=encoder; this.acesso=acesso;
    }
    public Aluno cadastrarAluno(AlunoForm form) {
        String email=validarEmail(form.getEmail());
        String cpf=form.getCpf().replaceAll("\\D", "");
        if (alunos.existsByCpf(cpf)) throw new IllegalArgumentException("Já existe um aluno com este CPF.");
        return alunos.save(new Aluno(form.getNome().strip(),email,encoder.encode(form.getSenha()),cpf,
            form.getRg().strip(),form.getEndereco().strip(),form.getCurso().strip(),instituicao(form.getInstituicaoId())));
    }
    public EmpresaParceira cadastrarEmpresa(ContaForm form) {
        String email=validarEmail(form.getEmail());
        return empresas.save(new EmpresaParceira(form.getNome().strip(),email,encoder.encode(form.getSenha())));
    }
    private String validarEmail(String valor) {
        String email=valor.strip().toLowerCase(java.util.Locale.ROOT);
        if (usuarios.existsByEmail(email)) throw new IllegalArgumentException("Já existe uma conta com este email.");
        return email;
    }
    public InstituicaoEnsino instituicao(Long id) {
        return instituicoes.findById(id).orElseThrow(() -> new IllegalArgumentException("Selecione uma instituição participante."));
    }
    public Aluno editarAluno(String email, Long id, EdicaoAlunoForm form) {
        Aluno aluno=acesso.exigir(acesso.bloquear(email),Aluno.class); acesso.exigirDono(aluno,id);
        aluno.atualizarDados(form.getNome().strip(),form.getRg().strip(),form.getEndereco().strip(),form.getCurso().strip(),instituicao(form.getInstituicaoId()));
        return aluno;
    }
    public EmpresaParceira editarEmpresa(String email, Long id, EdicaoContaForm form) {
        EmpresaParceira empresa=acesso.exigir(acesso.bloquear(email),EmpresaParceira.class); acesso.exigirDono(empresa,id);
        empresa.atualizar(form.getNome().strip()); return empresa;
    }
    public void remover(String email, Long id, Class<? extends Usuario> tipo) {
        Usuario usuario=acesso.exigir(acesso.bloquear(email),tipo); acesso.exigirDono(usuario,id); usuario.desativar();
    }
}
