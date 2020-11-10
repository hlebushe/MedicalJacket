#!/bin/bash
set -x #echo on
SERVER_IP=68.183.115.129
echo 'Uploading new .war'
scp ./target/mj-2.0.war root@$SERVER_IP:/var/lib/tomcat9/webapps/mj.war
echo 'Restarting the Tomcat...'
ssh root@$SERVER_IP 'service tomcat9 stop | service tomcat9 start'
echo 'Deployed successfully!'
