# Travel Assistant API Specifications

This directory contains OpenAPI 3.0 specifications for all the Travel Assistant microservices.

## Services Overview

### 1. API Gateway (Port 8080)

- **File**: `api-gateway-openapi.json`
- **Base URL**: `http://localhost:8080`
- **Purpose**: Central entry point that routes requests to appropriate microservices
- **Key Features**:
  - Health monitoring endpoints
  - Route and service discovery information
  - Proxies requests to backend services

### 2. Auth Service (Port 8020)

- **File**: `auth-service-openapi.json`
- **Base URL**: `http://localhost:8080/auth-service` (via gateway)
- **Direct URL**: `http://localhost:8020` (direct access)
- **Purpose**: User authentication and management
- **Key Endpoints**:
  - `POST /api/v1/auth/login` - User login
  - `POST /api/v1/auth/register` - User registration
  - `GET /api/v1/auth/users` - Get all users

### 3. Trip Service (Port 8000)

- **File**: `trip-service-openapi.json`
- **Base URL**: `http://localhost:8080/trip-service` (via gateway)
- **Direct URL**: `http://localhost:8000` (direct access)
- **Purpose**: Trip management and CRUD operations
- **Key Endpoints**:
  - `GET /api/v1/trips` - Get all trips
  - `GET /api/v1/trips/{tripId}` - Get trip by ID
  - `POST /api/v1/trips/create` - Create new trip
  - `PUT /api/v1/trips/{tripId}` - Update trip
  - `DELETE /api/v1/trips/{tripId}` - Delete trip

### 4. Suggestion Service (Port 8010)

- **File**: `suggestion-service-openapi.json`
- **Base URL**: `http://localhost:8080/suggestion-service` (via gateway)
- **Direct URL**: `http://localhost:8010` (direct access)
- **Purpose**: Travel suggestions and recommendations
- **Key Endpoints**:
  - `GET /api/v1/suggestions/trip/{tripId}` - Get suggestions for a trip
  - `GET /api/v1/suggestions/{suggestionId}` - Get suggestion by ID
  - `GET /api/v1/suggestions/health` - Health check

## Authentication

Most endpoints require JWT Bearer authentication. To authenticate:

1. Register or login using the auth service endpoints
2. Include the JWT token in the `Authorization` header: `Bearer <token>`

### Public Endpoints (No Authentication Required)

- Auth service: login and register endpoints
- Gateway: health and info endpoints
- Suggestion service: health endpoint

## Usage Examples

### 1. User Registration

```bash
curl -X POST http://localhost:8080/auth-service/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 2. User Login

```bash
curl -X POST http://localhost:8080/auth-service/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### 3. Create a Trip (Requires Authentication)

```bash
curl -X POST http://localhost:8080/trip-service/api/v1/trips/create \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "X-User-Id: <user-uuid>" \
  -d '{
    "destination": "Paris, France",
    "startsAt": "2024-06-15",
    "endsAt": "2024-06-22"
  }'
```

### 4. Get Trip Suggestions

```bash
curl -X GET http://localhost:8080/suggestion-service/api/v1/suggestions/trip/<trip-id> \
  -H "Authorization: Bearer <your-jwt-token>"
```

## For Frontend Development

### Using in React/Next.js

You can use these specifications to:

1. **Generate TypeScript Types**: Use tools like `openapi-typescript` to generate TypeScript interfaces
2. **Create API Clients**: Use libraries like `openapi-fetch` or `swagger-codegen`
3. **Mock API Data**: Use the schemas for creating mock data during development

### Code Generation Example

```bash
# Install openapi-typescript
npm install -D openapi-typescript

# Generate TypeScript types
npx openapi-typescript src/specs/auth-service-openapi.json -o src/types/auth-api.ts
npx openapi-typescript src/specs/trip-service-openapi.json -o src/types/trip-api.ts
npx openapi-typescript src/specs/suggestion-service-openapi.json -o src/types/suggestion-api.ts
```

### Viewing in Swagger UI

You can view and test these APIs using Swagger UI:

1. Install swagger-ui-express: `npm install swagger-ui-express`
2. Serve the JSON files through a local server
3. Or use online Swagger Editor: https://editor.swagger.io/

## Development Workflow

1. **Start Backend Services**: Run `docker-compose up` from the root directory
2. **Verify Services**: Check health endpoints to ensure all services are running
3. **Use Gateway**: All frontend requests should go through the API Gateway (port 8080)
4. **Authentication Flow**: Login → Get JWT → Use JWT for subsequent requests

## Notes

- All timestamps are in ISO 8601 format
- UUIDs are used for all entity identifiers
- The API Gateway handles CORS for frontend requests
- Rate limiting and circuit breakers are implemented at the gateway level
- All services include health check endpoints for monitoring

## Support

For questions or issues related to these APIs, please contact Fernando Torres.
