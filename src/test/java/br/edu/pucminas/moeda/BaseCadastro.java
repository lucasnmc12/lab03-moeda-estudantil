package br.edu.pucminas.moeda;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.repositorio.*;
import br.edu.pucminas.moeda.servico.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
@SpringBootTest @AutoConfigureMockMvc @ActiveProfiles("test")
abstract class BaseCadastro {
    @Autowired MockMvc mvc;
    @Autowired JdbcTemplate jdbc;
    @Autowired UsuarioRepository usuarios;
    @Autowired AlunoRepository alunos;
    @Autowired EmpresaParceiraRepository empresas;
    @Autowired ProfessorRepository professores;
    @Autowired InstituicaoEnsinoRepository instituicoes;
    @Autowired VantagemRepository vantagens;
    @Autowired TrocaVantagemRepository trocas;
    @Autowired NotificacaoRepository notificacoes;
    @Autowired EnvioMoedaRepository envios;
    @Autowired CreditoSemestralRepository creditos;
    @Autowired PasswordEncoder encoder;
    @Autowired CadastroService cadastro;
    @Autowired AcessoService acesso;
    Aluno ana,bruno; Professor professor; EmpresaParceira parceiro,outroParceiro; InstituicaoEnsino instituicao;
    @BeforeEach void preparar() {
        for (String tabela:new String[]{"notificacao","credito_semestral","troca_vantagem","envio_moeda","transacao","vantagem","aluno","professor","empresa_parceira","usuario","instituicao_ensino"}) jdbc.update("DELETE FROM "+tabela);
        instituicao=instituicoes.save(new InstituicaoEnsino("Universidade Teste"));
        String hash=encoder.encode("Senha123!");
        ana=alunos.save(new Aluno("Ana","ana@teste.local",hash,"52998224725","RG-1","Rua A, 1","Computação",instituicao));
        bruno=alunos.save(new Aluno("Bruno","bruno@teste.local",hash,"11144477735","RG-2","Rua B, 2","Engenharia",instituicao));
        professor=professores.save(new Professor("Professor","prof@teste.local",hash,"12345678909","Computação",instituicao));
        parceiro=empresas.save(new EmpresaParceira("Livraria","loja@teste.local",hash));
        outroParceiro=empresas.save(new EmpresaParceira("Café","cafe@teste.local",hash));
    }
    Vantagem oferta(int custo) {
        return vantagens.save(new Vantagem(parceiro,"Desconto em livro",custo,new byte[]{1,2,3},"image/png"));
    }
    void saldoAluno(int saldo) { jdbc.update("UPDATE aluno SET saldo=? WHERE id=?",saldo,ana.getId()); }
    void saldoProfessor(int saldo) { jdbc.update("UPDATE professor SET saldo=? WHERE id=?",saldo,professor.getId()); }
}
