FROM openliberty/open-liberty:full-java8-openj9-ubi

USER 0

COPY target/liberty/wlp/usr/shared /opt/ol/wlp/usr/shared
COPY target/liberty/wlp/usr/servers/BonusPayout/ /config/
ADD target/batch-bonuspayout-application.war /config/apps

RUN configure.sh

