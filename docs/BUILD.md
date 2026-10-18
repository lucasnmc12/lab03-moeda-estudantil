# Verificação da Release 1

## Estado

**TECHNICALLY_COMPLETE para demonstração local.** Os modelos e a implementação cobrem o escopo da Release 1, com 25 testes aprovados e materiais de apresentação preparados. Um clone limpo do GitHub, no commit `7f516e8`, também passou no build e nos 25 testes.

Permanecem externos: acesso do professor ao repositório privado, feedback das apresentações, ensaio humano cronometrado, apresentação em aula e entrega no Canvas. SMTP foi verificado com servidor local de captura; entrega a provedores externos depende de configuração e não foi alegada como testada.

## Rastreabilidade

| Critério | Implementação | Evidência |
|---|---|---|
| AC-01 | Casos de uso, histórias, classes e componentes em `docs/diagramas` | PlantUML renderizado, imagens revisadas, versões e tags preservadas |
| AC-02/03 | CadastroService, DTOs, CadastroController e templates de contas | CadastroMvcTest: os dois CRUDs, inválidos, instituição e duplicidade |
| AC-04 | SegurancaConfig e AcessoService | CadastroAcessoTest e SegurancaTest: login real dos três perfis, senha errada, papel, dono, conta desativada e CSRF |
| AC-05 | Entidades JOINED, repositories e migração Flyway V1 | PersistenciaTest: H2 em arquivo, CHECK de saldo, FK, email único e reabertura |
| AC-06 | CreditoSemestral e rotina de concessão | MoedaServiceTest: mesmo semestre não repete e saldo restante acumula no próximo |
| AC-07 | MoedaService e bloqueio anterior à leitura | MoedaServiceTest: débito/crédito, motivo, inválidos, rollback e dois envios simultâneos |
| AC-08 | Consulta própria e template do extrato | MoedaServiceTest: isolamento e renderização com lançamentos reais |
| AC-09 | VantagemService, DTO e formulário multipart | VantagemTest: foto PNG válida, arquivo falso, ausência de foto, custo e acesso cruzado |
| AC-10 | Resgate transacional e TrocaVantagem | VantagemTest: débito, saldo insuficiente, oferta retirada e dois resgates simultâneos |
| AC-11 | Notificacao e worker SMTP | NotificacaoSmtpTest: captura real via socket local, mesmo código nos dois emails, falha, nova tentativa e ausência de novo débito |
| AC-12 | Consulta por perfil e confirmação única do cupom | VantagemTest: parceiro alheio, perfil indevido, reutilização e histórico após remoção |
| AC-13 | Telas MVC e mensagens junto aos campos | Testes MVC com Thymeleaf real e navegação no navegador com contas fictícias |
| AC-14 | Dois decks editáveis, PDFs e roteiro de 20 minutos | Nove slides em cada deck, tabelas/diagramas editáveis, renderização e inspeção visual. Ensaio humano pendente |
| AC-15 | README, Maven Wrapper e commits com horários reais | Build limpo aprovado. Histórico e verificação de clone conforme registros abaixo |

## Comandos executados

- `./mvnw clean verify`, com `JAVA_HOME` apontando para JDK 21: **25 testes, zero falhas, zero erros, zero ignorados**. A execução precisa permitir sockets em loopback para os testes SMTP.
- Testes contra o schema Flyway real e o ORM real. O SMTP é um servidor de teste local, não um mock do método de envio.
- `plantuml -tpng docs/diagramas/*.puml`: fontes renderizadas para PNG. Os modelos correntes e as imagens foram conferidos.
- `python3 -m py_compile scripts/smtp_local.py`: sintaxe aprovada e caixa de email conferida no navegador com os textos decodificados.
- `env -u JAVA_HOME ./mvnw -v` e `sh -n mvnw`: wrapper encontra o JDK 21 do Homebrew sem exigir alteração do ambiente global.
- `git diff --check`: sem erros após normalização do CSS.
- As etapas de persistência, CRUDs e moedas também foram compiladas/testadas em cópias com apenas os arquivos do respectivo marco, evitando depender de código futuro ainda não commitado.

Os totais por suíte estão em `docs/evidencias/testes.json`. Logs transitórios e bancos de teste ficam fora do controle de versão.

## Mutações de segurança

