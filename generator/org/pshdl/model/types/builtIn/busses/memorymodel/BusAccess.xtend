/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *     
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for 
 *     reasonable and customary use in describing the origin of the Work.
 * 
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.types.builtIn.busses.memorymodel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashSet
import java.util.LinkedList
import java.util.List

class BusAccess {

	def generateStdDef(boolean withDate) '''/**
 * @file
 * @brief Provides standard definitions that are used by BusAccess.h
 */

#ifndef BusStdDefinitions_h
#define  BusStdDefinitions_h
#include <stdint.h>

«FOR I : 1 .. 32»
///A bit register of width «I»
typedef uint32_t bus_bit«I»_t;
«ENDFOR» 
«FOR I : 1 .. 32» 
///An unsigned register of width «I»
typedef uint32_t bus_uint«I»_t;
«ENDFOR» 
«FOR I : 1 .. 32» 
///A signed register of width «I»
typedef int32_t bus_int«I»_t;
«ENDFOR» 

/**
 * The various levels of warning that can be used.
 */ 
typedef enum {
	mask, /**< The value has simply being masked with an AND operation */
	limit, /**< The value has been saturated within the specified value range */
	error, /**< The value was out of range and an error has been returned */
	invalidIndex /**< The index for accessing the row was invalid */
} warningType_t;

/**
 * A function pointer for providing a custom waring handler 
 */
typedef void (*warnFunc_p)(warningType_t t, uint64_t value, char *def, char *row, char *msg);

/**
 * This methods allows the user to set a custom warning handler. Usually this is used
 * in conjunction with the implementation provided in BusPrint.h.
 *
 * @param warnFunction the new function to use for error reporting
 *
 * Example Usage:
 * @code
 *    #include "BusPrint.h"
 *    setWarn(defaultPrintfWarn);
 * @endcode
 */
void setWarn(warnFunc_p warnFunction);

/**
 * The variable holding the current warning handler
 */
extern warnFunc_p warn;

#endif	
'''

	def generatePrintC(Unit unit, List<Row> rows, boolean withDate) '''/**
 * @file
 * @brief Provides utility methods for printing structs defined by BusAccess.h
 */

#include <stdio.h>
#include "BusAccess.h"
#include "BusPrint.h"

void defaultPrintfWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg) {
    switch (t) {
        case error:
            printf("ERROR for value 0x%llx of definition %s of row %s %s\n",value ,def,row,msg);
            break;
        case limit:
            printf("Limited value 0x%llx for definition %s of row %s %s\n",value ,def,row,msg);
            break;
        case mask:
            printf("Masked value 0x%llx for definition %s of row %s %s\n",value ,def,row,msg);
            break;
        case invalidIndex:
            printf("The index 0x%llx is not valid for the column %s %s\n", value, row, msg);
    }
    
}
«generatePrint(rows)»
'''

	def generatePrintH(Unit unit, List<Row> rows, boolean withDate) '''/**
 * @file
 * @brief Provides utility methods for printing structs defined by BusAccess.h
 */

#ifndef BusPrint_h
#define BusPrint_h

#include "BusAccess.h"
/**
 * An implementation of the warn handler that prints the warning to stdout
 */
void defaultPrintfWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg);

«generatePrintDef(rows)»
#endif

'''

	def generatePrintDef(List<Row> rows) {
		var res = ''''''
		val checkedRows = new HashSet<String>()
		for (Row row : rows) {
			if (!checkedRows.contains(row.name)) {
				res = res + 
'''
/**
 * Prints the values within the «row.name» struct
 * @param data a non-null pointer to the struct
 */
void print«row.name.toFirstUpper»(«row.name»_t *data);
'''
			}
			checkedRows.add(row.name);
		}
		return res;
	}

