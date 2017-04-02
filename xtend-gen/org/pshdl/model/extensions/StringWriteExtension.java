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
package org.pshdl.model.extensions;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExport;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLOpExpression;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLTernary;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLUnresolvedFragmentFunction;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.parser.SourceInfo;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.SyntaxHighlighter;

@SuppressWarnings("all")
public class StringWriteExtension {
  private static StringWriteExtension INST = new StringWriteExtension();
  
  protected String _toString(final IHDLObject exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    throw new RuntimeException(_plus);
  }
  
  protected String _toString(final HDLExpression exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    throw new RuntimeException(_plus);
  }
  
  protected String _toString(final HDLStatement exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    throw new RuntimeException(_plus);
  }
  
  public static String asString(final IHDLObject exp, final SyntaxHighlighter highlight) {
    if ((exp == null)) {
      throw new IllegalArgumentException("Can not handle null argument");
    }
    return StringWriteExtension.INST.toString(exp, highlight);
  }
  
  protected String _toString(final HDLArrayInit array, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(array, highlight);
    _builder.append(_entering);
    _builder.append("{");
    {
      ArrayList<HDLExpression> _exp = array.getExp();
      boolean _hasElements = false;
      for(final HDLExpression e : _exp) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(e, highlight);
        _builder.append(_string);
      }
    }
    _builder.append("}");
    String _leaving = this.leaving(array, highlight);
    _builder.append(_leaving);
    return _builder.toString();
  }
  
  public String leaving(final IHDLObject init, final SyntaxHighlighter highlighter) {
    return highlighter.leaving(init);
  }
  
  public String entering(final IHDLObject init, final SyntaxHighlighter highlighter) {
    final List<String> comments = init.<List<String>>getMeta(SourceInfo.COMMENT);
    final StringBuilder sb = new StringBuilder();
    if ((comments != null)) {
      for (final String comment : comments) {
        sb.append(highlighter.comment(comment)).append(highlighter.newLine());
      }
    }
    String _entering = highlighter.entering(init);
    return (sb + _entering);
  }
  
  protected String _toString(final HDLAnnotation anno, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(anno, highlight));
    sb.append(highlight.annotation(anno.getName()));
    String _value = anno.getValue();
    boolean _tripleNotEquals = (_value != null);
    if (_tripleNotEquals) {
      String _value_1 = anno.getValue();
      String _plus = ("\"" + _value_1);
      String _plus_1 = (_plus + "\"");
      sb.append("(").append(highlight.string(_plus_1)).append(")");
    }
    sb.append(this.leaving(anno, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLTernary tern, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(tern, highlight);
    _builder.append(_entering);
    _builder.append("(");
    String _string = this.toString(tern.getIfExpr(), highlight);
    _builder.append(_string);
    String _operator = highlight.operator("?");
    _builder.append(_operator);
    String _string_1 = this.toString(tern.getThenExpr(), highlight);
    _builder.append(_string_1);
    String _operator_1 = highlight.operator(":");
    _builder.append(_operator_1);
    String _string_2 = this.toString(tern.getElseExpr(), highlight);
    _builder.append(_string_2);
    _builder.append(")");
    String _leaving = this.leaving(tern, highlight);
    _builder.append(_leaving);
    return _builder.toString();
  }
  
  protected String _toString(final HDLOpExpression op, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(op, highlight);
    _builder.append(_entering);
    _builder.append("(");
    String _string = this.toString(op.getLeft(), highlight);
    _builder.append(_string);
    String _operator = highlight.operator(op.getType().toString());
    _builder.append(_operator);
    String _string_1 = this.toString(op.getRight(), highlight);
    _builder.append(_string_1);
    _builder.append(")");
    String _leaving = this.leaving(op, highlight);
    _builder.append(_leaving);
    return _builder.toString();
  }
  
  protected String _toString(final HDLEqualityOp op, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(op, highlight);
    _builder.append(_entering);
    _builder.append("(");
    String _string = this.toString(op.getLeft(), highlight);
    _builder.append(_string);
    String _simpleSpace = highlight.simpleSpace();
    _builder.append(_simpleSpace);
    String _operator = highlight.operator(op.getType().toString());
    _builder.append(_operator);
    String _simpleSpace_1 = highlight.simpleSpace();
    _builder.append(_simpleSpace_1);
    String _string_1 = this.toString(op.getRight(), highlight);
    _builder.append(_string_1);
    _builder.append(")");
    String _leaving = this.leaving(op, highlight);
    _builder.append(_leaving);
    return _builder.toString();
  }
  
  protected String _toString(final HDLUnresolvedFragmentFunction frag, final SyntaxHighlighter highlight) {
    String _xifexpression = null;
    Boolean _isStatement = frag.getIsStatement();
    if ((_isStatement).booleanValue()) {
      _xifexpression = highlight.getSpacing().toString();
    } else {
      _xifexpression = "";
    }
    final String sb = _xifexpression;
    String _entering = this.entering(frag, highlight);
    String _plus = (sb + _entering);
    String _stringFrag = this.toStringFrag(frag, highlight, false);
    String _plus_1 = (_plus + _stringFrag);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLExpression> _params = frag.getParams();
      boolean _hasElements = false;
      for(final HDLExpression p : _params) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(p, highlight);
        _builder.append(_string);
      }
    }
    _builder.append(")");
    String res = (_plus_1 + _builder);
    HDLUnresolvedFragment _sub = frag.getSub();
    boolean _tripleNotEquals = (_sub != null);
    if (_tripleNotEquals) {
      String _string_1 = this.toString(frag.getSub(), highlight);
      String _plus_2 = ((res + ".") + _string_1);
      res = _plus_2;
    }
    Boolean _isStatement_1 = frag.getIsStatement();
    if ((_isStatement_1).booleanValue()) {
      res = (res + ";");
    }
    String _leaving = this.leaving(frag, highlight);
    return (res + _leaving);
  }
  
  protected String _toString(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight) {
    String _entering = this.entering(frag, highlight);
    String _stringFrag = this.toStringFrag(frag, highlight, true);
    String string = (_entering + _stringFrag);
    Boolean _isStatement = frag.getIsStatement();
    if ((_isStatement).booleanValue()) {
      string = (string + ";");
    }
    String _leaving = this.leaving(frag, highlight);
    return (string + _leaving);
  }
  
  public String toStringFrag(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight, final boolean doSub) {
    final StringBuilder sb = new StringBuilder();
    sb.append(frag.getFrag());
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLExpression> _array = frag.getArray();
      boolean _hasElements = false;
      for(final HDLExpression p : _array) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("[");
        } else {
          _builder.appendImmediate("][", "");
        }
        String _string = this.toString(p, highlight);
        _builder.append(_string);
      }
      if (_hasElements) {
        _builder.append("]");
      }
    }
    sb.append(_builder);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      ArrayList<HDLRange> _bits = frag.getBits();
      boolean _hasElements_1 = false;
      for(final HDLRange p_1 : _bits) {
        if (!_hasElements_1) {
          _hasElements_1 = true;
          _builder_1.append("{");
        } else {
          _builder_1.appendImmediate(",", "");
        }
        String _string_1 = this.toString(p_1, highlight);
        _builder_1.append(_string_1);
      }
      if (_hasElements_1) {
        _builder_1.append("}");
      }
    }
    sb.append(_builder_1);
    if ((doSub && (frag.getSub() != null))) {
      sb.append(".").append(this.toString(frag.getSub(), highlight));
    }
    return sb.toString();
  }
  
  protected String _toString(final HDLBitOp bitOp, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(bitOp, highlight));
    sb.append("(").append(this.toString(bitOp.getLeft(), highlight));
    final HDLBitOp.HDLBitOpType type = bitOp.getType();
    if ((Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_AND) || Objects.equal(type, HDLBitOp.HDLBitOpType.LOGI_OR))) {
      sb.append(highlight.simpleSpace()).append(highlight.operator(type.toString())).append(highlight.simpleSpace());
    } else {
      sb.append(highlight.operator(type.toString()));
    }
    sb.append(this.toString(bitOp.getRight(), highlight)).append(")");
    sb.append(this.leaving(bitOp, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLConcat concat, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    String _entering = this.entering(concat, highlight);
    _builder.append(_entering);
    {
      ArrayList<HDLExpression> _cats = concat.getCats();
      boolean _hasElements = false;
      for(final HDLExpression cat : _cats) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          String _operator = highlight.operator("#");
          _builder.appendImmediate(_operator, "");
        }
        String _operator_1 = highlight.operator(this.toString(cat, highlight));
        _builder.append(_operator_1);
      }
    }
    String _leaving = this.leaving(concat, highlight);
    _builder.append(_leaving);
    _builder.append(")");
    return _builder.toString();
  }
  
  protected String _toString(final HDLFunctionCall func, final SyntaxHighlighter highlight) {
    boolean isStatement = false;
    IHDLObject _container = func.getContainer();
    final IHDLObject container = _container;
    boolean _matched = false;
    if (container instanceof HDLBlock) {
      _matched=true;
      isStatement = true;
    }
    if (!_matched) {
      if (container instanceof HDLStatement) {
        _matched=true;
        isStatement = (((!(container instanceof HDLAssignment)) && (!(container instanceof HDLFunctionCall))) && 
          (!(container instanceof HDLInlineFunction)));
      }
    }
    if (!_matched) {
      if (container instanceof HDLUnit) {
        _matched=true;
        isStatement = true;
      }
    }
    StringBuilder _xifexpression = null;
    if (isStatement) {
      _xifexpression = highlight.getSpacing();
    } else {
      _xifexpression = new StringBuilder();
    }
    final StringBuilder sb = _xifexpression;
    sb.append(this.entering(func, highlight));
    sb.append(highlight.functionCall(func.getFunctionRefName().toString())).append("(");
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLExpression> _params = func.getParams();
      boolean _hasElements = false;
      for(final HDLExpression p : _params) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(p, highlight);
        _builder.append(_string);
      }
    }
    sb.append(_builder);
    sb.append(")");
    if (isStatement) {
      sb.append(";");
    }
    sb.append(this.leaving(func, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLFunctionParameter func, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    HDLFunctionParameter.RWType _rw = func.getRw();
    boolean _tripleNotEquals = (_rw != HDLFunctionParameter.RWType.READ);
    if (_tripleNotEquals) {
      sb.append(func.getRw());
    }
    sb.append(func.getType());
    HDLFunctionParameter.Type _type = func.getType();
    if (_type != null) {
      switch (_type) {
        case PARAM_ENUM:
          sb.append("<").append(highlight.enumRefType(func.getEnumSpecRefName().toString())).append(">");
          break;
        case PARAM_IF:
          sb.append("<").append(highlight.enumRefType(func.getEnumSpecRefName().toString())).append(">");
          break;
        case PARAM_FUNCTION:
          StringConcatenation _builder = new StringConcatenation();
          _builder.append("<");
          {
            ArrayList<HDLFunctionParameter> _funcSpec = func.getFuncSpec();
            boolean _hasElements = false;
            for(final HDLFunctionParameter p : _funcSpec) {
              if (!_hasElements) {
                _hasElements = true;
              } else {
                _builder.appendImmediate(",", "");
              }
              String _string = this.toString(p, highlight);
              _builder.append(_string);
            }
          }
          sb.append(_builder);
          HDLFunctionParameter _funcReturnSpec = func.getFuncReturnSpec();
          boolean _tripleNotEquals_1 = (_funcReturnSpec != null);
          if (_tripleNotEquals_1) {
            sb.append(highlight.simpleSpace()).append("=>").append(highlight.simpleSpace()).append(
              this.toString(func.getFuncReturnSpec(), highlight));
          }
          sb.append(">");
          break;
        default:
          break;
      }
    }
    HDLVariable _name = func.getName();
    boolean _tripleNotEquals_2 = (_name != null);
    if (_tripleNotEquals_2) {
      sb.append(highlight.simpleSpace()).append(highlight.varName(func.getName()));
    }
    ArrayList<HDLExpression> _dim = func.getDim();
    for (final HDLExpression d : _dim) {
      boolean _matched = false;
      if (d instanceof HDLLiteral) {
        Boolean _str = ((HDLLiteral)d).getStr();
        if (_str) {
          _matched=true;
          sb.append("[]");
        }
      }
      if (!_matched) {
        sb.append("[").append(this.toString(d, highlight)).append("]");
      }
    }
    return sb.toString();
  }
  
  protected String _toString(final HDLNativeFunction func, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(func, highlight));
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    Boolean _simOnly = func.getSimOnly();
    if ((_simOnly).booleanValue()) {
      sb.append(highlight.keyword("simulation")).append(highlight.simpleSpace());
    }
    sb.append(highlight.keyword("native"));
    sb.append(highlight.simpleSpace());
    sb.append(highlight.keyword("function"));
    sb.append(highlight.simpleSpace());
    HDLFunctionParameter _returnType = func.getReturnType();
    boolean _tripleNotEquals = (_returnType != null);
    if (_tripleNotEquals) {
      sb.append(this.toString(func.getReturnType(), highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.functionDecl(func.getName()));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLFunctionParameter> _args = func.getArgs();
      boolean _hasElements = false;
      for(final HDLFunctionParameter arg : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(arg, highlight);
        _builder.append(_string);
      }
    }
    _builder.append(")");
    sb.append(_builder);
    sb.append(";").append(highlight.newLine());
    sb.append(this.leaving(func, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLInlineFunction func, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(func, highlight));
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.keyword("inline")).append(highlight.simpleSpace()).append(highlight.keyword("function"));
    sb.append(highlight.simpleSpace());
    sb.append(this.toString(func.getReturnType(), highlight)).append(highlight.simpleSpace());
    sb.append(highlight.functionDecl(func.getName()));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLFunctionParameter> _args = func.getArgs();
      boolean _hasElements = false;
      for(final HDLFunctionParameter arg : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(arg, highlight);
        _builder.append(_string);
      }
    }
    _builder.append(")");
    sb.append(_builder);
    sb.append(highlight.simpleSpace()).append("->").append(highlight.simpleSpace()).append("(").append(
      this.toString(func.getExpr(), highlight)).append(")").append(highlight.newLine());
    sb.append(this.leaving(func, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLSubstituteFunction func, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(func, highlight));
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.keyword("substitute")).append(highlight.simpleSpace()).append(highlight.keyword("function")).append(highlight.simpleSpace());
    HDLFunctionParameter _returnType = func.getReturnType();
    boolean _tripleNotEquals = (_returnType != null);
    if (_tripleNotEquals) {
      sb.append(this.toString(func.getReturnType(), highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.functionDecl(func.getName()));
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLFunctionParameter> _args = func.getArgs();
      boolean _hasElements = false;
      for(final HDLFunctionParameter arg : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(arg, highlight);
        _builder.append(_string);
      }
    }
    _builder.append(")");
    sb.append(_builder);
    sb.append(highlight.simpleSpace()).append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLStatement> _stmnts = func.getStmnts();
    for (final HDLStatement string : _stmnts) {
      sb.append(this.toString(string, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append("}").append(highlight.newLine());
    sb.append(this.leaving(func, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceRef ref, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(ref, highlight));
    sb.append(highlight.interfaceRef(ref.getHIfRefName().getLastSegment()));
    ArrayList<HDLExpression> _ifArray = ref.getIfArray();
    for (final HDLExpression arr : _ifArray) {
      sb.append("[").append(this.toString(arr, highlight)).append("]");
    }
    sb.append(".");
    sb.append(this.varRef(ref, highlight));
    sb.append(this.leaving(ref, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
    String _entering = this.entering(ref, highlight);
    String _string = this.varRef(ref, highlight).toString();
    String _plus = (_entering + _string);
    String _leaving = this.leaving(ref, highlight);
    return (_plus + _leaving);
  }
  
  public StringBuilder varRef(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(ref, highlight));
    sb.append(highlight.variableRefName(ref));
    ArrayList<HDLExpression> _array = ref.getArray();
    for (final HDLExpression a : _array) {
      sb.append("[").append(this.toString(a, highlight)).append("]");
    }
    int _size = ref.getBits().size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("{");
      {
        ArrayList<HDLRange> _bits = ref.getBits();
        boolean _hasElements = false;
        for(final HDLRange bit : _bits) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "");
          }
          String _string = this.toString(bit, highlight);
          _builder.append(_string);
        }
      }
      _builder.append("}");
      sb.append(_builder);
    }
    sb.append(this.leaving(ref, highlight));
    return sb;
  }
  
  protected String _toString(final HDLLiteral lit, final SyntaxHighlighter highlight) {
    Boolean _str = lit.getStr();
    if ((_str).booleanValue()) {
      String _entering = this.entering(lit, highlight);
      String _val = lit.getVal();
      String _plus = ("\"" + _val);
      String _plus_1 = (_plus + "\"");
      String _literal = highlight.literal(_plus_1);
      String _plus_2 = (_entering + _literal);
      String _leaving = this.leaving(lit, highlight);
      return (_plus_2 + _leaving);
    }
    String _entering_1 = this.entering(lit, highlight);
    String _literal_1 = highlight.literal(lit.getVal());
    String _plus_3 = (_entering_1 + _literal_1);
    String _leaving_1 = this.leaving(lit, highlight);
    return (_plus_3 + _leaving_1);
  }
  
  protected String _toString(final HDLManip manip, final SyntaxHighlighter highlight) {
    final HDLManip.HDLManipType manipType = manip.getType();
    if (manipType != null) {
      switch (manipType) {
        case ARITH_NEG:
          String _entering = this.entering(manip, highlight);
          String _operator = highlight.operator("-");
          String _plus = (_entering + _operator);
          String _string = this.toString(manip.getTarget(), highlight);
          String _plus_1 = (_plus + _string);
          String _leaving = this.leaving(manip, highlight);
          return (_plus_1 + _leaving);
        case CAST:
          HDLType _castTo = manip.getCastTo();
          final HDLPrimitive type = ((HDLPrimitive) _castTo);
          final String entering = this.entering(manip, highlight);
          String _xifexpression = null;
          HDLExpression _width = type.getWidth();
          boolean _tripleNotEquals = (_width != null);
          if (_tripleNotEquals) {
            String _string_1 = this.toString(type.getWidth(), highlight);
            String _plus_2 = ("<" + _string_1);
            String _plus_3 = (_plus_2 + ">");
            _xifexpression = highlight.width(_plus_3);
          } else {
            _xifexpression = "";
          }
          final String width = _xifexpression;
          String _keyword = highlight.keyword(type.getType().toString().toLowerCase());
          String _plus_4 = ((entering + "(") + _keyword);
          String _plus_5 = (_plus_4 + width);
          String _plus_6 = (_plus_5 + ")");
          String _string_2 = this.toString(manip.getTarget(), highlight);
          String _plus_7 = (_plus_6 + _string_2);
          String _leaving_1 = this.leaving(manip, highlight);
          return (_plus_7 + _leaving_1);
        case BIT_NEG:
          String _entering_1 = this.entering(manip, highlight);
          String _operator_1 = highlight.operator("~");
          String _plus_8 = (_entering_1 + _operator_1);
          String _string_3 = this.toString(manip.getTarget(), highlight);
          String _plus_9 = (_plus_8 + _string_3);
          String _leaving_2 = this.leaving(manip, highlight);
          return (_plus_9 + _leaving_2);
        case LOGIC_NEG:
          String _entering_2 = this.entering(manip, highlight);
          String _operator_2 = highlight.operator("!");
          String _plus_10 = (_entering_2 + _operator_2);
          String _string_4 = this.toString(manip.getTarget(), highlight);
          String _plus_11 = (_plus_10 + _string_4);
          String _leaving_3 = this.leaving(manip, highlight);
          return (_plus_11 + _leaving_3);
        default:
          break;
      }
    }
    HDLManip.HDLManipType _type = manip.getType();
    String _plus_12 = ("Unexpected Type:" + _type);
    throw new IllegalArgumentException(_plus_12);
  }
  
  protected String _toString(final HDLBlock block, final SyntaxHighlighter highlight) {
    StringBuilder _spacing = highlight.getSpacing();
    final StringBuilder sb = new StringBuilder(_spacing);
    sb.append(this.entering(block, highlight));
    Boolean _process = block.getProcess();
    if ((_process).booleanValue()) {
      sb.append("process").append(highlight.simpleSpace());
    }
    sb.append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLStatement> _statements = block.getStatements();
    for (final HDLStatement string : _statements) {
      sb.append(this.toString(string, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append(highlight.getSpacing()).append("}");
    sb.append(this.leaving(block, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLExport export, final SyntaxHighlighter highlight) {
    final StringBuilder builder = highlight.getSpacing();
    builder.append(this.entering(export, highlight));
    builder.append(highlight.keyword("export"));
    builder.append(highlight.simpleSpace());
    builder.append(highlight.interfaceRef(export.getHIfRefName().getLastSegment()));
    HDLQualifiedName _varRefName = export.getVarRefName();
    boolean _tripleNotEquals = (_varRefName != null);
    if (_tripleNotEquals) {
      builder.append(".");
      HDLVariableRef _hDLVariableRef = new HDLVariableRef();
      builder.append(this.varRef(_hDLVariableRef.setVar(export.getVarRefName()), highlight));
    }
    String _match = export.getMatch();
    boolean _tripleNotEquals_1 = (_match != null);
    if (_tripleNotEquals_1) {
      builder.append(".");
      builder.append(highlight.exportMatch(export.getMatch()));
    }
    builder.append(";");
    builder.append(this.leaving(export, highlight));
    return builder.toString();
  }
  
  protected String _toString(final HDLAssignment ass, final SyntaxHighlighter highlight) {
    final StringBuilder builder = highlight.getSpacing();
    builder.append(this.entering(ass, highlight));
    builder.append(this.toString(ass.getLeft(), highlight));
    builder.append(highlight.operator(ass.getType().toString()));
    builder.append(this.toString(ass.getRight(), highlight)).append(";");
    builder.append(this.leaving(ass, highlight));
    return builder.toString();
  }
  
  protected String _toString(final HDLPrimitive prim, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(prim, highlight));
    sb.append(highlight.primitiveType(prim.getType().toString().toLowerCase()));
    HDLExpression _width = prim.getWidth();
    boolean _tripleNotEquals = (_width != null);
    if (_tripleNotEquals) {
      String _string = this.toString(prim.getWidth(), highlight);
      String _plus = ("<" + _string);
      String _plus_1 = (_plus + ">");
      sb.append(highlight.width(_plus_1));
    }
    sb.append(this.leaving(prim, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLForLoop loop, final SyntaxHighlighter highlight) {
    final StringBuilder space = highlight.getSpacing();
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(loop, highlight));
    sb.append(space).append(highlight.keyword("for")).append(highlight.simpleSpace()).append("(").append(
      loop.getParam().getName()).append(highlight.simpleSpace()).append("=").append(highlight.simpleSpace());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    {
      ArrayList<HDLRange> _range = loop.getRange();
      boolean _hasElements = false;
      for(final HDLRange range : _range) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(range, highlight);
        _builder.append(_string);
      }
    }
    _builder.append("}");
    sb.append(_builder);
    sb.append(")").append(highlight.simpleSpace()).append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLStatement> _dos = loop.getDos();
    for (final HDLStatement string : _dos) {
      sb.append(this.toString(string, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append(space).append("}");
    sb.append(this.leaving(loop, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLIfStatement ifStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(ifStmnt, highlight));
    final String origSpacing = sb.toString();
    sb.append(highlight.keyword("if")).append(highlight.simpleSpace()).append("(").append(
      this.toString(ifStmnt.getIfExp(), highlight)).append(")").append(highlight.simpleSpace()).append("{").append(
      highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLStatement> _thenDo = ifStmnt.getThenDo();
    for (final HDLStatement stmt : _thenDo) {
      sb.append(this.toString(stmt, highlight)).append(highlight.newLine());
    }
    int _size = ifStmnt.getElseDo().size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      sb.append(origSpacing).append("}").append(highlight.simpleSpace()).append(highlight.keyword("else")).append(
        highlight.simpleSpace()).append("{").append(highlight.newLine());
      ArrayList<HDLStatement> _elseDo = ifStmnt.getElseDo();
      for (final HDLStatement stmt_1 : _elseDo) {
        sb.append(this.toString(stmt_1, highlight)).append(highlight.newLine());
      }
    }
    highlight.decSpacing();
    sb.append(origSpacing).append("}");
    sb.append(this.leaving(ifStmnt, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLSwitchCaseStatement caseStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(caseStmnt, highlight));
    HDLExpression _label = caseStmnt.getLabel();
    boolean _tripleEquals = (_label == null);
    if (_tripleEquals) {
      sb.append(highlight.keyword("default")).append(":").append(highlight.simpleSpace()).append(highlight.newLine());
    } else {
      sb.append(highlight.keyword("case")).append(highlight.simpleSpace()).append(
        this.toString(caseStmnt.getLabel(), highlight)).append(":").append(highlight.simpleSpace()).append(highlight.newLine());
    }
    highlight.incSpacing();
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLStatement> _dos = caseStmnt.getDos();
      boolean _hasElements = false;
      for(final HDLStatement stmnt : _dos) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          String _newLine = highlight.newLine();
          _builder.appendImmediate(_newLine, "");
        }
        String _string = this.toString(stmnt, highlight);
        _builder.append(_string);
      }
    }
    sb.append(_builder);
    highlight.decSpacing();
    sb.append(this.leaving(caseStmnt, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLSwitchStatement switchStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(switchStmnt, highlight));
    sb.append(highlight.keyword("switch")).append("(").append(this.toString(switchStmnt.getCaseExp(), highlight)).append(")").append(highlight.simpleSpace()).append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLSwitchCaseStatement> _cases = switchStmnt.getCases();
    for (final HDLStatement stmnt : _cases) {
      sb.append(this.toString(stmnt, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append(highlight.getSpacing()).append("}");
    sb.append(this.leaving(switchStmnt, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLVariableDeclaration hvd, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(hvd, highlight));
    final Optional<? extends HDLType> resolveType = hvd.resolveType();
    ArrayList<HDLAnnotation> _annotations = hvd.getAnnotations();
    boolean _tripleNotEquals = (_annotations != null);
    if (_tripleNotEquals) {
      ArrayList<HDLAnnotation> _annotations_1 = hvd.getAnnotations();
      for (final HDLAnnotation hdla : _annotations_1) {
        sb.append(this.toString(hdla, highlight)).append(highlight.simpleSpace());
      }
    }
    final String dirString = hvd.getDirection().toString();
    int _length = dirString.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      sb.append(highlight.direction(dirString)).append(highlight.simpleSpace());
    }
    HDLRegisterConfig _register = hvd.getRegister();
    boolean _tripleNotEquals_1 = (_register != null);
    if (_tripleNotEquals_1) {
      sb.append(this.toString(hvd.getRegister(), highlight));
    }
    boolean _isPresent = resolveType.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      sb.append("#UNRESOLVED_TYPE#");
    } else {
      HDLType _get = resolveType.get();
      if ((_get instanceof HDLEnum)) {
        sb.append(highlight.keyword("enum")).append(highlight.simpleSpace()).append(
          this.toString(resolveType.get(), highlight));
      } else {
        sb.append(this.toString(resolveType.get(), highlight));
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLVariable> _variables = hvd.getVariables();
      boolean _hasElements = false;
      for(final HDLVariable hvar : _variables) {
        if (!_hasElements) {
          _hasElements = true;
          String _simpleSpace = highlight.simpleSpace();
          _builder.append(_simpleSpace);
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(hvar, highlight);
        _builder.append(_string);
      }
    }
    _builder.append(";");
    sb.append(_builder);
    SyntaxHighlighter.Context _context = highlight.getContext();
    boolean _equals = Objects.equal(_context, SyntaxHighlighter.Context.HDLPackage);
    if (_equals) {
      sb.append(highlight.newLine());
    }
    sb.append(this.leaving(hvd, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceDeclaration hid, final SyntaxHighlighter highlight) {
    highlight.pushContext(SyntaxHighlighter.Context.HDLInterface);
    final StringBuilder annos = highlight.getSpacing();
    annos.append(this.entering(hid, highlight));
    ArrayList<HDLAnnotation> _annotations = hid.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      annos.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    annos.append(this.toString(hid.getHIf(), highlight));
    annos.append(this.leaving(hid, highlight));
    highlight.popContext();
    return annos.toString();
  }
  
  protected String _toString(final HDLInterface hif, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(hif, highlight));
    sb.append(highlight.keyword("interface")).append(highlight.simpleSpace());
    sb.append(highlight.interfaceName(hif.getName()));
    sb.append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<HDLVariableDeclaration> _ports = hif.getPorts();
    for (final HDLVariableDeclaration hvar : _ports) {
      sb.append(this.toString(hvar, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append("}").append(highlight.newLine());
    sb.append(this.leaving(hif, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLEnumRef ref, final SyntaxHighlighter highlight) {
    String _entering = this.entering(ref, highlight);
    String _enumRefType = highlight.enumRefType(ref.getHEnumRefName().toString());
    String _plus = (_entering + _enumRefType);
    String _plus_1 = (_plus + ".");
    String _enumRefVar = highlight.enumRefVar(ref.getVarRefName().getLastSegment());
    String _plus_2 = (_plus_1 + _enumRefVar);
    String _leaving = this.leaving(ref, highlight);
    return (_plus_2 + _leaving);
  }
  
  protected String _toString(final HDLEnum e, final SyntaxHighlighter highlight) {
    String _entering = this.entering(e, highlight);
    String _enumName = highlight.enumName(e.getName());
    String _plus = (_entering + _enumName);
    String _leaving = this.leaving(e, highlight);
    return (_plus + _leaving);
  }
  
  protected String _toString(final HDLEnumDeclaration decl, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(decl, highlight));
    ArrayList<HDLAnnotation> _annotations = decl.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.keyword("enum")).append(highlight.simpleSpace());
    sb.append(highlight.enumName(decl.getHEnum().getName()));
    sb.append(highlight.simpleSpace());
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    {
      ArrayList<HDLVariable> _enums = decl.getHEnum().getEnums();
      boolean _hasElements = false;
      for(final HDLVariable henum : _enums) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          String _simpleSpace = highlight.simpleSpace();
          String _plus = ("," + _simpleSpace);
          _builder.appendImmediate(_plus, "");
        }
        String _string = this.toString(henum, highlight);
        _builder.append(_string);
      }
    }
    _builder.append("}");
    sb.append(_builder);
    sb.append(highlight.newLine());
    sb.append(this.leaving(decl, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLRegisterConfig reg, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(reg, highlight));
    sb.append(highlight.keyword("register"));
    final HDLRegisterConfig defaultReg = HDLRegisterConfig.defaultConfig();
    final StringBuilder params = new StringBuilder();
    params.append("(");
    boolean first = true;
    HDLExpression _clk = reg.getClk();
    HDLExpression _clk_1 = defaultReg.getClk();
    boolean _notEquals = (!Objects.equal(_clk, _clk_1));
    if (_notEquals) {
      params.append(highlight.param(HDLRegisterConfig.CLOCK_PARAM)).append("=").append(
        this.toString(reg.getClk(), highlight));
      first = false;
    }
    HDLExpression _rst = reg.getRst();
    HDLExpression _rst_1 = defaultReg.getRst();
    boolean _notEquals_1 = (!Objects.equal(_rst, _rst_1));
    if (_notEquals_1) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.RESET_PARAM)).append("=").append(
        this.toString(reg.getRst(), highlight));
      first = false;
    }
    HDLExpression _unresolvedClockType = reg.getUnresolvedClockType();
    boolean _tripleNotEquals = (_unresolvedClockType != null);
    if (_tripleNotEquals) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.EDGE_PARAM)).append("=").append(
        this.toString(reg.getUnresolvedClockType(), highlight));
      first = false;
    } else {
      if (((reg.getClockType() != null) && (reg.getClockType() != defaultReg.getClockType()))) {
        if ((!first)) {
          params.append(", ");
        }
        params.append(highlight.param(HDLRegisterConfig.EDGE_PARAM)).append("=").append(
          highlight.enumRefType("Edge")).append(".").append(highlight.enumRefVar(reg.getClockType().toString()));
        first = false;
      }
    }
    HDLExpression _unresolvedSyncType = reg.getUnresolvedSyncType();
    boolean _tripleNotEquals_1 = (_unresolvedSyncType != null);
    if (_tripleNotEquals_1) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.EDGE_PARAM)).append("=").append(
        this.toString(reg.getUnresolvedSyncType(), highlight));
      first = false;
    } else {
      if (((reg.getSyncType() != null) && (reg.getSyncType() != defaultReg.getSyncType()))) {
        if ((!first)) {
          params.append(", ");
        }
        params.append(highlight.param(HDLRegisterConfig.RESET_SYNC_PARAM)).append("=").append(
          highlight.enumRefType("Sync")).append(".").append(highlight.enumRefVar(reg.getSyncType().toString()));
        first = false;
      }
    }
    HDLExpression _unresolvedResetType = reg.getUnresolvedResetType();
    boolean _tripleNotEquals_2 = (_unresolvedResetType != null);
    if (_tripleNotEquals_2) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.EDGE_PARAM)).append("=").append(
        this.toString(reg.getUnresolvedResetType(), highlight));
      first = false;
    } else {
      if (((reg.getResetType() != null) && (reg.getResetType() != defaultReg.getResetType()))) {
        if ((!first)) {
          params.append(", ");
        }
        params.append(highlight.param(HDLRegisterConfig.RESET_TYPE_PARAM)).append("=").append(
          highlight.enumRefType("Active")).append(".").append(highlight.enumRefVar(reg.getResetType().toString()));
        first = false;
      }
    }
    HDLExpression _resetValue = reg.getResetValue();
    HDLExpression _resetValue_1 = defaultReg.getResetValue();
    boolean _notEquals_2 = (!Objects.equal(_resetValue, _resetValue_1));
    if (_notEquals_2) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.RESET_VALUE_PARAM)).append("=").append(
        this.toString(reg.getResetValue(), highlight));
      first = false;
    }
    HDLExpression _delay = reg.getDelay();
    HDLExpression _delay_1 = defaultReg.getDelay();
    boolean _notEquals_3 = (!Objects.equal(_delay, _delay_1));
    if (_notEquals_3) {
      if ((!first)) {
        params.append(", ");
      }
      params.append(highlight.param(HDLRegisterConfig.DELAY_PARAM)).append("=").append(
        this.toString(reg.getDelay(), highlight));
      first = false;
    }
    params.append(")");
    if ((!first)) {
      sb.append(params);
    }
    sb.append(highlight.simpleSpace());
    sb.append(this.leaving(reg, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLPackage pkg, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    highlight.pushContext(SyntaxHighlighter.Context.HDLPackage);
    sb.append(this.entering(pkg, highlight));
    String _pkg = pkg.getPkg();
    boolean _tripleNotEquals = (_pkg != null);
    if (_tripleNotEquals) {
      sb.append(highlight.keyword("package")).append(highlight.simpleSpace()).append(
        highlight.packageName(pkg.getPkg())).append(";").append(highlight.newLine());
    }
    ArrayList<HDLDeclaration> _declarations = pkg.getDeclarations();
    for (final HDLDeclaration decl : _declarations) {
      sb.append(this.toString(decl, highlight));
    }
    ArrayList<HDLUnit> _units = pkg.getUnits();
    for (final HDLUnit unit : _units) {
      sb.append(this.toString(unit, highlight));
    }
    sb.append(this.leaving(pkg, highlight));
    highlight.popContext();
    return sb.toString();
  }
  
  protected String _toString(final HDLUnit unit, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    highlight.pushContext(SyntaxHighlighter.Context.HDLUnit);
    sb.append(this.entering(unit, highlight));
    ArrayList<HDLAnnotation> _annotations = unit.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.newLine());
    }
    Boolean _simulation = unit.getSimulation();
    boolean _not = (!(_simulation).booleanValue());
    if (_not) {
      sb.append(highlight.keyword("module")).append(highlight.simpleSpace());
    } else {
      sb.append(highlight.keyword("testbench")).append(highlight.simpleSpace());
    }
    sb.append(highlight.unitName(unit.getName())).append("{").append(highlight.newLine());
    highlight.incSpacing();
    ArrayList<String> _imports = unit.getImports();
    for (final String imports : _imports) {
      sb.append(highlight.getSpacing()).append(highlight.keyword("import")).append(highlight.simpleSpace()).append(
        highlight.importName(imports)).append(";").append(highlight.newLine());
    }
    ArrayList<HDLStatement> _inits = unit.getInits();
    for (final HDLStatement stmnt : _inits) {
      sb.append(this.toString(stmnt, highlight)).append(highlight.newLine());
    }
    ArrayList<HDLStatement> _statements = unit.getStatements();
    for (final HDLStatement stmnt_1 : _statements) {
      sb.append(this.toString(stmnt_1, highlight)).append(highlight.newLine());
    }
    highlight.decSpacing();
    sb.append("}").append(highlight.newLine());
    sb.append(this.leaving(unit, highlight));
    highlight.popContext();
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceInstantiation hii, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    sb.append(this.entering(hii, highlight));
    ArrayList<HDLAnnotation> _annotations = hii.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.interfaceName(hii.getHIfRefName().toString())).append(highlight.simpleSpace()).append(
      this.toString(hii.getVar(), highlight));
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLArgument> _arguments = hii.getArguments();
      boolean _hasElements = false;
      for(final HDLArgument arg : _arguments) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("(");
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string = this.toString(arg, highlight);
        _builder.append(_string);
      }
      if (_hasElements) {
        _builder.append(")");
      }
    }
    sb.append(_builder);
    sb.append(";");
    sb.append(this.leaving(hii, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLArgument arg, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    sb.append(this.entering(arg, highlight));
    sb.append(highlight.param(arg.getName())).append("=").append(this.toString(arg.getExpression(), highlight));
    sb.append(this.leaving(arg, highlight));
    return sb.toString();
  }
  
  protected String _toString(final HDLRange range, final SyntaxHighlighter highlight) {
    HDLExpression _from = range.getFrom();
    boolean _tripleNotEquals = (_from != null);
    if (_tripleNotEquals) {
      String _entering = this.entering(range, highlight);
      String _string = this.toString(range.getFrom(), highlight);
      String _plus = (_entering + _string);
      String _plus_1 = (_plus + ":");
      String _string_1 = this.toString(range.getTo(), highlight);
      String _plus_2 = (_plus_1 + _string_1);
      String _leaving = this.leaving(range, highlight);
      return (_plus_2 + _leaving);
    }
    HDLExpression _inc = range.getInc();
    boolean _tripleNotEquals_1 = (_inc != null);
    if (_tripleNotEquals_1) {
      String _entering_1 = this.entering(range, highlight);
      String _string_2 = this.toString(range.getTo(), highlight);
      String _plus_3 = (_entering_1 + _string_2);
      String _plus_4 = (_plus_3 + " +: ");
      String _string_3 = this.toString(range.getInc(), highlight);
      String _plus_5 = (_plus_4 + _string_3);
      String _leaving_1 = this.leaving(range, highlight);
      return (_plus_5 + _leaving_1);
    }
    HDLExpression _dec = range.getDec();
    boolean _tripleNotEquals_2 = (_dec != null);
    if (_tripleNotEquals_2) {
      String _entering_2 = this.entering(range, highlight);
      String _string_4 = this.toString(range.getTo(), highlight);
      String _plus_6 = (_entering_2 + _string_4);
      String _plus_7 = (_plus_6 + " -: ");
      String _string_5 = this.toString(range.getDec(), highlight);
      String _plus_8 = (_plus_7 + _string_5);
      String _leaving_2 = this.leaving(range, highlight);
      return (_plus_8 + _leaving_2);
    }
    String _entering_3 = this.entering(range, highlight);
    String _string_6 = this.toString(range.getTo(), highlight);
    String _plus_9 = (_entering_3 + _string_6);
    String _leaving_3 = this.leaving(range, highlight);
    return (_plus_9 + _leaving_3);
  }
  
  protected String _toString(final HDLVariable hVar, final SyntaxHighlighter highlight) {
    final StringBuilder sb = new StringBuilder();
    this.entering(hVar, highlight);
    ArrayList<HDLAnnotation> _annotations = hVar.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    sb.append(highlight.varName(hVar));
    ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
    for (final HDLExpression arr : _dimensions) {
      sb.append("[").append(this.toString(arr, highlight)).append("]");
    }
    HDLExpression _defaultValue = hVar.getDefaultValue();
    boolean _tripleNotEquals = (_defaultValue != null);
    if (_tripleNotEquals) {
      sb.append("=").append(this.toString(hVar.getDefaultValue(), highlight));
    }
    this.leaving(hVar, highlight);
    return sb.toString();
  }
  
  protected String _toString(final HDLDirectGeneration hdg, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    this.entering(hdg, highlight);
    ArrayList<HDLAnnotation> _annotations = hdg.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      sb.append(this.toString(anno, highlight)).append(highlight.simpleSpace());
    }
    Boolean _include = hdg.getInclude();
    if ((_include).booleanValue()) {
      sb.append("include").append(highlight.simpleSpace());
    }
    sb.append(highlight.interfaceName(hdg.getIfName())).append(highlight.simpleSpace()).append(
      highlight.varName(hdg.getVar())).append("=");
    sb.append(highlight.simpleSpace()).append(highlight.keyword("generate")).append(highlight.simpleSpace()).append(
      highlight.generatorID(hdg.getGeneratorID()));
    sb.append("(");
    ArrayList<HDLArgument> _arguments = hdg.getArguments();
    for (final HDLArgument args : _arguments) {
      sb.append(this.toString(args, highlight));
    }
    sb.append(")");
    String _generatorContent = hdg.getGeneratorContent();
    boolean _tripleNotEquals = (_generatorContent != null);
    if (_tripleNotEquals) {
      sb.append(highlight.generatorContent(hdg.getGeneratorID(), hdg.getGeneratorContent()));
    }
    sb.append(";");
    this.leaving(hdg, highlight);
    return sb.toString();
  }
  
  public String toString(final IHDLObject ref, final SyntaxHighlighter highlight) {
    if (ref instanceof HDLInterfaceRef) {
      return _toString((HDLInterfaceRef)ref, highlight);
    } else if (ref instanceof HDLEnum) {
      return _toString((HDLEnum)ref, highlight);
    } else if (ref instanceof HDLEnumRef) {
      return _toString((HDLEnumRef)ref, highlight);
    } else if (ref instanceof HDLInlineFunction) {
      return _toString((HDLInlineFunction)ref, highlight);
    } else if (ref instanceof HDLNativeFunction) {
      return _toString((HDLNativeFunction)ref, highlight);
    } else if (ref instanceof HDLPrimitive) {
      return _toString((HDLPrimitive)ref, highlight);
    } else if (ref instanceof HDLSubstituteFunction) {
      return _toString((HDLSubstituteFunction)ref, highlight);
    } else if (ref instanceof HDLUnresolvedFragmentFunction) {
      return _toString((HDLUnresolvedFragmentFunction)ref, highlight);
    } else if (ref instanceof HDLVariableRef) {
      return _toString((HDLVariableRef)ref, highlight);
    } else if (ref instanceof HDLBitOp) {
      return _toString((HDLBitOp)ref, highlight);
    } else if (ref instanceof HDLBlock) {
      return _toString((HDLBlock)ref, highlight);
    } else if (ref instanceof HDLDirectGeneration) {
      return _toString((HDLDirectGeneration)ref, highlight);
    } else if (ref instanceof HDLEnumDeclaration) {
      return _toString((HDLEnumDeclaration)ref, highlight);
    } else if (ref instanceof HDLEqualityOp) {
      return _toString((HDLEqualityOp)ref, highlight);
    } else if (ref instanceof HDLForLoop) {
      return _toString((HDLForLoop)ref, highlight);
    } else if (ref instanceof HDLIfStatement) {
      return _toString((HDLIfStatement)ref, highlight);
    } else if (ref instanceof HDLInterface) {
      return _toString((HDLInterface)ref, highlight);
    } else if (ref instanceof HDLInterfaceDeclaration) {
      return _toString((HDLInterfaceDeclaration)ref, highlight);
    } else if (ref instanceof HDLInterfaceInstantiation) {
      return _toString((HDLInterfaceInstantiation)ref, highlight);
    } else if (ref instanceof HDLSwitchCaseStatement) {
      return _toString((HDLSwitchCaseStatement)ref, highlight);
    } else if (ref instanceof HDLSwitchStatement) {
      return _toString((HDLSwitchStatement)ref, highlight);
    } else if (ref instanceof HDLUnresolvedFragment) {
      return _toString((HDLUnresolvedFragment)ref, highlight);
    } else if (ref instanceof HDLVariableDeclaration) {
      return _toString((HDLVariableDeclaration)ref, highlight);
    } else if (ref instanceof HDLAnnotation) {
      return _toString((HDLAnnotation)ref, highlight);
    } else if (ref instanceof HDLArgument) {
      return _toString((HDLArgument)ref, highlight);
    } else if (ref instanceof HDLArrayInit) {
      return _toString((HDLArrayInit)ref, highlight);
    } else if (ref instanceof HDLAssignment) {
      return _toString((HDLAssignment)ref, highlight);
    } else if (ref instanceof HDLConcat) {
      return _toString((HDLConcat)ref, highlight);
    } else if (ref instanceof HDLExport) {
      return _toString((HDLExport)ref, highlight);
    } else if (ref instanceof HDLFunctionCall) {
      return _toString((HDLFunctionCall)ref, highlight);
    } else if (ref instanceof HDLFunctionParameter) {
      return _toString((HDLFunctionParameter)ref, highlight);
    } else if (ref instanceof HDLLiteral) {
      return _toString((HDLLiteral)ref, highlight);
    } else if (ref instanceof HDLManip) {
      return _toString((HDLManip)ref, highlight);
    } else if (ref instanceof HDLOpExpression) {
      return _toString((HDLOpExpression)ref, highlight);
    } else if (ref instanceof HDLPackage) {
      return _toString((HDLPackage)ref, highlight);
    } else if (ref instanceof HDLRange) {
      return _toString((HDLRange)ref, highlight);
    } else if (ref instanceof HDLRegisterConfig) {
      return _toString((HDLRegisterConfig)ref, highlight);
    } else if (ref instanceof HDLTernary) {
      return _toString((HDLTernary)ref, highlight);
    } else if (ref instanceof HDLUnit) {
      return _toString((HDLUnit)ref, highlight);
    } else if (ref instanceof HDLVariable) {
      return _toString((HDLVariable)ref, highlight);
    } else if (ref instanceof HDLExpression) {
      return _toString((HDLExpression)ref, highlight);
    } else if (ref instanceof HDLStatement) {
      return _toString((HDLStatement)ref, highlight);
    } else if (ref != null) {
      return _toString(ref, highlight);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(ref, highlight).toString());
    }
  }
}
