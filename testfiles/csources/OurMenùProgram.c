#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_fibonacci {
    float param1;
};

struct struct_pow {
    float param1;
};

struct struct_operation {
    float param1;
    float param2;
    float param3;
    float param4;
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

struct struct_operation operation(float a, float b) {
    struct struct_operation toReturn;

//Return
    toReturn.param1 = a + b;
    toReturn.param2 = a - b;
    toReturn.param3 = a * b;
    toReturn.param4 = a / b;
    return toReturn;
}

struct struct_pow pow(float base, float exp) {
    struct struct_pow toReturn;

//Function Variables
    float startBase;

//Function body
    startBase = base;
    if (exp > 0) {
        while (exp > 1) {
            base = base * startBase;
            exp = exp - 1;

        }
    } else {
        base = 1;
    }

//Return
    toReturn.param1 = base;
    return toReturn;
}

struct struct_fibonacci fibonacci(float i) {
    struct struct_fibonacci toReturn;

//Function Variables
    float res;

//Function body
    if (i < 0) {
        res = - (1);
    } else if (i == 0) {
        res = 0;
    } else if (i == 1) {
        res = 1;
    } else {
        res =  fibonacci(i - 1).param1 +  fibonacci(i - 2).param1;
    }

//Return
    toReturn.param1 = res;
    return toReturn;
}

int main () {

//Function Variables
    int choise;
    float var1, var2, result_add, result_sub, result_mult, result_div, result_pow, result_fibonacci;

//Function body
    printf("Press 0 to exit or 1 to continue\n");
    scanf("%d", &choise);
    while (choise != 0) {
        printf("Select Operation [2 -> + | 3 -> - | 4 -> * | 5 -> /| 6 -> ^ | 7 -> Fibonacci Calculation]: ");
        scanf("%d", &choise);
        if (choise == 2) {
            printf("Selection Add\n");
            printf("First value: ");
            scanf("%f", &var1);
            printf("Second value: ");
            scanf("%f", &var2);
            struct struct_operation str_localtemp_operation_1_1 = operation(var1, var2);
            result_add = str_localtemp_operation_1_1.param1;
            result_sub = str_localtemp_operation_1_1.param2;
            result_mult = str_localtemp_operation_1_1.param3;
            result_div = str_localtemp_operation_1_1.param4;
            printf("Result add: %f\n", (result_add));
        } else if (choise == 3) {
            printf("Selection Diff\n");
            printf("First value: ");
            scanf("%f", &var1);
            printf("Second value: ");
            scanf("%f", &var2);
            struct struct_operation str_localtemp_operation_1_2 = operation(var1, var2);
            result_add = str_localtemp_operation_1_2.param1;
            result_sub = str_localtemp_operation_1_2.param2;
            result_mult = str_localtemp_operation_1_2.param3;
            result_div = str_localtemp_operation_1_2.param4;
            printf("Result sub: %f\n", (result_sub));
        } else if (choise == 4) {
            printf("Selection Mult\n");
            printf("First value: ");
            scanf("%f", &var1);
            printf("Second value: ");
            scanf("%f", &var2);
            struct struct_operation str_localtemp_operation_1_3 = operation(var1, var2);
            result_add = str_localtemp_operation_1_3.param1;
            result_sub = str_localtemp_operation_1_3.param2;
            result_mult = str_localtemp_operation_1_3.param3;
            result_div = str_localtemp_operation_1_3.param4;
            printf("Result mult: %f\n", (result_mult));
        } else if (choise == 5) {
            printf("Selection Div\n");
            printf("First value: ");
            scanf("%f", &var1);
            printf("Second value: ");
            scanf("%f", &var2);
            struct struct_operation str_localtemp_operation_1_4 = operation(var1, var2);
            result_add = str_localtemp_operation_1_4.param1;
            result_sub = str_localtemp_operation_1_4.param2;
            result_mult = str_localtemp_operation_1_4.param3;
            result_div = str_localtemp_operation_1_4.param4;
            printf("Result div: %f\n", (result_div));
        } else if (choise == 6) {
            printf("Selection Exp\n");
            printf("Base: ");
            scanf("%f", &var1);
            printf("Exponent: ");
            scanf("%f", &var2);
            struct struct_pow str_localtemp_pow_1_5 = pow(var1, var2);
            result_pow = str_localtemp_pow_1_5.param1;
            printf("Result pow: %f\n", (result_pow));
        } else if (choise == 7) {
            printf("Selection Fibonacci\n");
            printf("Number: ");
            scanf("%f", &var1);
            struct struct_fibonacci str_localtemp_fibonacci_1_6 = fibonacci(var1);
            result_fibonacci = str_localtemp_fibonacci_1_6.param1;
            printf("Result fibonacci: %f\n", (result_fibonacci));
        } else {
            printf("Uncorrect operation\n");
        }

        printf("Press 0 to exit or 1 to continue\n");
        scanf("%d", &choise);
    }

//Return
    return 0;
}


