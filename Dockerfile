FROM openliberty/open-liberty:full-java8-openj9-ubi

COPY  --chown=1001:0 target/liberty/wlp/usr/shared /opt/ol/wlp/usr/shared
COPY  --chown=1001:0 target/liberty/wlp/usr/servers/BonusPayout/ /config/
COPY  --chown=1001:0 target/batch-bonuspayout-application.war /config/apps

RUN configure.sh

