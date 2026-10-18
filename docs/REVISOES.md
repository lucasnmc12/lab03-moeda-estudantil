# Revisões e decisões

## Base das três sprints

Não foi fornecido feedback do professor específico deste laboratório. As revisões abaixo são decisões técnicas desta execução, não correções atribuídas a apresentações que não ocorreram nesta sessão.

Dos laboratórios anteriores foram aproveitados: nomes precisos de domínio, generalização `Usuario` quando há comportamentos comuns, cadastro ligado ao perfil real e coerência entre histórias, modelos e implementação. O caso controverso do ator de estatísticas da biblioteca não foi tratado como orientação aprovada. Aqui SMTP aparece no diagrama de componentes como integração de infraestrutura; o envio de email é critério do envio de moedas e do resgate.

## Sprint 1

Autenticação é pré-condição dos fluxos privados. O cadastro é público para aluno e parceiro, sem ator intermediário. Crédito semestral é rotina do sistema, sem inventar um ator administrador no produto.

## Feedback das apresentações

Pendente de fornecimento pelo aluno. Preservar a origem e registrar futuras mudanças com data real e motivo.

## Sprint 2 — schema e cadastros

ORM, Flyway e H2 em arquivo substituem o banco volátil do laboratório anterior. O ER explicita PK/FK e a herança JOINED. A versão inicial já inclui validação e remoção lógica de contas próprias.

## Sprint 3 — ajustes verificados

- Bloquear a linha de usuario antes de ler o saldo, após o teste concorrente demonstrar perda de atualização com bloqueio posterior.
- Expor `Transacao.getTipo()` para o template, após o navegador e o teste de regressão reproduzirem erro de reflexão no extrato.
- Persistir notificações e priorizar pendências com menos tentativas, para que uma mensagem problemática não retenha toda a fila.
- Destacar erros junto aos campos do cadastro e manter a mesma nomenclatura nos modelos e no código.

Origem: implementação e verificação desta execução. Não são apontamentos atribuídos ao professor.

- Revisão final do ER: as relações de herança JOINED usam 1 para 0..1 por especialização, pois a PK da subclasse também é FK. As associações comuns continuam 1 para 0..*. Essa é uma correção técnica desta execução.
- O diagrama de casos de uso final reúne as pré-condições em uma nota e mantém regras detalhadas nas histórias, reduzindo a carga visual sem alterar os objetivos dos atores.
