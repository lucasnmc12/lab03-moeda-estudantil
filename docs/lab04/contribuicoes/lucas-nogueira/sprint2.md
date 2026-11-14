# Contribuições — Lucas Nogueira

## Sprint 2 — Lab04S02

Continuei o trabalho na branch `lab04-sprint2`. Modelei quatro diagramas de sequência: envio de moedas, consulta de extrato, cadastro de vantagens e listagem para o aluno. Usei os nomes das rotas, classes e métodos do projeto e representei os caminhos de validação, erro ou ausência de resultados.

O cadastro e o catálogo já existiam na Release 1. No cadastro, fiz o formulário mostrar os erros de descrição e custo ao lado dos respectivos campos. No catálogo, acrescentei o saldo do aluno e mostrei quantas moedas ainda faltam quando a oferta custa mais do que o saldo disponível.

Validei a entrega com a suíte Maven e com requisições HTTP na aplicação local. Cadastrei contas fictícias, conferi que o custo zero não criou uma oferta e verifiquei a publicação de uma oferta de 40 moedas com foto, descrição e custo. Também conferi que sua retirada a remove do catálogo do aluno.

Usei o Codex, da OpenAI, como apoio para inspecionar a base, escrever e renderizar os diagramas, ajustar as telas e verificar os fluxos. Os commits refletem o horário real da execução.
