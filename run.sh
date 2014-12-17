#!/bin/bash
#
# Precondition: derby must have been already setup in the system.
#


startNetworkServer &
if [ $? -ne 0 ] ; then
  exit 1
fi

trap 'stopNetworkServer' SIGINT SIGTERM EXIT

mvn clean compile exec:java -Dexec.mainClass=me.alb_i986.hbs.db.DbSetupMain

mvn exec:java

