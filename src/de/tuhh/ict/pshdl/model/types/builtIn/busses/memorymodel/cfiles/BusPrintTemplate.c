//
//  BusPrint.c
//  MemoryModelTest
//
//  Automatically generated on {DATE}.
//

#include <stdio.h>
#include "BusDefinitions.h"

void defaultPrintfWarn(warningType_t t, int value, char *def, char *row, char *msg) {
    switch (t) {
        case limit:
            printf("Limited value %d for definition %s of row %s %s\n",value ,def,row,msg);
            break;
        case mask:
            printf("Masked value %d for definition %s of row %s %s\n",value ,def,row,msg);
            break;
        case invalidIndex:
            printf("The index %d is not valid for the column %s %s\n", value, row, msg);
        default:
            break;
    }
    
}

{FUNCTIONS}