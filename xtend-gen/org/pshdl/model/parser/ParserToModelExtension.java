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
package org.pshdl.model.parser;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArgument;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLDeclaration;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLShiftOp;
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
import org.pshdl.model.parser.PSHDLLangLexer;
import org.pshdl.model.parser.PSHDLLangParser;
import org.pshdl.model.parser.SourceInfo;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;

@SuppressWarnings("all")
public class ParserToModelExtension {
  private BufferedTokenStream tokens;
  
  public ParserToModelExtension(final BufferedTokenStream tokens) {
    this.tokens = tokens;
  }
  
  public static HDLPackage toHDL(final BufferedTokenStream tokens, final PSHDLLangParser.PsModelContext ctx, final String libURI, final String src) {
    ParserToModelExtension _parserToModelExtension = new ParserToModelExtension(tokens);
    return _parserToModelExtension.toHDLPkg(ctx, libURI, src);
  }
  
  public HDLPackage toHDLPkg(final PSHDLLangParser.PsModelContext ctx, final String libURI, final String src) {
    HDLPackage _hDLPackage = new HDLPackage();
    HDLPackage pkg = _hDLPackage.setLibURI(libURI);
    PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
    boolean _tripleNotEquals = (_psQualifiedName != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = ctx.psQualifiedName();
      String _name = this.toName(_psQualifiedName_1);
      HDLPackage _setPkg = pkg.setPkg(_name);
      pkg = _setPkg;
    }
    List<PSHDLLangParser.PsUnitContext> _psUnit = ctx.psUnit();
    final Function1<PSHDLLangParser.PsUnitContext,HDLUnit> _function = new Function1<PSHDLLangParser.PsUnitContext,HDLUnit>() {
      public HDLUnit apply(final PSHDLLangParser.PsUnitContext it) {
        HDLUnit _hDLUnit = ParserToModelExtension.this.toHDLUnit(it, libURI);
        return _hDLUnit;
      }
    };
    List<HDLUnit> _map = ListExtensions.<PSHDLLangParser.PsUnitContext, HDLUnit>map(_psUnit, _function);
    HDLPackage _setUnits = pkg.setUnits(_map);
    pkg = _setUnits;
    List<PSHDLLangParser.PsDeclarationContext> _psDeclaration = ctx.psDeclaration();
    final Function1<PSHDLLangParser.PsDeclarationContext,HDLDeclaration> _function_1 = new Function1<PSHDLLangParser.PsDeclarationContext,HDLDeclaration>() {
      public HDLDeclaration apply(final PSHDLLangParser.PsDeclarationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLDeclaration) _hDL);
      }
    };
    List<HDLDeclaration> _map_1 = ListExtensions.<PSHDLLangParser.PsDeclarationContext, HDLDeclaration>map(_psDeclaration, _function_1);
    HDLPackage _setDeclarations = pkg.setDeclarations(_map_1);
    pkg = _setDeclarations;
    pkg.freeze(null);
    final HDLLibrary library = HDLLibrary.getLibrary(libURI);
    boolean _equals = Objects.equal(library, null);
    if (_equals) {
      IllegalArgumentException _illegalArgumentException = new IllegalArgumentException((("The library " + libURI) + " is not valid"));
      throw _illegalArgumentException;
    }
    library.addPkg(pkg, src);
    HDLPackage _attachContext = this.<HDLPackage>attachContext(pkg, ctx);
    return ((HDLPackage) _attachContext);
  }
  
  protected HDLDeclaration _toHDL(final PSHDLLangParser.PsDeclarationContext context) {
    PSHDLLangParser.PsDeclarationTypeContext _psDeclarationType = context.psDeclarationType();
    IHDLObject _hDL = this.toHDL(_psDeclarationType);
    HDLDeclaration res = ((HDLDeclaration) _hDL);
    List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
    final Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation>() {
      public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLAnnotation) _hDL);
      }
    };
    List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>map(_psAnnotation, _function);
    HDLDeclaration _setAnnotations = res.setAnnotations(_map);
    res = _setAnnotations;
    return this.<HDLDeclaration>attachContext(res, context);
  }
  
  public <T extends IHDLObject> T attachContext(final T obj, final ParserRuleContext context) {
    boolean _tripleEquals = (obj == null);
    if (_tripleEquals) {
      NullPointerException _nullPointerException = new NullPointerException("Null is not allowed");
      throw _nullPointerException;
    }
    SourceInfo _sourceInfo = new SourceInfo(this.tokens, context);
    obj.<SourceInfo>addMeta(SourceInfo.INFO, _sourceInfo);
    return obj;
  }
  
  protected HDLArgument _toHDL(final PSHDLLangParser.PsArgumentContext context) {
    HDLArgument _hDLArgument = new HDLArgument();
    TerminalNode _RULE_ID = context.RULE_ID();
    String _text = _RULE_ID.getText();
    HDLArgument res = _hDLArgument.setName(_text);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArgument _setExpression = res.setExpression(((HDLExpression) _hDL));
    res = _setExpression;
    return this.<HDLArgument>attachContext(res, context);
  }
  
  protected HDLBlock _toHDL(final PSHDLLangParser.PsProcessContext context) {
    HDLBlock _hDLBlock = new HDLBlock();
    HDLBlock block = _hDLBlock;
    boolean _tripleNotEquals = (context.isProcess != null);
    if (_tripleNotEquals) {
      HDLBlock _setProcess = block.setProcess(true);
      block = _setProcess;
    }
    List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
    final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock, _function);
    HDLBlock _setStatements = block.setStatements(_map);
    block = _setStatements;
    return this.<HDLBlock>attachContext(block, context);
  }
  
  protected HDLAnnotation _toHDL(final PSHDLLangParser.PsAnnotationContext context) {
    PSHDLLangParser.PsAnnotationTypeContext _psAnnotationType = context.psAnnotationType();
    final String name = _psAnnotationType.getText();
    String value = null;
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _tripleNotEquals = (_RULE_STRING != null);
    if (_tripleNotEquals) {
      TerminalNode _RULE_STRING_1 = context.RULE_STRING();
      String str = _RULE_STRING_1.getText();
      int _length = str.length();
      int _minus = (_length - 1);
      String _substring = str.substring(1, _minus);
      str = _substring;
      value = str;
    }
    HDLAnnotation _hDLAnnotation = new HDLAnnotation();
    HDLAnnotation _setName = _hDLAnnotation.setName(name);
    HDLAnnotation _setValue = _setName.setValue(value);
    return this.<HDLAnnotation>attachContext(_setValue, context);
  }
  
  protected HDLDeclaration _toHDL(final PSHDLLangParser.PsDeclarationTypeContext context) {
    PSHDLLangParser.PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
    boolean _tripleNotEquals = (_psFunctionDeclaration != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsFunctionDeclarationContext _psFunctionDeclaration_1 = context.psFunctionDeclaration();
      IHDLObject _hDL = this.toHDL(_psFunctionDeclaration_1);
      IHDLObject _attachContext = this.<IHDLObject>attachContext(_hDL, context);
      return ((HDLDeclaration) _attachContext);
    }
    PSHDLLangParser.PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
    boolean _tripleNotEquals_1 = (_psTypeDeclaration != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsTypeDeclarationContext _psTypeDeclaration_1 = context.psTypeDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psTypeDeclaration_1);
      IHDLObject _attachContext_1 = this.<IHDLObject>attachContext(_hDL_1, context);
      return ((HDLDeclaration) _attachContext_1);
    }
    PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    boolean _tripleNotEquals_2 = (_psVariableDeclaration != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration_1 = context.psVariableDeclaration();
      IHDLObject _hDL_2 = this.toHDL(_psVariableDeclaration_1);
      IHDLObject _attachContext_2 = this.<IHDLObject>attachContext(_hDL_2, context);
      return ((HDLDeclaration) _attachContext_2);
    }
    Class<? extends PSHDLLangParser.PsDeclarationTypeContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected HDLVariableDeclaration _toHDL(final PSHDLLangParser.PsVariableDeclarationContext context) {
    HDLVariableDeclaration _hDLVariableDeclaration = new HDLVariableDeclaration();
    HDLVariableDeclaration res = _hDLVariableDeclaration;
    PSHDLLangParser.PsPrimitiveContext _psPrimitive = context.psPrimitive();
    IHDLObject _hDL = this.toHDL(_psPrimitive);
    HDLVariableDeclaration _setType = res.setType(((HDLType) _hDL));
    res = _setType;
    HDLVariableDeclaration.HDLDirection dir = HDLVariableDeclaration.HDLDirection.INTERNAL;
    PSHDLLangParser.PsDirectionContext _psDirection = context.psDirection();
    boolean _tripleNotEquals = (_psDirection != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsDirectionContext _psDirection_1 = context.psDirection();
      String _text = _psDirection_1.getText();
      HDLVariableDeclaration.HDLDirection _op = HDLVariableDeclaration.HDLDirection.getOp(_text);
      dir = _op;
    }
    HDLVariableDeclaration _setDirection = res.setDirection(dir);
    res = _setDirection;
    List<PSHDLLangParser.PsDeclAssignmentContext> _psDeclAssignment = context.psDeclAssignment();
    for (final PSHDLLangParser.PsDeclAssignmentContext varDecl : _psDeclAssignment) {
      IHDLObject _hDL_1 = this.toHDL(varDecl);
      HDLVariableDeclaration _addVariables = res.addVariables(((HDLVariable) _hDL_1));
      res = _addVariables;
    }
    PSHDLLangParser.PsPrimitiveContext _psPrimitive_1 = context.psPrimitive();
    boolean _tripleNotEquals_1 = (_psPrimitive_1.isRegister != null);
    if (_tripleNotEquals_1) {
      ArrayList<HDLArgument> _arrayList = new ArrayList<HDLArgument>();
      Iterable<HDLArgument> args = _arrayList;
      PSHDLLangParser.PsPrimitiveContext _psPrimitive_2 = context.psPrimitive();
      PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = _psPrimitive_2.psPassedArguments();
      boolean _tripleNotEquals_2 = (_psPassedArguments != null);
      if (_tripleNotEquals_2) {
        PSHDLLangParser.PsPrimitiveContext _psPrimitive_3 = context.psPrimitive();
        PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = _psPrimitive_3.psPassedArguments();
        List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
        final Function1<PSHDLLangParser.PsArgumentContext,HDLArgument> _function = new Function1<PSHDLLangParser.PsArgumentContext,HDLArgument>() {
          public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
            IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
            return ((HDLArgument) _hDL);
          }
        };
        List<HDLArgument> _map = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument>map(_psArgument, _function);
        args = _map;
      }
      HDLRegisterConfig _fromArgs = HDLRegisterConfig.fromArgs(args);
      HDLVariableDeclaration _setRegister = res.setRegister(_fromArgs);
      res = _setRegister;
    }
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitContext context) {
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    boolean _tripleNotEquals = (_psExpression != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression();
      IHDLObject _hDL = this.toHDL(_psExpression_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
    return this.toHDL(_psArrayInitSubParens);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitExpContext context) {
    PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
    return this.toHDL(_psArrayInitSubParens);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitSubContext context) {
    List<PSHDLLangParser.PsExpressionContext> _psExpression = context.psExpression();
    boolean _tripleNotEquals = (_psExpression != null);
    if (_tripleNotEquals) {
      HDLArrayInit _hDLArrayInit = new HDLArrayInit();
      List<PSHDLLangParser.PsExpressionContext> _psExpression_1 = context.psExpression();
      final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
        public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLExpression) _hDL);
        }
      };
      List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression_1, _function);
      final HDLArrayInit arr = _hDLArrayInit.setExp(_map);
      return this.<HDLArrayInit>attachContext(arr, context);
    }
    PSHDLLangParser.PsArrayInitSubParensContext _psArrayInitSubParens = context.psArrayInitSubParens();
    return this.toHDL(_psArrayInitSubParens);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsArrayInitSubParensContext context) {
    PSHDLLangParser.PsArrayInitSubContext _psArrayInitSub = context.psArrayInitSub();
    IHDLObject _hDL = this.toHDL(_psArrayInitSub);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected HDLType _toHDL(final PSHDLLangParser.PsPrimitiveContext context) {
    PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    boolean _tripleNotEquals = (_psQualifiedName != null);
    if (_tripleNotEquals) {
      HDLEnum _hDLEnum = new HDLEnum();
      PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = context.psQualifiedName();
      String _name = this.toName(_psQualifiedName_1);
      HDLEnum _setName = _hDLEnum.setName(_name);
      return this.<HDLEnum>attachContext(_setName, context);
    }
    PSHDLLangParser.PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
    String _text = _psPrimitiveType.getText();
    String _upperCase = _text.toUpperCase();
    final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.valueOf(_upperCase);
    IHDLObject _hDL = null;
    PSHDLLangParser.PsWidthContext _psWidth = context.psWidth();
    if (_psWidth!=null) {
      _hDL=this.toHDL(_psWidth);
    }
    final HDLExpression width = ((HDLExpression) _hDL);
    HDLPrimitive _hDLPrimitive = new HDLPrimitive();
    HDLPrimitive.HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
    HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
    HDLPrimitive _setWidth = _setType.setWidth(width);
    return this.<HDLPrimitive>attachContext(_setWidth, context);
  }
  
  protected HDLVariable _toHDL(final PSHDLLangParser.PsDeclAssignmentContext context) {
    HDLVariable _hDLVariable = new HDLVariable();
    PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
    String _name = this.toName(_psVariable);
    HDLVariable res = _hDLVariable.setName(_name);
    List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
    final Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation>() {
      public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLAnnotation) _hDL);
      }
    };
    List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>map(_psAnnotation, _function);
    HDLVariable _setAnnotations = res.setAnnotations(_map);
    res = _setAnnotations;
    PSHDLLangParser.PsArrayContext _psArray = context.psArray();
    boolean _tripleNotEquals = (_psArray != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
      List<PSHDLLangParser.PsExpressionContext> _psExpression = _psArray_1.psExpression();
      final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function_1 = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
        public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLExpression) _hDL);
        }
      };
      List<HDLExpression> _map_1 = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression, _function_1);
      HDLVariable _setDimensions = res.setDimensions(_map_1);
      res = _setDimensions;
    }
    PSHDLLangParser.PsArrayInitContext _psArrayInit = context.psArrayInit();
    boolean _tripleNotEquals_1 = (_psArrayInit != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsArrayInitContext _psArrayInit_1 = context.psArrayInit();
      IHDLObject _hDL = this.toHDL(_psArrayInit_1);
      HDLVariable _setDefaultValue = res.setDefaultValue(((HDLExpression) _hDL));
      res = _setDefaultValue;
    }
    return this.<HDLVariable>attachContext(res, context);
  }
  
  public String toName(final PSHDLLangParser.PsVariableContext context) {
    TerminalNode _RULE_ID = context.RULE_ID();
    return _RULE_ID.getText();
  }
  
  public HDLPrimitive.HDLPrimitiveType getResultingType(final HDLPrimitive.HDLPrimitiveType pt, final HDLExpression width) {
    boolean _tripleNotEquals = (width != null);
    if (_tripleNotEquals) {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(pt,HDLPrimitive.HDLPrimitiveType.BIT)) {
          _matched=true;
          return HDLPrimitive.HDLPrimitiveType.BITVECTOR;
        }
      }
    } else {
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(pt,HDLPrimitive.HDLPrimitiveType.INT)) {
          _matched_1=true;
          return HDLPrimitive.HDLPrimitiveType.INTEGER;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(pt,HDLPrimitive.HDLPrimitiveType.UINT)) {
          _matched_1=true;
          return HDLPrimitive.HDLPrimitiveType.NATURAL;
        }
      }
    }
    return pt;
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsWidthContext context) {
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsValueContext context) {
    TerminalNode _RULE_PS_LITERAL_TERMINAL = context.RULE_PS_LITERAL_TERMINAL();
    boolean _tripleNotEquals = (_RULE_PS_LITERAL_TERMINAL != null);
    if (_tripleNotEquals) {
      HDLLiteral _hDLLiteral = new HDLLiteral();
      HDLLiteral _setStr = _hDLLiteral.setStr(false);
      TerminalNode _RULE_PS_LITERAL_TERMINAL_1 = context.RULE_PS_LITERAL_TERMINAL();
      String _text = _RULE_PS_LITERAL_TERMINAL_1.getText();
      HDLLiteral _setVal = _setStr.setVal(_text);
      return this.<HDLLiteral>attachContext(_setVal, context);
    }
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _tripleNotEquals_1 = (_RULE_STRING != null);
    if (_tripleNotEquals_1) {
      TerminalNode _RULE_STRING_1 = context.RULE_STRING();
      String str = _RULE_STRING_1.getText();
      int _length = str.length();
      int _minus = (_length - 1);
      String _substring = str.substring(1, _minus);
      str = _substring;
      HDLLiteral _hDLLiteral_1 = new HDLLiteral();
      HDLLiteral _setStr_1 = _hDLLiteral_1.setStr(true);
      HDLLiteral _setVal_1 = _setStr_1.setVal(str);
      return this.<HDLLiteral>attachContext(_setVal_1, context);
    }
    PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
    boolean _tripleNotEquals_2 = (_psVariableRef != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsVariableRefContext _psVariableRef_1 = context.psVariableRef();
      IHDLObject _hDL = this.toHDL(_psVariableRef_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    Class<? extends PSHDLLangParser.PsValueContext> _class = context.getClass();
    String _plus = ("Not correctly implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsValueExpContext context) {
    PSHDLLangParser.PsValueContext _psValue = context.psValue();
    IHDLObject _hDL = this.toHDL(_psValue);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected HDLConcat _toHDL(final PSHDLLangParser.PsConcatContext context) {
    HDLConcat _hDLConcat = new HDLConcat();
    HDLConcat cat = _hDLConcat;
    List<PSHDLLangParser.PsExpressionContext> _psExpression = context.psExpression();
    final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
      public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLExpression) _hDL);
      }
    };
    List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression, _function);
    HDLConcat _setCats = cat.setCats(_map);
    cat = _setCats;
    return this.<HDLConcat>attachContext(cat, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitLogOrContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.LOGI_OR);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitLogAndContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.LOGI_AND);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitXorContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.XOR);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitOrContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.OR);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLangParser.PsBitAndContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOp.HDLBitOpType.AND);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLShiftOp _toHDL(final PSHDLLangParser.PsShiftContext context) {
    String _text = context.op.getText();
    final HDLShiftOp.HDLShiftOpType type = HDLShiftOp.HDLShiftOpType.getOp(_text);
    HDLShiftOp _hDLShiftOp = new HDLShiftOp();
    HDLShiftOp res = _hDLShiftOp.setType(type);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLShiftOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLShiftOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLShiftOp>attachContext(res, context);
  }
  
  protected HDLEqualityOp _toHDL(final PSHDLLangParser.PsEqualityCompContext context) {
    String _text = context.op.getText();
    final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(_text);
    HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
    HDLEqualityOp res = _hDLEqualityOp.setType(type);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected HDLEqualityOp _toHDL(final PSHDLLangParser.PsEqualityContext context) {
    String _text = context.op.getText();
    final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(_text);
    HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
    HDLEqualityOp res = _hDLEqualityOp.setType(type);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected HDLArithOp _toHDL(final PSHDLLangParser.PsMulContext context) {
    String _text = context.op.getText();
    final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(_text);
    HDLArithOp _hDLArithOp = new HDLArithOp();
    HDLArithOp res = _hDLArithOp.setType(type);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected HDLArithOp _toHDL(final PSHDLLangParser.PsAddContext context) {
    String _text = context.op.getText();
    final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(_text);
    HDLArithOp _hDLArithOp = new HDLArithOp();
    HDLArithOp res = _hDLArithOp.setType(type);
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected HDLPrimitive _toHDL(final PSHDLLangParser.PsCastContext context) {
    PSHDLLangParser.PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
    String _text = _psPrimitiveType.getText();
    String _upperCase = _text.toUpperCase();
    final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.valueOf(_upperCase);
    IHDLObject _hDL = null;
    PSHDLLangParser.PsWidthContext _psWidth = context.psWidth();
    if (_psWidth!=null) {
      _hDL=this.toHDL(_psWidth);
    }
    final HDLExpression width = ((HDLExpression) _hDL);
    HDLPrimitive _hDLPrimitive = new HDLPrimitive();
    HDLPrimitive.HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
    HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
    HDLPrimitive _setWidth = _setType.setWidth(width);
    return this.<HDLPrimitive>attachContext(_setWidth, context);
  }
  
  protected HDLManip _toHDL(final PSHDLLangParser.PsManipContext context) {
    HDLManip _hDLManip = new HDLManip();
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLManip res = _hDLManip.setTarget(((HDLExpression) _hDL));
    PSHDLLangParser.PsCastContext _psCast = context.psCast();
    boolean _tripleNotEquals = (_psCast != null);
    if (_tripleNotEquals) {
      HDLManip _setType = res.setType(HDLManip.HDLManipType.CAST);
      res = _setType;
      PSHDLLangParser.PsCastContext _psCast_1 = context.psCast();
      IHDLObject _hDL_1 = this.toHDL(_psCast_1);
      HDLManip _setCastTo = res.setCastTo(((HDLType) _hDL_1));
      res = _setCastTo;
    } else {
      int _type = context.type.getType();
      final int _switchValue = _type;
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(_switchValue,PSHDLLangLexer.LOGIC_NEG)) {
          _matched=true;
          HDLManip _setType_1 = res.setType(HDLManip.HDLManipType.LOGIC_NEG);
          res = _setType_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,PSHDLLangLexer.ARITH_NEG)) {
          _matched=true;
          HDLManip _setType_2 = res.setType(HDLManip.HDLManipType.ARITH_NEG);
          res = _setType_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,PSHDLLangLexer.BIT_NEG)) {
          _matched=true;
          HDLManip _setType_3 = res.setType(HDLManip.HDLManipType.BIT_NEG);
          res = _setType_3;
        }
      }
    }
    return this.<HDLManip>attachContext(res, context);
  }
  
  protected HDLTernary _toHDL(final PSHDLLangParser.PsTernaryContext context) {
    HDLTernary _hDLTernary = new HDLTernary();
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLTernary res = _hDLTernary.setIfExpr(((HDLExpression) _hDL));
    PSHDLLangParser.PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLTernary _setThenExpr = res.setThenExpr(((HDLExpression) _hDL_1));
    res = _setThenExpr;
    PSHDLLangParser.PsExpressionContext _psExpression_2 = context.psExpression(2);
    IHDLObject _hDL_2 = this.toHDL(_psExpression_2);
    HDLTernary _setElseExpr = res.setElseExpr(((HDLExpression) _hDL_2));
    res = _setElseExpr;
    return this.<HDLTernary>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsParensContext context) {
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected HDLExpression _toHDL(final PSHDLLangParser.PsExpressionContext context) {
    Class<? extends PSHDLLangParser.PsExpressionContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public String toName(final PSHDLLangParser.PsEnumContext context) {
    PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    return this.toName(_psQualifiedName);
  }
  
  public String toName(final PSHDLLangParser.PsInterfaceContext context) {
    PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    return this.toName(_psQualifiedName);
  }
  
  public HDLQualifiedName toFQNName(final PSHDLLangParser.PsQualifiedNameContext context) {
    String _text = context.getText();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
    return _hDLQualifiedName;
  }
  
  public String toName(final PSHDLLangParser.PsQualifiedNameContext context) {
    String _text = context.getText();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
    return _hDLQualifiedName.toString();
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsTypeDeclarationContext context) {
    PSHDLLangParser.PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
    boolean _tripleNotEquals = (_psEnumDeclaration != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsEnumDeclarationContext _psEnumDeclaration_1 = context.psEnumDeclaration();
      IHDLObject _hDL = this.toHDL(_psEnumDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
    boolean _tripleNotEquals_1 = (_psInterfaceDeclaration != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsInterfaceDeclarationContext _psInterfaceDeclaration_1 = context.psInterfaceDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psInterfaceDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    Class<? extends PSHDLLangParser.PsTypeDeclarationContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected HDLInterfaceDeclaration _toHDL(final PSHDLLangParser.PsInterfaceDeclarationContext context) {
    HDLInterface _hDLInterface = new HDLInterface();
    PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
    String _name = this.toName(_psInterface);
    HDLInterface hIf = _hDLInterface.setName(_name);
    PSHDLLangParser.PsInterfaceDeclContext _psInterfaceDecl = context.psInterfaceDecl();
    List<PSHDLLangParser.PsPortDeclarationContext> _psPortDeclaration = _psInterfaceDecl.psPortDeclaration();
    final Function1<PSHDLLangParser.PsPortDeclarationContext,HDLVariableDeclaration> _function = new Function1<PSHDLLangParser.PsPortDeclarationContext,HDLVariableDeclaration>() {
      public HDLVariableDeclaration apply(final PSHDLLangParser.PsPortDeclarationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLVariableDeclaration) _hDL);
      }
    };
    List<HDLVariableDeclaration> _map = ListExtensions.<PSHDLLangParser.PsPortDeclarationContext, HDLVariableDeclaration>map(_psPortDeclaration, _function);
    HDLInterface _setPorts = hIf.setPorts(_map);
    hIf = _setPorts;
    HDLInterfaceDeclaration _hDLInterfaceDeclaration = new HDLInterfaceDeclaration();
    HDLInterfaceDeclaration _setHIf = _hDLInterfaceDeclaration.setHIf(hIf);
    return this.<HDLInterfaceDeclaration>attachContext(_setHIf, context);
  }
  
  protected HDLVariableDeclaration _toHDL(final PSHDLLangParser.PsPortDeclarationContext context) {
    PSHDLLangParser.PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    IHDLObject _hDL = this.toHDL(_psVariableDeclaration);
    HDLVariableDeclaration res = ((HDLVariableDeclaration) _hDL);
    List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
    final Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation>() {
      public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLAnnotation) _hDL);
      }
    };
    List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>map(_psAnnotation, _function);
    HDLVariableDeclaration _setAnnotations = res.setAnnotations(_map);
    res = _setAnnotations;
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsBlockContext context) {
    PSHDLLangParser.PsDeclarationContext _psDeclaration = context.psDeclaration();
    boolean _tripleNotEquals = (_psDeclaration != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsDeclarationContext _psDeclaration_1 = context.psDeclaration();
      IHDLObject _hDL = this.toHDL(_psDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsInstantiationContext _psInstantiation = context.psInstantiation();
    boolean _tripleNotEquals_1 = (_psInstantiation != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsInstantiationContext _psInstantiation_1 = context.psInstantiation();
      IHDLObject _hDL_1 = this.toHDL(_psInstantiation_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PSHDLLangParser.PsStatementContext _psStatement = context.psStatement();
    boolean _tripleNotEquals_2 = (_psStatement != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsStatementContext _psStatement_1 = context.psStatement();
      IHDLObject _hDL_2 = this.toHDL(_psStatement_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends PSHDLLangParser.PsBlockContext> _class = context.getClass();
    String _plus = ("Not correctly implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected HDLDirectGeneration _toHDL(final PSHDLLangParser.PsDirectGenerationContext context) {
    HDLDirectGeneration _hDLDirectGeneration = new HDLDirectGeneration();
    HDLDirectGeneration gen = _hDLDirectGeneration.setGeneratorContent("");
    boolean _tripleNotEquals = (context.isInclude != null);
    HDLDirectGeneration _setInclude = gen.setInclude(_tripleNotEquals);
    gen = _setInclude;
    PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
    IHDLObject _hDL = this.toHDL(_psInterface);
    HDLDirectGeneration _setHIf = gen.setHIf(((HDLInterface) _hDL));
    gen = _setHIf;
    PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL_1 = this.toHDL(_psVariable);
    HDLDirectGeneration _setVar = gen.setVar(((HDLVariable) _hDL_1));
    gen = _setVar;
    TerminalNode _RULE_ID = context.RULE_ID();
    String _text = _RULE_ID.getText();
    HDLDirectGeneration _setGeneratorID = gen.setGeneratorID(_text);
    gen = _setGeneratorID;
    PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _tripleNotEquals_1 = (_psPassedArguments != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
      List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
      final Function1<PSHDLLangParser.PsArgumentContext,HDLArgument> _function = new Function1<PSHDLLangParser.PsArgumentContext,HDLArgument>() {
        public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLArgument) _hDL);
        }
      };
      List<HDLArgument> _map = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument>map(_psArgument, _function);
      HDLDirectGeneration _setArguments = gen.setArguments(_map);
      gen = _setArguments;
    }
    TerminalNode _RULE_GENERATOR_CONTENT = context.RULE_GENERATOR_CONTENT();
    boolean _tripleNotEquals_2 = (_RULE_GENERATOR_CONTENT != null);
    if (_tripleNotEquals_2) {
      TerminalNode _RULE_GENERATOR_CONTENT_1 = context.RULE_GENERATOR_CONTENT();
      String _text_1 = _RULE_GENERATOR_CONTENT_1.getText();
      HDLDirectGeneration _setGeneratorContent = gen.setGeneratorContent(_text_1);
      gen = _setGeneratorContent;
    }
    return this.<HDLDirectGeneration>attachContext(gen, context);
  }
  
  protected HDLVariable _toHDL(final PSHDLLangParser.PsVariableContext context) {
    HDLVariable _hDLVariable = new HDLVariable();
    String _name = this.toName(context);
    HDLVariable _setName = _hDLVariable.setName(_name);
    return this.<HDLVariable>attachContext(_setName, context);
  }
  
  protected HDLInterface _toHDL(final PSHDLLangParser.PsInterfaceContext context) {
    HDLInterface _hDLInterface = new HDLInterface();
    String _name = this.toName(context);
    HDLInterface _setName = _hDLInterface.setName(_name);
    return this.<HDLInterface>attachContext(_setName, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsInstantiationContext context) {
    PSHDLLangParser.PsDirectGenerationContext _psDirectGeneration = context.psDirectGeneration();
    boolean _tripleNotEquals = (_psDirectGeneration != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsDirectGenerationContext _psDirectGeneration_1 = context.psDirectGeneration();
      IHDLObject _hDL = this.toHDL(_psDirectGeneration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsInterfaceInstantiationContext _psInterfaceInstantiation = context.psInterfaceInstantiation();
    boolean _tripleNotEquals_1 = (_psInterfaceInstantiation != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsInterfaceInstantiationContext _psInterfaceInstantiation_1 = context.psInterfaceInstantiation();
      IHDLObject _hDL_1 = this.toHDL(_psInterfaceInstantiation_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    Class<? extends PSHDLLangParser.PsInstantiationContext> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected HDLEnum _toHDL(final PSHDLLangParser.PsEnumContext context) {
    HDLEnum _hDLEnum = new HDLEnum();
    String _name = this.toName(context);
    HDLEnum _setName = _hDLEnum.setName(_name);
    return this.<HDLEnum>attachContext(_setName, context);
  }
  
  protected HDLEnumDeclaration _toHDL(final PSHDLLangParser.PsEnumDeclarationContext context) {
    PSHDLLangParser.PsEnumContext _psEnum = context.psEnum();
    IHDLObject _hDL = this.toHDL(_psEnum);
    HDLEnum he = ((HDLEnum) _hDL);
    List<PSHDLLangParser.PsVariableContext> _psVariable = context.psVariable();
    final Function1<PSHDLLangParser.PsVariableContext,HDLVariable> _function = new Function1<PSHDLLangParser.PsVariableContext,HDLVariable>() {
      public HDLVariable apply(final PSHDLLangParser.PsVariableContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLVariable) _hDL);
      }
    };
    List<HDLVariable> _map = ListExtensions.<PSHDLLangParser.PsVariableContext, HDLVariable>map(_psVariable, _function);
    HDLEnum _setEnums = he.setEnums(_map);
    he = _setEnums;
    HDLEnumDeclaration _hDLEnumDeclaration = new HDLEnumDeclaration();
    HDLEnumDeclaration _setHEnum = _hDLEnumDeclaration.setHEnum(he);
    return this.<HDLEnumDeclaration>attachContext(_setHEnum, context);
  }
  
  protected HDLSubstituteFunction _toHDL(final PSHDLLangParser.PsSubstituteFunctionContext context) {
    HDLSubstituteFunction _hDLSubstituteFunction = new HDLSubstituteFunction();
    HDLSubstituteFunction func = _hDLSubstituteFunction;
    PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLSubstituteFunction _setName = func.setName(_name);
    func = _setName;
    List<PSHDLLangParser.PsStatementContext> _psStatement = context.psStatement();
    final Function1<PSHDLLangParser.PsStatementContext,HDLStatement> _function = new Function1<PSHDLLangParser.PsStatementContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsStatementContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsStatementContext, HDLStatement>map(_psStatement, _function);
    HDLSubstituteFunction _setStmnts = func.setStmnts(_map);
    func = _setStmnts;
    PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
    List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
    final Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter> _function_1 = new Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter>() {
      public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLFunctionParameter) _hDL);
      }
    };
    List<HDLFunctionParameter> _map_1 = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>map(_psFuncSpec, _function_1);
    HDLSubstituteFunction _setArgs = func.setArgs(_map_1);
    func = _setArgs;
    PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
    boolean _tripleNotEquals = (_psFuncRecturnType != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType_1 = context.psFuncRecturnType();
      IHDLObject _hDL = this.toHDL(_psFuncRecturnType_1);
      HDLSubstituteFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL));
      func = _setReturnType;
    }
    return this.<HDLSubstituteFunction>attachContext(func, context);
  }
  
  protected HDLNativeFunction _toHDL(final PSHDLLangParser.PsNativeFunctionContext context) {
    HDLNativeFunction _hDLNativeFunction = new HDLNativeFunction();
    HDLNativeFunction func = _hDLNativeFunction;
    PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLNativeFunction _setName = func.setName(_name);
    func = _setName;
    boolean _tripleNotEquals = (context.isSim != null);
    HDLNativeFunction _setSimOnly = func.setSimOnly(_tripleNotEquals);
    func = _setSimOnly;
    PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
    List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
    final Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter>() {
      public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLFunctionParameter) _hDL);
      }
    };
    List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>map(_psFuncSpec, _function);
    HDLNativeFunction _setArgs = func.setArgs(_map);
    func = _setArgs;
    PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
    boolean _tripleNotEquals_1 = (_psFuncRecturnType != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType_1 = context.psFuncRecturnType();
      IHDLObject _hDL = this.toHDL(_psFuncRecturnType_1);
      HDLNativeFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL));
      func = _setReturnType;
    }
    return this.<HDLNativeFunction>attachContext(func, context);
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncRecturnTypeContext context) {
    PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = context.psFuncParamType();
    IHDLObject _hDL = this.toHDL(_psFuncParamType);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    HDLFunctionParameter _setRw = res.setRw(HDLFunctionParameter.RWType.RETURN);
    res = _setRw;
    final Function1<PSHDLLangParser.PsFuncOptArrayContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsFuncOptArrayContext,HDLExpression>() {
      public HDLExpression apply(final PSHDLLangParser.PsFuncOptArrayContext it) {
        HDLExpression _xifexpression = null;
        PSHDLLangParser.PsExpressionContext _psExpression = it.psExpression();
        boolean _tripleNotEquals = (_psExpression != null);
        if (_tripleNotEquals) {
          PSHDLLangParser.PsExpressionContext _psExpression_1 = it.psExpression();
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(_psExpression_1);
          _xifexpression = ((HDLExpression) _hDL);
        } else {
          HDLExpression _EMPTY_ARR = HDLFunctionParameter.EMPTY_ARR();
          _xifexpression = _EMPTY_ARR;
        }
        return _xifexpression;
      }
    };
    List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression>map(context.dims, _function);
    HDLFunctionParameter _setDim = res.setDim(_map);
    res = _setDim;
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncSpecContext context) {
    PSHDLLangParser.PsFuncParamWithRWContext _psFuncParamWithRW = context.psFuncParamWithRW();
    IHDLObject _hDL = this.toHDL(_psFuncParamWithRW);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    HDLVariable _hDLVariable = new HDLVariable();
    TerminalNode _RULE_ID = context.RULE_ID();
    String _text = _RULE_ID.getText();
    HDLVariable _setName = _hDLVariable.setName(_text);
    HDLFunctionParameter _setName_1 = res.setName(_setName);
    res = _setName_1;
    final Function1<PSHDLLangParser.PsFuncOptArrayContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsFuncOptArrayContext,HDLExpression>() {
      public HDLExpression apply(final PSHDLLangParser.PsFuncOptArrayContext it) {
        HDLExpression _xifexpression = null;
        PSHDLLangParser.PsExpressionContext _psExpression = it.psExpression();
        boolean _tripleNotEquals = (_psExpression != null);
        if (_tripleNotEquals) {
          PSHDLLangParser.PsExpressionContext _psExpression_1 = it.psExpression();
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(_psExpression_1);
          _xifexpression = ((HDLExpression) _hDL);
        } else {
          HDLExpression _EMPTY_ARR = HDLFunctionParameter.EMPTY_ARR();
          _xifexpression = _EMPTY_ARR;
        }
        return _xifexpression;
      }
    };
    List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsFuncOptArrayContext, HDLExpression>map(context.dims, _function);
    HDLFunctionParameter _setDim = res.setDim(_map);
    res = _setDim;
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncParamWithRWContext context) {
    PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = context.psFuncParamType();
    IHDLObject _hDL = this.toHDL(_psFuncParamType);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    PSHDLLangParser.PsFuncParamRWTypeContext _psFuncParamRWType = context.psFuncParamRWType();
    boolean _tripleNotEquals = (_psFuncParamRWType != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsFuncParamRWTypeContext _psFuncParamRWType_1 = context.psFuncParamRWType();
      String _text = _psFuncParamRWType_1.getText();
      HDLFunctionParameter.RWType _op = HDLFunctionParameter.RWType.getOp(_text);
      HDLFunctionParameter _setRw = res.setRw(_op);
      res = _setRw;
    } else {
      HDLFunctionParameter _setRw_1 = res.setRw(HDLFunctionParameter.RWType.READ);
      res = _setRw_1;
    }
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLangParser.PsFuncParamTypeContext context) {
    HDLFunctionParameter _hDLFunctionParameter = new HDLFunctionParameter();
    HDLFunctionParameter res = _hDLFunctionParameter;
    final PSHDLLangParser.PsFuncParamTypeContext x = context;
    boolean _matched = false;
    if (!_matched) {
      TerminalNode _ANY_INT = x.ANY_INT();
      boolean _tripleNotEquals = (_ANY_INT != null);
      if (_tripleNotEquals) {
        _matched=true;
        HDLFunctionParameter _setType = res.setType(HDLFunctionParameter.Type.ANY_INT);
        res = _setType;
      }
    }
    if (!_matched) {
      TerminalNode _ANY_UINT = x.ANY_UINT();
      boolean _tripleNotEquals_1 = (_ANY_UINT != null);
      if (_tripleNotEquals_1) {
        _matched=true;
        HDLFunctionParameter _setType_1 = res.setType(HDLFunctionParameter.Type.ANY_UINT);
        res = _setType_1;
      }
    }
    if (!_matched) {
      TerminalNode _ANY_BIT = x.ANY_BIT();
      boolean _tripleNotEquals_2 = (_ANY_BIT != null);
      if (_tripleNotEquals_2) {
        _matched=true;
        HDLFunctionParameter _setType_2 = res.setType(HDLFunctionParameter.Type.ANY_BIT);
        res = _setType_2;
      }
    }
    if (!_matched) {
      TerminalNode _INT = x.INT();
      boolean _tripleNotEquals_3 = (_INT != null);
      if (_tripleNotEquals_3) {
        _matched=true;
        HDLFunctionParameter _setType_3 = res.setType(HDLFunctionParameter.Type.REG_INT);
        res = _setType_3;
      }
    }
    if (!_matched) {
      TerminalNode _UINT = x.UINT();
      boolean _tripleNotEquals_4 = (_UINT != null);
      if (_tripleNotEquals_4) {
        _matched=true;
        HDLFunctionParameter _setType_4 = res.setType(HDLFunctionParameter.Type.REG_UINT);
        res = _setType_4;
      }
    }
    if (!_matched) {
      TerminalNode _BIT = x.BIT();
      boolean _tripleNotEquals_5 = (_BIT != null);
      if (_tripleNotEquals_5) {
        _matched=true;
        HDLFunctionParameter _setType_5 = res.setType(HDLFunctionParameter.Type.REG_BIT);
        res = _setType_5;
      }
    }
    if (!_matched) {
      TerminalNode _BOOL = x.BOOL();
      boolean _tripleNotEquals_6 = (_BOOL != null);
      if (_tripleNotEquals_6) {
        _matched=true;
        HDLFunctionParameter _setType_6 = res.setType(HDLFunctionParameter.Type.BOOL_TYPE);
        res = _setType_6;
      }
    }
    if (!_matched) {
      TerminalNode _STRING = x.STRING();
      boolean _tripleNotEquals_7 = (_STRING != null);
      if (_tripleNotEquals_7) {
        _matched=true;
        HDLFunctionParameter _setType_7 = res.setType(HDLFunctionParameter.Type.STRING_TYPE);
        res = _setType_7;
      }
    }
    if (!_matched) {
      TerminalNode _ANY_IF = x.ANY_IF();
      boolean _tripleNotEquals_8 = (_ANY_IF != null);
      if (_tripleNotEquals_8) {
        _matched=true;
        HDLFunctionParameter _setType_8 = res.setType(HDLFunctionParameter.Type.ANY_IF);
        res = _setType_8;
      }
    }
    if (!_matched) {
      TerminalNode _ANY_ENUM = x.ANY_ENUM();
      boolean _tripleNotEquals_9 = (_ANY_ENUM != null);
      if (_tripleNotEquals_9) {
        _matched=true;
        HDLFunctionParameter _setType_9 = res.setType(HDLFunctionParameter.Type.ANY_ENUM);
        res = _setType_9;
      }
    }
    if (!_matched) {
      TerminalNode _INTERFACE = x.INTERFACE();
      boolean _tripleNotEquals_10 = (_INTERFACE != null);
      if (_tripleNotEquals_10) {
        _matched=true;
        HDLFunctionParameter _setType_10 = res.setType(HDLFunctionParameter.Type.IF);
        res = _setType_10;
        PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = x.psQualifiedName();
        HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
        HDLFunctionParameter _setIfSpec = res.setIfSpec(_fQNName);
        res = _setIfSpec;
      }
    }
    if (!_matched) {
      TerminalNode _ENUM = x.ENUM();
      boolean _tripleNotEquals_11 = (_ENUM != null);
      if (_tripleNotEquals_11) {
        _matched=true;
        HDLFunctionParameter _setType_11 = res.setType(HDLFunctionParameter.Type.ENUM);
        res = _setType_11;
        PSHDLLangParser.PsQualifiedNameContext _psQualifiedName_1 = x.psQualifiedName();
        HDLQualifiedName _fQNName_1 = this.toFQNName(_psQualifiedName_1);
        HDLFunctionParameter _setEnumSpec = res.setEnumSpec(_fQNName_1);
        res = _setEnumSpec;
      }
    }
    if (!_matched) {
      TerminalNode _FUNCTION = x.FUNCTION();
      boolean _tripleNotEquals_12 = (_FUNCTION != null);
      if (_tripleNotEquals_12) {
        _matched=true;
        HDLFunctionParameter _setType_12 = res.setType(HDLFunctionParameter.Type.FUNCTION);
        res = _setType_12;
        List<PSHDLLangParser.PsFuncParamWithRWContext> _psFuncParamWithRW = x.psFuncParamWithRW();
        final Function1<PSHDLLangParser.PsFuncParamWithRWContext,HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncParamWithRWContext,HDLFunctionParameter>() {
          public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncParamWithRWContext it) {
            IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
            return ((HDLFunctionParameter) _hDL);
          }
        };
        List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncParamWithRWContext, HDLFunctionParameter>map(_psFuncParamWithRW, _function);
        HDLFunctionParameter _setFuncSpec = res.setFuncSpec(_map);
        res = _setFuncSpec;
        PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType = x.psFuncParamType();
        boolean _tripleNotEquals_13 = (_psFuncParamType != null);
        if (_tripleNotEquals_13) {
          PSHDLLangParser.PsFuncParamTypeContext _psFuncParamType_1 = x.psFuncParamType();
          IHDLObject _hDL = this.toHDL(_psFuncParamType_1);
          HDLFunctionParameter _setFuncReturnSpec = res.setFuncReturnSpec(((HDLFunctionParameter) _hDL));
          res = _setFuncReturnSpec;
        }
      }
    }
    PSHDLLangParser.PsWidthContext _psWidth = context.psWidth();
    boolean _tripleNotEquals_14 = (_psWidth != null);
    if (_tripleNotEquals_14) {
      PSHDLLangParser.PsWidthContext _psWidth_1 = context.psWidth();
      IHDLObject _hDL_1 = this.toHDL(_psWidth_1);
      HDLFunctionParameter _setContainer = res.setContainer(((HDLExpression) _hDL_1));
      res = _setContainer;
    }
    return res;
  }
  
  protected HDLInlineFunction _toHDL(final PSHDLLangParser.PsInlineFunctionContext context) {
    HDLInlineFunction _hDLInlineFunction = new HDLInlineFunction();
    HDLInlineFunction func = _hDLInlineFunction;
    PSHDLLangParser.PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLInlineFunction _setName = func.setName(_name);
    func = _setName;
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLInlineFunction _setExpr = func.setExpr(((HDLExpression) _hDL));
    func = _setExpr;
    PSHDLLangParser.PsFuncParamContext _psFuncParam = context.psFuncParam();
    List<PSHDLLangParser.PsFuncSpecContext> _psFuncSpec = _psFuncParam.psFuncSpec();
    final Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter> _function = new Function1<PSHDLLangParser.PsFuncSpecContext,HDLFunctionParameter>() {
      public HDLFunctionParameter apply(final PSHDLLangParser.PsFuncSpecContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLFunctionParameter) _hDL);
      }
    };
    List<HDLFunctionParameter> _map = ListExtensions.<PSHDLLangParser.PsFuncSpecContext, HDLFunctionParameter>map(_psFuncSpec, _function);
    HDLInlineFunction _setArgs = func.setArgs(_map);
    func = _setArgs;
    PSHDLLangParser.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
    IHDLObject _hDL_1 = this.toHDL(_psFuncRecturnType);
    HDLInlineFunction _setReturnType = func.setReturnType(((HDLFunctionParameter) _hDL_1));
    func = _setReturnType;
    return this.<HDLInlineFunction>attachContext(func, context);
  }
  
  public String toName(final PSHDLLangParser.PsFunctionContext context) {
    TerminalNode _RULE_ID = context.RULE_ID();
    return _RULE_ID.getText();
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsFunctionDeclarationContext context) {
    PSHDLLangParser.PsInlineFunctionContext _psInlineFunction = context.psInlineFunction();
    boolean _tripleNotEquals = (_psInlineFunction != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsInlineFunctionContext _psInlineFunction_1 = context.psInlineFunction();
      IHDLObject _hDL = this.toHDL(_psInlineFunction_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsNativeFunctionContext _psNativeFunction = context.psNativeFunction();
    boolean _tripleNotEquals_1 = (_psNativeFunction != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsNativeFunctionContext _psNativeFunction_1 = context.psNativeFunction();
      IHDLObject _hDL_1 = this.toHDL(_psNativeFunction_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PSHDLLangParser.PsSubstituteFunctionContext _psSubstituteFunction = context.psSubstituteFunction();
    boolean _tripleNotEquals_2 = (_psSubstituteFunction != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsSubstituteFunctionContext _psSubstituteFunction_1 = context.psSubstituteFunction();
      IHDLObject _hDL_2 = this.toHDL(_psSubstituteFunction_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends PSHDLLangParser.PsFunctionDeclarationContext> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected HDLUnresolvedFragment _toHDL(final PSHDLLangParser.PsRefPartContext context) {
    HDLUnresolvedFragment frag = null;
    PSHDLLangParser.PsFuncArgsContext _psFuncArgs = context.psFuncArgs();
    boolean _tripleNotEquals = (_psFuncArgs != null);
    if (_tripleNotEquals) {
      HDLUnresolvedFragmentFunction _hDLUnresolvedFragmentFunction = new HDLUnresolvedFragmentFunction();
      TerminalNode _RULE_ID = context.RULE_ID();
      String _text = _RULE_ID.getText();
      HDLUnresolvedFragmentFunction uff = _hDLUnresolvedFragmentFunction.setFrag(_text);
      PSHDLLangParser.PsFuncArgsContext _psFuncArgs_1 = context.psFuncArgs();
      List<PSHDLLangParser.PsExpressionContext> _psExpression = _psFuncArgs_1.psExpression();
      final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
        public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLExpression) _hDL);
        }
      };
      List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression, _function);
      HDLUnresolvedFragmentFunction _setParams = uff.setParams(_map);
      frag = _setParams;
    } else {
      HDLUnresolvedFragment _hDLUnresolvedFragment = new HDLUnresolvedFragment();
      TerminalNode _RULE_ID_1 = context.RULE_ID();
      String _text_1 = _RULE_ID_1.getText();
      HDLUnresolvedFragment _setFrag = _hDLUnresolvedFragment.setFrag(_text_1);
      frag = _setFrag;
      PSHDLLangParser.PsArrayContext _psArray = context.psArray();
      boolean _tripleNotEquals_1 = (_psArray != null);
      if (_tripleNotEquals_1) {
        PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
        List<PSHDLLangParser.PsExpressionContext> _psExpression_1 = _psArray_1.psExpression();
        final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function_1 = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
          public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
            IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
            return ((HDLExpression) _hDL);
          }
        };
        List<HDLExpression> _map_1 = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression_1, _function_1);
        HDLUnresolvedFragment _setArray = frag.setArray(_map_1);
        frag = _setArray;
      }
      PSHDLLangParser.PsBitAccessContext _psBitAccess = context.psBitAccess();
      boolean _tripleNotEquals_2 = (_psBitAccess != null);
      if (_tripleNotEquals_2) {
        PSHDLLangParser.PsBitAccessContext _psBitAccess_1 = context.psBitAccess();
        List<PSHDLLangParser.PsAccessRangeContext> _psAccessRange = _psBitAccess_1.psAccessRange();
        final Function1<PSHDLLangParser.PsAccessRangeContext,HDLRange> _function_2 = new Function1<PSHDLLangParser.PsAccessRangeContext,HDLRange>() {
          public HDLRange apply(final PSHDLLangParser.PsAccessRangeContext it) {
            IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
            return ((HDLRange) _hDL);
          }
        };
        List<HDLRange> _map_2 = ListExtensions.<PSHDLLangParser.PsAccessRangeContext, HDLRange>map(_psAccessRange, _function_2);
        HDLUnresolvedFragment _setBits = frag.setBits(_map_2);
        frag = _setBits;
      }
    }
    return this.<HDLUnresolvedFragment>attachContext(frag, context);
  }
  
  protected HDLReference _toHDL(final PSHDLLangParser.PsVariableRefContext context) {
    boolean _tripleNotEquals = (context.isClk != null);
    if (_tripleNotEquals) {
      HDLVariable _defaultClk = HDLRegisterConfig.defaultClk(true);
      HDLVariableRef _asHDLRef = _defaultClk.asHDLRef();
      return this.<HDLVariableRef>attachContext(_asHDLRef, context);
    }
    boolean _tripleNotEquals_1 = (context.isRst != null);
    if (_tripleNotEquals_1) {
      HDLVariable _defaultRst = HDLRegisterConfig.defaultRst(true);
      HDLVariableRef _asHDLRef_1 = _defaultRst.asHDLRef();
      return this.<HDLVariableRef>attachContext(_asHDLRef_1, context);
    }
    HDLUnresolvedFragment current = null;
    List<PSHDLLangParser.PsRefPartContext> _psRefPart = context.psRefPart();
    List<PSHDLLangParser.PsRefPartContext> _reverseView = ListExtensions.<PSHDLLangParser.PsRefPartContext>reverseView(_psRefPart);
    for (final PSHDLLangParser.PsRefPartContext sub : _reverseView) {
      {
        IHDLObject _hDL = this.toHDL(sub);
        HDLUnresolvedFragment frag = ((HDLUnresolvedFragment) _hDL);
        boolean _tripleNotEquals_2 = (current != null);
        if (_tripleNotEquals_2) {
          HDLUnresolvedFragment _setSub = frag.setSub(current);
          frag = _setSub;
        }
        current = frag;
      }
    }
    return this.<HDLUnresolvedFragment>attachContext(current, context);
  }
  
  protected HDLRange _toHDL(final PSHDLLangParser.PsAccessRangeContext context) {
    HDLRange _hDLRange = new HDLRange();
    IHDLObject _hDL = this.toHDL(context.from);
    HDLRange res = _hDLRange.setTo(((HDLExpression) _hDL));
    boolean _tripleNotEquals = (context.to != null);
    if (_tripleNotEquals) {
      IHDLObject _hDL_1 = this.toHDL(context.from);
      HDLRange _setFrom = res.setFrom(((HDLExpression) _hDL_1));
      IHDLObject _hDL_2 = this.toHDL(context.to);
      HDLRange _setTo = _setFrom.setTo(((HDLExpression) _hDL_2));
      res = _setTo;
    }
    boolean _tripleNotEquals_1 = (context.inc != null);
    if (_tripleNotEquals_1) {
      IHDLObject _hDL_3 = this.toHDL(context.from);
      HDLRange _setTo_1 = res.setTo(((HDLExpression) _hDL_3));
      IHDLObject _hDL_4 = this.toHDL(context.inc);
      HDLRange _setInc = _setTo_1.setInc(((HDLExpression) _hDL_4));
      res = _setInc;
    }
    boolean _tripleNotEquals_2 = (context.dec != null);
    if (_tripleNotEquals_2) {
      IHDLObject _hDL_5 = this.toHDL(context.from);
      HDLRange _setTo_2 = res.setTo(((HDLExpression) _hDL_5));
      IHDLObject _hDL_6 = this.toHDL(context.dec);
      HDLRange _setDec = _setTo_2.setDec(((HDLExpression) _hDL_6));
      res = _setDec;
    }
    return this.<HDLRange>attachContext(res, context);
  }
  
  protected HDLSwitchCaseStatement _toHDL(final PSHDLLangParser.PsCaseStatementsContext context) {
    HDLSwitchCaseStatement _hDLSwitchCaseStatement = new HDLSwitchCaseStatement();
    HDLSwitchCaseStatement hCase = _hDLSwitchCaseStatement;
    PSHDLLangParser.PsValueContext _psValue = context.psValue();
    boolean _tripleNotEquals = (_psValue != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsValueContext _psValue_1 = context.psValue();
      IHDLObject _hDL = this.toHDL(_psValue_1);
      HDLSwitchCaseStatement _setLabel = hCase.setLabel(((HDLExpression) _hDL));
      hCase = _setLabel;
    }
    List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
    final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock, _function);
    HDLSwitchCaseStatement _setDos = hCase.setDos(_map);
    hCase = _setDos;
    return this.<HDLSwitchCaseStatement>attachContext(hCase, context);
  }
  
  protected HDLSwitchStatement _toHDL(final PSHDLLangParser.PsSwitchStatementContext context) {
    HDLSwitchStatement _hDLSwitchStatement = new HDLSwitchStatement();
    PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
    IHDLObject _hDL = this.toHDL(_psVariableRef);
    HDLSwitchStatement switchStmnt = _hDLSwitchStatement.setCaseExp(((HDLExpression) _hDL));
    List<PSHDLLangParser.PsCaseStatementsContext> _psCaseStatements = context.psCaseStatements();
    final Function1<PSHDLLangParser.PsCaseStatementsContext,HDLSwitchCaseStatement> _function = new Function1<PSHDLLangParser.PsCaseStatementsContext,HDLSwitchCaseStatement>() {
      public HDLSwitchCaseStatement apply(final PSHDLLangParser.PsCaseStatementsContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLSwitchCaseStatement) _hDL);
      }
    };
    List<HDLSwitchCaseStatement> _map = ListExtensions.<PSHDLLangParser.PsCaseStatementsContext, HDLSwitchCaseStatement>map(_psCaseStatements, _function);
    HDLSwitchStatement _setCases = switchStmnt.setCases(_map);
    switchStmnt = _setCases;
    return this.<HDLSwitchStatement>attachContext(switchStmnt, context);
  }
  
  protected HDLInterfaceInstantiation _toHDL(final PSHDLLangParser.PsInterfaceInstantiationContext context) {
    PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL = this.toHDL(_psVariable);
    HDLVariable hVar = ((HDLVariable) _hDL);
    PSHDLLangParser.PsArrayContext _psArray = context.psArray();
    boolean _tripleNotEquals = (_psArray != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsArrayContext _psArray_1 = context.psArray();
      List<PSHDLLangParser.PsExpressionContext> _psExpression = _psArray_1.psExpression();
      final Function1<PSHDLLangParser.PsExpressionContext,HDLExpression> _function = new Function1<PSHDLLangParser.PsExpressionContext,HDLExpression>() {
        public HDLExpression apply(final PSHDLLangParser.PsExpressionContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLExpression) _hDL);
        }
      };
      List<HDLExpression> _map = ListExtensions.<PSHDLLangParser.PsExpressionContext, HDLExpression>map(_psExpression, _function);
      HDLVariable _setDimensions = hVar.setDimensions(_map);
      hVar = _setDimensions;
    }
    HDLInterfaceInstantiation _hDLInterfaceInstantiation = new HDLInterfaceInstantiation();
    HDLInterfaceInstantiation _setVar = _hDLInterfaceInstantiation.setVar(hVar);
    PSHDLLangParser.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
    HDLInterfaceInstantiation hii = _setVar.setHIf(_fQNName);
    PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _tripleNotEquals_1 = (_psPassedArguments != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
      List<PSHDLLangParser.PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
      final Function1<PSHDLLangParser.PsArgumentContext,HDLArgument> _function_1 = new Function1<PSHDLLangParser.PsArgumentContext,HDLArgument>() {
        public HDLArgument apply(final PSHDLLangParser.PsArgumentContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLArgument) _hDL);
        }
      };
      List<HDLArgument> _map_1 = ListExtensions.<PSHDLLangParser.PsArgumentContext, HDLArgument>map(_psArgument, _function_1);
      HDLInterfaceInstantiation _setArguments = hii.setArguments(_map_1);
      hii = _setArguments;
    }
    return this.<HDLInterfaceInstantiation>attachContext(hii, context);
  }
  
  protected HDLForLoop _toHDL(final PSHDLLangParser.PsForStatementContext context) {
    HDLForLoop _hDLForLoop = new HDLForLoop();
    PSHDLLangParser.PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL = this.toHDL(_psVariable);
    HDLForLoop loop = _hDLForLoop.setParam(((HDLVariable) _hDL));
    PSHDLLangParser.PsBitAccessContext _psBitAccess = context.psBitAccess();
    List<PSHDLLangParser.PsAccessRangeContext> _psAccessRange = _psBitAccess.psAccessRange();
    final Function1<PSHDLLangParser.PsAccessRangeContext,HDLRange> _function = new Function1<PSHDLLangParser.PsAccessRangeContext,HDLRange>() {
      public HDLRange apply(final PSHDLLangParser.PsAccessRangeContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLRange) _hDL);
      }
    };
    List<HDLRange> _map = ListExtensions.<PSHDLLangParser.PsAccessRangeContext, HDLRange>map(_psAccessRange, _function);
    HDLForLoop _setRange = loop.setRange(_map);
    loop = _setRange;
    PSHDLLangParser.PsSimpleBlockContext _psSimpleBlock = context.psSimpleBlock();
    List<PSHDLLangParser.PsBlockContext> _psBlock = _psSimpleBlock.psBlock();
    final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function_1 = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map_1 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock, _function_1);
    HDLForLoop _setDos = loop.setDos(_map_1);
    loop = _setDos;
    return this.<HDLForLoop>attachContext(loop, context);
  }
  
  protected HDLIfStatement _toHDL(final PSHDLLangParser.PsIfStatementContext context) {
    HDLIfStatement _hDLIfStatement = new HDLIfStatement();
    PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLIfStatement res = _hDLIfStatement.setIfExp(((HDLExpression) _hDL));
    List<PSHDLLangParser.PsBlockContext> _psBlock = context.ifBlk.psBlock();
    final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock, _function);
    HDLIfStatement _setThenDo = res.setThenDo(_map);
    res = _setThenDo;
    boolean _tripleNotEquals = (context.elseBlk != null);
    if (_tripleNotEquals) {
      List<PSHDLLangParser.PsBlockContext> _psBlock_1 = context.elseBlk.psBlock();
      final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function_1 = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
        public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
          IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
          return ((HDLStatement) _hDL);
        }
      };
      List<HDLStatement> _map_1 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock_1, _function_1);
      HDLIfStatement _setElseDo = res.setElseDo(_map_1);
      res = _setElseDo;
    }
    return this.<HDLIfStatement>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsCompoundStatementContext context) {
    PSHDLLangParser.PsForStatementContext _psForStatement = context.psForStatement();
    boolean _tripleNotEquals = (_psForStatement != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsForStatementContext _psForStatement_1 = context.psForStatement();
      IHDLObject _hDL = this.toHDL(_psForStatement_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsIfStatementContext _psIfStatement = context.psIfStatement();
    boolean _tripleNotEquals_1 = (_psIfStatement != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsIfStatementContext _psIfStatement_1 = context.psIfStatement();
      IHDLObject _hDL_1 = this.toHDL(_psIfStatement_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PSHDLLangParser.PsSwitchStatementContext _psSwitchStatement = context.psSwitchStatement();
    boolean _tripleNotEquals_2 = (_psSwitchStatement != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsSwitchStatementContext _psSwitchStatement_1 = context.psSwitchStatement();
      IHDLObject _hDL_2 = this.toHDL(_psSwitchStatement_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends PSHDLLangParser.PsCompoundStatementContext> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsStatementContext context) {
    PSHDLLangParser.PsAssignmentOrFuncContext _psAssignmentOrFunc = context.psAssignmentOrFunc();
    boolean _tripleNotEquals = (_psAssignmentOrFunc != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsAssignmentOrFuncContext _psAssignmentOrFunc_1 = context.psAssignmentOrFunc();
      IHDLObject _hDL = this.toHDL(_psAssignmentOrFunc_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PSHDLLangParser.PsCompoundStatementContext _psCompoundStatement = context.psCompoundStatement();
    boolean _tripleNotEquals_1 = (_psCompoundStatement != null);
    if (_tripleNotEquals_1) {
      PSHDLLangParser.PsCompoundStatementContext _psCompoundStatement_1 = context.psCompoundStatement();
      IHDLObject _hDL_1 = this.toHDL(_psCompoundStatement_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PSHDLLangParser.PsProcessContext _psProcess = context.psProcess();
    boolean _tripleNotEquals_2 = (_psProcess != null);
    if (_tripleNotEquals_2) {
      PSHDLLangParser.PsProcessContext _psProcess_1 = context.psProcess();
      IHDLObject _hDL_2 = this.toHDL(_psProcess_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends PSHDLLangParser.PsStatementContext> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PSHDLLangParser.PsAssignmentOrFuncContext context) {
    PSHDLLangParser.PsVariableRefContext _psVariableRef = context.psVariableRef();
    IHDLObject _hDL = this.toHDL(_psVariableRef);
    final HDLReference hVar = ((HDLReference) _hDL);
    PSHDLLangParser.PsAssignmentOpContext _psAssignmentOp = context.psAssignmentOp();
    boolean _tripleNotEquals = (_psAssignmentOp != null);
    if (_tripleNotEquals) {
      PSHDLLangParser.PsAssignmentOpContext _psAssignmentOp_1 = context.psAssignmentOp();
      String _text = _psAssignmentOp_1.getText();
      final HDLAssignment.HDLAssignmentType type = HDLAssignment.HDLAssignmentType.getOp(_text);
      HDLAssignment _hDLAssignment = new HDLAssignment();
      HDLAssignment _setLeft = _hDLAssignment.setLeft(hVar);
      HDLAssignment ass = _setLeft.setType(type);
      PSHDLLangParser.PsExpressionContext _psExpression = context.psExpression();
      IHDLObject _hDL_1 = this.toHDL(_psExpression);
      HDLAssignment _setRight = ass.setRight(((HDLExpression) _hDL_1));
      ass = _setRight;
      return this.<HDLAssignment>attachContext(ass, context);
    }
    return this.<HDLReference>attachContext(hVar, context);
  }
  
  protected IHDLObject _toHDL(final Object context) {
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public HDLUnit toHDLUnit(final PSHDLLangParser.PsUnitContext context, final String libURI) {
    HDLUnit _hDLUnit = new HDLUnit();
    PSHDLLangParser.PsInterfaceContext _psInterface = context.psInterface();
    String _name = this.toName(_psInterface);
    HDLUnit _setName = _hDLUnit.setName(_name);
    HDLUnit unit = _setName.setLibURI(libURI);
    int _type = context.unitType.getType();
    boolean _equals = (_type == PSHDLLangLexer.TESTBENCH);
    HDLUnit _setSimulation = unit.setSimulation(_equals);
    unit = _setSimulation;
    List<PSHDLLangParser.PsAnnotationContext> _psAnnotation = context.psAnnotation();
    final Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation> _function = new Function1<PSHDLLangParser.PsAnnotationContext,HDLAnnotation>() {
      public HDLAnnotation apply(final PSHDLLangParser.PsAnnotationContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLAnnotation) _hDL);
      }
    };
    List<HDLAnnotation> _map = ListExtensions.<PSHDLLangParser.PsAnnotationContext, HDLAnnotation>map(_psAnnotation, _function);
    HDLUnit _setAnnotations = unit.setAnnotations(_map);
    unit = _setAnnotations;
    List<PSHDLLangParser.PsImportsContext> _psImports = context.psImports();
    final Function1<PSHDLLangParser.PsImportsContext,String> _function_1 = new Function1<PSHDLLangParser.PsImportsContext,String>() {
      public String apply(final PSHDLLangParser.PsImportsContext it) {
        String _name = ParserToModelExtension.this.toName(it);
        return _name;
      }
    };
    List<String> _map_1 = ListExtensions.<PSHDLLangParser.PsImportsContext, String>map(_psImports, _function_1);
    HDLUnit _setImports = unit.setImports(_map_1);
    unit = _setImports;
    List<PSHDLLangParser.PsBlockContext> _psBlock = context.psBlock();
    final Function1<PSHDLLangParser.PsBlockContext,HDLStatement> _function_2 = new Function1<PSHDLLangParser.PsBlockContext,HDLStatement>() {
      public HDLStatement apply(final PSHDLLangParser.PsBlockContext it) {
        IHDLObject _hDL = ParserToModelExtension.this.toHDL(it);
        return ((HDLStatement) _hDL);
      }
    };
    List<HDLStatement> _map_2 = ListExtensions.<PSHDLLangParser.PsBlockContext, HDLStatement>map(_psBlock, _function_2);
    HDLUnit _setStatements = unit.setStatements(_map_2);
    unit = _setStatements;
    return this.<HDLUnit>attachContext(unit, context);
  }
  
  public String toName(final PSHDLLangParser.PsImportsContext context) {
    PSHDLLangParser.PsQualifiedNameImportContext _psQualifiedNameImport = context.psQualifiedNameImport();
    return _psQualifiedNameImport.getText();
  }
  
  public IHDLObject toHDL(final Object context) {
    if (context instanceof PSHDLLangParser.PsAddContext) {
      return _toHDL((PSHDLLangParser.PsAddContext)context);
    } else if (context instanceof PSHDLLangParser.PsArrayInitExpContext) {
      return _toHDL((PSHDLLangParser.PsArrayInitExpContext)context);
    } else if (context instanceof PSHDLLangParser.PsBitAndContext) {
      return _toHDL((PSHDLLangParser.PsBitAndContext)context);
    } else if (context instanceof PSHDLLangParser.PsBitLogAndContext) {
      return _toHDL((PSHDLLangParser.PsBitLogAndContext)context);
    } else if (context instanceof PSHDLLangParser.PsBitLogOrContext) {
      return _toHDL((PSHDLLangParser.PsBitLogOrContext)context);
    } else if (context instanceof PSHDLLangParser.PsBitOrContext) {
      return _toHDL((PSHDLLangParser.PsBitOrContext)context);
    } else if (context instanceof PSHDLLangParser.PsBitXorContext) {
      return _toHDL((PSHDLLangParser.PsBitXorContext)context);
    } else if (context instanceof PSHDLLangParser.PsConcatContext) {
      return _toHDL((PSHDLLangParser.PsConcatContext)context);
    } else if (context instanceof PSHDLLangParser.PsEqualityCompContext) {
      return _toHDL((PSHDLLangParser.PsEqualityCompContext)context);
    } else if (context instanceof PSHDLLangParser.PsEqualityContext) {
      return _toHDL((PSHDLLangParser.PsEqualityContext)context);
    } else if (context instanceof PSHDLLangParser.PsManipContext) {
      return _toHDL((PSHDLLangParser.PsManipContext)context);
    } else if (context instanceof PSHDLLangParser.PsMulContext) {
      return _toHDL((PSHDLLangParser.PsMulContext)context);
    } else if (context instanceof PSHDLLangParser.PsParensContext) {
      return _toHDL((PSHDLLangParser.PsParensContext)context);
    } else if (context instanceof PSHDLLangParser.PsShiftContext) {
      return _toHDL((PSHDLLangParser.PsShiftContext)context);
    } else if (context instanceof PSHDLLangParser.PsTernaryContext) {
      return _toHDL((PSHDLLangParser.PsTernaryContext)context);
    } else if (context instanceof PSHDLLangParser.PsValueExpContext) {
      return _toHDL((PSHDLLangParser.PsValueExpContext)context);
    } else if (context instanceof PSHDLLangParser.PsAccessRangeContext) {
      return _toHDL((PSHDLLangParser.PsAccessRangeContext)context);
    } else if (context instanceof PSHDLLangParser.PsAnnotationContext) {
      return _toHDL((PSHDLLangParser.PsAnnotationContext)context);
    } else if (context instanceof PSHDLLangParser.PsArgumentContext) {
      return _toHDL((PSHDLLangParser.PsArgumentContext)context);
    } else if (context instanceof PSHDLLangParser.PsArrayInitContext) {
      return _toHDL((PSHDLLangParser.PsArrayInitContext)context);
    } else if (context instanceof PSHDLLangParser.PsArrayInitSubContext) {
      return _toHDL((PSHDLLangParser.PsArrayInitSubContext)context);
    } else if (context instanceof PSHDLLangParser.PsArrayInitSubParensContext) {
      return _toHDL((PSHDLLangParser.PsArrayInitSubParensContext)context);
    } else if (context instanceof PSHDLLangParser.PsAssignmentOrFuncContext) {
      return _toHDL((PSHDLLangParser.PsAssignmentOrFuncContext)context);
    } else if (context instanceof PSHDLLangParser.PsBlockContext) {
      return _toHDL((PSHDLLangParser.PsBlockContext)context);
    } else if (context instanceof PSHDLLangParser.PsCaseStatementsContext) {
      return _toHDL((PSHDLLangParser.PsCaseStatementsContext)context);
    } else if (context instanceof PSHDLLangParser.PsCastContext) {
      return _toHDL((PSHDLLangParser.PsCastContext)context);
    } else if (context instanceof PSHDLLangParser.PsCompoundStatementContext) {
      return _toHDL((PSHDLLangParser.PsCompoundStatementContext)context);
    } else if (context instanceof PSHDLLangParser.PsDeclAssignmentContext) {
      return _toHDL((PSHDLLangParser.PsDeclAssignmentContext)context);
    } else if (context instanceof PSHDLLangParser.PsDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsDeclarationTypeContext) {
      return _toHDL((PSHDLLangParser.PsDeclarationTypeContext)context);
    } else if (context instanceof PSHDLLangParser.PsDirectGenerationContext) {
      return _toHDL((PSHDLLangParser.PsDirectGenerationContext)context);
    } else if (context instanceof PSHDLLangParser.PsEnumContext) {
      return _toHDL((PSHDLLangParser.PsEnumContext)context);
    } else if (context instanceof PSHDLLangParser.PsEnumDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsEnumDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsExpressionContext) {
      return _toHDL((PSHDLLangParser.PsExpressionContext)context);
    } else if (context instanceof PSHDLLangParser.PsForStatementContext) {
      return _toHDL((PSHDLLangParser.PsForStatementContext)context);
    } else if (context instanceof PSHDLLangParser.PsFuncParamTypeContext) {
      return _toHDL((PSHDLLangParser.PsFuncParamTypeContext)context);
    } else if (context instanceof PSHDLLangParser.PsFuncParamWithRWContext) {
      return _toHDL((PSHDLLangParser.PsFuncParamWithRWContext)context);
    } else if (context instanceof PSHDLLangParser.PsFuncRecturnTypeContext) {
      return _toHDL((PSHDLLangParser.PsFuncRecturnTypeContext)context);
    } else if (context instanceof PSHDLLangParser.PsFuncSpecContext) {
      return _toHDL((PSHDLLangParser.PsFuncSpecContext)context);
    } else if (context instanceof PSHDLLangParser.PsFunctionDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsFunctionDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsIfStatementContext) {
      return _toHDL((PSHDLLangParser.PsIfStatementContext)context);
    } else if (context instanceof PSHDLLangParser.PsInlineFunctionContext) {
      return _toHDL((PSHDLLangParser.PsInlineFunctionContext)context);
    } else if (context instanceof PSHDLLangParser.PsInstantiationContext) {
      return _toHDL((PSHDLLangParser.PsInstantiationContext)context);
    } else if (context instanceof PSHDLLangParser.PsInterfaceContext) {
      return _toHDL((PSHDLLangParser.PsInterfaceContext)context);
    } else if (context instanceof PSHDLLangParser.PsInterfaceDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsInterfaceDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsInterfaceInstantiationContext) {
      return _toHDL((PSHDLLangParser.PsInterfaceInstantiationContext)context);
    } else if (context instanceof PSHDLLangParser.PsNativeFunctionContext) {
      return _toHDL((PSHDLLangParser.PsNativeFunctionContext)context);
    } else if (context instanceof PSHDLLangParser.PsPortDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsPortDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsPrimitiveContext) {
      return _toHDL((PSHDLLangParser.PsPrimitiveContext)context);
    } else if (context instanceof PSHDLLangParser.PsProcessContext) {
      return _toHDL((PSHDLLangParser.PsProcessContext)context);
    } else if (context instanceof PSHDLLangParser.PsRefPartContext) {
      return _toHDL((PSHDLLangParser.PsRefPartContext)context);
    } else if (context instanceof PSHDLLangParser.PsStatementContext) {
      return _toHDL((PSHDLLangParser.PsStatementContext)context);
    } else if (context instanceof PSHDLLangParser.PsSubstituteFunctionContext) {
      return _toHDL((PSHDLLangParser.PsSubstituteFunctionContext)context);
    } else if (context instanceof PSHDLLangParser.PsSwitchStatementContext) {
      return _toHDL((PSHDLLangParser.PsSwitchStatementContext)context);
    } else if (context instanceof PSHDLLangParser.PsTypeDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsTypeDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsValueContext) {
      return _toHDL((PSHDLLangParser.PsValueContext)context);
    } else if (context instanceof PSHDLLangParser.PsVariableContext) {
      return _toHDL((PSHDLLangParser.PsVariableContext)context);
    } else if (context instanceof PSHDLLangParser.PsVariableDeclarationContext) {
      return _toHDL((PSHDLLangParser.PsVariableDeclarationContext)context);
    } else if (context instanceof PSHDLLangParser.PsVariableRefContext) {
      return _toHDL((PSHDLLangParser.PsVariableRefContext)context);
    } else if (context instanceof PSHDLLangParser.PsWidthContext) {
      return _toHDL((PSHDLLangParser.PsWidthContext)context);
    } else if (context != null) {
      return _toHDL(context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(context).toString());
    }
  }
}
