FROM openliberty/open-liberty:full-java8-openj9-ubi
COPY target/liberty/wlp/usr/servers/BonusPayout/ /config/
ADD target/batch-bonuspayout-application.war /config/dropins
USER 0
RUN chmod -R 777 /opt/ol
USER 1001
RUN configure.sh

