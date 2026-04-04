# 🏭 Sistema MES (Manufacturing Execution System) - API

## 📖 Sobre o Projeto
Esta é uma API desenvolvida para o gerenciamento e controle de chão de fábrica. O sistema permite o acompanhamento do ciclo de vida das ordens de produção, desde o abastecimento de insumos até a aprovação final e controle de qualidade, garantindo a rastreabilidade e eficiência industrial.

## 🚀 Tecnologias Utilizadas
* **Linguagem:** Java
* **Framework:** Spring Boot
* **Arquitetura:** Monolito Modular
* **Documentação:** Swagger (OpenAPI 3.1)

---

## ⚙️ Endpoints da Aplicação

Abaixo estão as rotas principais documentadas no módulo de **Produção**:

### 📦 Programação (programacao-controller)

| Verbo HTTP | Endpoint | Descrição                                                    |
| :--- | :--- |:-------------------------------------------------------------|
| `GET` | `/programacao` | Lista todas as programações existentes.                      |
| `GET` | `/programacao/{id}` | Busca os detalhes de uma programação específica pelo ID.     |
| `POST` | `/programacao/save` | Cria uma nova programação no sistema.                        |
| `PUT` | `/programacao/{id}/{idTroca}/sequencia` | Atualiza a sequência fila da programação.                    |
| `PATCH` | `/programacao/{id}/abastecer` | Registra o abastecimento de insumos para a programação.      |
| `PATCH` | `/programacao/{id}/produzir` | Altera o status da programação indicando início da produção. |
| `PATCH` | `/programacao/{id}/qualidade` | Envia a produção para análise de qualidade.                  |
| `PATCH` | `/programacao/{id}/aprovar` | Aprova a programação após o fluxo de produção/qualidade.     |
| `DELETE`| `/programacao/{id}/deletar` | Remove ou inativa uma programação do sistema.                |

> **Nota para o Recrutador/Desenvolvedor:**  O fluxo de status de uma programação geralmente segue a ordem: **Criar ➔ Abastecer ➔ Produzir ➔ Qualidade ➔ Aprovar**.

### 📋 Ordem de Produção (ordem-producao-controller)

| Verbo HTTP | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/ordem_producao` | Lista todas as ordens de produção ativas. |
| `GET` | `/ordem_producao/{id}` | Busca os detalhes de uma ordem de produção específica. |
| `GET` | `/ordem_producao/{id}/lotes` | Lista os lotes associados a uma ordem de produção. |
| `POST` | `/ordem_producao/save` | Cria uma nova ordem de produção. |
| `PATCH` | `/ordem_producao/{idLote}/{idProd}` | Atualiza dados específicos de um lote dentro da ordem. |
| `DELETE`| `/ordem_producao/{idLote}/{idProd}` | Remove a associação de um lote específico da ordem. |
| `DELETE`| `/ordem_producao/{idProd}` | Exclui uma ordem de produção do sistema. |
> **Nota para o Recrutador/Desenvolvedor:**  O fluxo de status de uma programação geralmente segue a ordem: **Iniciada ➔ Processando ➔ Finalizada**.
>
### 🏷️ Lote (lote-controller)

| Verbo HTTP | Endpoint | Descrição                                                                           |
| :--- | :--- |:------------------------------------------------------------------------------------|
| `GET` | `/lote` | Lista todos os lotes cadastrados no sistema.                                        |
| `GET` | `/lote/{id}` | Busca os detalhes de um lote específico pelo ID.                                    |
| `GET` | `/lote/sem-op` | Lista os lotes que ainda não estão vinculados a uma Ordem de Produção (sem OP).     |
| `POST` | `/lote/save` | Cria um novo lote no sistema.                                                       |
| `PATCH` | `/lote/{id}/desabastecer` | Atualiza o status de um lote específico para desabastecido e retira da programação. |
| `DELETE` | `/lote/{id}` | Remove um lote do sistema.                                                          |

---
### ⚙️ Equipamento (equipamento-controller)

| Verbo HTTP | Endpoint | Descrição |
|:-----------| :--- | :--- |
| `GET`      | `/equipamento` | Lista todos os equipamentos cadastrados no sistema. |
| `GET`      | `/equipamento/{id}` | Busca os detalhes de um equipamento específico pelo ID. |
| `POST`     | `/equipamento/salvar` | Cadastra um novo equipamento no sistema. |
| `DELETE`   | `/equipamento/{id}/remover` | Remove permanentemente um equipamento do sistema. |
| `PATCH`    | `/equipamento/{id}/desativar` | Altera o status do equipamento para inativo (exclusão lógica). |
## 🛠️ Como executar o projeto localmente

> ⚠️ **ATENÇÃO AVALIADOR / RECRUTADOR: É OBRIGATÓRIO SUBIR O DOCKER PRIMEIRO!** ⚠️
> Este projeto utiliza o **Keycloak** para gestão de segurança. Se você iniciar o Spring Boot antes de subir o contêiner do Keycloak, a aplicação vai falhar na inicialização.

1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/LucasMathews1995/MES-BackEnd.git](https://github.com/LucasMathews1995/MES-BackEnd.git)


