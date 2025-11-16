#!/bin/bash

# Set Java home (adjust path if needed)
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# Run with explicit module path for Mac M4
mvn clean compile exec:java \
    -Dexec.mainClass="com.jobportal.Main" \
    -Dexec.cleanupDaemonThreads=false \
    -Djavafx.verbose=true \
    -Dexec.args=""