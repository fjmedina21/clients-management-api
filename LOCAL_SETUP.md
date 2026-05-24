
# Cómo Levantar la Aplicación en Local

## Requisitos Previos

Antes de ejecutar el proyecto, asegúrese de tener instalado:

| Herramienta    | Versión Recomendada |
|----------------|---------------------|
| Java           | 25                  |
| Maven          | 3.9+                |
| Docker         | Última versión      |
| Docker Compose | Última versión      |
| PostgreSQL     | 17                  |



# Clonar el Proyecto

```bash
git clone <https://github.com/fjmedina21/clients-management-api.git>
cd clientsManagement
```

---

# Configurar Base de Datos Local

El proyecto utiliza PostgreSQL.

## Crear base de datos

```sql
CREATE DATABASE testdb;
```

---

## Credenciales utilizadas por defecto

```properties
username=postgres
password=postgres
```

---

# Configuración de `application.properties`

Archivo:

```text
src/main/resources/application.properties
```

Configuración:

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/testdb
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
```

---

# Ejecutar el Proyecto en Modo Desarrollo

## Comando

```bash
./mvnw quarkus:dev
```

o en Windows:

```bash
mvnw.cmd quarkus:dev
```

---

# Compilar el Proyecto

```bash
./mvnw clean package
```

El artefacto generado se encontrará en:

```text
target/quarkus-app/
```

---

# Ejecutar el Jar Empaquetado

```bash
java -jar target/quarkus-app/quarkus-run.jar
```

---

# Acceder a la Aplicación

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

# Levantar la Aplicación con Docker Compose

## Construir imagen manualmente

```bash
docker buildx build \
  --platform linux/amd64,linux/arm64 \
  -f src/main/docker/Dockerfile.jvm \
  -t fjmedina21/clients-management:2.1.0 \
  .
```

---

## Ejecutar contenedores

```bash
docker compose up -d
```

---

## Ver logs

```bash
docker compose logs -f
```

---

## Detener contenedores

```bash
docker compose down
```

---

# Servicios Docker

| Servicio   | Puerto |
| ---------- | ------ |
| PostgreSQL | 5432   |
| API        | 8080   |

---

# Variables de Entorno Docker

Definidas en `docker-compose.yaml`:

```yaml
POSTGRES_URL
POSTGRES_USER
POSTGRES_PASSWORD
HIBERNATE_ORM_SCHEMA_MANAGEMENT_STRATEGY
```

---

# Ejecutar Tests

## Ejecutar todas las pruebas

```bash
./mvnw test
```

---

# Ejecutar Perfil Docker

La aplicación incluye:

```properties
application-docker.properties
```

Para ejecutar utilizando el perfil Docker:

```bash
-Dquarkus.profile=docker
```

---

# Hot Reload en Desarrollo

Quarkus soporta `live reload`.

Al ejecutar:

```bash
./mvnw quarkus:dev
```

los cambios se reflejan automáticamente sin reiniciar manualmente la aplicación.