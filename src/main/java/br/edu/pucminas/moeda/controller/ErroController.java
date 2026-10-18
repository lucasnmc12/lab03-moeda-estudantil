package br.edu.pucminas.moeda.controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpServletRequest;
@Controller
public class ErroController implements ErrorController {
    @RequestMapping("/error") public String erro(HttpServletRequest req,Model model) {
        Object codigo=req.getAttribute("jakarta.servlet.error.status_code");
        model.addAttribute("codigo",codigo==null?500:codigo);
        model.addAttribute("mensagem",Integer.valueOf(404).equals(codigo)?"Não encontramos este registro.":Integer.valueOf(403).equals(codigo)?"Sua conta não tem acesso a esta ação.":"Não foi possível concluir a ação. Confira os dados e tente novamente.");
        return "erro";
    }
}
