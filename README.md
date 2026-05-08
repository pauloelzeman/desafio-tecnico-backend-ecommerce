Aplicação
**Spring Boot (Java 17)** com autenticação JWT, CRUD de produtos, criação de pedidos com fluxo de pagamento e consultas analíticas otimizadas.

---

## ⚙️ Principais Funcionalidades

- **Autenticação JWT** segura com perfis `ADMIN` e `USER`.
- **CRUD completo de produtos** com controle de estoque (somente `ADMIN`).
- **Criação e Pagamento de Pedidos** (`USER`) com atualização automática de estoque.
- **Pedidos com múltiplos produtos** e atualização de estoque apenas após pagamento.
- **Cancelamento automático de pedidos** quando há falta de estoque.
- **Cálculo dinâmico** do valor total do pedido.
- **Consultas SQL Analiticas otimizadas** para relatórios de:
    - Top 5 usuários que mais compraram.
    - Ticket médio de pedidos por usuário.
    - Faturamento total mensal.

---

## 🧩 Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot** (Web, Data JPA, Security)
- **MySQL**
- **JJWT** (autenticação JWT)
- **Lombok**

---

## 🧱 Como Executar Localmente

1. Instale e inicie o **MySQL**.
2. Crie o banco de dados `desafio_db` (ou ajuste o nome no `application.properties`).
3. Configure suas credenciais:
   ```
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```
4. (Opcional) Altere `app.jwtSecret` para um valor seguro.
5. Compile e execute o projeto:

   ```bash
   mvn clean package
   java -jar target/desafio-backend-1.0.0.jar
   ```

---

## 🔑 Endpoints de Autenticação

### Registrar Usuário
`POST /api/auth/register`

**Body:**
```json
{
  "username": "userteste",
  "password": "123456"
}
```

### Login
`POST /api/auth/login`

**Body:**
```json
{
  "username": "admin",
  "password": "123456"
}
```

Ambos retornam um **token JWT** a ser usado no header:
```
Authorization: Bearer <token>
```

---

## 📦 Endpoints de Produtos (ADMIN)

| Método | Endpoint | Descrição |
|---------|-----------|------------|
| `GET` | `/api/products` | Lista todos os produtos |
| `POST` | `/api/products` | Cria um novo produto |
| `PUT` | `/api/products/{id}` | Atualiza um produto existente |
| `DELETE` | `/api/products/{id}` | Remove um produto |

**Exemplo de criação:**
```json
{
  "name": "Camiseta Vermelha",
  "description": "Camiseta 100% algodão",
  "price": 59.90,
  "category": "Roupas",
  "quantity": 200
}
```

---

## 🛒 Endpoints de Pedidos (USER)

| Método | Endpoint | Descrição |
|---------|-----------|------------|
| `POST` | `/api/orders` | Cria um novo pedido |
| `GET` | `/api/orders/my` | Lista pedidos do usuário autenticado |
| `POST` | `/api/orders/{id}/pay` | Paga o pedido (atualiza estoque ou cancela se insuficiente) |

**Exemplo de criação de pedido:**
```json
{
  "items": [
    { "productId": "795aa2c5-ba98-11f0-b511-00155ddbe096", "quantity": 2 },
    { "productId": "795aa5a8-ba98-11f0-b511-00155ddbe096", "quantity": 1 }
  ]
}
```

---

## 📊 Endpoints de Analytics

| Endpoint | Descrição |
|-----------|------------|
| `GET /api/analytics/top5-users` | Retorna os 5 usuários que mais compraram |
| `GET /api/analytics/avg-ticket` | Retorna o ticket médio dos pedidos por usuário |
| `GET /api/analytics/revenue/{year}/{month}` | Retorna o faturamento total do mês especificado |
**🔒 Todos os endpoints de Analytics exigem usuário ADMIN autenticado.**

---

## 🧪 Testes da API com Postman
**🔹 1. Obtenha o Token JWT**

**No Postman, envie:**

POST http://localhost:8080/api/auth/login
```json
{
"username": "admin",
"password": "123456"
}
```

Copie o campo token da resposta e adicione no Postman:

Vá em Authorization → Type: Bearer Token

Cole o token no campo.

---

**🔹 2. Teste os Endpoints**

Após configurar o token, teste livremente:

Produtos (/api/products) — CRUD completo.

Pedidos (/api/orders) — criação e pagamento.

Analytics (/api/analytics/...) — relatórios administrativos.

---

**🔹 3. Postman Collection**

Uma coleção Postman pode ser importada diretamente para testar todos os endpoints:

📁 Arquivo:
/postman/desafio-ecommerce-collection.json

Inclui também um ambiente Postman com:

🔹{{baseUrl}} = http://localhost:8080

🔹{{token}} = < JWT gerado >

---

## 🧠 Observações Técnicas

- As consultas analíticas usam **SQL nativo via `EntityManager`** para otimização.
- O JWT armazena o **ID do usuário** no `subject` e o papel em `role`.
- As senhas são armazenadas com **BCrypt**.
- Inclui **dump.sql** para popular o banco inicial.

---

## 📦 Entrega

- **Repositório GitHub** contendo o código-fonte e o arquivo `dump.sql`.
- Este README contém todas as instruções de configuração e uso.
