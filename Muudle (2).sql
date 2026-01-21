-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 21-01-2026 a las 17:37:55
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
  `fecha_registro` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `ASISTENCIA`
--

INSERT INTO `ASISTENCIA` (`id_asistencia`, `id_usuario`, `id_curso`, `apellidos`, `nota`, `nFaltas`, `fecha_registro`) VALUES
(1, 4, 1, 'wda', '0.00', 0, '2025-12-16'),
(2, 5, 1, 'EDADA', '0.00', 0, '2025-12-16'),
(3, 6, 2, 'adwadwa', '0.00', 0, '2025-12-16'),
(4, 7, 2, 'ddaw', '0.00', 0, '2025-12-16'),
(5, 8, 3, 'ALGAR MORALES', '0.00', 0, '2026-01-19'),
(6, 9, 3, 'ALGAR MORALES', '0.00', 0, '2026-01-19'),
(7, 8, 4, 'ALGAR MORALES', '0.00', 0, '2026-01-20'),
(8, 12, 4, 'ALGO ALGO', '0.00', 0, '2026-01-20'),
(9, 15, 4, 'DDD', '0.00', 0, '2026-01-20'),
(10, 15, 3, 'DDD', '5.00', 10, '2026-01-20');

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
(1, 'DAM', 'dammmm', NULL, 2, NULL),
(2, 'DAM 2', 'DAD', NULL, 2, NULL),
(3, 'ddddddddd', 'aa', NULL, 3, NULL),
(4, 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', 'AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA', NULL, 3, NULL);

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
(5, 'pEPE', 'EDADA', 'CORREO@gmail.com', 'password123', 'alumno', 21),
(6, 'wdadwa', 'adwadwa', 'aw@gmail.com', 'password123', 'alumno', 12),
(7, 'dawdaw', 'ddaw', 'ada@gmail.com', 'password123', 'alumno', 43),
(8, 'ALVARO', 'ALGAR MORALES', 'alvaro@mail.com', '12345', 'alumno', 19),
(9, 'MARIA', 'ALGAR MORALES', 'alvaro@gmail.com', '12345', 'alumno', 19),
(10, 'dwa', 'daw', 'adw@gmail.com', '1111', 'alumno', 22),
(11, 'Administrador', 'Sistema', 'admin@muudle.com', 'admin123', 'profesor', 30),
(12, 'ALGO', 'ALGO ALGO', 'algo@gmail.com', '1111', 'alumno', 21),
(13, 'PEPONCIO', 'PPP PPP', 'pp@gmail.com', '4444', 'alumno', 21),
(14, 'AAA', 'AAA', 'AAA@gmail.com', '2222', 'alumno', 21),
(15, 'AAD', 'DDD', 'AAAA@GMAIL.COM', '1111', 'alumno', 21),
(16, 'PPPP', 'PPP', 'PPP@gmail.com', '3333', 'profesor', 21);

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
  MODIFY `id_asistencia` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT de la tabla `CURSO`
--
ALTER TABLE `CURSO`
  MODIFY `id_curso` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `USUARIO`
--
ALTER TABLE `USUARIO`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

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
