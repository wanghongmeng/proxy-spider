#!/bin/bash

source tlc-proxy.conf
/bin/kill -9 `cat ${pid}`
current_date=`date "+%Y-%m-%d %H:%M:%S"`
echo "${current_date} shutdown tlc-proxy by shutdown.sh" >>${log_path}/${log_name}.log