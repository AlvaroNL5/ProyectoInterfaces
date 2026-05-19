-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 02-02-2026 a las 12:26:13
-- Versión del servidor: 5.7.35-0ubuntu0.18.04.2
-- Versión de PHP: 8.0.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `Muudle`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ASISTENCIA`
--

CREATE TABLE `ASISTENCIA` (
  `id_asistencia` int(11) NOT NULL,
  `id_usuario` int(11) NOT NULL,
  `id_curso` int(11) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `nota` decimal(4,2) DEFAULT NULL,
  `nFaltas` int(11) DEFAULT '0',
  `fecha_registro` date NOT NULL,
  `matriculado` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `ASISTENCIA`
--

INSERT INTO `ASISTENCIA` (`id_asistencia`, `id_usuario`, `id_curso`, `apellidos`, `nota`, `nFaltas`, `fecha_registro`, `matriculado`) VALUES
(31, 8, 8, 'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(34, 8, 9, 'ALGAR MORALES', '2.00', 5, '2026-01-31', 1),
(35, 9, 9, 'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(37, 15, 9, 'DDD', '6.50', 7, '2026-01-31', 1),
(38, 9, 8, 'ALGAR MORALES', '0.00', 0, '2026-01-31', 0),
(39, 17, 10, 'Algar Morales', '0.00', 0, '2026-02-01', 0),
(41, 6, 10, 'adwadwa', '0.00', 0, '2026-02-01', 0),
(42, 15, 10, 'DDD', '0.00', 0, '2026-02-01', 0),
(44, 12, 11, 'ALGO ALGO', '0.00', 0, '2026-02-01', 0),
(49, 17, 8, 'Algar Morales', '9.00', 6, '2026-02-01', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `CURSO`
--

CREATE TABLE `CURSO` (
  `id_curso` int(11) NOT NULL,
  `nombre_curso` varchar(255) NOT NULL,
  `descripcion` text,
  `img_portada` longtext,
  `cant_usuarios` int(11) DEFAULT '0',
  `id_usuario` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `CURSO`
--

INSERT INTO `CURSO` (`id_curso`, `nombre_curso`, `descripcion`, `img_portada`, `cant_usuarios`, `id_usuario`) VALUES
(8, 'Alvaro Curso', 'Curso de Alvaro', NULL, 3, NULL),
(9, 'awdawdwad', 'wdawdwawddwwd', NULL, 3, NULL),
(10, 'wadadwa', 'wdadwawadwadawdwda', NULL, 3, NULL),
(11, 'PPPPPPPPPPPPPPPPPPPPPAAAAAAAAAA', 'ddddddddddddddddddddddd', NULL, 1, NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `USUARIO`
--

CREATE TABLE `USUARIO` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) NOT NULL,
  `email` varchar(255) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `tipo_usuario` enum('profesor','alumno') NOT NULL DEFAULT 'alumno',
  `edad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `USUARIO`
--

INSERT INTO `USUARIO` (`id_usuario`, `nombre`, `apellido`, `email`, `contraseña`, `tipo_usuario`, `edad`) VALUES
(4, 'wdad', 'wda', 'awda@gmail.com', 'admin', 'alumno', 21),
(6, 'wdadwa', 'adwadwa', 'aw@gmail.com', 'password123', 'alumno', 12),
(8, 'ALVARO', 'ALGAR MORALES', 'alvaro@mail.com', '12345', 'alumno', 19),
(9, 'MARIA', 'ALGAR MORALES', 'alvaro@gmail.com', '12345', 'alumno', 19),
(12, 'ALGO', 'ALGO ALGO', 'algo2@gmail.com', '1111', 'alumno', 21),
(15, 'AAD', 'DDD', 'AAAA@GMAIL.COM', '1111', 'alumno', 21),
(17, 'AlvaroAlvaro', 'Algar Morales', 'alvaroalvaro@gmail.com', 'aaaaa', 'alumno', 20),
(18, 'ProfesorProfesor', 'Profesor Profesor', 'profesorprofesor@gmail.com', 'sssss', 'profesor', 23),
(20, 'aaaaaaaaaaa', 'aaaaaaaaaaaa', 'aaaaa@gmail.com', 'wwww', 'alumno', 54);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `ASISTENCIA`
--
ALTER TABLE `ASISTENCIA`
  ADD PRIMARY KEY (`id_asistencia`),
  ADD KEY `id_usuario` (`id_usuario`),
  ADD KEY `id_curso` (`id_curso`);

--
-- Indices de la tabla `CURSO`
--
ALTER TABLE `CURSO`
  ADD PRIMARY KEY (`id_curso`),
  ADD KEY `fk_curso_usuario` (`id_usuario`);

--
-- Indices de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `ASISTENCIA`
--
ALTER TABLE `ASISTENCIA`
  MODIFY `id_asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;

--
-- AUTO_INCREMENT de la tabla `CURSO`
--
ALTER TABLE `CURSO`
  MODIFY `id_curso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `ASISTENCIA`
--
ALTER TABLE `ASISTENCIA`
  ADD CONSTRAINT `ASISTENCIA_ibfk_1` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`) ON DELETE CASCADE,
  ADD CONSTRAINT `ASISTENCIA_ibfk_2` FOREIGN KEY (`id_curso`) REFERENCES `CURSO` (`id_curso`) ON DELETE CASCADE;

--
-- Filtros para la tabla `CURSO`
--
ALTER TABLE `CURSO`
  ADD CONSTRAINT `fk_curso_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `USUARIO` (`id_usuario`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
