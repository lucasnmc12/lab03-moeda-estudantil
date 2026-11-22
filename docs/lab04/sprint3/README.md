# Lab04S03 — Troca de vantagens e apresentação final

Continuação da `main` após o PR #2, base `b6e25e9`. A branch da entrega é `lab04-sprint3`.

## Critérios e verificações previstas

| Critério | Implementação | Verificação |
| --- | --- | --- |
| Sequência geral (1,0) | [PlantUML](diagramas/sequencia-geral.puml) e [SVG](diagramas/sequencia-geral.svg) | Sintaxe PlantUML e comparação com os métodos reais |
| Troca completa (2,0) | Reutilizar resgate transacional, controller, cupom e SMTP; acrescentar dois templates específicos | MVC, saldo insuficiente, rollback e SMTP local com o mesmo código |
| Apresentação final (2,0) | Comparar casos de uso, classes e componentes iniciais com a Release 2 | Slides e roteiro reproduzível; ensaio e apresentação humana pendentes |
| Histórico (2,0) | Commits por responsabilidade com autoria `lucasnmc12` | Intervalos reais de pelo menos 10 minutos e push após cada entrega |

O diagrama geral apresenta o encadeamento dos fluxos. As validações detalhadas continuam nos [quatro diagramas da Sprint 2](../sprint2/README.md). O resgate, as regras de saldo, o código único e a confirmação pelo parceiro já existem desde o Lab03 e serão reaproveitados.

Para regenerar a imagem, na raiz do repositório:

```sh
plantuml -charset UTF-8 -tsvg -nometadata docs/lab04/sprint3/diagramas/sequencia-geral.puml
```

O roteiro anuncia 40% para commits, mas sua tabela atribui 2 de 7 pontos. Esta execução começa em 22/11/2026, domingo, fora da aula indicada. Os horários reais não comprovam participação em aula. Não houve novas notas ou correções detalhadas fornecidas pelo professor.
