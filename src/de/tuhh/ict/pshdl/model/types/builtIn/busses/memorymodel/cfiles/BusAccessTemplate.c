//
//  BusAcces.c
//
//  Automatically generated on {DATE}.
//

#include <stdint.h>
#include "BusDefinitions.h"
#include "BusStdDefinitions.h"

static void defaultWarn(warningType_t t, int value, char *def, char *row, char *msg){
}

warnFunc_p warn=defaultWarn;

void setWarn(warnFunc_p warnFunction){
    warn=warnFunction;
}

{FUNCTIONS}
