# Contribuição — Lucas Nogueira — Sprint 2

Material preparado com apoio do Codex, da OpenAI, nesta execução. A implementação escolhe Spring Data JPA/Hibernate, com migração Flyway e H2 persistente, mantendo Java/Spring MVC do laboratório anterior.

O ER declara chaves, referências e restrições. A versão inicial conecta cadastros de aluno e empresa à interface Thymeleaf. Além de criar e consultar, já permite editar e desativar a própria conta. CPF, email, instituição e campos obrigatórios recebem validação; DTOs impedem alterar saldo por parâmetros do formulário.

Os sete testes do marco `sprint2` verificam os CRUDs, autenticação, acesso cruzado, CSRF e persistência em arquivo. Essa etapa foi compilada e testada em uma cópia contendo apenas seus arquivos, antes do commit. Nenhum feedback específico das apresentações foi fornecido. Não se registra aprovação ou participação em aula.
