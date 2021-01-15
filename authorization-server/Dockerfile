FROM openjdk:11
ADD target/authorization-server.jar authorization-server.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "/authorization-server.jar"]
EXPOSE 8882