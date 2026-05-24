# Docker Setup

## Descripción

El proyecto incluye soporte completo para ejecución utilizando Docker y Docker Compose.

La infraestructura está compuesta por:

- API Quarkus
- PostgreSQL
- Red Docker dedicada
- Persistencia mediante volumes

---

# Requisitos Previos

Antes de ejecutar el proyecto con Docker, asegúrese de tener instalado:

| Herramienta | Versión Recomendada |
|---|---|
| Docker | Última versión |
| Docker Compose | Última versión |

---

# Estructura Docker del Proyecto

Archivos utilizados:

```text
docker-compose.yaml
src/main/docker/Dockerfile.jvm
src/main/resources/application-docker.properties
````

---

# Dockerfile

## `Dockerfile.jvm`

Ubicación:

```text
src/main/docker/Dockerfile.jvm
```

Base image utilizada:

```dockerfile
FROM registry.access.redhat.com/ubi9/openjdk-25-runtime:1.24
```

El contenedor ejecuta:

* Java 25
* Quarkus JVM Mode
* Puerto 8080

---

# Construcción de Imagen Docker

## Build local

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -f src/main/docker/Dockerfile.jvm \
  -t fjmedina21/clients-management:latest \
  .
```

---

# Docker Compose

## `docker-compose.yaml`

El proyecto define dos servicios:

| Servicio | Descripción        |
| -------- | ------------------ |
| database | PostgreSQL         |
| api      | Aplicación Quarkus |

---

# Servicio PostgreSQL

Configuración:

```yaml
database:
  image: postgres:17
```

Variables de entorno:

```yaml
POSTGRES_DB: testdb
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres
```

Puerto expuesto:

```text
5432
```

---

# Persistencia de Datos

La base de datos utiliza un volume persistente:

```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
```

Esto evita pérdida de datos al reiniciar contenedores.

---

# Healthcheck

El servicio PostgreSQL implementa:

```yaml
healthcheck:
  test: [ "CMD-SHELL", "pg_isready -U postgres" ]
```

La API espera hasta que PostgreSQL esté listo antes de iniciar.

---

# Servicio API

Configuración:

```yaml
api:
  image: fjmedina21/clients-management:latest
```

Puerto expuesto:

```text
8080
```

---

# Variables de Entorno API

```yaml
POSTGRES_URL
POSTGRES_USER
POSTGRES_PASSWORD
HIBERNATE_ORM_SCHEMA_MANAGEMENT_STRATEGY
```

---

# Perfil Docker

La aplicación utiliza:

```properties
application-docker.properties
```

Activado mediante:

```bash
-Dquarkus.profile=docker
```

---

# Ejecutar Contenedores

## Levantar infraestructura

```bash
docker compose up -d
```

---

## Ver logs

```bash
docker compose logs -f
```

---

## Ver logs únicamente de la API

```bash
docker compose logs -f api
```

---

## Ver logs únicamente de PostgreSQL

```bash
docker compose logs -f database
```

---

# Detener Infraestructura

```bash
docker compose down
```

---

# Eliminar Volumes

```bash
docker compose down -v
```

Esto eliminará:

* contenedores
* networks
* datos persistidos

---

# Reconstruir Imagen

Cuando existan cambios en el código:

```bash
docker compose build --no-cache
```

o:

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -f src/main/docker/Dockerfile.jvm \
  -t fjmedina21/clients-management:latest \
  .
```

---

# Verificar Contenedores Activos

```bash
docker ps
```

---

# Verificar Networks

```bash
docker network ls
```

---

# Verificar Volumes

```bash
docker volume ls
```

---

# Acceso a la Aplicación

## API Base URL

```text
http://localhost:8080/api
```

---

## Swagger UI

```text
http://localhost:8080/swagger
```

---

## OpenAPI

```text
http://localhost:8080/openapi
```

---

# Acceso PostgreSQL

## Host

```text
localhost
```

## Puerto

```text
5432
```

## Database

```text
testdb
```

## Usuario

```text
postgres
```

## Password

```text
postgres
```

---

# Arquitectura Docker

```text
┌────────────────────┐
│    Client / UI     │
└─────────┬──────────┘
          │
          ▼
┌────────────────────┐
│    Quarkus API     │
│  clientsManagement │
└─────────┬──────────┘
          │
          ▼
┌────────────────────┐
│     PostgreSQL     │
└────────────────────┘
```

---

# Consideraciones Técnicas

## Uso de Docker Networks

La infraestructura utiliza:

```yaml
clients-management-network
```

Permitiendo comunicación interna entre contenedores.

---

# Restart Policy

La API utiliza:

```yaml
restart: unless-stopped
```

Esto mejora disponibilidad en reinicios inesperados.

---

# Posibles Mejoras Futuras

* Multi-stage builds
* Docker image optimization
* CI/CD pipeline
* Secrets management
* Health endpoints avanzados
* Observabilidad con Prometheus y Grafana
* Centralized logging

---
