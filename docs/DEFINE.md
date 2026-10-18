# Requisitos — Release 1

## Base e objetivo

Fontes: enunciado e três roteiros fornecidos pelo aluno, copiados sem alteração em `docs/enunciado.md` e `docs/sprints/`. Autor: Lucas Nogueira. O objetivo é reconhecer alunos e permitir a troca de moedas por vantagens reais de parceiros. MVC e PlantUML são restrições da disciplina.

## Perfis e acesso

`Usuario` generaliza `Aluno`, `Professor` e `EmpresaParceira`. O cadastro público cria somente aluno ou parceiro. Professor e instituição entram por pré-cadastro operacional. Login por email e senha, hash BCrypt, sessão e CSRF. Cada perfil edita apenas a própria conta. Alunos consultam somente seu extrato e cupons; professores consultam apenas seus envios; parceiros administram apenas suas vantagens e conferem seus cupons. Dados pessoais nunca aparecem numa listagem pública.

## Critérios observáveis

| ID | Comportamento esperado e limites | Evidência prevista |
|---|---|---|
| AC-01 | Modelos de casos de uso, classes e componentes e histórias coerentes com os três perfis e o enunciado | PlantUML, comparação com os casos abaixo |
| AC-02 | Aluno cadastra nome, email, CPF válido, RG, endereço, curso e instituição existente; duplicidade e dados inválidos geram mensagem | Testes MVC de cadastro e rejeição |
| AC-03 | Parceiro cadastra nome, email e senha; edita, consulta e remove sua conta; outro usuário não acessa a conta | Testes MVC dos dois CRUDs, isolamento e sessão |
| AC-04 | Autenticação para os três perfis; senha incorreta, papel indevido e POST sem CSRF são negados | Integração Spring Security e mutação isolada |
| AC-05 | ER declara PK/FK, constraints e rastreabilidade às entidades; ORM funciona e dados sobrevivem a reinício | Schema SQL e teste em H2 de arquivo |
| AC-06 | Professor recebe 1.000 moedas por semestre, preservando o restante; repetir processamento do mesmo semestre não duplica o crédito | Teste de acumulação e idempotência |
| AC-07 | Professor envia quantidade inteira positiva com motivo obrigatório, somente com saldo suficiente; débito e crédito são atômicos | Testes de envio, rollback e concorrência |
| AC-08 | Professor e aluno consultam saldo e transações próprias, com data, quantidade e motivo/vantagem | Testes de extrato e telas |
| AC-09 | Parceiro cadastra vantagem com descrição, foto e custo positivo; altera apenas a própria vantagem | Testes de validação e acesso cruzado |
| AC-10 | Aluno resgata vantagem com saldo suficiente; débito e cupom de código único são atômicos | Testes de resgate e saldo insuficiente |
| AC-11 | Recebimento gera email para aluno; resgate gera emails ao aluno e parceiro com o mesmo código; falha de SMTP mantém mensagem para nova tentativa | Integração com servidor SMTP local de teste e cenário de falha |
| AC-12 | Parceiro confere cupom próprio e confirma uso uma única vez; outro parceiro é negado | Testes de confirmação e reutilização |
| AC-13 | CRUDs têm telas de criar, consultar, editar e remover, feedback e validações compreensíveis | MVC com templates reais e inspeção visual |
| AC-14 | Slides refletem arquitetura/persistência reais; tutorial de 20 minutos tem roteiro de demonstração | PPTX/PDF e roteiro reproduzível; ensaio humano pendente |
| AC-15 | README segue seções aplicáveis do template, execução de clone limpo e histórico real com commits separados | Build limpo, Git e documentação |

## Decisões desta execução

- O email será o login único de todos os perfis. Professor recebe email no pré-cadastro porque o enunciado exige login, mas não especifica seu identificador.
- CNPJ não é obrigatório: aparece como sugestão do roteiro, mas não é dado exigido pelo enunciado. Não será inventado para limitar o cadastro de parceiros.
- Foto será enviada como arquivo PNG/JPEG, armazenada no banco com limite de tamanho. Não dependerá de imagem remota.
- Remoção de conta será desativação lógica: bloqueia login e preserva extratos e cupons já emitidos. Exclusão física destruiria o histórico de reconhecimento e troca.
- O calendário civil janeiro–junho/julho–dezembro define o semestre operacional inicial. A instituição poderá adotar outro calendário em versão futura.
- O enunciado não restringe envio à mesma instituição. O professor pode selecionar qualquer aluno ativo; essa restrição não será acrescentada sem orientação.
- Emails usam caixa de saída transacional e SMTP configurável. Ambiente demonstrativo deve usar um capturador SMTP local. Entrega a provedores externos depende de configuração real.

## Limites e pendências externas

Sem feedback específico das apresentações das Sprints 1 e 2, nenhuma correção será atribuída ao professor. Orientações anteriores sobre precisão dos atores e consistência entre artefatos servem como base técnica. Presença em aula, avaliação, ensaio humano e entrega no Canvas não podem ser realizados por esta implementação. Datas e horários dos commits serão reais. Publicação no GitHub foi autorizada pelo aluno.
