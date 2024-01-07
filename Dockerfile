# Build the application jar ignoring the tests
FROM maven:3.8.4-openjdk-17 as builder

ENV SRC_DIR=/usr/src/app

COPY pom.xml ${SRC_DIR}/
COPY src/ ${SRC_DIR}/src/
COPY lombok.config ${SRC_DIR}/

WORKDIR ${SRC_DIR}

RUN mvn -DskipTests -PPipeline package

# Build the application runtime image
FROM openjdk:17-jdk-slim-buster

LABEL APPLICATION="bookstore"

EXPOSE 8080/tcp

ENV JAVA_OPTS=""

COPY --from=builder /usr/src/app/target/bookstore-*.jar /

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar bookstore-*.jar"]
