#!/bin/bash

echo "🚀 Building SafePilgrim Microservices System..."

# Build Android app
echo "📱 Building Android app..."
cd app
./gradlew assembleDebug
if [ $? -ne 0 ]; then
    echo "❌ Android app build failed"
    exit 1
fi
cd ..

# Build companion API
echo "🔧 Building Android companion API..."
cd android-companion-api
./gradlew build
if [ $? -ne 0 ]; then
    echo "❌ Companion API build failed"
    exit 1
fi
cd ..

# Build Docker images
echo "🐳 Building Docker images..."
docker-compose build

echo "✅ All components built successfully!"
echo ""
echo "To start the system:"
echo "  ./scripts/start-dev.sh"
echo ""
echo "To deploy to production:"
echo "  ./scripts/deploy-prod.sh"
