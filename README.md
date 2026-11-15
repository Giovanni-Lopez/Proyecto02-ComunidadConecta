# 🤝 Comunidad Conecta (`Proyecto02`)

![Java](https://img.shields.io/badge/Java-JDK%201.8-orange?style=flat-square&logo=java)
![JSF](https://img.shields.io/badge/JSF-PrimeFaces%208.0-blue?style=flat-square)
![SQL Server](https://img.shields.io/badge/Database-SQL%20Server%202017-CC2927?style=flat-square&logo=microsoftsqlserver)
![Server](https://img.shields.io/badge/Server-GlassFish%204.1-green?style=flat-square)
![IDE](https://img.shields.io/badge/IDE-NetBeans%208.2-1B6AC6?style=flat-square&logo=apachenetbeanside)

Plataforma web desarrollada para centralizar, gestionar y dar seguimiento a solicitudes de apoyo comunitario, conectando de manera organizada a **solicitantes**, **voluntarios** y **organizaciones solidarias**.

> **Asignatura:** Programación III (PRN315)  
> **Institución:** Universidad de El Salvador (UES)  
> **Grupo de Trabajo:** GT 02

---

## 📋 Características Principales

* **Autenticación y Control de Acceso:** Inicio de sesión validado contra base de datos con soporte para distintos roles (`Administrador`, `Voluntario` y `Solicitante`).
* **Gestión de Solicitudes:** Registro, visualización, actualización de estado (`Pendiente`, `En proceso`, `Atendida`, `Cerrada`) y asignación de recursos comunitarios.
* **Trazabilidad y Seguimiento:** Historial de comentarios y avances asociados a cada solicitud registrada.
* **Arquitectura por Capas:** Separación clara entre vistas (JSF/PrimeFaces), controladores (Managed Beans), acceso a datos (DAO/JDBC) y entidades del dominio con herencia orientada a objetos.

---

## 🛠️ Tecnologías Utilizadas

| Capa | Tecnología / Herramienta |
| :--- | :--- |
| **Lenguaje** | Java (JDK 1.8) |
| **Frontend / Vista** | JavaServer Faces (JSF) + PrimeFaces 8.0 |
| **Backend / Lógica** | Java Web (Servlets / Managed Beans) |
| **Base de Datos** | Microsoft SQL Server 2017 Express |
| **Conectividad BD** | JDBC (`mssql-jdbc-7.2.2.jre8.jar`) |
| **Servidor de Aplicaciones** | GlassFish Server 4.1 |
| **Entorno de Desarrollo** | Apache NetBeans IDE 8.2 |

---

## 📂 Estructura del Proyecto

```text
Proyecto02/
├── Web Pages/
│   ├── WEB-INF/
│   │   ├── faces-config.xml
│   │   └── web.xml
│   ├── index.xhtml                  # Pantalla de inicio de sesión
│   └── solicitudes.xhtml            # Módulo principal de gestión de solicitudes
├── Source Packages/
│   └── com.comunidadconecta/
│       ├── controlador/
│       │   ├── LoginBean.java       # Controlador de autenticación y sesión
│       │   └── SolicitudBean.java   # Controlador CRUD de solicitudes
│       ├── dao/
│       │   ├── Conexion.java        # Gestión de conexión JDBC a SQL Server
│       │   ├── SolicitudDAO.java    # Operaciones SQL para solicitudes
│       │   └── UsuarioDAO.java      # Operaciones SQL y mapeo de usuarios
│       └── modelo/
│           ├── Usuario.java         # Clase base de usuario
│           ├── Administrador.java   # Subclase rol Administrador
│           ├── Solicitante.java     # Subclase rol Solicitante
│           ├── Voluntario.java      # Subclase rol Voluntario
│           ├── Organizacion.java    # Entidad de organizaciones aliadas
│           ├── Categoria.java       # Entidad de categorías de apoyo
│           ├── Solicitud.java       # Entidad principal de solicitud
│           └── Seguimiento.java     # Entidad de bitácora de seguimiento
├── BD_ComunidadConecta.sql          # Script de creación y datos de prueba
└── LEEME_INSTALACION.txt            # Guía rápida de despliegue local
```

---

## 🗄️ Modelo de Base de Datos (`BD_ComunidadConecta`)

El script `BD_ComunidadConecta.sql` genera automáticamente la base de datos con integridad referencial, restricciones `CHECK` y **6 registros de prueba por tabla**:

1. **`Usuario`**: Almacena administradores, voluntarios y solicitantes discriminados por el campo `rol`.
2. **`Categoria`**: Clasifica los tipos de ayuda (*Víveres, Medicamentos, Vivienda, Útiles Escolares, Ropa, Apoyo Psicológico*).
3. **`Organizacion`**: Registra las entidades y fundaciones que brindan apoyo.
4. **`Solicitud`**: Tabla transaccional que vincula al solicitante, categoría, estado, voluntario asignado y organización.
5. **`Seguimiento`**: Bitácora cronológica de observaciones por cada solicitud.

---

## 🚀 Instalación y Ejecución Local

### 1. Configurar la Base de Datos
1. Abre **SQL Server Management Studio (SSMS)**.
2. Abre y ejecuta el archivo `BD_ComunidadConecta.sql` incluido en el repositorio.
3. Verifica en **SQL Server Configuration Manager** que el protocolo **TCP/IP** esté habilitado en el puerto `1433` y que el usuario `sa` esté activo.
4. Si tu contraseña de SQL Server es distinta de `123`, actualízala en:
   ```text
   src/java/com/comunidadconecta/dao/Conexion.java
   ```

### 2. Importar y Ejecutar en NetBeans 8.2
1. Clona este repositorio o descarga el código fuente:
   ```bash
   git clone https://github.com/tu-usuario/tu-repositorio.git
   ```
2. En **NetBeans 8.2**, ve a `File -> Open Project...` y selecciona la carpeta `Proyecto02`.
3. Si NetBeans solicita resolver referencias faltantes (*Resolve Missing Server / Libraries*):
   * Asigna **GlassFish Server 4.1** como servidor de destino.
   * Haz clic derecho en el proyecto -> `Properties -> Libraries -> Add JAR/Folder` y agrega `primefaces-8.0.jar` y `mssql-jdbc-7.2.2.jre8.jar`.
4. Haz clic derecho sobre `Proyecto02` -> **`Clean and Build`** y luego **`Run`**.

---

## 🔑 Credenciales de Prueba

La base de datos incluye usuarios precargados listos para probar cada perfil del sistema:

| Rol | Correo Electrónico | Contraseña |
| :--- | :--- | :--- |
| **Administrador** | `admin@comunidad.sv` | `12345` |
| **Voluntario** | `jose.vol@comunidad.sv` | `12345` |
| **Solicitante** | `juan.sol@correo.sv` | `12345` |

---

## 👥 Autores

* **Grupo de Trabajo GT 02** — *Programación III (PRN315), Universidad de El Salvador.*