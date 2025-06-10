#!/bin/bash

set -e  # Si algo falla, el script se detiene

echo "🔧 Compilando ms-task-service..."
cd /usr/local/gestion-tareas/ms-task-service
mvn clean package -DskipTests
cd ..

echo "🔧 Compilando ms-client-service..."
cd /usr/local/gestion-tareas/ms-client-service
mvn clean package -DskipTests
cd ..

echo "🔧 Compilando api-gateway..."
cd /usr/local/gestion-tareas/api-gateway
mvn clean package -DskipTests
cd ..

echo "🐳 Construyendo y levantando todos los servicios con Docker Compose..."
docker compose up -d --build

echo "✅ Proyecto construido y servicios levantados correctamente."
