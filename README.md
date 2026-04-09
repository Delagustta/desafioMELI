# Item Comparison API

Backend API for item comparison built with Spring Boot, following clean layering and API best practices.

## Strategic Overview

The project has two API scopes:

- `/products` (main scope): comparison-focused API aligned with the challenge requirements.
- `/model` (legacy scope): compatibility endpoints required by pre-existing dynamic tests.

This split allows feature evolution without breaking the existing contract validated by `http00.json` and `http01.json`.

## Tech Stack

- Java 21
- Spring Boot 3.2.5
- Spring Web + Spring Data JPA
- H2 in-memory database
- Springdoc OpenAPI (Swagger UI)
- Lombok
- Maven

## Project Structure

- `controller`: HTTP layer (`ProductController`, `ModelController`)
- `service`: business rules (`ProductService`, `ModelService`)
- `repository`: persistence abstraction (`ProductRepository`, `ModelRepository`)
- `model`: JPA entities (`Product`, `Model`)
- `dto`: request/response contracts (`ProductResponse`, `CompareProductsRequest`, `ApiErrorResponse`)
- `exception`: domain exceptions and global handler (`GlobalExceptionHandler`)
- `config`: bootstrap components (`ProductSeedRunner`)

## Running Locally

Prerequisites:

- JDK 21
- Maven 3.9+

Commands:

```bash
mvn clean test
mvn spring-boot:run
```

Default application URL:

- `http://localhost:8080`

Swagger UI:

- `http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8080/v3/api-docs`

H2 console:

- `http://localhost:8080/h2-console`

## Seed Data

Products are loaded at startup from:

- `src/main/resources/products.json`

Bootstrap is done by:

- `ProductSeedRunner` (`ApplicationRunner`)

The runner only seeds when the product table is empty.

## Main Endpoints (Item Comparison)

### `GET /products`

Returns all products with:

- `id`
- `name`
- `imageUrl`
- `description`
- `price`
- `rating`
- `specifications` (`Map<String, String>`)

### `GET /products/{id}`

Returns one product by id.

- `200` when found
- `404` when not found

### `POST /products/compare`

Compares multiple products by ids.

Request body:

```json
{
  "ids": [101, 102, 103]
}
```

Behavior:

- Returns products in the same order as requested ids.
- `400` for invalid payload (for example empty ids).
- `404` if at least one requested id does not exist.

## Error Handling

Errors are centralized in `GlobalExceptionHandler` with standardized response payload:

- `timestamp`
- `status`
- `error`
- `message`
- `path`
- `details`

Mapped scenarios:

- business bad request -> `400`
- missing resource -> `404`
- validation errors -> `400`
- unexpected errors -> `500`

## Legacy Contract for Dynamic Tests

The following endpoints are intentionally preserved to satisfy existing dynamic tests:

- `POST /model`
- `GET /model`
- `GET /model/{id}`
- `DELETE /model/{id}`
- `DELETE /erase`

Dynamic test resources:

- `src/test/resources/testcases/http00.json`
- `src/test/resources/testcases/http01.json`

## Testing Notes

- `mvn test` runs integration-style dynamic tests (`HttpJsonDynamicUnitTest`).
- The test suite also writes custom reports under `target/customReports/`.

