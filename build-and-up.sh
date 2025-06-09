#!/bin/bash

set -e  # Si algo falla, el script se detiene

echo "🔧 Compilando ms-task-service..."
cd /usr/local/gestion-tareas/ms-task-service
mvn clean package -DskipTests
cd ..

echo "🔧 Compilando ms-gestion-tarea..."
cd /usr/local/gestion-tareas/ms-gestion-tarea
mvn clean package -DskipTests
cd ..

echo "🔧 Compilando api-gateway..."
cd /usr/local/gestion-tareas/api-gateway
mvn clean package -DskipTests
cd ..

echo "🐳 Construyendo y levantando todos los servicios con Docker Compose..."
docker compose up -d --build

echo "✅ Proyecto construido y servicios levantados correctamente."
