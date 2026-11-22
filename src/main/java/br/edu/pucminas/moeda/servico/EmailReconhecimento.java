package br.edu.pucminas.moeda.servico;

import br.edu.pucminas.moeda.modelo.EnvioMoeda;
import br.edu.pucminas.moeda.modelo.TrocaVantagem;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

@Component
public class EmailReconhecimento {
    private final SpringTemplateEngine templates;

    public EmailReconhecimento() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/emails/");
        resolver.setSuffix(".txt");
        resolver.setTemplateMode(TemplateMode.TEXT);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        templates = new SpringTemplateEngine();
        templates.setTemplateResolver(resolver);
    }

    public String confirmacaoProfessor(EnvioMoeda envio) {
        return renderizar("envio-professor", envio);
    }

    public String recebimentoAluno(EnvioMoeda envio) {
        return renderizar("recebimento-aluno", envio);
    }

    public String cupomAluno(TrocaVantagem troca) {
        return renderizarTroca("cupom-aluno", troca);
    }

    public String confirmacaoEmpresa(TrocaVantagem troca) {
        return renderizarTroca("confirmacao-empresa", troca);
    }

    private String renderizarTroca(String template, TrocaVantagem troca) {
        var contexto = new Context(Locale.forLanguageTag("pt-BR"), Map.of(
            "aluno", troca.getAluno().getNome(),
            "empresa", troca.getVantagem().getEmpresa().getNome(),
            "codigo", troca.getCodigo(),
            "descricao", troca.getDescricaoVantagem(),
            "quantidade", troca.getQuantidade()));
        return templates.process(template, contexto);
    }

    private String renderizar(String template, EnvioMoeda envio) {
        var contexto = new Context(Locale.forLanguageTag("pt-BR"), Map.of(
            "professor", envio.getProfessor().getNome(),
            "aluno", envio.getAluno().getNome(),
            "quantidade", envio.getQuantidade(),
            "motivo", envio.getMotivo()));
        return templates.process(template, contexto);
    }
}
