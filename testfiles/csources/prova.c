#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>

// Structures for functions

struct struct_getValues {
    int param1;
    int param2;
    int param3;
};

struct struct_getAnotherValue {
    int param1;
    int param2;
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

struct struct_getValues getValues(int a, int b) {
    struct struct_getValues toReturn;

//Return
    toReturn.param1 = 10;
    toReturn.param2 = 20;
    toReturn.param3 = 30;
    return toReturn;
}

struct struct_getAnotherValue getAnotherValue(int a, int b) {
    struct struct_getAnotherValue toReturn;

//Return
    toReturn.param1 = 10;
    toReturn.param2 = 20;
    return toReturn;
}

int main () {

//Function Variables
    int a = 0, b, c, d, e;
    char mario[512] = "Ciao mario";
    bool f;

//Function body
    printf("Inserire un booleano: ");
    int temp_bool1;
    scanf("%d", &temp_bool1);
    f = temp_bool1;
    if (f) {
        printf("TRUE\n");
        struct struct_getValues str_localtemp_getValues_1_1 = getValues(a, b);
        e = 1;
        b = str_localtemp_getValues_1_1.param1;
        c = str_localtemp_getValues_1_1.param2;
        d = str_localtemp_getValues_1_1.param3;
        a = 1;
        struct struct_getValues str_localtemp_getValues_1_2 = getValues(e, d);
        struct struct_getAnotherValue str_localtemp_getAnotherValue_2_3 = getAnotherValue(a, b);
        a = str_localtemp_getValues_1_2.param1;
        b = str_localtemp_getValues_1_2.param2;
        c = str_localtemp_getValues_1_2.param3;
        d = str_localtemp_getAnotherValue_2_3.param1;
        e = str_localtemp_getAnotherValue_2_3.param2;
    } else if (! (f)) {
        printf("FALSE\n");
        struct struct_getValues str_localtemp_getValues_1_4 = getValues( getAnotherValue(a, b).param1,  getAnotherValue(a, b).param2);
        b = str_localtemp_getValues_1_4.param1;
        c = str_localtemp_getValues_1_4.param2;
        d = str_localtemp_getValues_1_4.param3;
    } else {
        a = 4;
    }
    struct struct_getValues str_localtemp_getValues_1_5 = getValues(a, b);
    struct struct_getValues str_localtemp_getValues_2_6 = getValues( getAnotherValue( getAnotherValue(a, b).param1,  getAnotherValue(a, b).param2).param1,  getAnotherValue( getAnotherValue(a, b).param1,  getAnotherValue(a, b).param2).param2);
    printf("%d %d %d pippo %d %d %d %d %s %d", str_localtemp_getValues_1_5.param1, str_localtemp_getValues_1_5.param2, str_localtemp_getValues_1_5.param3, str_localtemp_getValues_2_6.param1, str_localtemp_getValues_2_6.param2, str_localtemp_getValues_2_6.param3, (1 + 4), (mario), (f));
//Return
    return 0;
}


