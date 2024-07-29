### Exactabank

# Spring Boot Project

Este é um projeto Spring Boot configurado para desenvolvimento de aplicações Java. Este arquivo README fornece uma visão geral das principais configurações e dependências do projeto.

## Estrutura do Projeto

O projeto utiliza o `spring-boot-starter-parent` como parent para fornecer uma configuração padrão e simplificada para o Spring Boot. Abaixo está a configuração básica do `pom.xml`:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.2</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>

<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
            <version>2.6.0</version>
        </dependency>
        <dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-core</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <version>42.7.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${org.mapstruct.version}</version>
        </dependency>
    </dependencies>
```


## Como executar o projeto
O comando abaixo roda a aplicação em `docker`, ao executa-lo ele faz a build de uma imagem `postgres` e da aplicação `exatabank`

```shellscript
docker compose up
```

## Acessá-lo após a build
A aplicação estará disponível após a build na porta 8080 em [http://localhost:8080/swagger](http://localhost:8080/swagger)


## Desenvolvimento
Para rodar somente a base de dado

```shellscript
 docker compose -f docker-compose-pg.yml up
```
