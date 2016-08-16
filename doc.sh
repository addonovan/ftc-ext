#!/bin/bash

# run dokka and generate the documentation
./gradlew dokka

# replace that god-awful css with some of our own to make it more readable
cp doc/kotlin-style.css doc/kotlin/style.css

# copy over the index.html so it redirects to the main page
cp doc/index.html doc/kotlin/index.html
