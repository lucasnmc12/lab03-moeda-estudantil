Pretende-se desenvolver um sistema para estimular o reconhecimento do mérito estudantil por meio de uma moeda virtual. Essa moeda pode ser distribuída por professores aos seus alunos e trocada pelos alunos por produtos e descontos em empresas parceiras. Após a análise inicial de requisitos do sistema, foram levantadas as seguintes informações.
Os alunos que desejam ingressar no sistema de mérito devem realizar um cadastro, indicando nome, email, CPF, RG, endereço, instituição de ensino e curso. As instituições participantes já estão pré-cadastradas no sistema, para que o aluno selecione a sua.
Os professores já estarão pré-cadastrados no sistema, a partir da lista enviada pela instituição no momento da parceria. Cada professor está vinculado a uma instituição participante, e o sistema armazena seu nome, CPF e departamento.
A cada semestre, cada professor recebe um total de 1.000 moedas para distribuir aos seus alunos, como forma de reconhecimento por bom comportamento, participação em aula, entre outros critérios. Esse saldo é acumulável entre semestres: caso o professor não distribua todas as moedas recebidas em um semestre, as moedas restantes permanecem em seu saldo, somando-se às 1.000 novas moedas do semestre seguinte.
Para enviar moedas, o professor deve possuir saldo suficiente, indicando qual aluno deverá receber o montante, bem como o motivo pelo qual ele está sendo reconhecido (uma mensagem aberta, de preenchimento obrigatório).
Ao receber uma moeda, o aluno deve ser notificado por email.
Professores e alunos devem ser capazes de consultar o extrato de sua conta, visualizando o total de moedas que ainda possuem, bem como as transações que realizaram (para o professor, o envio de moedas; para o aluno, o recebimento ou a troca de moedas).
Para trocar moedas, o aluno deve selecionar uma das vantagens cadastradas no sistema. Elas incluem, por exemplo, desconto em restaurantes da universidade, desconto de mensalidade ou compra de materiais específicos.
Empresas que desejam realizar parceria também devem se cadastrar no sistema, incluindo as vantagens que desejam oferecer e o custo de cada uma delas, em moedas.
Para cadastrar uma vantagem, a empresa parceira deve adicionar também uma descrição e uma foto do produto.
Ao resgatar uma vantagem, o aluno deve ter o valor correspondente descontado do seu saldo. Um email de cupom deve ser enviado a ele para uso na troca presencial, e um email equivalente deve ser enviado ao parceiro, para que possa conferir a troca. Ambos os emails devem incluir o mesmo código, gerado pelo sistema, a fim de facilitar a conferência.
Por fim, alunos, professores e empresas parceiras precisam ter um login e uma senha cadastrados para acessar o sistema. Em todos os casos, um processo de autenticação é necessário para a realização dos requisitos.
Ao final da última sprint (Sprint 03), os alunos deverão apresentar o protótipo produzido para a primeira release, comparando o resultado com os modelos descritos inicialmente, bem como apresentando as modificações inseridas para o funcionamento adequado do software, conforme a especificação anterior. Além disso, ao final da Sprint 03, cada grupo deverá apresentar um tutorial das tecnologias escolhidas para desenvolver o sistema, em um salão de tecnologias durante a aula. Cada apresentação deve ter cerca de 20 minutos de duração, e faz parte da especificação da própria Sprint 03.
O sistema deverá ser desenvolvido utilizando arquitetura MVC, atendendo aos requisitos apresentados e cumprindo a modelagem produzida nas sprints iniciais do projeto. O repositório GitHub deve estar atualizado, com todas as versões produzidas dos modelos UML e o código final desenvolvido. A avaliação final do projeto levará em consideração os seguintes aspectos:
- Qualidade do sistema produzido (adequação aos requisitos apresentados);
- Alinhamento entre modelo (de classes e de arquitetura) e código;
- Atualizações dos modelos conforme necessidade do projeto.
Lab03S01 (6 pontos)
- Modelagem do sistema: Diagrama de Casos de Uso, Histórias do Usuário, Diagrama de Classes e Diagrama de Componentes.
Lab03S02 (7 pontos)
- Modelo Entidade-Relacionamento (ER).
- Definição e implementação da estratégia de acesso ao banco de dados (ORM, padrão DAO, entre outros).
- CRUDs de aluno e de empresa parceira, versão inicial: front-end e comunicação com o back-end.
Lab03S03 (7 pontos)
- CRUDs de aluno e de empresa parceira, versão final.
- Apresentação da arquitetura do sistema e da camada de persistência, com slides preparados previamente.
- Tutorial das tecnologias escolhidas pelo grupo (cerca de 20 minutos), no salão de tecnologias da aula.
Para este projeto, utilize o template de README.md disponível em [https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/blob/main/TEMPLATES/template_README.mdLinks to an external site.](https://github.com/joaopauloaramuni/laboratorio-de-desenvolvimento-de-software/blob/main/TEMPLATES/template_README.md).
Prazo final: consulte o cronograma da disciplina disponibilizado no Canvas.
Valor total: 20 pontos   |   Desconto de 1,0 ponto por dia de atraso
Semanalmente, todos os grupos deverão apresentar o andamento das entregas durante a aula. A não participação do grupo implicará na perda automática de 50% dos pontos da sprint.
Em conformidade com a política de uso responsável de Inteligência Artificial da disciplina, informa-se que este documento (enunciado do Laboratório 03) foi produzido com apoio da ferramenta Claude, da empresa Anthropic, utilizada na geração e revisão de texto. O conteúdo foi revisado pelo professor antes da disponibilização aos alunos.