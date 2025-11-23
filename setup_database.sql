-- Script para crear la base de datos NutriApp en MySQL/XAMPP
-- Ejecutar esto en phpMyAdmin o en la consola MySQL de XAMPP

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS nutriapp;
USE nutriapp;

-- Tabla de Usuarios
CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Alimentos
CREATE TABLE IF NOT EXISTS alimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    calorias_por100g INT NOT NULL,
    proteinas_por100g FLOAT NOT NULL,
    carbos_por100g FLOAT NOT NULL,
    grasas_por100g FLOAT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de Comidas
CREATE TABLE IF NOT EXISTS comida (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_de_comida VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    usuario_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabla de ComidaAlimento (relación muchos-a-muchos)
CREATE TABLE IF NOT EXISTS comida_alimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    comida_id BIGINT NOT NULL,
    alimento_id BIGINT NOT NULL,
    cantidad_en_gramos FLOAT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (comida_id) REFERENCES comida(id) ON DELETE CASCADE,
    FOREIGN KEY (alimento_id) REFERENCES alimento(id) ON DELETE CASCADE,
    UNIQUE KEY unique_comida_alimento (comida_id, alimento_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear índices para mejorar búsquedas
CREATE INDEX idx_usuario_username ON usuario(username);
CREATE INDEX idx_usuario_email ON usuario(email);
CREATE INDEX idx_comida_usuario_id ON comida(usuario_id);
CREATE INDEX idx_comida_fecha ON comida(fecha);
CREATE INDEX idx_comida_tipo ON comida(tipo_de_comida);
CREATE INDEX idx_alimento_nombre ON alimento(nombre);

-- Insertar datos de prueba (opcional)
-- Usuario de prueba
INSERT INTO usuario (full_name, username, email, password_hash) 
VALUES ('Juan Pérez', 'juanperez', 'juan@example.com', 'hashed_password_123');

-- Alimentos de prueba
INSERT INTO alimento (nombre, calorias_por100g, proteinas_por100g, carbos_por100g, grasas_por100g)
VALUES 
('Pollo', 165, 31.0, 0.0, 3.6),
('Arroz', 130, 2.7, 28.0, 0.3),
('Manzana', 52, 0.3, 14.0, 0.2),
('Salmón', 208, 20.0, 0.0, 13.0),
('Brócoli', 34, 2.8, 7.0, 0.4);

-- Comida de prueba
INSERT INTO comida (tipo_de_comida, fecha, usuario_id)
VALUES 
('Desayuno', CURDATE(), 1),
('Almuerzo', CURDATE(), 1),
('Cena', CURDATE(), 1);

-- Relaciones comida-alimento de prueba
INSERT INTO comida_alimento (comida_id, alimento_id, cantidad_en_gramos)
VALUES 
(1, 2, 150),  -- Desayuno: 150g de arroz
(1, 3, 200),  -- Desayuno: 200g de manzana
(2, 1, 250),  -- Almuerzo: 250g de pollo
(2, 2, 200),  -- Almuerzo: 200g de arroz
(3, 4, 150),  -- Cena: 150g de salmón
(3, 5, 300);  -- Cena: 300g de brócoli
