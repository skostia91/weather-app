FROM maven:3 AS build
COPY /src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package -PskipTestContainers

FROM eclipse-temurin:17-jre
WORKDIR /
COPY --from=build /target/*.jar test.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "test.jar"]