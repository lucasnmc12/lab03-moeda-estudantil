# Contribuições — Lucas Nogueira

## Sprint 1 — Lab03S01

### Contribuição

Como estou desenvolvendo o Laboratório 03 sozinho, fiquei responsável por todos os artefatos desta sprint. Organizei o enunciado do Sistema de Moeda Estudantil em oito casos de uso, cobrindo autenticação, cadastros de aluno e empresa parceira, envio de moedas, consulta de extrato, manutenção de vantagens, resgate e conferência de cupom. Modelei Aluno, Professor e Empresa Parceira como especializações de Usuário, mantendo os papéis de cada um explícitos.

Escrevi as histórias de usuário com critérios de aceitação para as regras que afetam os fluxos: o professor precisa informar o motivo do reconhecimento e ter saldo para enviar moedas; o aluno só pode resgatar uma vantagem quando possui saldo suficiente; e a empresa só pode administrar suas próprias vantagens e confirmar os cupons associados a elas. Também registrei o crédito de mil moedas por semestre para cada professor, com acumulação do saldo não utilizado.

No diagrama de classes, separei `EnvioMoeda`, `TrocaVantagem` e `CreditoSemestral` como especializações de `Transacao`, pois representam movimentações com origens e regras diferentes. Modelei a vantagem vinculada à empresa parceira, o cupom associado ao resgate e a notificação persistente necessária para acompanhar o envio dos emails.

Preparei os modelos em PlantUML e exportei suas imagens para consulta no repositório. Preservei uma cópia das fontes iniciais e o marco Git `sprint1`, permitindo comparar a modelagem desta etapa com a versão consolidada nas sprints seguintes.

### Decisões e revisão

Aproveitei a revisão do Projeto 2 para ligar o cadastro diretamente aos perfis do sistema, sem acrescentar um ator intermediário. Mantive autenticação como condição de acesso às funções privadas e representei validação de saldo, crédito semestral e envio de email nos critérios das histórias e no modelo de classes, porque são regras e efeitos das operações.

Mantive professores e instituições previamente cadastrados, conforme o enunciado, e reservei o cadastro público para alunos e empresas parceiras. A revisão considerou o enunciado e as lições dos laboratórios anteriores. Não havia feedback específico do professor para este laboratório entre os materiais disponíveis.

### Uso do Codex

Usei o Codex, da OpenAI, como apoio para organizar os requisitos, escrever as histórias, elaborar os arquivos PlantUML e verificar a consistência entre casos de uso e classes. A ferramenta também auxiliou na exportação das imagens e na preservação das versões dos modelos.