	def generatePrint(List<Row> rows) {
		var res = ''''''
		val checkedRows = new HashSet<String>()
		for (Row row : rows) {
			if (!checkedRows.contains(row.name)) {
				res = res + '''void print«row.name.toFirstUpper»(«row.name»_t *data){
    printf("«row.name.toFirstUpper» «FOR Definition d : row.allDefs» «d.name»: 0x%0«Math.ceil(
					MemoryModel.getSize(d) / 4f).intValue»x«ENDFOR»\n"«FOR Definition d : row.allDefs», data->«row.
					getVarNameIndex(d)»«ENDFOR»);
}
'''
			}
			checkedRows.add(row.name);
		}
		return res;
	}

	def generateAccessH(Unit unit, List<Row> rows, boolean withDate) '''/**
 * @file
 * @brief this file defines methods and structs for accessing and storing the memory mapped registers.
 * This file was generated from the following definition.
 \verbatim
	«unit.toString» 
 \endverbatim
 */

#ifndef BusDefinitions_h
#define BusDefinitions_h

#include "BusStdDefinitions.h"

«generateDeclarations(unit, rows)»

#endif
'''

	def generateDeclarations(Unit unit, List<Row> rows) {
		var res = ''''''
		val checkedRows = new HashSet<String>()
		for (Row row : rows) {
			val checkedDefs = new HashSet<String>()
			if (!checkedRows.contains(row.name)) {
				res = res + '''
					//Typedef
					/**
					 * This struct stores all fields that are declared within row «row.name»
					 */
					typedef struct «row.name» {
						«FOR Definition d : row.allDefs»«IF !checkedDefs.contains(d.name)»«IF checkedDefs.add(d.name)»«ENDIF»
						///Field «d.name» within row «row.name»
						«d.busType»	«row.getVarNameArray(d)»;
						«ENDIF»«ENDFOR»
					} «row.name»_t;
				'''
				if (row.hasWriteDefs)
					res = res + '''
						// Setter
						«setterDirectDoc(row, rows, true)»
						int set«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition definition : row.writeDefs»«getParameter(
							row, definition, false)»«ENDFOR»);
						«setterDoc(row, rows, true)»
						int set«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *newVal);
					'''
				res = res + '''
					//Getter
					«getterDirectDoc(row, rows, true)»
					int get«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition definition : row.allDefs»«getParameter(
						row, definition, true)»«ENDFOR»);
					«getterDoc(row, rows, true)»
					int get«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *result);
				'''
				checkedRows.add(row.name)
			}
		}
		for (NamedElement ne : unit.declarations.values()) {
			if (!checkedRows.contains(ne.name)) {
				if (ne instanceof Column) {
					val Column col = ne as Column;
					res = res + '''
						///This struct stores all rows defined in colum «col.name»
						typedef struct «col.name» {
							«FOR NamedElement neRow : col.rows»
								///Struct for row «neRow.name»
								«neRow.name»_t «neRow.name»;
							«ENDFOR»
						} «col.name»_t;
					'''
				}
			}
		}
		return res
	}

	def generateAccessC(List<Row> rows, boolean withDate) '''/**
 * @brief Provides access to the memory mapped registers
 * 
 * For each type of row there are methods for setting/getting the values
 * either directly, or as a struct. A memory map overview has been
 * generated into BusMap.html.
 */
 
#include <stdint.h>
#include "BusAccess.h"
#include "BusStdDefinitions.h"

/**
 * This method provides a null implementation of the warning functionality. You
 * can use it to provide your own error handling, or you can use the implementation
 * provided in BusPrint.h
 */
static void defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){
}

warnFunc_p warn=defaultWarn;

/**
 * This methods allows the user to set a custom warning function. Usually this is used
 * in conjunction with the implementation provided in BusPrint.h.
 *
 * @param warnFunction the new function to use for error reporting
 *
 * Example Usage:
 * @code
 *    #include "BusPrint.h"
 *    setWarn(defaultPrintfWarn);
 * @endcode
 */
void setWarn(warnFunc_p warnFunction){
    warn=warnFunction;
}

//Setter functions
«generateSetterFunctions(rows)»

//Getter functions
«generateGetterFunctions(rows)»
'''

