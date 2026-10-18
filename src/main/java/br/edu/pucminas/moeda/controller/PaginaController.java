package br.edu.pucminas.moeda.controller;
import br.edu.pucminas.moeda.servico.AcessoService;
import br.edu.pucminas.moeda.modelo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.security.Principal;
@Controller
public class PaginaController {
    private final AcessoService acesso;
    public PaginaController(AcessoService acesso) { this.acesso=acesso; }
    @GetMapping("/") public String inicio(Principal principal) { return principal==null?"inicio":"redirect:/painel"; }
    @GetMapping("/entrar") public String entrar() { return "entrar"; }
    @GetMapping("/painel") public String painel() { return "painel"; }
}
