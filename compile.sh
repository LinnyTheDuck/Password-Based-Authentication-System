#!/bin/bash
javac -cp '.:lib/bcprov-ext-jdk15to18-171.jar' src/*.java
jar cmvf META-INF/MANIFEST.MF run.jar src
chmod 744 run.jar