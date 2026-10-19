# Contribuições — Lucas Nogueira

## Sprint 3 — Lab03S03

Nesta sprint trabalhei sozinho. Conectei os cadastros aos fluxos de reconhecimento e troca de moedas e atualizei os diagramas para refletir a implementação. Mantive a hierarquia de usuários e transações e ajustei as cardinalidades do modelo ER para representar as tabelas de especialização com chave primária compartilhada.

Implementei o crédito semestral de mil moedas para o professor, preservando o saldo acumulado e impedindo a concessão repetida no mesmo semestre. No envio ao aluno, o sistema exige quantidade positiva, saldo suficiente e motivo do reconhecimento. O débito, o crédito e o registro da movimentação são feitos na mesma transação. Alunos e professores consultam seus próprios extratos.

Implementei a manutenção de vantagens pela empresa parceira, incluindo descrição, custo e foto em PNG ou JPEG de até 2 MB. No resgate, o sistema desconta o saldo do aluno, registra os dados da vantagem naquele momento e gera um código de cupom. Somente a empresa responsável pela vantagem pode confirmar sua utilização, uma única vez.

Organizei o envio de emails por uma caixa de saída persistente, registrada junto à operação. Um processo envia as mensagens por SMTP e tenta novamente em caso de falha. Assim, o aluno e a empresa recebem o mesmo código de resgate sem depender da disponibilidade imediata do servidor de email. A demonstração usa um servidor SMTP local; uma falha ambígua na confirmação do envio pode repetir o email, mas não repete o débito de moedas.

Durante os testes concorrentes, encontrei uma situação em que o saldo podia ser lido antes do bloqueio das entidades com herança `JOINED`. Corrigi a operação para bloquear primeiro a linha de `Usuario` e depois carregar ou atualizar os dados do perfil. Também corrigi uma expressão do extrato que falhava ao exibir movimentações, substituindo o acesso à classe por uma propriedade de apresentação chamada `tipo` e acrescentando um teste com lançamentos reais.

Verifiquei os fluxos com testes de integração, incluindo envios e resgates concorrentes, saldo insuficiente, acesso a dados de outro usuário, proteção CSRF, persistência e falha e nova tentativa de email. A versão final terminou com 25 testes aprovados, sem falhas, erros ou testes ignorados. Também executei a compilação e os testes em uma cópia obtida do repositório para conferir a reprodução da entrega.

Na demonstração pela interface, cadastrei dados fictícios, enviei 120 moedas para uma aluna e resgatei uma vantagem de 40, conferindo o saldo final de 80 moedas. Verifiquei o mesmo código nos emails locais do aluno e da empresa e confirmei a utilização do cupom pelo perfil da empresa parceira.

Preparei as apresentações editáveis e suas versões em PDF sobre arquitetura e persistência, além do tutorial com roteiro de vinte minutos. O ensaio cronometrado e a apresentação ainda precisam ser realizados. Usei o Codex, da OpenAI, como apoio na implementação, atualização dos diagramas, documentação, preparação das apresentações e execução das verificações. Organizei a evolução em commits pequenos, com assuntos separados e mensagens objetivas.
