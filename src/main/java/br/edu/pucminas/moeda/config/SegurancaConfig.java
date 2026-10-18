package br.edu.pucminas.moeda.config;
import br.edu.pucminas.moeda.repositorio.UsuarioRepository;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SegurancaConfig {
    @Bean SecurityFilterChain security(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/entrar", "/css/**", "/error").permitAll()
            .requestMatchers(HttpMethod.GET,"/alunos/novo","/empresas/novo").permitAll()
            .requestMatchers(HttpMethod.POST,"/alunos","/empresas").permitAll()
            .requestMatchers("/moedas/**").hasRole("PROFESSOR")
            .requestMatchers("/extrato").hasAnyRole("ALUNO","PROFESSOR")
            .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/entrar").defaultSuccessUrl("/painel",true).failureUrl("/entrar?erro").permitAll())
            .logout(logout -> logout.logoutUrl("/sair").logoutSuccessUrl("/entrar?saiu"))
            .build();
    }
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean UserDetailsService userDetailsService(UsuarioRepository usuarios) {
        return email -> usuarios.findByEmail(email.strip().toLowerCase(java.util.Locale.ROOT))
            .map(u -> User.withUsername(u.getEmail()).password(u.getSenhaHash()).roles(u.getPerfil()).disabled(!u.isAtivo()).build())
            .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
    }
}
