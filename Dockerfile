FROM payara/micro:5-SNAPSHOT
COPY target/content-service.war /opt/payara/deployments
