package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.tuhh.ict.pshdl.model.HDLAnnotation;
import de.tuhh.ict.pshdl.model.HDLArgument;
import de.tuhh.ict.pshdl.model.HDLArrayInit;
import de.tuhh.ict.pshdl.model.HDLAssignment;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLBlock;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLDeclaration;
import de.tuhh.ict.pshdl.model.HDLDirectGeneration;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration;
import de.tuhh.ict.pshdl.model.HDLEnumRef;
import de.tuhh.ict.pshdl.model.HDLEqualityOp;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLIfStatement;
import de.tuhh.ict.pshdl.model.HDLInlineFunction;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration;
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation;
import de.tuhh.ict.pshdl.model.HDLInterfaceRef;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLNativeFunction;
import de.tuhh.ict.pshdl.model.HDLOpExpression;
import de.tuhh.ict.pshdl.model.HDLPackage;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLReference;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import de.tuhh.ict.pshdl.model.HDLStatement;
import de.tuhh.ict.pshdl.model.HDLSubstituteFunction;
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement;
import de.tuhh.ict.pshdl.model.HDLSwitchStatement;
import de.tuhh.ict.pshdl.model.HDLTernary;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnit;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragmentFunction;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import de.tuhh.ict.pshdl.model.utils.SyntaxHighlighter;
import de.tuhh.ict.pshdl.model.utils.SyntaxHighlighter.Context;
import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class StringWriteExtension {
  public static StringWriteExtension INST = new Function0<StringWriteExtension>() {
    public StringWriteExtension apply() {
      StringWriteExtension _stringWriteExtension = new StringWriteExtension();
      return _stringWriteExtension;
    }
  }.apply();
  
  protected String _toString(final IHDLObject exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  protected String _toString(final HDLExpression exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  protected String _toString(final HDLStatement exp, final SyntaxHighlighter highlight) {
    HDLClass _classType = exp.getClassType();
    String _plus = ("Did not implement toString for " + _classType);
    RuntimeException _runtimeException = new RuntimeException(_plus);
    throw _runtimeException;
  }
  
  public static String asString(final IHDLObject exp, final SyntaxHighlighter highlight) {
    boolean _tripleEquals = (exp == null);
    if (_tripleEquals) {
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Can not handle null argument");
      throw _illegalArgumentException;
    }
    return StringWriteExtension.INST.toString(exp, highlight);
  }
  
  protected String _toString(final HDLArrayInit array, final SyntaxHighlighter highlight) {
    ArrayList<HDLExpression> _exp = array.getExp();
    int _size = _exp.size();
    boolean _equals = (_size == 1);
    if (_equals) {
      String _entering = this.entering(array, highlight);
      ArrayList<HDLExpression> _exp_1 = array.getExp();
      HDLExpression _get = _exp_1.get(0);
      String _string = this.toString(_get, highlight);
      String _plus = (_entering + _string);
      String _leaving = this.leaving(array, highlight);
      return (_plus + _leaving);
    }
    StringConcatenation _builder = new StringConcatenation();
    String _entering_1 = this.entering(array, highlight);
    _builder.append(_entering_1, "");
    _builder.append("{");
    {
      ArrayList<HDLExpression> _exp_2 = array.getExp();
      boolean _hasElements = false;
      for(final HDLExpression e : _exp_2) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string_1 = this.toString(e, highlight);
        _builder.append(_string_1, "");
      }
    }
    _builder.append("}");
    String _leaving_1 = this.leaving(array, highlight);
    _builder.append(_leaving_1, "");
    return _builder.toString();
  }
  
  public String leaving(final IHDLObject init, final SyntaxHighlighter highlighter) {
    return highlighter.leaving(init);
  }
  
  public String entering(final IHDLObject init, final SyntaxHighlighter highlighter) {
    return highlighter.entering(init);
  }
  
  protected String _toString(final HDLAnnotation anno, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(anno, highlight);
    sb.append(_entering);
    String _name = anno.getName();
    String _annotation = highlight.annotation(_name);
    sb.append(_annotation);
    String _value = anno.getValue();
    boolean _tripleNotEquals = (_value != null);
    if (_tripleNotEquals) {
      StringBuilder _append = sb.append("(");
      String _value_1 = anno.getValue();
      String _plus = ("\"" + _value_1);
      String _plus_1 = (_plus + "\"");
      String _string = highlight.string(_plus_1);
      StringBuilder _append_1 = _append.append(_string);
      _append_1.append(")");
    }
    String _leaving = this.leaving(anno, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLTernary tern, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(tern, highlight);
    _builder.append(_entering, "");
    _builder.append("(");
    HDLExpression _ifExpr = tern.getIfExpr();
    String _string = this.toString(_ifExpr, highlight);
    _builder.append(_string, "");
    String _operator = highlight.operator("?");
    _builder.append(_operator, "");
    HDLExpression _thenExpr = tern.getThenExpr();
    String _string_1 = this.toString(_thenExpr, highlight);
    _builder.append(_string_1, "");
    String _operator_1 = highlight.operator(":");
    _builder.append(_operator_1, "");
    HDLExpression _elseExpr = tern.getElseExpr();
    String _string_2 = this.toString(_elseExpr, highlight);
    _builder.append(_string_2, "");
    _builder.append(")");
    String _leaving = this.leaving(tern, highlight);
    _builder.append(_leaving, "");
    return _builder.toString();
  }
  
  protected String _toString(final HDLOpExpression op, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(op, highlight);
    _builder.append(_entering, "");
    _builder.append("(");
    HDLExpression _left = op.getLeft();
    String _string = this.toString(_left, highlight);
    _builder.append(_string, "");
    Enum<? extends Object> _type = op.getType();
    String _string_1 = _type.toString();
    String _operator = highlight.operator(_string_1);
    _builder.append(_operator, "");
    HDLExpression _right = op.getRight();
    String _string_2 = this.toString(_right, highlight);
    _builder.append(_string_2, "");
    _builder.append(")");
    String _leaving = this.leaving(op, highlight);
    _builder.append(_leaving, "");
    return _builder.toString();
  }
  
  protected String _toString(final HDLEqualityOp op, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    String _entering = this.entering(op, highlight);
    _builder.append(_entering, "");
    _builder.append("(");
    HDLExpression _left = op.getLeft();
    String _string = this.toString(_left, highlight);
    _builder.append(_string, "");
    String _simpleSpace = highlight.simpleSpace();
    _builder.append(_simpleSpace, "");
    HDLEqualityOpType _type = op.getType();
    String _string_1 = _type.toString();
    String _operator = highlight.operator(_string_1);
    _builder.append(_operator, "");
    String _simpleSpace_1 = highlight.simpleSpace();
    _builder.append(_simpleSpace_1, "");
    HDLExpression _right = op.getRight();
    String _string_2 = this.toString(_right, highlight);
    _builder.append(_string_2, "");
    _builder.append(")");
    String _leaving = this.leaving(op, highlight);
    _builder.append(_leaving, "");
    return _builder.toString();
  }
  
  protected String _toString(final HDLUnresolvedFragmentFunction frag, final SyntaxHighlighter highlight) {
    boolean isStatement = false;
    IHDLObject _container = frag.getContainer();
    final IHDLObject container = _container;
    boolean _matched = false;
    if (!_matched) {
      if (container instanceof HDLStatement) {
        final HDLStatement _hDLStatement = (HDLStatement)container;
        _matched=true;
        boolean _and = false;
        boolean _not = (!(_hDLStatement instanceof HDLAssignment));
        if (!_not) {
          _and = false;
        } else {
          boolean _not_1 = (!(_hDLStatement instanceof HDLFunctionCall));
          _and = (_not && _not_1);
        }
        isStatement = _and;
      }
    }
    if (!_matched) {
      if (container instanceof HDLBlock) {
        final HDLBlock _hDLBlock = (HDLBlock)container;
        _matched=true;
        isStatement = true;
      }
    }
    if (!_matched) {
      if (container instanceof HDLUnit) {
        final HDLUnit _hDLUnit = (HDLUnit)container;
        _matched=true;
        isStatement = true;
      }
    }
    String _xifexpression = null;
    if (isStatement) {
      StringBuilder _spacing = highlight.getSpacing();
      String _string = _spacing.toString();
      _xifexpression = _string;
    } else {
      _xifexpression = "";
    }
    final String sb = _xifexpression;
    String _entering = this.entering(frag, highlight);
    String _plus = (sb + _entering);
    String _stringFrag = this.toStringFrag(frag, highlight);
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
        String _string_1 = this.toString(p, highlight);
        _builder.append(_string_1, "");
      }
    }
    _builder.append(")");
    String res = (_plus_1 + _builder);
    if (isStatement) {
      String _plus_2 = (res + ";");
      res = _plus_2;
    }
    String _leaving = this.leaving(frag, highlight);
    return (res + _leaving);
  }
  
  protected String _toString(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight) {
    String _entering = this.entering(frag, highlight);
    String _stringFrag = this.toStringFrag(frag, highlight);
    String _plus = (_entering + _stringFrag);
    String _leaving = this.leaving(frag, highlight);
    return (_plus + _leaving);
  }
  
  public String toStringFrag(final HDLUnresolvedFragment frag, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _frag = frag.getFrag();
    sb.append(_frag);
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLExpression> _array = frag.getArray();
      boolean _hasElements = false;
      for(final HDLExpression p : _array) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("[", "");
        } else {
          _builder.appendImmediate("][", "");
        }
        String _string = this.toString(p, highlight);
        _builder.append(_string, "");
      }
      if (_hasElements) {
        _builder.append("]", "");
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
          _builder_1.append("{", "");
        } else {
          _builder_1.appendImmediate(",", "");
        }
        String _string_1 = this.toString(p_1, highlight);
        _builder_1.append(_string_1, "");
      }
      if (_hasElements_1) {
        _builder_1.append("}", "");
      }
    }
    sb.append(_builder_1);
    HDLUnresolvedFragment _sub = frag.getSub();
    boolean _tripleNotEquals = (_sub != null);
    if (_tripleNotEquals) {
      StringBuilder _append = sb.append(".");
      HDLUnresolvedFragment _sub_1 = frag.getSub();
      String _string_2 = this.toString(_sub_1, highlight);
      _append.append(_string_2);
    }
    return sb.toString();
  }
  
  protected String _toString(final HDLBitOp bitOp, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(bitOp, highlight);
    sb.append(_entering);
    StringBuilder _append = sb.append("(");
    HDLExpression _left = bitOp.getLeft();
    String _string = this.toString(_left, highlight);
    _append.append(_string);
    final HDLBitOpType type = bitOp.getType();
    boolean _or = false;
    boolean _equals = Objects.equal(type, HDLBitOpType.LOGI_AND);
    if (_equals) {
      _or = true;
    } else {
      boolean _equals_1 = Objects.equal(type, HDLBitOpType.LOGI_OR);
      _or = (_equals || _equals_1);
    }
    if (_or) {
      String _simpleSpace = highlight.simpleSpace();
      StringBuilder _append_1 = sb.append(_simpleSpace);
      String _string_1 = type.toString();
      String _operator = highlight.operator(_string_1);
      StringBuilder _append_2 = _append_1.append(_operator);
      String _simpleSpace_1 = highlight.simpleSpace();
      _append_2.append(_simpleSpace_1);
    } else {
      String _string_2 = type.toString();
      String _operator_1 = highlight.operator(_string_2);
      sb.append(_operator_1);
    }
    HDLExpression _right = bitOp.getRight();
    String _string_3 = this.toString(_right, highlight);
    StringBuilder _append_3 = sb.append(_string_3);
    _append_3.append(")");
    String _leaving = this.leaving(bitOp, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLConcat concat, final SyntaxHighlighter highlight) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    String _entering = this.entering(concat, highlight);
    _builder.append(_entering, "");
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
        String _string = this.toString(cat, highlight);
        String _operator_1 = highlight.operator(_string);
        _builder.append(_operator_1, "");
      }
    }
    String _leaving = this.leaving(concat, highlight);
    _builder.append(_leaving, "");
    _builder.append(")");
    return _builder.toString();
  }
  
  protected String _toString(final HDLFunctionCall func, final SyntaxHighlighter highlight) {
    boolean isStatement = false;
    IHDLObject _container = func.getContainer();
    final IHDLObject container = _container;
    boolean _matched = false;
    if (!_matched) {
      if (container instanceof HDLStatement) {
        final HDLStatement _hDLStatement = (HDLStatement)container;
        _matched=true;
        boolean _and = false;
        boolean _not = (!(_hDLStatement instanceof HDLAssignment));
        if (!_not) {
          _and = false;
        } else {
          boolean _not_1 = (!(_hDLStatement instanceof HDLFunctionCall));
          _and = (_not && _not_1);
        }
        isStatement = _and;
      }
    }
    if (!_matched) {
      if (container instanceof HDLBlock) {
        final HDLBlock _hDLBlock = (HDLBlock)container;
        _matched=true;
        isStatement = true;
      }
    }
    if (!_matched) {
      if (container instanceof HDLUnit) {
        final HDLUnit _hDLUnit = (HDLUnit)container;
        _matched=true;
        isStatement = true;
      }
    }
    StringBuilder _xifexpression = null;
    if (isStatement) {
      StringBuilder _spacing = highlight.getSpacing();
      _xifexpression = _spacing;
    } else {
      StringBuilder _stringBuilder = new StringBuilder();
      _xifexpression = _stringBuilder;
    }
    final StringBuilder sb = _xifexpression;
    String _entering = this.entering(func, highlight);
    sb.append(_entering);
    HDLQualifiedName _nameRefName = func.getNameRefName();
    String _string = _nameRefName.toString();
    String _functionCall = highlight.functionCall(_string);
    StringBuilder _append = sb.append(_functionCall);
    _append.append("(");
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
        String _string_1 = this.toString(p, highlight);
        _builder.append(_string_1, "");
      }
    }
    sb.append(_builder);
    sb.append(")");
    if (isStatement) {
      sb.append(";");
    }
    String _leaving = this.leaving(func, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLNativeFunction func, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(func, highlight);
    sb.append(_entering);
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = sb.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    Boolean _simOnly = func.getSimOnly();
    if ((_simOnly).booleanValue()) {
      String _keyword = highlight.keyword("simulation");
      StringBuilder _append_1 = sb.append(_keyword);
      String _simpleSpace_1 = highlight.simpleSpace();
      _append_1.append(_simpleSpace_1);
    }
    String _keyword_1 = highlight.keyword("native");
    sb.append(_keyword_1);
    String _simpleSpace_2 = highlight.simpleSpace();
    sb.append(_simpleSpace_2);
    String _keyword_2 = highlight.keyword("function");
    sb.append(_keyword_2);
    String _simpleSpace_3 = highlight.simpleSpace();
    sb.append(_simpleSpace_3);
    String _name = func.getName();
    String _functionDecl = highlight.functionDecl(_name);
    StringBuilder _append_2 = sb.append(_functionDecl);
    StringBuilder _append_3 = _append_2.append(";");
    String _newLine = highlight.newLine();
    _append_3.append(_newLine);
    String _leaving = this.leaving(func, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLInlineFunction func, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(func, highlight);
    sb.append(_entering);
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = sb.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    String _keyword = highlight.keyword("inline");
    StringBuilder _append_1 = sb.append(_keyword);
    String _simpleSpace_1 = highlight.simpleSpace();
    StringBuilder _append_2 = _append_1.append(_simpleSpace_1);
    String _keyword_1 = highlight.keyword("function");
    StringBuilder _append_3 = _append_2.append(_keyword_1);
    String _simpleSpace_2 = highlight.simpleSpace();
    _append_3.append(_simpleSpace_2);
    String _name = func.getName();
    String _functionDecl = highlight.functionDecl(_name);
    sb.append(_functionDecl);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLVariable> _args = func.getArgs();
      boolean _hasElements = false;
      for(final HDLVariable hVar : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _varName = highlight.varName(hVar);
        _builder.append(_varName, "");
      }
    }
    _builder.append(")");
    sb.append(_builder);
    String _simpleSpace_3 = highlight.simpleSpace();
    StringBuilder _append_4 = sb.append(_simpleSpace_3);
    StringBuilder _append_5 = _append_4.append("->");
    String _simpleSpace_4 = highlight.simpleSpace();
    StringBuilder _append_6 = _append_5.append(_simpleSpace_4);
    StringBuilder _append_7 = _append_6.append("(");
    HDLExpression _expr = func.getExpr();
    String _string_1 = this.toString(_expr, highlight);
    StringBuilder _append_8 = _append_7.append(_string_1);
    StringBuilder _append_9 = _append_8.append(")");
    String _newLine = highlight.newLine();
    _append_9.append(_newLine);
    String _leaving = this.leaving(func, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLSubstituteFunction func, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(func, highlight);
    sb.append(_entering);
    ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = sb.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    String _keyword = highlight.keyword("substitute");
    StringBuilder _append_1 = sb.append(_keyword);
    String _simpleSpace_1 = highlight.simpleSpace();
    StringBuilder _append_2 = _append_1.append(_simpleSpace_1);
    String _keyword_1 = highlight.keyword("function");
    StringBuilder _append_3 = _append_2.append(_keyword_1);
    String _simpleSpace_2 = highlight.simpleSpace();
    _append_3.append(_simpleSpace_2);
    String _name = func.getName();
    String _functionDecl = highlight.functionDecl(_name);
    sb.append(_functionDecl);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("(");
    {
      ArrayList<HDLVariable> _args = func.getArgs();
      boolean _hasElements = false;
      for(final HDLVariable hVar : _args) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(",", "");
        }
        String _varName = highlight.varName(hVar);
        _builder.append(_varName, "");
      }
    }
    _builder.append(")");
    sb.append(_builder);
    String _simpleSpace_3 = highlight.simpleSpace();
    StringBuilder _append_4 = sb.append(_simpleSpace_3);
    StringBuilder _append_5 = _append_4.append("{");
    String _newLine = highlight.newLine();
    _append_5.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLStatement> _stmnts = func.getStmnts();
    for (final HDLStatement string : _stmnts) {
      String _string_1 = this.toString(string, highlight);
      StringBuilder _append_6 = sb.append(_string_1);
      String _newLine_1 = highlight.newLine();
      _append_6.append(_newLine_1);
    }
    highlight.decSpacing();
    StringBuilder _append_7 = sb.append("}");
    String _newLine_2 = highlight.newLine();
    _append_7.append(_newLine_2);
    String _leaving = this.leaving(func, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceRef ref, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(ref, highlight);
    sb.append(_entering);
    HDLQualifiedName _hIfRefName = ref.getHIfRefName();
    String _lastSegment = _hIfRefName.getLastSegment();
    String _interfaceRef = highlight.interfaceRef(_lastSegment);
    sb.append(_interfaceRef);
    ArrayList<HDLExpression> _ifArray = ref.getIfArray();
    for (final HDLExpression arr : _ifArray) {
      StringBuilder _append = sb.append("[");
      String _string = this.toString(arr, highlight);
      StringBuilder _append_1 = _append.append(_string);
      _append_1.append("]");
    }
    sb.append(".");
    StringBuilder _varRef = this.varRef(ref, highlight);
    sb.append(_varRef);
    String _leaving = this.leaving(ref, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
    String _entering = this.entering(ref, highlight);
    StringBuilder _varRef = this.varRef(ref, highlight);
    String _string = _varRef.toString();
    String _plus = (_entering + _string);
    String _leaving = this.leaving(ref, highlight);
    return (_plus + _leaving);
  }
  
  public StringBuilder varRef(final HDLVariableRef ref, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(ref, highlight);
    sb.append(_entering);
    String _variableRefName = highlight.variableRefName(ref);
    sb.append(_variableRefName);
    ArrayList<HDLExpression> _array = ref.getArray();
    for (final HDLExpression a : _array) {
      StringBuilder _append = sb.append("[");
      String _string = this.toString(a, highlight);
      StringBuilder _append_1 = _append.append(_string);
      _append_1.append("]");
    }
    ArrayList<HDLRange> _bits = ref.getBits();
    int _size = _bits.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("{");
      {
        ArrayList<HDLRange> _bits_1 = ref.getBits();
        boolean _hasElements = false;
        for(final HDLRange bit : _bits_1) {
          if (!_hasElements) {
            _hasElements = true;
          } else {
            _builder.appendImmediate(",", "");
          }
          String _string_1 = this.toString(bit, highlight);
          _builder.append(_string_1, "");
        }
      }
      _builder.append("}");
      sb.append(_builder);
    }
    String _leaving = this.leaving(ref, highlight);
    sb.append(_leaving);
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
    String _val_1 = lit.getVal();
    String _literal_1 = highlight.literal(_val_1);
    String _plus_3 = (_entering_1 + _literal_1);
    String _leaving_1 = this.leaving(lit, highlight);
    return (_plus_3 + _leaving_1);
  }
  
  protected String _toString(final HDLManip manip, final SyntaxHighlighter highlight) {
    final HDLManipType manipType = manip.getType();
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(manipType,HDLManipType.ARITH_NEG)) {
        _matched=true;
        String _entering = this.entering(manip, highlight);
        String _operator = highlight.operator("-");
        String _plus = (_entering + _operator);
        HDLExpression _target = manip.getTarget();
        String _string = this.toString(_target, highlight);
        String _plus_1 = (_plus + _string);
        String _leaving = this.leaving(manip, highlight);
        return (_plus_1 + _leaving);
      }
    }
    if (!_matched) {
      if (Objects.equal(manipType,HDLManipType.CAST)) {
        _matched=true;
        HDLType _castTo = manip.getCastTo();
        final HDLPrimitive type = ((HDLPrimitive) _castTo);
        final String entering = this.entering(manip, highlight);
        String _xifexpression = null;
        HDLExpression _width = type.getWidth();
        boolean _tripleNotEquals = (_width != null);
        if (_tripleNotEquals) {
          HDLExpression _width_1 = type.getWidth();
          String _string_1 = this.toString(_width_1, highlight);
          String _plus_2 = ("<" + _string_1);
          String _plus_3 = (_plus_2 + ">");
          String _width_2 = highlight.width(_plus_3);
          _xifexpression = _width_2;
        } else {
          _xifexpression = "";
        }
        final String width = _xifexpression;
        String _plus_4 = (entering + "(");
        HDLPrimitiveType _type = type.getType();
        String _string_2 = _type.toString();
        String _lowerCase = _string_2.toLowerCase();
        String _keyword = highlight.keyword(_lowerCase);
        String _plus_5 = (_plus_4 + _keyword);
        String _plus_6 = (_plus_5 + width);
        String _plus_7 = (_plus_6 + ")");
        HDLExpression _target_1 = manip.getTarget();
        String _string_3 = this.toString(_target_1, highlight);
        String _plus_8 = (_plus_7 + _string_3);
        String _leaving_1 = this.leaving(manip, highlight);
        return (_plus_8 + _leaving_1);
      }
    }
    if (!_matched) {
      if (Objects.equal(manipType,HDLManipType.BIT_NEG)) {
        _matched=true;
        String _entering_1 = this.entering(manip, highlight);
        String _operator_1 = highlight.operator("~");
        String _plus_9 = (_entering_1 + _operator_1);
        HDLExpression _target_2 = manip.getTarget();
        String _string_4 = this.toString(_target_2, highlight);
        String _plus_10 = (_plus_9 + _string_4);
        String _leaving_2 = this.leaving(manip, highlight);
        return (_plus_10 + _leaving_2);
      }
    }
    if (!_matched) {
      if (Objects.equal(manipType,HDLManipType.LOGIC_NEG)) {
        _matched=true;
        String _entering_2 = this.entering(manip, highlight);
        String _operator_2 = highlight.operator("!");
        String _plus_11 = (_entering_2 + _operator_2);
        HDLExpression _target_3 = manip.getTarget();
        String _string_5 = this.toString(_target_3, highlight);
        String _plus_12 = (_plus_11 + _string_5);
        String _leaving_3 = this.leaving(manip, highlight);
        return (_plus_12 + _leaving_3);
      }
    }
    HDLManipType _type_1 = manip.getType();
    String _plus_13 = ("Unexpected Type:" + _type_1);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus_13);
    throw _illegalArgumentException;
  }
  
  protected String _toString(final HDLBlock block, final SyntaxHighlighter highlight) {
    StringBuilder _spacing = highlight.getSpacing();
    StringBuilder _stringBuilder = new StringBuilder(_spacing);
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(block, highlight);
    sb.append(_entering);
    Boolean _process = block.getProcess();
    if ((_process).booleanValue()) {
      StringBuilder _append = sb.append("process");
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    StringBuilder _append_1 = sb.append("{");
    String _newLine = highlight.newLine();
    _append_1.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLStatement> _statements = block.getStatements();
    for (final HDLStatement string : _statements) {
      String _string = this.toString(string, highlight);
      StringBuilder _append_2 = sb.append(_string);
      String _newLine_1 = highlight.newLine();
      _append_2.append(_newLine_1);
    }
    highlight.decSpacing();
    StringBuilder _spacing_1 = highlight.getSpacing();
    StringBuilder _append_3 = sb.append(_spacing_1);
    _append_3.append("}");
    String _leaving = this.leaving(block, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLAssignment ass, final SyntaxHighlighter highlight) {
    final StringBuilder builder = highlight.getSpacing();
    String _entering = this.entering(ass, highlight);
    builder.append(_entering);
    HDLReference _left = ass.getLeft();
    String _string = this.toString(_left, highlight);
    builder.append(_string);
    HDLAssignmentType _type = ass.getType();
    String _string_1 = _type.toString();
    String _operator = highlight.operator(_string_1);
    builder.append(_operator);
    HDLExpression _right = ass.getRight();
    String _string_2 = this.toString(_right, highlight);
    StringBuilder _append = builder.append(_string_2);
    _append.append(";");
    String _leaving = this.leaving(ass, highlight);
    builder.append(_leaving);
    return builder.toString();
  }
  
  protected String _toString(final HDLPrimitive prim, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(prim, highlight);
    sb.append(_entering);
    HDLPrimitiveType _type = prim.getType();
    String _string = _type.toString();
    String _lowerCase = _string.toLowerCase();
    String _primitiveType = highlight.primitiveType(_lowerCase);
    sb.append(_primitiveType);
    HDLExpression _width = prim.getWidth();
    boolean _tripleNotEquals = (_width != null);
    if (_tripleNotEquals) {
      HDLExpression _width_1 = prim.getWidth();
      String _string_1 = this.toString(_width_1, highlight);
      String _plus = ("<" + _string_1);
      String _plus_1 = (_plus + ">");
      String _width_2 = highlight.width(_plus_1);
      sb.append(_width_2);
    }
    String _leaving = this.leaving(prim, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLForLoop loop, final SyntaxHighlighter highlight) {
    final StringBuilder space = highlight.getSpacing();
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(loop, highlight);
    sb.append(_entering);
    StringBuilder _append = sb.append(space);
    String _keyword = highlight.keyword("for");
    StringBuilder _append_1 = _append.append(_keyword);
    String _simpleSpace = highlight.simpleSpace();
    StringBuilder _append_2 = _append_1.append(_simpleSpace);
    StringBuilder _append_3 = _append_2.append("(");
    HDLVariable _param = loop.getParam();
    String _name = _param.getName();
    StringBuilder _append_4 = _append_3.append(_name);
    String _simpleSpace_1 = highlight.simpleSpace();
    StringBuilder _append_5 = _append_4.append(_simpleSpace_1);
    StringBuilder _append_6 = _append_5.append("=");
    String _simpleSpace_2 = highlight.simpleSpace();
    _append_6.append(_simpleSpace_2);
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
        _builder.append(_string, "");
      }
    }
    _builder.append("}");
    sb.append(_builder);
    StringBuilder _append_7 = sb.append(")");
    String _simpleSpace_3 = highlight.simpleSpace();
    StringBuilder _append_8 = _append_7.append(_simpleSpace_3);
    StringBuilder _append_9 = _append_8.append("{");
    String _newLine = highlight.newLine();
    _append_9.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLStatement> _dos = loop.getDos();
    for (final HDLStatement string : _dos) {
      String _string_1 = this.toString(string, highlight);
      StringBuilder _append_10 = sb.append(_string_1);
      String _newLine_1 = highlight.newLine();
      _append_10.append(_newLine_1);
    }
    highlight.decSpacing();
    StringBuilder _append_11 = sb.append(space);
    _append_11.append("}");
    String _leaving = this.leaving(loop, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLIfStatement ifStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(ifStmnt, highlight);
    sb.append(_entering);
    final String origSpacing = sb.toString();
    String _keyword = highlight.keyword("if");
    StringBuilder _append = sb.append(_keyword);
    String _simpleSpace = highlight.simpleSpace();
    StringBuilder _append_1 = _append.append(_simpleSpace);
    StringBuilder _append_2 = _append_1.append("(");
    HDLExpression _ifExp = ifStmnt.getIfExp();
    String _string = this.toString(_ifExp, highlight);
    StringBuilder _append_3 = _append_2.append(_string);
    StringBuilder _append_4 = _append_3.append(")");
    String _simpleSpace_1 = highlight.simpleSpace();
    StringBuilder _append_5 = _append_4.append(_simpleSpace_1);
    StringBuilder _append_6 = _append_5.append("{");
    String _newLine = highlight.newLine();
    _append_6.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLStatement> _thenDo = ifStmnt.getThenDo();
    for (final HDLStatement stmt : _thenDo) {
      String _string_1 = this.toString(stmt, highlight);
      StringBuilder _append_7 = sb.append(_string_1);
      String _newLine_1 = highlight.newLine();
      _append_7.append(_newLine_1);
    }
    ArrayList<HDLStatement> _elseDo = ifStmnt.getElseDo();
    int _size = _elseDo.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      StringBuilder _append_8 = sb.append(origSpacing);
      StringBuilder _append_9 = _append_8.append("}");
      String _simpleSpace_2 = highlight.simpleSpace();
      StringBuilder _append_10 = _append_9.append(_simpleSpace_2);
      String _keyword_1 = highlight.keyword("else");
      StringBuilder _append_11 = _append_10.append(_keyword_1);
      String _simpleSpace_3 = highlight.simpleSpace();
      StringBuilder _append_12 = _append_11.append(_simpleSpace_3);
      StringBuilder _append_13 = _append_12.append("{");
      String _newLine_2 = highlight.newLine();
      _append_13.append(_newLine_2);
      ArrayList<HDLStatement> _elseDo_1 = ifStmnt.getElseDo();
      for (final HDLStatement stmt_1 : _elseDo_1) {
        String _string_2 = this.toString(stmt_1, highlight);
        StringBuilder _append_14 = sb.append(_string_2);
        String _newLine_3 = highlight.newLine();
        _append_14.append(_newLine_3);
      }
    }
    highlight.decSpacing();
    StringBuilder _append_15 = sb.append(origSpacing);
    _append_15.append("}");
    String _leaving = this.leaving(ifStmnt, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLSwitchCaseStatement caseStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(caseStmnt, highlight);
    sb.append(_entering);
    HDLExpression _label = caseStmnt.getLabel();
    boolean _tripleEquals = (_label == null);
    if (_tripleEquals) {
      String _keyword = highlight.keyword("default");
      StringBuilder _append = sb.append(_keyword);
      StringBuilder _append_1 = _append.append(":");
      String _simpleSpace = highlight.simpleSpace();
      StringBuilder _append_2 = _append_1.append(_simpleSpace);
      String _newLine = highlight.newLine();
      _append_2.append(_newLine);
    } else {
      String _keyword_1 = highlight.keyword("case");
      StringBuilder _append_3 = sb.append(_keyword_1);
      String _simpleSpace_1 = highlight.simpleSpace();
      StringBuilder _append_4 = _append_3.append(_simpleSpace_1);
      HDLExpression _label_1 = caseStmnt.getLabel();
      String _string = this.toString(_label_1, highlight);
      StringBuilder _append_5 = _append_4.append(_string);
      StringBuilder _append_6 = _append_5.append(":");
      String _simpleSpace_2 = highlight.simpleSpace();
      StringBuilder _append_7 = _append_6.append(_simpleSpace_2);
      String _newLine_1 = highlight.newLine();
      _append_7.append(_newLine_1);
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
          String _newLine_2 = highlight.newLine();
          _builder.appendImmediate(_newLine_2, "");
        }
        String _string_1 = this.toString(stmnt, highlight);
        _builder.append(_string_1, "");
      }
    }
    sb.append(_builder);
    highlight.decSpacing();
    String _leaving = this.leaving(caseStmnt, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLSwitchStatement switchStmnt, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(switchStmnt, highlight);
    sb.append(_entering);
    String _keyword = highlight.keyword("switch");
    StringBuilder _append = sb.append(_keyword);
    StringBuilder _append_1 = _append.append("(");
    HDLExpression _caseExp = switchStmnt.getCaseExp();
    String _string = this.toString(_caseExp, highlight);
    StringBuilder _append_2 = _append_1.append(_string);
    StringBuilder _append_3 = _append_2.append(")");
    String _simpleSpace = highlight.simpleSpace();
    StringBuilder _append_4 = _append_3.append(_simpleSpace);
    StringBuilder _append_5 = _append_4.append("{");
    String _newLine = highlight.newLine();
    _append_5.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLSwitchCaseStatement> _cases = switchStmnt.getCases();
    for (final HDLStatement stmnt : _cases) {
      String _string_1 = this.toString(stmnt, highlight);
      StringBuilder _append_6 = sb.append(_string_1);
      String _newLine_1 = highlight.newLine();
      _append_6.append(_newLine_1);
    }
    highlight.decSpacing();
    StringBuilder _spacing = highlight.getSpacing();
    StringBuilder _append_7 = sb.append(_spacing);
    _append_7.append("}");
    String _leaving = this.leaving(switchStmnt, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLVariableDeclaration hvd, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(hvd, highlight);
    sb.append(_entering);
    final Optional<? extends HDLType> resolveType = hvd.resolveType();
    ArrayList<HDLAnnotation> _annotations = hvd.getAnnotations();
    boolean _tripleNotEquals = (_annotations != null);
    if (_tripleNotEquals) {
      ArrayList<HDLAnnotation> _annotations_1 = hvd.getAnnotations();
      for (final HDLAnnotation hdla : _annotations_1) {
        String _string = this.toString(hdla, highlight);
        StringBuilder _append = sb.append(_string);
        String _simpleSpace = highlight.simpleSpace();
        _append.append(_simpleSpace);
      }
    }
    HDLDirection _direction = hvd.getDirection();
    final String dirString = _direction.toString();
    int _length = dirString.length();
    boolean _greaterThan = (_length > 0);
    if (_greaterThan) {
      String _direction_1 = highlight.direction(dirString);
      StringBuilder _append_1 = sb.append(_direction_1);
      String _simpleSpace_1 = highlight.simpleSpace();
      _append_1.append(_simpleSpace_1);
    }
    HDLRegisterConfig _register = hvd.getRegister();
    boolean _tripleNotEquals_1 = (_register != null);
    if (_tripleNotEquals_1) {
      HDLRegisterConfig _register_1 = hvd.getRegister();
      String _string_1 = this.toString(_register_1, highlight);
      sb.append(_string_1);
    }
    boolean _isPresent = resolveType.isPresent();
    boolean _not = (!_isPresent);
    if (_not) {
      sb.append("#UNRESOLVED_TYPE#");
    } else {
      HDLType _get = resolveType.get();
      if ((_get instanceof HDLEnum)) {
        String _keyword = highlight.keyword("enum");
        StringBuilder _append_2 = sb.append(_keyword);
        String _simpleSpace_2 = highlight.simpleSpace();
        StringBuilder _append_3 = _append_2.append(_simpleSpace_2);
        HDLType _get_1 = resolveType.get();
        String _string_2 = this.toString(_get_1, highlight);
        _append_3.append(_string_2);
      } else {
        HDLType _get_2 = resolveType.get();
        String _string_3 = this.toString(_get_2, highlight);
        sb.append(_string_3);
      }
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLVariable> _variables = hvd.getVariables();
      boolean _hasElements = false;
      for(final HDLVariable hvar : _variables) {
        if (!_hasElements) {
          _hasElements = true;
          String _simpleSpace_3 = highlight.simpleSpace();
          _builder.append(_simpleSpace_3, "");
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string_4 = this.toString(hvar, highlight);
        _builder.append(_string_4, "");
      }
    }
    _builder.append(";");
    sb.append(_builder);
    Context _context = highlight.getContext();
    boolean _equals = Objects.equal(_context, Context.HDLPackage);
    if (_equals) {
      String _newLine = highlight.newLine();
      sb.append(_newLine);
    }
    String _leaving = this.leaving(hvd, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceDeclaration hid, final SyntaxHighlighter highlight) {
    highlight.pushContext(Context.HDLInterface);
    final StringBuilder annos = highlight.getSpacing();
    String _entering = this.entering(hid, highlight);
    annos.append(_entering);
    ArrayList<HDLAnnotation> _annotations = hid.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = annos.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    HDLInterface _hIf = hid.getHIf();
    String _string_1 = this.toString(_hIf, highlight);
    annos.append(_string_1);
    String _leaving = this.leaving(hid, highlight);
    annos.append(_leaving);
    highlight.popContext();
    return annos.toString();
  }
  
  protected String _toString(final HDLInterface hif, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(hif, highlight);
    sb.append(_entering);
    String _keyword = highlight.keyword("interface");
    StringBuilder _append = sb.append(_keyword);
    String _simpleSpace = highlight.simpleSpace();
    _append.append(_simpleSpace);
    String _name = hif.getName();
    String _interfaceName = highlight.interfaceName(_name);
    sb.append(_interfaceName);
    StringBuilder _append_1 = sb.append("{");
    String _newLine = highlight.newLine();
    _append_1.append(_newLine);
    highlight.incSpacing();
    ArrayList<HDLVariableDeclaration> _ports = hif.getPorts();
    for (final HDLVariableDeclaration hvar : _ports) {
      String _string = this.toString(hvar, highlight);
      StringBuilder _append_2 = sb.append(_string);
      String _newLine_1 = highlight.newLine();
      _append_2.append(_newLine_1);
    }
    highlight.decSpacing();
    StringBuilder _append_3 = sb.append("}");
    String _newLine_2 = highlight.newLine();
    _append_3.append(_newLine_2);
    String _leaving = this.leaving(hif, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLEnumRef ref, final SyntaxHighlighter highlight) {
    String _entering = this.entering(ref, highlight);
    HDLQualifiedName _hEnumRefName = ref.getHEnumRefName();
    String _string = _hEnumRefName.toString();
    String _enumRefType = highlight.enumRefType(_string);
    String _plus = (_entering + _enumRefType);
    String _plus_1 = (_plus + ".");
    HDLQualifiedName _varRefName = ref.getVarRefName();
    String _lastSegment = _varRefName.getLastSegment();
    String _enumRefVar = highlight.enumRefVar(_lastSegment);
    String _plus_2 = (_plus_1 + _enumRefVar);
    String _leaving = this.leaving(ref, highlight);
    return (_plus_2 + _leaving);
  }
  
  protected String _toString(final HDLEnum e, final SyntaxHighlighter highlight) {
    String _entering = this.entering(e, highlight);
    String _name = e.getName();
    String _enumName = highlight.enumName(_name);
    String _plus = (_entering + _enumName);
    String _leaving = this.leaving(e, highlight);
    return (_plus + _leaving);
  }
  
  protected String _toString(final HDLEnumDeclaration decl, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(decl, highlight);
    sb.append(_entering);
    ArrayList<HDLAnnotation> _annotations = decl.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = sb.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    String _keyword = highlight.keyword("enum");
    StringBuilder _append_1 = sb.append(_keyword);
    String _simpleSpace_1 = highlight.simpleSpace();
    _append_1.append(_simpleSpace_1);
    HDLEnum _hEnum = decl.getHEnum();
    String _name = _hEnum.getName();
    String _enumName = highlight.enumName(_name);
    sb.append(_enumName);
    String _simpleSpace_2 = highlight.simpleSpace();
    StringBuilder _append_2 = sb.append(_simpleSpace_2);
    StringBuilder _append_3 = _append_2.append("=");
    String _simpleSpace_3 = highlight.simpleSpace();
    _append_3.append(_simpleSpace_3);
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{");
    {
      HDLEnum _hEnum_1 = decl.getHEnum();
      ArrayList<HDLVariable> _enums = _hEnum_1.getEnums();
      boolean _hasElements = false;
      for(final HDLVariable henum : _enums) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          String _simpleSpace_4 = highlight.simpleSpace();
          String _plus = ("," + _simpleSpace_4);
          _builder.appendImmediate(_plus, "");
        }
        String _string_1 = this.toString(henum, highlight);
        _builder.append(_string_1, "");
      }
    }
    _builder.append("}");
    sb.append(_builder);
    String _newLine = highlight.newLine();
    sb.append(_newLine);
    String _leaving = this.leaving(decl, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLRegisterConfig reg, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(reg, highlight);
    sb.append(_entering);
    String _keyword = highlight.keyword("register");
    sb.append(_keyword);
    final HDLRegisterConfig defaultReg = HDLRegisterConfig.defaultConfig();
    StringBuilder _stringBuilder_1 = new StringBuilder();
    final StringBuilder params = _stringBuilder_1;
    params.append("(");
    boolean first = true;
    HDLQualifiedName _clkRefName = reg.getClkRefName();
    HDLQualifiedName _clkRefName_1 = defaultReg.getClkRefName();
    boolean _equals = _clkRefName.equals(_clkRefName_1);
    boolean _not = (!_equals);
    if (_not) {
      String _param = highlight.param(HDLRegisterConfig.CLOCK_PARAM);
      StringBuilder _append = params.append(_param);
      StringBuilder _append_1 = _append.append("=");
      HDLQualifiedName _clkRefName_2 = reg.getClkRefName();
      String _variableRefName = highlight.variableRefName(_clkRefName_2);
      _append_1.append(_variableRefName);
      first = false;
    }
    HDLQualifiedName _rstRefName = reg.getRstRefName();
    HDLQualifiedName _rstRefName_1 = defaultReg.getRstRefName();
    boolean _equals_1 = _rstRefName.equals(_rstRefName_1);
    boolean _not_1 = (!_equals_1);
    if (_not_1) {
      boolean _not_2 = (!first);
      if (_not_2) {
        params.append(", ");
      }
      String _param_1 = highlight.param(HDLRegisterConfig.RESET_PARAM);
      StringBuilder _append_2 = params.append(_param_1);
      StringBuilder _append_3 = _append_2.append("=");
      HDLQualifiedName _rstRefName_2 = reg.getRstRefName();
      String _variableRefName_1 = highlight.variableRefName(_rstRefName_2);
      _append_3.append(_variableRefName_1);
      first = false;
    }
    boolean _and = false;
    HDLRegClockType _clockType = reg.getClockType();
    boolean _tripleNotEquals = (_clockType != null);
    if (!_tripleNotEquals) {
      _and = false;
    } else {
      HDLRegClockType _clockType_1 = reg.getClockType();
      HDLRegClockType _clockType_2 = defaultReg.getClockType();
      boolean _equals_2 = _clockType_1.equals(_clockType_2);
      boolean _not_3 = (!_equals_2);
      _and = (_tripleNotEquals && _not_3);
    }
    if (_and) {
      boolean _not_4 = (!first);
      if (_not_4) {
        params.append(", ");
      }
      String _param_2 = highlight.param(HDLRegisterConfig.EDGE_PARAM);
      StringBuilder _append_4 = params.append(_param_2);
      StringBuilder _append_5 = _append_4.append("=");
      String _enumRefType = highlight.enumRefType("Edge");
      StringBuilder _append_6 = _append_5.append(_enumRefType);
      StringBuilder _append_7 = _append_6.append(".");
      HDLRegClockType _clockType_3 = reg.getClockType();
      String _string = _clockType_3.toString();
      String _enumRefVar = highlight.enumRefVar(_string);
      _append_7.append(_enumRefVar);
      first = false;
    }
    boolean _and_1 = false;
    HDLRegSyncType _syncType = reg.getSyncType();
    boolean _tripleNotEquals_1 = (_syncType != null);
    if (!_tripleNotEquals_1) {
      _and_1 = false;
    } else {
      HDLRegSyncType _syncType_1 = reg.getSyncType();
      HDLRegSyncType _syncType_2 = defaultReg.getSyncType();
      boolean _equals_3 = _syncType_1.equals(_syncType_2);
      boolean _not_5 = (!_equals_3);
      _and_1 = (_tripleNotEquals_1 && _not_5);
    }
    if (_and_1) {
      boolean _not_6 = (!first);
      if (_not_6) {
        params.append(", ");
      }
      String _param_3 = highlight.param(HDLRegisterConfig.RESET_SYNC_PARAM);
      StringBuilder _append_8 = params.append(_param_3);
      StringBuilder _append_9 = _append_8.append("=");
      String _enumRefType_1 = highlight.enumRefType("Sync");
      StringBuilder _append_10 = _append_9.append(_enumRefType_1);
      StringBuilder _append_11 = _append_10.append(".");
      HDLRegSyncType _syncType_3 = reg.getSyncType();
      String _string_1 = _syncType_3.toString();
      String _enumRefVar_1 = highlight.enumRefVar(_string_1);
      _append_11.append(_enumRefVar_1);
      first = false;
    }
    boolean _and_2 = false;
    HDLRegResetActiveType _resetType = reg.getResetType();
    boolean _tripleNotEquals_2 = (_resetType != null);
    if (!_tripleNotEquals_2) {
      _and_2 = false;
    } else {
      HDLRegResetActiveType _resetType_1 = reg.getResetType();
      HDLRegResetActiveType _resetType_2 = defaultReg.getResetType();
      boolean _equals_4 = _resetType_1.equals(_resetType_2);
      boolean _not_7 = (!_equals_4);
      _and_2 = (_tripleNotEquals_2 && _not_7);
    }
    if (_and_2) {
      boolean _not_8 = (!first);
      if (_not_8) {
        params.append(", ");
      }
      String _param_4 = highlight.param(HDLRegisterConfig.RESET_TYPE_PARAM);
      StringBuilder _append_12 = params.append(_param_4);
      StringBuilder _append_13 = _append_12.append("=");
      String _enumRefType_2 = highlight.enumRefType("Active");
      StringBuilder _append_14 = _append_13.append(_enumRefType_2);
      StringBuilder _append_15 = _append_14.append(".");
      HDLRegResetActiveType _resetType_3 = reg.getResetType();
      String _string_2 = _resetType_3.toString();
      String _enumRefVar_2 = highlight.enumRefVar(_string_2);
      _append_15.append(_enumRefVar_2);
      first = false;
    }
    HDLExpression _resetValue = reg.getResetValue();
    HDLExpression _resetValue_1 = defaultReg.getResetValue();
    boolean _equals_5 = _resetValue.equals(_resetValue_1);
    boolean _not_9 = (!_equals_5);
    if (_not_9) {
      boolean _not_10 = (!first);
      if (_not_10) {
        params.append(", ");
      }
      String _param_5 = highlight.param(HDLRegisterConfig.RESET_VALUE_PARAM);
      StringBuilder _append_16 = params.append(_param_5);
      StringBuilder _append_17 = _append_16.append("=");
      HDLExpression _resetValue_2 = reg.getResetValue();
      String _string_3 = this.toString(_resetValue_2, highlight);
      _append_17.append(_string_3);
      first = false;
    }
    params.append(")");
    boolean _not_11 = (!first);
    if (_not_11) {
      sb.append(params);
    }
    String _simpleSpace = highlight.simpleSpace();
    sb.append(_simpleSpace);
    String _leaving = this.leaving(reg, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLPackage pkg, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    highlight.pushContext(Context.HDLPackage);
    String _entering = this.entering(pkg, highlight);
    sb.append(_entering);
    String _pkg = pkg.getPkg();
    boolean _tripleNotEquals = (_pkg != null);
    if (_tripleNotEquals) {
      String _keyword = highlight.keyword("package");
      StringBuilder _append = sb.append(_keyword);
      String _simpleSpace = highlight.simpleSpace();
      StringBuilder _append_1 = _append.append(_simpleSpace);
      String _pkg_1 = pkg.getPkg();
      String _packageName = highlight.packageName(_pkg_1);
      StringBuilder _append_2 = _append_1.append(_packageName);
      StringBuilder _append_3 = _append_2.append(";");
      String _newLine = highlight.newLine();
      _append_3.append(_newLine);
    }
    ArrayList<HDLDeclaration> _declarations = pkg.getDeclarations();
    for (final HDLDeclaration decl : _declarations) {
      String _string = this.toString(decl, highlight);
      sb.append(_string);
    }
    ArrayList<HDLUnit> _units = pkg.getUnits();
    for (final HDLUnit unit : _units) {
      String _string_1 = this.toString(unit, highlight);
      sb.append(_string_1);
    }
    String _leaving = this.leaving(pkg, highlight);
    sb.append(_leaving);
    highlight.popContext();
    return sb.toString();
  }
  
  protected String _toString(final HDLUnit unit, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    highlight.pushContext(Context.HDLUnit);
    String _entering = this.entering(unit, highlight);
    sb.append(_entering);
    Boolean _simulation = unit.getSimulation();
    boolean _not = (!(_simulation).booleanValue());
    if (_not) {
      String _keyword = highlight.keyword("module");
      StringBuilder _append = sb.append(_keyword);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    } else {
      String _keyword_1 = highlight.keyword("testbench");
      StringBuilder _append_1 = sb.append(_keyword_1);
      String _simpleSpace_1 = highlight.simpleSpace();
      _append_1.append(_simpleSpace_1);
    }
    String _name = unit.getName();
    String _unitName = highlight.unitName(_name);
    StringBuilder _append_2 = sb.append(_unitName);
    StringBuilder _append_3 = _append_2.append("{");
    String _newLine = highlight.newLine();
    _append_3.append(_newLine);
    highlight.incSpacing();
    ArrayList<String> _imports = unit.getImports();
    for (final String imports : _imports) {
      StringBuilder _spacing = highlight.getSpacing();
      StringBuilder _append_4 = sb.append(_spacing);
      String _keyword_2 = highlight.keyword("import");
      StringBuilder _append_5 = _append_4.append(_keyword_2);
      String _simpleSpace_2 = highlight.simpleSpace();
      StringBuilder _append_6 = _append_5.append(_simpleSpace_2);
      String _importName = highlight.importName(imports);
      StringBuilder _append_7 = _append_6.append(_importName);
      StringBuilder _append_8 = _append_7.append(";");
      String _newLine_1 = highlight.newLine();
      _append_8.append(_newLine_1);
    }
    ArrayList<HDLStatement> _inits = unit.getInits();
    for (final HDLStatement stmnt : _inits) {
      String _string = this.toString(stmnt, highlight);
      StringBuilder _append_9 = sb.append(_string);
      String _newLine_2 = highlight.newLine();
      _append_9.append(_newLine_2);
    }
    ArrayList<HDLStatement> _statements = unit.getStatements();
    for (final HDLStatement stmnt_1 : _statements) {
      String _string_1 = this.toString(stmnt_1, highlight);
      StringBuilder _append_10 = sb.append(_string_1);
      String _newLine_3 = highlight.newLine();
      _append_10.append(_newLine_3);
    }
    highlight.decSpacing();
    StringBuilder _append_11 = sb.append("}");
    String _newLine_4 = highlight.newLine();
    _append_11.append(_newLine_4);
    String _leaving = this.leaving(unit, highlight);
    sb.append(_leaving);
    highlight.popContext();
    return sb.toString();
  }
  
  protected String _toString(final HDLInterfaceInstantiation hii, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    String _entering = this.entering(hii, highlight);
    sb.append(_entering);
    HDLQualifiedName _hIfRefName = hii.getHIfRefName();
    String _string = _hIfRefName.toString();
    String _interfaceName = highlight.interfaceName(_string);
    StringBuilder _append = sb.append(_interfaceName);
    String _simpleSpace = highlight.simpleSpace();
    StringBuilder _append_1 = _append.append(_simpleSpace);
    HDLVariable _var = hii.getVar();
    String _string_1 = this.toString(_var, highlight);
    _append_1.append(_string_1);
    StringConcatenation _builder = new StringConcatenation();
    {
      ArrayList<HDLArgument> _arguments = hii.getArguments();
      boolean _hasElements = false;
      for(final HDLArgument arg : _arguments) {
        if (!_hasElements) {
          _hasElements = true;
          _builder.append("(", "");
        } else {
          _builder.appendImmediate(",", "");
        }
        String _string_2 = this.toString(arg, highlight);
        _builder.append(_string_2, "");
      }
      if (_hasElements) {
        _builder.append(")", "");
      }
    }
    sb.append(_builder);
    sb.append(";");
    String _leaving = this.leaving(hii, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLArgument arg, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    String _entering = this.entering(arg, highlight);
    sb.append(_entering);
    String _name = arg.getName();
    String _param = highlight.param(_name);
    StringBuilder _append = sb.append(_param);
    StringBuilder _append_1 = _append.append("=");
    HDLExpression _expression = arg.getExpression();
    String _string = this.toString(_expression, highlight);
    _append_1.append(_string);
    String _leaving = this.leaving(arg, highlight);
    sb.append(_leaving);
    return sb.toString();
  }
  
  protected String _toString(final HDLRange range, final SyntaxHighlighter highlight) {
    HDLExpression _from = range.getFrom();
    boolean _tripleNotEquals = (_from != null);
    if (_tripleNotEquals) {
      String _entering = this.entering(range, highlight);
      HDLExpression _from_1 = range.getFrom();
      String _string = this.toString(_from_1, highlight);
      String _plus = (_entering + _string);
      String _plus_1 = (_plus + ":");
      HDLExpression _to = range.getTo();
      String _string_1 = this.toString(_to, highlight);
      String _plus_2 = (_plus_1 + _string_1);
      String _leaving = this.leaving(range, highlight);
      return (_plus_2 + _leaving);
    }
    String _entering_1 = this.entering(range, highlight);
    HDLExpression _to_1 = range.getTo();
    String _string_2 = this.toString(_to_1, highlight);
    String _plus_3 = (_entering_1 + _string_2);
    String _leaving_1 = this.leaving(range, highlight);
    return (_plus_3 + _leaving_1);
  }
  
  protected String _toString(final HDLVariable hVar, final SyntaxHighlighter highlight) {
    StringBuilder _stringBuilder = new StringBuilder();
    final StringBuilder sb = _stringBuilder;
    this.entering(hVar, highlight);
    ArrayList<HDLAnnotation> _annotations = hVar.getAnnotations();
    for (final HDLAnnotation anno : _annotations) {
      String _string = this.toString(anno, highlight);
      StringBuilder _append = sb.append(_string);
      String _simpleSpace = highlight.simpleSpace();
      _append.append(_simpleSpace);
    }
    String _varName = highlight.varName(hVar);
    sb.append(_varName);
    ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
    for (final HDLExpression arr : _dimensions) {
      StringBuilder _append_1 = sb.append("[");
      String _string_1 = this.toString(arr, highlight);
      StringBuilder _append_2 = _append_1.append(_string_1);
      _append_2.append("]");
    }
    HDLExpression _defaultValue = hVar.getDefaultValue();
    boolean _tripleNotEquals = (_defaultValue != null);
    if (_tripleNotEquals) {
      StringBuilder _append_3 = sb.append("=");
      HDLExpression _defaultValue_1 = hVar.getDefaultValue();
      String _string_2 = this.toString(_defaultValue_1, highlight);
      _append_3.append(_string_2);
    }
    this.leaving(hVar, highlight);
    return sb.toString();
  }
  
  protected String _toString(final HDLDirectGeneration hdg, final SyntaxHighlighter highlight) {
    final StringBuilder sb = highlight.getSpacing();
    this.entering(hdg, highlight);
    HDLInterface _hIf = hdg.getHIf();
    String _name = _hIf.getName();
    String _interfaceName = highlight.interfaceName(_name);
    StringBuilder _append = sb.append(_interfaceName);
    String _simpleSpace = highlight.simpleSpace();
    StringBuilder _append_1 = _append.append(_simpleSpace);
    HDLVariable _var = hdg.getVar();
    String _varName = highlight.varName(_var);
    StringBuilder _append_2 = _append_1.append(_varName);
    _append_2.append("=");
    String _simpleSpace_1 = highlight.simpleSpace();
    StringBuilder _append_3 = sb.append(_simpleSpace_1);
    String _keyword = highlight.keyword("generate");
    StringBuilder _append_4 = _append_3.append(_keyword);
    String _simpleSpace_2 = highlight.simpleSpace();
    StringBuilder _append_5 = _append_4.append(_simpleSpace_2);
    String _generatorID = hdg.getGeneratorID();
    String _generatorID_1 = highlight.generatorID(_generatorID);
    _append_5.append(_generatorID_1);
    sb.append("(");
    ArrayList<HDLArgument> _arguments = hdg.getArguments();
    for (final HDLArgument args : _arguments) {
      String _string = this.toString(args, highlight);
      sb.append(_string);
    }
    sb.append(")");
    String _generatorContent = hdg.getGeneratorContent();
    boolean _tripleNotEquals = (_generatorContent != null);
    if (_tripleNotEquals) {
      String _generatorID_2 = hdg.getGeneratorID();
      String _generatorContent_1 = hdg.getGeneratorContent();
      String _generatorContent_2 = highlight.generatorContent(_generatorID_2, _generatorContent_1);
      sb.append(_generatorContent_2);
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
    } else if (ref instanceof HDLBlock) {
      return _toString((HDLBlock)ref, highlight);
    } else if (ref instanceof HDLConcat) {
      return _toString((HDLConcat)ref, highlight);
    } else if (ref instanceof HDLFunctionCall) {
      return _toString((HDLFunctionCall)ref, highlight);
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
