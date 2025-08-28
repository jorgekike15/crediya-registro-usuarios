FROM amazoncorretto:21.0.8-alpine
WORKDIR /app
COPY applications/app-service/build/libs/crediya-registro-usuarios.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "app.jar"]