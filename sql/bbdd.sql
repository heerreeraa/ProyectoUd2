DROP DATABASE IF EXISTS asociacion_juegos;
CREATE DATABASE asociacion_juegos CHARACTER SET utf8mb4 COLLATE utf8mb4_spanish_ci;
USE asociacion_juegos;

CREATE TABLE asociacion (
    id INT PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL,
    cif VARCHAR(9) NOT NULL,
    direccion VARCHAR(150),
    telefono VARCHAR(15),
    email VARCHAR(100),
    fecha_apertura DATE
);

CREATE TABLE socia (
    dni VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(15),
    edad INT,
    email VARCHAR(100),
    eliminada BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT chk_edad CHECK (edad >= 0 AND edad <= 120)
);

CREATE TABLE juego (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    autores VARCHAR(150),
    descripcion TEXT,
    min_jugadores INT NOT NULL,
    max_jugadores INT NOT NULL,
    duracion INT,
    tematica VARCHAR(50),
    tipo ENUM('COMPETITIVO', 'COOPERATIVO') NOT NULL,
    ubicacion VARCHAR(50),
    estado ENUM('DISPONIBLE', 'PRESTADO', 'EN_REPARACION') NOT NULL DEFAULT 'DISPONIBLE',
    dni_propietaria VARCHAR(15) NULL,
    fecha_alta DATE NOT NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_juego_socia FOREIGN KEY (dni_propietaria) REFERENCES socia(dni),
    CONSTRAINT chk_jugadores CHECK (min_jugadores >= 1 AND min_jugadores <= max_jugadores)
);

CREATE TABLE partida (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_juego INT NOT NULL,
    fecha DATE NOT NULL,
    hora TIME NOT NULL,
    jugadores_deseados INT NOT NULL,
    eliminada BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_partida_juego FOREIGN KEY (id_juego) REFERENCES juego(id)
);

CREATE TABLE partida_jugadora (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_partida INT NOT NULL,
    dni_socia VARCHAR(15) NULL,
    nombre_invitada VARCHAR(100) NULL,
    ganadora BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_pj_partida FOREIGN KEY (id_partida) REFERENCES partida(id) ON DELETE CASCADE,
    CONSTRAINT fk_pj_socia FOREIGN KEY (dni_socia) REFERENCES socia(dni),
    CONSTRAINT uq_socia_partida UNIQUE (id_partida, dni_socia),
    CONSTRAINT chk_socia_o_invitada CHECK (dni_socia IS NOT NULL OR nombre_invitada IS NOT NULL)
);

INSERT INTO asociacion VALUES
(1, 'Asociación de Juegos de Mesa Mahai Jokoak', 'G01234567', 'Calle Cuchillería 24, 01001 Vitoria-Gasteiz',
 '945123456', 'info@mahaijokoak.eus', '2019-03-15');

INSERT INTO socia (dni, nombre, apellidos, telefono, edad, email) VALUES
('12345678Z', 'Ane', 'Etxeberria Ortiz', '688111222', 29, 'ane.etxeberria@mail.com'),
('23456789D', 'Laura', 'Martínez Gil', '677222333', 34, 'laura.martinez@mail.com'),
('34567890V', 'Irati', 'Sáenz de Ugarte', '666333444', 22, 'irati.saenz@mail.com'),
('45678901G', 'Nerea', 'Fernández Ruiz', '655444555', 41, 'nerea.fernandez@mail.com'),
('56789012B', 'Lucía', 'Gómez Pérez', '644555666', 19, 'lucia.gomez@mail.com'),
('X1234567L', 'Sofía', 'Rossi', '633666777', 27, 'sofia.rossi@mail.com');

INSERT INTO juego (nombre, autores, descripcion, min_jugadores, max_jugadores, duracion, tematica, tipo, ubicacion, estado, dni_propietaria, fecha_alta) VALUES
('Catan', 'Klaus Teuber', 'Coloniza una isla comerciando con recursos', 3, 4, 90, 'Comercio', 'COMPETITIVO', 'Estantería A1', 'DISPONIBLE', NULL, '2019-03-15'),
('Carcassonne', 'Klaus-Jürgen Wrede', 'Construye ciudades y caminos colocando losetas', 2, 5, 45, 'Medieval', 'COMPETITIVO', 'Estantería A2', 'DISPONIBLE', NULL, '2019-04-02'),
('Pandemic', 'Matt Leacock', 'Salvad el mundo de cuatro enfermedades', 2, 4, 45, 'Medicina', 'COOPERATIVO', 'Estantería B1', 'PRESTADO', NULL, '2020-01-20'),
('Azul', 'Michael Kiesling', 'Decora el palacio con azulejos', 2, 4, 40, 'Abstracto', 'COMPETITIVO', 'Estantería A3', 'DISPONIBLE', '12345678Z', '2021-06-11'),
('Dixit', 'Jean-Louis Roubira', 'Adivina la carta a partir de una pista', 3, 8, 30, 'Fantasía', 'COMPETITIVO', 'Estantería C1', 'DISPONIBLE', NULL, '2020-09-05'),
('Wingspan', 'Elizabeth Hargrave', 'Atrae aves a tu reserva natural', 1, 5, 70, 'Naturaleza', 'COMPETITIVO', 'Estantería B2', 'EN_REPARACION', '23456789D', '2022-02-18'),
('Terraforming Mars', 'Jacob Fryxelius', 'Haz habitable el planeta Marte', 1, 5, 120, 'Ciencia ficción', 'COMPETITIVO', 'Estantería B3', 'DISPONIBLE', NULL, '2023-05-30'),
('Hanabi', 'Antoine Bauza', 'Montad juntas un espectáculo de fuegos artificiales', 2, 5, 25, 'Cartas', 'COOPERATIVO', 'Estantería C2', 'DISPONIBLE', '45678901G', '2024-11-08');

INSERT INTO partida (id_juego, fecha, hora, jugadores_deseados) VALUES
(1, '2026-10-03', '18:00', 4),
(3, '2026-10-10', '17:30', 4),
(2, '2026-10-17', '19:00', 3),
(5, '2026-10-24', '18:00', 6),
(7, '2026-10-31', '16:00', 4);

INSERT INTO partida_jugadora (id_partida, dni_socia, nombre_invitada, ganadora) VALUES
(1, '12345678Z', NULL, TRUE),
(1, '23456789D', NULL, FALSE),
(1, '34567890V', NULL, FALSE),
(1, NULL, 'Marta (amiga de Ane)', FALSE),
(2, '23456789D', NULL, TRUE),
(2, '45678901G', NULL, TRUE),
(2, '56789012B', NULL, TRUE),
(3, '12345678Z', NULL, FALSE),
(3, 'X1234567L', NULL, TRUE),
(3, '34567890V', NULL, FALSE),
(4, '12345678Z', NULL, FALSE),
(4, '45678901G', NULL, FALSE),
(4, NULL, 'Jon (invitado)', FALSE),
(5, '23456789D', NULL, FALSE);
