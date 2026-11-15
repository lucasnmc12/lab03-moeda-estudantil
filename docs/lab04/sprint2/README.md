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

## Verificação

- `./mvnw clean verify`: 32 testes, sem falhas, erros ou ignorados. A suíte inclui a regressão de custo zero no formulário e os testes de SMTP local da Sprint 1.
- Aplicação no perfil `demo`, com banco H2 temporário e contas fictícias: conferência via HTTP do login dos dois perfis, catálogo vazio, rejeição do custo zero com descrição preservada, cadastro por 40 moedas, entrega dos bytes do PNG e retirada da oferta.
- No catálogo do aluno com saldo zero, a oferta apresentou descrição, parceiro, foto, custo e quantas moedas faltavam. A tentativa de abrir o formulário de cadastro como aluno retornou HTTP 403.

Para reproduzir, inicie a aplicação com o perfil `demo`, crie uma conta de empresa e uma de aluno e abra o menu **Vantagens** com cada perfil. Como empresa, cadastre uma oferta com PNG ou JPEG de até 2 MB. Como aluno, confira o cartão, o saldo e o aviso de moedas faltantes. Retire a oferta com a empresa e atualize o catálogo do aluno.

O roteiro menciona 40% para o histórico, enquanto a rubrica atribui 2 de 7 pontos. Os commits desta execução foram feitos fora do horário de aula indicado no roteiro.

[Contribuição individual](../contribuicoes/lucas-nogueira/sprint2.md)
