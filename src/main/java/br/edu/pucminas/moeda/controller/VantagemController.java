package br.edu.pucminas.moeda.controller;
import br.edu.pucminas.moeda.dto.VantagemForm;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.servico.*;
import br.edu.pucminas.moeda.repositorio.VantagemRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
@Controller
public class VantagemController {
    private final VantagemService service; private final VantagemRepository vantagens; private final AcessoService acesso;
    public VantagemController(VantagemService service,VantagemRepository vantagens,AcessoService acesso) { this.service=service;this.vantagens=vantagens;this.acesso=acesso; }
    @GetMapping("/vantagens") public String lista(Principal principal,Model model) {
        Usuario usuario=acesso.atual(principal.getName());
        model.addAttribute("vantagens",usuario instanceof EmpresaParceira?vantagens.findByEmpresaIdAndAtivaTrueOrderByIdDesc(usuario.getId()):vantagens.findByAtivaTrueAndEmpresaAtivoTrueOrderByIdDesc());
        return "vantagens/lista";
    }
    @GetMapping("/vantagens/nova") public String nova(Principal principal,Model model) {
        acesso.exigir(acesso.atual(principal.getName()),EmpresaParceira.class);
        model.addAttribute("form",new VantagemForm());model.addAttribute("acao","/vantagens/nova");return "vantagens/formulario";
    }
    @GetMapping("/vantagens/{id}/editar") public String editar(@PathVariable Long id,Principal principal,Model model) {
        Vantagem vantagem=service.propria(principal.getName(),id); VantagemForm form=new VantagemForm();
        form.setDescricao(vantagem.getDescricao());form.setCusto(vantagem.getCusto());
        model.addAttribute("form",form);model.addAttribute("acao","/vantagens/"+id+"/editar");model.addAttribute("edicao",true);return "vantagens/formulario";
    }
    @PostMapping({"/vantagens/nova","/vantagens/{id}/editar"})
    public String salvar(@PathVariable(required=false) Long id,@Valid @ModelAttribute("form") VantagemForm form,BindingResult erros,Principal principal,Model model,RedirectAttributes flash) {
        acesso.exigir(acesso.atual(principal.getName()),EmpresaParceira.class);
        if (!erros.hasErrors()) {
            try { service.salvar(principal.getName(),id,form);flash.addFlashAttribute("sucesso","Vantagem salva.");return "redirect:/vantagens"; }
            catch (IllegalArgumentException e) { erros.reject("vantagem",e.getMessage()); }
        }
        model.addAttribute("acao",id==null?"/vantagens/nova":"/vantagens/"+id+"/editar");model.addAttribute("edicao",id!=null);return "vantagens/formulario";
    }
    @GetMapping("/vantagens/{id}/foto") public ResponseEntity<byte[]> foto(@PathVariable Long id) {
        Vantagem vantagem=vantagens.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(vantagem.getFotoTipo())).header("X-Content-Type-Options","nosniff").body(vantagem.getFoto());
    }
    @PostMapping("/vantagens/{id}/excluir") public String excluir(@PathVariable Long id,Principal principal,RedirectAttributes flash) {
        service.remover(principal.getName(),id);flash.addFlashAttribute("sucesso","Vantagem retirada do catálogo.");return "redirect:/vantagens";
    }
    @PostMapping("/vantagens/{id}/resgatar") public String resgatar(@PathVariable Long id,Principal principal,RedirectAttributes flash) {
        try { service.resgatar(principal.getName(),id);flash.addFlashAttribute("sucesso","Cupom emitido. Os emails estão na fila de entrega.");return "redirect:/cupons"; }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("erro",e.getMessage());return "redirect:/vantagens"; }
    }
    @GetMapping("/cupons") public String cupons(Principal principal,Model model) { model.addAttribute("cupons",service.cupons(principal.getName()));return "vantagens/cupons"; }
    @PostMapping("/cupons/{codigo}/utilizar") public String utilizar(@PathVariable String codigo,Principal principal,RedirectAttributes flash) {
        try { service.utilizar(principal.getName(),codigo);flash.addFlashAttribute("sucesso","Uso do cupom confirmado."); }
        catch (IllegalArgumentException e) { flash.addFlashAttribute("erro",e.getMessage()); }
        return "redirect:/cupons";
    }
}
