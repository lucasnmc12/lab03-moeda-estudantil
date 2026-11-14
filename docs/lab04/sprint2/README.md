# Lab04S02 — Vantagens e diagramas de sequência

Esta sprint continua a Release 2 no mesmo repositório do Lab03. O cadastro e a listagem de vantagens já existiam na base. A revisão desta sprint melhora o retorno de validação no formulário e ajuda o aluno a comparar o custo das vantagens com seu saldo.

| Caso de uso | Diagrama | Fonte |
| --- | --- | --- |
| Enviar moedas | [SVG](diagramas/enviar-moedas.svg) | [PlantUML](diagramas/enviar-moedas.puml) |
| Consultar extrato | [SVG](diagramas/consultar-extrato.svg) | [PlantUML](diagramas/consultar-extrato.puml) |
| Cadastrar vantagem | [SVG](diagramas/cadastrar-vantagem.svg) | [PlantUML](diagramas/cadastrar-vantagem.puml) |
| Listar vantagens | [SVG](diagramas/listar-vantagens.svg) | [PlantUML](diagramas/listar-vantagens.puml) |

Os diagramas representam as rotas, métodos e fluxos alternativos da aplicação. O diagrama geral é entregável da Sprint 3.

Para regenerar os arquivos na raiz do repositório:

```sh
plantuml -charset UTF-8 -tsvg -nometadata docs/lab04/sprint2/diagramas/*.puml
```
