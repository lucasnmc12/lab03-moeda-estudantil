package br.edu.pucminas.moeda;
import br.edu.pucminas.moeda.servico.*;
import org.springframework.beans.factory.annotation.Autowired;
abstract class BaseIntegracao extends BaseCadastro {
    @Autowired MoedaService moedas;
}
