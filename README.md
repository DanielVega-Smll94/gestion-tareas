# 📌 Sistema de Gestión de Tareas - Caso Práctico

Este proyecto implementa un sistema de gestión de tareas para la empresa **XYZ**, utilizando una arquitectura basada en **microservicios** con **Spring Boot (Java 17)**, comunicación asíncrona con **RabbitMQ** y orquestación mediante **Docker**.

## 🧩 Microservicios

- `ms-client-service` (Client Service)
- `ms-task-service` (Task Service)
- `api-gateway` (Gateway)
- Base de datos: PostgreSQL 14
- Sistema de mensajería: RabbitMQ
- FTP: integración de archivos de tareas en formato JSON

---

## ✅ Requisitos Previos

- **Ubuntu 22.04 o superior || Centos**
- **Java 17**:
  ```bash
  sudo apt update
  sudo apt install openjdk-17-jdk -y
  java -version
  ```

- **Maven**
  - Para Ubuntu:
    ```bash
    sudo apt install -y maven
    ```
  - Para CentOS:
    ```bash
    sudo dnf install -y maven
    ```

- **Docker y Docker Compose**
  ```bash
  docker -v
  docker compose version
  ```

---

## ⚙️ Instalación y Ejecución

1. Colocar la carpeta del proyecto en:

   ```
   /usr/local/gestion-tareas
   ```

2. Acceder al proyecto:

   ```bash
   cd /usr/local/gestion-tareas
   ```

3. Dar permisos de ejecución:

   ```bash
   chmod +x build-and-up.sh
   ```

4. Ejecutar el script:

   ```bash
   ./build-and-up.sh
   ```

5. Asegúrate de que estén abiertos los siguientes puertos en el firewall (si aplica):

   ```bash
   sudo ufw allow 5432
   sudo ufw allow 15672
   sudo ufw allow 8045
   sudo ufw allow 8046
   ```

---

## 🌐 Accesos y Servicios

| Servicio                       | URL                                                       	|
|--------------------------------|--------------------------------------------------------------|
| **RabbitMQ**                   | http://149.102.140.120:15672/#/                           	|
| **Swagger - ms-client-service** | http://149.102.140.120:8045/swagger-ui/index.html         	|
| **Swagger - ms-task-service**  | http://149.102.140.120:8046/swagger-ui/index.html 		 	|
| **Postman Docs**               | https://documenter.getpostman.com/view/45282445/2sB2x3nYh1	|

---

## 🔧 Funcionalidades Implementadas

- **F1**: CRUD completo para Usuarios y Tareas (`POST`, `GET`, `PUT`, `DELETE`)
  - No se utilizo el DELETE fisico, solo se hizo un cambio de estado a false.
- **F2**: Actualización del estado de una tarea
- **F3**: Asignación de una tarea a un usuario con validaciones:
  - Si la tarea o el usuario no existen, se devuelve error.
  - Si la tarea está en estado `DONE`, no se puede asignar.
- **F4**: Notificación (mediante log del sistema) al asignar tareas.
- **F5**: Consulta de tareas por usuario.
- **F6**: Lectura de archivo JSON desde servidor FTP para registrar tareas automáticamente.
- **F7**: Pruebas unitarias e integración en `ms-task-service`.
- **F8**: Proyecto dockerizado, incluyendo documentación con Swagger y Postman.

---

## 🧪 Pruebas

Para ejecutar las pruebas del servicio de tareas:

```bash
cd ms-task-service
mvn test
```

---

## 🗂️ Estructura del Proyecto

```
gestion-tareas/
├── ms-client-service/
├── ms-task-service/
├── gateway/
├── diagramas/
├── docker-compose.yml
├── build-and-up.sh
├── README.md
└── database/
    └── init.sql
```

---

## 📊 Diagramas del Proyecto

Los siguientes diagramas se encuentran en la carpeta `diagramas/`:

- Diagrama C4
- Diagrama de Arquitectura
- Diagrama de Componentes (Task Service)
- Diagrama de Secuencia (Asignación de Tareas)

---

## 🔐 Configuración del Servidor SFTP

Para que el sistema pueda importar tareas automáticamente desde un archivo `.json`, es necesario configurar un servidor SFTP con las siguientes condiciones:
- Debe estar activo y accesible desde el microservicio `ms-task-service`.
- El archivo JSON debe estar ubicado en la ruta configurada (`sftpFilePath`), por defecto:
  Colocar la ruta del archivo de la siguiente forma o en el directorio que prefieras: /usr/local/gestion-tareas/pendientes/tareas.json
- Procura cambiar el host (IP) y clave de tu servidor en el archivo .application.yml se encuentra en la sección sftp el password


---

## 📌 Notas Finales

- Revisa que los contenedores estén activos con `docker ps`.
- Puedes modificar los puertos y variables en el archivo `docker-compose.yml`.
- Para simular carga de tareas por FTP, coloca un archivo `.json` con formato válido en el servidor configurado.
- Documentación de endpoints disponible en Swagger y Postman.