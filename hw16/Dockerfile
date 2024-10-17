FROM bellsoft/liberica-openjdk-alpine-musl:17
COPY /target/hw16-docker-0.0.1-SNAPSHOT.jar /app/library.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/library.jar"]
