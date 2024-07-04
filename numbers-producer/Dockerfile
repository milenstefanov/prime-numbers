FROM amazoncorretto:21.0.3-alpine
WORKDIR /opt/prime-numbers
RUN mkdir -p /opt/prime-numbers/data
COPY target/numbers-producer-0.0.1-SNAPSHOT.jar ./numbers-producer-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/opt/prime-numbers/numbers-producer-0.0.1-SNAPSHOT.jar"]