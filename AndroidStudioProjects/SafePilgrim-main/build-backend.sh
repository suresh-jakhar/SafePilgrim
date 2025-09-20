#!/bin/bash

echo "Building SafePilgrim Backend Services..."

# Build Digital ID Service
echo "Building Digital ID Service..."
cd backend/digital-id-service
mvn clean package -DskipTests
if [ $? -eq 0 ]; then
    echo "Digital ID Service built successfully"
else
    echo "Failed to build Digital ID Service"
    exit 1
fi
cd ../..

echo "All backend services built successfully!"
echo "You can now run: docker-compose up -d"
