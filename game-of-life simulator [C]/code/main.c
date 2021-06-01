//-----------------------------------------
// NAME: Arshpreet Buttar 
// COURSE: COMP 2160
// 
// REMARKS: 
// This program simulates game of life algorithm 
// by reading in the necessary data from input and printing
// to STDOUT. It also implements Design by Contract and 
// edge-case checking for input
//----------------------------------------

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>

#ifdef NDEBUG
  #define MAX_PRINT 10
#else
  #define MAX_PRINT 250
#endif

#define LINE_LENGTH 80 //max characters an input line can have
#define MAX_SIZE 60    //max size of a 2D array (60x60)
#define MAX_GEN 250    //max number of generations a case can have
#define ALIVE_CELL '*'
#define DEAD_CELL '.'
#define ALIVE_INPUT 'X'
#define DEAD_INPUT ' '
#define CORNER_BORDER '+'
#define TOP_BOTTOM_BORDER '-'
#define LEFT_RIGHT_BORDER '|'

typedef struct {
    // each generation struct will have a 2D array of MAX_SIZE
    char arr[MAX_SIZE][MAX_SIZE];

} Generation;

void validate(Generation, int, int);
void playGameOfLife(Generation, int, int);
Generation getNextGeneration(Generation, int, int);
int compareGeneration(Generation, Generation, int, int);
int checkGeneration(Generation, int, int);
void printGeneration(Generation, int, int);


// Invariant to check the most fundamental aspects of Generation 
void validate(Generation gen, int rows, int cols) {

    // assertions to check the validity of row and column value
    assert(rows > 0);
    assert(cols > 0);
    assert(rows <= MAX_SIZE);
    assert(cols <= MAX_SIZE);

    // assertion to check if the given generation 2D array has valid entries
    // should always return 1
    assert(checkGeneration(gen, rows, cols));

}

//------------------------------------------------------------------
// PURPOSE: this function executes individual cases once the input for 
// that case is received fully. All the suceeeding generations are
// stored in an array of size 250, until a cycle is found or the number
// of generations exceed 250. Then the generations are printed accordingly
//
// INPUT PARAMETERS:
// Generation gen: the starting generation received directly from input
// int wows: number of non-empty rows of the generation's 2D array
// int cols: number of non-empty columns of the generation's 2D array
//
// OUTPUT PARAMETERS:
// void: only prints and calls other functions
//------------------------------------------------------------------
void playGameOfLife(Generation gen, int rows, int cols) {

    // assertion to check the validity of the given values for rows and columns
    validate(gen, rows, cols);

    // pre condition to play game of life:
    // only continue if the given row and column values are valid as well as the generation itself
    if (rows <= MAX_SIZE && cols <= MAX_SIZE && rows > 0 && cols > 0 && checkGeneration(gen, rows, cols)) { 
        
        // Generation array to hold maximum of 250 generations
        Generation genArray[MAX_GEN];
        // variable to check if a cycle has been found, in which it will be set to 1
        int cycleFound = 0;
        // to keep track of number of elements in genArray
        int arrayCount = 0;

        printf("Generation 0:\n");
        printGeneration(gen, rows, cols);

        // keep getting the succeeding generations until a cycle is found or the genArray becomes full
        while (arrayCount < MAX_GEN && !cycleFound) {
            
            // reintialize gen to store the next generation every iteration
            gen = getNextGeneration(gen, rows, cols);
            // append the current generation to the array
            genArray[arrayCount] = gen;

            // check for cycle by looping through all the generations in the array and comparing them to the current generation
            for (int gen = 0; gen < arrayCount && !cycleFound; gen++) {
                
                if (compareGeneration(genArray[gen], genArray[arrayCount], rows, cols)) {
                    // cycle is found, which will result in exiting the for loop and then the while loop
                    cycleFound = 1;
                    printf("Found a cycle between generation %d and generation %d\n", gen+1, arrayCount+1);
                }
            }
            arrayCount++;
        }

        // after the while loop finishes executing, either a cycle SHOULD be found
        // or the arrayCount SHOULD be 250 indicating the generation array is full
        assert(arrayCount == 250 || cycleFound);

        // post condition for cycle check:
        // only print the generations inside the array if a cycle is found OR max generation is reached
        if (arrayCount == 250 || cycleFound) {

            // if less than total 10 generations for a case, simply print these generations
            if (arrayCount < 10) {

                for (int gen = 0; gen < arrayCount; gen++) {
                    printf("Generation %d\n", gen + 1);
                    printGeneration(genArray[gen], rows, cols);
                }
            }

            // otherwise print last 10 generations if compiled with DNDEBUG
            // or all generations in array if not compiled with DNDEBUG
            else {

                if (MAX_PRINT == 250) { // print all generations

                    for (int gen = 0; gen < arrayCount; gen++) {
                        printf("Generation %d\n", gen + 1);
                        printGeneration(genArray[gen], rows, cols);
                    }

                }

                else { // print last 10 generations

                    for (int gen = arrayCount - 10; gen < arrayCount; gen++) {
                        printf("Generation %d\n", gen + 1);
                        printGeneration(genArray[gen], rows, cols);
                    }

                }
            }
        }
    }
}

