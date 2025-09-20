# SafePilgrim Microservices Backend

This directory contains the Docker-based microservices backend for the SafePilgrim application.

## Architecture

The backend consists of the following microservices:

1. **API Gateway (Nginx)** - Routes requests to appropriate microservices
2. **Digital ID Service** - Handles digital ID generation, verification, and updates
3. **Emergency Service** - Manages emergency alerts and responses
4. **Geofencing Service** - Handles geofence creation and location monitoring
5. **AI Analytics Service** - Provides safety scoring and risk prediction
6. **PostgreSQL** - Database for all services
7. **Redis** - Caching layer
8. **TensorFlow Serving** - AI model serving

## Quick Start

### Prerequisites

- Docker and Docker Compose
- Maven (for building Java services)
- At least 4GB RAM available for Docker

### Building and Running

1. **Build the backend services:**
   ```bash
   ./build-backend.sh
   ```

2. **Start all services:**
   ```bash
   docker-compose up -d
   ```

3. **Check service health:**
   ```bash
   curl http://localhost:8080/health
   ```

4. **View logs:**
   ```bash
   docker-compose logs -f
   ```

### API Endpoints

The API Gateway runs on port 8080 and routes requests to the appropriate microservices:

- **Digital ID Service**: `http://localhost:8080/api/digital-id/`
- **Emergency Service**: `http://localhost:8080/api/emergency/`
- **Geofencing Service**: `http://localhost:8080/api/geofencing/`
- **AI Analytics Service**: `http://localhost:8080/api/analytics/`

### Testing the Digital ID Service

```bash
# Generate a Digital ID
curl -X POST http://localhost:8080/api/digital-id/generate \
  -H "Content-Type: application/json" \
  -d '{
    "passportData": {
      "passportNumber": "A1234567",
      "name": "John Doe",
      "nationality": "US",
      "dateOfBirth": "1990-01-01",
      "expiryDate": "2030-01-01"
    },
    "emergencyContacts": [
      {
        "name": "Jane Doe",
        "relationship": "Spouse",
        "phoneNumber": "+1234567890",
        "email": "jane@example.com"
      }
    ],
    "travelItinerary": {
      "entryDate": "2024-01-01",
      "exitDate": "2024-01-15",
      "plannedDestinations": []
    },
    "kycDocuments": [],
    "biometricData": null
  }'
```

### Stopping Services

```bash
docker-compose down
```

### Development

To add new microservices:

1. Create a new directory under `backend/`
2. Add the service to `docker-compose.yml`
3. Update `nginx.conf` with new routes
4. Add database initialization in `init-db.sql`

### Troubleshooting

- **Port conflicts**: Make sure ports 8080, 5432, and 6379 are available
- **Memory issues**: Increase Docker memory allocation
- **Build failures**: Check Maven and Java versions
- **Database connection**: Ensure PostgreSQL is running and accessible

### Monitoring

- **Health checks**: `http://localhost:8080/health`
- **Service logs**: `docker-compose logs [service-name]`
- **Database**: Connect to `localhost:5432` with user `safepilgrim`
- **Redis**: Connect to `localhost:6379`
