ARG DEPENDENCY_IMAGE=dependency
ARG BUILD_IMAGE=build

FROM amazoncorretto:20 AS dependency
ADD . /build
WORKDIR /build

# Download maven dependecies
RUN sh ./mvnw -DskipTests package
RUN sh ./mvnw -Dtest=EmptyTest -DfailIfNoTests=false test
RUN sh ./mvnw clean
RUN rm -rf /build



FROM ${DEPENDENCY_IMAGE} AS pre-build
ADD . /build
WORKDIR /build
RUN sh ./mvnw clean



FROM pre-build AS build
# Build native image
RUN sh ./mvnw -DskipTests package



FROM pre-build as dev
CMD [ "sh", "./mvnw", "-DskipTests", "spring-boot:run", "--add-opens=java.base/java.util=ALL-UNNAMED" ]



FROM pre-build AS test
CMD [ "sh", "-c", "./mvnw test" ]



FROM ${BUILD_IMAGE} as pre-final
FROM amazoncorretto:20 as final

COPY --from=pre-final "/build/target/fulltext-across-datasources.jar" fulltext-across-datasources.jar

ENTRYPOINT ["java", "-jar","fulltext-across-datasources.jar", "--add-opens=java.base/java.util=ALL-UNNAMED"]
EXPOSE 8080
