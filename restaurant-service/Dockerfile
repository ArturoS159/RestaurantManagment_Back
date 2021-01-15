FROM openjdk:11
ADD target/restaurant-service.jar restaurant-service.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=k8s", "-jar", "/restaurant-service.jar"]
EXPOSE 8883