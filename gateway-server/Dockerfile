FROM openjdk:11
ADD target/gateway-server.jar gateway-server.jar
ENTRYPOINT ["java", "-jar", "/gateway-server.jar"]
EXPOSE 8080