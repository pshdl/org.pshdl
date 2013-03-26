package de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Column;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.RWType;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.Type;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Definition.WarnType;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.MemoryModel;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.NamedElement;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Row;
import de.tuhh.ict.pshdl.model.types.builtIn.busses.memorymodel.Unit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.StringExtensions;

@SuppressWarnings("all")
public class BusAccess {
  public CharSequence generateStdDef() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  BusStdDefinitions.h");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  Automatically generated on ");
    DateFormat _dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    Date _date = new Date();
    String _format = _dateTimeInstance.format(_date);
    _builder.append(_format, "");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append("//");
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
      IntegerRange _upTo = new IntegerRange(1, 32);
      for(final Integer I : _upTo) {
        _builder.append("typedef uint32_t bus_bit");
        _builder.append(I, "");
        _builder.append("_t;");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      IntegerRange _upTo_1 = new IntegerRange(1, 32);
      for(final Integer I_1 : _upTo_1) {
        _builder.append("typedef uint32_t bus_uint");
        _builder.append(I_1, "");
        _builder.append("_t;");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      IntegerRange _upTo_2 = new IntegerRange(1, 32);
      for(final Integer I_2 : _upTo_2) {
        _builder.append("typedef int32_t bus_int");
        _builder.append(I_2, "");
        _builder.append("_t;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("typedef enum {mask, limit, error, invalidIndex} warningType_t;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("typedef void (*warnFunc_p)(warningType_t t, int value, char *def, char *row, char *msg);");
    _builder.newLine();
    _builder.append("void setWarn(warnFunc_p warnFunction);");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#endif\t");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence generatePrintC(final Unit unit, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  BusPrint.c");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  Automatically generated on ");
    DateFormat _dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    Date _date = new Date();
    String _format = _dateTimeInstance.format(_date);
    _builder.append(_format, "");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append("//");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <stdio.h>");
    _builder.newLine();
    _builder.append("#include \"BusAccess.h\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void defaultPrintfWarn(warningType_t t, int value, char *def, char *row, char *msg) {");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("switch (t) {");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("case limit:");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("printf(\"Limited value %d for definition %s of row %s %s\\n\",value ,def,row,msg);");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("case mask:");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("printf(\"Masked value %d for definition %s of row %s %s\\n\",value ,def,row,msg);");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("case invalidIndex:");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("printf(\"The index %d is not valid for the column %s %s\\n\", value, row, msg);");
    _builder.newLine();
    _builder.append("        ");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("break;");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("}");
    _builder.newLine();
    _builder.append("    ");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    String _generatePrint = this.generatePrint(rows);
    _builder.append(_generatePrint, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public CharSequence generatePrintH(final Unit unit, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  BusPrint.h");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  Automatically generated on ");
    DateFormat _dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    Date _date = new Date();
    String _format = _dateTimeInstance.format(_date);
    _builder.append(_format, "");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append("//");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#ifndef BusPrint_h");
    _builder.newLine();
    _builder.append("#define BusPrint_h");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include \"BusAccess.h\"");
    _builder.newLine();
    _builder.append("void defaultPrintfWarn(warningType_t t, int value, char *def, char *row, char *msg);");
    _builder.newLine();
    _builder.newLine();
    String _generatePrintDef = this.generatePrintDef(rows);
    _builder.append(_generatePrintDef, "");
    _builder.newLineIfNotEmpty();
    _builder.append("#endif");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public String generatePrintDef(final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    HashSet<String> _hashSet = new HashSet<String>();
    final HashSet<String> checkedRows = _hashSet;
    for (final Row row : rows) {
      {
        boolean _contains = checkedRows.contains(row.name);
        boolean _not = (!_contains);
        if (_not) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("void print");
          String _firstUpper = StringExtensions.toFirstUpper(row.name);
          _builder_1.append(_firstUpper, "");
          _builder_1.append("(");
          _builder_1.append(row.name, "");
          _builder_1.append("_t *data);");
          _builder_1.newLineIfNotEmpty();
          String _plus = (res + _builder_1);
          res = _plus;
        }
        checkedRows.add(row.name);
      }
    }
    return res;
  }
  
  public String generatePrint(final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    HashSet<String> _hashSet = new HashSet<String>();
    final HashSet<String> checkedRows = _hashSet;
    for (final Row row : rows) {
      {
        boolean _contains = checkedRows.contains(row.name);
        boolean _not = (!_contains);
        if (_not) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("void print");
          String _firstUpper = StringExtensions.toFirstUpper(row.name);
          _builder_1.append(_firstUpper, "");
          _builder_1.append("(");
          _builder_1.append(row.name, "");
          _builder_1.append("_t *data){");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("    ");
          _builder_1.append("printf(\"");
          String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
          _builder_1.append(_firstUpper_1, "    ");
          _builder_1.append(" ");
          {
            List<Definition> _allDefs = this.allDefs(row);
            for(final Definition d : _allDefs) {
              _builder_1.append(" ");
              _builder_1.append(d.name, "    ");
              _builder_1.append(": 0x%0");
              int _size = MemoryModel.getSize(d);
              float _divide = (_size / 4f);
              double _ceil = Math.ceil(_divide);
              int _intValue = Double.valueOf(_ceil).intValue();
              _builder_1.append(_intValue, "    ");
              _builder_1.append("x");
            }
          }
          _builder_1.append("\\n\"");
          {
            List<Definition> _allDefs_1 = this.allDefs(row);
            for(final Definition d_1 : _allDefs_1) {
              _builder_1.append(", data->");
              String _varNameIndex = this.getVarNameIndex(row, d_1);
              _builder_1.append(_varNameIndex, "    ");
            }
          }
          _builder_1.append(");");
          _builder_1.newLineIfNotEmpty();
          _builder_1.append("}");
          _builder_1.newLine();
          String _plus = (res + _builder_1);
          res = _plus;
        }
        checkedRows.add(row.name);
      }
    }
    return res;
  }
  
  public CharSequence generateAccessH(final Unit unit, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  BusDefinitions.h");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  Automatically generated on ");
    DateFormat _dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    Date _date = new Date();
    String _format = _dateTimeInstance.format(_date);
    _builder.append(_format, "");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append("//");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#ifndef BusDefinitions_h");
    _builder.newLine();
    _builder.append("#define BusDefinitions_h");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include \"BusStdDefinitions.h\"");
    _builder.newLine();
    _builder.newLine();
    String _generateDeclarations = this.generateDeclarations(unit, rows);
    _builder.append(_generateDeclarations, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("#endif");
    _builder.newLine();
    return _builder;
  }
  
  public String generateDeclarations(final Unit unit, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    HashSet<String> _hashSet = new HashSet<String>();
    final HashSet<String> checkedRows = _hashSet;
    for (final Row row : rows) {
      {
        HashSet<String> _hashSet_1 = new HashSet<String>();
        final HashSet<String> checkedDefs = _hashSet_1;
        boolean _contains = checkedRows.contains(row.name);
        boolean _not = (!_contains);
        if (_not) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("//Typedef");
          _builder_1.newLine();
          _builder_1.append("typedef struct ");
          _builder_1.append(row.name, "");
          _builder_1.append(" {");
          _builder_1.newLineIfNotEmpty();
          {
            List<Definition> _allDefs = this.allDefs(row);
            for(final Definition d : _allDefs) {
              {
                boolean _contains_1 = checkedDefs.contains(d.name);
                boolean _not_1 = (!_contains_1);
                if (_not_1) {
                  {
                    boolean _add = checkedDefs.add(d.name);
                    if (_add) {
                    }
                  }
                  _builder_1.append("\t");
                  CharSequence _busType = this.getBusType(d);
                  _builder_1.append(_busType, "	");
                  _builder_1.append("\t");
                  String _varNameArray = this.getVarNameArray(row, d);
                  _builder_1.append(_varNameArray, "	");
                  _builder_1.append(";");
                  _builder_1.newLineIfNotEmpty();
                }
              }
            }
          }
          _builder_1.append("} ");
          _builder_1.append(row.name, "");
          _builder_1.append("_t;");
          _builder_1.newLineIfNotEmpty();
          String _plus = (res + _builder_1);
          res = _plus;
          boolean _hasWriteDefs = this.hasWriteDefs(row);
          if (_hasWriteDefs) {
            StringConcatenation _builder_2 = new StringConcatenation();
            _builder_2.append("// Setter");
            _builder_2.newLine();
            _builder_2.append("int set");
            String _firstUpper = StringExtensions.toFirstUpper(row.name);
            _builder_2.append(_firstUpper, "");
            _builder_2.append("Direct(uint32_t *base, int index");
            {
              List<Definition> _writeDefs = this.writeDefs(row);
              for(final Definition definition : _writeDefs) {
                String _parameter = this.getParameter(row, definition, false);
                _builder_2.append(_parameter, "");
              }
            }
            _builder_2.append(");");
            _builder_2.newLineIfNotEmpty();
            _builder_2.append("int set");
            String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
            _builder_2.append(_firstUpper_1, "");
            _builder_2.append("(uint32_t *base, int index, ");
            _builder_2.append(row.name, "");
            _builder_2.append("_t *newVal);");
            _builder_2.newLineIfNotEmpty();
            String _plus_1 = (res + _builder_2);
            res = _plus_1;
          }
          StringConcatenation _builder_3 = new StringConcatenation();
          _builder_3.append("//Getter");
          _builder_3.newLine();
          _builder_3.append("int get");
          String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
          _builder_3.append(_firstUpper_2, "");
          _builder_3.append("Direct(uint32_t *base, int index");
          {
            List<Definition> _allDefs_1 = this.allDefs(row);
            for(final Definition definition_1 : _allDefs_1) {
              String _parameter_1 = this.getParameter(row, definition_1, true);
              _builder_3.append(_parameter_1, "");
            }
          }
          _builder_3.append(");");
          _builder_3.newLineIfNotEmpty();
          _builder_3.append("int get");
          String _firstUpper_3 = StringExtensions.toFirstUpper(row.name);
          _builder_3.append(_firstUpper_3, "");
          _builder_3.append("(uint32_t *base, int index, ");
          _builder_3.append(row.name, "");
          _builder_3.append("_t *result);");
          _builder_3.newLineIfNotEmpty();
          String _plus_2 = (res + _builder_3);
          res = _plus_2;
          checkedRows.add(row.name);
        }
      }
    }
    Collection<NamedElement> _values = unit.declarations.values();
    for (final NamedElement ne : _values) {
      String _name = ne.getName();
      boolean _contains = checkedRows.contains(_name);
      boolean _not = (!_contains);
      if (_not) {
        if ((ne instanceof Column)) {
          final Column col = ((Column) ne);
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("typedef struct ");
          _builder_1.append(col.name, "");
          _builder_1.append(" {");
          _builder_1.newLineIfNotEmpty();
          {
            for(final NamedElement neRow : col.rows) {
              _builder_1.append("\t");
              String _name_1 = neRow.getName();
              _builder_1.append(_name_1, "	");
              _builder_1.append("_t ");
              String _name_2 = neRow.getName();
              _builder_1.append(_name_2, "	");
              _builder_1.append(";");
              _builder_1.newLineIfNotEmpty();
            }
          }
          _builder_1.append("} ");
          _builder_1.append(col.name, "");
          _builder_1.append("_t;");
          _builder_1.newLineIfNotEmpty();
          String _plus = (res + _builder_1);
          res = _plus;
        }
      }
    }
    return res;
  }
  
  public CharSequence generateAccessC(final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  BusAcces.c");
    _builder.newLine();
    _builder.append("//");
    _builder.newLine();
    _builder.append("//  Automatically generated on ");
    DateFormat _dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
    Date _date = new Date();
    String _format = _dateTimeInstance.format(_date);
    _builder.append(_format, "");
    _builder.append(".");
    _builder.newLineIfNotEmpty();
    _builder.append("//");
    _builder.newLine();
    _builder.newLine();
    _builder.append("#include <stdint.h>");
    _builder.newLine();
    _builder.append("#include \"BusAccess.h\"");
    _builder.newLine();
    _builder.append("#include \"BusStdDefinitions.h\"");
    _builder.newLine();
    _builder.newLine();
    _builder.append("static void defaultWarn(warningType_t t, int value, char *def, char *row, char *msg){");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("warnFunc_p warn=defaultWarn;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("void setWarn(warnFunc_p warnFunction){");
    _builder.newLine();
    _builder.append("    ");
    _builder.append("warn=warnFunction;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//Setter functions");
    _builder.newLine();
    String _generateSetterFunctions = this.generateSetterFunctions(rows);
    _builder.append(_generateSetterFunctions, "");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("//Getter functions");
    _builder.newLine();
    String _generateGetterFunctions = this.generateGetterFunctions(rows);
    _builder.append(_generateGetterFunctions, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public String generateGetterFunctions(final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    HashSet<String> _hashSet = new HashSet<String>();
    final HashSet<String> doneRows = _hashSet;
    for (final Row row : rows) {
      {
        final boolean handled = doneRows.contains(row.name);
        boolean _not = (!handled);
        if (_not) {
          CharSequence _generateGetterFunction = this.generateGetterFunction(row, rows);
          String _plus = (res + _generateGetterFunction);
          res = _plus;
        }
        doneRows.add(row.name);
      }
    }
    return res;
  }
  
  public CharSequence generateGetterFunction(final Row row, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int get");
    String _firstUpper = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper, "");
    _builder.append("Direct(uint32_t *base, int index");
    {
      List<Definition> _allDefs = this.allDefs(row);
      for(final Definition definition : _allDefs) {
        String _parameter = this.getParameter(row, definition, true);
        _builder.append(_parameter, "");
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("uint32_t val=0;");
    _builder.newLine();
    _builder.append("\t");
    String _generateAddressReadSwitch = this.generateAddressReadSwitch(row, rows);
    _builder.append(_generateAddressReadSwitch, "	");
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _allDefs_1 = this.allDefs(row);
      for(final Definition d : _allDefs_1) {
        _builder.append("\t");
        _builder.append("*");
        String _varName = this.getVarName(row, d);
        _builder.append(_varName, "	");
        _builder.append("=(val >> ");
        int _shiftVal = this.shiftVal(d);
        _builder.append(_shiftVal, "	");
        _builder.append(") & ");
        String _maxValueHex = this.getMaxValueHex(d);
        _builder.append(_maxValueHex, "	");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return 1;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("int get");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_1, "");
    _builder.append("(uint32_t *base, int index, ");
    _builder.append(row.name, "");
    _builder.append("_t *result){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return get");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_2, "	");
    _builder.append("Direct(base, index");
    {
      List<Definition> _allDefs_2 = this.allDefs(row);
      for(final Definition d_1 : _allDefs_2) {
        _builder.append(", &result->");
        String _varNameIndex = this.getVarNameIndex(row, d_1);
        _builder.append(_varNameIndex, "	");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public int shiftVal(final Definition d) {
    final int size = MemoryModel.getSize(d);
    int _minus = (size - 1);
    return (d.bitPos - _minus);
  }
  
  public String generateSetterFunctions(final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    String res = _builder.toString();
    HashSet<String> _hashSet = new HashSet<String>();
    final HashSet<String> doneRows = _hashSet;
    for (final Row row : rows) {
      {
        final boolean handled = doneRows.contains(row.name);
        boolean _and = false;
        boolean _not = (!handled);
        if (!_not) {
          _and = false;
        } else {
          boolean _hasWriteDefs = this.hasWriteDefs(row);
          _and = (_not && _hasWriteDefs);
        }
        if (_and) {
          CharSequence _generateSetterFunction = this.generateSetterFunction(row, rows);
          String _plus = (res + _generateSetterFunction);
          res = _plus;
        }
        doneRows.add(row.name);
      }
    }
    return res;
  }
  
  public CharSequence generateSetterFunction(final Row row, final List<Row> rows) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("int set");
    String _firstUpper = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper, "");
    _builder.append("Direct(uint32_t *base, int index");
    {
      List<Definition> _writeDefs = this.writeDefs(row);
      for(final Definition d : _writeDefs) {
        String _parameter = this.getParameter(row, d, false);
        _builder.append(_parameter, "");
      }
    }
    _builder.append("){");
    _builder.newLineIfNotEmpty();
    {
      List<Definition> _writeDefs_1 = this.writeDefs(row);
      for(final Definition ne : _writeDefs_1) {
        _builder.append("\t");
        CharSequence _generateConditions = this.generateConditions(row, ne);
        _builder.append(_generateConditions, "	");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("uint32_t newVal=");
    {
      List<Definition> _writeDefs_2 = this.writeDefs(row);
      for(final Definition d_1 : _writeDefs_2) {
        String _shifted = this.shifted(d_1, row);
        _builder.append(_shifted, "	");
      }
    }
    _builder.append(" 0;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _generateAddressSwitch = this.generateAddressSwitch(row, rows);
    _builder.append(_generateAddressSwitch, "	");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("warn(invalidIndex, index, \"\", \"");
    _builder.append(row.name, "	");
    _builder.append("\", \"\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return 0;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("int set");
    String _firstUpper_1 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_1, "");
    _builder.append("(uint32_t *base, int index, ");
    _builder.append(row.name, "");
    _builder.append("_t *newVal) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return set");
    String _firstUpper_2 = StringExtensions.toFirstUpper(row.name);
    _builder.append(_firstUpper_2, "	");
    _builder.append("Direct(base, index");
    {
      List<Definition> _writeDefs_3 = this.writeDefs(row);
      for(final Definition d_2 : _writeDefs_3) {
        _builder.append(", newVal->");
        String _varNameIndex = this.getVarNameIndex(row, d_2);
        _builder.append(_varNameIndex, "	");
      }
    }
    _builder.append(");");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public String shifted(final Definition d, final Row row) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(" ");
    _builder.append("(");
    String _varName = this.getVarName(row, d);
    _builder.append(_varName, " ");
    _builder.append(" << ");
    int _shiftVal = this.shiftVal(d);
    _builder.append(_shiftVal, " ");
    _builder.append(") |");
    return _builder.toString();
  }
  
  public List<Definition> allDefs(final Row row) {
    LinkedList<Definition> _linkedList = new LinkedList<Definition>();
    final List<Definition> res = _linkedList;
    for (final NamedElement ne : row.definitions) {
      boolean _notEquals = (!Objects.equal(((Definition) ne).type, Type.UNUSED));
      if (_notEquals) {
        res.add(((Definition) ne));
      }
    }
    return res;
  }
  
  public List<Definition> writeDefs(final Row row) {
    LinkedList<Definition> _linkedList = new LinkedList<Definition>();
    final List<Definition> res = _linkedList;
    for (final NamedElement ne : row.definitions) {
      boolean _hasWrite = this.hasWrite(ne);
      if (_hasWrite) {
        res.add(((Definition) ne));
      }
    }
    return res;
  }
  
  public String generateAddressReadSwitch(final Row row, final List<Row> rows) {
    int idx = 0;
    int rIdx = 0;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("switch (index) {");
    _builder.newLine();
    String res = _builder.toString();
    for (final Row r : rows) {
      {
        boolean _equals = r.name.equals(row.name);
        if (_equals) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("case ");
          _builder_1.append(rIdx, "");
          _builder_1.append(": val=base[");
          _builder_1.append(idx, "");
          _builder_1.append("]; break;");
          _builder_1.newLineIfNotEmpty();
          String _plus = (res + _builder_1);
          res = _plus;
          int _plus_1 = (rIdx + 1);
          rIdx = _plus_1;
        }
        int _plus_2 = (idx + 1);
        idx = _plus_2;
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("default:");
    _builder_1.newLine();
    _builder_1.append("\t");
    _builder_1.append("warn(invalidIndex, index, \"\", \"");
    _builder_1.append(row.name, "	");
    _builder_1.append("\", \"\"); ");
    _builder_1.newLineIfNotEmpty();
    _builder_1.append("\t");
    _builder_1.append("return 0;");
    _builder_1.newLine();
    _builder_1.append("}");
    _builder_1.newLine();
    String _plus = (res + _builder_1);
    res = _plus;
    return res;
  }
  
  public String generateAddressSwitch(final Row row, final List<Row> rows) {
    int idx = 0;
    int rIdx = 0;
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("switch (index) {");
    _builder.newLine();
    String res = _builder.toString();
    for (final Row r : rows) {
      {
        boolean _equals = r.name.equals(row.name);
        if (_equals) {
          StringConcatenation _builder_1 = new StringConcatenation();
          _builder_1.append("case ");
          _builder_1.append(rIdx, "");
          _builder_1.append(": base[");
          _builder_1.append(idx, "");
          _builder_1.append("]=newVal; return 1;");
          _builder_1.newLineIfNotEmpty();
          String _plus = (res + _builder_1);
          res = _plus;
          int _plus_1 = (rIdx + 1);
          rIdx = _plus_1;
        }
        int _plus_2 = (idx + 1);
        idx = _plus_2;
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    _builder_1.append("}");
    _builder_1.newLine();
    String _plus = (res + _builder_1);
    res = _plus;
    return res;
  }
  
  public CharSequence generateConditions(final Row row, final Definition d) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _equals = Objects.equal(d.warn, WarnType.silentLimit);
      if (_equals) {
        _builder.append("if (");
        String _varName = this.getVarName(row, d);
        _builder.append(_varName, "");
        _builder.append(" > ");
        String _maxValueHex = this.getMaxValueHex(d);
        _builder.append(_maxValueHex, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        String _varName_1 = this.getVarName(row, d);
        _builder.append(_varName_1, "	");
        _builder.append("=");
        String _maxValueHex_1 = this.getMaxValueHex(d);
        _builder.append(_maxValueHex_1, "	");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
        {
          boolean _equals_1 = Objects.equal(d.type, Type.INT);
          if (_equals_1) {
            _builder.append("if (");
            String _varName_2 = this.getVarName(row, d);
            _builder.append(_varName_2, "");
            _builder.append(" < ");
            String _maxValueNegHex = this.getMaxValueNegHex(d);
            _builder.append(_maxValueNegHex, "");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _varName_3 = this.getVarName(row, d);
            _builder.append(_varName_3, "	");
            _builder.append("=");
            String _maxValueNegHex_1 = this.getMaxValueNegHex(d);
            _builder.append(_maxValueNegHex_1, "	");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      } else {
        boolean _equals_2 = Objects.equal(d.warn, WarnType.limit);
        if (_equals_2) {
          _builder.append("if (");
          String _varName_4 = this.getVarName(row, d);
          _builder.append(_varName_4, "");
          _builder.append(" > ");
          String _maxValueHex_2 = this.getMaxValueHex(d);
          _builder.append(_maxValueHex_2, "");
          _builder.append(") {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("warn(limit, ");
          String _varName_5 = this.getVarName(row, d);
          _builder.append(_varName_5, "	");
          _builder.append(", \"");
          String _varNameIndex = this.getVarNameIndex(row, d);
          _builder.append(_varNameIndex, "	");
          _builder.append("\", \"");
          _builder.append(row.name, "	");
          _builder.append("\", \"using ");
          String _maxValueHex_3 = this.getMaxValueHex(d);
          _builder.append(_maxValueHex_3, "	");
          _builder.append("\");");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          String _varName_6 = this.getVarName(row, d);
          _builder.append(_varName_6, "	");
          _builder.append("=");
          String _maxValueHex_4 = this.getMaxValueHex(d);
          _builder.append(_maxValueHex_4, "	");
          _builder.append(";");
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
          {
            boolean _equals_3 = Objects.equal(d.type, Type.INT);
            if (_equals_3) {
              _builder.append("if (");
              String _varName_7 = this.getVarName(row, d);
              _builder.append(_varName_7, "");
              _builder.append(" < ");
              String _maxValueNegHex_2 = this.getMaxValueNegHex(d);
              _builder.append(_maxValueNegHex_2, "");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("warn(limit, ");
              String _varName_8 = this.getVarName(row, d);
              _builder.append(_varName_8, "	");
              _builder.append(", \"");
              String _varNameIndex_1 = this.getVarNameIndex(row, d);
              _builder.append(_varNameIndex_1, "	");
              _builder.append("\", \"");
              _builder.append(row.name, "	");
              _builder.append("\", \"using ");
              String _maxValueNegHex_3 = this.getMaxValueNegHex(d);
              _builder.append(_maxValueNegHex_3, "	");
              _builder.append("\");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              String _varName_9 = this.getVarName(row, d);
              _builder.append(_varName_9, "	");
              _builder.append("=");
              String _maxValueNegHex_4 = this.getMaxValueNegHex(d);
              _builder.append(_maxValueNegHex_4, "	");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
            }
          }
        } else {
          boolean _equals_4 = Objects.equal(d.warn, WarnType.silentMask);
          if (_equals_4) {
            _builder.append("if (");
            String _varName_10 = this.getVarName(row, d);
            _builder.append(_varName_10, "");
            _builder.append(" > ");
            String _maxValueHex_5 = this.getMaxValueHex(d);
            _builder.append(_maxValueHex_5, "");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            String _varName_11 = this.getVarName(row, d);
            _builder.append(_varName_11, "	");
            _builder.append("&=");
            String _maxValueHex_6 = this.getMaxValueHex(d);
            _builder.append(_maxValueHex_6, "	");
            _builder.append(";");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
            {
              boolean _equals_5 = Objects.equal(d.type, Type.INT);
              if (_equals_5) {
                _builder.append("if (");
                String _varName_12 = this.getVarName(row, d);
                _builder.append(_varName_12, "");
                _builder.append(" < ");
                String _maxValueNegHex_5 = this.getMaxValueNegHex(d);
                _builder.append(_maxValueNegHex_5, "");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                String _varName_13 = this.getVarName(row, d);
                _builder.append(_varName_13, "	");
                _builder.append("&=");
                String _maxValueNegHex_6 = this.getMaxValueNegHex(d);
                _builder.append(_maxValueNegHex_6, "	");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
                _builder.append("}");
                _builder.newLine();
              }
            }
          } else {
            boolean _equals_6 = Objects.equal(d.warn, WarnType.mask);
            if (_equals_6) {
              _builder.append("if (");
              String _varName_14 = this.getVarName(row, d);
              _builder.append(_varName_14, "");
              _builder.append(" > ");
              String _maxValueHex_7 = this.getMaxValueHex(d);
              _builder.append(_maxValueHex_7, "");
              _builder.append(") {");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              _builder.append("warn(mask, ");
              String _varName_15 = this.getVarName(row, d);
              _builder.append(_varName_15, "	");
              _builder.append(", \"");
              String _varNameIndex_2 = this.getVarNameIndex(row, d);
              _builder.append(_varNameIndex_2, "	");
              _builder.append("\", \"");
              _builder.append(row.name, "	");
              _builder.append("\", \"masking with ");
              String _maxValueHex_8 = this.getMaxValueHex(d);
              _builder.append(_maxValueHex_8, "	");
              _builder.append("\");");
              _builder.newLineIfNotEmpty();
              _builder.append("\t");
              String _varName_16 = this.getVarName(row, d);
              _builder.append(_varName_16, "	");
              _builder.append("&=");
              String _maxValueHex_9 = this.getMaxValueHex(d);
              _builder.append(_maxValueHex_9, "	");
              _builder.append(";");
              _builder.newLineIfNotEmpty();
              _builder.append("}");
              _builder.newLine();
              {
                boolean _equals_7 = Objects.equal(d.type, Type.INT);
                if (_equals_7) {
                  _builder.append("if (");
                  String _varName_17 = this.getVarName(row, d);
                  _builder.append(_varName_17, "");
                  _builder.append(" < ");
                  String _maxValueNegHex_7 = this.getMaxValueNegHex(d);
                  _builder.append(_maxValueNegHex_7, "");
                  _builder.append(") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("warn(mask, ");
                  String _varName_18 = this.getVarName(row, d);
                  _builder.append(_varName_18, "	");
                  _builder.append(", \"");
                  String _varNameIndex_3 = this.getVarNameIndex(row, d);
                  _builder.append(_varNameIndex_3, "	");
                  _builder.append("\", \"");
                  _builder.append(row.name, "	");
                  _builder.append("\", \"masking with ");
                  String _maxValueNegHex_8 = this.getMaxValueNegHex(d);
                  _builder.append(_maxValueNegHex_8, "	");
                  _builder.append("\");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  String _varName_19 = this.getVarName(row, d);
                  _builder.append(_varName_19, "	");
                  _builder.append("&=");
                  String _maxValueNegHex_9 = this.getMaxValueNegHex(d);
                  _builder.append(_maxValueNegHex_9, "	");
                  _builder.append(";");
                  _builder.newLineIfNotEmpty();
                  _builder.append("}");
                  _builder.newLine();
                }
              }
            } else {
              boolean _equals_8 = Objects.equal(d.warn, WarnType.silentError);
              if (_equals_8) {
                _builder.append("if (");
                String _varName_20 = this.getVarName(row, d);
                _builder.append(_varName_20, "");
                _builder.append(" > ");
                String _maxValueHex_10 = this.getMaxValueHex(d);
                _builder.append(_maxValueHex_10, "");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("return 0;");
                _builder.newLine();
                _builder.append("}");
                _builder.newLine();
                {
                  boolean _equals_9 = Objects.equal(d.type, Type.INT);
                  if (_equals_9) {
                    _builder.append("if (");
                    String _varName_21 = this.getVarName(row, d);
                    _builder.append(_varName_21, "");
                    _builder.append(" < ");
                    String _maxValueNegHex_10 = this.getMaxValueNegHex(d);
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
                boolean _equals_10 = Objects.equal(d.warn, WarnType.error);
                if (_equals_10) {
                  _builder.append("if (");
                  String _varName_22 = this.getVarName(row, d);
                  _builder.append(_varName_22, "");
                  _builder.append(" > ");
                  String _maxValueHex_11 = this.getMaxValueHex(d);
                  _builder.append(_maxValueHex_11, "");
                  _builder.append(") {");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("warn(error, ");
                  String _varName_23 = this.getVarName(row, d);
                  _builder.append(_varName_23, "	");
                  _builder.append(", \"");
                  String _varNameIndex_4 = this.getVarNameIndex(row, d);
                  _builder.append(_varNameIndex_4, "	");
                  _builder.append("\", \"");
                  _builder.append(row.name, "	");
                  _builder.append("\", \"returning with 0\");");
                  _builder.newLineIfNotEmpty();
                  _builder.append("\t");
                  _builder.append("return 0;");
                  _builder.newLine();
                  _builder.append("}");
                  _builder.newLine();
                  {
                    boolean _equals_11 = Objects.equal(d.type, Type.INT);
                    if (_equals_11) {
                      _builder.append("if (");
                      String _varName_24 = this.getVarName(row, d);
                      _builder.append(_varName_24, "");
                      _builder.append(" < ");
                      String _maxValueNegHex_11 = this.getMaxValueNegHex(d);
                      _builder.append(_maxValueNegHex_11, "");
                      _builder.append(") {");
                      _builder.newLineIfNotEmpty();
                      _builder.append("\t");
                      _builder.append("warn(error, ");
                      String _varName_25 = this.getVarName(row, d);
                      _builder.append(_varName_25, "	");
                      _builder.append(", \"");
                      String _varNameIndex_5 = this.getVarNameIndex(row, d);
                      _builder.append(_varNameIndex_5, "	");
                      _builder.append("\", \"");
                      _builder.append(row.name, "	");
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
    return _builder;
  }
  
  public boolean hasWriteDefs(final Row row) {
    for (final NamedElement ne : row.definitions) {
      boolean _hasWrite = this.hasWrite(ne);
      if (_hasWrite) {
        return true;
      }
    }
    return false;
  }
  
  public boolean hasWrite(final NamedElement ne) {
    boolean _and = false;
    boolean _notEquals = (!Objects.equal(((Definition) ne).rw, RWType.r));
    if (!_notEquals) {
      _and = false;
    } else {
      boolean _notEquals_1 = (!Objects.equal(((Definition) ne).type, Type.UNUSED));
      _and = (_notEquals && _notEquals_1);
    }
    return _and;
  }
  
  public String getMaxValueHex(final Definition d) {
    int _maxValue = this.getMaxValue(d);
    String _hexString = Integer.toHexString(_maxValue);
    String _plus = ("0x" + _hexString);
    return _plus;
  }
  
  public String getMaxValueNegHex(final Definition d) {
    int _maxValue = this.getMaxValue(d);
    int _minus = (-_maxValue);
    int _minus_1 = (_minus - 1);
    String _hexString = Integer.toHexString(_minus_1);
    String _plus = ("0x" + _hexString);
    return _plus;
  }
  
  public int getMaxValue(final Definition d) {
    boolean _notEquals = (!Objects.equal(d.type, Type.INT));
    if (_notEquals) {
      int _size = MemoryModel.getSize(d);
      int _doubleLessThan = (1 << _size);
      return (_doubleLessThan - 1);
    } else {
      int _size_1 = MemoryModel.getSize(d);
      int _minus = (_size_1 - 1);
      int _doubleLessThan_1 = (1 << _minus);
      return (_doubleLessThan_1 - 1);
    }
  }
  
  public String getParameter(final Row row, final Definition d, final boolean pointer) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(", ");
    CharSequence _busType = this.getBusType(d);
    _builder.append(_busType, "");
    _builder.append(" ");
    {
      if (pointer) {
        _builder.append("*");
      }
    }
    String _varName = this.getVarName(row, d);
    _builder.append(_varName, "");
    return _builder.toString();
  }
  
  public String getVarName(final Row row, final Definition d) {
    final Integer dim = row.defCount.get(d.name);
    boolean _equals = ((dim).intValue() == 1);
    if (_equals) {
      return d.name;
    } else {
      return (d.name + d.arrayIndex);
    }
  }
  
  public String getVarNameIndex(final Row row, final Definition d) {
    final Integer dim = row.defCount.get(d.name);
    boolean _equals = ((dim).intValue() == 1);
    if (_equals) {
      return d.name;
    } else {
      String _plus = (d.name + "[");
      String _plus_1 = (_plus + d.arrayIndex);
      return (_plus_1 + "]");
    }
  }
  
  public String getVarNameArray(final Row row, final Definition d) {
    final Integer dim = row.defCount.get(d.name);
    boolean _equals = ((dim).intValue() == 1);
    if (_equals) {
      return d.name;
    } else {
      String _plus = (d.name + "[");
      String _plus_1 = (_plus + dim);
      return (_plus_1 + "]");
    }
  }
  
  public CharSequence getBusType(final Definition d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("bus_");
    String _string = d.type.toString();
    String _lowerCase = _string.toLowerCase();
    _builder.append(_lowerCase, "");
    int _size = MemoryModel.getSize(d);
    _builder.append(_size, "");
    _builder.append("_t");
    return _builder;
  }
}
