-- Crear bases de datos
CREATE DATABASE clientdb;
CREATE DATABASE taskdb;
\connect clientdb;

-- Crear esquema y tabla en clientdb
CREATE SCHEMA IF NOT EXISTS client;

CREATE TABLE client.users (
    id SERIAL PRIMARY KEY,
    identificacion VARCHAR(50) UNIQUE NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    cargo VARCHAR(100),
    fecha_creacion timestamp without time zone NOT NULL DEFAULT now(),
    usuario_creacion character varying,
    fecha_modifica timestamp without time zone,
    usuario_modifica  character varying,
    estado boolean default TRUE
);

\connect taskdb;

-- Crear esquema y tabla en taskdb
CREATE SCHEMA IF NOT EXISTS task;

CREATE TABLE task.tasks (
    id SERIAL PRIMARY KEY,
    codigo_tarea VARCHAR(50) UNIQUE NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    criterios_aceptacion TEXT,
    fecha_inicio DATE,
    fecha_final DATE,
    tiempo_desarrollo INT,
    estado_tarea VARCHAR(20) CHECK (estado_tarea IN ('Backlog', 'Doing', 'In Review', 'Done')),
    usuario_id_asignado INTEGER,
    fecha_creacion timestamp without time zone NOT NULL DEFAULT now(),
    usuario_creacion character varying,
    fecha_modifica timestamp without time zone,
    usuario_modifica character varying,
    activo BOOLEAN DEFAULT true
);