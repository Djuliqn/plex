FROM openjdk:11
ENV JASYPT_ENCRYPTOR_PASSWORD=plex-secret-key
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} plex.jar
ENTRYPOINT ["java","-jar","/plex.jar"]