//----------------------------------------------------------------
// PURPOSE: this function creates and returns a new instance of the 
// succeeding generation from the old generation (given as input) as per
// the game of life rules
//
// INPUT PARAMETERS:
// Generation prevGen: the generation from which to get the next generation
// int rows: number of non-empty rows of the prevGen's 2D array
// int cols: number of non-empty columns of the prevGen's 2D array
//
// OUTPUT PARAMETERS:
// Generation: returns a Generation struct which holds the succeeding generation
//----------------------------------------------------------------
Generation getNextGeneration(Generation prevGen, int rows, int cols) { 

    // check validity of previous generation
    validate(prevGen, rows, cols);

    // will hold the succeeding generation and returned at end of function
    Generation nextGen;

    // loop through the rows and columns of prevGen
    for (int row = 0; row < rows; row++) {

        for (int col = 0; col < cols; col++) {

            /* 
            How the neighbours are calculated and updated to nextGen:

            - Each index of the 2D array will first be checked for edge cases,
              such as the leftmost or rightmost column and first or last row to avoid accessing out of bounds

            - If an index is part of a edge case, the bounds will be adjusted accordingly representing a 'square' 
              sub-array, after which 2 for loops will be utilized to traverse through each element in that sub-array
              and checked for neighbours (excluding the index element) according to the game of life rules

            - Otherwise, if an index is not part of a edge case, the bounds will be set normally and the same 2 for loops
              will be applied to calculate the neighbours

            - After the neighbours for an index are calculated, the nextGen array is updated with a alive or dead cell 
              at that same index as per the game of life rules
            */

            // holds the total number of neighbours of each index
            int neighbour = 0; 
            // holds the respective bounds 
            int left, right, top, bottom = -1;

            // check for leftmost border edge case so that we don't access an index out of bounds
            // if column is the leftmost, set the left bound as same and the right bound as one more
            if (col==0) {
                left = col;
                right = col + 1;
            }

            // check for rightmost border edge case
            // if column is the rightmost, set the right bound as same and the left bound as one less
            else if (col==cols-1) {
                left = col - 1;
                right = col;
            }

            // otherwise, if this is not an edge case, simply set left as one less and right as one more
            else {
                left = col - 1;
                right = col + 1;
            }

            // just the same way as columns, check for row top edge case
            if (row==0) {
                top = row;
                // EXTREME EDGE CASE: if there is only one row, set bottom same as top, otherwise set it as top + 1
                bottom = (rows==1)? top:row+1;
            }

            // check for row bottom edge case
            else if (row==rows-1) {
                top = row - 1;
                bottom = row;
            }

            // otherwise, set the top as one less and bottom as one more
            else {
                top = row - 1;
                bottom = row + 1;
            }

            // assertion to check if all bounds are valid, which they should be
            assert (left >= 0 && right >= 0 && top >= 0 && bottom >= 0);

            // traverse through each index in the sub-array like one normally would for a 2D array
            for (int i = top; i <= bottom; i++) {
                
                for (int j = left; j <= right; j++) {

                    // don't check for the current index
                    if ( !(i == row && j == col) ) {
                
                        // increase neighbour count for every alive cell in the sub-array
                        if (prevGen.arr[i][j] == ALIVE_CELL) 
                            neighbour++;
                    
                    } 
                }
            }

            // update nextGen at index [row][col] based on the number of neighbours calculated & as per game of life rules

            if (prevGen.arr[row][col] == ALIVE_CELL) {

                if ( (neighbour==0) || (neighbour==1) || (neighbour>=4) )
                    nextGen.arr[row][col] = DEAD_CELL;

                else if ( (neighbour==2) || (neighbour==3) )
                    nextGen.arr[row][col] = ALIVE_CELL;

            }

            else {
                
                if ( neighbour==3 )
                    nextGen.arr[row][col] = ALIVE_CELL;
                
                else
                    nextGen.arr[row][col] = DEAD_CELL;

            }
        }
    }
    // Check validity of next generation
    validate(nextGen, rows, cols);
    // return nextGen which holds the succeeding generation of prevGen
    return nextGen;
}

