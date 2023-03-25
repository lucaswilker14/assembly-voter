FROM gradle:latest AS build
COPY --chown=gradle:gradle . /app/prod
WORKDIR /app/prod
RUN gradle build -x test

FROM openjdk:18-alpine
EXPOSE 8080 5432 15672 5672
RUN mkdir /app
COPY --from=build /app/prod/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]