package br.edu.pucminas.moeda.controller;
import br.edu.pucminas.moeda.dto.*;
import br.edu.pucminas.moeda.modelo.*;
import br.edu.pucminas.moeda.servico.*;
import br.edu.pucminas.moeda.repositorio.InstituicaoEnsinoRepository;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.core.Authentication;
import org.springframework.dao.DataIntegrityViolationException;
import java.security.Principal;
import java.util.List;
@Controller
public class CadastroController {
    private final CadastroService cadastro;
    private final AcessoService acesso;
    private final InstituicaoEnsinoRepository instituicoes;
    public CadastroController(CadastroService cadastro,AcessoService acesso,InstituicaoEnsinoRepository instituicoes) {
        this.cadastro=cadastro; this.acesso=acesso; this.instituicoes=instituicoes;
    }
    private String formulario(Model model,Object form,boolean aluno,boolean edicao,Long id) {
        model.addAttribute("form",form); model.addAttribute("aluno",aluno); model.addAttribute("edicao",edicao);
        model.addAttribute("acao",(aluno?"/alunos":"/empresas")+(edicao?"/"+id:""));
        model.addAttribute("instituicoes",instituicoes.findAll()); return "contas/formulario";
    }
    @GetMapping("/alunos/novo") public String novoAluno(Model model) { return formulario(model,new AlunoForm(),true,false,null); }
    @GetMapping("/empresas/novo") public String novaEmpresa(Model model) { return formulario(model,new ContaForm(),false,false,null); }
    @PostMapping("/alunos") public String criarAluno(@Valid @ModelAttribute("form") AlunoForm form,BindingResult erros,Model model,RedirectAttributes flash) {
        if (!erros.hasErrors()) {
            try { cadastro.cadastrarAluno(form); flash.addFlashAttribute("sucesso","Conta criada. Entre com seu email e senha."); return "redirect:/entrar"; }
            catch (IllegalArgumentException e) { erros.reject("cadastro",e.getMessage()); }
            catch (DataIntegrityViolationException e) { erros.reject("cadastro","Email ou CPF já cadastrado."); }
        }
        return formulario(model,form,true,false,null);
    }
    @PostMapping("/empresas") public String criarEmpresa(@Valid @ModelAttribute("form") ContaForm form,BindingResult erros,Model model,RedirectAttributes flash) {
        if (!erros.hasErrors()) {
            try { cadastro.cadastrarEmpresa(form); flash.addFlashAttribute("sucesso","Conta criada. Entre com seu email e senha."); return "redirect:/entrar"; }
            catch (IllegalArgumentException e) { erros.reject("cadastro",e.getMessage()); }
            catch (DataIntegrityViolationException e) { erros.reject("cadastro","Email já cadastrado."); }
        }
        return formulario(model,form,false,false,null);
    }
    private Usuario proprio(String email,Long id,Class<? extends Usuario> tipo) {
        Usuario conta=acesso.exigir(acesso.atual(email),tipo); acesso.exigirDono(conta,id); return conta;
    }
    @GetMapping({"/alunos","/empresas"}) public String lista(Principal principal,Model model,HttpServletRequest req) {
        Class<? extends Usuario> tipo=req.getRequestURI().equals("/alunos")?Aluno.class:EmpresaParceira.class;
        Usuario conta=acesso.exigir(acesso.atual(principal.getName()),tipo);
        model.addAttribute("contas",List.of(conta)); model.addAttribute("base",tipo==Aluno.class?"/alunos":"/empresas"); return "contas/lista";
    }
    @GetMapping("/alunos/{id}") public String aluno(@PathVariable Long id,Principal principal,Model model) {
        model.addAttribute("conta",proprio(principal.getName(),id,Aluno.class)); model.addAttribute("base","/alunos"); return "contas/detalhes";
    }
    @GetMapping("/empresas/{id}") public String empresa(@PathVariable Long id,Principal principal,Model model) {
        model.addAttribute("conta",proprio(principal.getName(),id,EmpresaParceira.class)); model.addAttribute("base","/empresas"); return "contas/detalhes";
    }
    @GetMapping("/alunos/{id}/editar") public String editarAluno(@PathVariable Long id,Principal principal,Model model) {
        Aluno a=(Aluno)proprio(principal.getName(),id,Aluno.class); EdicaoAlunoForm form=new EdicaoAlunoForm();
        form.setNome(a.getNome());form.setRg(a.getRg());form.setEndereco(a.getEndereco());form.setCurso(a.getCurso());form.setInstituicaoId(a.getInstituicao().getId());
        return formulario(model,form,true,true,id);
    }
    @GetMapping("/empresas/{id}/editar") public String editarEmpresa(@PathVariable Long id,Principal principal,Model model) {
        Usuario a=proprio(principal.getName(),id,EmpresaParceira.class); EdicaoContaForm form=new EdicaoContaForm(); form.setNome(a.getNome());
        return formulario(model,form,false,true,id);
    }
    @PostMapping("/alunos/{id}") public String salvarAluno(@PathVariable Long id,@Valid @ModelAttribute("form") EdicaoAlunoForm form,BindingResult erros,Principal principal,Model model,RedirectAttributes flash) {
        proprio(principal.getName(),id,Aluno.class);
        if (!erros.hasErrors()) {
            try { cadastro.editarAluno(principal.getName(),id,form); flash.addFlashAttribute("sucesso","Cadastro atualizado."); return "redirect:/alunos/"+id; }
            catch (IllegalArgumentException e) { erros.reject("cadastro",e.getMessage()); }
        }
        return formulario(model,form,true,true,id);
    }
    @PostMapping("/empresas/{id}") public String salvarEmpresa(@PathVariable Long id,@Valid @ModelAttribute("form") EdicaoContaForm form,BindingResult erros,Principal principal,Model model,RedirectAttributes flash) {
        proprio(principal.getName(),id,EmpresaParceira.class);
        if (erros.hasErrors()) return formulario(model,form,false,true,id);
        cadastro.editarEmpresa(principal.getName(),id,form); flash.addFlashAttribute("sucesso","Cadastro atualizado."); return "redirect:/empresas/"+id;
    }
    @PostMapping({"/alunos/{id}/excluir","/empresas/{id}/excluir"})
    public String excluir(@PathVariable Long id,Authentication auth,HttpServletRequest req) {
        Class<? extends Usuario> tipo=req.getRequestURI().startsWith("/alunos")?Aluno.class:EmpresaParceira.class;
        cadastro.remover(auth.getName(),id,tipo); new SecurityContextLogoutHandler().logout(req,null,auth); return "redirect:/entrar?removida";
    }
}
