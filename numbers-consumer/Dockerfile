FROM amazoncorretto:21.0.3-alpine
WORKDIR /opt/prime-numbers
RUN mkdir -p /opt/prime-numbers/data
COPY target/numbers-consumer-0.0.1-SNAPSHOT.jar ./numbers-consumer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/opt/prime-numbers/numbers-consumer-0.0.1-SNAPSHOT.jar"]