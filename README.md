# Sistema de Moeda Estudantil

Laboratório 03 de Desenvolvimento de Software — Release 1.

O sistema reconhece o mérito estudantil por meio de moedas virtuais. Professores distribuem moedas com uma justificativa, alunos consultam seu extrato e resgatam vantagens, e empresas parceiras oferecem produtos ou descontos e conferem cupons.

## Integrantes

- Lucas Nogueira

## Professor responsável

Material de referência: João Paulo Aramuni. Acesso ao repositório deve ser concedido ao professor responsável antes da avaliação.

## Repositório

[lucasnmc12/lab03-moeda-estudantil](https://github.com/lucasnmc12/lab03-moeda-estudantil) — repositório privado independente. O professor precisa receber acesso antes da avaliação.

## Histórias de usuário

[Histórias e critérios de aceitação](docs/historias-de-usuario.md).

## Diagramas

- [Casos de uso](docs/diagramas/casos-de-uso.puml)
- [Classes](docs/diagramas/diagrama-de-classes.puml)
- [Componentes](docs/diagramas/diagrama-de-componentes.puml)
- [Modelo entidade-relacionamento](docs/diagramas/modelo-er.puml)
- [Estratégia de persistência](docs/PERSISTENCIA.md)

## Tecnologias

Java 21, Spring Boot, Spring MVC, Thymeleaf, Spring Security, Spring Data JPA/Hibernate e H2 persistente em arquivo. A estratégia mantém o padrão do Laboratório 02 e acrescenta persistência entre execuções.

## Organização das entregas

- Sprint 1: casos de uso, histórias, classes e componentes.
- Sprint 2: modelo ER, estratégia de persistência e CRUDs iniciais.
- Sprint 3: CRUDs finais, fluxo de moedas e vantagens, slides de arquitetura e tutorial.

[Enunciado](docs/enunciado.md) e [roteiros originais](docs/sprints/).

## Instalação e execução

Requer JDK 21 e acesso à internet para baixar Maven/dependências na primeira execução.

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=demo
```

Abra [localhost:8080](http://localhost:8080). O perfil `demo` pré-cadastra uma instituição fictícia e o professor `professor@demo.local`, com senha `Professor123!`. Alunos e empresas criam suas próprias contas pela interface. Use esses dados apenas para demonstração local.

Os dados ficam em `dados/moeda.mv.db` e sobrevivem ao reinício. Sem o perfil `demo`, a instalação começa sem cadastros demonstrativos.

## Testes

```bash
./mvnw clean verify
```

## Créditos

Estrutura adaptada do [template indicado pela disciplina](https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/blob/main/TEMPLATES/template_README.md), preservando apenas seções aplicáveis. Desenvolvimento com apoio do Codex, da OpenAI. Não há registro de feedback específico do professor para este laboratório até esta execução.
