# QR Product Validator Server

Aplicación Spring Boot para validar productos mediante QR. Base de datos en PostgreSQL vía Docker y ejecución local desde IntelliJ IDEA.

---

## Requisitos
- Java 21 (JDK 21)
- Maven 3.9+
- Docker y Docker Compose
- IntelliJ IDEA (recomendado)

## Stack y puertos
- Spring Boot 3.5.6 — puerto app: `8081`
- PostgreSQL 15 — puerto expuesto: `5432`
- pgAdmin 4 — puerto expuesto: `80`

## Variables y credenciales
- PostgreSQL
  - `POSTGRES_DB`: `db`
  - `POSTGRES_USER`: `admin`
  - `POSTGRES_PASSWORD`: `admin`
- JDBC (configurado en `src/main/resources/application.properties`)
  - `spring.datasource.url=jdbc:postgresql://localhost:5432/db`
  - `spring.datasource.username=admin`
  - `spring.datasource.password=admin`

## Endpoints útiles
- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api/docs`

---

## 1) Levantar la base de datos con Docker

```bash
# En la raíz del proyecto
docker compose up -d postgres pgadmin

# Ver logs (opcional)
docker compose logs -f postgres
```

- El contenedor `postgres` expone `5432:5432` y tiene healthcheck.
- El contenedor `pgadmin` expone `80:80`.

Accede a pgAdmin: `http://localhost/`
- Email: `admin@admin.com`
- Password: `admin`

Registrar servidor en pgAdmin:
- General > Name: `Local Postgres`
- Connection > Host name/address: `postgres` (porque pgAdmin corre en la misma red de Docker)
- Port: `5432`
- Maintenance DB: `db`
- Username: `admin`
- Password: `admin`

Si prefieres conectarte desde un cliente local (DBeaver, IntelliJ, psql):
- Host: `localhost`
- Port: `5432`
- DB: `db`
- User: `admin`
- Password: `admin`

---

## 2) Ejecutar la aplicación en IntelliJ IDEA (modo local)
1. Abre el proyecto.
2. Asegúrate de usar SDK de proyecto: `Java 21`.
3. Espera a que Maven resuelva dependencias.
4. Inicia la clase `QrProductValidatorServerApplication` (botón Run ▶️).
5. Verifica que arranca en `http://localhost:8081` y abre Swagger: `http://localhost:8081/swagger-ui.html`.

> La app usa `spring.jpa.hibernate.ddl-auto=update`, por lo que creará/actualizará tablas automáticamente.

### Ejecutar con Maven (alternativa)
```bash
mvn spring-boot:run
```

---

## Flujo completo (TL;DR)
1. `docker compose up -d postgres pgadmin`
2. Abrir IntelliJ y ejecutar `QrProductValidatorServerApplication`.
3. Probar API en `http://localhost:8081/swagger-ui.html`.

---

## Troubleshooting
- Puerto 5432 ocupado (Postgres):
  - Detén el proceso que usa 5432 o ajusta el mapeo en `Docker-compose.yaml` y `spring.datasource.url`.
- Puerto 80 ocupado (pgAdmin):
  - Cambia el mapeo a `8080:80` en `Docker-compose.yaml` y accede a `http://localhost:8080/`.
- La app no conecta a la BD:
  - Espera a que `postgres` esté healthy (usa `docker compose ps` o `logs`).
  - Confirma credenciales y URL JDBC en `application.properties`.
- Error de versión Java:
  - Configura el SDK del proyecto a Java 21 en IntelliJ.

---

## Scripts útiles
```bash
# Ver estado y salud
docker compose ps

# Ver logs
docker compose logs -f postgres

# Reiniciar servicios
docker compose restart postgres pgadmin

# Apagar y mantener datos
docker compose down

# Apagar y borrar volúmenes (¡destruye datos!)
docker compose down -v
```

---

## Proyecto
- Java: `21`
- Spring Boot: `3.5.6`
- DB: PostgreSQL `15-alpine`
- OpenAPI UI: `springdoc-openapi-starter-webmvc-ui`
- QR: ZXing (`core`/`javase` 3.4.1)

> Cualquier cambio de puertos/credenciales actualízalo tanto en `Docker-compose.yaml` como en `application.properties` para mantener el entorno consistente.
