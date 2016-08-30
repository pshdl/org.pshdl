/**
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
 */
package org.pshdl.model.types.builtIn.busses.memorymodel;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.StringExtensions;

import com.google.common.base.Objects;

@SuppressWarnings("all")
public class BusAccess {
	public CharSequence generateStdDef(final boolean withDate) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @file");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @brief Provides standard definitions that are used by BusAccess.h");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.newLine();
		_builder.append("#ifndef BusStdDefinitions_h");
		_builder.newLine();
		_builder.append("#define  BusStdDefinitions_h");
		_builder.newLine();
		_builder.append("#include <stdint.h>");
		_builder.newLine();
		_builder.newLine();
		{
			final IntegerRange _upTo = new IntegerRange(1, 32);
			for (final Integer I : _upTo) {
				_builder.append("///A bit register of width ");
				_builder.append(I, "");
				_builder.newLineIfNotEmpty();
				_builder.append("typedef uint32_t bus_bit");
				_builder.append(I, "");
				_builder.append("_t;");
				_builder.newLineIfNotEmpty();
			}
		}
		{
			final IntegerRange _upTo_1 = new IntegerRange(1, 32);
			for (final Integer I_1 : _upTo_1) {
				_builder.append("///An unsigned register of width ");
				_builder.append(I_1, "");
				_builder.newLineIfNotEmpty();
				_builder.append("typedef uint32_t bus_uint");
				_builder.append(I_1, "");
				_builder.append("_t;");
				_builder.newLineIfNotEmpty();
			}
		}
		{
			final IntegerRange _upTo_2 = new IntegerRange(1, 32);
			for (final Integer I_2 : _upTo_2) {
				_builder.append("///A signed register of width ");
				_builder.append(I_2, "");
				_builder.newLineIfNotEmpty();
				_builder.append("typedef int32_t bus_int");
				_builder.append(I_2, "");
				_builder.append("_t;");
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* The various levels of warning that can be used.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/ ");
		_builder.newLine();
		_builder.append("typedef enum {");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("mask, /**< The value has simply being masked with an AND operation */");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("limit, /**< The value has been saturated within the specified value range */");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("error, /**< The value was out of range and an error has been returned */");
		_builder.newLine();
		_builder.append("\t");
		_builder.append("invalidIndex /**< The index for accessing the row was invalid */");
		_builder.newLine();
		_builder.append("} warningType_t;");
		_builder.newLine();
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* A function pointer for providing a custom waring handler ");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("typedef void (*warnFunc_p)(warningType_t t, uint64_t value, char *def, char *row, char *msg);");
		_builder.newLine();
		_builder.newLine();
		_builder.newLine();
		_builder.append("#endif\t");
		_builder.newLine();
		return _builder;
	}

	public CharSequence generatePrintC(final Unit unit, final String prefix, final List<Row> rows, final boolean withDate) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @file");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @brief Provides utility methods for printing structs defined by BusAccess.h");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.newLine();
		_builder.append("#include <stdio.h>");
		_builder.newLine();
		_builder.append("#include \"");
		_builder.append(prefix, "");
		_builder.append("BusAccess.h\"");
		_builder.newLineIfNotEmpty();
		_builder.append("#include \"");
		_builder.append(prefix, "");
		_builder.append("BusPrint.h\"");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("void ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, "");
		_builder.append("defaultPrintfWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg) {");
		_builder.newLineIfNotEmpty();
		_builder.append("    ");
		_builder.append("switch (t) {");
		_builder.newLine();
		_builder.append("        ");
		_builder.append("case error:");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("printf(\"ERROR for value 0x%llx of definition %s of row %s %s\\n\",value ,def,row,msg);");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("break;");
		_builder.newLine();
		_builder.append("        ");
		_builder.append("case limit:");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("printf(\"Limited value 0x%llx for definition %s of row %s %s\\n\",value ,def,row,msg);");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("break;");
		_builder.newLine();
		_builder.append("        ");
		_builder.append("case mask:");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("printf(\"Masked value 0x%llx for definition %s of row %s %s\\n\",value ,def,row,msg);");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("break;");
		_builder.newLine();
		_builder.append("        ");
		_builder.append("case invalidIndex:");
		_builder.newLine();
		_builder.append("            ");
		_builder.append("printf(\"The index 0x%llx is not valid for the column %s %s\\n\", value, row, msg);");
		_builder.newLine();
		_builder.append("    ");
		_builder.append("}");
		_builder.newLine();
		_builder.append("    ");
		_builder.newLine();
		_builder.append("}");
		_builder.newLine();
		final String _generatePrint = this.generatePrint(rows, prefix);
		_builder.append(_generatePrint, "");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	public CharSequence generatePrintH(final Unit unit, final String prefix, final List<Row> rows, final boolean withDate) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @file");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @brief Provides utility methods for printing structs defined by BusAccess.h");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.newLine();
		_builder.append("#ifndef ");
		_builder.append(prefix, "");
		_builder.append("BusPrint_h");
		_builder.newLineIfNotEmpty();
		_builder.append("#define ");
		_builder.append(prefix, "");
		_builder.append("BusPrint_h");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("#include \"");
		_builder.append(prefix, "");
		_builder.append("BusAccess.h\"");
		_builder.newLineIfNotEmpty();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* An implementation of the warn handler that prints the warning to stdout");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("void ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, "");
		_builder.append("defaultPrintfWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg);");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		final String _generatePrintDef = this.generatePrintDef(rows, prefix);
		_builder.append(_generatePrintDef, "");
		_builder.newLineIfNotEmpty();
		_builder.append("#endif");
		_builder.newLine();
		_builder.newLine();
		return _builder;
	}

	public String generatePrintDef(final List<Row> rows, final String prefix) {
		final StringConcatenation _builder = new StringConcatenation();
		String res = _builder.toString();
		final LinkedHashSet<String> checkedRows = new LinkedHashSet<String>();
		for (final Row row : rows) {
			{
				final boolean _contains = checkedRows.contains(row.name);
				final boolean _not = (!_contains);
				if (_not) {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("/**");
					_builder_1.newLine();
					_builder_1.append(" ");
					_builder_1.append("* Prints the values within the ");
					_builder_1.append(row.name, " ");
					_builder_1.append(" struct");
					_builder_1.newLineIfNotEmpty();
					_builder_1.append(" ");
					_builder_1.append("* @param data a non-null pointer to the struct");
					_builder_1.newLine();
					_builder_1.append(" ");
					_builder_1.append("*/");
					_builder_1.newLine();
					_builder_1.append("void ");
					final String _prefix = this.getPrefix(prefix);
					_builder_1.append(_prefix, "");
					_builder_1.append("print");
					final String _firstUpper = StringExtensions.toFirstUpper(row.name);
					_builder_1.append(_firstUpper, "");
					_builder_1.append("(");
					final String _prefix_1 = this.getPrefix(prefix);
					_builder_1.append(_prefix_1, "");
					_builder_1.append(row.name, "");
					_builder_1.append("_t *data);");
					_builder_1.newLineIfNotEmpty();
					final String _plus = (res + _builder_1);
					res = _plus;
				}
				checkedRows.add(row.name);
			}
		}
		return res;
	}

	public String generatePrint(final List<Row> rows, final String prefix) {
		final StringConcatenation _builder = new StringConcatenation();
		String res = _builder.toString();
		final LinkedHashSet<String> checkedRows = new LinkedHashSet<String>();
		for (final Row row : rows) {
			{
				final boolean _contains = checkedRows.contains(row.name);
				final boolean _not = (!_contains);
				if (_not) {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("void ");
					final String _prefix = this.getPrefix(prefix);
					_builder_1.append(_prefix, "");
					_builder_1.append("print");
					final String _firstUpper = StringExtensions.toFirstUpper(row.name);
					_builder_1.append(_firstUpper, "");
					_builder_1.append("(");
					final String _prefix_1 = this.getPrefix(prefix);
					_builder_1.append(_prefix_1, "");
					_builder_1.append(row.name, "");
					_builder_1.append("_t *data){");
					_builder_1.newLineIfNotEmpty();
					_builder_1.append("    ");
					_builder_1.append("printf(\"");
					final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
					_builder_1.append(_firstUpper_1, "    ");
					_builder_1.append(" ");
					{
						final List<Definition> _allDefs = this.allDefs(row);
						for (final Definition d : _allDefs) {
							_builder_1.append(" ");
							_builder_1.append(d.name, "    ");
							_builder_1.append(": 0x%0");
							final int _size = MemoryModel.getSize(d);
							final float _divide = (_size / 4f);
							final double _ceil = Math.ceil(_divide);
							final int _intValue = Double.valueOf(_ceil).intValue();
							_builder_1.append(_intValue, "    ");
							_builder_1.append("x");
						}
					}
					_builder_1.append("\\n\"");
					{
						final List<Definition> _allDefs_1 = this.allDefs(row);
						for (final Definition d_1 : _allDefs_1) {
							_builder_1.append(", data->");
							final String _varNameIndex = this.getVarNameIndex(row, d_1);
							_builder_1.append(_varNameIndex, "    ");
						}
					}
					_builder_1.append(");");
					_builder_1.newLineIfNotEmpty();
					_builder_1.append("}");
					_builder_1.newLine();
					final String _plus = (res + _builder_1);
					res = _plus;
				}
				checkedRows.add(row.name);
			}
		}
		return res;
	}

	public CharSequence generateAccessH(final Unit unit, final String prefix, final List<Row> rows, final boolean withDate) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @file");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @brief this file defines methods and structs for accessing and storing the memory mapped registers.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* This file was generated from the following definition.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("\\verbatim");
		_builder.newLine();
		_builder.append("\t");
		final String _string = unit.toString();
		_builder.append(_string, "\t");
		_builder.append(" ");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("\\endverbatim");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.newLine();
		_builder.append("#ifndef ");
		_builder.append(prefix, "");
		_builder.append("BusDefinitions_h");
		_builder.newLineIfNotEmpty();
		_builder.append("#define ");
		_builder.append(prefix, "");
		_builder.append("BusDefinitions_h");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("#include \"BusStdDefinitions.h\"");
		_builder.newLine();
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* This methods allows the user to set a custom warning handler. Usually this is used");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* in conjunction with the implementation provided in BusPrint.h.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param warnFunction the new function to use for error reporting");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* Example Usage:");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @code");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*    #include \"");
		_builder.append(prefix, " ");
		_builder.append("BusPrint.h\"");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*    ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, " ");
		_builder.append("setWarn(defaultPrintfWarn);");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* @endcode");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("void ");
		final String _prefix_1 = this.getPrefix(prefix);
		_builder.append(_prefix_1, "");
		_builder.append("setWarn(warnFunc_p warnFunction);");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* The variable holding the current warning handler");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("extern warnFunc_p ");
		final String _prefix_2 = this.getPrefix(prefix);
		_builder.append(_prefix_2, "");
		_builder.append("warn;");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		final String _generateDeclarations = this.generateDeclarations(unit, prefix, rows);
		_builder.append(_generateDeclarations, "");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("#endif");
		_builder.newLine();
		return _builder;
	}

	public String generateDeclarations(final Unit unit, final String prefix, final List<Row> rows) {
		final StringConcatenation _builder = new StringConcatenation();
		String res = _builder.toString();
		final LinkedHashSet<String> checkedRows = new LinkedHashSet<String>();
		for (final Row row : rows) {
			{
				final LinkedHashSet<String> checkedDefs = new LinkedHashSet<String>();
				final boolean _contains = checkedRows.contains(row.name);
				final boolean _not = (!_contains);
				if (_not) {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("//Typedef");
					_builder_1.newLine();
					_builder_1.append("/**");
					_builder_1.newLine();
					_builder_1.append(" ");
					_builder_1.append("* This struct stores all fields that are declared within row ");
					_builder_1.append(row.name, " ");
					_builder_1.newLineIfNotEmpty();
					_builder_1.append(" ");
					_builder_1.append("*/");
					_builder_1.newLine();
					_builder_1.append("typedef struct ");
					final String _prefix = this.getPrefix(prefix);
					_builder_1.append(_prefix, "");
					_builder_1.append(row.name, "");
					_builder_1.append(" {");
					_builder_1.newLineIfNotEmpty();
					{
						final List<Definition> _allDefs = this.allDefs(row);
						for (final Definition d : _allDefs) {
							{
								final boolean _contains_1 = checkedDefs.contains(d.name);
								final boolean _not_1 = (!_contains_1);
								if (_not_1) {
									{
										final boolean _add = checkedDefs.add(d.name);
										if (_add) {
										}
									}
									_builder_1.append("\t");
									_builder_1.append("///Field ");
									_builder_1.append(d.name, "\t");
									_builder_1.append(" within row ");
									_builder_1.append(row.name, "\t");
									_builder_1.newLineIfNotEmpty();
									_builder_1.append("\t");
									final CharSequence _busType = this.getBusType(d);
									_builder_1.append(_busType, "\t");
									_builder_1.append("\t");
									final String _varNameArray = this.getVarNameArray(row, d);
									_builder_1.append(_varNameArray, "\t");
									_builder_1.append(";");
									_builder_1.newLineIfNotEmpty();
								}
							}
						}
					}
					_builder_1.append("} ");
					final String _prefix_1 = this.getPrefix(prefix);
					_builder_1.append(_prefix_1, "");
					_builder_1.append(row.name, "");
					_builder_1.append("_t;");
					_builder_1.newLineIfNotEmpty();
					final String _plus = (res + _builder_1);
					res = _plus;
					final boolean _hasWriteDefs = this.hasWriteDefs(row);
					if (_hasWriteDefs) {
						final StringConcatenation _builder_2 = new StringConcatenation();
						_builder_2.append("// Setter");
						_builder_2.newLine();
						final CharSequence _setterDirectDoc = this.setterDirectDoc(row, rows, true);
						_builder_2.append(_setterDirectDoc, "");
						_builder_2.newLineIfNotEmpty();
						_builder_2.append("int ");
						final String _prefix_2 = this.getPrefix(prefix);
						_builder_2.append(_prefix_2, "");
						_builder_2.append("set");
						final String _firstUpper = StringExtensions.toFirstUpper(row.name);
						_builder_2.append(_firstUpper, "");
						_builder_2.append("Direct(uint32_t *base, uint32_t index");
						{
							final List<Definition> _writeDefs = this.writeDefs(row);
							for (final Definition definition : _writeDefs) {
								final String _parameter = this.getParameter(row, definition, false);
								_builder_2.append(_parameter, "");
							}
						}
						_builder_2.append(");");
						_builder_2.newLineIfNotEmpty();
						final CharSequence _setterDoc = this.setterDoc(row, rows, true);
						_builder_2.append(_setterDoc, "");
						_builder_2.newLineIfNotEmpty();
						_builder_2.append("int ");
						final String _prefix_3 = this.getPrefix(prefix);
						_builder_2.append(_prefix_3, "");
						_builder_2.append("set");
						final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
						_builder_2.append(_firstUpper_1, "");
						_builder_2.append("(uint32_t *base, uint32_t index, ");
						final String _prefix_4 = this.getPrefix(prefix);
						_builder_2.append(_prefix_4, "");
						_builder_2.append(row.name, "");
						_builder_2.append("_t *newVal);");
						_builder_2.newLineIfNotEmpty();
						final String _plus_1 = (res + _builder_2);
						res = _plus_1;
					}
					final StringConcatenation _builder_3 = new StringConcatenation();
					_builder_3.append("//Getter");
					_builder_3.newLine();
					final CharSequence _terDirectDoc = this.getterDirectDoc(row, rows, true);
					_builder_3.append(_terDirectDoc, "");
					_builder_3.newLineIfNotEmpty();
					_builder_3.append("int ");
					final String _prefix_5 = this.getPrefix(prefix);
					_builder_3.append(_prefix_5, "");
					_builder_3.append("get");
					final String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
					_builder_3.append(_firstUpper_2, "");
					_builder_3.append("Direct(uint32_t *base, uint32_t index");
					{
						final List<Definition> _allDefs_1 = this.allDefs(row);
						for (final Definition definition_1 : _allDefs_1) {
							final String _parameter_1 = this.getParameter(row, definition_1, true);
							_builder_3.append(_parameter_1, "");
						}
					}
					_builder_3.append(");");
					_builder_3.newLineIfNotEmpty();
					final CharSequence _terDoc = this.getterDoc(row, rows, true);
					_builder_3.append(_terDoc, "");
					_builder_3.newLineIfNotEmpty();
					_builder_3.append("int ");
					final String _prefix_6 = this.getPrefix(prefix);
					_builder_3.append(_prefix_6, "");
					_builder_3.append("get");
					final String _firstUpper_3 = StringExtensions.toFirstUpper(row.name);
					_builder_3.append(_firstUpper_3, "");
					_builder_3.append("(uint32_t *base, uint32_t index, ");
					final String _prefix_7 = this.getPrefix(prefix);
					_builder_3.append(_prefix_7, "");
					_builder_3.append(row.name, "");
					_builder_3.append("_t *result);");
					_builder_3.newLineIfNotEmpty();
					final String _plus_2 = (res + _builder_3);
					res = _plus_2;
					checkedRows.add(row.name);
				}
			}
		}
		final Collection<NamedElement> _values = unit.declarations.values();
		for (final NamedElement ne : _values) {
			final String _name = ne.getName();
			final boolean _contains = checkedRows.contains(_name);
			final boolean _not = (!_contains);
			if (_not) {
				if ((ne instanceof Column)) {
					final Column col = ((Column) ne);
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("///This struct stores all rows defined in colum ");
					_builder_1.append(col.name, "");
					_builder_1.newLineIfNotEmpty();
					_builder_1.append("typedef struct ");
					final String _prefix = this.getPrefix(prefix);
					_builder_1.append(_prefix, "");
					_builder_1.append(col.name, "");
					_builder_1.append(" {");
					_builder_1.newLineIfNotEmpty();
					{
						for (final NamedElement neRow : col.rows) {
							_builder_1.append("\t");
							_builder_1.append("///Struct for row ");
							final String _name_1 = neRow.getName();
							_builder_1.append(_name_1, "\t");
							_builder_1.newLineIfNotEmpty();
							_builder_1.append("\t");
							final String _simpleName = neRow.getSimpleName();
							_builder_1.append(_simpleName, "\t");
							_builder_1.append("_t ");
							final String _simpleName_1 = neRow.getSimpleName();
							_builder_1.append(_simpleName_1, "\t");
							_builder_1.append(";");
							_builder_1.newLineIfNotEmpty();
						}
					}
					_builder_1.append("} ");
					final String _prefix_1 = this.getPrefix(prefix);
					_builder_1.append(_prefix_1, "");
					_builder_1.append(col.name, "");
					_builder_1.append("_t;");
					_builder_1.newLineIfNotEmpty();
					final String _plus = (res + _builder_1);
					res = _plus;
				}
			}
		}
		return res;
	}

	public CharSequence generateAccessC(final List<Row> rows, final String prefix, final boolean withDate) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @brief Provides access to the memory mapped registers");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* ");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* For each type of row there are methods for setting/getting the values");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* either directly, or as a struct. A memory map overview has been");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* generated into BusMap.html.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append(" ");
		_builder.newLine();
		_builder.append("#include <stdint.h>");
		_builder.newLine();
		_builder.append("#include \"");
		_builder.append(prefix, "");
		_builder.append("BusAccess.h\"");
		_builder.newLineIfNotEmpty();
		_builder.append("#include \"BusStdDefinitions.h\"");
		_builder.newLine();
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* This method provides a null implementation of the warning functionality. You");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* can use it to provide your own error handling, or you can use the implementation");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* provided in ");
		_builder.append(prefix, " ");
		_builder.append("BusPrint.h");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("static void ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, "");
		_builder.append("defaultWarn(warningType_t t, uint64_t value, char *def, char *row, char *msg){");
		_builder.newLineIfNotEmpty();
		_builder.append("}");
		_builder.newLine();
		_builder.newLine();
		_builder.append("warnFunc_p ");
		final String _prefix_1 = this.getPrefix(prefix);
		_builder.append(_prefix_1, "");
		_builder.append("warn=");
		final String _prefix_2 = this.getPrefix(prefix);
		_builder.append(_prefix_2, "");
		_builder.append("defaultWarn;");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("/**");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* This methods allows the user to set a custom warning function. Usually this is used");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* in conjunction with the implementation provided in ");
		_builder.append(prefix, " ");
		_builder.append("BusPrint.h.");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param warnFunction the new function to use for error reporting");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* Example Usage:");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @code");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*    #include \"");
		_builder.append(prefix, " ");
		_builder.append("BusPrint.h\"");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*    ");
		final String _prefix_3 = this.getPrefix(prefix);
		_builder.append(_prefix_3, " ");
		_builder.append("setWarn(");
		_builder.append(prefix, " ");
		_builder.append("defaultPrintfWarn);");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* @endcode");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		_builder.append("void ");
		final String _prefix_4 = this.getPrefix(prefix);
		_builder.append(_prefix_4, "");
		_builder.append("setWarn(warnFunc_p warnFunction){");
		_builder.newLineIfNotEmpty();
		_builder.append("    ");
		final String _prefix_5 = this.getPrefix(prefix);
		_builder.append(_prefix_5, "    ");
		_builder.append("warn=warnFunction;");
		_builder.newLineIfNotEmpty();
		_builder.append("}");
		_builder.newLine();
		_builder.newLine();
		_builder.append("//Setter functions");
		_builder.newLine();
		final String _generateSetterFunctions = this.generateSetterFunctions(rows, prefix);
		_builder.append(_generateSetterFunctions, "");
		_builder.newLineIfNotEmpty();
		_builder.newLine();
		_builder.append("//Getter functions");
		_builder.newLine();
		final String _generateGetterFunctions = this.generateGetterFunctions(rows, prefix);
		_builder.append(_generateGetterFunctions, "");
		_builder.newLineIfNotEmpty();
		return _builder;
	}

	public String getPrefix(final String string) {
		final boolean _equals = Objects.equal(string, "");
		if (_equals)
			return "";
		return (string + "_");
	}

	public String generateGetterFunctions(final List<Row> rows, final String prefix) {
		final StringConcatenation _builder = new StringConcatenation();
		String res = _builder.toString();
		final LinkedHashSet<String> doneRows = new LinkedHashSet<String>();
		for (final Row row : rows) {
			{
				final boolean handled = doneRows.contains(row.name);
				if ((!handled)) {
					final CharSequence _generateGetterFunction = this.generateGetterFunction(row, prefix, rows);
					final String _plus = (res + _generateGetterFunction);
					res = _plus;
				}
				doneRows.add(row.name);
			}
		}
		return res;
	}

	public CharSequence generateGetterFunction(final Row row, final String prefix, final List<Row> rows) {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _terDirectDoc = this.getterDirectDoc(row, rows, false);
		_builder.append(_terDirectDoc, "");
		_builder.newLineIfNotEmpty();
		_builder.append("int ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, "");
		_builder.append("get");
		final String _firstUpper = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper, "");
		_builder.append("Direct(uint32_t *base, uint32_t index");
		{
			final List<Definition> _allDefs = this.allDefs(row);
			for (final Definition definition : _allDefs) {
				final String _parameter = this.getParameter(row, definition, true);
				_builder.append(_parameter, "");
			}
		}
		_builder.append("){");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("uint32_t val=0;");
		_builder.newLine();
		_builder.append("\t");
		final String _generateAddressReadSwitch = this.generateAddressReadSwitch(row, prefix, rows);
		_builder.append(_generateAddressReadSwitch, "\t");
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _allDefs_1 = this.allDefs(row);
			for (final Definition d : _allDefs_1) {
				{
					if ((d.width == 32)) {
						_builder.append("\t");
						_builder.append("*");
						final String _varName = this.getVarName(row, d);
						_builder.append(_varName, "\t");
						_builder.append("=val;");
						_builder.newLineIfNotEmpty();
					} else {
						_builder.append("\t");
						_builder.append("*");
						final String _varName_1 = this.getVarName(row, d);
						_builder.append(_varName_1, "\t");
						_builder.append("=(val >> ");
						final int _shiftVal = this.shiftVal(d);
						_builder.append(_shiftVal, "\t");
						_builder.append(") & ");
						final String _maxValueHex = this.getMaxValueHex(d);
						_builder.append(_maxValueHex, "\t");
						_builder.append(";");
						_builder.newLineIfNotEmpty();
					}
				}
			}
		}
		_builder.append("\t");
		_builder.append("return 1;");
		_builder.newLine();
		_builder.append("}");
		_builder.newLine();
		_builder.newLine();
		final CharSequence _terDoc = this.getterDoc(row, rows, false);
		_builder.append(_terDoc, "");
		_builder.newLineIfNotEmpty();
		_builder.append("int ");
		final String _prefix_1 = this.getPrefix(prefix);
		_builder.append(_prefix_1, "");
		_builder.append("get");
		final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_1, "");
		_builder.append("(uint32_t *base, uint32_t index, ");
		final String _prefix_2 = this.getPrefix(prefix);
		_builder.append(_prefix_2, "");
		_builder.append(row.name, "");
		_builder.append("_t *result){");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("return ");
		final String _prefix_3 = this.getPrefix(prefix);
		_builder.append(_prefix_3, "\t");
		_builder.append("get");
		final String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_2, "\t");
		_builder.append("Direct(base, index");
		{
			final List<Definition> _allDefs_2 = this.allDefs(row);
			for (final Definition d_1 : _allDefs_2) {
				_builder.append(", &result->");
				final String _varNameIndex = this.getVarNameIndex(row, d_1);
				_builder.append(_varNameIndex, "\t");
			}
		}
		_builder.append(");");
		_builder.newLineIfNotEmpty();
		_builder.append("}");
		_builder.newLine();
		return _builder;
	}

	public CharSequence setterDoc(final Row row, final List<Row> rows, final boolean header) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/*");
		{
			if ((!header)) {
				_builder.append("*");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* Updates the values in memory from the struct.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param index the row that you want to access. ");
		{
			final int _count = this.count(rows, row);
			final boolean _equals = (_count == 1);
			if (_equals) {
				_builder.append("The only valid index is 0");
			} else {
				_builder.append("Valid values are 0..");
				final int _count_1 = this.count(rows, row);
				final int _minus = (_count_1 - 1);
				_builder.append(_minus, " ");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* @param newVal the values of this row will be written into the struct");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 1  Successfully updated the values");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 0  Something went wrong (invalid index or value exceeds range for example)");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/\t");
		_builder.newLine();
		return _builder;
	}

	public int count(final List<Row> rows, final Row row) {
		final Function1<Row, Boolean> _function = new Function1<Row, Boolean>() {
			@Override
			public Boolean apply(final Row it) {
				return Boolean.valueOf(Objects.equal(it.name, row.name));
			}
		};
		final Iterable<Row> _filter = IterableExtensions.<Row> filter(rows, _function);
		return ((Object[]) Conversions.unwrapArray(_filter, Object.class)).length;
	}

	public CharSequence getterDoc(final Row row, final List<Row> rows, final boolean header) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/*");
		{
			if ((!header)) {
				_builder.append("*");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* Retrieve the fields of row ");
		_builder.append(row.name, " ");
		_builder.append(" into the struct.");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param index the row that you want to access. ");
		{
			final int _count = this.count(rows, row);
			final boolean _equals = (_count == 1);
			if (_equals) {
				_builder.append("The only valid index is 0");
			} else {
				_builder.append("Valid values are 0..");
				final int _count_1 = this.count(rows, row);
				final int _minus = (_count_1 - 1);
				_builder.append(_minus, " ");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* @param result the values of this row will be written into the struct");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 1  Successfully retrieved the values");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 0  Something went wrong (invalid index for example)");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		return _builder;
	}

	public CharSequence setterDirectDoc(final Row row, final List<Row> rows, final boolean header) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/*");
		{
			if ((!header)) {
				_builder.append("*");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* Updates the values in memory from the struct.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param index the row that you want to access. ");
		{
			final int _count = this.count(rows, row);
			final boolean _equals = (_count == 1);
			if (_equals) {
				_builder.append("The only valid index is 0");
			} else {
				_builder.append("Valid values are 0..");
				final int _count_1 = this.count(rows, row);
				final int _minus = (_count_1 - 1);
				_builder.append(_minus, " ");
			}
		}
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _allDefs = this.allDefs(row);
			for (final Definition d : _allDefs) {
				_builder.append(" ");
				_builder.append("* @param ");
				_builder.append(d.name, " ");
				_builder.append(" the value of ");
				_builder.append(d.name, " ");
				_builder.append(" will be written into the register. ");
				final StringBuilder _explain = this.explain(d);
				_builder.append(_explain, " ");
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 1  Successfully updated the values");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 0  Something went wrong (invalid index or value exceeds its range for example)");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		return _builder;
	}

	public CharSequence getterDirectDoc(final Row row, final List<Row> rows, final boolean header) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("/*");
		{
			if ((!header)) {
				_builder.append("*");
			}
		}
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("* Directly retrieve the fields of row ");
		_builder.append(row.name, " ");
		_builder.append(".");
		_builder.newLineIfNotEmpty();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param base a (volatile) pointer to the memory offset at which the IP core can be found in memory.");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @param index the row that you want to access. ");
		{
			final int _count = this.count(rows, row);
			final boolean _equals = (_count == 1);
			if (_equals) {
				_builder.append("The only valid index is 0");
			} else {
				_builder.append("Valid values are 0..");
				final int _count_1 = this.count(rows, row);
				final int _minus = (_count_1 - 1);
				_builder.append(_minus, " ");
			}
		}
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _allDefs = this.allDefs(row);
			for (final Definition d : _allDefs) {
				_builder.append(" ");
				_builder.append("* @param ");
				_builder.append(d.name, " ");
				_builder.append(" the value of ");
				_builder.append(d.name, " ");
				_builder.append(" will be written into the memory of this pointer.");
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 1  Successfully retrieved the values");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("* @retval 0  Something went wrong (invalid index for example)");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*");
		_builder.newLine();
		_builder.append(" ");
		_builder.append("*/");
		_builder.newLine();
		return _builder;
	}

	public int shiftVal(final Definition d) {
		final int size = MemoryModel.getSize(d);
		return (d.bitPos - (size - 1));
	}

	public String generateSetterFunctions(final List<Row> rows, final String prefix) {
		final StringConcatenation _builder = new StringConcatenation();
		String res = _builder.toString();
		final LinkedHashSet<String> doneRows = new LinkedHashSet<String>();
		for (final Row row : rows) {
			{
				final boolean handled = doneRows.contains(row.name);
				if (((!handled) && this.hasWriteDefs(row))) {
					final CharSequence _generateSetterFunction = this.generateSetterFunction(row, prefix, rows);
					final String _plus = (res + _generateSetterFunction);
					res = _plus;
				}
				doneRows.add(row.name);
			}
		}
		return res;
	}

	public CharSequence generateSetterFunction(final Row row, final String prefix, final List<Row> rows) {
		final StringConcatenation _builder = new StringConcatenation();
		final CharSequence _setterDirectDoc = this.setterDirectDoc(row, rows, false);
		_builder.append(_setterDirectDoc, "");
		_builder.newLineIfNotEmpty();
		_builder.append("int ");
		final String _prefix = this.getPrefix(prefix);
		_builder.append(_prefix, "");
		_builder.append("set");
		final String _firstUpper = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper, "");
		_builder.append("Direct(uint32_t *base, uint32_t index");
		{
			final List<Definition> _writeDefs = this.writeDefs(row);
			for (final Definition d : _writeDefs) {
				final String _parameter = this.getParameter(row, d, false);
				_builder.append(_parameter, "");
			}
		}
		_builder.append("){");
		_builder.newLineIfNotEmpty();
		{
			final List<Definition> _writeDefs_1 = this.writeDefs(row);
			for (final Definition ne : _writeDefs_1) {
				_builder.append("\t");
				final CharSequence _generateConditions = this.generateConditions(row, prefix, ne);
				_builder.append(_generateConditions, "\t");
				_builder.newLineIfNotEmpty();
			}
		}
		_builder.append("\t");
		_builder.append("uint32_t newVal=");
		{
			final List<Definition> _writeDefs_2 = this.writeDefs(row);
			for (final Definition d_1 : _writeDefs_2) {
				final String _shifted = this.shifted(d_1, row);
				_builder.append(_shifted, "\t");
			}
		}
		_builder.append(" 0;");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		final String _generateAddressSwitch = this.generateAddressSwitch(row, rows);
		_builder.append(_generateAddressSwitch, "\t");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		final String _prefix_1 = this.getPrefix(prefix);
		_builder.append(_prefix_1, "\t");
		_builder.append("warn(invalidIndex, index, \"\", \"");
		_builder.append(row.name, "\t");
		_builder.append("\", \"\");");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("return 0;");
		_builder.newLine();
		_builder.append("}");
		_builder.newLine();
		_builder.newLine();
		final CharSequence _setterDoc = this.setterDoc(row, rows, false);
		_builder.append(_setterDoc, "");
		_builder.newLineIfNotEmpty();
		_builder.append("int ");
		final String _prefix_2 = this.getPrefix(prefix);
		_builder.append(_prefix_2, "");
		_builder.append("set");
		final String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_1, "");
		_builder.append("(uint32_t *base, uint32_t index, ");
		final String _prefix_3 = this.getPrefix(prefix);
		_builder.append(_prefix_3, "");
		_builder.append(row.name, "");
		_builder.append("_t *newVal) {");
		_builder.newLineIfNotEmpty();
		_builder.append("\t");
		_builder.append("return ");
		final String _prefix_4 = this.getPrefix(prefix);
		_builder.append(_prefix_4, "\t");
		_builder.append("set");
		final String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
		_builder.append(_firstUpper_2, "\t");
		_builder.append("Direct(base, index");
		{
			final List<Definition> _writeDefs_3 = this.writeDefs(row);
			for (final Definition d_2 : _writeDefs_3) {
				_builder.append(", newVal->");
				final String _varNameIndex = this.getVarNameIndex(row, d_2);
				_builder.append(_varNameIndex, "\t");
			}
		}
		_builder.append(");");
		_builder.newLineIfNotEmpty();
		_builder.append("}");
		_builder.newLine();
		return _builder;
	}

	public StringBuilder explain(final Definition d) {
		final StringBuilder sb = new StringBuilder();
		final Definition.WarnType _switchValue = d.warn;
		if (_switchValue != null) {
			switch (_switchValue) {
			case error:
				final StringConcatenation _builder = new StringConcatenation();
				_builder.append("When this value exceeds its valid range [");
				final CharSequence _humanRange = this.humanRange(d);
				_builder.append(_humanRange, "");
				_builder.append("], an error is returned and the warn function called.");
				sb.append(_builder);
				break;
			case limit:
				final StringConcatenation _builder_1 = new StringConcatenation();
				_builder_1.append("When this value exceeds its valid range [");
				final CharSequence _humanRange_1 = this.humanRange(d);
				_builder_1.append(_humanRange_1, "");
				_builder_1.append("], the highest/lowest value is used and the warn function called.");
				sb.append(_builder_1);
				break;
			case mask:
				final StringConcatenation _builder_2 = new StringConcatenation();
				_builder_2.append("When this value exceeds its valid range [");
				final CharSequence _humanRange_2 = this.humanRange(d);
				_builder_2.append(_humanRange_2, "");
				_builder_2.append("], the value is masked with 0x");
				_builder_2.append(((1l << d.width) - 1), "");
				_builder_2.append(" and the warn function called.");
				sb.append(_builder_2);
				break;
			case silentError:
				final StringConcatenation _builder_3 = new StringConcatenation();
				_builder_3.append("When this value exceeds its valid range [");
				final CharSequence _humanRange_3 = this.humanRange(d);
				_builder_3.append(_humanRange_3, "");
				_builder_3.append("], an error is returned.");
				sb.append(_builder_3);
				break;
			case silentLimit:
				final StringConcatenation _builder_4 = new StringConcatenation();
				_builder_4.append("When this value exceeds its valid range [");
				final CharSequence _humanRange_4 = this.humanRange(d);
				_builder_4.append(_humanRange_4, "");
				_builder_4.append("], an the highest/lowest value is used.");
				sb.append(_builder_4);
				break;
			case silentMask:
				final StringConcatenation _builder_5 = new StringConcatenation();
				_builder_5.append("When this value exceeds its valid range [");
				final CharSequence _humanRange_5 = this.humanRange(d);
				_builder_5.append(_humanRange_5, "");
				_builder_5.append("], the value is masked with 0x");
				_builder_5.append(((1l << d.width) - 1), "");
				_builder_5.append(".");
				sb.append(_builder_5);
				break;
			default:
				break;
			}
		}
		return sb;
	}

	public CharSequence humanRange(final Definition d) {
		final StringConcatenation _builder = new StringConcatenation();
		{
			if ((d.type == Definition.Type.INT)) {
				final String _maxValueNegHex = this.getMaxValueNegHex(d);
				_builder.append(_maxValueNegHex, "");
			} else {
				_builder.append("0");
			}
		}
		_builder.append(" .. ");
		final String _maxValueHex = this.getMaxValueHex(d);
		_builder.append(_maxValueHex, "");
		return _builder;
	}

	public String shifted(final Definition d, final Row row) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append(" ");
		_builder.append("(");
		final String _varName = this.getVarName(row, d);
		_builder.append(_varName, " ");
		_builder.append(" << ");
		final int _shiftVal = this.shiftVal(d);
		_builder.append(_shiftVal, " ");
		_builder.append(") |");
		return _builder.toString();
	}

	public List<Definition> allDefs(final Row row) {
		final List<Definition> res = new LinkedList<Definition>();
		for (final NamedElement ne : row.definitions) {
			final boolean _notEquals = (!Objects.equal(((Definition) ne).type, Definition.Type.UNUSED));
			if (_notEquals) {
				res.add(((Definition) ne));
			}
		}
		return res;
	}

	public List<Definition> writeDefs(final Row row) {
		final List<Definition> res = new LinkedList<Definition>();
		for (final NamedElement ne : row.definitions) {
			final boolean _hasWrite = this.hasWrite(ne);
			if (_hasWrite) {
				res.add(((Definition) ne));
			}
		}
		return res;
	}

	public String generateAddressReadSwitch(final Row row, final String prefix, final List<Row> rows) {
		int idx = 0;
		int rIdx = 0;
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("switch (index) {");
		_builder.newLine();
		String res = _builder.toString();
		for (final Row r : rows) {
			{
				final boolean _equals = r.name.equals(row.name);
				if (_equals) {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("case ");
					_builder_1.append(rIdx, "");
					_builder_1.append(": val=base[");
					_builder_1.append(idx, "");
					_builder_1.append("]; break;");
					_builder_1.newLineIfNotEmpty();
					final String _plus = (res + _builder_1);
					res = _plus;
					rIdx = (rIdx + 1);
				}
				idx = (idx + 1);
			}
		}
		final StringConcatenation _builder_1 = new StringConcatenation();
		_builder_1.append("default:");
		_builder_1.newLine();
		_builder_1.append("\t");
		final String _prefix = this.getPrefix(prefix);
		_builder_1.append(_prefix, "\t");
		_builder_1.append("warn(invalidIndex, index, \"\", \"");
		_builder_1.append(row.name, "\t");
		_builder_1.append("\", \"\"); ");
		_builder_1.newLineIfNotEmpty();
		_builder_1.append("\t");
		_builder_1.append("return 0;");
		_builder_1.newLine();
		_builder_1.append("}");
		_builder_1.newLine();
		final String _plus = (res + _builder_1);
		res = _plus;
		return res;
	}

	public String generateAddressSwitch(final Row row, final List<Row> rows) {
		int idx = 0;
		int rIdx = 0;
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("switch (index) {");
		_builder.newLine();
		String res = _builder.toString();
		for (final Row r : rows) {
			{
				final boolean _equals = r.name.equals(row.name);
				if (_equals) {
					final StringConcatenation _builder_1 = new StringConcatenation();
					_builder_1.append("case ");
					_builder_1.append(rIdx, "");
					_builder_1.append(": base[");
					_builder_1.append(idx, "");
					_builder_1.append("]=newVal; return 1;");
					_builder_1.newLineIfNotEmpty();
					final String _plus = (res + _builder_1);
					res = _plus;
					rIdx = (rIdx + 1);
				}
				idx = (idx + 1);
			}
		}
		final StringConcatenation _builder_1 = new StringConcatenation();
		_builder_1.append("}");
		_builder_1.newLine();
		final String _plus = (res + _builder_1);
		res = _plus;
		return res;
	}

	public CharSequence generateConditions(final Row row, final String prefix, final Definition d) {
		final StringConcatenation _builder = new StringConcatenation();
		{
			if ((d.width == 32)) {
			} else {
				final boolean _equals = Objects.equal(d.warn, Definition.WarnType.silentLimit);
				if (_equals) {
					_builder.append("if (");
					final String _varName = this.getVarName(row, d);
					_builder.append(_varName, "");
					_builder.append(" > ");
					final String _maxValueHex = this.getMaxValueHex(d);
					_builder.append(_maxValueHex, "");
					_builder.append(") {");
					_builder.newLineIfNotEmpty();
					_builder.append("\t");
					final String _varName_1 = this.getVarName(row, d);
					_builder.append(_varName_1, "\t");
					_builder.append("=");
					final String _maxValueHex_1 = this.getMaxValueHex(d);
					_builder.append(_maxValueHex_1, "\t");
					_builder.append(";");
					_builder.newLineIfNotEmpty();
					_builder.append("}");
					_builder.newLine();
					{
						final boolean _equals_1 = Objects.equal(d.type, Definition.Type.INT);
						if (_equals_1) {
							_builder.append("if (");
							final String _varName_2 = this.getVarName(row, d);
							_builder.append(_varName_2, "");
							_builder.append(" < ");
							final String _maxValueNegHex = this.getMaxValueNegHex(d);
							_builder.append(_maxValueNegHex, "");
							_builder.append(") {");
							_builder.newLineIfNotEmpty();
							_builder.append("\t");
							final String _varName_3 = this.getVarName(row, d);
							_builder.append(_varName_3, "\t");
							_builder.append("=");
							final String _maxValueNegHex_1 = this.getMaxValueNegHex(d);
							_builder.append(_maxValueNegHex_1, "\t");
							_builder.append(";");
							_builder.newLineIfNotEmpty();
							_builder.append("}");
							_builder.newLine();
						}
					}
				} else {
					final boolean _equals_2 = Objects.equal(d.warn, Definition.WarnType.limit);
					if (_equals_2) {
						_builder.append("if (");
						final String _varName_4 = this.getVarName(row, d);
						_builder.append(_varName_4, "");
						_builder.append(" > ");
						final String _maxValueHex_2 = this.getMaxValueHex(d);
						_builder.append(_maxValueHex_2, "");
						_builder.append(") {");
						_builder.newLineIfNotEmpty();
						_builder.append("\t");
						final String _prefix = this.getPrefix(prefix);
						_builder.append(_prefix, "\t");
						_builder.append("warn(limit, ");
						final String _varName_5 = this.getVarName(row, d);
						_builder.append(_varName_5, "\t");
						_builder.append(", \"");
						final String _varNameIndex = this.getVarNameIndex(row, d);
						_builder.append(_varNameIndex, "\t");
						_builder.append("\", \"");
						_builder.append(row.name, "\t");
						_builder.append("\", \"using ");
						final String _maxValueHex_3 = this.getMaxValueHex(d);
						_builder.append(_maxValueHex_3, "\t");
						_builder.append("\");");
						_builder.newLineIfNotEmpty();
						_builder.append("\t");
						final String _varName_6 = this.getVarName(row, d);
						_builder.append(_varName_6, "\t");
						_builder.append("=");
						final String _maxValueHex_4 = this.getMaxValueHex(d);
						_builder.append(_maxValueHex_4, "\t");
						_builder.append(";");
						_builder.newLineIfNotEmpty();
						_builder.append("}");
						_builder.newLine();
						{
							final boolean _equals_3 = Objects.equal(d.type, Definition.Type.INT);
							if (_equals_3) {
								_builder.append("if (");
								final String _varName_7 = this.getVarName(row, d);
								_builder.append(_varName_7, "");
								_builder.append(" < ");
								final String _maxValueNegHex_2 = this.getMaxValueNegHex(d);
								_builder.append(_maxValueNegHex_2, "");
								_builder.append(") {");
								_builder.newLineIfNotEmpty();
								_builder.append("\t");
								final String _prefix_1 = this.getPrefix(prefix);
								_builder.append(_prefix_1, "\t");
								_builder.append("warn(limit, ");
								final String _varName_8 = this.getVarName(row, d);
								_builder.append(_varName_8, "\t");
								_builder.append(", \"");
								final String _varNameIndex_1 = this.getVarNameIndex(row, d);
								_builder.append(_varNameIndex_1, "\t");
								_builder.append("\", \"");
								_builder.append(row.name, "\t");
								_builder.append("\", \"using ");
								final String _maxValueNegHex_3 = this.getMaxValueNegHex(d);
								_builder.append(_maxValueNegHex_3, "\t");
								_builder.append("\");");
								_builder.newLineIfNotEmpty();
								_builder.append("\t");
								final String _varName_9 = this.getVarName(row, d);
								_builder.append(_varName_9, "\t");
								_builder.append("=");
								final String _maxValueNegHex_4 = this.getMaxValueNegHex(d);
								_builder.append(_maxValueNegHex_4, "\t");
								_builder.append(";");
								_builder.newLineIfNotEmpty();
								_builder.append("}");
								_builder.newLine();
							}
						}
					} else {
						final boolean _equals_4 = Objects.equal(d.warn, Definition.WarnType.silentMask);
						if (_equals_4) {
							_builder.append("if (");
							final String _varName_10 = this.getVarName(row, d);
							_builder.append(_varName_10, "");
							_builder.append(" > ");
							final String _maxValueHex_5 = this.getMaxValueHex(d);
							_builder.append(_maxValueHex_5, "");
							_builder.append(") {");
							_builder.newLineIfNotEmpty();
							_builder.append("\t");
							final String _varName_11 = this.getVarName(row, d);
							_builder.append(_varName_11, "\t");
							_builder.append("&=");
							final String _maxValueHex_6 = this.getMaxValueHex(d);
							_builder.append(_maxValueHex_6, "\t");
							_builder.append(";");
							_builder.newLineIfNotEmpty();
							_builder.append("}");
							_builder.newLine();
							{
								final boolean _equals_5 = Objects.equal(d.type, Definition.Type.INT);
								if (_equals_5) {
									_builder.append("if (");
									final String _varName_12 = this.getVarName(row, d);
									_builder.append(_varName_12, "");
									_builder.append(" < ");
									final String _maxValueNegHex_5 = this.getMaxValueNegHex(d);
									_builder.append(_maxValueNegHex_5, "");
									_builder.append(") {");
									_builder.newLineIfNotEmpty();
									_builder.append("\t");
									final String _varName_13 = this.getVarName(row, d);
									_builder.append(_varName_13, "\t");
									_builder.append("&=");
									final String _maxValueNegHex_6 = this.getMaxValueNegHex(d);
									_builder.append(_maxValueNegHex_6, "\t");
									_builder.append(";");
									_builder.newLineIfNotEmpty();
									_builder.append("}");
									_builder.newLine();
								}
							}
						} else {
							final boolean _equals_6 = Objects.equal(d.warn, Definition.WarnType.mask);
							if (_equals_6) {
								_builder.append("if (");
								final String _varName_14 = this.getVarName(row, d);
								_builder.append(_varName_14, "");
								_builder.append(" > ");
								final String _maxValueHex_7 = this.getMaxValueHex(d);
								_builder.append(_maxValueHex_7, "");
								_builder.append(") {");
								_builder.newLineIfNotEmpty();
								_builder.append("\t");
								final String _prefix_2 = this.getPrefix(prefix);
								_builder.append(_prefix_2, "\t");
								_builder.append("warn(mask, ");
								final String _varName_15 = this.getVarName(row, d);
								_builder.append(_varName_15, "\t");
								_builder.append(", \"");
								final String _varNameIndex_2 = this.getVarNameIndex(row, d);
								_builder.append(_varNameIndex_2, "\t");
								_builder.append("\", \"");
								_builder.append(row.name, "\t");
								_builder.append("\", \"masking with ");
								final String _maxValueHex_8 = this.getMaxValueHex(d);
								_builder.append(_maxValueHex_8, "\t");
								_builder.append("\");");
								_builder.newLineIfNotEmpty();
								_builder.append("\t");
								final String _varName_16 = this.getVarName(row, d);
								_builder.append(_varName_16, "\t");
								_builder.append("&=");
								final String _maxValueHex_9 = this.getMaxValueHex(d);
								_builder.append(_maxValueHex_9, "\t");
								_builder.append(";");
								_builder.newLineIfNotEmpty();
								_builder.append("}");
								_builder.newLine();
								{
									final boolean _equals_7 = Objects.equal(d.type, Definition.Type.INT);
									if (_equals_7) {
										_builder.append("if (");
										final String _varName_17 = this.getVarName(row, d);
										_builder.append(_varName_17, "");
										_builder.append(" < ");
										final String _maxValueNegHex_7 = this.getMaxValueNegHex(d);
										_builder.append(_maxValueNegHex_7, "");
										_builder.append(") {");
										_builder.newLineIfNotEmpty();
										_builder.append("\t");
										final String _prefix_3 = this.getPrefix(prefix);
										_builder.append(_prefix_3, "\t");
										_builder.append("warn(mask, ");
										final String _varName_18 = this.getVarName(row, d);
										_builder.append(_varName_18, "\t");
										_builder.append(", \"");
										final String _varNameIndex_3 = this.getVarNameIndex(row, d);
										_builder.append(_varNameIndex_3, "\t");
										_builder.append("\", \"");
										_builder.append(row.name, "\t");
										_builder.append("\", \"masking with ");
										final String _maxValueNegHex_8 = this.getMaxValueNegHex(d);
										_builder.append(_maxValueNegHex_8, "\t");
										_builder.append("\");");
										_builder.newLineIfNotEmpty();
										_builder.append("\t");
										final String _varName_19 = this.getVarName(row, d);
										_builder.append(_varName_19, "\t");
										_builder.append("&=");
										final String _maxValueNegHex_9 = this.getMaxValueNegHex(d);
										_builder.append(_maxValueNegHex_9, "\t");
										_builder.append(";");
										_builder.newLineIfNotEmpty();
										_builder.append("}");
										_builder.newLine();
									}
								}
							} else {
								final boolean _equals_8 = Objects.equal(d.warn, Definition.WarnType.silentError);
								if (_equals_8) {
									_builder.append("if (");
									final String _varName_20 = this.getVarName(row, d);
									_builder.append(_varName_20, "");
									_builder.append(" > ");
									final String _maxValueHex_10 = this.getMaxValueHex(d);
									_builder.append(_maxValueHex_10, "");
									_builder.append(") {");
									_builder.newLineIfNotEmpty();
									_builder.append("\t");
									_builder.append("return 0;");
									_builder.newLine();
									_builder.append("}");
									_builder.newLine();
									{
										final boolean _equals_9 = Objects.equal(d.type, Definition.Type.INT);
										if (_equals_9) {
											_builder.append("if (");
											final String _varName_21 = this.getVarName(row, d);
											_builder.append(_varName_21, "");
											_builder.append(" < ");
											final String _maxValueNegHex_10 = this.getMaxValueNegHex(d);
											_builder.append(_maxValueNegHex_10, "");
											_builder.append(") {");
											_builder.newLineIfNotEmpty();
											_builder.append("\t");
											_builder.append("return 0;");
											_builder.newLine();
											_builder.append("}");
											_builder.newLine();
										}
									}
								} else {
									final boolean _equals_10 = Objects.equal(d.warn, Definition.WarnType.error);
									if (_equals_10) {
										_builder.append("if (");
										final String _varName_22 = this.getVarName(row, d);
										_builder.append(_varName_22, "");
										_builder.append(" > ");
										final String _maxValueHex_11 = this.getMaxValueHex(d);
										_builder.append(_maxValueHex_11, "");
										_builder.append(") {");
										_builder.newLineIfNotEmpty();
										_builder.append("\t");
										final String _prefix_4 = this.getPrefix(prefix);
										_builder.append(_prefix_4, "\t");
										_builder.append("warn(error, ");
										final String _varName_23 = this.getVarName(row, d);
										_builder.append(_varName_23, "\t");
										_builder.append(", \"");
										final String _varNameIndex_4 = this.getVarNameIndex(row, d);
										_builder.append(_varNameIndex_4, "\t");
										_builder.append("\", \"");
										_builder.append(row.name, "\t");
										_builder.append("\", \"returning with 0\");");
										_builder.newLineIfNotEmpty();
										_builder.append("\t");
										_builder.append("return 0;");
										_builder.newLine();
										_builder.append("}");
										_builder.newLine();
										{
											final boolean _equals_11 = Objects.equal(d.type, Definition.Type.INT);
											if (_equals_11) {
												_builder.append("if (");
												final String _varName_24 = this.getVarName(row, d);
												_builder.append(_varName_24, "");
												_builder.append(" < ");
												final String _maxValueNegHex_11 = this.getMaxValueNegHex(d);
												_builder.append(_maxValueNegHex_11, "");
												_builder.append(") {");
												_builder.newLineIfNotEmpty();
												_builder.append("\t");
												final String _prefix_5 = this.getPrefix(prefix);
												_builder.append(_prefix_5, "\t");
												_builder.append("warn(error, ");
												final String _varName_25 = this.getVarName(row, d);
												_builder.append(_varName_25, "\t");
												_builder.append(", \"");
												final String _varNameIndex_5 = this.getVarNameIndex(row, d);
												_builder.append(_varNameIndex_5, "\t");
												_builder.append("\", \"");
												_builder.append(row.name, "\t");
												_builder.append("\", \"returning with 0\");");
												_builder.newLineIfNotEmpty();
												_builder.append("\t");
												_builder.append("return 0;");
												_builder.newLine();
												_builder.append("}");
												_builder.newLine();
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return _builder;
	}

	public boolean hasWriteDefs(final Row row) {
		final Function1<NamedElement, Boolean> _function = new Function1<NamedElement, Boolean>() {
			@Override
			public Boolean apply(final NamedElement it) {
				return Boolean.valueOf(BusAccess.this.hasWrite(it));
			}
		};
		final NamedElement _findFirst = IterableExtensions.<NamedElement> findFirst(row.definitions, _function);
		return (_findFirst != null);
	}

	public boolean hasWrite(final NamedElement ne) {
		return ((((Definition) ne).rw != Definition.RWType.r) && (((Definition) ne).type != Definition.Type.UNUSED));
	}

	public String getMaxValueHex(final Definition d) {
		final long _maxValue = this.getMaxValue(d);
		final String _hexString = Long.toHexString(_maxValue);
		return ("0x" + _hexString);
	}

	public String getMaxValueNegHex(final Definition d) {
		final long _maxValue = this.getMaxValue(d);
		final long _plus = (_maxValue + 1);
		final String _hexString = Long.toHexString(_plus);
		return ("-0x" + _hexString);
	}

	public long getMaxValue(final Definition d) {
		final boolean _notEquals = (!Objects.equal(d.type, Definition.Type.INT));
		if (_notEquals) {
			final int _size = MemoryModel.getSize(d);
			final long _doubleLessThan = (1l << _size);
			return (_doubleLessThan - 1);
		} else {
			final int _size_1 = MemoryModel.getSize(d);
			final int _minus = (_size_1 - 1);
			final long _doubleLessThan_1 = (1l << _minus);
			return (_doubleLessThan_1 - 1);
		}
	}

	public String getParameter(final Row row, final Definition d, final boolean pointer) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append(", ");
		final CharSequence _busType = this.getBusType(d);
		_builder.append(_busType, "");
		_builder.append(" ");
		{
			if (pointer) {
				_builder.append("*");
			}
		}
		final String _varName = this.getVarName(row, d);
		_builder.append(_varName, "");
		return _builder.toString();
	}

	public String getVarName(final Row row, final Definition d) {
		final String _name = d.getName(row);
		final Integer dim = row.defCount.get(_name);
		if (((dim).intValue() == 1))
			return d.name;
		else
			return (d.name + d.arrayIndex);
	}

	public String getVarNameIndex(final Row row, final Definition d) {
		final String _name = d.getName(row);
		final Integer dim = row.defCount.get(_name);
		if (((dim).intValue() == 1))
			return d.name;
		else
			return (((d.name + "[") + d.arrayIndex) + "]");
	}

	public String getVarNameArray(final Row row, final Definition d) {
		final String _name = d.getName(row);
		final Integer dim = row.defCount.get(_name);
		if (((dim).intValue() == 1))
			return d.name;
		else
			return (((d.name + "[") + dim) + "]");
	}

	public CharSequence getBusType(final Definition d) {
		final StringConcatenation _builder = new StringConcatenation();
		_builder.append("bus_");
		final String _string = d.type.toString();
		final String _lowerCase = _string.toLowerCase();
		_builder.append(_lowerCase, "");
		final int _size = MemoryModel.getSize(d);
		_builder.append(_size, "");
		_builder.append("_t");
		return _builder;
	}
}
