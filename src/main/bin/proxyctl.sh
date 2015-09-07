#!/bin/bash
ff=unix

group_name=tlc
project_name=tlc-proxy
basedir=/opt/webapps/job/${project_name}
log_path=/opt/logs/job/${project_name}
log_name=stdout
pid=${log_path}/${project_name}.pid
java_args="-DgroupName=${group_name} -DprojectName=${project_name} -DlogPath=${log_path}/${log_name}"
jvm_args="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=50030 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Xms1G -Xmx1G -Xmn700M -XX:PermSize=128M -XX:MaxPermSize=128M -XX:+UseFastAccessorMethods -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:MaxTenuringThreshold=20 -XX:CMSFullGCsBeforeCompaction=5 -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=75 -XX:-PrintGC  -XX:-PrintGCTimeStamps -XX:+PrintGCDetails -XX:+PrintHeapAtGC -Xloggc:${log_path}/gc.log"

export JAVA_HOME=/usr/local/jdk1.7.0_79/
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar

command=$1
if [ ${command}x != "start"x -a ${command}x != "stop"x ];
    then
        echo "usage: proxyctl [start|stop]"
elif [ ${command}x = "start"x ];
    then
        nohup java ${java_args} ${jvm_args} -jar ${basedir}/${project_name}-1.0.0.jar --spring.profiles.active=prod >>${log_path}/${log_name}.log 2>&1 &
        echo $!>${pid}
else [ ${command}x = "stop"x ]
        /bin/kill -9 `cat ${pid}`
        current_date=`date "+%Y-%m-%d %H:%M:%S"`
        echo "${current_date} shutdown tlc-proxy by shutdown.sh" >>${log_path}/${log_name}.log
fi