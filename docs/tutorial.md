# Tutorial de tecnologias — roteiro de 20 minutos

Material para Lucas Nogueira apresentar no salão de tecnologias. O roteiro foi preparado nesta execução; o ensaio com cronômetro e a apresentação em aula devem ser realizados pelo aluno.

Slides: [PowerPoint editável](apresentacao/tutorial-tecnologias.pptx) e [PDF](apresentacao/tutorial-tecnologias.pdf). Arquitetura: [PowerPoint](apresentacao/arquitetura.pptx) e [PDF](apresentacao/arquitetura.pdf).

## Preparação antes da aula

1. Instalar JDK 21 e confirmar `java -version`. Definir `JAVA_HOME` se necessário. No macOS desta execução foi utilizado `/opt/homebrew/opt/openjdk@21`.
2. Na raiz do projeto, executar `./mvnw clean verify` antes da apresentação, para baixar dependências e verificar o pacote.
3. Abrir `python3 scripts/smtp_local.py` em um terminal. Esse capturador não entrega mensagens externas.
4. Abrir `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo` em outro terminal.
5. Abrir `http://localhost:8080` e `http://localhost:8025`. Não executar um segundo processo contra o mesmo arquivo H2.
6. Separar um arquivo PNG/JPEG de até 2 MB para a foto da vantagem. Usar somente dados fictícios nos cadastros.

## Sequência cronometrada

| Tempo | Slides | Ação e explicação |
|---|---|---|
| 0:00–2:00 | 1–2 | Apresentar objetivo e MVC. Seguir o caminho navegador, Security, controller, service e repository. Explicar a escolha de um único projeto com páginas no servidor. |
| 2:00–4:00 | 3 | Abrir `Aluno.java`, `UsuarioRepository.java` e `V1__cria_estrutura.sql`. Relacionar `@Entity`, PK/FK, JOINED, ORM e migrações. Mostrar que H2 fica em arquivo. |
| 4:00–6:00 | 4 | Abrir `SegurancaConfig.java`, `AlunoForm.java` e o formulário. Explicar hash BCrypt, sessão, CSRF e validação, usando os nomes reais das classes. |
| 6:00–8:00 | 5 | Mostrar os dois terminais e as portas. Explicar a caixa SMTP local. Não gastar esse período baixando dependências. |
| 8:00–12:00 | 6 | Cadastrar aluno, testar entrada inválida, corrigir e entrar. Consultar e editar a conta. Repetir com parceiro. Remover uma conta descartável para mostrar o fim do acesso. |
| 12:00–16:00 | 7 | Professor envia 120 moedas com motivo. Parceiro cadastra vantagem por 40. Aluno resgata, consulta saldo de 80 e cupom. Comparar o mesmo UUID nos dois emails. Parceiro confirma uso. |
| 16:00–18:00 | 8 | Explicar os problemas reais encontrados: lock tardio em JOINED e expressão de reflexão no template. Mostrar os testes que detectaram esses problemas. |
| 18:00–20:00 | 9 | Mostrar resultado da suíte e uma asserção de acesso negado. Responder onde está a regra de saldo, o que ocorre sem SMTP e como o cupom evita reutilização. |

## Credenciais e dados fictícios

O perfil `demo` cria somente `professor@demo.local` / `Professor123!` e uma instituição fictícia. Criar aluno e parceiro pela interface. Exemplo de CPF apenas para teste: `52998224725`. Usar email com domínio `demo.local`, que não será entregue à internet pelo capturador.

Na verificação desta execução foram criados `ana@demo.local` / `Aluno123!` e `parceiro@demo.local` / `Parceiro123!` no banco local. Esses cadastros não fazem parte do seed de um clone novo. O aluno recebeu 120, resgatou 40 e ficou com 80. Para repetir o cálculo em banco já usado, criar um novo aluno ou considerar o saldo existente.

## Perguntas para ensaiar

- Por que `CadastroController` não acessa SQL nem altera saldo?
- Qual a diferença entre o diagrama de classes e o ER?
- Por que o saldo deve ser lido depois de obter o bloqueio?
- Como a aplicação preserva o histórico após remover uma conta?
- Por que o email pode ser repetido em uma falha ambígua do SMTP, mas o débito não?
- Quais mudanças de código seriam necessárias para outro banco ou calendário semestral?

Referências técnicas para estudo: [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html), [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/), [Spring Security](https://docs.spring.io/spring-security/reference/), [Thymeleaf](https://www.thymeleaf.org/documentation.html) e [Flyway](https://documentation.red-gate.com/fd).
