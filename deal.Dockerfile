FROM openjdk:21-slim
LABEL authors="dashavav"
COPY build/libs/deal*.jar /deal.jar
ENTRYPOINT ["java", "-jar", "/deal.jar"]