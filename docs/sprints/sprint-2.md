- Vencimento 12 Out por 23:59
- Pontos 7
Sistema de Moeda Estudantil (Release 1)
Este roteiro complementa o enunciado do Laboratório 03 e a Rubrica Lab03S02. Consulte o enunciado para os critérios completos de avaliação e prazos.
Nota de transparência sobre uso de IA: em conformidade com a política de uso responsável de Inteligência Artificial da disciplina, informa-se que este roteiro foi produzido com apoio da ferramenta Claude, da empresa Anthropic, utilizada na geração e revisão de texto e dos exemplos de código. O conteúdo foi revisado pelo professor antes da disponibilização aos alunos.
Nesta sprint, o grupo corrige os diagramas conforme o feedback da Sprint 1, constrói o Modelo Entidade-Relacionamento, define e implementa a estratégia de acesso ao banco de dados (ORM, padrão DAO ou outra de escolha do grupo) e implementa a versão inicial dos CRUDs de aluno e empresa parceira, com front-end e comunicação com o back-end.
A rubrica desta sprint distribui os 7,0 pontos assim:
- Sprint 1 concluída: diagramas e histórias no repositório.
- Feedback da apresentação da Sprint 1 anotado.
- Ambiente de desenvolvimento configurado (JDK, IDE, gerenciador de dependências).
- Banco de dados escolhido pelo grupo instalado ou acessível (local ou em nuvem).
O Diagrama de Classes (Sprint 1) descreve o domínio de forma orientada a objetos: classes, atributos, métodos e relacionamentos. O Modelo Entidade-Relacionamento descreve como esses mesmos dados serão armazenados em um banco relacional: entidades viram tabelas, atributos viram colunas, e é preciso definir explicitamente chaves primárias e chaves estrangeiras para os relacionamentos.
- ORM (Object-Relational Mapping): mapeia objetos do código diretamente para tabelas do banco, sem exigir a escrita manual de SQL para operações básicas. No ecossistema Java/Spring, a opção mais comum é o Spring Data JPA (com Hibernate por trás).
- Padrão DAO (Data Access Object): uma classe ou interface dedicada a isolar o acesso a dados (consultas SQL, conexões), separando essa responsabilidade do restante da aplicação. Pode ser implementado manualmente com JDBC, mesmo sem um framework ORM completo.
O grupo pode optar por qualquer uma das duas abordagens, ou combiná-las (por exemplo, usar JPA e ainda assim isolar o acesso em uma camada de repositório). O que é avaliado é a existência de uma estratégia clara e implementada, não a técnica específica escolhida.
Anotem, em grupo, o que o professor apontou para corrigir nos diagramas ou nas histórias.
Atualizem os diagramas de casos de uso, classes e componentes conforme o feedback, e commitem a correção separadamente.
bash
```bash
git add docs/diagramas/
git commit -m "fix: corrige diagramas conforme feedback da apresentacao"
git push origin main
```
Para cada classe que representa uma entidade persistente, definam uma tabela. Exemplo de mapeamento para este sistema:
```
Tabela ALUNO
  id (PK)
  nome, email, senha, cpf, rg, endereco, curso, saldo
  instituicao_id (FK -> INSTITUICAO_ENSINO)

Tabela PROFESSOR
  id (PK)
  nome, email, senha, cpf, departamento, saldo
  instituicao_id (FK -> INSTITUICAO_ENSINO)

Tabela INSTITUICAO_ENSINO
  id (PK)
  nome

Tabela EMPRESA_PARCEIRA
  id (PK)
  nome, email, senha, cnpj

Tabela VANTAGEM
  id (PK)
  descricao, foto, custo_em_moedas
  empresa_id (FK -> EMPRESA_PARCEIRA)

Tabela ENVIO_MOEDA
  id (PK)
  data, quantidade, motivo
  professor_id (FK -> PROFESSOR)
  aluno_id (FK -> ALUNO)

Tabela TROCA_VANTAGEM
  id (PK)
  data, quantidade, codigo
  aluno_id (FK -> ALUNO)
  vantagem_id (FK -> VANTAGEM)
```
Registrem esse modelo em docs/diagramas/modelo-er.* (imagem, script SQL comentado, ou ambos).
Opção A, ORM com Spring Data JPA (exemplo de entidade):
java
```java
package br.edu.pucminas.moeda.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "aluno")
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String rg;
    private String endereco;
    private String curso;
    private int saldo;

    @ManyToOne
    @JoinColumn(name = "instituicao_id")
    private InstituicaoEnsino instituicao;

    // getters e setters
}
```
java
```java
package br.edu.pucminas.moeda.repositorio;

import br.edu.pucminas.moeda.modelo.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}
```
Com o Spring Data JPA, AlunoRepository já oferece os métodos básicos de CRUD (save, findById, findAll, deleteById) sem que o grupo precise escrever a implementação.
Opção B, padrão DAO manual (exemplo simplificado com JDBC):
java
```java
package br.edu.pucminas.moeda.dao;

import br.edu.pucminas.moeda.modelo.Aluno;
import java.sql.*;
import java.util.*;

public class AlunoDAO {
    private final Connection conexao;

    public AlunoDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void salvar(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO aluno (nome, email, cpf) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setString(1, aluno.getNome());
            stmt.setString(2, aluno.getEmail());
            stmt.setString(3, aluno.getCpf());
            stmt.executeUpdate();
        }
    }

    public List<Aluno> listarTodos() throws SQLException {
        // implementação da consulta e montagem dos objetos Aluno
        return new ArrayList<>();
    }
}
```
O grupo escolhe uma das opções (ou justifica uma alternativa) e documenta a decisão no README.
Estrutura sugerida em camadas (Controller, Service, Repository/DAO), consistente com a arquitetura MVC exigida no enunciado:
java
```java
package br.edu.pucminas.moeda.controller;

import br.edu.pucminas.moeda.modelo.Aluno;
import br.edu.pucminas.moeda.servico.AlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    public List<Aluno> listar() {
        return alunoService.listarTodos();
    }

    @PostMapping
    public Aluno criar(@RequestBody Aluno aluno) {
        return alunoService.salvar(aluno);
    }

    @PutMapping("/{id}")
    public Aluno atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {
        return alunoService.atualizar(id, aluno);
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        alunoService.remover(id);
    }
}
```
Repitam a mesma estrutura em camadas (Controller, Service, Repository/DAO) para EmpresaParceira.
Nesta sprint, o front-end pode ser simples, o importante é comprovar a comunicação com o back-end. Duas opções comuns:
- Páginas server-side com Thymeleaf (mais direto em uma arquitetura MVC tradicional em Spring).
- Uma API REST consumida por um front-end separado (React, Vue, ou até páginas HTML com fetch).
O grupo escolhe a abordagem, desde que demonstre, na apresentação, o cadastro e a listagem de aluno e de empresa parceira funcionando de ponta a ponta.
Verifiquem manualmente que é possível cadastrar e listar um aluno e uma empresa parceira pela interface, não apenas por uma ferramenta de teste de API.
Recomenda-se uma branch por funcionalidade (por exemplo, feature/crud-aluno), com Pull Request para juntar o trabalho na main, já que várias entidades e camadas são desenvolvidas em paralelo nesta sprint.
bash
```bash
git checkout main
git pull origin main
git checkout -b feature/crud-aluno

git add docs/diagramas/modelo-er.*
git commit -m "docs: adiciona modelo entidade-relacionamento"

git add src/main/java/br/edu/pucminas/moeda/modelo/Aluno.java
git commit -m "feat: cria entidade Aluno"

git add src/main/java/br/edu/pucminas/moeda/repositorio/AlunoRepository.java
git commit -m "feat: cria repositorio de Aluno"

git add src/main/java/br/edu/pucminas/moeda/controller/AlunoController.java
git commit -m "feat: implementa CRUD inicial de Aluno"

git push origin feature/crud-aluno
```
Confira o checklist da seção 6 e garanta que o grupo consegue explicar a estratégia de persistência escolhida e o motivo da escolha.
Assim como na Sprint 1, este é o critério de maior peso da rubrica. Ele avalia o histórico do repositório GitHub do grupo, verificando se os commits foram feitos durante o horário da aula (segundas-feiras, das 10:40 às 12:20).
- Façam commits por funcionalidade concluída (uma entidade, um endpoint, uma tela), não um único commit ao final da aula.
- Se usarem branches e Pull Requests, garantam que os commits dentro da branch também aconteçam durante a aula, não só o merge final.
- Mensagens de commit claras (feat: cria entidade Aluno, feat: implementa CRUD inicial de EmpresaParceira).
- git push regular, não apenas ao final do dia.
- Modelo ER que não reflete o Diagrama de Classes. Toda entidade do ER deve ser rastreável até uma classe da Sprint 1.
- Camadas misturadas. Evite lógica de acesso a dados dentro do Controller; separem Controller, Service e Repository/DAO.
- CRUD que só funciona por chamada direta à API, sem interface. A entrega desta sprint pede front-end e comunicação com o back-end, não apenas o back-end isolado.
- Deixar a escolha entre ORM e DAO indefinida. O grupo precisa decidir e documentar, mesmo que de forma simples, qual estratégia foi adotada.
- Commits concentrados fora do horário de aula. Reduz diretamente a nota do critério de maior peso da sprint.
 