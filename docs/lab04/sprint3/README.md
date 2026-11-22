# Lab04S03 — Troca de vantagens e apresentação final

Continuação da `main` após o PR #2, base `b6e25e9`. A branch da entrega é `lab04-sprint3`.

## Critérios e evidências

| Critério | Implementação | Verificação |
| --- | --- | --- |
| Sequência geral (1,0) | [PlantUML](diagramas/sequencia-geral.puml) e [SVG](diagramas/sequencia-geral.svg) | Sintaxe PlantUML e comparação com os métodos reais |
| Troca completa (2,0) | Resgate transacional, controller, cupom e SMTP reutilizados; dois templates específicos acrescentados | 36 testes passaram; HTTP real com saldo insuficiente e SMTP local com o mesmo código |
| Apresentação final (2,0) | Dez slides e roteiro com comparação de casos de uso, classes e componentes iniciais com a Release 2 | Slides renderizados e revisados; ensaio e apresentação humana pendentes |
| Histórico (2,0) | Commits por responsabilidade com autoria `lucasnmc12` | Intervalos reais de pelo menos 10 minutos e push após cada entrega |

O diagrama geral apresenta o encadeamento dos fluxos. As validações detalhadas continuam nos [quatro diagramas da Sprint 2](../sprint2/README.md), complementados pela [sequência específica de resgate](diagramas/trocar-vantagem.svg), com [fonte PlantUML](diagramas/trocar-vantagem.puml). O resgate, as regras de saldo, o código único e a confirmação pelo parceiro já existem desde o Lab03 e foram reaproveitados.

Para regenerar a imagem, na raiz do repositório:

```sh
plantuml -charset UTF-8 -tsvg -nometadata docs/lab04/sprint3/diagramas/sequencia-geral.puml
```

O roteiro anuncia 40% para commits, mas sua tabela atribui 2 de 7 pontos. Esta execução começa em 22/11/2026, domingo, fora da aula indicada. Os horários reais não comprovam participação em aula. Não houve novas notas ou correções detalhadas fornecidas pelo professor.

## Entrega funcional

`VantagemService.resgatar` mantém o bloqueio, a validação de disponibilidade, o débito e a persistência atômica do cupom e das duas notificações. O controller e a tela de cupons existentes continuam o fluxo. `EmailReconhecimento` reaproveita o motor de templates da Sprint 1 com os novos métodos `cupomAluno` e `confirmacaoEmpresa`. Não foi criada uma classe paralela chamada `EmailService` só para reproduzir o nome ilustrativo do roteiro.

- `templates/emails/cupom-aluno.txt` explica o débito e a apresentação presencial.
- `templates/emails/confirmacao-empresa.txt` orienta a conferência e a confirmação única.
- Os dois leem `TrocaVantagem.codigo`, gerado uma única vez na entidade.
- `TrocaAtomicidadeTest` força falha no segundo template e confere rollback de débito, cupom e primeira mensagem.
- `TrocaSprint3MvcTest` percorre login, envio, catálogo, resgate, extrato e confirmação; confere saldo insuficiente e conteúdo literal dos templates.
- `NotificacaoSmtpTest` confere destinatários, conteúdo distinto e código idêntico nas mensagens realmente recebidas pelo servidor SMTP local.

O [diagrama de componentes atual](../../diagramas/diagrama-de-componentes.puml) foi alinhado ao caminho real de preparação/persistência/entrega dos emails. Os [modelos iniciais](../../diagramas/versoes/sprint1/) foram preservados.

## Verificações executadas

- `./mvnw clean verify` — **36 testes**, zero falhas, erros ou ignorados. [Resumo por suíte](evidencias/testes.json).
- PlantUML validou o diagrama geral e gerou SVG e PNG; a imagem foi inspecionada. O diagrama de componentes foi regenerado.
- Verificação HTTP em aplicação real com H2 temporário, login por sessão e PNG válido: empresa cadastra oferta de 40, aluno sem saldo é rejeitado, professor envia 120, aluno resgata e termina com 80; parceiro confirma o cupom uma única vez. SMTP local capturou quatro mensagens, duas contendo o código do cupom. [Resultado](evidencias/fluxo-http.json).
- O primeiro rascunho do script HTTP tinha uma expectativa textual incorreta na confirmação de envio; ela foi corrigida para conferir a saída −120 no extrato. Nenhuma mudança funcional foi necessária por essa falha do script.
- [Verificador reproduzível](evidencias/verificar-fluxo-http.py), após `./mvnw package`: `python3 docs/lab04/sprint3/evidencias/verificar-fluxo-http.py`. Usa Java 21 no caminho Homebrew, portas locais 18083/11026 e banco temporário, sem dados de `dados/moeda` nem emails externos. Requer essas portas livres.
- Dez slides renderizados e inspecionados individualmente, com [imagens versionadas](apresentacao/) e [notas, comparação e demonstração](APRESENTACAO.md).

## Encerramento

Entrega técnica verificada localmente. Ensaio humano, apresentação em aula, acesso do professor e envio no Canvas permanecem pendentes. Não houve deploy, migrations remotas ou teste de email em provedor externo. Nenhuma autorização foi alterada nesta sprint; as regras e os testes existentes foram preservados.

[Contribuição individual](../contribuicoes/lucas-nogueira/sprint3.md)
