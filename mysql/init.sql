CREATE DATABASE IF NOT EXISTS login;

USE login;

    -- login.usuario definition

CREATE TABLE `usuario` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `email` varchar(255) NOT NULL,
                           `nome` varchar(255) NOT NULL,
                           `senha` varchar(255) NOT NULL,
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `UK5171l57faosmj8myawaucatdw` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- login.`role` definition

CREATE TABLE `role` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UK8sewwnpamngi6b1dwaa88askk` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- login.usuario_role definition

CREATE TABLE `usuario_role` (
                                `usuario_id` bigint NOT NULL,
                                `role_id` bigint NOT NULL,
                                KEY `FKe7gfguqsiox6p89xggm8g2twf` (`role_id`),
                                KEY `FKpc2qjts6sqq4hja9f6i3hf0ep` (`usuario_id`),
                                CONSTRAINT `FKe7gfguqsiox6p89xggm8g2twf` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
                                CONSTRAINT `FKpc2qjts6sqq4hja9f6i3hf0ep` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `role` (name) VALUES
    ('ADMIN'),
    ('USER');

INSERT INTO usuario (email,nome,senha) VALUES
    ('admin123456@admin123456.com','admin','$2a$10$q4Yoxg20mSG7CO3DKbKzlO1NcPWpR9d2uK00gizx4SgGkwGdGRQTO');

INSERT INTO usuario_role (usuario_id,role_id) VALUES
    ((select max(id) from usuario where email = 'admin123456@admin123456.com'),
     (select max(id) from role where name = 'ADMIN'));