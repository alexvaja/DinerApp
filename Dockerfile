FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/dinerapp.jar
ADD ${JAR_FILE} dinerapp.jar
COPY src/main/resourrces/logback.xml /logback.xml
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dlogging.config=/logback.xml", "-jar","/dinerapp.jar"]