Seis alterações temporárias em uma cópia isolada, sem dados reais ou deploy, produziram falha nos testes focados. Os arquivos da cópia foram restaurados e comparados byte a byte com o original. A suíte restaurada passou. O código principal não recebeu essas mutações.

| Alteração temporária | Teste que detectou |
|---|---|
| Retirar correspondência entre ID da URL e conta da sessão | CadastroAcessoTest.contaAlheiaEPapelIncorretoSaoNegados |
| Retirar negação explícita de papel no service | SegurancaTest.rotasPrivadasPapeisECsrfSaoProtegidos |
| Retirar dono do cupom | VantagemTest.somenteDonoConfirmaCupomUmaVezEHistoricoSobreviveRemocao |
| Desabilitar CSRF | CadastroAcessoTest.loginRealECsrfProtegemCadastro |
| Permitir conta inativa na consulta da sessão | SegurancaTest.contaDesativadaNaoEntraNemUsaSessaoAnterior |
| Retirar dono da vantagem | VantagemTest.rejeitaFotoFalsaCustoInvalidoEVantagemAlheia |

Limite da interpretação: retirar a checagem de ID da conta perde o 404 esperado, mas a consulta continua partindo da sessão própria. Retirar o papel explícito causa erro de conversão de tipo em vez de 403; o tipo Java continua impedindo aquela operação. Esses dois testes detectam a quebra do contrato de negação, sem afirmar que todos os demais mecanismos de isolamento foram removidos. As mutações de cupom/vantagem, conta ativa e CSRF exercitam os respectivos caminhos negados.

[Resultados das mutações](evidencias/mutacoes.json).

## Navegação e defeitos corrigidos

No navegador foram criados aluno e parceiro fictícios. O professor demonstrativo enviou 120 moedas com motivo. A aluna consultou o extrato, resgatou uma vantagem de 40, ficou com 80 e recebeu um código. Os emails do aluno e do parceiro apresentaram o mesmo código na caixa SMTP local. O parceiro confirmou a utilização, e o botão de confirmação desapareceu. O saldo foi preservado após reiniciar a aplicação.

O teste concorrente detectou leitura de saldo anterior ao bloqueio em JOINED. A correção usa lock SQL na linha base antes de atualizar a entidade. A primeira navegação com extrato preenchido detectou uma expressão inválida de reflexão no template. Um teste de regressão reproduziu a falha, e a propriedade `tipo` substituiu a reflexão.

Capturas da execução: `docs/evidencias/painel-professor.png`, `extrato-aluno.png`, `cupom-aluno.png` e `cupom-utilizado.png`. Todos os registros são demonstrativos. A versão final do cadastro também foi conferida com CPF inválido e erro junto ao campo, em `docs/evidencias/validacao-cadastro.png`.

## Materiais de apresentação

Os dois PPTX passaram pela validação de integridade, geometria, fonte Arial e reimportação. As tabelas e diagramas permanecem editáveis. Cada um dos dezoito slides foi renderizado e inspecionado. Os PDFs reproduzem os slides para apresentação/consulta; a edição é feita nos PPTX. Não foi alegado teste no aplicativo PowerPoint.

## Operação e limites

H2 em arquivo atende à demonstração local e não foi validado para múltiplas instâncias. O calendário inicial usa os semestres civis. Remoção lógica preserva histórico. Emails têm entrega pelo menos uma vez: falha após aceitação SMTP pode repetir uma mensagem, mantendo o mesmo cupom e sem novo débito. O remetente e as credenciais SMTP precisam ser configurados para um provedor externo. Publicação exige HTTPS e operação de backups.

Não houve feedback específico deste laboratório fornecido pelo professor, participação em aula simulada ou alteração artificial dos horários de commit. As tags das sprints identificam marcos desta execução.

## Clone limpo

`gh repo clone lucasnmc12/lab03-moeda-estudantil` em diretório temporário vazio, seguido de `./mvnw clean verify`, com JDK 21. O Maven Wrapper baixou o Maven nessa cópia, sem depender dos arquivos ignorados da pasta de trabalho. Resultado: **25 testes, zero falhas, zero erros, zero ignorados**, pacote executável gerado. Commit verificado: `7f516e8bdf247592d1eadc3a14149f588863964b`. O commit final posterior consolida documentação, apresentações e reconhecimento opcional do JDK 21 do Homebrew pelo wrapper, preservando um JAVA_HOME já definido. O código da aplicação permanece o mesmo. [Registro](evidencias/clone-limpo.json).
