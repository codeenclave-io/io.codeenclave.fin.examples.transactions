FROM openjdk:8-jre-alpine

ADD  build/libs/io.codeenclave.fin.examples.service.transactions-0.0.1.jar /io.codeenclave.fin.examples.service.transactions/app.jar
EXPOSE 8080

CMD ["java", "-jar", "/io.codeenclave.fin.examples.service.transactions/app.jar"]
