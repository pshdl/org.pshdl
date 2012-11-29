//
//  BusStdDefinitions.h
//
//  Created by Karsten Becker on 31.10.12.
//  Copyright (c) 2012 Karsten Becker. All rights reserved.
//

#ifndef MemoryModelTest_BusStdDefinitions_h
#define MemoryModelTest_BusStdDefinitions_h
#include <stdint.h>
typedef uint32_t bus_bit1_t;
typedef uint32_t bus_bit2_t;
typedef uint32_t bus_bit3_t;
typedef uint32_t bus_bit4_t;
typedef uint32_t bus_bit5_t;
typedef uint32_t bus_bit6_t;
typedef uint32_t bus_bit7_t;
typedef uint32_t bus_bit8_t;
typedef uint32_t bus_bit9_t;
typedef uint32_t bus_bit10_t;
typedef uint32_t bus_bit11_t;
typedef uint32_t bus_bit12_t;
typedef uint32_t bus_bit13_t;
typedef uint32_t bus_bit14_t;
typedef uint32_t bus_bit15_t;
typedef uint32_t bus_bit16_t;
typedef uint32_t bus_bit17_t;
typedef uint32_t bus_bit18_t;
typedef uint32_t bus_bit19_t;
typedef uint32_t bus_bit20_t;
typedef uint32_t bus_bit21_t;
typedef uint32_t bus_bit22_t;
typedef uint32_t bus_bit23_t;
typedef uint32_t bus_bit24_t;
typedef uint32_t bus_bit25_t;
typedef uint32_t bus_bit26_t;
typedef uint32_t bus_bit27_t;
typedef uint32_t bus_bit28_t;
typedef uint32_t bus_bit29_t;
typedef uint32_t bus_bit30_t;
typedef uint32_t bus_bit31_t;
typedef uint32_t bus_bit32_t;
typedef uint32_t bus_uint1_t;
typedef uint32_t bus_uint2_t;
typedef uint32_t bus_uint3_t;
typedef uint32_t bus_uint4_t;
typedef uint32_t bus_uint5_t;
typedef uint32_t bus_uint6_t;
typedef uint32_t bus_uint7_t;
typedef uint32_t bus_uint8_t;
typedef uint32_t bus_uint9_t;
typedef uint32_t bus_uint10_t;
typedef uint32_t bus_uint11_t;
typedef uint32_t bus_uint12_t;
typedef uint32_t bus_uint13_t;
typedef uint32_t bus_uint14_t;
typedef uint32_t bus_uint15_t;
typedef uint32_t bus_uint16_t;
typedef uint32_t bus_uint17_t;
typedef uint32_t bus_uint18_t;
typedef uint32_t bus_uint19_t;
typedef uint32_t bus_uint20_t;
typedef uint32_t bus_uint21_t;
typedef uint32_t bus_uint22_t;
typedef uint32_t bus_uint23_t;
typedef uint32_t bus_uint24_t;
typedef uint32_t bus_uint25_t;
typedef uint32_t bus_uint26_t;
typedef uint32_t bus_uint27_t;
typedef uint32_t bus_uint28_t;
typedef uint32_t bus_uint29_t;
typedef uint32_t bus_uint30_t;
typedef uint32_t bus_uint31_t;
typedef uint32_t bus_uint32_t;
typedef int32_t bus_int1_t;
typedef int32_t bus_int2_t;
typedef int32_t bus_int3_t;
typedef int32_t bus_int4_t;
typedef int32_t bus_int5_t;
typedef int32_t bus_int6_t;
typedef int32_t bus_int7_t;
typedef int32_t bus_int8_t;
typedef int32_t bus_int9_t;
typedef int32_t bus_int10_t;
typedef int32_t bus_int11_t;
typedef int32_t bus_int12_t;
typedef int32_t bus_int13_t;
typedef int32_t bus_int14_t;
typedef int32_t bus_int15_t;
typedef int32_t bus_int16_t;
typedef int32_t bus_int17_t;
typedef int32_t bus_int18_t;
typedef int32_t bus_int19_t;
typedef int32_t bus_int20_t;
typedef int32_t bus_int21_t;
typedef int32_t bus_int22_t;
typedef int32_t bus_int23_t;
typedef int32_t bus_int24_t;
typedef int32_t bus_int25_t;
typedef int32_t bus_int26_t;
typedef int32_t bus_int27_t;
typedef int32_t bus_int28_t;
typedef int32_t bus_int29_t;
typedef int32_t bus_int30_t;
typedef int32_t bus_int31_t;
typedef int32_t bus_int32_t;

typedef enum {mask, limit, invalidIndex} warningType_t;

typedef void (*warnFunc_p)(warningType_t t, int value, char *def, char *row, char *msg);
void setWarn(warnFunc_p warnFunction);

#endif
