FROM openjdk:12-jdk-alpine

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME

ADD ../build.gradle $HOME

RUN gradle build