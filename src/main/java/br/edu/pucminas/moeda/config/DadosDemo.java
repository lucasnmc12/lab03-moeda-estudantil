package br.edu.pucminas.moeda.config;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.repositorio.*;
import org.springframework.context.annotation.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration @Profile("demo")
public class DadosDemo {
    @Bean CommandLineRunner dados(InstituicaoEnsinoRepository instituicoes, UsuarioRepository usuarios,
            ProfessorRepository professores, PasswordEncoder encoder) {
        return args -> {
            InstituicaoEnsino instituicao=instituicoes.findAll().stream().findFirst()
                .orElseGet(() -> instituicoes.save(new InstituicaoEnsino("Instituição de demonstração")));
            if (!usuarios.existsByEmail("professor@demo.local")) {
                professores.save(new Professor("Professor de demonstração","professor@demo.local",encoder.encode("Professor123!"),"52998224725","Computação",instituicao));
            }
        };
    }
}
