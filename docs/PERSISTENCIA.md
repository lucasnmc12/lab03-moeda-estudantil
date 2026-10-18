# Persistência — Sprint 2

## ORM com Spring Data JPA e Hibernate

A aplicação mantém o padrão MVC do Lab02, mas troca o H2 em memória por H2 em arquivo. Services delimitam transações, repositories isolam as consultas e Flyway aplica `V1__cria_estrutura.sql`. Hibernate valida o schema (`ddl-auto=validate`), sem criar ou destruir tabelas automaticamente.

## Mapeamento

| Classe | Tabela e chaves |
|---|---|
| InstituicaoEnsino | instituicao_ensino (PK id) |
| Usuario | usuario (PK id, email UNIQUE) |
| Aluno | aluno (PK/FK id para usuario, FK instituicao_id, CPF UNIQUE) |
| Professor | professor (PK/FK id para usuario, FK instituicao_id, CPF UNIQUE) |
| EmpresaParceira | empresa_parceira (PK/FK id para usuario) |
| Vantagem | vantagem (PK id, FK empresa_id) |
| Transacao | transacao (PK id) |
| EnvioMoeda | envio_moeda (PK/FK id para transacao, professor_id e aluno_id) |
| TrocaVantagem | troca_vantagem (PK/FK id para transacao, aluno_id e vantagem_id, código UNIQUE) |
| CreditoSemestral | credito_semestral (PK/FK id para transacao, professor_id, UNIQUE professor/semestre) |
| Notificacao | notificacao (PK id, FK transacao_id) |

A herança JOINED centraliza credenciais em `usuario`, e data/quantidade em `transacao`. O custo e a descrição do momento da troca ficam preservados, mesmo se a vantagem mudar depois. Saldo e custo têm CHECK no banco, além das regras em Java.

## Consistência

Envio e resgate usam uma transação para saldo, lançamento e caixa de saída de emails. O bloqueio SQL da linha de `usuario` acontece antes da leitura das subclasses: `SELECT id ... FOR UPDATE`, seguido de leitura atualizada da entidade. Isso evita a leitura antecipada que pode ocorrer com bloqueio posterior em herança JOINED. Os testes concorrentes verificam essa decisão contra H2 real.

A exclusão é lógica e revoga o acesso. Não há cascade REMOVE de contas para transações. Oferta retirada desaparece do catálogo, mantendo o cupom válido para conferência.

## Instalação e backup

Executar na raiz do projeto cria `dados/moeda.mv.db`. Parar o processo antes de copiar a pasta `dados/`. Para restaurar, parar a aplicação e repor a cópia completa, mantendo a versão do código compatível. Não editar uma migração aplicada: a próxima mudança ganha V2. Não há dados de produção neste repositório.

## Pré-cadastro operacional

Instituições e professores não possuem cadastro público. O perfil `demo` inclui apenas uma instituição fictícia e um professor fictício com credencial documentada. Para outras instituições, o responsável pela instalação deve fornecer a lista oficial e provisionar os dados com senha BCrypt; importação de lista não faz parte dos CRUDs exigidos pela rubrica.
