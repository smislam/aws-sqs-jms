FROM maven:3-amazoncorretto-17 AS BUILDER
WORKDIR /app
COPY . /app
ENV AWS_REGION=us-east-1
RUN mvn clean install

FROM amazoncorretto:17
WORKDIR /
COPY --from=BUILDER /app/target/*.jar app.jar

ENV PORT 8080
EXPOSE ${PORT}
CMD ["java","-jar","app.jar","-Dserver.port=${PORT}"]