# Contribuições — Lucas Nogueira

## Sprint 1 — Lab04S01

### Contribuição

Nesta sprint continuei como único integrante e dei continuidade ao Sistema de Moeda Estudantil no mesmo repositório, em uma branch própria. Parti da Release 1, que já possuía envio de moedas, extratos, autenticação e caixa de saída de emails. Comparei essa base com o roteiro do Laboratório 04 para identificar o que precisava ser acrescentado e verificado nesta entrega.

Separei o conteúdo dos emails de reconhecimento em dois templates de texto: um confirma ao professor o envio e o outro informa ao aluno o recebimento. Cada template recebe os nomes, a quantidade e o motivo da movimentação. Acrescentei a confirmação do professor e mantive o aviso do aluno, registrando as duas notificações na mesma transação que altera os saldos e grava o envio. Também atualizei a mensagem de sucesso do formulário para informar que ambos os emails estão na fila.

Mantive a organização MVC e a infraestrutura de persistência e SMTP da Release 1. O componente `EmailReconhecimento` cuida da renderização dos templates, enquanto `NotificacaoService` continua responsável por enviar o conteúdo persistido e tentar novamente quando o SMTP falha. Atualizei o diagrama de componentes para representar essa separação.

### Decisões e verificação

Escolhi templates em texto UTF-8 com Thymeleaf em modo TEXT, usando um renderizador separado das páginas HTML. Assim, o texto fixo não fica concatenado no serviço de moedas e pode ser ajustado sem alterar a regra de negócio. Não foi necessário modificar o banco, pois uma transação já podia ter mais de uma notificação.

Acrescentei testes para o envio pelo formulário com login real, os extratos dos envolvidos, o isolamento de outro aluno e a mensagem de saldo insuficiente. Também verifiquei os dois emails em um servidor SMTP local, incluindo destinatários, assuntos, acentos e motivo. Um teste simula falha na preparação do segundo template e confirma o rollback dos saldos, da movimentação e da primeira notificação. A suíte final passou com 29 testes, sem falhas ou erros.

Na demonstração pelo navegador, utilizei um banco temporário e contas fictícias. O professor enviou 120 moedas e ficou com 880; a aluna recebeu 120. Conferi os dois extratos e as mensagens específicas de envio e recebimento na caixa SMTP local. Registrei capturas e preparei um roteiro para repetir a demonstração. A apresentação em aula ou a gravação do vídeo ainda precisa ser realizada.

### Uso do Codex

Usei o Codex, da OpenAI, como apoio na análise do roteiro, implementação dos templates e notificações, escrita e execução dos testes, demonstração local e documentação. Mantive os registros anteriores do Lab03 e organizei o trabalho da nova sprint em commits separados, com horários reais.
