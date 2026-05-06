# Contenerizacion del proyecto (Frontend + Backend + MySQL)

## Requisitos
- Docker
- Docker Compose

## Estructura
- `frontend-unab-master`: Angular 10, servido con Nginx en contenedor
- `backend-unab-master`: Spring Boot (Java 11) en contenedor
- `mysql`: base de datos en contenedor independiente con volumen persistente

## Alcance
Este archivo describe el flujo local con Docker Compose.

Para AWS de menor costo, el frontend no requiere contenedor en produccion:

- Angular compilado
- archivos estaticos en S3
- CloudFront para distribucion y cache
- reglas de SPA en CloudFront/S3
- backend expuesto por una ruta `/api/*` o por una URL absoluta configurada en build si se necesitara

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
- Frontend: `http://localhost:3000`
- Backend: `http://localhost:9090`
- MySQL: `localhost:3306`

## Validacion rapida
1. Ejecutar `docker compose up --build -d`.
2. Verificar contenedores activos con `docker compose ps`.
3. Abrir `http://localhost:3000`.
4. Probar autenticacion/consumo de endpoints contra `http://localhost:9090`.

## Nota para AWS
Para despliegue estatico en AWS:

- publicar `frontend-unab-master/dist/frontend-unab` en S3
- servir el bucket con CloudFront
- configurar fallback de SPA para `index.html` en errores 403/404
- enrutar `/api/*` desde CloudFront hacia el backend para evitar hardcodear hosts en el frontend
