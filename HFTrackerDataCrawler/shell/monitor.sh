#! /bin/sh

. /etc/profile
PATH=/usr/local/sbin:/usr/local/bin:/sbin:/bin:/usr/sbin:/usr/bin

proc_name="jetty"

proc_num(){
	num=`ps -ef | grep $proc_name | grep -v grep | wc -l`
	return $num
}

proc_num
number=$?
echo $number
if [ $number -eq 0 ]
then
	/home/admin/deploy-api/bin/jettyctl.sh start

fi
