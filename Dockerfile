FROM gradle:latest AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle clean build -x test

FROM openjdk:18-alpine
EXPOSE 8080 5432 15672 5672
RUN mkdir /app
COPY --from=build /app/build/libs/*.jar /app/assembly-voter.jar
ENTRYPOINT ["java", "-jar", "/app/assembly-voter.jar"]