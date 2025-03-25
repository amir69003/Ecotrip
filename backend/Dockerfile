FROM openjdk:21-jdk-slim

WORKDIR /app

# execute mvn clean install -DskipTests
CMD ["mvn", "clean", "install", "-DskipTests"]

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
