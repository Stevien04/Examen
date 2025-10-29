-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         10.4.27-MariaDB - mariadb.org binary distribution
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.4.0.6659
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para db_reclamossma
CREATE DATABASE IF NOT EXISTS `db_reclamossma` /*!40100 DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci */;
USE `db_reclamossma`;

-- Volcando estructura para tabla db_reclamossma.categoriasma
CREATE TABLE IF NOT EXISTS `categoriasma` (
  `idCategoriaSMA` int(11) NOT NULL AUTO_INCREMENT,
  `nombreCategoria` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idCategoriaSMA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla db_reclamossma.categoriasma: ~3 rows (aproximadamente)
INSERT INTO `categoriasma` (`idCategoriaSMA`, `nombreCategoria`, `descripcion`) VALUES
	(1, 'Infraestructura', 'Problemas con equipos o aulas'),
	(2, 'Servicios', 'Problemas con servicios brindados'),
	(3, 'Académico', 'Inconvenientes con docentes o matrículas');

-- Volcando estructura para tabla db_reclamossma.reclamosma
CREATE TABLE IF NOT EXISTS `reclamosma` (
  `idReclamoSMA` int(11) NOT NULL AUTO_INCREMENT,
  `idUsuarioSMA` int(11) NOT NULL,
  `idCategoriaSMA` int(11) NOT NULL,
  `descripcion` text NOT NULL,
  `fecha` date NOT NULL DEFAULT curdate(),
  `estado` enum('Pendiente','En atención','Resuelto') DEFAULT 'Pendiente',
  PRIMARY KEY (`idReclamoSMA`),
  KEY `idUsuarioSMA` (`idUsuarioSMA`),
  KEY `idCategoriaSMA` (`idCategoriaSMA`),
  CONSTRAINT `reclamosma_ibfk_1` FOREIGN KEY (`idUsuarioSMA`) REFERENCES `usuariosma` (`idUsuarioSMA`),
  CONSTRAINT `reclamosma_ibfk_2` FOREIGN KEY (`idCategoriaSMA`) REFERENCES `categoriasma` (`idCategoriaSMA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla db_reclamossma.reclamosma: ~3 rows (aproximadamente)
INSERT INTO `reclamosma` (`idReclamoSMA`, `idUsuarioSMA`, `idCategoriaSMA`, `descripcion`, `fecha`, `estado`) VALUES
	(1, 2, 1, 'El proyector del laboratorio no enciende.', '2025-10-29', 'Pendiente'),
	(2, 2, 2, 'No hay agua en los baños del pabellón A.', '2025-10-29', 'En atención'),
	(3, 2, 3, 'Error en la nota del curso de Programación.', '2025-10-29', 'Resuelto');

-- Volcando estructura para tabla db_reclamossma.rolsma
CREATE TABLE IF NOT EXISTS `rolsma` (
  `idRolSMA` int(11) NOT NULL AUTO_INCREMENT,
  `nombreRol` varchar(50) NOT NULL,
  PRIMARY KEY (`idRolSMA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla db_reclamossma.rolsma: ~2 rows (aproximadamente)
INSERT INTO `rolsma` (`idRolSMA`, `nombreRol`) VALUES
	(1, 'Administrador'),
	(2, 'Usuario');

-- Volcando estructura para tabla db_reclamossma.seguimientosma
CREATE TABLE IF NOT EXISTS `seguimientosma` (
  `idSeguimientoSMA` int(11) NOT NULL AUTO_INCREMENT,
  `idReclamoSMA` int(11) NOT NULL,
  `idAdminSMA` int(11) DEFAULT NULL,
  `fecha` datetime DEFAULT current_timestamp(),
  `observacion` text DEFAULT NULL,
  `estado` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`idSeguimientoSMA`),
  KEY `idReclamoSMA` (`idReclamoSMA`),
  KEY `idAdminSMA` (`idAdminSMA`),
  CONSTRAINT `seguimientosma_ibfk_1` FOREIGN KEY (`idReclamoSMA`) REFERENCES `reclamosma` (`idReclamoSMA`),
  CONSTRAINT `seguimientosma_ibfk_2` FOREIGN KEY (`idAdminSMA`) REFERENCES `usuariosma` (`idUsuarioSMA`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla db_reclamossma.seguimientosma: ~3 rows (aproximadamente)
INSERT INTO `seguimientosma` (`idSeguimientoSMA`, `idReclamoSMA`, `idAdminSMA`, `fecha`, `observacion`, `estado`) VALUES
	(1, 1, 1, '2025-10-29 18:46:57', 'Se revisó el proyector, se solicitará mantenimiento.', 'En atención'),
	(2, 2, 1, '2025-10-29 18:46:57', 'El área de servicios está interviniendo.', 'En atención'),
	(3, 3, 1, '2025-10-29 18:46:57', 'Error corregido en el sistema académico.', 'Resuelto');

-- Volcando estructura para tabla db_reclamossma.usuariosma
CREATE TABLE IF NOT EXISTS `usuariosma` (
  `idUsuarioSMA` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `idRolSMA` int(11) NOT NULL,
  `ip_autorizada` varchar(50) DEFAULT NULL,
  `activo` tinyint(4) DEFAULT 1,
  PRIMARY KEY (`idUsuarioSMA`),
  UNIQUE KEY `correo` (`correo`),
  KEY `idRolSMA` (`idRolSMA`),
  CONSTRAINT `usuariosma_ibfk_1` FOREIGN KEY (`idRolSMA`) REFERENCES `rolsma` (`idRolSMA`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1 COLLATE=latin1_swedish_ci;

-- Volcando datos para la tabla db_reclamossma.usuariosma: ~2 rows (aproximadamente)
INSERT INTO `usuariosma` (`idUsuarioSMA`, `nombre`, `correo`, `password`, `idRolSMA`, `ip_autorizada`, `activo`) VALUES
	(1, 'Administrador General', 'admin@upt.edu.pe', 'admin123', 1, '192.168.1.10', 1),
	(2, 'Juan Pérez', 'juanperez@upt.edu.pe', '123456', 2, '192.168.1.20', 1);

-- Volcando estructura para vista db_reclamossma.vista_resumensma
-- Creando tabla temporal para superar errores de dependencia de VIEW
CREATE TABLE `vista_resumensma` (
	`categoria` VARCHAR(100) NOT NULL COLLATE 'latin1_swedish_ci',
	`estado` ENUM('Pendiente','En atención','Resuelto') NULL COLLATE 'latin1_swedish_ci',
	`total` BIGINT(21) NOT NULL
) ENGINE=MyISAM;

-- Volcando estructura para vista db_reclamossma.vista_resumensma
-- Eliminando tabla temporal y crear estructura final de VIEW
DROP TABLE IF EXISTS `vista_resumensma`;
CREATE ALGORITHM=UNDEFINED SQL SECURITY DEFINER VIEW `vista_resumensma` AS SELECT c.nombreCategoria AS categoria, 
       r.estado, 
       COUNT(r.idReclamoSMA) AS total
FROM reclamoSMA r
INNER JOIN categoriaSMA c ON r.idCategoriaSMA = c.idCategoriaSMA
GROUP BY c.nombreCategoria, r.estado ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
