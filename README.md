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
infrastructure/config      ← SecurityConfig, OpenApiConfig
```

---

## Prerrequisitos

- Java 21
- Docker Desktop (recomendado) **o** PostgreSQL + RabbitMQ instalados localmente
- IntelliJ IDEA (cualquier edición)

---

## Levantar localmente

### Opción 1 — Docker Compose (recomendado)

Desde la raíz del proyecto:

```bash
docker compose up -d
```

Esto levanta tres containers:

| Container | Puerto host | Descripción |
|---|---|---|
| `postgres-msidentity` | `5435` | Base de datos de msidentity |
| `postgres-msfinance` | `5434` | Base de datos de msfinance |
| `rabbitmq` | `5672` / `15672` | Broker de mensajes / UI de gestión |

> **¿Por qué 5435 y 5434 y no 5432/5433?**
> En Windows es común tener PostgreSQL instalado como servicio nativo en el puerto `5432`. Docker no puede reclamar ese puerto si ya está ocupado. Para evitar la colisión se mapearon los containers a `5435` y `5434`. Si tu máquina no tiene PostgreSQL instalado localmente podés cambiar los puertos en [`compose.yaml`](compose.yaml) y en los `application.properties` correspondientes.

Una vez que los containers están corriendo, levantá cada servicio desde IntelliJ (ver más abajo). Las tablas se crean automáticamente al primer arranque gracias a `spring.jpa.hibernate.ddl-auto=update`.

---

### Opción 2 — PostgreSQL y RabbitMQ locales (sin Docker)

Si ya tenés PostgreSQL y RabbitMQ instalados en tu máquina, creá las bases de datos y usuarios manualmente:

**msidentity**
```sql
CREATE USER msidentity_user WITH PASSWORD 'msidentity_pass';
CREATE DATABASE msidentity_db OWNER msidentity_user;
```

**msfinance**
```sql
CREATE USER msfinance_user WITH PASSWORD 'msfinance_pass';
CREATE DATABASE msfinance_db OWNER msfinance_user;
```

RabbitMQ debe estar corriendo con usuario `admin` / contraseña `admin_pass`, o actualizá los valores en los `application.properties`.

---

## Correr desde IntelliJ IDEA

Al ser un multi-project build, IntelliJ detecta **dos aplicaciones Spring Boot independientes**. No hay un único entry point — cada microservicio tiene el suyo.

**Paso 1** — Abrí [`msidentity/.../MsidentityApplication.java`](msidentity/src/main/java/com/devsu/msidentity/MsidentityApplication.java) y hacé click en el ícono ▶ verde al lado del método `main`. IntelliJ crea la run configuration automáticamente.

**Paso 2** — Repetí lo mismo con [`msfinance/.../MsfinanceApplication.java`](msfinance/src/main/java/com/devsu/msfinance/MsfinanceApplication.java).

Ahora tenés **dos run configurations independientes** en el dropdown de IntelliJ. Podés correr o debuggear cada servicio por separado.

> ⚠️ **Importante:** IntelliJ corre las apps directamente en Windows, por lo que `localhost` resuelve al stack de red de Windows. Si tenés PostgreSQL instalado localmente en el puerto `5432`, la app va a intentar conectarse ahí en lugar del container Docker. La solución es usar Docker Compose con los puertos `5435`/`5434` como se describió arriba.

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

## Resolución de conflictos de puerto

Si `docker compose up -d` falla con:

```
Error response from daemon: Bind for 0.0.0.0:5434 failed: port is already allocated
```

Significa que ese puerto está ocupado en tu máquina. Cambiá el puerto host en [`compose.yaml`](compose.yaml):

```yaml
# antes
- "5434:5432"
# después (elegí un puerto libre, ej. 5436)
- "5436:5432"
```

Y actualizá el `application.properties` del servicio correspondiente:

```properties
# msfinance/src/main/resources/application.properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5436/msfinance_db}
```

---

## Nota sobre migraciones de base de datos

Actualmente ambos servicios usan `spring.jpa.hibernate.ddl-auto=update`. Hibernate crea y actualiza las tablas automáticamente al arrancar. Implementar migrations es overkill y no estaba contenplado en el test.Esto es suficiente para desarrollo, pero tiene limitaciones importantes:


| Operación | `ddl-auto=update` |
|---|---|
| Agregar columna nueva | ✅ la crea |
| Eliminar columna del código | ❌ la deja en la DB |
| Renombrar columna | ❌ crea una nueva, deja la vieja |
| Migrar datos existentes | ❌ no sabe nada de los datos |

Para un entorno productivo la solución correcta es **Flyway**: archivos SQL versionados que se ejecutan en orden y se registran en una tabla de historial.

```
db/migration/
├── V1__create_person_client_tables.sql
├── V2__create_account_movement_tables.sql
└── V3__add_index_on_client_identification.sql
```

Con Flyway activo, `ddl-auto` debe cambiarse a `validate` o `none`.

---

## Estado del proyecto

- [x] `msidentity` — dominio, servicio, persistencia, REST, Swagger
- [ ] `msfinance` — en construcción (dominio completo, application + REST pendientes)
- [ ] Comunicación entre servicios vía RabbitMQ
- [ ] Docker Compose para levantar ambos microservicios completos
