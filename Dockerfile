FROM tomcat:9.0-jdk8
LABEL maintainer="victor.k.lee@ontario.ca"
ADD target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]