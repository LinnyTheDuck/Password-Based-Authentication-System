#!/bin/bash
javac src/*.java
jar cmvf META-INF/MANIFEST.MF run.jar src
chmod 744 run.jar