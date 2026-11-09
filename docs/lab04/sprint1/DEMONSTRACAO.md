# Demonstração — Lab04S01

## Preparação

Na raiz do projeto, com JDK 21:

```bash
git switch lab04-sprint1
./mvnw clean verify
```

Em um terminal, iniciar o capturador local:

```bash
python3 scripts/smtp_local.py
```

Em outro, iniciar a aplicação com dados fictícios:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

Abrir http://localhost:8080 e http://localhost:8025. O capturador só recebe emails neste computador. Nenhuma credencial SMTP real é necessária para esta demonstração. Para um provedor externo, configurar `SMTP_HOST`, `SMTP_PORT`, `SMTP_USER`, `SMTP_PASSWORD`, `SMTP_AUTH`, `SMTP_TLS` e `SMTP_FROM` por variáveis de ambiente, sem versionar os valores secretos.

## Percurso para apresentar

1. Cadastrar um aluno com dados fictícios e selecionar a instituição demonstrativa. Usar um CPF de teste válido e ainda não cadastrado; exemplo: `11144477735`. Os professores já são pré-cadastrados.
2. Entrar com `professor@demo.local` e senha `Professor123!`. Em banco novo, o crédito semestral deixa o professor com 1.000 moedas. Em banco utilizado anteriormente, considerar o saldo existente.
3. Abrir **Reconhecer um aluno**, escolher o aluno, informar 120 moedas e um motivo. Mostrar que motivo vazio e quantidade não positiva não são aceitos.
4. Enviar e abrir o extrato do professor. Em banco novo, mostrar saldo 880 e movimentação de −120.
5. Tentar enviar mais moedas do que o saldo disponível. Mostrar a mensagem **Saldo de moedas insuficiente.** e a ausência de novo lançamento.
6. Sair e entrar como aluno. Mostrar saldo 120 e o recebimento de +120 no extrato. Uma conta de outro aluno não deve ver esse reconhecimento.
7. Abrir a caixa em http://localhost:8025 e aguardar a rotina de entrega, executada a cada 15 segundos. Mostrar os dois destinatários, os assuntos distintos e a mesma quantidade e motivo.
8. Explicar que a confirmação do professor informa **Você enviou**, enquanto a notificação do aluno informa **Você recebeu**. Os textos fixos estão em arquivos separados, e os dados variáveis vêm da movimentação.
9. Explicar a retentativa: se o SMTP estiver desligado, as duas notificações ficam pendentes no banco. Religar o capturador e aguardar a entrega; o saldo não deve mudar novamente.

## Explicação da implementação

`MoedaController` recebe o formulário e utiliza o usuário autenticado. `MoedaService`, dentro de uma transação, bloqueia os saldos, valida e registra o envio. `EmailReconhecimento` renderiza os dois arquivos `.txt` com Thymeleaf em modo TEXT, e os resultados são gravados em duas notificações. `NotificacaoService` envia o conteúdo persistido por SMTP. O extrato é uma consulta ao histórico, sem tabela própria.

A falha em preparar uma notificação desfaz toda a operação. Uma indisponibilidade posterior do SMTP mantém a operação e os emails pendentes. Uma falha ambígua após aceitação SMTP pode repetir o email, sem repetir a movimentação.

## Evidências desta execução

A demonstração real no navegador utilizou portas alternativas (aplicação 8081, SMTP 1026, caixa 8026) e banco H2 temporário em `/tmp/lab04-sprint1-demo`, preservando os dados do Lab03. Foram utilizados apenas cadastros fictícios. O professor enviou 120 moedas, ficou com 880, e a aluna ficou com 120. Os dois emails foram capturados com o mesmo motivo e textos específicos para cada perfil.

- [Extrato do professor](evidencias/extrato-professor.png).
- [Extrato do aluno](evidencias/extrato-aluno.png).
- [Emails de confirmação e recebimento](evidencias/emails-professor-aluno.png).
- [Resultados automatizados](evidencias/testes.json).

As imagens documentam a execução local; não constituem um vídeo nem comprovam apresentação em aula. Se o ambiente não puder ser reproduzido no laboratório, Lucas deverá gravar e disponibilizar o vídeo exigido no roteiro.
