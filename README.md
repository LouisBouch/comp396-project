# Comp 396 project: 3D Habitability simulator 

## Overview

This simulation aims to serve as an interactive tool to better understand and visualize the necessary conditions for life to emerge on planets in a solar system.  
To do so, it will simulate the motions of planets, suns and other various celestial bodies as they interact through gravity. The habitability of each planet will be computed based on the intensity of the suns, their proximity and atmospheric conditions.  
More aspect are likely to be considered as the project evolves.  

## Demo

Here is a quick demo of this project  
[![YouTube](https://i3.ytimg.com/vi/LNxFyyh6oBs/maxresdefault.jpg)](https://www.youtube.com/watch?v=LNxFyyh6oBs)

## Installation

This project uses [java 21](https://www.oracle.com/ca-en/java/technologies/downloads/#java21). Make you have java installed before running this application.  
- [Intellij](https://www.jetbrains.com/idea/)
  - Clone repo into a project directory
  - Open the project in IntelliJ, select “Trust Project”
  - Where it says “Project JDK is not defined”, click “Setup SDK”
  - Select/download JDK. Consider Oracle OpenJDK: version 19
  - Right-click on the “resources” folder (path: comp396-project/app/src/main/) in the project’s structure, and select “Mark Directory as” -> “Resources Root”
  - Open the “Window.java” file (path: comp396-project/app/src/main/java/graphInterface/core/) and run it to launch the simulator
  - Enjoy the sim!

- [Gradle](https://gradle.org/install/)
  - Clone repo into a project directory
  - cd to the project directory (Where .git resides)
  - Run "gradle init" in the cloned directory, and enter the following values:
    -  yes
    -  1 (Application)
    -  1 (Java)
    -  21 (Java21)
    -  Enter (default name)
    -  1 (Single application)
    -  1 (Kotlin)
    -  4 (JUnit Jupiter)
    -  Enter (default "no")
  - Edit the ./app/build.gradle.kts file
    - Search for a line containing the word "mainClass"
    - Replace "org.example.App" with "graphInterface.core.Window"
  - Go back to the root directory (Where .git resides)
  - Execute ./gradlew run
  - Enjoy the sim!


## Current features

This is an interactive simulation, and as such, the user has complete control over the environment.  
****
  - 

This repository is automatically mirrored in McGill's Computer Science GitLab servers!
