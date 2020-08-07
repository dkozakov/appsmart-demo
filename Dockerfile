FROM openjdk:14-jdk-oracle
COPY rest/target/demo-rest-*.jar demo-rest.jar
ENTRYPOINT ["java","-jar","/demo-rest.jar"]