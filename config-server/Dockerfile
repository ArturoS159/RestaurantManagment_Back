FROM openjdk:11
ADD target/config-server.jar config-server.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "/config-server.jar"]
EXPOSE 8888