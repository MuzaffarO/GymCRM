#!/bin/sh

EUREKA_URL=http://eureka-server:8761/eureka/apps

echo "🔄 Waiting for Eureka to be available at $EUREKA_URL..."

until curl -s $EUREKA_URL > /dev/null; do
  echo "❌ Eureka not reachable yet..."
  sleep 5
done

echo "✅ Eureka is up. Starting main app..."
exec java -jar app.jar
