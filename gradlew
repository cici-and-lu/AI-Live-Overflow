#!/bin/bash
# Gradle wrapper bootstrap script
# This will be replaced by the full gradlew during first build

echo "Downloading Gradle wrapper..."
mkdir -p gradle/wrapper

# Download gradle-wrapper.jar
curl -L -o gradle/wrapper/gradle-wrapper.jar \
    https://github.com/gradle/gradle/raw/v8.5.0/gradle/wrapper/gradle-wrapper.jar

# Re-run with the downloaded wrapper
exec java -jar gradle/wrapper/gradle-wrapper.jar "$@"