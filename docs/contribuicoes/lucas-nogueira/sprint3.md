# Contribuição — Lucas Nogueira — Sprint 3

Material preparado com apoio do Codex, da OpenAI, nesta execução. O fluxo de moedas e vantagens foi conectado aos CRUDs: crédito semestral acumulável, envio justificado, extrato, foto, resgate e conferência de cupom de uso único.

O teste concorrente revelou que a leitura do saldo em herança JOINED podia ocorrer antes do bloqueio. A aplicação passou a bloquear a linha base antes de atualizar a entidade. A navegação encontrou um erro na expressão do extrato, corrigido com a propriedade de apresentação `tipo` e um teste que renderiza lançamentos reais.

A integração de email usa caixa de saída transacional e SMTP. Testes capturam mensagens em servidor local e verificam falha e nova tentativa. Na interface de demonstração, uma aluna recebeu 120 moedas, resgatou uma vantagem de 40 e ficou com 80. Aluno e parceiro receberam emails locais com o mesmo código, e o parceiro confirmou a utilização do cupom.

A versão final destaca erros nos campos do cadastro. Foram preparados slides editáveis e PDFs sobre arquitetura e persistência, além do tutorial com roteiro de vinte minutos. Lucas deve fazer o ensaio cronometrado e a apresentação. Nenhum histórico de aula, feedback ou aprovação do professor foi inventado.
