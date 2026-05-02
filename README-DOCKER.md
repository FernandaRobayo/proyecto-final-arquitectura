# Contenerizacion del proyecto (Frontend + Backend + MySQL)

## Requisitos
- Docker
- Docker Compose

## Estructura
- `frontend-unab-master`: Angular 10, servido con Nginx en contenedor
- `backend-unab-master`: Spring Boot (Java 11) en contenedor
- `mysql`: base de datos en contenedor independiente con volumen persistente

## Variables de entorno
Configura valores en el archivo `.env` en la raiz del proyecto:

- `MYSQL_ROOT_PASSWORD`
- `MYSQL_DATABASE`
- `MYSQL_USER`
- `MYSQL_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`

## Comandos de operacion
Levantar todo:

```bash
docker compose up --build -d
```

Ver logs del backend:

```bash
docker compose logs -f backend
```

Detener contenedores:

```bash
docker compose down
```

Detener y borrar volumenes (limpieza total de datos):

```bash
docker compose down -v
```

## URLs esperadas
- Frontend: `http://localhost:8080`
- Backend: `http://localhost:9090`
- MySQL: `localhost:3306z`

## Validacion rapida
1. Ejecutar `docker compose up --build -d`.
2. Verificar contenedores activos con `docker compose ps`.
3. Abrir `http://localhost:4200`.
4. Probar autenticacion/consumo de endpoints contra `http://localhost:9090`.
