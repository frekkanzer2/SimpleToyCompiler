#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_add {
    float param1;
};

struct struct_division {
    float param1;
};

struct struct_sub {
    float param1;
};

struct struct_mult {
    float param1;
};


//Global Variables

int var = 1;



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

struct struct_add add(float a, float b) {
    struct struct_add toReturn;

//Return
    toReturn.param1 = a + b;
    return toReturn;
}

struct struct_sub sub(float a, float b) {
    struct struct_sub toReturn;

//Return
    toReturn.param1 = a - b;
    return toReturn;
}

struct struct_mult mult(float a, float b) {
    struct struct_mult toReturn;

//Return
    toReturn.param1 = a * b;
    return toReturn;
}

struct struct_division division(float a, float b) {
    struct struct_division toReturn;

//Return
    toReturn.param1 = a / b;
    return toReturn;
}

int main () {

//Function Variables
    int choise;
    float var1 = 5, var2, result;

//Function body
    printf("Press 0 or 1 to exit\n");
    printf("Press 2 to add\n");
    printf("Press 3 to sub\n");
    printf("Press 4 to mult\n");
    printf("Press 5 to div\n");
    scanf("%d", &choise);
    while (choise != 0 && choise != 1) {
        if (choise > 5 || choise < 0) {
            printf("Incorrect value. Please retry.");
        } else {
            printf("First value: ");
            scanf("%f", &var1);
            printf("Second value: ");
            scanf("%f", &var2);
            if (choise == 2) {
                struct struct_add str_localtemp_add_1_1 = add(var1, var2);
                result = str_localtemp_add_1_1.param1;
            } else if (choise == 3) {
                struct struct_sub str_localtemp_sub_1_2 = sub(var1, var2);
                result = str_localtemp_sub_1_2.param1;
            } else if (choise == 4) {
                struct struct_mult str_localtemp_mult_1_3 = mult(var1, var2);
                result = str_localtemp_mult_1_3.param1;
            } else if (choise == 5) {
                struct struct_division str_localtemp_division_1_4 = division(var1, var2);
                result = str_localtemp_division_1_4.param1;
            }
            printf("Result: %f", (result));
        }

        printf("Press 0 or 1 to exit\n");
        printf("Press 2 to add\n");
        printf("Press 3 to sub\n");
        printf("Press 4 to mult\n");
        printf("Press 5 to div\n");
        scanf("%d", &choise);
    }

//Return
    return 0;
}


