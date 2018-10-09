#!/bin/bash

docker run -it --rm --name account-storage-rest -u 1000:1000 -v ~/.m2:/var/maven/.m2 -v "$(pwd)":/usr/src/mymaven -e MAVEN_CONFIG=/var/maven/.m2 -w /usr/src/mymaven maven:3.5-jdk-8 mvn -Duser.home=/var/maven clean package
