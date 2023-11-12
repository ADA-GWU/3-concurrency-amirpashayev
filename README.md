[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/YybNWfh8)

# Concurrency


## About the project

This assignment focuses on implementing concurrency on a picture by applying interpolation to each row that consists of a square of pixels, size of which is given as an argument when the program is run. When the program is run in a single thread mode (also passed as an argument from terminal/command prompt) the rows of the picture are analysed one by one. However, in multi-thread mode, the rows of the picture are analysed concurrently (the number of rows analysed at the same time depends on the number of cores of the computer). The result is then saved to a different file - result.jpg.


## Dependencies and instructions to run

You should have JDK 15 or later installed on your computer. When running the Main.java file it should be in the following style: "java {path to the Main.java file} {path to the jpg file} {square size} {Single or Multi-Thread (S/M)}".

