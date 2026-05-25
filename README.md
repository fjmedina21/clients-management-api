# Clients Management API - Documentación Técnica

## Descripción General

`clientsManagement` es una API RESTful desarrollada como prueba técnica para la gestión de clientes.
El sistema permite:

* Crear clientes
* Consultar clientes
* Filtrar clientes por país
* Actualizar información
* Eliminar clientes mediante `soft delete`
* Consultar países utilizando la API externa `Rest Countries API`

La solución fue desarrollada utilizando una arquitectura en capas basada en servicios, repositorios y DTOs, siguiendo principios de separación de responsabilidades y mantenibilidad.

---

# Tecnologías Utilizadas

| Tecnología            | Uso                  |
| --------------------- | -------------------- |
| Java 25               | Lenguaje principal   |
| Quarkus 3.35.3        | Framework backend    |
| Hibernate ORM Panache | Persistencia         |
| PostgreSQL            | Base de datos        |
| RESTEasy Reactive     | Endpoints REST       |
| Jackson               | Serialización JSON   |
| Docker                | Contenerización      |
| Docker Compose        | Orquestación         |
| Swagger/OpenAPI       | Documentación de API |
| JUnit 5               | Testing              |
| Mockito               | Mocking              |

---

# Arquitectura del Proyecto

El proyecto utiliza una arquitectura basada en capas:

```text
Controller Layer
        ↓
Service Layer
        ↓
Repository Layer
        ↓
Database
```

---

# Estructura del Proyecto

```text
src
├── controllers
├── services
├── Repository
├── helpers
├── middlewares
├── mappers
├── models
│   ├── apiResponses
│   ├── dtos
│   └── entities
└── test
```

---

# Configuración del Proyecto

## `pom.xml`

El proyecto utiliza Maven como sistema de construcción.

### Dependencias principales

```xml
quarkus-rest
quarkus-rest-jackson
quarkus-hibernate-orm-panache
quarkus-jdbc-postgresql
quarkus-smallrye-openapi
quarkus-swagger-ui
```

### Dependencias de pruebas

```xml
quarkus-junit5
quarkus-junit5-mockito
rest-assured
```

---

# Configuración de Aplicación

## `application.properties`

Configuración principal:

```properties
quarkus.datasource.db-kind=postgresql
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/testdb
quarkus.datasource.username=postgres
quarkus.datasource.password=postgres
```

### Swagger

```properties
quarkus.swagger-ui.path=/swagger
```

### OpenAPI

```properties
quarkus.smallrye-openapi.path=/openapi
```

---

# Arquitectura de Capas

# Controllers

Los controllers reciben solicitudes HTTP y delegan la lógica al servicio correspondiente.

## `ClientsController.java`

Endpoints:

| Método | Endpoint        | Descripción            |
| ------ | --------------- | ---------------------- |
| POST   | `/clients`      | Crear cliente          |
| GET    | `/clients`      | Obtener clientes       |
| GET    | `/clients/{id}` | Obtener cliente por ID |
| PATCH  | `/clients/{id}` | Actualizar cliente     |
| DELETE | `/clients/{id}` | Eliminar cliente       |

---

## `RestCountriesController.java`

Endpoints relacionados con la API externa.

| Método | Endpoint                 | Descripción              |
| ------ | ------------------------ | ------------------------ |
| GET    | `/countries/name/{name}` | Buscar países por nombre |

---

# Services

La capa `services` contiene la lógica de negocio.

## `ClientService.java`

Responsabilidades principales:

* Validar datos
* Gestionar clientes
* Aplicar reglas de negocio
* Integrarse con `ExternalService`
* Manejar respuestas API

---

## Métodos Principales

### `getClients()`

Obtiene clientes con soporte de:

* filtrado por país
* paginación

Utiliza:

```java
PagedList.toPagedList()
```

---

### `createClient()`

Proceso:

1. Validación de datos
2. Verificación de email duplicado
3. Conversión DTO → Entity
4. Consulta a `Rest Countries API`
5. Obtención del gentilicio
6. Persistencia

---

### `updateClient()`

Permite actualizar:

* correo electrónico
* teléfono
* dirección
* país
* gentilicio (se actualiza automaticamente en base al país seleccionado, consultando un servicio externo, [restcountriesapi.com](restcountries.com/v3.1/alpha/{code}))
---

### `deleteClient()`

Implementa `soft delete`:

```java
client.setDeletedAt(java.time.Instant.now());
```

No elimina físicamente el registro.

---

# Repository Layer

## `ClientRepository.java`

Extiende:

```java
PanacheRepository<Client>
```

### Métodos

| Método             | Función                  |
| ------------------ | ------------------------ |
| `findByEmail()`    | Buscar por email         |
| `canUpdateEmail()` | Validar duplicados       |
| `findById()`       | Buscar por ID            |
| `listAllClients()` | Obtener clientes activos |
| `save()`           | Persistir cliente        |

---

# Persistencia

## `Client.java`

Entidad principal del sistema.

### Campos

