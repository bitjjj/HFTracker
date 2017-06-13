#!/bin/sh

logFile="/home/admin/jin10/error.log"

logCount=`ls ${logFile} | wc -l`

if test ${logCount} -eq 1
then
#	rm -f ${logFile}
echo $logCount
fi

jinPS=`ps -ef | grep Jin10 | grep -v grep | awk '{print $2}'`
echo $jinPS
#kill -s 9 ${jinPS}

sleep 10

echo "sleep 10"

#nohup java -cp .:Jin10.jar:socketio.jar:mysql-connector-java-5.1.31-bin.jar com.oppsisfund.fx.headline.Main 1>/dev/null 2>error.log &
