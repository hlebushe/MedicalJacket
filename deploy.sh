#!/bin/bash
set -x #echo on
SERVER_IP=159.65.136.232
echo 'Stopping tomcat'
ssh root@$SERVER_IP 'service tomcat stop'
echo 'Uploading new .war'
scp ./target/mj-2.0.war root@$SERVER_IP:/opt/tomcat/webapps/mj.war
echo 'Starting the Tomcat...'
ssh root@$SERVER_IP 'service tomcat start'
echo 'Deployed successfully!'
