
FROM openjdk:23-ea-21-jdk-slim

RUN apt-get update -y && \
    apt-get install -y curl unzip netcat-openbsd && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /opt/app

COPY ./mvnw ./mvnw
COPY ./mvnw.cmd ./mvnw.cmd
RUN chmod +x ./mvnw

COPY . .

RUN ./mvnw clean install

RUN cp target/bank-*.jar app.jar

COPY entrypoint.sh /opt/app/entrypoint.sh
RUN chmod +x /opt/app/entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/opt/app/entrypoint.sh"]