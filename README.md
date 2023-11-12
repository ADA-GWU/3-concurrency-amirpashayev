[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/YybNWfh8)

# Concurrency


## About the project

This assignment focuses on implementing concurrency on a picture by applying interpolation to each row that consists of a square of pixels, size of which is given as an argument when the program is run. When the program is run in a single thread mode (also passed as an argument from terminal/command prompt) the rows of the picture are analysed one by one. However, in multi-thread mode, the rows of the picture are analysed concurrently (the number of rows analysed at the same time depends on the number of cores of the computer). The result is then saved to a different file - result.jpg.


## Dependencies and instructions to run

You should have JDK 15 or later installed on your computer. When running the Main.java file it should be in the following style: "java {path to the Main.java file} {path to the jpg file} {square size} {Single or Multi-Thread (S/M)}". Due to the fact that this application will automatically scale down all images to take up maximum of 600px in width, the square size will also be scaled down at the same ratio. So in the case of images with bigger size, you might want to choose bigger square size in order for the process not to be very slow and in order for the squares to be visible. However, this only applies to the displayed image, the result.jpg file will have original dimensions and originally entered square size.


## Project Details

Image Initialization:

Reads an image file (JPEG format) and initializes three BufferedImage objects: image, resultImage, and displayImage.
displayImage is a scaled version of the original image for display purposes.

Processing Squares:

The image is divided into squares of a specified size.
The average color of each square is calculated using the getAverageColor method.

Updating Image:

The colorPixelsInsideSquare method sets the color of the entire square to the calculated average color.
The display image is updated in real-time, and the UI is refreshed using updateUI().

Multi-threaded Processing:

In multi-threaded mode, the image processing is parallelized using multiple threads.
The executeMultiThread method divides the image into rows, assigns each row to a separate thread, and processes them concurrently.

Main Method:

The main method parses command-line arguments to get the filename, square size, and processing mode.
It initializes the Main object, processes the image based on the specified mode, and writes the result to a new file ("result.jpg").