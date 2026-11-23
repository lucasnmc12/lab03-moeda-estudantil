# Contribuições — Lucas Nogueira — Lab05S01

## Primeira etapa: Diagrama de Comunicação

Modelei a troca de vantagem com QR Code a partir do resgate já implementado na Release 2. Usei objetos conectados e mensagens numeradas, preservando os nomes reais do controller, serviços, entidades e repositórios. Separei a transação do resgate da entrega assíncrona dos emails.

Identifiquei explicitamente as partes planejadas: acesso ao código da troca pelo worker, gerador de PNG e email com anexo. O QR Code deve representar o mesmo UUID já salvo no cupom; não criei um identificador paralelo. Registrei os principais erros e o comportamento esperado para tentativas de entrega.

Preparei a fonte PlantUML, as imagens SVG e PNG e uma explicação com referências ao código. Conferi a sintaxe, a renderização e a legibilidade do diagrama. Nesta etapa não implementei QR Code, Diagrama de Implantação ou publicação em nuvem.

Sou o único integrante informado. Usei apoio do Codex/OpenAI na análise e elaboração dos artefatos. O commit registra o horário real da execução; não representa comprovação de presença ou avaliação do professor.
