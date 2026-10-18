- Vencimento Segunda-feira por 23:59
- Pontos 7
Sistema de Moeda Estudantil (Release 1)
Este roteiro complementa o enunciado do Laboratório 03 e a Rubrica Lab03S03. Consulte o enunciado para os critérios completos de avaliação e prazos.
Nota de transparência sobre uso de IA: em conformidade com a política de uso responsável de Inteligência Artificial da disciplina, informa-se que este roteiro foi produzido com apoio da ferramenta Claude, da empresa Anthropic, utilizada na geração e revisão de texto. O conteúdo foi revisado pelo professor antes da disponibilização aos alunos.
Nesta sprint, o grupo corrige os apontamentos da Sprint 2, finaliza os CRUDs de aluno e empresa parceira, prepara uma apresentação da arquitetura do sistema e da camada de persistência (com slides) e conduz um tutorial de aproximadamente 20 minutos sobre as tecnologias escolhidas, no salão de tecnologias da aula. Esta é também a sprint da Apresentação Final do laboratório: o protótipo completo da Release 1 é comparado com os modelos produzidos desde a Sprint 1.
A rubrica desta sprint distribui os 7,0 pontos assim:
- Sprint 2 concluída: modelo ER, estratégia de persistência e CRUDs iniciais funcionando.
- Feedback da apresentação da Sprint 2 anotado.
A versão inicial (Sprint 2) prova que o caminho feliz funciona: cadastrar e listar um registro. A versão final acrescenta o que faz o CRUD estar pronto para uso real:
- Validações de entrada (campos obrigatórios, formatos como e-mail e CPF, regras de negócio como saldo suficiente).
- Tratamento de erros com mensagens compreensíveis, em vez de falhas silenciosas ou telas quebradas.
- Refinamento da interface (organização das telas, retorno visual após uma ação).
- Edição e remoção completas, não apenas criação e listagem.
Uma apresentação de arquitetura costuma cobrir: a visão geral do sistema, o diagrama de componentes (revisitado da Sprint 1, já com o que foi de fato implementado), a camada de persistência (o que foi escolhido e por quê) e as principais decisões técnicas tomadas ao longo do projeto.
Um tutorial curto e eficaz costuma seguir esta estrutura: o que é a tecnologia (poucos minutos), por que o grupo a escolheu, uma demonstração prática rápida (o ponto mais importante) e as dificuldades encontradas pelo grupo ao aprendê-la. Ensaiar com cronômetro ajuda a caber no tempo.
Anotem, em grupo, o que o professor apontou sobre o modelo ER, a estratégia de persistência ou os CRUDs iniciais.
bash
```bash
git add docs/diagramas/ docs/diagramas/modelo-er.*
git commit -m "fix: corrige diagramas e modelo ER conforme feedback da apresentacao"
git push origin main
```
Exemplo de validação de regra de negócio ao enviar moedas (professor precisa ter saldo suficiente):
java
```java
public EnvioMoeda enviarMoedas(Professor professor, Aluno aluno, int quantidade, String motivo) {
    if (motivo == null || motivo.isBlank()) {
        throw new IllegalArgumentException("O motivo do reconhecimento e obrigatorio.");
    }
    if (professor.getSaldo() < quantidade) {
        throw new IllegalStateException("Saldo de moedas insuficiente para o envio.");
    }
    professor.debitar(quantidade);
    aluno.creditar(quantidade);
    return new EnvioMoeda(professor, aluno, quantidade, motivo);
}
```
Repitam o padrão para as demais regras já descritas no enunciado (por exemplo, desconto do saldo do aluno ao resgatar uma vantagem).
Garantam que os erros de validação apareçam como mensagens compreensíveis para quem está usando o sistema, e não como uma tela de erro genérica ou um travamento.
Pequenos ajustes de usabilidade contam bastante: confirmação visual após salvar, mensagens de sucesso e erro claras, campos obrigatórios sinalizados.
Percorram, como se fossem um usuário real, os fluxos de criar, listar, editar e remover, tanto para aluno quanto para empresa parceira:
- Cadastro de aluno com dados válidos.
- Cadastro de aluno com dado inválido (deve ser rejeitado com mensagem clara).
- Edição de um aluno existente.
- Remoção de um aluno.
- Os mesmos quatro passos para empresa parceira.
Estrutura sugerida:
1. Visão geral do sistema (1 a 2 slides).
2. Diagrama de componentes atualizado, já refletindo o que foi implementado.
3. Camada de persistência: estratégia escolhida (ORM ou DAO) e principais entidades.
4. Decisões técnicas relevantes e o que mudou desde a modelagem inicial.
Usem a estrutura da seção 3.3 (o que é, por que escolheram, demonstração prática, dificuldades encontradas). Ensaiem para caber nos ~20 minutos previstos no enunciado.
Façam pelo menos um ensaio cronometrado de cada apresentação antes da aula.
bash
```bash
git checkout main
git pull origin main
git checkout -b feature/validacoes-finais

git add src/
git commit -m "feat: adiciona validacao de saldo no envio de moedas"

git add src/
git commit -m "feat: adiciona tratamento de erros na interface"

git add src/
git commit -m "feat: implementa edicao e remocao de aluno e empresa parceira"

git add docs/apresentacao-arquitetura.pdf
git commit -m "docs: adiciona slides da apresentacao de arquitetura"

git push origin feature/validacoes-finais
```
Confiram que o README está atualizado, os diagramas das três sprints estão corretos e o projeto compila e executa a partir de um clone limpo do repositório, antes da apresentação final.
Preparem uma breve comparação entre o que foi modelado desde a Sprint 1 e o que foi de fato implementado, destacando mudanças e o motivo de cada uma. Essa comparação faz parte da Apresentação Final exigida no enunciado.
Como nas sprints anteriores, este é o critério de maior peso da rubrica. Ele avalia o histórico do repositório GitHub do grupo, verificando se os commits foram feitos durante o horário da aula (segundas-feiras, das 10:40 às 12:20).
- Façam commits por entrega concluída (uma validação, um ajuste de interface, os slides), não um único commit no fim da aula.
- Mensagens claras, indicando o que mudou em relação à versão inicial da Sprint 2.
- git push regular ao longo da aula.
- Reservem tempo, dentro da própria aula, para consolidar e enviar o trabalho, não deixem tudo para a revisão final fora do horário de aula.
- Entregar a mesma versão da Sprint 2 sem refinamento. A rubrica desta sprint avalia explicitamente a evolução em relação à versão inicial.
- Slides de arquitetura genéricos, sem refletir o que foi de fato implementado. Prefiram mostrar o diagrama de componentes atualizado, não apenas o da Sprint 1.
- Tutorial de tecnologias muito teórico, sem demonstração prática. A parte mais valiosa para a audiência costuma ser ver a tecnologia em uso.
- Não ensaiar o tempo do tutorial. Ultrapassar muito os 20 minutos previstos prejudica a nota deste critério.
- Deixar commits importantes para depois da aula. Mesmo que o trabalho seja concluído, o volume registrado durante o horário de aula é o que conta para os 40% da nota.

