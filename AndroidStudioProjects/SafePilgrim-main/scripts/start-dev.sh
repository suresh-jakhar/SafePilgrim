#!/bin/bash

echo "🚀 Starting SafePilgrim Development Environment..."

# Check if .env file exists
if [ ! -f .env ]; then
    echo "⚠️  .env file not found. Copying from env.example..."
    cp env.example .env
    echo "📝 Please edit .env file with your actual values before continuing."
    echo "   Press Enter when ready, or Ctrl+C to exit."
    read
fi

# Start the microservices
echo "🐳 Starting Docker containers..."
docker-compose up -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 30

# Check service health
echo "🔍 Checking service health..."
curl -f http://localhost:8080/health || echo "❌ API Gateway not ready"
curl -f http://localhost:8081/actuator/health || echo "❌ Android Companion not ready"

echo ""
echo "✅ SafePilgrim Development Environment is running!"
echo ""
echo "📱 Android App: Install the APK from app/build/outputs/apk/debug/"
echo "🌐 API Gateway: http://localhost:8080"
echo "🔧 Android Companion: http://localhost:8081"
echo "🗄️  Database: localhost:5432"
echo "📊 Redis: localhost:6379"
echo ""
echo "📋 Available endpoints:"
echo "  - Health Check: http://localhost:8080/health"
echo "  - Mobile Config: http://localhost:8080/api/v1/mobile/config"
echo "  - Digital ID Service: http://localhost:8080/api/v1/digital-id/"
echo "  - Geofencing Service: http://localhost:8080/api/v1/geofencing/"
echo "  - AI Analytics Service: http://localhost:8080/api/v1/ai-analytics/"
echo "  - Emergency Service: http://localhost:8080/api/v1/emergency/"
echo ""
echo "🛑 To stop: docker-compose down"
