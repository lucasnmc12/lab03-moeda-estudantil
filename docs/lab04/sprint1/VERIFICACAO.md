# Verificação — Lab04S01

## Resultado

**TECHNICALLY_COMPLETE para demonstração local.** A Sprint 1 da Release 2 foi implementada na branch `lab04-sprint1`, a partir de `f704297`, mantendo o mesmo projeto. A suíte terminou com **29 testes, zero falhas, zero erros e zero ignorados**. O fluxo foi demonstrado no navegador com banco temporário e captura SMTP local. Não houve integração à main nesta execução.

Permanecem externos: apresentação em aula ou vídeo caso o ambiente não seja reproduzível no laboratório, confirmação dos pesos divergentes da rubrica e entrega no Canvas. SMTP de um provedor externo não foi configurado nem testado.

## Critérios e evidências

| Critério | Implementação | Verificação executada |
|---|---|---|
| Envio de moedas atômico | MoedaService, transação e bloqueio de saldo herdados, com duas notificações | MoedaServiceTest: valores válidos/inválidos e concorrência; MoedaAtomicidadeTest: falha no segundo template desfaz débito, crédito, envio e primeira notificação |
| Formulário e saldo insuficiente | MoedaController e página de envio existentes; feedback atualizado | MoedaSprint1MvcTest: login real, POST com CSRF, redirecionamento, erro compreensível e ausência de alteração/emails no erro |
| Extratos de professor/aluno | Consulta própria existente, incluindo recebimentos e trocas | MoedaSprint1MvcTest: saldo880/120, saída−120/entrada+120 e extrato vazio de outro aluno; testes existentes de autorização preservados |
| Template específico do professor | EmailReconhecimento e envio-professor.txt | MoedaServiceTest e NotificacaoSmtpTest: destinatário professor, assunto de confirmação, aluno, quantidade e motivo |
| Template específico do aluno | EmailReconhecimento e recebimento-aluno.txt | Captura SMTP real e decodificação MIME: destinatário aluno, assunto de recebimento, professor, quantidade e acentos |
| Templates separados da regra | Dois arquivos TEXT UTF-8, renderizador SpringTemplateEngine próprio | MoedaSprint1MvcTest: motivo literal com acentos, sinais e expressão não interpretada |
| Retentativa SMTP | Worker e tabela de notificações existentes | NotificacaoSmtpTest: duas mensagens pendentes após falha; entrega posterior sem novo débito; regressão dos cupons preservada |
| Demonstração funcional | Perfil demo, H2 em arquivo e capturador SMTP local | Cadastro e login no navegador, envio de120, extratos880/120 e dois emails capturados; imagens e JSON em evidencias/ |
| Documentação e modelagem | README, contribuição, roteiro e componente de renderização de emails | PlantUML renderizado e imagem inspecionada; fontes do Lab04 preservadas; git diff --check sem erros |

## Execuções

1. Baseline: `./mvnw clean verify` inicialmente encontrou dois erros de permissão de socket no ambiente restrito. Com loopback permitido, os 25 testes originais passaram. Não foi necessário modificar o código para corrigir esse impedimento ambiental.
2. Antes da implementação, a nova expectativa de duas notificações falhou como esperado: `expected: <2> but was: <1>` em `MoedaServiceTest.envioDebitaCreditaRegistraMotivoEDuasNotificacoes`.
3. Na primeira integração, o renderizador Thymeleaf genérico tentou usar OGNL, ausente nas dependências Spring existentes. A correção usou `SpringTemplateEngine`, sem acrescentar dependências. A asserção do novo teste de saldo insuficiente também foi ajustada para o texto real já existente na aplicação.
4. Após essas correções, `./mvnw clean verify` passou com 29 testes. São os 25 cenários anteriores, com expectativas atualizadas para os dois emails, e quatro cenários novos. [Totais por suíte](evidencias/testes.json).
5. `plantuml -tpng docs/diagramas/diagrama-de-componentes.puml` gerou a imagem atualizada, que foi inspecionada.
6. A demonstração no navegador utilizou a aplicação em8081, SMTP1026 e caixa8026, com H2 em `/tmp/lab04-sprint1-demo`. Foram conferidos os saldos, os lançamentos e as duas mensagens. [Registro](evidencias/demonstracao.json) e [roteiro reproduzível](DEMONSTRACAO.md).

O teste de atomicidade simula uma exceção na preparação do segundo template; saldos, transação e notificações são conferidos no banco real. O teste SMTP usa um servidor real local via socket, não um mock de envio de email. Nenhuma checagem de autenticação ou autorização foi alterada; não houve nova mutação de segurança nesta sprint, e a suíte existente de permissões continuou passando.

## Compatibilidade e operação

Não há migração de banco ou mudança de configuração obrigatória. A tabela de notificações já comporta as duas mensagens, e o worker envia tanto as antigas quanto as novas. Uma falha ao renderizar um template impede toda a movimentação. Uma falha posterior do SMTP mantém as mensagens pendentes. A entrega permanece pelo menos uma vez: uma confirmação SMTP ambígua pode repetir um email, mas não a movimentação financeira.

Os templates novos se aplicam aos envios realizados após esta alteração; notificações antigas preservam o texto gravado originalmente. Reverter a alteração não exige desfazer o schema nem remover registros.

A execução mantém horários reais de commit e os intervalos solicitados. Isso não comprova participação em aula nem substitui a avaliação do professor. As Sprints 2 e 3 do Lab04 permanecem fora desta entrega, embora funcionalidades de vantagens/cupons já existam na base herdada.
