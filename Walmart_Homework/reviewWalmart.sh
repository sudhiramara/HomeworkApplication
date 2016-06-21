#!/bin/bash

javac -classpath lib/\* Users.java HomeworkApplication.java ReviewVO.java

java -cp .:lib/* Users
