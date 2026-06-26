FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/nullbank-*.jar /app/nullbank.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/nullbank.jar"]
