FROM openjdk:11
ARG JAR_FILE_PATH=../build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
ENTRYPOINT ["java", "-jar","-Dspring.profiles.active=dev","-Duser.timezone=Asia/Seoul","app.jar"]
