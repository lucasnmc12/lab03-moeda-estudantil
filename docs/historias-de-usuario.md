# Histórias do usuário

As histórias correspondem aos UC01–UC08. Regras internas de saldo e envio de email aparecem como critérios, evitando transformar cada passo interno em um objetivo de ator. Autenticação é pré-condição dos fluxos privados, sem exigir novo login a cada ação.

## HU01 — Realizar login (UC01)

Como usuário, quero acessar minha conta com email e senha, para usar as funções do meu perfil.

Critérios de aceitação: Credenciais corretas abrem o painel do perfil. Senha incorreta mostra erro. Conta desativada não entra. Rotas privadas exigem sessão e formulários exigem CSRF.

## HU02 — Manter cadastro de aluno (UC02)

Como aluno, quero cadastrar e manter meus dados, para participar do reconhecimento estudantil.

Critérios de aceitação: Cadastro exige todos os dados do enunciado e instituição pré-cadastrada. CPF e email duplicados são rejeitados. Somente o dono consulta, edita ou remove sua conta. Saldo não pode ser enviado pelo formulário.

## HU03 — Manter cadastro de empresa (UC03)

Como empresa parceira, quero cadastrar e manter minha conta, para oferecer vantagens aos alunos.

Critérios de aceitação: Nome, email e senha são obrigatórios. O cadastro é público. Consulta, edição e remoção exigem a própria sessão. A remoção preserva histórico e bloqueia novo login.

## HU04 — Enviar moedas (UC04)

Como professor, quero enviar moedas e informar o motivo, para reconhecer o mérito de um aluno.

Critérios de aceitação: Quantidade positiva, motivo obrigatório e saldo suficiente. Aluno deve estar ativo. Débito, crédito, transação e notificação persistem juntos. Créditos semestrais de 1.000 acumulam e não se repetem no mesmo semestre.

## HU05 — Consultar extrato (UC05)

Como aluno ou professor, quero consultar saldo e movimentações, para acompanhar o uso das moedas.

Critérios de aceitação: Professor vê seus créditos semestrais e envios. Aluno vê recebimentos e resgates. Cada lançamento informa data e quantidade. Um usuário não vê o extrato alheio.

## HU06 — Manter vantagens (UC06)

Como empresa parceira, quero cadastrar vantagens com descrição, foto e custo, para oferecer produtos e descontos.

Critérios de aceitação: Descrição, PNG/JPEG e custo inteiro positivo são obrigatórios. Apenas a empresa dona edita ou retira a oferta. A retirada impede novos resgates e preserva os cupons já emitidos.

## HU07 — Resgatar vantagem (UC07)

Como aluno, quero trocar meu saldo por uma vantagem, para usar um benefício de um parceiro.

Critérios de aceitação: Saldo suficiente e vantagem ativa. O sistema desconta o custo uma vez por operação, gera código único e envia o mesmo código por email ao aluno e parceiro. Falha de envio de email permite tentar novamente sem repetir o resgate.

## HU08 — Conferir e utilizar cupom (UC08)

Como empresa parceira, quero consultar e confirmar o código apresentado, para entregar a vantagem ao aluno.

Critérios de aceitação: Parceiro vê apenas cupons de suas vantagens. Código inexistente e de outro parceiro não revelam dados. Cupom usado não pode ser confirmado novamente. Aluno consulta seus próprios códigos.

