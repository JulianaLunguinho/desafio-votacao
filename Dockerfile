FROM openjdk:17.0.2-jdk-oracle
RUN mkdir /app
WORKDIR /app
COPY /target/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
