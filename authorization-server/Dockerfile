FROM openjdk:11
ADD target/authorization-server.jar authorization-server.jar
ENTRYPOINT ["java", "-jar", "/authorization-server.jar"]
EXPOSE 8882