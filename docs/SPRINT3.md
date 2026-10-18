# Sprint 3 — versão final e apresentação

## Resultado funcional

Os CRUDs preservam a separação controller/service/repository da versão inicial. A interface final destaca os campos inválidos junto às mensagens e mantém feedback de sucesso, confirmação de remoção e navegação por perfil. A conta removida perde o acesso e mantém extratos e cupons no histórico.

A Release 1 também conecta o fluxo completo do enunciado: crédito semestral acumulável e sem duplicação, envio com motivo, extrato próprio, vantagem com foto, resgate com saldo suficiente, código de cupom e utilização única pelo parceiro dono. Emails persistem na mesma transação e seguem por SMTP posteriormente. O capturador local permite demonstrar a entrega sem contas ou endereços reais.

## Comparação entre modelo e código

| Modelo inicial | Implementação e motivo |
|---|---|
| Usuario com três perfis | Herança JPA JOINED preserva a hierarquia e centraliza email/hash/atividade |
| EnvioMoeda e TrocaVantagem distintos | Mantidos como especializações de Transacao, com participantes e regras separados |
| Recebimento semestral | CreditoSemestral registra cada concessão, com unicidade por professor/semestre |
| Notificações do envio e resgate | Notificacao persiste a caixa de saída, permitindo nova tentativa sem repetir a operação |
| Foto da vantagem | Binário PNG/JPEG no banco, com limite de 2 MB, em lugar de caminho externo |
| CRUD de conta | Remoção lógica preserva transações e cupons já emitidos |
| Cardinalidade da herança no ER | Relação 1 para 0..1 por especialização, conforme PK/FK compartilhada |
| Consulta de extrato | `getTipo()` fornece a informação de apresentação sem reflexão sobre classes no template |

`docs/diagramas/versoes/sprint1/` conserva os modelos iniciais. A tag `sprint2` conserva o marco dos CRUDs. O modelo corrente inclui os ajustes de implementação. As mudanças desta execução são técnicas, sem feedback oral específico recebido do professor.

## Apresentações

- Arquitetura e persistência: `docs/apresentacao/arquitetura.pptx` e PDF, nove slides.
- Tutorial de tecnologias: `docs/apresentacao/tutorial-tecnologias.pptx` e PDF, nove slides com notas de apresentação.
- Roteiro de vinte minutos e preparação: `docs/tutorial.md`.

As apresentações estão preparadas, mas a participação em aula, o ensaio humano cronometrado e a entrega no Canvas permanecem a cargo do aluno. Não foram simulados presença, aprovação ou feedback do professor.
