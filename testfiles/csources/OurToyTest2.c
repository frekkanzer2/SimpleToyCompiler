#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_operation {
    int param1;
    int param2;
    int param3;
    int param4;
};


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

struct struct_operation operation(int a, int b, float c) {
    struct struct_operation toReturn;

//Return
    toReturn.param1 = a + b;
    toReturn.param2 = a - b;
    toReturn.param3 = a * b;
    toReturn.param4 = a / b;
    return toReturn;
}

int main () {

//Function Variables
    int choise;
    int var1, var2;
    float result_add, result_sub, result_mult, result_div;

//Function body
    printf("Press 0 to exit or 1 to continue: ");
    scanf("%d", &choise);
    while (choise != 0) {
        printf("First value: ");
        scanf("%d", &var1);
        printf("Second value: ");
        scanf("%d", &var2);
        printf("Select Operation [2 -> + | 3 -> - | 4 -> * | 5 -> /]: ");
        scanf("%d", &choise);
        struct struct_operation str_localtemp_operation_1_1 = operation(var1, var2, 5.5);
        result_add = str_localtemp_operation_1_1.param1;
        result_sub = str_localtemp_operation_1_1.param2;
        result_mult = str_localtemp_operation_1_1.param3;
        result_div = str_localtemp_operation_1_1.param4;
        if (choise == 2) {
            printf("Result add: %f\n", (result_add));
        } else if (choise == 3) {
            printf("Result sub: %f\n", (result_sub));
        } else if (choise == 4) {
            printf("Result mult: %f\n", (result_mult));
        } else if (choise == 5) {
            printf("Result div: %f\n", (result_div));
        } else {
            printf("Uncorrect operation\n");
        }

        printf("Press 0 to exit or 1 to continue: ");
        scanf("%d", &choise);
    }

//Return
    return 0;
}


