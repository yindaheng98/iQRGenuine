FROM gradle:jdk8
COPY ./ /app
WORKDIR /app
RUN ./gradlew wa

FROM jetty:9-jre8-alpine

COPY --from=0 /app/build/libs/iQRGenuine.war /var/lib/jetty/webapps/iQRGenuine.war