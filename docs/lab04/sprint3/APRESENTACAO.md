# Apresentação final — Moeda Estudantil, Release 2

Autor: Lucas Nogueira (`lucasnmc12`). Dez slides com notas de apresentação. O tempo abaixo é uma estimativa de planejamento, não um ensaio realizado.

[Apresentação editável no Spaces](page://page_7ef9a4a9a2f48191bfe6c1b89b151132) — a abertura usa o aplicativo e depende do acesso à conta/compartilhamento. As [imagens dos dez slides](apresentacao/) também estão versionadas para consulta sem Spaces. O conteúdo e a fala estão preservados integralmente abaixo para consulta no próprio repositório. A apresentação pode ser exportada para PowerPoint pelo visualizador do Spaces.

## Preparação

1. Na raiz, execute `python3 scripts/smtp_local.py` e, em outro terminal, `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo`.
2. Abra `http://localhost:8080` e `http://localhost:8025`. Use somente contas fictícias. O perfil demo só provisiona `professor@demo.local`, senha `Professor123!`. Cadastre uma empresa e um aluno pela interface antes da apresentação e anote seus próprios acessos.
3. Separe uma imagem PNG/JPEG válida de até 2 MB para o cadastro da vantagem. Confira o saldo do professor, pois o banco em arquivo mantém operações anteriores.
4. Deixe disponíveis os [modelos iniciais](../../diagramas/versoes/sprint1/), os [modelos atuais](../../diagramas/), as [sequências da Sprint 2](../sprint2/README.md) e o [diagrama geral](diagramas/sequencia-geral.svg).
5. Se já houver operações anteriores, escolha valores compatíveis com o saldo. Não resgate o mesmo cupom duas vezes durante a preparação e espere que os emails sejam capturados antes de começar.

## Slide 1 — Moeda estudantil (30 s)

**Na tela:** Release 2 · Apresentação final. Reconhecer o mérito, consultar o saldo e trocar por vantagens. Lucas Nogueira · Lab04S03.

**Fala:** “O sistema permite que professores reconheçam o mérito dos alunos com moedas virtuais. O aluno acompanha o saldo e troca moedas por vantagens oferecidas por empresas parceiras. Vou mostrar o ciclo e compará-lo com os modelos do início do projeto.”

## Slide 2 — O ciclo completo da moeda (30 s)

**Na tela:** Professor reconhece e envia; aluno consulta e escolhe; parceiro confere e entrega. O débito acontece no resgate, antes da entrega presencial.

**Fala:** “Cada perfil tem uma responsabilidade. O professor distribui moedas; o aluno decide como usar o saldo; a empresa cadastra ofertas e confere o cupom gerado. A confirmação presencial registra a utilização, sem debitar o aluno novamente.”

## Slide 3 — Casos de uso iniciais e protótipo final (45 s)

| Modelo inicial | Release 2 | Tela |
| --- | --- | --- |
| Professor envia moedas | Motivo obrigatório, saldo e confirmações distintas | Enviar moedas / Extrato |
| Aluno consulta e troca | Histórico, catálogo, débito e cupom | Extrato / Vantagens / Cupons |
| Empresa cadastra vantagens | Descrição, foto e custo positivo | Vantagens |
| Parceiro confere a troca | Código próprio e confirmação única | Conferir cupons |

**Fala:** “Os três atores do modelo inicial permanecem. Não foi necessário criar um ator intermediário para o cadastro nem um administrador para representar a rotina semestral. O login é a pré-condição dos fluxos privados. Cada caso de uso pode ser localizado na interface e nos controllers.”

Fonte: [casos de uso iniciais](../../diagramas/versoes/sprint1/casos-de-uso.puml) e [atuais](../../diagramas/casos-de-uso.puml).

## Slide 4 — Classes preservadas na implementação (45 s)

**Na tela:** `Usuario` com `Aluno`, `Professor` e `EmpresaParceira`; `Transacao` com `EnvioMoeda`, `CreditoSemestral` e `TrocaVantagem`. A troca preserva descrição/custo, código e data de utilização.

**Fala:** “A hierarquia foi implementada com JPA JOINED. O custo e a descrição da vantagem são copiados no resgate para preservar o histórico se a oferta for editada. Crédito semestral, notificação e cupom já estavam previstos no modelo inicial; não são classes inventadas agora na Sprint 3.”

Fonte: [classes iniciais](../../diagramas/versoes/sprint1/diagrama-de-classes.puml), [classes atuais](../../diagramas/diagrama-de-classes.puml) e `src/main/java/br/edu/pucminas/moeda/modelo/`.

## Slide 5 — Arquitetura MVC e entrega de emails (45 s)

**Na tela:** Navegador → Security → Controller → Service → JPA → H2. Débito, cupom e notificações na mesma transação; envio SMTP posterior pelo worker.

**Fala:** “O controller recebe o formulário, o serviço aplica as regras e o repositório acessa o banco. A caixa de saída persiste as mensagens junto à operação. O diagrama atual refina o inicial: os serviços gravam as notificações pelo JPA, e `NotificacaoService` entrega as pendências. SMTP indisponível mantém o email para nova tentativa, sem repetir o resgate.”

Fonte: [componentes iniciais](../../diagramas/versoes/sprint1/diagrama-de-componentes.puml) e [atuais](../../diagramas/diagrama-de-componentes.puml).

## Slide 6 — Diagrama de Sequência Geral (45 s)

**Na tela:** Reconhecimento → oferta → consulta → resgate → emails → troca presencial.

**Fala:** “Os quatro diagramas anteriores detalham casos isolados. O geral mostra como eles se encadeiam. Vou abrir o SVG para mostrar as linhas de vida dos atores, controllers, serviços, persistência e SMTP. O envio dos emails aparece após a confirmação da transação.”

Abra o [diagrama geral](diagramas/sequencia-geral.svg). O esquema dos slides resume esse artefato, sem substituí-lo.

## Slide 7 — Resgate atômico e cupom único (45 s)

**Na tela:** Validar perfil, disponibilidade e saldo → debitar → registrar troca → preparar duas notificações. Um UUID para tela e dois emails.

**Fala:** “`VantagemService.resgatar` roda com `@Transactional` na classe. Bloqueia o aluno e a vantagem, debita e registra a troca. O UUID nasce uma vez em `TrocaVantagem`; os templates só leem esse código. Se o segundo template falhar, o teste confirma que saldo, cupom e primeira notificação são revertidos.”

## Slide 8 — O que mudou na Release 2 (30 s)

**Na tela:** S1 — templates de envio/recebimento e provas de extrato. S2 — quatro sequências, validação e catálogo com saldo. S3 — sequência geral, templates do resgate, rollback e comparação final.

**Fala:** “O resgate e as demais regras de negócio já vieram do Lab03. A Release 2 reaproveita essa base e aprofunda modelagem, mensagens e verificações. Nesta sprint, a melhoria funcional está nos emails de cupom com textos específicos para aluno e empresa, preservando as regras e o mesmo código.”

## Slide 9 — Demonstração do protótipo (4 min)

1. Empresa cadastra “Desconto em livros” por 40 moedas com foto.
2. Professor envia 120 moedas ao aluno e informa o motivo.
3. Aluno abre o extrato, confere +120, entra no catálogo e resgata a oferta.
4. Mostre o saldo final 80, a saída −40, o cupom e os dois emails com o mesmo código na caixa local. Diferencie os textos dos destinatários.
5. Empresa abre Conferir cupons, compara o código e confirma o uso. O cupom passa a utilizado. Nova tentativa de confirmar o mesmo código é rejeitada no servidor.

**Fala:** “Este é o ciclo que foi modelado. O extrato permite conferir a entrada e a saída, e o cupom conecta o resgate digital à entrega presencial.”

Caso o saldo inicial do aluno não seja zero, informe o saldo inicial e calcule `saldo inicial + 120 − 40` para não apresentar 80 como um valor fixo.

## Slide 10 — Verificação e fechamento (45 s)

**Na tela:** 36 testes passaram. Login, saldo, concorrência, rollback, código e SMTP local. Próximos passos do aluno: ensaiar, apresentar e entregar no Canvas.

**Fala:** “A suíte verifica o ciclo e os caminhos de falha, incluindo saldo insuficiente e falha na preparação do email. Também conferimos HTTP com H2 temporário e SMTP local. Esses testes não substituem a apresentação nem comprovam entrega de email a um provedor externo.”

## Comparação final e justificativas

| Aspecto | Modelo preservado | Resultado e justificativa |
| --- | --- | --- |
| Atores e cadastros | Três perfis, cadastro sem ator intermediário | Preservados; autenticação e verificação do dono protegem os fluxos privados |
| Herança e transações | Usuario e Transacao abstratos | JOINED realiza o desenho no banco; tipos concretos mantêm regras próprias |
| Histórico da troca | Código e descrição da vantagem na TrocaVantagem | Snapshot de custo/descrição evita alterar cupom antigo ao editar uma oferta |
| Notificações | Notificacao e rotina SMTP previstas | Persistência atômica e retentativa desacoplam a entrega SMTP do resgate |
| Componentes | Desenho inicial ligava serviços ao componente de email | Atualizado para mostrar preparação por templates e persistência JPA; worker entrega depois |
| Sequências | Não existiam na Sprint 1 do Lab03 | Quatro fluxos detalhados na S2 e visão integrada na S3 complementam a estrutura estática |
| Emails do resgate | Mesmo texto montado no serviço | Dois templates externos orientam corretamente aluno e parceiro, com o mesmo código |

## Perguntas para o ensaio

- **Por que o email pode chegar depois do resgate?** A notificação já está persistida; o worker tenta SMTP depois do commit e repete em caso de falha.
- **Por que o aluno não fica com saldo negativo em dois resgates simultâneos?** O serviço bloqueia a conta antes de ler e debitar o saldo; os testes concorrentes cobrem esse cenário.
- **Por que não gerar o código em cada email?** Eles precisam representar a mesma troca. O UUID pertence à transação e é reutilizado.
- **O parceiro pode confirmar o cupom de outro?** O serviço exige o parceiro dono e rejeita acesso cruzado.
- **O que falta para encerrar a disciplina?** Ensaio humano, apresentação real, conferência do acesso do professor e entrega no Canvas.

Apoio de IA: Codex/OpenAI auxiliou modelagem, código, testes e materiais. As decisões técnicas não foram atribuídas ao professor sem um registro fornecido. As correções conhecidas dos laboratórios anteriores orientam nomenclatura de domínio e vínculo dos cadastros ao perfil correto; as notas detalhadas ainda não foram fornecidas.
