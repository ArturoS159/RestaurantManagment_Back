FROM openjdk:11
ADD target/order-service.jar order-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "/order-service.jar"]
EXPOSE 8884