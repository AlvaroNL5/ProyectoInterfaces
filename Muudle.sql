-- ============================================================
--  MUUDLE - Script de recreación completa de la base de datos
--  Ejecutar en phpMyAdmin o MySQL Workbench como root
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

-- Eliminar tablas si existen (en orden correcto por FK).
-- EVENTO se incluye en el DROP por compatibilidad con BBDD viejas que la
-- tuvieran creada; la tabla en si ya NO forma parte del esquema vigente.
DROP TABLE IF EXISTS `FORO_RESPUESTA`;
DROP TABLE IF EXISTS `FORO`;
DROP TABLE IF EXISTS `ENTREGA`;
DROP TABLE IF EXISTS `TAREA`;
DROP TABLE IF EXISTS `CONTROL_ASISTENCIA`;
DROP TABLE IF EXISTS `EVENTO`;
DROP TABLE IF EXISTS `ASISTENCIA`;
DROP TABLE IF EXISTS `CURSO`;
DROP TABLE IF EXISTS `USUARIO`;

-- ============================================================
--  USUARIO
-- ============================================================
CREATE TABLE `USUARIO` (
  `id_usuario`   INT(11)      NOT NULL AUTO_INCREMENT,
  `nombre`       VARCHAR(100) NOT NULL,
  `apellido`     VARCHAR(100) NOT NULL,
  `email`        VARCHAR(255) NOT NULL,
  `contraseña`   VARCHAR(255) NOT NULL,
  `tipo_usuario` ENUM('profesor','alumno') NOT NULL DEFAULT 'alumno',
  `edad`         INT(11)      NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=21;

INSERT INTO `USUARIO` VALUES
(4,  'wdad',            'wda',             'awda@gmail.com',             'admin',       'alumno',  21),
(6,  'wdadwa',          'adwadwa',         'aw@gmail.com',               'password123', 'alumno',  12),
(8,  'ALVARO',          'ALGAR MORALES',   'alvaro@mail.com',            '12345',       'alumno',  19),
(9,  'MARIA',           'ALGAR MORALES',   'alvaro@gmail.com',           '12345',       'alumno',  19),
(12, 'ALGO',            'ALGO ALGO',       'algo2@gmail.com',            '1111',        'alumno',  21),
(15, 'AAD',             'DDD',             'AAAA@GMAIL.COM',             '1111',        'alumno',  21),
(17, 'AlvaroAlvaro',    'Algar Morales',   'alvaroalvaro@gmail.com',     'aaaaa',       'alumno',  20),
(18, 'ProfesorProfesor','Profesor Profesor','profesorprofesor@gmail.com', 'sssss',       'profesor',23),
(20, 'aaaaaaaaaaa',     'aaaaaaaaaaaa',    'aaaaa@gmail.com',            'wwww',        'alumno',  54);

-- ============================================================
--  CURSO
-- ============================================================
CREATE TABLE `CURSO` (
  `id_curso`     INT(11)      NOT NULL AUTO_INCREMENT,
  `nombre_curso` VARCHAR(255) NOT NULL,
  `descripcion`  TEXT,
  `img_portada`  LONGTEXT,
  `cant_usuarios` INT(11)     DEFAULT '0',
  `id_usuario`   INT(11)      DEFAULT NULL,
  PRIMARY KEY (`id_curso`),
  KEY `fk_curso_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=12;

INSERT INTO `CURSO` (`id_curso`,`nombre_curso`,`descripcion`,`img_portada`,`cant_usuarios`,`id_usuario`) VALUES
(8,  'Alvaro Curso',                 'Curso de Alvaro',           NULL, 3, NULL),
(9,  'awdawdwad',                    'wdawdwawddwwd',             NULL, 3, NULL),
(10, 'wadadwa',                      'wdadwawadwadawdwda',        NULL, 3, NULL),
(11, 'PPPPPPPPPPPPPPPPPPPPPAAAAAAAAAA','ddddddddddddddddddddddd', NULL, 1, NULL);

-- ============================================================
--  ASISTENCIA (matriculación)
-- ============================================================
CREATE TABLE `ASISTENCIA` (
  `id_asistencia`  INT(11)        NOT NULL AUTO_INCREMENT,
  `id_usuario`     INT(11)        NOT NULL,
  `id_curso`       INT(11)        NOT NULL,
  `apellidos`      VARCHAR(100)   NOT NULL,
  `nota`           DECIMAL(4,2)   DEFAULT NULL,
  `nFaltas`        INT(11)        DEFAULT '0',
  `fecha_registro` DATE           NOT NULL,
  `matriculado`    TINYINT(1)     NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_asistencia`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_curso`   (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=50;

INSERT INTO `ASISTENCIA` VALUES
(31, 8,  8,  'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(34, 8,  9,  'ALGAR MORALES', '2.00', 5, '2026-01-31', 1),
(35, 9,  9,  'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(37, 15, 9,  'DDD',           '6.50', 7, '2026-01-31', 1),
(38, 9,  8,  'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(39, 17, 10, 'Algar Morales', '0.00', 0, '2026-02-01', 0),
(41, 6,  10, 'adwadwa',       '0.00', 0, '2026-02-01', 0),
(42, 15, 10, 'DDD',           '0.00', 0, '2026-02-01', 0),
(44, 12, 11, 'ALGO ALGO',     '0.00', 0, '2026-02-01', 0),
(49, 17, 8,  'Algar Morales', '9.00', 6, '2026-02-01', 1);

-- ============================================================
--  TAREA
-- ============================================================
CREATE TABLE `TAREA` (
  `id_tarea`          INT(11)       NOT NULL AUTO_INCREMENT,
  `id_curso`          INT(11)       NOT NULL,
  `id_profesor`       INT(11)       NOT NULL,
  `titulo`            VARCHAR(255)  NOT NULL,
  `descripcion`       TEXT,
  `fecha_creacion`    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fecha_limite`      DATETIME      NOT NULL,
  `puntuacion_maxima` DECIMAL(5,2)  NOT NULL DEFAULT '10.00',
  PRIMARY KEY (`id_tarea`),
  KEY `id_curso`    (`id_curso`),
  KEY `id_profesor` (`id_profesor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

-- ============================================================
--  ENTREGA
-- ============================================================
CREATE TABLE `ENTREGA` (
  `id_entrega`          INT(11)      NOT NULL AUTO_INCREMENT,
  `id_tarea`            INT(11)      NOT NULL,
  `id_alumno`           INT(11)      NOT NULL,
  `contenido`           TEXT,
  `archivo`             LONGTEXT,
  `fecha_entrega`       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `calificacion`        DECIMAL(5,2) DEFAULT NULL,
  `comentario_profesor` TEXT,
  `fecha_calificacion`  DATETIME     DEFAULT NULL,
  PRIMARY KEY (`id_entrega`),
  KEY `id_tarea`  (`id_tarea`),
  KEY `id_alumno` (`id_alumno`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

-- ============================================================
--  FORO
-- ============================================================
CREATE TABLE `FORO` (
  `id_hilo`        INT(11)      NOT NULL AUTO_INCREMENT,
  `id_curso`       INT(11)      NOT NULL,
  `id_usuario`     INT(11)      NOT NULL,
  `titulo`         VARCHAR(255) NOT NULL,
  `contenido`      TEXT         NOT NULL,
  `fecha_creacion` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_hilo`),
  KEY `id_curso`   (`id_curso`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

-- ============================================================
--  FORO_RESPUESTA
-- ============================================================
CREATE TABLE `FORO_RESPUESTA` (
  `id_respuesta`   INT(11)  NOT NULL AUTO_INCREMENT,
  `id_hilo`        INT(11)  NOT NULL,
  `id_usuario`     INT(11)  NOT NULL,
  `contenido`      TEXT     NOT NULL,
  `fecha_creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_respuesta`),
  KEY `id_hilo`    (`id_hilo`),
  KEY `id_usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

-- ============================================================
--  CONTROL_ASISTENCIA
-- ============================================================
CREATE TABLE `CONTROL_ASISTENCIA` (
  `id_control`    INT(11)  NOT NULL AUTO_INCREMENT,
  `id_usuario`    INT(11)  NOT NULL,
  `id_curso`      INT(11)  NOT NULL,
  `fecha`         DATE     NOT NULL,
  `estado`        ENUM('presente','ausente','justificado','retraso') NOT NULL DEFAULT 'presente',
  `observaciones` TEXT,
  PRIMARY KEY (`id_control`),
  KEY `id_usuario` (`id_usuario`),
  KEY `id_curso`   (`id_curso`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;

-- ============================================================
--  CLAVES FORÁNEAS
-- ============================================================
ALTER TABLE `ASISTENCIA`
  ADD CONSTRAINT `ASISTENCIA_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `ASISTENCIA_ibfk_2` FOREIGN KEY (`id_curso`)   REFERENCES `CURSO`   (`id_curso`)   ON DELETE CASCADE;

ALTER TABLE `CURSO`
  ADD CONSTRAINT `fk_curso_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`);

ALTER TABLE `TAREA`
  ADD CONSTRAINT `TAREA_ibfk_1` FOREIGN KEY (`id_curso`)    REFERENCES `CURSO`   (`id_curso`)   ON DELETE CASCADE,
  ADD CONSTRAINT `TAREA_ibfk_2` FOREIGN KEY (`id_profesor`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE;

ALTER TABLE `ENTREGA`
  ADD CONSTRAINT `ENTREGA_ibfk_1` FOREIGN KEY (`id_tarea`)  REFERENCES `TAREA`   (`id_tarea`)   ON DELETE CASCADE,
  ADD CONSTRAINT `ENTREGA_ibfk_2` FOREIGN KEY (`id_alumno`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE;

ALTER TABLE `FORO`
  ADD CONSTRAINT `FORO_ibfk_1` FOREIGN KEY (`id_curso`)   REFERENCES `CURSO`   (`id_curso`)   ON DELETE CASCADE,
  ADD CONSTRAINT `FORO_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE;

ALTER TABLE `FORO_RESPUESTA`
  ADD CONSTRAINT `FORO_RESPUESTA_ibfk_1` FOREIGN KEY (`id_hilo`)    REFERENCES `FORO`    (`id_hilo`)    ON DELETE CASCADE,
  ADD CONSTRAINT `FORO_RESPUESTA_ibfk_2` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE;

ALTER TABLE `CONTROL_ASISTENCIA`
  ADD CONSTRAINT `CONTROL_ASISTENCIA_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `CONTROL_ASISTENCIA_ibfk_2` FOREIGN KEY (`id_curso`)   REFERENCES `CURSO`   (`id_curso`)   ON DELETE CASCADE;

SET FOREIGN_KEY_CHECKS = 1;
