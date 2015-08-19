#!/bin/bash

source tlc-proxy.conf
nohup java ${java_args} ${jvm_args} -jar ${basedir}/${pro_name}-1.0.0.jar --spring.profiles.active=prod >>${log_path}/${log_name}.log 2>&1 &
echo $!>${pid}

#
#command=$1
#if [ ${command}x != "start"x -a ${command}x != "stop"x ];
#    then
#        echo "usage: proxyctl [start|stop]"
#elif [ ${command}x = "start"x ];
#    then
#        nohup java ${java_args} ${jvm_args} -jar ${basedir}/${pro_name}-1.0.0.jar --spring.profiles.active=prod >/dev/null 2>$1 &
#        echo $!>${pid}
#else [ ${command}x = "stop"x ]
#        /bin/kill -9 `cat ${pid}`
#        current_date=`date "+%Y-%m-%d %H:%M:%S"`
#        echo "${current_date} shutdown tlc-proxy by shutdown.sh" >>${log_path}/${log_name}.log
#fi