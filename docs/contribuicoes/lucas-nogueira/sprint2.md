# Contribuições — Lucas Nogueira

## Sprint 2 — Lab03S02

### Contribuição

Nesta sprint continuei como único integrante. Revisei os modelos anteriores e preparei o modelo entidade-relacionamento com chaves primárias, referências e restrições. Detalhei a arquitetura no diagrama de componentes, separando as páginas, os controllers, os serviços, a persistência e a integração de email. A revisão foi técnica e documental, pois não havia correções específicas do professor para este laboratório nos materiais disponíveis.

Implementei os CRUDs de aluno e empresa parceira em Java 21 com Spring Boot, Spring MVC, Thymeleaf, Spring Data JPA e Bean Validation. O aluno informa nome, email, CPF, RG, endereço, curso e instituição. A empresa informa seus dados de cadastro e acesso. As telas permitem cadastrar, consultar, editar e desativar a própria conta, com validação dos campos obrigatórios e das duplicidades de CPF e email.

Organizei a aplicação em entidades, DTOs de formulário, repositórios, serviços, controllers e templates, seguindo a divisão MVC utilizada no Projeto 2. Os serviços concentram as regras de cadastro e as transações; os controllers recebem as requisições e escolhem as páginas; e os DTOs delimitam os campos que podem ser alterados pelo formulário, impedindo que parâmetros enviados pelo navegador modifiquem o saldo.

Também implementei login por email com Spring Security, senha armazenada como hash BCrypt e proteção CSRF. As operações de consulta e alteração usam a identidade da sessão para restringir o acesso à própria conta. A desativação impede novos acessos e preserva os registros necessários ao histórico do sistema.

### Decisões

Mantive páginas renderizadas pelo servidor com Thymeleaf para demonstrar o percurso completo na mesma aplicação Java. Escolhi JPA/Hibernate para o mapeamento das entidades e Flyway para controlar a criação do banco. A hierarquia de usuários usa a estratégia `JOINED`, com os dados comuns em `Usuario` e os dados específicos nas tabelas de cada perfil.

Usei H2 em arquivo para manter a execução local simples e preservar os dados após reiniciar a aplicação. Essa escolha atende à necessidade de persistência desta entrega e permite verificar a continuidade dos cadastros sem instalar um servidor de banco separado.

### Verificação

Preparei sete testes para o marco `sprint2`, cobrindo os CRUDs, validações, autenticação, tentativa de acesso a outra conta, proteção CSRF e persistência em arquivo. A etapa foi compilada e testada em uma cópia contendo apenas os arquivos daquele marco, com os sete testes aprovados.

### Uso do Codex

Usei o Codex, da OpenAI, como apoio na modelagem do banco, implementação da aplicação Spring, criação das páginas e escrita e execução dos testes. Mantive as alterações em commits com assuntos separados para registrar a evolução da persistência e dos cadastros.
