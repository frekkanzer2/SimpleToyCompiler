#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_multAddDiff {
    int param1;
    int param2;
    int param3;
};


//Global Variables

char nome[512] = "Michele";



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

struct struct_multAddDiff multAddDiff() {
    struct struct_multAddDiff toReturn;

//Function Variables
    int primo, secondo, mul, add, diff;

//Function body
    printf("Inserire il primo argomento:\n");
    scanf("%d", &primo);
    printf("Inserire il secondo argomento:\n");
    scanf("%d", &secondo);
    mul = primo * secondo;
    add = primo + secondo;
    diff = primo - secondo;

//Return
    toReturn.param1 = mul;
    toReturn.param2 = add;
    toReturn.param3 = diff;
    return toReturn;
}

void writeNewLines(int n) {

//Function body
    while (n > 0) {
        printf("\n");
        n = n - 1;

    }
}

int main () {

//Function Variables
    int a, b, c = 0;

//Function body
    struct struct_multAddDiff str_localtemp_multAddDiff_1_1 = multAddDiff();
    a = str_localtemp_multAddDiff_1_1.param1;
    b = str_localtemp_multAddDiff_1_1.param2;
    c = str_localtemp_multAddDiff_1_1.param3;
    printf("Ciao %s", (nome));
    writeNewLines(2);
    printf("I tuoi valori sono:\n%d per la moltiplicazione\n%d per la somma, e \n%d per la differenza", (a), (b), (c));
//Return
    return 0;
}


