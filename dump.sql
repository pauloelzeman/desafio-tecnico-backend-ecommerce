-- Dump : Desafio técnico back-end pleno
-- Compatível com MySQL 8.0+

CREATE DATABASE IF NOT EXISTS desafio_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE desafio_db;

-- ==========================================
-- TABELA: USERS
-- ==========================================
CREATE TABLE IF NOT EXISTS users (
                                     id BINARY(16) NOT NULL PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    ) ENGINE=InnoDB;

-- ==========================================
-- TABELA: PRODUCTS
-- ==========================================
CREATE TABLE IF NOT EXISTS products (
                                        id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(19,2) NOT NULL,
    category VARCHAR(255),
    quantity INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    ) ENGINE=InnoDB;

-- Índices para performance
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_name ON products(name);

-- ==========================================
-- TABELA: ORDERS
-- ==========================================
CREATE TABLE IF NOT EXISTS orders (
                                      id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    total DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    status ENUM('PENDENTE', 'PAGO', 'CANCELADO') NOT NULL DEFAULT 'PENDENTE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
    ) ENGINE=InnoDB;

-- Índices úteis para queries analíticas
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

-- ==========================================
-- TABELA: ORDER_ITEMS
-- ==========================================
CREATE TABLE IF NOT EXISTS orders_items (
                                            id BINARY(16) NOT NULL PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price_at_purchase DECIMAL(19,2) NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_order_items_product FOREIGN KEY (product_id) REFERENCES products(id)
    ) ENGINE=InnoDB;

CREATE INDEX idx_order_items_order_id ON orders_items(order_id);
CREATE INDEX idx_order_items_product_id ON orders_items(product_id);

-- ==========================================
-- INSERÇÕES DE EXEMPLO
-- ==========================================

-- Observação: substitua as senhas por hashes BCrypt reais ao popular em produção.
INSERT INTO users (id, username, password, role)
VALUES
-- Senha padrão 123456
(UUID_TO_BIN(UUID()), 'admin', '$2a$10$BRUS7cteAHnt4nHALSJuVeKU4bqbM2tr2iPBPoiSSYJyI/MlQHXCm', 'ROLE_ADMIN'),
(UUID_TO_BIN(UUID()), 'user', '$2a$10$BRUS7cteAHnt4nHALSJuVeKU4bqbM2tr2iPBPoiSSYJyI/MlQHXCm', 'ROLE_USER');

-- Produtos iniciais (exemplo)
INSERT INTO products (id, name, description, price, category, quantity)
VALUES
    (UUID_TO_BIN(UUID()), 'Camiseta Azul', 'Camiseta 100% algodão', 49.90, 'Roupas', 100),
    (UUID_TO_BIN(UUID()), 'Tênis Preto', 'Tênis esportivo leve', 199.90, 'Calçados', 50),
    (UUID_TO_BIN(UUID()), 'Calça Jeans', 'Modelo slim fit', 129.90, 'Roupas', 75);

-- ==========================================
-- Exemplo de Pedido
-- ==========================================
INSERT INTO orders (id, user_id, total, status)
VALUES (UUID_TO_BIN(UUID()),
        (SELECT id FROM users WHERE username = 'user' LIMIT 1),
    0.00, 'PENDENTE'
    );

-- ==========================================
-- Observações finais
-- ==========================================
-- Use UUID_TO_BIN() para gerar IDs binários em inserts.
-- As queries analíticas exigidas pelo desafio podem ser executadas
-- com base nessas tabelas e índices.
-- O estoque é atualizado pela aplicação após o pagamento.
-- Exemplos de inserções: as senhas são espaços reservados — gere o bcrypt no aplicativo ou substitua-o por hashes.
-- Para criar um usuário administrador por meio do aplicativo, registre-o e atualize a função no banco de dados ou insira um usuário com um hash bcrypt.
-- Exemplos de UUIDs (substitua pelas funções UUID_TO_BIN(...) se desejar)
-- Você pode usar seu cliente MySQL para substituir UUIDs legíveis por humanos por binários, se necessário.

