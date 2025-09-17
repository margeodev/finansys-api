CREATE SCHEMA `finansys_db` ;
USE finansys_db;

-- Tabela de usuários
CREATE TABLE `tb_user` (
  `id_user` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(150) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `phone_number` VARCHAR(15) NOT NULL UNIQUE,
  `email` VARCHAR(150) NOT NULL UNIQUE,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_active` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id_user`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


-- Tabela de categorias
CREATE TABLE `tb_category` (
  `id_category` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME,
  `is_active` CHAR(1) NOT NULL,
  PRIMARY KEY (`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Tabela de despesas
CREATE TABLE `tb_expense` (
  `id_expense` BIGINT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) NOT NULL,
  `id_user` BIGINT NOT NULL,
  `id_category` BIGINT NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT NULL,
  `is_active` CHAR(1) NOT NULL DEFAULT '1',
  `is_personal` CHAR(1) DEFAULT '0',
  `is_advance_payment` CHAR(1) DEFAULT '0',
  PRIMARY KEY (`id_expense`),
  FOREIGN KEY (`id_user`) REFERENCES `tb_user`(`id_user`),
  FOREIGN KEY (`id_category`) REFERENCES `tb_category`(`id_category`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

INSERT INTO `tb_category` (`id_category`, `description`, `created_at`, `updated_at`, `is_active`) VALUES
(1, 'Moradia', NOW(), NULL, '1'),
(2, 'Supermercado', NOW(), NULL, '1'),
(3, 'Conta Consumo', NOW(), NULL, '1'),
(4, 'Transporte', NOW(), NULL, '1'),
(5, 'Lazer', NOW(), NULL, '1'),
(6, 'Saúde', NOW(), NULL, '1'),
(7, 'Bares e Rest.', NOW(), NULL, '1'),
(8, 'Manutenção Casa', NOW(), NULL, '1'),
(9, 'Padaria', NOW(), NULL, '1'),
(10, 'Farmácia', NOW(), NULL, '1'),
(11, 'Outros', NOW(), NULL, '1'),
(12, 'Pets', NOW(), NULL, '1'),
(13, 'Manutenção Carro', NOW(), NULL, '1');