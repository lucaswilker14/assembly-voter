FROM gradle:latest AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test

FROM openjdk:18-alpine
EXPOSE 8080 5432 15672 5672
RUN mkdir /app
ARG JAR_FILE
COPY --from=build ${JAR_FILE} /app/assembly-voter.jar/
CMD java -jar /app/assembly-voter.jar