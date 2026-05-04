# devsu-ms-test

Proyecto test con dos microservicios Spring Boot organizados en un **Gradle multi-project build**.

---

## ¿Por qué multi-project build?

En lugar de tener dos repositorios separados o dos proyectos independientes, un multi-project build de Gradle permite:

- Un único `settings.gradle` en la raíz que registra ambos subproyectos (`msidentity`, `msfinance`)
- Un `build.gradle` raíz que actúa como coordinador: define dependencias comunes (Lombok, JUnit) **una sola vez** con `apply false`, y los subproyectos las aplican
- Cada microservicio tiene su propio `build.gradle` solo con lo que necesita específicamente
- Un único comando (`./gradlew test`) corre los tests de ambos servicios

La configuración compartida vive en [`build.gradle`](build.gradle) (raíz). Las dependencias específicas de cada servicio están en [`msidentity/build.gradle`](msidentity/build.gradle) y [`msfinance/build.gradle`](msfinance/build.gradle).

---

## Servicios

| Servicio | Descripción | Puerto |
|---|---|---|
| **msidentity** | Gestión de identidad de clientes (Persona / Cliente) | `8080` |
| **msfinance** | Gestión de cuentas y movimientos bancarios | `8081` |

### Arquitectura

Ambos servicios siguen **Arquitectura Hexagonal (Ports & Adapters)**:

```
infrastructure/rest        ← HTTP: controllers, DTOs, mappers
application/service        ← casos de uso, lógica de negocio
domain/model               ← entidades puras
domain/port/in             ← contratos de entrada (use cases)
domain/port/out            ← contratos de salida (repositorios)
infrastructure/persistence ← adaptadores JPA + Spring Data
infrastructure/messaging   ← publishers y consumers de RabbitMQ
infrastructure/config      ← SecurityConfig, OpenApiConfig
```

### Comunicación entre servicios

La comunicación entre `msidentity` y `msfinance` es **asíncrona vía RabbitMQ**:

- Al eliminar un cliente en `msidentity`, se publica un `ClientDeletedEvent`
- `msfinance` consume el evento y desactiva todas las cuentas asociadas al cliente
- Se implementan: DLQ (Dead Letter Queue), reintentos con backoff exponencial e idempotencia por `eventId`

---

## Prerrequisitos

- Docker Desktop

---

## Levantar con Docker Compose (recomendado)

Copiá el archivo de variables de entorno y ajustá las credenciales si es necesario:

```bash
cp .env.example .env
```

Desde la raíz del proyecto:

```bash
docker compose up -d --build
```

Esto construye las imágenes y levanta cinco containers:

| Container | Puerto host | Descripción |
|---|---|---|
| `postgres-msidentity` | `5435` | Base de datos de msidentity |
| `postgres-msfinance` | `5434` | Base de datos de msfinance |
| `rabbitmq` | `5672` / `15672` | Broker de mensajes / UI de gestión |
| `msidentity` | `8080` | Servicio de identidad |
| `msfinance` | `8081` | Servicio financiero |

Las tablas se crean automáticamente al primer arranque gracias a **Flyway** — no se requiere ningún paso manual de base de datos.

> **¿Por qué 5435 y 5434 y no 5432/5433?**
> En Windows es común tener PostgreSQL instalado como servicio nativo en el puerto `5432`. Para evitar la colisión los containers usan `5435` y `5434`. Si tu máquina no tiene PostgreSQL local podés cambiar los puertos en [`compose.yaml`](compose.yaml).

### Resetear la base de datos

Para borrar todos los datos y volver a aplicar las migraciones desde cero:

```bash
docker compose down -v && docker compose up -d --build
```

---

## Levantar en modo desarrollo (IntelliJ IDEA)

Útil para debugging. En este modo solo la infraestructura corre en Docker y las apps se ejecutan directamente en la JVM local.

**Paso 1** — Levantá solo la infraestructura:

```bash
docker compose up -d postgres-msidentity postgres-msfinance rabbitmq
```

**Paso 2** — Abrí [`msidentity/.../MsidentityApplication.java`](msidentity/src/main/java/com/devsu/msidentity/MsidentityApplication.java) y hacé click en el ícono ▶ verde. IntelliJ crea la run configuration automáticamente.

**Paso 3** — Repetí lo mismo con [`msfinance/.../MsfinanceApplication.java`](msfinance/src/main/java/com/devsu/msfinance/MsfinanceApplication.java).

> ⚠️ **Importante:** IntelliJ corre las apps directamente en Windows. Si tenés PostgreSQL instalado localmente en el puerto `5432`, la app va a intentar conectarse ahí en lugar del container Docker. Usá los puertos `5435`/`5434` como se describió arriba.

---

## URLs de acceso

| Servicio | URL |
|---|---|
| msidentity API | http://localhost:8080 |
| msidentity Swagger UI | http://localhost:8080/swagger-ui/index.html |
| msfinance API | http://localhost:8081 |
| msfinance Swagger UI | http://localhost:8081/swagger-ui/index.html |
| RabbitMQ Management UI | http://localhost:15672 (usuario: `admin` / contraseña: `admin_pass`) |

---

## Importar la colección en Postman

### Opción A — Usar los archivos incluidos en el repositorio

En la carpeta [`docs/`](docs/) se encuentran las especificaciones OpenAPI de ambos servicios:

- [`docs/msidentity-api.json`](docs/msidentity-api.json)
- [`docs/msfinance-api.json`](docs/msfinance-api.json)

En Postman: **Import** → seleccioná ambos archivos → se crean las colecciones automáticamente.

### Opción B — Exportar en vivo desde los servicios

Con los servicios corriendo, abrí estas URLs en el browser y guardá el JSON:

- `http://localhost:8080/v3/api-docs` → guardalo como `msidentity-api.json`
- `http://localhost:8081/v3/api-docs` → guardalo como `msfinance-api.json`

En Postman: **Import** → seleccioná los archivos descargados.

---

## Resolución de conflictos de puerto

Si `docker compose up` falla con `port is already allocated`, cambiá el puerto host en [`compose.yaml`](compose.yaml):

```yaml
# antes
- "5434:5432"
# después (elegí un puerto libre, ej. 5436)
- "5436:5432"
```

Y actualizá el valor correspondiente en [`.env`](.env.example):

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5436/msfinance_db
```

---

## Migraciones de base de datos

Ambos servicios usan **Flyway** para gestionar el schema. Al arrancar, Flyway aplica automáticamente los scripts versionados en orden:

```
msidentity/src/main/resources/db/migration/
└── V1__create_person_client_tables.sql

msfinance/src/main/resources/db/migration/
├── V1__create_account_movement_tables.sql
└── V2__create_processed_events_table.sql
└── V3__create_known_clients_table.sql
```

El schema completo consolidado está disponible en [`BaseDatos.sql`](BaseDatos.sql).

---

## Correr los tests

```bash
./gradlew test
```

Corre los tests de ambos servicios. Los tests usan H2 en memoria — no requieren Docker ni base de datos externa.

---

## Estado del proyecto

- [x] `msidentity` — dominio, servicio, persistencia, REST, Swagger, tests
- [x] `msfinance` — dominio, servicio, persistencia, REST, Swagger
- [x] Comunicación asíncrona vía RabbitMQ (DLQ, retries, idempotencia)
- [x] Flyway migrations
- [x] Docker Compose — stack completo (infra + microservicios)
- [x] Prueba unitaria — `ClientServiceTest`
- [x] Prueba de integración — `ClientControllerIT`
- [x] `BaseDatos.sql`
