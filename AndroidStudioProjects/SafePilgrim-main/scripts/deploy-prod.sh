#!/bin/bash

echo "🚀 Deploying SafePilgrim to Production..."

# Check if production .env exists
if [ ! -f .env.prod ]; then
    echo "❌ Production .env file (.env.prod) not found!"
    echo "   Please create .env.prod with production values."
    exit 1
fi

# Build production images
echo "🏗️  Building production images..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml build

# Deploy to production
echo "🚀 Deploying to production..."
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up -d

echo "✅ SafePilgrim deployed to production!"
echo ""
echo "🔍 Check deployment status:"
echo "  docker-compose -f docker-compose.yml -f docker-compose.prod.yml ps"
echo ""
echo "📊 View logs:"
echo "  docker-compose -f docker-compose.yml -f docker-compose.prod.yml logs -f"
