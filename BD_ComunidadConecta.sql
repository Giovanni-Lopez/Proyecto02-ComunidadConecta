-- ============================================================================
-- Script de Base de Datos: Comunidad Conecta
-- Asignatura: Programación III (PRN315) - Universidad de El Salvador
-- Grupo de Trabajo: GT 02
-- ============================================================================

USE master;
GO

IF DB_ID('BD_ComunidadConecta') IS NOT NULL
BEGIN
    ALTER DATABASE BD_ComunidadConecta SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE BD_ComunidadConecta;
END
GO

CREATE DATABASE BD_ComunidadConecta;
GO

USE BD_ComunidadConecta;
GO

-- 1. Crea Tabla Usuario
CREATE TABLE Usuario (
    idUsuario INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Solicitante', 'Voluntario', 'Administrador')),
    direccion VARCHAR(200) NULL,
    disponibilidad VARCHAR(80) NULL
);
GO

-- 2. Crea Tabla Categoria
CREATE TABLE Categoria (
    idCategoria INT IDENTITY(1,1) PRIMARY KEY,
    nombreCategoria VARCHAR(80) NOT NULL
);
GO

-- 3. Crea Tabla Organizacion
CREATE TABLE Organizacion (
    idOrganizacion INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    tipoApoyo VARCHAR(100) NOT NULL
);
GO

-- 4. Crea Tabla Solicitud
CREATE TABLE Solicitud (
    idSolicitud INT IDENTITY(1,1) PRIMARY KEY,
    descripcion VARCHAR(300) NOT NULL,
    fechaRegistro DATE NOT NULL DEFAULT GETDATE(),
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente'
        CHECK (estado IN ('Pendiente', 'En proceso', 'Atendida', 'Cerrada')),
    idSolicitante INT NOT NULL,
    idVoluntario INT NULL,
    idCategoria INT NOT NULL,
    idOrganizacion INT NULL,
    CONSTRAINT FK_Solicitud_Solicitante FOREIGN KEY (idSolicitante) REFERENCES Usuario(idUsuario),
    CONSTRAINT FK_Solicitud_Voluntario FOREIGN KEY (idVoluntario) REFERENCES Usuario(idUsuario),
    CONSTRAINT FK_Solicitud_Categoria FOREIGN KEY (idCategoria) REFERENCES Categoria(idCategoria),
    CONSTRAINT FK_Solicitud_Organizacion FOREIGN KEY (idOrganizacion) REFERENCES Organizacion(idOrganizacion)
);
GO

-- 5. Crea Tabla Seguimiento
CREATE TABLE Seguimiento (
    idSeguimiento INT IDENTITY(1,1) PRIMARY KEY,
    fecha DATE NOT NULL DEFAULT GETDATE(),
    comentario VARCHAR(300) NOT NULL,
    idSolicitud INT NOT NULL,
    CONSTRAINT FK_Seguimiento_Solicitud FOREIGN KEY (idSolicitud) REFERENCES Solicitud(idSolicitud)
);
GO

-- ============================================================================
-- INSERTAMOS MANUALMENTE 6 REGISTROS POR TABLA SEGÚN RÚBRICA
-- ============================================================================

INSERT INTO Usuario (nombre, correo, contrasena, telefono, rol, direccion, disponibilidad) VALUES
('Alexis Giovanni Angel Lopez', 'admin@comunidad.sv', '12345', '7000-1111', 'Administrador', NULL, NULL),
('José Alberto Perez', 'jose.vol@comunidad.sv', '12345', '7000-2222', 'Voluntario', NULL, 'Fines de semana'),
('Alberto Barreras Hernandez', 'alexis.vol@comunidad.sv', '12345', '7000-3333', 'Voluntario', NULL, 'Tardes Lunes a Viernes'),
('Juan Diego Chicas', 'juan.sol@correo.sv', '12345', '7000-4444', 'Solicitante', 'Col. Escalón, San Salvador', NULL),
('María Elena Pérez', 'maria.sol@correo.sv', '12345', '7000-5555', 'Solicitante', 'Barrio El Centro, Zacatecoluca', NULL),
('Carlos Alberto Mendoza', 'carlos.sol@correo.sv', '12345', '7000-6666', 'Solicitante', 'Res. Las Arboledas, Santa Tecla', NULL);
GO

INSERT INTO Categoria (nombreCategoria) VALUES
('Víveres y Alimentación'),
('Medicamentos y Salud'),
('Reparación de Vivienda'),
('Útiles Escolares'),
('Ropa y Abrigo'),
('Apoyo Psicológico');
GO

INSERT INTO Organizacion (nombre, tipoApoyo) VALUES
('Fundación Manos Unidas', 'Canastas básicas y alimentos'),
('Cruz Verde Local', 'Primeros auxilios y medicamentos'),
('Hábitat Comunitario SV', 'Materiales de construcción'),
('Red Juvenil Solidaria', 'Voluntariado y logística'),
('Clínica Parroquial San José', 'Consultas médicas generales'),
('Asociación Educación para Todos', 'Becas y paquetes escolares');
GO

INSERT INTO Solicitud (descripcion, fechaRegistro, estado, idSolicitante, idVoluntario, idCategoria, idOrganizacion) VALUES
('Canasta básica para familia de 5 personas afectadas por lluvias', '2026-11-01', 'Atendida', 4, 2, 1, 1),
('Medicamentos para control de hipertensión de adulto mayor', '2026-11-03', 'En proceso', 5, 3, 2, 2),
('Láminas galvanizadas para reparación de techo dañado', '2026-11-05', 'Pendiente', 6, NULL, 3, 3),
('Paquetes de cuadernos y mochilas para 3 niños de primaria', '2026-11-07', 'En proceso', 4, 2, 4, 6),
('Ropa de abrigo y frazadas para familia en zona alta', '2026-11-09', 'Pendiente', 5, NULL, 5, 4),
('Atención médica preventiva y chequeo general comunitario', '2026-11-11', 'Cerrada', 6, 3, 2, 5);
GO

INSERT INTO Seguimiento (fecha, comentario, idSolicitud) VALUES
('2026-11-02', 'Solicitud verificada mediante llamada telefónica con el solicitante.', 1),
('2026-11-03', 'Canasta básica entregada satisfactoriamente en el domicilio.', 1),
('2026-11-04', 'Receta médica validada con Cruz Verde Local; pendiente retiro.', 2),
('2026-11-08', 'Inventario de útiles escolares apartado en bodega central.', 4),
('2026-11-12', 'Atención médica completada sin novedades.', 6),
('2026-11-13', 'Caso cerrado con acta de conformidad firmada.', 6);
GO

-- Consultas de verificación
SELECT * FROM Usuario;
SELECT * FROM Categoria;
SELECT * FROM Organizacion;
SELECT * FROM Solicitud;
SELECT * FROM Seguimiento;
GO