//----------------------------------------------------------------
// PURPOSE: this function compares if two generations's 2D array
// content is strictly equal. Both generations's 2D array is guaranteed
// to be of size 60x60, but the actual non-empty dimension of the 2D array
// will be different, therefore the 2D array will be checked only for its
// non-empty rows and columns to determine the equality
//
// INPUT PARAMETERS:
// Generation a: the generation to perform comparison with b
// Generation b: the generation to perfom comparison with a
// int rows: number of non-empty rows of the 2D array of both generations
// int cols: number of non-empty columns of the 2D array of both generations
//
// OUTPUT PARAMETERS:
// Returns 1 if the the two generations are indeed equal
// Returns 0 if the two generations are not equal
//----------------------------------------------------------------
int compareGeneration(Generation a, Generation b, int rows, int cols) {

    // default value for result (representing true)
    int result = 1;

    validate(a, rows, cols);
    validate(b, rows, cols);

    // pre-condition to perfom comparison:
    // if either one of the generations is invalid or rows and column values are invalid, set result to 0 (false)
    if (!(rows <= MAX_SIZE && cols <= MAX_SIZE && rows > 0 && cols > 0 && checkGeneration(a, rows, cols) && checkGeneration(b, rows, cols))) 
        result = 0;

    // otherwise perform the comparison
    else {

        // loop through each row and column of both generations, if at any index
        // the element of a or b differ, then the generations are not the same 
        // in which case set result to 0 (false)
        for(int row = 0; row < rows && result; row++) {

            for(int col = 0; col < cols && result; col++) {
        
                if (a.arr[row][col] != b.arr[row][col])
                    result = 0;
            }
        }
    }
    // result SHOULD hold a value of either 0 or 1
    assert(result == 1 || result == 0);
    return result;
}

//----------------------------------------------------------------
// PURPOSE: this function checks if a generation 2D array has valid
// entries, meaning it should only contain either '*' or '.' 
// in any of its indices, if not then the generation is invalid
//
// INPUT PARAMETERS:
// Generation gen: the generation to perform check on
// int rows: number of non-empty rows of the 2D array of gen
// int cols: number of non-empty columns of the 2D array of gen
//
// OUTPUT PARAMETERS:
// Returns 1 if the generation is valid
// Returns 0 if the generation is invalid
//----------------------------------------------------------------
int checkGeneration(Generation gen, int rows, int cols) {

    // default value for result
    int result = 1;

    // loop through all non-empty indices in 2D array and check validity
    for (int row = 0; row < rows; row++) {

        for (int col = 0; col < cols; col++) {
            
            if (gen.arr[row][col] != '*' && gen.arr[row][col] != '.')
                result = 0;

        }
    }
    // result should only hold a value of 0 (false) or 1 (true)
    assert(result == 1 || result == 0);
    return result;
}