| Campo             | Tipo    |
| ----------------- | ------- |
| id                | UUID    |
| primerNombre      | String  |
| segundoNombre     | String  |
| primerApellido    | String  |
| segundoApellido   | String  |
| correoElectronico | String  |
| direccion         | String  |
| telefono          | String  |
| pais              | String  |
| gentilicio        | String  |
| createdAt         | Instant |
| updatedAt         | Instant |
| deletedAt         | Instant |

---

# Soft Delete

La eliminación utiliza:

```java
deletedAt
```

Los registros eliminados no son retornados:

```java
deletedAt is null
```

---

# DTOs

## `ClientCreateRequest`

Representa el payload de creación.

## `ClientUpdateRequest`

Representa el payload de actualización.

## `ClientResponse`

Representa la respuesta enviada al cliente.

## `RestCountriesResponse`

DTO para mapear la respuesta de `Rest Countries API`.

---

# Mappers

Los mappers transforman DTOs y entidades.

## `ClientCreateRequestMapper`

Convierte:

```text
ClientCreateRequest → Client
```

---

## `ClientResponseMapper`

Convierte:

```text
Client → ClientResponse
```

---

# Validaciones

## `ClientValidator.java`

Contiene validaciones manuales.

### Validaciones implementadas

| Validación | Regla                 |
| ---------- | --------------------- |
| Email      | Regex                 |
| Teléfono   | 7-15 dígitos          |
| País       | ISO 3166-1 alpha-2    |
| Dirección  | Máximo 200 caracteres |

---

## Validación de Email

```java
isValidEmail()
```

Permite emails con `+tag`.

---

## Validación de País

```java
^[a-zA-Z]{2}$
```

Ejemplo:

```text
DO
US
MX
```

---

# Integración Externa

## `ExternalService.java`

Servicio encargado de consumir:

```text
https://restcountries.com
```

---

## Funcionalidades

### `getDemonymByCode()`

Obtiene el gentilicio de un país.

Ejemplo:

```text
DO → Dominican
US → American
```

---

### `getCountriesByName()`

Busca países por nombre.

---

# Manejo Global de Errores

## `GlobalErrorHandler.java`

Implementa:

```java
ExceptionMapper<Exception>
```

Funcionalidades:

* captura excepciones globales
* logging centralizado
* generación de `traceId`
* respuestas consistentes

---

# Respuestas API

## `BaseApiResponse`

Clase base para todas las respuestas.

Campos:

```java
ok
statusCode
detail
```

---

## `ApiResponse<T>`

Respuesta estándar con datos.

---

## `ErrorApiResponse`

Respuesta de errores.

Campos:

```java
errorType
errors
```

---

## `PaginatedApiResponse<T>`

Respuesta paginada.

Incluye:

```java
currentPage
totalPages
pageSize
totalCount
hasPrevious
hasNext
```

---

# Paginación

## `PagedList.java`

Implementa paginación manual utilizando:

```java
skip()
limit()
```

---

# Docker

## `Dockerfile.jvm`

Construcción de imagen para despliegue.

Base image:

```dockerfile
registry.access.redhat.com/ubi9/openjdk-25-runtime:1.24
```

---

# Docker Compose

## `docker-compose.yaml`

Servicios:

| Servicio | Función            |
| -------- | ------------------ |
| database | PostgreSQL         |
| api      | Aplicación Quarkus |

---

## Variables de entorno

```yaml
POSTGRES_URL
POSTGRES_USER
POSTGRES_PASSWORD
HIBERNATE_ORM_SCHEMA_MANAGEMENT_STRATEGY
```

---

# Swagger y OpenAPI

## Swagger UI

Disponible en:

```text
/swagger
```

---

## OpenAPI

Disponible en:

```text
/openapi
```

---

# Testing

## `ClienteServiceTests.java`

Pruebas unitarias utilizando:

* JUnit 5
* Mockito

---

# Cobertura de pruebas

## CreateClientTests

Casos:

* cliente válido
* email duplicado
* email inválido
* teléfono inválido
* país inválido

---

## GetClientsTests

Casos:

* obtener lista
* lista vacía
* filtrado por país

---

## GetClientByIdTests

Casos:

* cliente existente
* cliente inexistente

---

## updateClientTests

Casos:

* actualización correcta
* email duplicado
* cambio de gentilicio
* cliente inexistente

---

## DeleteClientTests

Casos:

* eliminación correcta
* cliente inexistente

---

# Flujo General del Sistema

## Crear Cliente

```text
HTTP Request
    ↓
ClientsController
    ↓
ClientService
    ↓
ClientValidator
    ↓
ClientRepository
    ↓
ExternalService
    ↓
PostgreSQL
```

---

# Consideraciones Técnicas

## Uso de UUID

La entidad utiliza:

```java
GenerationType.UUID
```

Ventajas:

* evita colisiones
* mejora distribución
* ideal para microservicios

---

## Soft Delete

Ventajas:

* recuperación de datos
* auditoría
* trazabilidad

---

## Separación DTO/Entity

Evita:

* exponer entidades
* acoplamiento
* problemas de serialización

---

# Conclusión

`clientsManagement` es una API REST desarrollada con una arquitectura limpia y modular utilizando Quarkus.
El proyecto implementa:

* separación de responsabilidades
* validaciones robustas
* respuestas estandarizadas
* paginación
* integración externa
* soft delete
* pruebas unitarias
* despliegue con Docker
