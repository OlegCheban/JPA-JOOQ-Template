FROM eclipse-temurin:25-jre AS builder
WORKDIR /application
COPY target/demo.jar demo.jar
RUN java -Djarmode=layertools -jar demo.jar extract

FROM eclipse-temurin:25-jre
WORKDIR /application
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./
ENV JAVA_TOOL_OPTIONS="-XX:MaxRAMPercentage=80"
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]