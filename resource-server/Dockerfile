FROM openjdk:11
ADD target/resource-server.jar resource-server.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "/resource-server.jar"]
EXPOSE 8881