## General information

This repository holds some of the past course projects I have worked on throughout my studies at uManitoba.

The purpose of this repository is to showcase my work, so I have not added any supplementary instructions or files (Makefiles etc.) so that others can run the code on their machines.

Each projects folder has a:

* Code folder that includes the source code
* Project folder that includes a document (or HTML in some cases) that the project is based on and further information
* Sample input/output folder that includes files that showcase how the project runs and what it outputs on various inputs

**That's all. Thanks for viewing!**

## Projects

### Game of Life Simulator (C programming):

Implements Conway's [Game of Life](https://en.wikipedia.org/wiki/Conway's_Game_of_Life). Outputs generation cycles (up to 250 generations) based on the given input (2D matrix).

### Garbage Collector (C programming):

Implements a Mark and Sweep Defragmenting / Mark Compact garbage collector in C using an object manager abstract data type (ADT)

### Huffman File Encoder (Object Oriented Programming):

Implements Huffman data compression using the Huffman Encoding algorithm. Binary tree and dictionary (both built from scratch) are utilized to build the algorithm. Takes in any text file as input and outputs a text file as sequence of 0s and 1s

### Wiki Manager (Object Oriented Programming):

Implements a text-based Wiki Manager that manages a collection of documents which can be edited by a set of users. Every action is logged and document history and restoration is also implemented. Driven by a set  of text commands. Takes in a text file with valid commands as input and outputs a text file with the respective prompts based on the input commands.

### "whodunit" board game (Object Oriented Programming):

Implements "whodunit?" (very similar to the board game Clue). Allows 1 human player to play with as many computer players as they wish. Implements a simple AI algorithm for the computer players. Game is entirely text-based and implements active prompting to the human player as the game progresses.
