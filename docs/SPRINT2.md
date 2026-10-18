# Sprint 2 — persistência e CRUDs iniciais

## Entrega executável

ORM com Spring Data JPA/Hibernate, migração Flyway e H2 persistente. Cadastro e consulta de aluno e empresa estão conectados a páginas Thymeleaf. A versão inicial já inclui edição, desativação e validação de entrada para evitar reimplementar regras no fechamento da Sprint 3.

A listagem é privada e mostra a própria conta. O enunciado exige autenticação de todos os perfis e não define um operador com permissão para gerenciar dados pessoais alheios. Assim, não foi criado um administrador sem respaldo no requisito.

## Verificação

`CadastroMvcTest` percorre os dois CRUDs, campos inválidos, CPF inválido, instituição inexistente e duplicidade. `CadastroAcessoTest` verifica login real, senha incorreta, CSRF, perfil e acesso cruzado. `PersistenciaTest` aplica a migração em um arquivo H2, verifica constraints e reabre o banco. Os testes não usam um banco ou cadastro real.

## Evolução prevista para a Sprint 3

Conectar envio de moedas, crédito semestral, extrato, vantagens, resgate, conferência de cupons e SMTP. Refinar a interface final e documentar a comparação entre modelos e código. Preparar slides de arquitetura e tutorial.

## Feedback

Nenhum apontamento específico das apresentações deste laboratório foi fornecido. Esta revisão é técnica e baseada no enunciado. Não registra presença, aprovação ou correção oral fictícia.
