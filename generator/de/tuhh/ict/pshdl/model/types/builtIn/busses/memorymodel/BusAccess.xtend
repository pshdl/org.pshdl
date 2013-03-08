package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.HashSet
import java.util.LinkedList
import java.util.List

class BusAccess {

	def generateStdDef() '''
//
//  BusStdDefinitions.h
//
//  Automatically generated on «SimpleDateFormat::dateTimeInstance.format(new Date)».
//

#ifndef BusStdDefinitions_h
#define  BusStdDefinitions_h
#include <stdint.h>

«FOR I : 1 .. 32» 
typedef uint32_t bus_bit«I»_t;
«ENDFOR» 
«FOR I : 1 .. 32» 
typedef uint32_t bus_uint«I»_t;
«ENDFOR» 
«FOR I : 1 .. 32» 
typedef int32_t bus_int«I»_t;
«ENDFOR» 

typedef enum {mask, limit, error, invalidIndex} warningType_t;

typedef void (*warnFunc_p)(warningType_t t, int value, char *def, char *row, char *msg);
void setWarn(warnFunc_p warnFunction);

#endif	
'''

	def generatePrintC(Unit unit, List<Row> rows) '''//
//  BusPrint.c
//
//  Automatically generated on «SimpleDateFormat::dateTimeInstance.format(new Date)».
//

#include <stdio.h>
#include "BusAccess.h"

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
«generatePrint(rows)»
'''

	def generatePrintH(Unit unit, List<Row> rows) '''//
//  BusPrint.h
//
//  Automatically generated on «SimpleDateFormat::dateTimeInstance.format(new Date)».
//

#ifndef BusPrint_h
#define BusPrint_h

#include "BusAccess.h"
void defaultPrintfWarn(warningType_t t, int value, char *def, char *row, char *msg);

«generatePrintDef(rows)»
#endif

'''

	def generatePrintDef(List<Row> rows) {
		var res = ''''''
		val checkedRows = new HashSet<String>()
		for (Row row : rows) {
			if (!checkedRows.contains(row.name)) {
				res = res + '''void print«row.name.toFirstUpper»(«row.name»_t *data);
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
    printf("«row.name.toFirstUpper» «FOR Definition d : row.allDefs» «d.name»: 0x%0«Math::ceil(
					MemoryModel::getSize(d) / 4f).intValue»x«ENDFOR»\n"«FOR Definition d : row.allDefs», data->«row.
					getVarNameIndex(d)»«ENDFOR»);
}
'''
			}
			checkedRows.add(row.name);
		}
		return res;
	}

