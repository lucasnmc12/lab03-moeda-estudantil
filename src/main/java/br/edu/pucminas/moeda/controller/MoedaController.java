package br.edu.pucminas.moeda.controller;
import br.edu.pucminas.moeda.dto.EnvioForm;
import br.edu.pucminas.moeda.servico.MoedaService;
import br.edu.pucminas.moeda.repositorio.AlunoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
@Controller
public class MoedaController {
    private final MoedaService moedas; private final AlunoRepository alunos;
    public MoedaController(MoedaService moedas,AlunoRepository alunos) { this.moedas=moedas;this.alunos=alunos; }
    @GetMapping("/extrato") public String extrato(Principal principal,Model model) {
        model.addAttribute("transacoes",moedas.extrato(principal.getName())); return "moedas/extrato";
    }
    @GetMapping("/moedas/enviar") public String enviar(Model model) {
        model.addAttribute("form",new EnvioForm()); model.addAttribute("alunos",alunos.findByAtivoTrueOrderByNome()); return "moedas/enviar";
    }
    @PostMapping("/moedas/enviar") public String reconhecer(@Valid @ModelAttribute("form") EnvioForm form,BindingResult erros,Principal principal,Model model,RedirectAttributes flash) {
        if (!erros.hasErrors()) {
            try { moedas.enviar(principal.getName(),form.getAlunoId(),form.getQuantidade(),form.getMotivo()); flash.addFlashAttribute("sucesso","Reconhecimento enviado. Os emails de confirmação e recebimento estão na fila de entrega."); return "redirect:/extrato"; }
            catch (IllegalArgumentException e) { erros.reject("envio",e.getMessage()); }
        }
        model.addAttribute("alunos",alunos.findByAtivoTrueOrderByNome()); return "moedas/enviar";
    }
}
