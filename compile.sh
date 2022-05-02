#!/bin/bash
rm src/*.class
javac -cp '.:lib/bcprov-ext-jdk15to18-171.jar' src/*.java
jar cmvf META-INF/MANIFEST.MF run.jar src lib
chmod 744 run.jar