#!/bin/sh
# ----------------------------------------------------------------------------
mvn clean && cd framework && mvn clean deploy $@ && cd ../
mvn clean && cd registry  && mvn clean deploy $@ && cd ../
mvn clean && mvn package $@