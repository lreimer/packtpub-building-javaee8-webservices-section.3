FROM payara/micro:5-SNAPSHOT
COPY target/content-service.war /opt/payara/deployments

ENTRYPOINT ["java", "-Xmx128m", "-client", "-jar", "/opt/payara/payara-micro.jar"]
CMD ["--noCluster", "--deploymentDir", "/opt/payara/deployments"]