#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_anotherFunct {
    int param1;
    int param2;
    int param3;
};

struct struct_myFunct {
    int param1;
    int param2;
};

struct struct_factorial {
    int param1;
};


//Global Variables

int n = 0;
int a = 0;



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

struct struct_factorial factorial(int n) {
    struct struct_factorial toReturn;

//Function Variables
    int result = 0;
    int a = 100;

//Function body
    if (n == 0) {
        result = 1;
    } else {
        result = n *  factorial(n - 1).param1;
    }

//Return
    toReturn.param1 = result;
    return toReturn;
}

struct struct_myFunct myFunct() {
    struct struct_myFunct toReturn;

//Return
    toReturn.param1 = 5;
    toReturn.param2 = 10;
    return toReturn;
}

struct struct_anotherFunct anotherFunct() {
    struct struct_anotherFunct toReturn;

//Return
    toReturn.param1 = 1;
    toReturn.param2 = 2;
    toReturn.param3 = 3;
    return toReturn;
}

int main () {

//Function Variables
    int q, w, e, r, s;

//Function body
    struct struct_myFunct str_localtemp_myFunct_1_1 = myFunct();
    struct struct_anotherFunct str_localtemp_anotherFunct_2_2 = anotherFunct();
    q = str_localtemp_myFunct_1_1.param1;
    w = str_localtemp_myFunct_1_1.param2;
    e = str_localtemp_anotherFunct_2_2.param1;
    r = str_localtemp_anotherFunct_2_2.param2;
    s = str_localtemp_anotherFunct_2_2.param3;
    printf("aysyayd asdasd
           wajinwaidna");printf("Enter n, or >= 10 to exit: ");scanf("%d", &n);
    while (n < 10) {
    struct struct_factorial str_localtemp_factorial_1_3 = factorial(n);
        printf("Factorial of %d is %d \n", (n), str_localtemp_factorial_1_3.param1);
        printf("Enter n, or >= 10 to exit: ");
        scanf("%d", &n);
    }
    printf("%d", (a));
//Return
    return 0;
}


