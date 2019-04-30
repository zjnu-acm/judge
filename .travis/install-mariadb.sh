#!/bin/sh

remove_mysql(){
    service mysql stop
    apt-get -qq autoremove --purge mysql-server mysql-client mysql-common
    rm -rf /etc/mysql||true
    rm -rf /var/lib/mysql||true
}

remove_mysql

service mysql stop

apt-get install software-properties-common
apt-key adv --recv-keys --keyserver hkp://keyserver.ubuntu.com:80 0xcbcb082a1bb943db
add-apt-repository 'deb [arch=amd64,i386,ppc64el] http://mariadb.mirror.iweb.com/repo/10.3/ubuntu trusty main'
apt-get update
apt-get install mariadb-server

service mysql start
