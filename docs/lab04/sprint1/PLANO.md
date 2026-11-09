# Lab04S01 — Envio de moedas e extratos

Continuação da Release 1 no mesmo repositório, na branch `lab04-sprint1`, a partir de `f704297`. Somente a Sprint 1 do Laboratório 04 está no escopo desta entrega. O Lab03 já contém envio, extratos, vantagens, cupons e SMTP local; funcionalidades herdadas não serão apresentadas como implementadas pela primeira vez nesta sprint.

## Critérios e verificação planejada

| Critério | Comportamento / implementação | Verificação |
|---|---|---|
| Envio de moedas | Manter transação atômica em MoedaService, saldo positivo/suficiente e motivo obrigatório | Testes de serviço existentes e novo teste MVC com autenticação, formulário e saldo insuficiente |
| Extratos | Manter consulta privada para professor/aluno, com saldo e histórico | Renderizar ambos após envio pelo controller; verificar sinal e isolamento |
| Email do professor | Acrescentar confirmação com nome, aluno, quantidade e motivo | Template próprio em arquivo, notificação persistente e captura SMTP real |
| Email do aluno | Extrair texto do serviço para template próprio com professor, quantidade e motivo | Conferir conteúdo personalizado, destinatário e caracteres acentuados no SMTP |
| Falha no SMTP | Manter as duas mensagens pendentes sem desfazer ou repetir a movimentação | Falha e retentativa no servidor local, com conferência de saldos e transação única |
| Demonstração | Ambiente reproduzível com perfil demo e capturador SMTP | Roteiro passo a passo e verificação funcional local; apresentação em aula ou vídeo depende de Lucas |
| Versionamento | Commits pequenos, push da branch e PR para main | Horários reais e registro da continuidade; sem merge nesta execução |

## Decisões

- Reutilizar Java 21, Spring MVC, Thymeleaf, JPA/Hibernate, H2 em arquivo, Flyway e Spring Security.
- Renderizar emails em texto UTF-8 com Thymeleaf em modo TEXT e resolver próprio, separado das páginas HTML. Persistir o texto renderizado na mesma transação do envio. O worker SMTP existente continua responsável pela entrega e retentativa.
- Sem mudança de schema: a tabela de notificações já permite mais de uma mensagem por transação e mantém o conteúdo enviado.
- Preservar os modelos e registros do Lab03. Documentação e contribuições novas ficam em `docs/lab04/`.
- Nenhuma regra de autorização será alterada; os testes existentes continuam verificando as restrições de acesso.

## Pontos da rubrica

O roteiro se contradiz nos pesos: a tabela inicial informa 1,6 para envio/extrato e 2,4 para commits; o checklist informa 2,0 para ambos. Os critérios funcionais permanecem os mesmos. A distribuição oficial deve ser confirmada com o professor.

O horário de aula informado no roteiro é segunda-feira, 10h40–12h20. Os commits terão horários reais e intervalos de 7 a 15 minutos conforme solicitado; isso não comprova presença nem substitui a demonstração em aula. As Sprints 2 e 3 não serão executadas nesta branch.