	def generateAccessH(Unit unit, List<Row> rows) '''//
//  BusDefinitions.h
//
//  Automatically generated on «SimpleDateFormat::dateTimeInstance.format(new Date)».
//

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
					typedef struct «row.name» {
						«FOR Definition d : row.allDefs»«IF !checkedDefs.contains(d.name)»«IF checkedDefs.add(d.name)»«ENDIF»
						«d.busType»	«row.getVarNameArray(d)»;
						«ENDIF»«ENDFOR»
					} «row.name»_t;
				'''
				if (row.hasWriteDefs)
					res = res + '''
						// Setter
						int set«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition definition : row.writeDefs»«getParameter(
							row, definition, false)»«ENDFOR»);
						int set«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *newVal);
					'''
				res = res + '''
					//Getter
					int get«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition definition : row.allDefs»«getParameter(
						row, definition, true)»«ENDFOR»);
					int get«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *result);
				'''
				checkedRows.add(row.name)
			}
		}
		for (NamedElement ne : unit.declarations.values()) {
			if (!checkedRows.contains(ne.name)) {
				if (ne instanceof Column) {
					val Column col = ne as Column;
					res = res + '''
						typedef struct «col.name» {
							«FOR NamedElement neRow : col.rows»
								«neRow.name»_t «neRow.name»;
							«ENDFOR»
						} «col.name»_t;
					'''
				}
			}
		}
		return res
	}

	def generateAccessC(List<Row> rows) '''//
//  BusAcces.c
//
//  Automatically generated on «SimpleDateFormat::dateTimeInstance.format(new Date)».
//

#include <stdint.h>
#include "BusAccess.h"
#include "BusStdDefinitions.h"

static void defaultWarn(warningType_t t, int value, char *def, char *row, char *msg){
}

warnFunc_p warn=defaultWarn;

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
int get«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition definition : row.allDefs»«getParameter(
		row, definition, true)»«ENDFOR»){
	uint32_t val=0;
	«row.generateAddressReadSwitch(rows)»
	«FOR Definition d : row.allDefs»
	*«row.getVarName(d)»=(val >> «d.shiftVal») & «d.maxValueHex»;
	«ENDFOR»
	return 1;
}
int get«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *result){
	return get«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.allDefs», &result->«row.
		getVarNameIndex(d)»«ENDFOR»);
}
'''

	def shiftVal(Definition d) {
		val size = MemoryModel::getSize(d)
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
int set«row.name.toFirstUpper»Direct(uint32_t *base, int index«FOR Definition d : row.writeDefs»«row.
		getParameter(d, false)»«ENDFOR»){
	«FOR Definition ne : row.writeDefs»
		«row.generateConditions(ne)»
	«ENDFOR»
	uint32_t newVal=«FOR Definition d : row.writeDefs»«d.shifted(row)»«ENDFOR» 0;
	«row.generateAddressSwitch(rows)»
	warn(invalidIndex, index, "", "«row.name»", "");
	return 0;
}
int set«row.name.toFirstUpper»(uint32_t *base, int index, «row.name»_t *newVal) {
	return set«row.name.toFirstUpper»Direct(base, index«FOR Definition d : row.writeDefs», newVal->«row.
		getVarNameIndex(d)»«ENDFOR»);
}
'''

	def shifted(Definition d, Row row) {
		return ''' («row.getVarName(d)» << «d.shiftVal») |'''
	}

	def allDefs(Row row) {
		val List<Definition> res = new LinkedList<Definition>()
		for (NamedElement ne : row.definitions) {
			if ((ne as Definition).type != Definition$Type::UNUSED) {
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
					«IF d.warn == Definition$WarnType::silentLimit»
						if («row.getVarName(d)» > «d.maxValueHex») {
							«row.getVarName(d)»=«d.maxValueHex»;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								«row.getVarName(d)»=«d.maxValueNegHex»;
							}
						«ENDIF»
					«ELSEIF d.warn == Definition$WarnType::limit»
						if («row.getVarName(d)» > «d.maxValueHex») {
							warn(limit, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "using «d.maxValueHex»");
							«row.getVarName(d)»=«d.maxValueHex»;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								warn(limit, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "using «d.maxValueNegHex»");
								«row.getVarName(d)»=«d.maxValueNegHex»;
							}
						«ENDIF»
					«ELSEIF d.warn == Definition$WarnType::silentMask»
						if («row.getVarName(d)» > «d.maxValueHex») {
							«row.getVarName(d)»&=«d.maxValueHex»;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								«row.getVarName(d)»&=«d.maxValueNegHex»;
							}
						«ENDIF»
					«ELSEIF d.warn == Definition$WarnType::mask»
						if («row.getVarName(d)» > «d.maxValueHex») {
							warn(mask, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "masking with «d.maxValueHex»");
							«row.getVarName(d)»&=«d.maxValueHex»;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								warn(mask, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "masking with «d.maxValueNegHex»");
								«row.getVarName(d)»&=«d.maxValueNegHex»;
							}
						«ENDIF»
					«ELSEIF d.warn == Definition$WarnType::silentError»
						if («row.getVarName(d)» > «d.maxValueHex») {
							return 0;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								return 0;
							}
						«ENDIF»
					«ELSEIF d.warn == Definition$WarnType::error»
						if («row.getVarName(d)» > «d.maxValueHex») {
							warn(error, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "returning with 0");
							return 0;
						}
						«IF d.type == Definition$Type::INT»
							if («row.getVarName(d)» < «d.maxValueNegHex») {
								warn(error, «row.getVarName(d)», "«row.getVarNameIndex(d)»", "«row.name»", "returning with 0");
								return 0;
							}
						«ENDIF»
					«ENDIF»
				'''

				def boolean hasWriteDefs(Row row) {
					for (NamedElement ne : row.definitions) {
						if (ne.hasWrite)
							return true
					}
					return false
				}

				def boolean hasWrite(NamedElement ne) {
					(ne as Definition).rw != Definition$RWType::r && (ne as Definition).type != Definition$Type::UNUSED
				}

				def getMaxValueHex(Definition d) {
					"0x" + Integer::toHexString(d.maxValue)
				}

				def getMaxValueNegHex(Definition d) {
					"0x" + Integer::toHexString(-d.maxValue - 1)
				}

				def getMaxValue(Definition d) {
					if (d.type != Definition$Type::INT) {
						return (1 << MemoryModel::getSize(d)) - 1
					} else {
						return (1 << (MemoryModel::getSize(d) - 1)) - 1
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

				def getBusType(Definition d) '''bus_«d.type.toString.toLowerCase»«MemoryModel::getSize(d)»_t'''

			}
			