	def generateGetterFunctions(List<Row> rows) {
		var String res = ''''''
		val doneRows = new HashSet<String>()
		for (Row row : rows) {
			val handled = doneRows.contains(row.name)
			if (!handled) {
				res = res + generateGetterFunction(row, rows)
			}
			doneRows.add(row.name)
		}
		return res
	}

	def generateGetterFunction(Row row, List<Row> rows) '''
«getterDirectDoc(row, rows, false)»
int get«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition definition : row.
		allDefs»«getParameter(row, definition, true)»«ENDFOR»){
	uint32_t val=0;
	«row.generateAddressReadSwitch(rows)»
	«FOR Definition d : row.allDefs»
	*«row.getVarName(d)»=(val >> «d.shiftVal») & «d.maxValueHex»;
	«ENDFOR»
	return 1;
}

«getterDoc(row, rows, false)»
int get«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *result){
	return get«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.allDefs», &result->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	def setterDoc(Row row, List<Row> rows, boolean header) '''
/*«IF !header»*«ENDIF»
 * Updates the values in memory from the struct.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.
 * @param index the row that you want to access. «IF rows.count(row) == 1»The only valid index is 0«ELSE»Valid values are 0..«rows.
		count(row) - 1»«ENDIF»
 * @param newVal the values of this row will be written into the struct
 *
 * @retval 1  Successfully updated the values
 * @retval 0  Something went wrong (invalid index or value exceeds range for example)
 *
 */	
'''
	
	def int count(List<Row> rows, Row row){
		return rows.filter[name==row.name].length
	}

	def getterDoc(Row row, List<Row> rows, boolean header) '''
/*«IF !header»*«ENDIF»
 * Retrieve the fields of row «row.name» into the struct.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.
 * @param index the row that you want to access. «IF rows.count(row) == 1»The only valid index is 0«ELSE»Valid values are 0..«rows.
		count(row) - 1»«ENDIF»
 * @param result the values of this row will be written into the struct
 *
 * @retval 1  Successfully retrieved the values
 * @retval 0  Something went wrong (invalid index for example)
 *
 */
'''

	def setterDirectDoc(Row row, List<Row> rows, boolean header) '''
/*«IF !header»*«ENDIF»
 * Updates the values in memory from the struct.
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.
 * @param index the row that you want to access. «IF rows.count(row) == 1»The only valid index is 0«ELSE»Valid values are 0..«rows.
		count(row) - 1»«ENDIF»
 «FOR Definition d : row.allDefs»
 * @param «d.name» the value of «d.name» will be written into the register. «explain(d)»
 «ENDFOR»
 *
 * @retval 1  Successfully updated the values
 * @retval 0  Something went wrong (invalid index or value exceeds its range for example)
 *
 */
'''

	def getterDirectDoc(Row row, List<Row> rows, boolean header) '''
/*«IF !header»*«ENDIF»
 * Directly retrieve the fields of row «row.name».
 *
 * @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.
 * @param index the row that you want to access. «IF rows.count(row) == 1»The only valid index is 0«ELSE»Valid values are 0..«rows.
	count(row) - 1»«ENDIF»
 «FOR Definition d : row.allDefs»
 	* @param «d.name» the value of «d.name» will be written into the memory of this pointer.
 «ENDFOR»
 *
 * @retval 1  Successfully retrieved the values
 * @retval 0  Something went wrong (invalid index for example)
 *
 */
'''

	def shiftVal(Definition d) {
		val size = MemoryModel.getSize(d)
		return d.bitPos - (size - 1)
	}

	def generateSetterFunctions(List<Row> rows) {
		var String res = ''''''
		val doneRows = new HashSet<String>()
		for (Row row : rows) {
			val handled = doneRows.contains(row.name)
			if ((!handled) && row.hasWriteDefs) {
				res = res + generateSetterFunction(row, rows)
			}
			doneRows.add(row.name)
		}
		return res
	}

	def generateSetterFunction(Row row, List<Row> rows) '''
«setterDirectDoc(row, rows, false)»
int set«row.name.toFirstUpper»Direct(uint32_t *base, uint32_t index«FOR Definition d : row.writeDefs»«row.
		getParameter(d, false)»«ENDFOR»){
	«FOR Definition ne : row.writeDefs»
		«row.generateConditions(ne)»
	«ENDFOR»
	uint32_t newVal=«FOR Definition d : row.writeDefs»«d.shifted(row)»«ENDFOR» 0;
	«row.generateAddressSwitch(rows)»
	warn(invalidIndex, index, "", "«row.name»", "");
	return 0;
}

«setterDoc(row, rows, false)»
int set«row.name.toFirstUpper»(uint32_t *base, uint32_t index, «row.name»_t *newVal) {
	return set«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.writeDefs», newVal->«row.getVarNameIndex(d)»«ENDFOR»);
}
'''

	def explain(Definition d) {
		val StringBuilder sb = new StringBuilder
		switch (d.warn) {
			case error: {
				sb.append(
					'''When this value exceeds its valid range [«humanRange(d)»], an error is returned and the warn function called.''')
			}
			case limit: {
				sb.append(
					'''When this value exceeds its valid range [«humanRange(d)»], the highest/lowest value is used and the warn function called.''')
			}
			case mask: {
				sb.append(
					'''When this value exceeds its valid range [«humanRange(d)»], the value is masked with 0x«(1l <<
						d.width) - 1» and the warn function called.''')
			}
			case silentError: {
				sb.append('''When this value exceeds its valid range [«humanRange(d)»], an error is returned.''')
			}
			case silentLimit: {
				sb.append(
					'''When this value exceeds its valid range [«humanRange(d)»], an the highest/lowest value is used.''')
			}
			case silentMask: {
				sb.append(
					'''When this value exceeds its valid range [«humanRange(d)»], the value is masked with 0x«(1l <<
						d.width) - 1».''')
			}
		}
		return sb
	}

	def humanRange(Definition d) '''«IF d.type === Definition.Type.INT»«d.maxValueNegHex»«ELSE»0«ENDIF» .. «d.
		maxValueHex»'''

	def shifted(Definition d, Row row) {
		return ''' («row.getVarName(d)» << «d.shiftVal») |'''
	}

	def allDefs(Row row) {
		val List<Definition> res = new LinkedList<Definition>()
		for (NamedElement ne : row.definitions) {
			if ((ne as Definition).type != Definition$Type.UNUSED) {
				res.add(ne as Definition)
			}
		}
		return res
	}

	def writeDefs(Row row) {
		val List<Definition> res = new LinkedList<Definition>()
		for (NamedElement ne : row.definitions) {
			if (ne.hasWrite) {
				res.add(ne as Definition)
			}
		}
		return res
	}

	def generateAddressReadSwitch(Row row, List<Row> rows) {
		var idx = 0
		var rIdx = 0
		var res = '''switch (index) {
			'''
		for (Row r : rows) {
			if (r.name.equals(row.name)) {
				res = res + '''
					case «rIdx»: val=base[«idx»]; break;
				'''
				rIdx = rIdx + 1
			}
			idx = idx + 1
		}
		res = res + '''
			default:
				warn(invalidIndex, index, "", "«row.name»", ""); 
				return 0;
			}
		'''
		return res
	}

	def generateAddressSwitch(Row row, List<Row> rows) {
		var idx = 0
		var rIdx = 0
		var res = '''switch (index) {
			'''
		for (Row r : rows) {
			if (r.name.equals(row.name)) {
				res = res + '''
					case «rIdx»: base[«idx»]=newVal; return 1;
				'''
				rIdx = rIdx + 1
			}
			idx = idx + 1
		}
		res = res + '''}
			'''
		return res
	}

	def generateConditions(Row row, Definition d) '''
		«IF d.width == 32»
		«««		Required because maxValue overflows..		
		«ELSEIF d.warn == Definition$WarnType.silentLimit»
			if («row.getVarName(d)» > «d.maxValueHex») {
				«row.getVarName(d)»=«d.maxValueHex»;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					«row.getVarName(d)»=«d.maxValueNegHex»;
				}
			«ENDIF»
		«ELSEIF d.warn == Definition$WarnType.limit»
			if («row.getVarName(d)» > «d.maxValueHex») {
				warn(limit, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "using «d.maxValueHex»");
				«row.getVarName(d)»=«d.maxValueHex»;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					warn(limit, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "using «d.maxValueNegHex»");
					«row.getVarName(d)»=«d.maxValueNegHex»;
				}
			«ENDIF»
		«ELSEIF d.warn == Definition$WarnType.silentMask»
			if («row.getVarName(d)» > «d.maxValueHex») {
				«row.getVarName(d)»&=«d.maxValueHex»;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					«row.getVarName(d)»&=«d.maxValueNegHex»;
				}
			«ENDIF»
		«ELSEIF d.warn == Definition$WarnType.mask»
			if («row.getVarName(d)» > «d.maxValueHex») {
				warn(mask, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "masking with «d.maxValueHex»");
				«row.getVarName(d)»&=«d.maxValueHex»;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					warn(mask, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "masking with «d.maxValueNegHex»");
					«row.getVarName(d)»&=«d.maxValueNegHex»;
				}
			«ENDIF»
		«ELSEIF d.warn == Definition$WarnType.silentError»
			if («row.getVarName(d)» > «d.maxValueHex») {
				return 0;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					return 0;
				}
			«ENDIF»
		«ELSEIF d.warn == Definition$WarnType.error»
			if («row.getVarName(d)» > «d.maxValueHex») {
				warn(error, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "returning with 0");
				return 0;
			}
			«IF d.type == Definition$Type.INT»
				if («row.getVarName(d)» < «d.maxValueNegHex») {
					warn(error, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "returning with 0");
					return 0;
				}
			«ENDIF»
		«ENDIF»
	'''

	def boolean hasWriteDefs(Row row) {
		row.definitions.findFirst[it.hasWrite] !== null
	}

	def boolean hasWrite(NamedElement ne) {
		(ne as Definition).rw !== Definition$RWType.r && (ne as Definition).type !== Definition$Type.UNUSED
	}

	def getMaxValueHex(Definition d) {
		"0x" + Integer.toHexString(d.maxValue)
	}

	def getMaxValueNegHex(Definition d) {
		"-0x" + Integer.toHexString(d.maxValue + 1)
	}

	def getMaxValue(Definition d) {
		if (d.type != Definition$Type.INT) {
			return (1 << MemoryModel.getSize(d)) - 1
		} else {
			return (1 << (MemoryModel.getSize(d) - 1)) - 1
		}
	}

	def getParameter(Row row, Definition d, boolean pointer) {
		return ''', «d.busType» «IF pointer»*«ENDIF»«row.getVarName(d)»'''
	}

	def getVarName(Row row, Definition d) {
		val dim = row.defCount.get(d.name)
		if (dim == 1) {
			return d.name
		} else {
			return d.name + d.arrayIndex
		}
	}

	def getVarNameIndex(Row row, Definition d) {
		val dim = row.defCount.get(d.name)
		if (dim == 1) {
			return d.name
		} else {
			return d.name + '[' + d.arrayIndex + ']'
		}
	}

	def getVarNameArray(Row row, Definition d) {
		val dim = row.defCount.get(d.name)
		if (dim == 1) {
			return d.name
		} else {
			return d.name + '[' + dim + ']'
		}
	}

	def getBusType(Definition d) '''bus_«d.type.toString.toLowerCase»«MemoryModel.getSize(d)»_t'''

}
