#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions


//Global Variables




char* concat(char* first, char* second) {
    int i = 0, j = 0;
    char* str3 = malloc(512 * sizeof(char));
    // Insert the first string in the new string
    while (first[i] != '\0') {
        str3[j] = first[i];
        i++;
        j++;
    }

    // Insert the second string in the new string
    i = 0;
    while (second[i] != '\0') {
        str3[j] = second[i];
        i++;
        j++;
    }
    str3[j] = '\0';
    return str3;
}


//Program Functions

void printPeg(int peg) {

//Function body
    if (peg == 1) {
        printf("left");
    } else if (peg == 2) {
        printf("center");
    } else {
        printf("right");
    }
}

void hanoi(int n, int fromPeg, int usingPeg, int toPeg) {

//Function body
    if (n != 0) {
        hanoi(n - 1, fromPeg, toPeg, usingPeg);
        printf("Move disk from ");
        printPeg(fromPeg);
        printf(" peg to ");
        printPeg(toPeg);
        printf(" peg.\n");
        hanoi(n - 1, usingPeg, fromPeg, toPeg);
    }
}

int main () {

//Function Variables
    int n = 0;

//Function body
    printf("How many pegs? ");
    scanf("%d", &n);
    hanoi(n, 1, 2, 3);

//Return
    return 0;
}