//----------------------------------------------------------------
// PURPOSE: this function prints the non-empty rows and columns of 
// a generation's 2D array with borders
//
// INPUT PARAMETERS:
// Generation gen: the generation to be printed
// int arrRows: number of non-empty rows of the generation's 2D array
// int arrCols: number of non-empty columns of the generation's 2D array
//
// OUTPUT PARAMETERS:
// void: only prints the 2D array to STDOUT
//----------------------------------------------------------------
void printGeneration(Generation gen, int arrRows, int arrCols) {

    // assertion for checking if number of rows and columns given are valid
    validate(gen, arrRows, arrCols);

    // Pre condition to print:
    // only print if the rows and columns are valid
    if (arrRows <= MAX_SIZE && arrCols <= MAX_SIZE && arrRows > 0 && arrCols > 0 && checkGeneration(gen, arrRows, arrCols)) { 

        // increment the number of rows and columns by 2 to accommodate for the borders
        int rows = arrRows + 2;
        int cols = arrCols + 2;

        for(int row = 0; row < rows; row++) {

            for(int col = 0; col < cols; col++) {

                // check for corner borders (top left, top right, bottom left, bottom right)
                if ( (row==0 && (col==0 || col==cols-1) ) || ( row==rows-1 && (col==0 || col==cols-1) ))
                    printf("%c", CORNER_BORDER);
            
                // check if the row is first (top border) or last (bottom border)
                else if (row==0 || row==rows-1)
                    printf("%c", TOP_BOTTOM_BORDER);

                // check if column is left or right (representing leftmost and rightmost border)
                else if ( col==0 || col==cols-1 )
                    printf("%c", LEFT_RIGHT_BORDER);

                // if none of the other 3 cases, then simply print the character from gen's 2D array
                else
                    printf("%c", gen.arr[row-1][col-1]);
            }
        
            printf("\n");
        }
    }
}

//----------------------------------------------------------------
// MAIN(VOID): 
// the main function receives and stores the case data from input
// to Generation struct. After data for a individual case is fully received and
// stored, the cases are executed in the order they appear on the input until EOF
//----------------------------------------------------------------
int main(void) {

    // holds the starting generation
    Generation gen;   
    // holds each input line of max length LINE_LENGTH to be read using fgets()
    char input[LINE_LENGTH];
    // holds the number of rows and columns given for each case
    // -1 to represent the values are yet to be read from input
    int rows, cols = -1;
    // delimiter pointer to tokenize input
    const char *DELIMITER = " ";

    // read input file until EOF
    while (fgets(input, LINE_LENGTH, stdin) != NULL) {

        // if the first character of the line is '*', 
        // representing a case name and number, then print that line
        if (input[0] == '*') 
            printf("%s", input);

        // if the first character of the line is a digit, representing number of row and columns
        // for that case, get the row and column value and store it in their respective variables
        else if (input[0] >= '0' && input[0] <= '9') {

            // DELIMITER is used to seperate the row and column values using strtok()
            // then the values are converted from character to int using atoi() and stored
            char *token;
            token = strtok(input, DELIMITER);
            // check if token pointer is not null before assigning to rows
            assert(token != NULL); 
            rows = (token != NULL)? atoi(token): -1;
            
            token = strtok(NULL, DELIMITER);
            // check if token pointer is not null before assigning to cols
            assert(token != NULL);
            cols = (token != NULL)? atoi(token): -1;
                
            // assertion to check if row and column values are valid after reading from input
            assert(rows > 0 && cols > 0 && rows <= MAX_SIZE && cols <= MAX_SIZE);

            // only continue with the rest of input if the row and column values are valid
            if (rows > 0 && cols > 0 && rows <= MAX_SIZE && cols <= MAX_SIZE) {

                // Now we have the exact number of rows (lines) and columns (characters) to traverse 
                // through the input to get the 2D array and store it in generation struct
                for (int row = 0; row < rows; row++) {

                    fgets(input, LINE_LENGTH, stdin);

                    for (int col = 0; col < cols; col++) { 

                        // if the character is 'dead' from the input, add the corresponding 'dead' character
                        // to the gen array at that particular row and col index
                        if (input[col] == DEAD_INPUT) 
                            gen.arr[row][col] = DEAD_CELL;

                        // otherwise if the character is 'alive' from the input, add the 'dead' character
                        // to the array at the row and col index
                        else if (input[col] == ALIVE_INPUT) 
                            gen.arr[row][col] = ALIVE_CELL;

                    }
                }
                // validate before playing
                validate(gen, rows, cols);

                // post condition before executing the case:
                // after reading an individual case fully, check 2D array validity
                if (checkGeneration(gen, rows, cols))
                    playGameOfLife(gen, rows, cols);
            }

            else 
                printf("Error: invalid value for row or column given, case not executed\n");
        }

        else
            printf("Error, invalid input given\n");
    }

    return 0;
}
