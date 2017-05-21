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
import org.pshdl.model.HDLExport;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInstantiation;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLObject;
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
import org.pshdl.model.parser.PSHDLLang;
import org.pshdl.model.parser.PSHDLLangLexer;
import org.pshdl.model.parser.SourceInfo;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLQualifiedName;

@SuppressWarnings("all")
public class ParserToModelExtension {
  private BufferedTokenStream tokens;
  
  public ParserToModelExtension(final BufferedTokenStream tokens) {
    this.tokens = tokens;
  }
  
  public static HDLPackage toHDL(final BufferedTokenStream tokens, final PSHDLLang.PsModelContext ctx, final String libURI, final String src) {
    return new ParserToModelExtension(tokens).toHDLPkg(ctx, libURI, src);
  }
  
  public static HDLExpression toHDLExpression(final BufferedTokenStream tokens, final PSHDLLang.PsExpressionContext ctx) {
    IHDLObject _hDL = new ParserToModelExtension(tokens).toHDL(ctx, false);
    return ((HDLExpression) _hDL);
  }
  
  public HDLPackage toHDLPkg(final PSHDLLang.PsModelContext ctx, final String libURI, final String src) {
    HDLPackage pkg = new HDLPackage().setLibURI(libURI);
    PSHDLLang.PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
    boolean _tripleNotEquals = (_psQualifiedName != null);
    if (_tripleNotEquals) {
      pkg = pkg.setPkg(this.toName(ctx.psQualifiedName()));
    }
    final Function1<PSHDLLang.PsUnitContext, HDLUnit> _function = (PSHDLLang.PsUnitContext it) -> {
      return this.toHDLUnit(it, libURI);
    };
    pkg = pkg.setUnits(ListExtensions.<PSHDLLang.PsUnitContext, HDLUnit>map(ctx.psUnit(), _function));
    final Function1<PSHDLLang.PsDeclarationContext, HDLDeclaration> _function_1 = (PSHDLLang.PsDeclarationContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLDeclaration) _hDL);
    };
    pkg = pkg.setDeclarations(ListExtensions.<PSHDLLang.PsDeclarationContext, HDLDeclaration>map(ctx.psDeclaration(), _function_1));
    pkg.freeze(null);
    final HDLLibrary library = HDLLibrary.getLibrary(libURI);
    if ((library == null)) {
      throw new IllegalArgumentException((("The library " + libURI) + " is not valid"));
    }
    library.addPkg(pkg, src);
    HDLPackage _attachContext = this.<HDLPackage>attachContext(pkg, ctx);
    return ((HDLPackage) _attachContext);
  }
  
  protected HDLDeclaration _toHDL(final PSHDLLang.PsDeclarationContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psDeclarationType(), isStatement);
    HDLDeclaration res = ((HDLDeclaration) _hDL);
    final Function1<PSHDLLang.PsAnnotationContext, HDLAnnotation> _function = (PSHDLLang.PsAnnotationContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, false);
      return ((HDLAnnotation) _hDL_1);
    };
    res = res.setAnnotations(ListExtensions.<PSHDLLang.PsAnnotationContext, HDLAnnotation>map(context.psAnnotation(), _function));
    return this.<HDLDeclaration>attachContext(res, context);
  }
  
  public <T extends IHDLObject> T attachContext(final T obj, final ParserRuleContext context) {
    if ((obj == null)) {
      throw new NullPointerException("Null is not allowed");
    }
    SourceInfo _sourceInfo = new SourceInfo(this.tokens, context);
    obj.<SourceInfo>addMeta(SourceInfo.INFO, _sourceInfo);
    return obj;
  }
  
  protected HDLArgument _toHDL(final PSHDLLang.PsArgumentContext context, final boolean isStatement) {
    HDLArgument res = new HDLArgument().setName(context.RULE_ID().getText());
    IHDLObject _hDL = this.toHDL(context.psExpression(), false);
    res = res.setExpression(((HDLExpression) _hDL));
    return this.<HDLArgument>attachContext(res, context);
  }
  
  protected HDLBlock _toHDL(final PSHDLLang.PsProcessContext context, final boolean isStatement) {
    HDLBlock block = new HDLBlock();
    if ((context.isProcess != null)) {
      block = block.setProcess(true);
    }
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLStatement) _hDL);
    };
    block = block.setStatements(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psBlock(), _function));
    return this.<HDLBlock>attachContext(block, context);
  }
  
  protected HDLAnnotation _toHDL(final PSHDLLang.PsAnnotationContext context, final boolean isStatement) {
    final String name = context.psAnnotationType().getText();
    String value = null;
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _tripleNotEquals = (_RULE_STRING != null);
    if (_tripleNotEquals) {
      String str = context.RULE_STRING().getText();
      int _length = str.length();
      int _minus = (_length - 1);
      str = str.substring(1, _minus);
      value = str;
    }
    return this.<HDLAnnotation>attachContext(new HDLAnnotation().setName(name).setValue(value), context);
  }
  
  protected HDLDeclaration _toHDL(final PSHDLLang.PsDeclarationTypeContext context, final boolean isStatement) {
    PSHDLLang.PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
    boolean _tripleNotEquals = (_psFunctionDeclaration != null);
    if (_tripleNotEquals) {
      IHDLObject _attachContext = this.<IHDLObject>attachContext(this.toHDL(context.psFunctionDeclaration(), true), context);
      return ((HDLDeclaration) _attachContext);
    }
    PSHDLLang.PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
    boolean _tripleNotEquals_1 = (_psTypeDeclaration != null);
    if (_tripleNotEquals_1) {
      IHDLObject _attachContext_1 = this.<IHDLObject>attachContext(this.toHDL(context.psTypeDeclaration(), true), context);
      return ((HDLDeclaration) _attachContext_1);
    }
    PSHDLLang.PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    boolean _tripleNotEquals_2 = (_psVariableDeclaration != null);
    if (_tripleNotEquals_2) {
      IHDLObject _attachContext_2 = this.<IHDLObject>attachContext(this.toHDL(context.psVariableDeclaration(), true), context);
      return ((HDLDeclaration) _attachContext_2);
    }
    Class<? extends PSHDLLang.PsDeclarationTypeContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected HDLVariableDeclaration _toHDL(final PSHDLLang.PsVariableDeclarationContext context, final boolean isStatement) {
    HDLVariableDeclaration res = new HDLVariableDeclaration();
    IHDLObject _hDL = this.toHDL(context.psPrimitive(), false);
    res = res.setType(((HDLType) _hDL));
    HDLVariableDeclaration.HDLDirection dir = HDLVariableDeclaration.HDLDirection.INTERNAL;
    PSHDLLang.PsDirectionContext _psDirection = context.psDirection();
    boolean _tripleNotEquals = (_psDirection != null);
    if (_tripleNotEquals) {
      dir = HDLVariableDeclaration.HDLDirection.getOp(context.psDirection().getText());
    }
    res = res.setDirection(dir);
    List<PSHDLLang.PsDeclAssignmentContext> _psDeclAssignment = context.psDeclAssignment();
    for (final PSHDLLang.PsDeclAssignmentContext varDecl : _psDeclAssignment) {
      IHDLObject _hDL_1 = this.toHDL(varDecl, false);
      res = res.addVariables(((HDLVariable) _hDL_1));
    }
    if ((context.psPrimitive().isRegister != null)) {
      Iterable<HDLArgument> args = new ArrayList<HDLArgument>();
      PSHDLLang.PsPassedArgumentsContext _psPassedArguments = context.psPrimitive().psPassedArguments();
      boolean _tripleNotEquals_1 = (_psPassedArguments != null);
      if (_tripleNotEquals_1) {
        final Function1<PSHDLLang.PsArgumentContext, HDLArgument> _function = (PSHDLLang.PsArgumentContext it) -> {
          IHDLObject _hDL_2 = this.toHDL(it, false);
          return ((HDLArgument) _hDL_2);
        };
        args = ListExtensions.<PSHDLLang.PsArgumentContext, HDLArgument>map(context.psPrimitive().psPassedArguments().psArgument(), _function);
      }
      res = res.setRegister(HDLRegisterConfig.fromArgs(args));
    }
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsArrayInitContext context, final boolean isStatement) {
    PSHDLLang.PsExpressionContext _psExpression = context.psExpression();
    boolean _tripleNotEquals = (_psExpression != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psExpression(), false), context);
    }
    return this.toHDL(context.psArrayInitSubParens(), false);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsArrayInitExpContext context, final boolean isStatement) {
    return this.toHDL(context.psArrayInitSubParens(), false);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsArrayInitSubContext context, final boolean isStatement) {
    List<PSHDLLang.PsExpressionContext> _psExpression = context.psExpression();
    boolean _tripleNotEquals = (_psExpression != null);
    if (_tripleNotEquals) {
      final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function = (PSHDLLang.PsExpressionContext it) -> {
        IHDLObject _hDL = this.toHDL(it, isStatement);
        return ((HDLExpression) _hDL);
      };
      final HDLArrayInit arr = new HDLArrayInit().setExp(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psExpression(), _function));
      return this.<HDLArrayInit>attachContext(arr, context);
    }
    return this.toHDL(context.psArrayInitSubParens(), false);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsArrayInitSubParensContext context, final boolean isStatement) {
    return this.<IHDLObject>attachContext(this.toHDL(context.psArrayInitSub(), false), context);
  }
  
  public final static HDLObject.GenericMeta<Boolean> isDeprecatedDeclaration = new HDLObject.GenericMeta<Boolean>("isDeprecatedDeclaration", true);
  
  protected HDLType _toHDL(final PSHDLLang.PsPrimitiveContext context, final boolean isStatement) {
    PSHDLLang.PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    boolean _tripleNotEquals = (_psQualifiedName != null);
    if (_tripleNotEquals) {
      return this.<HDLEnum>attachContext(new HDLEnum().setName(this.toName(context.psQualifiedName())), context);
    }
    final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.getOp(context.psPrimitiveType().getText());
    PSHDLLang.PsWidthContext _psWidth = context.psWidth();
    IHDLObject _hDL = null;
    if (_psWidth!=null) {
      _hDL=this.toHDL(_psWidth, false);
    }
    final HDLExpression width = ((HDLExpression) _hDL);
    final HDLPrimitive result = this.<HDLPrimitive>attachContext(new HDLPrimitive().setType(this.getResultingType(pt, width)).setWidth(width), context);
    if (((pt == HDLPrimitive.HDLPrimitiveType.INT) && (width == null))) {
      result.setMeta(ParserToModelExtension.isDeprecatedDeclaration);
    }
    if (((pt == HDLPrimitive.HDLPrimitiveType.UINT) && (width == null))) {
      result.setMeta(ParserToModelExtension.isDeprecatedDeclaration);
    }
    return result;
  }
  
  protected HDLVariable _toHDL(final PSHDLLang.PsDeclAssignmentContext context, final boolean isStatement) {
    HDLVariable res = new HDLVariable().setName(this.toName(context.psVariable()));
    final Function1<PSHDLLang.PsAnnotationContext, HDLAnnotation> _function = (PSHDLLang.PsAnnotationContext it) -> {
      IHDLObject _hDL = this.toHDL(it, false);
      return ((HDLAnnotation) _hDL);
    };
    res = res.setAnnotations(ListExtensions.<PSHDLLang.PsAnnotationContext, HDLAnnotation>map(context.psAnnotation(), _function));
    PSHDLLang.PsArrayContext _psArray = context.psArray();
    boolean _tripleNotEquals = (_psArray != null);
    if (_tripleNotEquals) {
      final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function_1 = (PSHDLLang.PsExpressionContext it) -> {
        IHDLObject _hDL = this.toHDL(it, false);
        return ((HDLExpression) _hDL);
      };
      res = res.setDimensions(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psArray().psExpression(), _function_1));
    }
    PSHDLLang.PsArrayInitContext _psArrayInit = context.psArrayInit();
    boolean _tripleNotEquals_1 = (_psArrayInit != null);
    if (_tripleNotEquals_1) {
      IHDLObject _hDL = this.toHDL(context.psArrayInit(), false);
      res = res.setDefaultValue(((HDLExpression) _hDL));
    }
    return this.<HDLVariable>attachContext(res, context);
  }
  
  public String toName(final PSHDLLang.PsVariableContext context) {
    return context.RULE_ID().getText();
  }
  
  public HDLPrimitive.HDLPrimitiveType getResultingType(final HDLPrimitive.HDLPrimitiveType pt, final HDLExpression width) {
    if ((width != null)) {
      if (pt != null) {
        switch (pt) {
          case BIT:
            return HDLPrimitive.HDLPrimitiveType.BITVECTOR;
          default:
            break;
        }
      }
    } else {
      if (pt != null) {
        switch (pt) {
          case INT:
            return HDLPrimitive.HDLPrimitiveType.INTEGER;
          case UINT:
            return HDLPrimitive.HDLPrimitiveType.NATURAL;
          default:
            break;
        }
      }
    }
    return pt;
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsWidthContext context, final boolean isStatement) {
    return this.<IHDLObject>attachContext(this.toHDL(context.psExpression(), false), context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsValueContext context, final boolean isStatement) {
    TerminalNode _RULE_PS_LITERAL_TERMINAL = context.RULE_PS_LITERAL_TERMINAL();
    boolean _tripleNotEquals = (_RULE_PS_LITERAL_TERMINAL != null);
    if (_tripleNotEquals) {
      return this.<HDLLiteral>attachContext(new HDLLiteral().setStr(false).setVal(context.RULE_PS_LITERAL_TERMINAL().getText()), context);
    }
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _tripleNotEquals_1 = (_RULE_STRING != null);
    if (_tripleNotEquals_1) {
      String str = context.RULE_STRING().getText();
      int _length = str.length();
      int _minus = (_length - 1);
      str = str.substring(1, _minus);
      return this.<HDLLiteral>attachContext(new HDLLiteral().setStr(true).setVal(str), context);
    }
    PSHDLLang.PsVariableRefContext _psVariableRef = context.psVariableRef();
    boolean _tripleNotEquals_2 = (_psVariableRef != null);
    if (_tripleNotEquals_2) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psVariableRef(), false), context);
    }
    Class<? extends PSHDLLang.PsValueContext> _class = context.getClass();
    String _plus = ("Not correctly implemented:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsValueExpContext context, final boolean isStatement) {
    return this.<IHDLObject>attachContext(this.toHDL(context.psValue(), false), context);
  }
  
  protected HDLConcat _toHDL(final PSHDLLang.PsConcatContext context, final boolean isStatement) {
    HDLConcat cat = new HDLConcat();
    final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function = (PSHDLLang.PsExpressionContext it) -> {
      IHDLObject _hDL = this.toHDL(it, false);
      return ((HDLExpression) _hDL);
    };
    cat = cat.setCats(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psExpression(), _function));
    return this.<HDLConcat>attachContext(cat, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLang.PsBitLogOrContext context, final boolean isStatement) {
    HDLBitOp res = new HDLBitOp().setType(HDLBitOp.HDLBitOpType.LOGI_OR);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLang.PsBitLogAndContext context, final boolean isStatement) {
    HDLBitOp res = new HDLBitOp().setType(HDLBitOp.HDLBitOpType.LOGI_AND);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLang.PsBitXorContext context, final boolean isStatement) {
    HDLBitOp res = new HDLBitOp().setType(HDLBitOp.HDLBitOpType.XOR);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLang.PsBitOrContext context, final boolean isStatement) {
    HDLBitOp res = new HDLBitOp().setType(HDLBitOp.HDLBitOpType.OR);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLBitOp _toHDL(final PSHDLLang.PsBitAndContext context, final boolean isStatement) {
    HDLBitOp res = new HDLBitOp().setType(HDLBitOp.HDLBitOpType.AND);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected HDLShiftOp _toHDL(final PSHDLLang.PsShiftContext context, final boolean isStatement) {
    final HDLShiftOp.HDLShiftOpType type = HDLShiftOp.HDLShiftOpType.getOp(context.op.getText());
    HDLShiftOp res = new HDLShiftOp().setType(type);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLShiftOp>attachContext(res, context);
  }
  
  protected HDLEqualityOp _toHDL(final PSHDLLang.PsEqualityCompContext context, final boolean isStatement) {
    final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(context.op.getText());
    HDLEqualityOp res = new HDLEqualityOp().setType(type);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected HDLEqualityOp _toHDL(final PSHDLLang.PsEqualityContext context, final boolean isStatement) {
    final HDLEqualityOp.HDLEqualityOpType type = HDLEqualityOp.HDLEqualityOpType.getOp(context.op.getText());
    HDLEqualityOp res = new HDLEqualityOp().setType(type);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected HDLArithOp _toHDL(final PSHDLLang.PsMulContext context, final boolean isStatement) {
    final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(context.op.getText());
    HDLArithOp res = new HDLArithOp().setType(type);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected HDLArithOp _toHDL(final PSHDLLang.PsAddContext context, final boolean isStatement) {
    final HDLArithOp.HDLArithOpType type = HDLArithOp.HDLArithOpType.getOp(context.op.getText());
    HDLArithOp res = new HDLArithOp().setType(type);
    IHDLObject _hDL = this.toHDL(context.psExpression(0), false);
    res = res.setLeft(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setRight(((HDLExpression) _hDL_1));
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected HDLPrimitive _toHDL(final PSHDLLang.PsCastContext context, final boolean isStatement) {
    final HDLPrimitive.HDLPrimitiveType pt = HDLPrimitive.HDLPrimitiveType.getOp(context.psPrimitiveType().getText());
    PSHDLLang.PsWidthContext _psWidth = context.psWidth();
    IHDLObject _hDL = null;
    if (_psWidth!=null) {
      _hDL=this.toHDL(_psWidth, false);
    }
    final HDLExpression width = ((HDLExpression) _hDL);
    return this.<HDLPrimitive>attachContext(new HDLPrimitive().setType(this.getResultingType(pt, width)).setWidth(width), context);
  }
  
  protected HDLManip _toHDL(final PSHDLLang.PsManipContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psExpression(), false);
    HDLManip res = new HDLManip().setTarget(((HDLExpression) _hDL));
    PSHDLLang.PsCastContext _psCast = context.psCast();
    boolean _tripleNotEquals = (_psCast != null);
    if (_tripleNotEquals) {
      res = res.setType(HDLManip.HDLManipType.CAST);
      IHDLObject _hDL_1 = this.toHDL(context.psCast(), false);
      res = res.setCastTo(((HDLType) _hDL_1));
    } else {
      int _type = context.type.getType();
      switch (_type) {
        case PSHDLLangLexer.LOGIC_NEG:
          res = res.setType(HDLManip.HDLManipType.LOGIC_NEG);
          break;
        case PSHDLLangLexer.ARITH_NEG:
          res = res.setType(HDLManip.HDLManipType.ARITH_NEG);
          break;
        case PSHDLLangLexer.BIT_NEG:
          res = res.setType(HDLManip.HDLManipType.BIT_NEG);
          break;
      }
    }
    return this.<HDLManip>attachContext(res, context);
  }
  
  protected HDLTernary _toHDL(final PSHDLLang.PsTernaryContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psExpression(0), isStatement);
    HDLTernary res = new HDLTernary().setIfExpr(((HDLExpression) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psExpression(1), false);
    res = res.setThenExpr(((HDLExpression) _hDL_1));
    IHDLObject _hDL_2 = this.toHDL(context.psExpression(2), false);
    res = res.setElseExpr(((HDLExpression) _hDL_2));
    return this.<HDLTernary>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsParensContext context, final boolean isStatement) {
    return this.<IHDLObject>attachContext(this.toHDL(context.psExpression(), false), context);
  }
  
  protected HDLExpression _toHDL(final PSHDLLang.PsExpressionContext context, final boolean isStatement) {
    Class<? extends PSHDLLang.PsExpressionContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  public String toName(final PSHDLLang.PsEnumContext context) {
    return this.toName(context.psQualifiedName());
  }
  
  public String toName(final PSHDLLang.PsInterfaceContext context) {
    return this.toName(context.psQualifiedName());
  }
  
  public HDLQualifiedName toFQNName(final PSHDLLang.PsQualifiedNameContext context) {
    String _text = context.getText();
    return new HDLQualifiedName(_text);
  }
  
  public String toName(final PSHDLLang.PsQualifiedNameContext context) {
    String _text = context.getText();
    return new HDLQualifiedName(_text).toString();
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsTypeDeclarationContext context, final boolean isStatement) {
    PSHDLLang.PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
    boolean _tripleNotEquals = (_psEnumDeclaration != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psEnumDeclaration(), true), context);
    }
    PSHDLLang.PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
    boolean _tripleNotEquals_1 = (_psInterfaceDeclaration != null);
    if (_tripleNotEquals_1) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psInterfaceDeclaration(), true), context);
    }
    Class<? extends PSHDLLang.PsTypeDeclarationContext> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected HDLInterfaceDeclaration _toHDL(final PSHDLLang.PsInterfaceDeclarationContext context, final boolean isStatement) {
    HDLInterface hIf = new HDLInterface().setName(this.toName(context.psInterface()));
    final Function1<PSHDLLang.PsPortDeclarationContext, HDLVariableDeclaration> _function = (PSHDLLang.PsPortDeclarationContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLVariableDeclaration) _hDL);
    };
    hIf = hIf.setPorts(ListExtensions.<PSHDLLang.PsPortDeclarationContext, HDLVariableDeclaration>map(context.psInterfaceDecl().psPortDeclaration(), _function));
    return this.<HDLInterfaceDeclaration>attachContext(new HDLInterfaceDeclaration().setHIf(hIf), context);
  }
  
  protected HDLVariableDeclaration _toHDL(final PSHDLLang.PsPortDeclarationContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psVariableDeclaration(), true);
    HDLVariableDeclaration res = ((HDLVariableDeclaration) _hDL);
    final Function1<PSHDLLang.PsAnnotationContext, HDLAnnotation> _function = (PSHDLLang.PsAnnotationContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, true);
      return ((HDLAnnotation) _hDL_1);
    };
    res = res.setAnnotations(ListExtensions.<PSHDLLang.PsAnnotationContext, HDLAnnotation>map(context.psAnnotation(), _function));
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsBlockContext context, final boolean isStatement) {
    PSHDLLang.PsDeclarationContext _psDeclaration = context.psDeclaration();
    boolean _tripleNotEquals = (_psDeclaration != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psDeclaration(), true), context);
    }
    PSHDLLang.PsInstantiationContext _psInstantiation = context.psInstantiation();
    boolean _tripleNotEquals_1 = (_psInstantiation != null);
    if (_tripleNotEquals_1) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psInstantiation(), true), context);
    }
    PSHDLLang.PsStatementContext _psStatement = context.psStatement();
    boolean _tripleNotEquals_2 = (_psStatement != null);
    if (_tripleNotEquals_2) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psStatement(), true), context);
    }
    List<PSHDLLang.PsBlockContext> _psBlock = context.psBlock();
    boolean _tripleNotEquals_3 = (_psBlock != null);
    if (_tripleNotEquals_3) {
      final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function = (PSHDLLang.PsBlockContext it) -> {
        IHDLObject _hDL = this.toHDL(it, true);
        return ((HDLStatement) _hDL);
      };
      return new HDLBlock().setProcess(false).setStatements(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psBlock(), _function));
    }
    Class<? extends PSHDLLang.PsBlockContext> _class = context.getClass();
    String _plus = ("Not correctly implemented type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected HDLDirectGeneration _toHDL(final PSHDLLang.PsDirectGenerationContext context, final boolean isStatement) {
    HDLDirectGeneration gen = new HDLDirectGeneration().setGeneratorContent("");
    gen = gen.setInclude((context.isInclude != null));
    IHDLObject _hDL = this.toHDL(context.psInterface(), false);
    gen = gen.setHIf(((HDLInterface) _hDL));
    IHDLObject _hDL_1 = this.toHDL(context.psVariable(), false);
    gen = gen.setVar(((HDLVariable) _hDL_1));
    gen = gen.setGeneratorID(context.RULE_ID().getText());
    PSHDLLang.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _tripleNotEquals = (_psPassedArguments != null);
    if (_tripleNotEquals) {
      final Function1<PSHDLLang.PsArgumentContext, HDLArgument> _function = (PSHDLLang.PsArgumentContext it) -> {
        IHDLObject _hDL_2 = this.toHDL(it, isStatement);
        return ((HDLArgument) _hDL_2);
      };
      gen = gen.setArguments(ListExtensions.<PSHDLLang.PsArgumentContext, HDLArgument>map(context.psPassedArguments().psArgument(), _function));
    }
    TerminalNode _RULE_GENERATOR_CONTENT = context.RULE_GENERATOR_CONTENT();
    boolean _tripleNotEquals_1 = (_RULE_GENERATOR_CONTENT != null);
    if (_tripleNotEquals_1) {
      gen = gen.setGeneratorContent(context.RULE_GENERATOR_CONTENT().getText());
    }
    return this.<HDLDirectGeneration>attachContext(gen, context);
  }
  
  protected HDLVariable _toHDL(final PSHDLLang.PsVariableContext context, final boolean isStatement) {
    return this.<HDLVariable>attachContext(new HDLVariable().setName(this.toName(context)), context);
  }
  
  protected HDLInterface _toHDL(final PSHDLLang.PsInterfaceContext context, final boolean isStatement) {
    return this.<HDLInterface>attachContext(new HDLInterface().setName(this.toName(context)), context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsInstantiationContext context, final boolean isStatement) {
    HDLInstantiation res = null;
    PSHDLLang.PsDirectGenerationContext _psDirectGeneration = context.psDirectGeneration();
    boolean _tripleNotEquals = (_psDirectGeneration != null);
    if (_tripleNotEquals) {
      IHDLObject _hDL = this.toHDL(context.psDirectGeneration(), true);
      res = ((HDLInstantiation) _hDL);
    }
    PSHDLLang.PsInterfaceInstantiationContext _psInterfaceInstantiation = context.psInterfaceInstantiation();
    boolean _tripleNotEquals_1 = (_psInterfaceInstantiation != null);
    if (_tripleNotEquals_1) {
      IHDLObject _hDL_1 = this.toHDL(context.psInterfaceInstantiation(), true);
      res = ((HDLInstantiation) _hDL_1);
    }
    if ((res != null)) {
      final Function1<PSHDLLang.PsAnnotationContext, HDLAnnotation> _function = (PSHDLLang.PsAnnotationContext it) -> {
        IHDLObject _hDL_2 = this.toHDL(it, false);
        return ((HDLAnnotation) _hDL_2);
      };
      res = res.setAnnotations(ListExtensions.<PSHDLLang.PsAnnotationContext, HDLAnnotation>map(context.psAnnotation(), _function));
      return this.<HDLInstantiation>attachContext(res, context);
    }
    Class<? extends PSHDLLang.PsInstantiationContext> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected HDLEnum _toHDL(final PSHDLLang.PsEnumContext context, final boolean isStatement) {
    return this.<HDLEnum>attachContext(new HDLEnum().setName(this.toName(context)), context);
  }
  
  protected HDLEnumDeclaration _toHDL(final PSHDLLang.PsEnumDeclarationContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psEnum(), false);
    HDLEnum he = ((HDLEnum) _hDL);
    final Function1<PSHDLLang.PsVariableContext, HDLVariable> _function = (PSHDLLang.PsVariableContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, false);
      return ((HDLVariable) _hDL_1);
    };
    he = he.setEnums(ListExtensions.<PSHDLLang.PsVariableContext, HDLVariable>map(context.psVariable(), _function));
    return this.<HDLEnumDeclaration>attachContext(new HDLEnumDeclaration().setHEnum(he), context);
  }
  
  protected HDLSubstituteFunction _toHDL(final PSHDLLang.PsSubstituteFunctionContext context, final boolean isStatement) {
    HDLSubstituteFunction func = new HDLSubstituteFunction();
    func = func.setName(this.toName(context.psFunction()));
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLStatement) _hDL);
    };
    func = func.setStmnts(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psBlock(), _function));
    final Function1<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter> _function_1 = (PSHDLLang.PsFuncSpecContext it) -> {
      IHDLObject _hDL = this.toHDL(it, false);
      return ((HDLFunctionParameter) _hDL);
    };
    func = func.setArgs(ListExtensions.<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter>map(context.psFuncParam().psFuncSpec(), _function_1));
    PSHDLLang.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
    boolean _tripleNotEquals = (_psFuncRecturnType != null);
    if (_tripleNotEquals) {
      IHDLObject _hDL = this.toHDL(context.psFuncRecturnType(), false);
      func = func.setReturnType(((HDLFunctionParameter) _hDL));
    }
    return this.<HDLSubstituteFunction>attachContext(func, context);
  }
  
  protected HDLNativeFunction _toHDL(final PSHDLLang.PsNativeFunctionContext context, final boolean isStatement) {
    HDLNativeFunction func = new HDLNativeFunction();
    func = func.setName(this.toName(context.psFunction()));
    func = func.setSimOnly((context.isSim != null));
    final Function1<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter> _function = (PSHDLLang.PsFuncSpecContext it) -> {
      IHDLObject _hDL = this.toHDL(it, false);
      return ((HDLFunctionParameter) _hDL);
    };
    func = func.setArgs(ListExtensions.<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter>map(context.psFuncParam().psFuncSpec(), _function));
    PSHDLLang.PsFuncRecturnTypeContext _psFuncRecturnType = context.psFuncRecturnType();
    boolean _tripleNotEquals = (_psFuncRecturnType != null);
    if (_tripleNotEquals) {
      IHDLObject _hDL = this.toHDL(context.psFuncRecturnType(), false);
      func = func.setReturnType(((HDLFunctionParameter) _hDL));
    }
    return this.<HDLNativeFunction>attachContext(func, context);
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLang.PsFuncRecturnTypeContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psFuncParamType(), isStatement);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    res = res.setRw(HDLFunctionParameter.RWType.RETURN);
    final Function1<PSHDLLang.PsFuncOptArrayContext, HDLExpression> _function = (PSHDLLang.PsFuncOptArrayContext it) -> {
      HDLExpression _xifexpression = null;
      PSHDLLang.PsExpressionContext _psExpression = it.psExpression();
      boolean _tripleNotEquals = (_psExpression != null);
      if (_tripleNotEquals) {
        IHDLObject _hDL_1 = this.toHDL(it.psExpression(), false);
        _xifexpression = ((HDLExpression) _hDL_1);
      } else {
        _xifexpression = HDLFunctionParameter.EMPTY_ARR();
      }
      return _xifexpression;
    };
    res = res.setDim(
      ListExtensions.<PSHDLLang.PsFuncOptArrayContext, HDLExpression>map(context.dims, _function));
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLang.PsFuncSpecContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psFuncParamWithRW(), false);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    res = res.setName(new HDLVariable().setName(context.RULE_ID().getText()));
    final Function1<PSHDLLang.PsFuncOptArrayContext, HDLExpression> _function = (PSHDLLang.PsFuncOptArrayContext it) -> {
      HDLExpression _xifexpression = null;
      PSHDLLang.PsExpressionContext _psExpression = it.psExpression();
      boolean _tripleNotEquals = (_psExpression != null);
      if (_tripleNotEquals) {
        IHDLObject _hDL_1 = this.toHDL(it.psExpression(), false);
        _xifexpression = ((HDLExpression) _hDL_1);
      } else {
        _xifexpression = HDLFunctionParameter.EMPTY_ARR();
      }
      return _xifexpression;
    };
    res = res.setDim(
      ListExtensions.<PSHDLLang.PsFuncOptArrayContext, HDLExpression>map(context.dims, _function));
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLang.PsFuncParamWithRWContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psFuncParamType(), isStatement);
    HDLFunctionParameter res = ((HDLFunctionParameter) _hDL);
    PSHDLLang.PsFuncParamRWTypeContext _psFuncParamRWType = context.psFuncParamRWType();
    boolean _tripleNotEquals = (_psFuncParamRWType != null);
    if (_tripleNotEquals) {
      res = res.setRw(HDLFunctionParameter.RWType.getOp(context.psFuncParamRWType().getText()));
    } else {
      res = res.setRw(HDLFunctionParameter.RWType.READ);
    }
    if ((context.constant != null)) {
      res = res.setConstant(true);
    }
    return res;
  }
  
  protected HDLFunctionParameter _toHDL(final PSHDLLang.PsFuncParamTypeContext context, final boolean isStatement) {
    HDLFunctionParameter res = new HDLFunctionParameter().setConstant(false);
    final PSHDLLang.PsFuncParamTypeContext x = context;
    boolean _matched = false;
    TerminalNode _ANY_INT = x.ANY_INT();
    boolean _tripleNotEquals = (_ANY_INT != null);
    if (_tripleNotEquals) {
      _matched=true;
      res = res.setType(HDLFunctionParameter.Type.PARAM_ANY_INT);
    }
    if (!_matched) {
      TerminalNode _ANY_UINT = x.ANY_UINT();
      boolean _tripleNotEquals_1 = (_ANY_UINT != null);
      if (_tripleNotEquals_1) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_ANY_UINT);
      }
    }
    if (!_matched) {
      TerminalNode _ANY_BIT = x.ANY_BIT();
      boolean _tripleNotEquals_2 = (_ANY_BIT != null);
      if (_tripleNotEquals_2) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_ANY_BIT);
      }
    }
    if (!_matched) {
      TerminalNode _INT = x.INT();
      boolean _tripleNotEquals_3 = (_INT != null);
      if (_tripleNotEquals_3) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_INT);
      }
    }
    if (!_matched) {
      TerminalNode _UINT = x.UINT();
      boolean _tripleNotEquals_4 = (_UINT != null);
      if (_tripleNotEquals_4) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_UINT);
      }
    }
    if (!_matched) {
      TerminalNode _BIT = x.BIT();
      boolean _tripleNotEquals_5 = (_BIT != null);
      if (_tripleNotEquals_5) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_BIT);
      }
    }
    if (!_matched) {
      TerminalNode _BOOL = x.BOOL();
      boolean _tripleNotEquals_6 = (_BOOL != null);
      if (_tripleNotEquals_6) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_BOOL);
      }
    }
    if (!_matched) {
      TerminalNode _STRING = x.STRING();
      boolean _tripleNotEquals_7 = (_STRING != null);
      if (_tripleNotEquals_7) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_STRING);
      }
    }
    if (!_matched) {
      TerminalNode _ANY_IF = x.ANY_IF();
      boolean _tripleNotEquals_8 = (_ANY_IF != null);
      if (_tripleNotEquals_8) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_ANY_IF);
      }
    }
    if (!_matched) {
      TerminalNode _ANY_ENUM = x.ANY_ENUM();
      boolean _tripleNotEquals_9 = (_ANY_ENUM != null);
      if (_tripleNotEquals_9) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_ANY_ENUM);
      }
    }
    if (!_matched) {
      TerminalNode _INTERFACE = x.INTERFACE();
      boolean _tripleNotEquals_10 = (_INTERFACE != null);
      if (_tripleNotEquals_10) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_IF);
        res = res.setIfSpec(this.toFQNName(x.psQualifiedName()));
      }
    }
    if (!_matched) {
      TerminalNode _ENUM = x.ENUM();
      boolean _tripleNotEquals_11 = (_ENUM != null);
      if (_tripleNotEquals_11) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_ENUM);
        res = res.setEnumSpec(this.toFQNName(x.psQualifiedName()));
      }
    }
    if (!_matched) {
      TerminalNode _FUNCTION = x.FUNCTION();
      boolean _tripleNotEquals_12 = (_FUNCTION != null);
      if (_tripleNotEquals_12) {
        _matched=true;
        res = res.setType(HDLFunctionParameter.Type.PARAM_FUNCTION);
        final Function1<PSHDLLang.PsFuncParamWithRWContext, HDLFunctionParameter> _function = (PSHDLLang.PsFuncParamWithRWContext it) -> {
          IHDLObject _hDL = this.toHDL(it, false);
          return ((HDLFunctionParameter) _hDL);
        };
        res = res.setFuncSpec(ListExtensions.<PSHDLLang.PsFuncParamWithRWContext, HDLFunctionParameter>map(x.psFuncParamWithRW(), _function));
        PSHDLLang.PsFuncParamTypeContext _psFuncParamType = x.psFuncParamType();
        boolean _tripleNotEquals_13 = (_psFuncParamType != null);
        if (_tripleNotEquals_13) {
          IHDLObject _hDL = this.toHDL(x.psFuncParamType(), false);
          res = res.setFuncReturnSpec(((HDLFunctionParameter) _hDL));
        }
      }
    }
    return res;
  }
  
  protected HDLInlineFunction _toHDL(final PSHDLLang.PsInlineFunctionContext context, final boolean isStatement) {
    HDLInlineFunction func = new HDLInlineFunction();
    func = func.setName(this.toName(context.psFunction()));
    IHDLObject _hDL = this.toHDL(context.psExpression(), false);
    func = func.setExpr(((HDLExpression) _hDL));
    final Function1<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter> _function = (PSHDLLang.PsFuncSpecContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, false);
      return ((HDLFunctionParameter) _hDL_1);
    };
    func = func.setArgs(ListExtensions.<PSHDLLang.PsFuncSpecContext, HDLFunctionParameter>map(context.psFuncParam().psFuncSpec(), _function));
    IHDLObject _hDL_1 = this.toHDL(context.psFuncRecturnType(), false);
    func = func.setReturnType(((HDLFunctionParameter) _hDL_1));
    return this.<HDLInlineFunction>attachContext(func, context);
  }
  
  public String toName(final PSHDLLang.PsFunctionContext context) {
    return context.RULE_ID().getText();
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsFunctionDeclarationContext context, final boolean isStatement) {
    PSHDLLang.PsInlineFunctionContext _psInlineFunction = context.psInlineFunction();
    boolean _tripleNotEquals = (_psInlineFunction != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psInlineFunction(), true), context);
    }
    PSHDLLang.PsNativeFunctionContext _psNativeFunction = context.psNativeFunction();
    boolean _tripleNotEquals_1 = (_psNativeFunction != null);
    if (_tripleNotEquals_1) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psNativeFunction(), true), context);
    }
    PSHDLLang.PsSubstituteFunctionContext _psSubstituteFunction = context.psSubstituteFunction();
    boolean _tripleNotEquals_2 = (_psSubstituteFunction != null);
    if (_tripleNotEquals_2) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psSubstituteFunction(), true), context);
    }
    Class<? extends PSHDLLang.PsFunctionDeclarationContext> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected HDLUnresolvedFragment _toHDL(final PSHDLLang.PsRefPartContext context, final boolean isStatement) {
    HDLUnresolvedFragment frag = null;
    PSHDLLang.PsFuncArgsContext _psFuncArgs = context.psFuncArgs();
    boolean _tripleNotEquals = (_psFuncArgs != null);
    if (_tripleNotEquals) {
      HDLUnresolvedFragmentFunction uff = new HDLUnresolvedFragmentFunction().setFrag(context.RULE_ID().getText());
      final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function = (PSHDLLang.PsExpressionContext it) -> {
        IHDLObject _hDL = this.toHDL(it, false);
        return ((HDLExpression) _hDL);
      };
      frag = uff.setParams(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psFuncArgs().psExpression(), _function));
    } else {
      frag = new HDLUnresolvedFragment().setFrag(context.RULE_ID().getText());
      PSHDLLang.PsArrayContext _psArray = context.psArray();
      boolean _tripleNotEquals_1 = (_psArray != null);
      if (_tripleNotEquals_1) {
        final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function_1 = (PSHDLLang.PsExpressionContext it) -> {
          IHDLObject _hDL = this.toHDL(it, false);
          return ((HDLExpression) _hDL);
        };
        frag = frag.setArray(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psArray().psExpression(), _function_1));
      }
      PSHDLLang.PsBitAccessContext _psBitAccess = context.psBitAccess();
      boolean _tripleNotEquals_2 = (_psBitAccess != null);
      if (_tripleNotEquals_2) {
        final Function1<PSHDLLang.PsAccessRangeContext, HDLRange> _function_2 = (PSHDLLang.PsAccessRangeContext it) -> {
          IHDLObject _hDL = this.toHDL(it, false);
          return ((HDLRange) _hDL);
        };
        frag = frag.setBits(ListExtensions.<PSHDLLang.PsAccessRangeContext, HDLRange>map(context.psBitAccess().psAccessRange(), _function_2));
      }
    }
    frag = frag.setIsStatement(isStatement);
    return this.<HDLUnresolvedFragment>attachContext(frag, context);
  }
  
  protected HDLReference _toHDL(final PSHDLLang.PsVariableRefContext context, final boolean isStatement) {
    if ((context.isClk != null)) {
      return this.<HDLVariableRef>attachContext(HDLRegisterConfig.defaultClk(true).asHDLRef(), context);
    }
    if ((context.isRst != null)) {
      return this.<HDLVariableRef>attachContext(HDLRegisterConfig.defaultRst(true).asHDLRef(), context);
    }
    HDLUnresolvedFragment current = null;
    List<PSHDLLang.PsRefPartContext> _reverseView = ListExtensions.<PSHDLLang.PsRefPartContext>reverseView(context.psRefPart());
    for (final PSHDLLang.PsRefPartContext sub : _reverseView) {
      {
        IHDLObject _hDL = this.toHDL(sub, false);
        HDLUnresolvedFragment frag = ((HDLUnresolvedFragment) _hDL);
        if ((current != null)) {
          frag = frag.setSub(current);
        }
        current = frag;
      }
    }
    if ((current != null)) {
      current = current.setIsStatement(isStatement);
      return this.<HDLUnresolvedFragment>attachContext(current, context);
    }
    return null;
  }
  
  protected HDLRange _toHDL(final PSHDLLang.PsAccessRangeContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.from, false);
    HDLRange res = new HDLRange().setTo(((HDLExpression) _hDL));
    if ((context.to != null)) {
      IHDLObject _hDL_1 = this.toHDL(context.from, false);
      IHDLObject _hDL_2 = this.toHDL(context.to, isStatement);
      res = res.setFrom(((HDLExpression) _hDL_1)).setTo(
        ((HDLExpression) _hDL_2));
    }
    if ((context.inc != null)) {
      IHDLObject _hDL_3 = this.toHDL(context.from, false);
      IHDLObject _hDL_4 = this.toHDL(context.inc, isStatement);
      res = res.setTo(((HDLExpression) _hDL_3)).setInc(
        ((HDLExpression) _hDL_4));
    }
    if ((context.dec != null)) {
      IHDLObject _hDL_5 = this.toHDL(context.from, false);
      IHDLObject _hDL_6 = this.toHDL(context.dec, isStatement);
      res = res.setTo(((HDLExpression) _hDL_5)).setDec(
        ((HDLExpression) _hDL_6));
    }
    return this.<HDLRange>attachContext(res, context);
  }
  
  protected HDLSwitchCaseStatement _toHDL(final PSHDLLang.PsCaseStatementsContext context, final boolean isStatement) {
    HDLSwitchCaseStatement hCase = new HDLSwitchCaseStatement();
    PSHDLLang.PsValueContext _psValue = context.psValue();
    boolean _tripleNotEquals = (_psValue != null);
    if (_tripleNotEquals) {
      IHDLObject _hDL = this.toHDL(context.psValue(), false);
      hCase = hCase.setLabel(((HDLExpression) _hDL));
    }
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, true);
      return ((HDLStatement) _hDL_1);
    };
    hCase = hCase.setDos(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psBlock(), _function));
    return this.<HDLSwitchCaseStatement>attachContext(hCase, context);
  }
  
  protected HDLSwitchStatement _toHDL(final PSHDLLang.PsSwitchStatementContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psVariableRef(), false);
    HDLSwitchStatement switchStmnt = new HDLSwitchStatement().setCaseExp(((HDLExpression) _hDL));
    final Function1<PSHDLLang.PsCaseStatementsContext, HDLSwitchCaseStatement> _function = (PSHDLLang.PsCaseStatementsContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, true);
      return ((HDLSwitchCaseStatement) _hDL_1);
    };
    switchStmnt = switchStmnt.setCases(ListExtensions.<PSHDLLang.PsCaseStatementsContext, HDLSwitchCaseStatement>map(context.psCaseStatements(), _function));
    return this.<HDLSwitchStatement>attachContext(switchStmnt, context);
  }
  
  protected HDLInterfaceInstantiation _toHDL(final PSHDLLang.PsInterfaceInstantiationContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psVariable(), isStatement);
    HDLVariable hVar = ((HDLVariable) _hDL);
    PSHDLLang.PsArrayContext _psArray = context.psArray();
    boolean _tripleNotEquals = (_psArray != null);
    if (_tripleNotEquals) {
      final Function1<PSHDLLang.PsExpressionContext, HDLExpression> _function = (PSHDLLang.PsExpressionContext it) -> {
        IHDLObject _hDL_1 = this.toHDL(it, false);
        return ((HDLExpression) _hDL_1);
      };
      hVar = hVar.setDimensions(ListExtensions.<PSHDLLang.PsExpressionContext, HDLExpression>map(context.psArray().psExpression(), _function));
    }
    HDLInterfaceInstantiation hii = new HDLInterfaceInstantiation().setVar(hVar).setHIf(this.toFQNName(context.psQualifiedName()));
    PSHDLLang.PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _tripleNotEquals_1 = (_psPassedArguments != null);
    if (_tripleNotEquals_1) {
      final Function1<PSHDLLang.PsArgumentContext, HDLArgument> _function_1 = (PSHDLLang.PsArgumentContext it) -> {
        IHDLObject _hDL_1 = this.toHDL(it, false);
        return ((HDLArgument) _hDL_1);
      };
      hii = hii.setArguments(ListExtensions.<PSHDLLang.PsArgumentContext, HDLArgument>map(context.psPassedArguments().psArgument(), _function_1));
    }
    return this.<HDLInterfaceInstantiation>attachContext(hii, context);
  }
  
  protected HDLForLoop _toHDL(final PSHDLLang.PsForStatementContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psVariable(), false);
    HDLForLoop loop = new HDLForLoop().setParam(((HDLVariable) _hDL));
    final Function1<PSHDLLang.PsAccessRangeContext, HDLRange> _function = (PSHDLLang.PsAccessRangeContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, false);
      return ((HDLRange) _hDL_1);
    };
    loop = loop.setRange(ListExtensions.<PSHDLLang.PsAccessRangeContext, HDLRange>map(context.psBitAccess().psAccessRange(), _function));
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function_1 = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, true);
      return ((HDLStatement) _hDL_1);
    };
    loop = loop.setDos(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psSimpleBlock().psBlock(), _function_1));
    return this.<HDLForLoop>attachContext(loop, context);
  }
  
  protected HDLIfStatement _toHDL(final PSHDLLang.PsIfStatementContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psExpression(), false);
    HDLIfStatement res = new HDLIfStatement().setIfExp(((HDLExpression) _hDL));
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL_1 = this.toHDL(it, true);
      return ((HDLStatement) _hDL_1);
    };
    res = res.setThenDo(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.ifBlk.psBlock(), _function));
    if ((context.elseBlk != null)) {
      final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function_1 = (PSHDLLang.PsBlockContext it) -> {
        IHDLObject _hDL_1 = this.toHDL(it, true);
        return ((HDLStatement) _hDL_1);
      };
      res = res.setElseDo(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.elseBlk.psBlock(), _function_1));
    }
    return this.<HDLIfStatement>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsCompoundStatementContext context, final boolean isStatement) {
    PSHDLLang.PsForStatementContext _psForStatement = context.psForStatement();
    boolean _tripleNotEquals = (_psForStatement != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psForStatement(), true), context);
    }
    PSHDLLang.PsIfStatementContext _psIfStatement = context.psIfStatement();
    boolean _tripleNotEquals_1 = (_psIfStatement != null);
    if (_tripleNotEquals_1) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psIfStatement(), true), context);
    }
    PSHDLLang.PsSwitchStatementContext _psSwitchStatement = context.psSwitchStatement();
    boolean _tripleNotEquals_2 = (_psSwitchStatement != null);
    if (_tripleNotEquals_2) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psSwitchStatement(), true), context);
    }
    Class<? extends PSHDLLang.PsCompoundStatementContext> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsStatementContext context, final boolean isStatement) {
    PSHDLLang.PsAssignmentOrFuncContext _psAssignmentOrFunc = context.psAssignmentOrFunc();
    boolean _tripleNotEquals = (_psAssignmentOrFunc != null);
    if (_tripleNotEquals) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psAssignmentOrFunc(), true), context);
    }
    PSHDLLang.PsCompoundStatementContext _psCompoundStatement = context.psCompoundStatement();
    boolean _tripleNotEquals_1 = (_psCompoundStatement != null);
    if (_tripleNotEquals_1) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psCompoundStatement(), true), context);
    }
    PSHDLLang.PsProcessContext _psProcess = context.psProcess();
    boolean _tripleNotEquals_2 = (_psProcess != null);
    if (_tripleNotEquals_2) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psProcess(), true), context);
    }
    PSHDLLang.PsExportContext _psExport = context.psExport();
    boolean _tripleNotEquals_3 = (_psExport != null);
    if (_tripleNotEquals_3) {
      return this.<IHDLObject>attachContext(this.toHDL(context.psExport(), true), context);
    }
    Class<? extends PSHDLLang.PsStatementContext> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsExportContext context, final boolean isStatement) {
    HDLExport _hDLExport = new HDLExport();
    String _name = this.toName(context.psVariable());
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_name);
    HDLExport export = _hDLExport.setHIf(_hDLQualifiedName);
    PSHDLLang.PsVariableMatchContext _psVariableMatch = context.psVariableMatch();
    boolean _tripleNotEquals = (_psVariableMatch != null);
    if (_tripleNotEquals) {
      export = export.setMatch(context.psVariableMatch().getText());
    }
    PSHDLLang.PsGroupMatchContext _psGroupMatch = context.psGroupMatch();
    boolean _tripleNotEquals_1 = (_psGroupMatch != null);
    if (_tripleNotEquals_1) {
      export = export.setMatch(context.psGroupMatch().getText());
    }
    return this.<HDLExport>attachContext(export, context);
  }
  
  protected IHDLObject _toHDL(final PSHDLLang.PsAssignmentOrFuncContext context, final boolean isStatement) {
    IHDLObject _hDL = this.toHDL(context.psVariableRef(), isStatement);
    HDLReference hVar = ((HDLReference) _hDL);
    PSHDLLang.PsAssignmentOpContext _psAssignmentOp = context.psAssignmentOp();
    boolean _tripleNotEquals = (_psAssignmentOp != null);
    if (_tripleNotEquals) {
      final HDLAssignment.HDLAssignmentType type = HDLAssignment.HDLAssignmentType.getOp(context.psAssignmentOp().getText());
      if ((hVar instanceof HDLUnresolvedFragment)) {
        hVar = ((HDLUnresolvedFragment)hVar).setIsStatement(false);
      }
      HDLAssignment ass = new HDLAssignment().setLeft(hVar).setType(type);
      IHDLObject _hDL_1 = this.toHDL(context.psExpression(), false);
      ass = ass.setRight(((HDLExpression) _hDL_1));
      return this.<HDLAssignment>attachContext(ass, context);
    }
    return this.<HDLReference>attachContext(hVar, context);
  }
  
  protected IHDLObject _toHDL(final Object context, final boolean isStatement) {
    Class<?> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    throw new IllegalArgumentException(_plus);
  }
  
  public HDLUnit toHDLUnit(final PSHDLLang.PsUnitContext context, final String libURI) {
    HDLUnit unit = new HDLUnit().setName(this.toName(context.psInterface())).setLibURI(libURI);
    int _type = context.unitType.getType();
    boolean _equals = (_type == PSHDLLangLexer.TESTBENCH);
    unit = unit.setSimulation(_equals);
    final Function1<PSHDLLang.PsAnnotationContext, HDLAnnotation> _function = (PSHDLLang.PsAnnotationContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLAnnotation) _hDL);
    };
    unit = unit.setAnnotations(ListExtensions.<PSHDLLang.PsAnnotationContext, HDLAnnotation>map(context.psAnnotation(), _function));
    final Function1<PSHDLLang.PsImportsContext, String> _function_1 = (PSHDLLang.PsImportsContext it) -> {
      return this.toName(it);
    };
    unit = unit.setImports(ListExtensions.<PSHDLLang.PsImportsContext, String>map(context.psImports(), _function_1));
    final Function1<PSHDLLang.PsBlockContext, HDLStatement> _function_2 = (PSHDLLang.PsBlockContext it) -> {
      IHDLObject _hDL = this.toHDL(it, true);
      return ((HDLStatement) _hDL);
    };
    unit = unit.setStatements(ListExtensions.<PSHDLLang.PsBlockContext, HDLStatement>map(context.psBlock(), _function_2));
    return this.<HDLUnit>attachContext(unit, context);
  }
  
  public String toName(final PSHDLLang.PsImportsContext context) {
    return context.psQualifiedNameImport().getText();
  }
  
  public IHDLObject toHDL(final Object context, final boolean isStatement) {
    if (context instanceof PSHDLLang.PsAddContext) {
      return _toHDL((PSHDLLang.PsAddContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsArrayInitExpContext) {
      return _toHDL((PSHDLLang.PsArrayInitExpContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBitAndContext) {
      return _toHDL((PSHDLLang.PsBitAndContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBitLogAndContext) {
      return _toHDL((PSHDLLang.PsBitLogAndContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBitLogOrContext) {
      return _toHDL((PSHDLLang.PsBitLogOrContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBitOrContext) {
      return _toHDL((PSHDLLang.PsBitOrContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBitXorContext) {
      return _toHDL((PSHDLLang.PsBitXorContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsConcatContext) {
      return _toHDL((PSHDLLang.PsConcatContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsEqualityCompContext) {
      return _toHDL((PSHDLLang.PsEqualityCompContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsEqualityContext) {
      return _toHDL((PSHDLLang.PsEqualityContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsManipContext) {
      return _toHDL((PSHDLLang.PsManipContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsMulContext) {
      return _toHDL((PSHDLLang.PsMulContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsParensContext) {
      return _toHDL((PSHDLLang.PsParensContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsShiftContext) {
      return _toHDL((PSHDLLang.PsShiftContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsTernaryContext) {
      return _toHDL((PSHDLLang.PsTernaryContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsValueExpContext) {
      return _toHDL((PSHDLLang.PsValueExpContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsAccessRangeContext) {
      return _toHDL((PSHDLLang.PsAccessRangeContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsAnnotationContext) {
      return _toHDL((PSHDLLang.PsAnnotationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsArgumentContext) {
      return _toHDL((PSHDLLang.PsArgumentContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsArrayInitContext) {
      return _toHDL((PSHDLLang.PsArrayInitContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsArrayInitSubContext) {
      return _toHDL((PSHDLLang.PsArrayInitSubContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsArrayInitSubParensContext) {
      return _toHDL((PSHDLLang.PsArrayInitSubParensContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsAssignmentOrFuncContext) {
      return _toHDL((PSHDLLang.PsAssignmentOrFuncContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsBlockContext) {
      return _toHDL((PSHDLLang.PsBlockContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsCaseStatementsContext) {
      return _toHDL((PSHDLLang.PsCaseStatementsContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsCastContext) {
      return _toHDL((PSHDLLang.PsCastContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsCompoundStatementContext) {
      return _toHDL((PSHDLLang.PsCompoundStatementContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsDeclAssignmentContext) {
      return _toHDL((PSHDLLang.PsDeclAssignmentContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsDeclarationContext) {
      return _toHDL((PSHDLLang.PsDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsDeclarationTypeContext) {
      return _toHDL((PSHDLLang.PsDeclarationTypeContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsDirectGenerationContext) {
      return _toHDL((PSHDLLang.PsDirectGenerationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsEnumContext) {
      return _toHDL((PSHDLLang.PsEnumContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsEnumDeclarationContext) {
      return _toHDL((PSHDLLang.PsEnumDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsExportContext) {
      return _toHDL((PSHDLLang.PsExportContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsExpressionContext) {
      return _toHDL((PSHDLLang.PsExpressionContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsForStatementContext) {
      return _toHDL((PSHDLLang.PsForStatementContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsFuncParamTypeContext) {
      return _toHDL((PSHDLLang.PsFuncParamTypeContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsFuncParamWithRWContext) {
      return _toHDL((PSHDLLang.PsFuncParamWithRWContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsFuncRecturnTypeContext) {
      return _toHDL((PSHDLLang.PsFuncRecturnTypeContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsFuncSpecContext) {
      return _toHDL((PSHDLLang.PsFuncSpecContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsFunctionDeclarationContext) {
      return _toHDL((PSHDLLang.PsFunctionDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsIfStatementContext) {
      return _toHDL((PSHDLLang.PsIfStatementContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsInlineFunctionContext) {
      return _toHDL((PSHDLLang.PsInlineFunctionContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsInstantiationContext) {
      return _toHDL((PSHDLLang.PsInstantiationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsInterfaceContext) {
      return _toHDL((PSHDLLang.PsInterfaceContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsInterfaceDeclarationContext) {
      return _toHDL((PSHDLLang.PsInterfaceDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsInterfaceInstantiationContext) {
      return _toHDL((PSHDLLang.PsInterfaceInstantiationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsNativeFunctionContext) {
      return _toHDL((PSHDLLang.PsNativeFunctionContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsPortDeclarationContext) {
      return _toHDL((PSHDLLang.PsPortDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsPrimitiveContext) {
      return _toHDL((PSHDLLang.PsPrimitiveContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsProcessContext) {
      return _toHDL((PSHDLLang.PsProcessContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsRefPartContext) {
      return _toHDL((PSHDLLang.PsRefPartContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsStatementContext) {
      return _toHDL((PSHDLLang.PsStatementContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsSubstituteFunctionContext) {
      return _toHDL((PSHDLLang.PsSubstituteFunctionContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsSwitchStatementContext) {
      return _toHDL((PSHDLLang.PsSwitchStatementContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsTypeDeclarationContext) {
      return _toHDL((PSHDLLang.PsTypeDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsValueContext) {
      return _toHDL((PSHDLLang.PsValueContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsVariableContext) {
      return _toHDL((PSHDLLang.PsVariableContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsVariableDeclarationContext) {
      return _toHDL((PSHDLLang.PsVariableDeclarationContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsVariableRefContext) {
      return _toHDL((PSHDLLang.PsVariableRefContext)context, isStatement);
    } else if (context instanceof PSHDLLang.PsWidthContext) {
      return _toHDL((PSHDLLang.PsWidthContext)context, isStatement);
    } else if (context != null) {
      return _toHDL(context, isStatement);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(context, isStatement).toString());
    }
  }
}
