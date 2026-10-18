# Design técnico — Release 1

## Escolha da arquitetura

Java 21 e Spring Boot 3.5.6, como `../lab02-aluguel-carros/pom.xml`. MVC com Thymeleaf evita um segundo projeto frontend. Spring Data JPA/Hibernate isola o banco nos repositórios; H2 em arquivo mantém execução simples e dados entre reinícios. Migrações Flyway versionam o schema e Hibernate apenas valida. Testes usam H2 isolado em memória e teste separado em arquivo.

## Componentes e contratos

- `controller`: recebe formulários, valida Bean Validation, seleciona templates, redireciona após sucesso. Nenhum SQL ou ajuste de saldo aqui.
- `servico/CadastroService`: criação, edição e desativação de contas próprias. Cadastro nunca recebe saldo, perfil arbitrário ou instituição criada pelo usuário.
- `servico/MoedaService`: crédito de 1.000 por professor/semestre, envio e extrato. Atualizações usam transação e bloqueio pessimista do proprietário do saldo.
- `servico/VantagemService`: oferta própria, foto PNG/JPEG até 2 MB, resgate e cupom de uso único.
- `servico/NotificacaoService`: grava emails na mesma transação do evento. Worker SMTP tenta entregar pendentes e grava sucesso. Falha não perde cupom nem repete débito. Entrega tem semântica pelo menos uma vez: uma falha após aceitação SMTP pode repetir o email, nunca o resgate.
- `config`: sessão, BCrypt, CSRF, dados demonstrativos e jobs. Contas de demonstração só existem no perfil `demo`.

## Persistência e concorrência

Herança `JOINED` para `Usuario` e `Transacao`. Email único na tabela base, CPF único em aluno e em professor. Saldo inteiro não negativo, quantidade e custo positivos protegidos também por CHECK no schema. Instituição obrigatória em aluno/professor. Transações são imutáveis no histórico. Conta e vantagem são desativadas logicamente para preservar referências. `TrocaVantagem` guarda custo/descrição do resgate, código UUID único e data de uso. Crédito possui UNIQUE(professor_id, semestre).

Bloqueio de usuário serializa débitos concorrentes. Envios bloqueiam professor e depois aluno; resgate bloqueia aluno e depois vantagem. Atualização e retirada de oferta também bloqueiam vantagem. Cadastro/edição de conta usa o mesmo lock do usuário. Contas desativadas são rejeitadas a cada ação de serviço, inclusive com sessão antiga. Cupons usam lock na confirmação para impedir uso simultâneo.

## Rotas previstas

| Rotas | Perfil e resultado |
|---|---|
| GET/POST `/entrar`, POST `/sair` | Login de qualquer perfil e logout |
| GET `/alunos/novo`, POST `/alunos` | Cadastro público de aluno |
| GET `/empresas/novo`, POST `/empresas` | Cadastro público de parceiro |
| GET `/alunos`, `/empresas` | Lista privada com a própria conta |
| GET `/alunos/{id}`, `/empresas/{id}` | Consulta do próprio registro |
| GET `/{perfil}/{id}/editar`, POST `/{perfil}/{id}` | Edita o próprio registro |
| POST `/{perfil}/{id}/excluir` | Desativa conta própria e encerra sessão |
| GET `/extrato` | Professor/aluno veem saldo e histórico próprios |
| GET/POST `/moedas/enviar` | Professor envia reconhecimento |
| GET `/vantagens` | Catálogo autenticado e ofertas próprias do parceiro |
| GET/POST `/vantagens/nova`, GET/POST `/vantagens/{id}/editar` | Parceiro mantém vantagem própria |
| GET `/vantagens/{id}/foto`, POST `/vantagens/{id}/excluir` | Foto autenticada e retirada pelo dono |
| POST `/vantagens/{id}/resgatar` | Aluno troca saldo por cupom |
| GET `/cupons`, POST `/cupons/{codigo}/utilizar` | Aluno consulta próprios; parceiro consulta/confirma os seus |

Erros de regra voltam ao formulário com mensagem. Recurso privado alheio retorna 404. Papel incorreto retorna 403. Erros inesperados recebem página sem stack trace. Todos os POST exigem CSRF. HTML usa escape padrão Thymeleaf.

## Manifesto e verificação

| Critérios | Arquivos previstos | Prova planejada |
|---|---|---|
| AC-01 | `docs/diagramas/*.puml`, histórias | Render PlantUML e comparação dos nomes |
| AC-02/03/13 | `modelo/Aluno`, `EmpresaParceira`, `dto/*`, `CadastroService`, controllers, templates | `CadastroMvcTest`: ciclo completo de ambos, inválidos, duplicidade, conta alheia, sessão desativada |
| AC-04 | `SegurancaConfig`, checagens dos serviços | `SegurancaTest`: login real, papéis, CSRF, recursos alheios; mutações temporárias em cópia isolada |
| AC-05 | `db/migration`, entidades/repositórios | `PersistenciaTest`: migração, constraints, reabertura do arquivo |
| AC-06/07/08 | `MoedaService`, transações | `MoedaServiceTest`: crédito repetido, acumulado, envio inválido, saldo e corrida de débitos |
| AC-09/10/12 | `VantagemService`, vantagem/cupom, templates | `VantagemTest`: foto, custo, parceiro alheio, saldo, corrida e uso único |
| AC-11 | `NotificacaoService`, SMTP | `NotificacaoSmtpTest`: servidor SMTP local capturando texto real, falha e nova tentativa |
| AC-14 | `docs/apresentacao/`, `docs/tutorial.md` | Exportação/render de slides e roteiro. Ensaio pelo aluno |
| AC-15 | README, `mvnw`, Git | `./mvnw clean verify`, clone local isolado, intervalos reais no log |

Mutação planejada: retirar a checagem de dono do cadastro, retirar validação de papel no serviço, permitir uso de cupom alheio e desabilitar CSRF. Testes focados devem falhar em ambiente isolado. Restaurar arquivos e repetir a suíte antes do commit.

## Operação e reversão

Aplicação usa porta 8080. SMTP local na 1025, sem entrega externa por padrão. Arquivos de dados, senhas SMTP e material de teste ficam fora do Git. Para atualizar: parar aplicação, copiar `dados/`, atualizar jar e iniciar. Migração V1 é instalação inicial; mudanças futuras ganham novas versões. Reversão usa jar e backup compatíveis, nunca edita uma migração já aplicada. Banco não deve ser aberto simultaneamente por duas instâncias. Publicação real exige HTTPS, credenciais SMTP e controle de backup. Não há deploy nesta entrega.
