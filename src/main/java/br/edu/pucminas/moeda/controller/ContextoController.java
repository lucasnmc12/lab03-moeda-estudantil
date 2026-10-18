package br.edu.pucminas.moeda.controller;
import br.edu.pucminas.moeda.servico.AcessoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.security.Principal;
@ControllerAdvice
public class ContextoController {
    private final AcessoService acesso;
    public ContextoController(AcessoService acesso) { this.acesso=acesso; }
    @ModelAttribute public void usuario(Principal principal, Model model) {
        if (principal!=null) model.addAttribute("usuario",acesso.atual(principal.getName()));
    }
}
