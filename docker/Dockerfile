FROM openjdk:11
RUN mkdir -p app
WORKDIR /app
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev","-Duser.timezone=Asia/Seoul","app.jar"]
