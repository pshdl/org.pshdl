package de.tuhh.ict.pshdl.model.parser;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.HDLAnnotation;
import de.tuhh.ict.pshdl.model.HDLArgument;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLArrayInit;
import de.tuhh.ict.pshdl.model.HDLAssignment;
import de.tuhh.ict.pshdl.model.HDLAssignment.HDLAssignmentType;
import de.tuhh.ict.pshdl.model.HDLBitOp;
import de.tuhh.ict.pshdl.model.HDLBitOp.HDLBitOpType;
import de.tuhh.ict.pshdl.model.HDLBlock;
import de.tuhh.ict.pshdl.model.HDLConcat;
import de.tuhh.ict.pshdl.model.HDLDeclaration;
import de.tuhh.ict.pshdl.model.HDLDirectGeneration;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration;
import de.tuhh.ict.pshdl.model.HDLEqualityOp;
import de.tuhh.ict.pshdl.model.HDLEqualityOp.HDLEqualityOpType;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLIfStatement;
import de.tuhh.ict.pshdl.model.HDLInlineFunction;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration;
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLManip;
import de.tuhh.ict.pshdl.model.HDLManip.HDLManipType;
import de.tuhh.ict.pshdl.model.HDLNativeFunction;
import de.tuhh.ict.pshdl.model.HDLPackage;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLReference;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig;
import de.tuhh.ict.pshdl.model.HDLShiftOp;
import de.tuhh.ict.pshdl.model.HDLShiftOp.HDLShiftOpType;
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
import de.tuhh.ict.pshdl.model.parser.PSHDLLangLexer;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAccessRangeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAddContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAnnotationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAnnotationTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArgumentContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArrayContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArrayInitContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArrayInitSubContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAssignmentOpContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAssignmentOrFuncContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitAccessContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitAndContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitLogAndContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitLogOrContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitOrContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBitXorContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsBlockContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsCaseStatementsContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsCastContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsCompoundStatementContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsConcatContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclAssignmentContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclarationTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDirectGenerationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDirectionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsEnumContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsEnumDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsEqualityCompContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsEqualityContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsExpressionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsForStatementContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsFuncArgsContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsFuncParamContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsFunctionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsFunctionDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsIfStatementContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsImportsContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInlineFunctionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInstantiationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceInstantiationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsManipContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsMulContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsNativeFunctionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsParensContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPassedArgumentsContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPortDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPrimitiveContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPrimitiveTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsProcessContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameImportContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsRefPartContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsShiftContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsSimpleBlockContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsStatementContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsSubstituteFunctionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsSwitchStatementContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsTernaryContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsTypeDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsUnitContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsValueContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsValueExpContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsVariableContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsVariableDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsVariableRefContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsWidthContext;
import de.tuhh.ict.pshdl.model.parser.SourceInfo;
import de.tuhh.ict.pshdl.model.utils.HDLLibrary;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.BufferedTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;

@SuppressWarnings("all")
public class ParserToModelExtension {
  private BufferedTokenStream tokens;
  
  public ParserToModelExtension(final BufferedTokenStream tokens) {
    this.tokens = tokens;
  }
  
  public static HDLPackage toHDL(final BufferedTokenStream tokens, final PsModelContext ctx, final String libURI) {
    ParserToModelExtension _parserToModelExtension = new ParserToModelExtension(tokens);
    return _parserToModelExtension.toHDLPkg(ctx, libURI);
  }
  
  public HDLPackage toHDLPkg(final PsModelContext ctx, final String libURI) {
    HDLPackage _hDLPackage = new HDLPackage();
    HDLPackage pkg = _hDLPackage.setLibURI(libURI);
    PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psQualifiedName, null);
    if (_notEquals) {
      PsQualifiedNameContext _psQualifiedName_1 = ctx.psQualifiedName();
      String _name = this.toName(_psQualifiedName_1);
      HDLPackage _setPkg = pkg.setPkg(_name);
      pkg = _setPkg;
    }
    List<PsUnitContext> _psUnit = ctx.psUnit();
    for (final PsUnitContext unit : _psUnit) {
      HDLUnit _hDLUnit = this.toHDLUnit(unit, libURI);
      HDLPackage _addUnits = pkg.addUnits(_hDLUnit);
      pkg = _addUnits;
    }
    List<PsDeclarationContext> _psDeclaration = ctx.psDeclaration();
    for (final PsDeclarationContext decl : _psDeclaration) {
      IHDLObject _hDL = this.toHDL(decl);
      HDLPackage _addDeclarations = pkg.addDeclarations(((HDLDeclaration) _hDL));
      pkg = _addDeclarations;
    }
    pkg.freeze(null);
    HDLLibrary _library = HDLLibrary.getLibrary(libURI);
    _library.addPkg(pkg);
    HDLPackage _attachContext = this.<HDLPackage>attachContext(pkg, ctx);
    return ((HDLPackage) _attachContext);
  }
  
  protected IHDLObject _toHDL(final PsDeclarationContext context) {
    PsDeclarationTypeContext _psDeclarationType = context.psDeclarationType();
    IHDLObject _hDL = this.toHDL(_psDeclarationType);
    HDLDeclaration res = ((HDLDeclaration) _hDL);
    List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
    for (final PsAnnotationContext anno : _psAnnotation) {
      IHDLObject _hDL_1 = this.toHDL(anno);
      HDLDeclaration _addAnnotations = res.addAnnotations(((HDLAnnotation) _hDL_1));
      res = _addAnnotations;
    }
    return this.<HDLDeclaration>attachContext(res, context);
  }
  
  public <T extends IHDLObject> T attachContext(final T obj, final ParserRuleContext context) {
    boolean _equals = ObjectExtensions.operator_equals(obj, null);
    if (_equals) {
      NullPointerException _nullPointerException = new NullPointerException("Null is not allowed");
      throw _nullPointerException;
    }
    SourceInfo _sourceInfo = new SourceInfo(this.tokens, context);
    obj.<SourceInfo>addMeta(SourceInfo.INFO, _sourceInfo);
    return obj;
  }
  
  protected IHDLObject _toHDL(final PsArgumentContext context) {
    HDLArgument _hDLArgument = new HDLArgument();
    TerminalNode _RULE_ID = context.RULE_ID();
    String _text = _RULE_ID.getText();
    HDLArgument res = _hDLArgument.setName(_text);
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArgument _setExpression = res.setExpression(((HDLExpression) _hDL));
    res = _setExpression;
    return this.<HDLArgument>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsProcessContext context) {
    HDLBlock _hDLBlock = new HDLBlock();
    HDLBlock block = _hDLBlock;
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.isProcess, null);
    if (_notEquals) {
      HDLBlock _setProcess = block.setProcess(true);
      block = _setProcess;
    }
    List<PsBlockContext> _psBlock = context.psBlock();
    for (final PsBlockContext subBlock : _psBlock) {
      IHDLObject _hDL = this.toHDL(subBlock);
      HDLBlock _addStatements = block.addStatements(((HDLStatement) _hDL));
      block = _addStatements;
    }
    return this.<HDLBlock>attachContext(block, context);
  }
  
  protected IHDLObject _toHDL(final PsAnnotationContext context) {
    PsAnnotationTypeContext _psAnnotationType = context.psAnnotationType();
    final String name = _psAnnotationType.getText();
    String value = null;
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_RULE_STRING, null);
    if (_notEquals) {
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
  
  protected IHDLObject _toHDL(final PsDeclarationTypeContext context) {
    PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psFunctionDeclaration, null);
    if (_notEquals) {
      PsFunctionDeclarationContext _psFunctionDeclaration_1 = context.psFunctionDeclaration();
      IHDLObject _hDL = this.toHDL(_psFunctionDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psTypeDeclaration, null);
    if (_notEquals_1) {
      PsTypeDeclarationContext _psTypeDeclaration_1 = context.psTypeDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psTypeDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psVariableDeclaration, null);
    if (_notEquals_2) {
      PsVariableDeclarationContext _psVariableDeclaration_1 = context.psVariableDeclaration();
      IHDLObject _hDL_2 = this.toHDL(_psVariableDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsVariableDeclarationContext context) {
    HDLVariableDeclaration _hDLVariableDeclaration = new HDLVariableDeclaration();
    HDLVariableDeclaration res = _hDLVariableDeclaration;
    PsPrimitiveContext _psPrimitive = context.psPrimitive();
    IHDLObject _hDL = this.toHDL(_psPrimitive);
    HDLVariableDeclaration _setType = res.setType(((HDLType) _hDL));
    res = _setType;
    HDLDirection dir = HDLDirection.INTERNAL;
    PsDirectionContext _psDirection = context.psDirection();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psDirection, null);
    if (_notEquals) {
      PsDirectionContext _psDirection_1 = context.psDirection();
      String _text = _psDirection_1.getText();
      HDLDirection _op = HDLDirection.getOp(_text);
      dir = _op;
    }
    HDLVariableDeclaration _setDirection = res.setDirection(dir);
    res = _setDirection;
    List<PsDeclAssignmentContext> _psDeclAssignment = context.psDeclAssignment();
    for (final PsDeclAssignmentContext varDecl : _psDeclAssignment) {
      IHDLObject _hDL_1 = this.toHDL(varDecl);
      HDLVariableDeclaration _addVariables = res.addVariables(((HDLVariable) _hDL_1));
      res = _addVariables;
    }
    PsPrimitiveContext _psPrimitive_1 = context.psPrimitive();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psPrimitive_1.isRegister, null);
    if (_notEquals_1) {
      ArrayList<HDLArgument> _arrayList = new ArrayList<HDLArgument>();
      ArrayList<HDLArgument> args = _arrayList;
      PsPrimitiveContext _psPrimitive_2 = context.psPrimitive();
      PsPassedArgumentsContext _psPassedArguments = _psPrimitive_2.psPassedArguments();
      boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psPassedArguments, null);
      if (_notEquals_2) {
        PsPrimitiveContext _psPrimitive_3 = context.psPrimitive();
        PsPassedArgumentsContext _psPassedArguments_1 = _psPrimitive_3.psPassedArguments();
        List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
        for (final PsArgumentContext arg : _psArgument) {
          IHDLObject _hDL_2 = this.toHDL(arg);
          args.add(((HDLArgument) _hDL_2));
        }
      }
      HDLRegisterConfig _fromArgs = HDLRegisterConfig.fromArgs(args);
      HDLVariableDeclaration _setRegister = res.setRegister(_fromArgs);
      res = _setRegister;
    }
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsArrayInitContext context) {
    PsExpressionContext _psExpression = context.psExpression();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psExpression, null);
    if (_notEquals) {
      PsExpressionContext _psExpression_1 = context.psExpression();
      IHDLObject _hDL = this.toHDL(_psExpression_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    HDLArrayInit _hDLArrayInit = new HDLArrayInit();
    HDLArrayInit arr = _hDLArrayInit;
    List<PsArrayInitSubContext> _psArrayInitSub = context.psArrayInitSub();
    for (final PsArrayInitSubContext sub : _psArrayInitSub) {
      IHDLObject _hDL_1 = this.toHDL(sub);
      HDLArrayInit _addExp = arr.addExp(((HDLExpression) _hDL_1));
      arr = _addExp;
    }
    return this.<HDLArrayInit>attachContext(arr, context);
  }
  
  protected IHDLObject _toHDL(final PsArrayInitSubContext context) {
    List<PsExpressionContext> _psExpression = context.psExpression();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psExpression, null);
    if (_notEquals) {
      List<PsExpressionContext> _psExpression_1 = context.psExpression();
      int _size = _psExpression_1.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        List<PsExpressionContext> _psExpression_2 = context.psExpression();
        PsExpressionContext _get = _psExpression_2.get(0);
        IHDLObject _hDL = this.toHDL(_get);
        return this.<IHDLObject>attachContext(_hDL, context);
      }
      HDLArrayInit _hDLArrayInit = new HDLArrayInit();
      HDLArrayInit arr = _hDLArrayInit;
      List<PsExpressionContext> _psExpression_3 = context.psExpression();
      for (final PsExpressionContext exp : _psExpression_3) {
        IHDLObject _hDL_1 = this.toHDL(exp);
        HDLArrayInit _addExp = arr.addExp(((HDLExpression) _hDL_1));
        arr = _addExp;
      }
      return this.<HDLArrayInit>attachContext(arr, context);
    }
    HDLArrayInit _hDLArrayInit_1 = new HDLArrayInit();
    HDLArrayInit arr_1 = _hDLArrayInit_1;
    List<PsArrayInitSubContext> _psArrayInitSub = context.psArrayInitSub();
    for (final PsArrayInitSubContext sub : _psArrayInitSub) {
      IHDLObject _hDL_2 = this.toHDL(sub);
      HDLArrayInit _addExp_1 = arr_1.addExp(((HDLExpression) _hDL_2));
      arr_1 = _addExp_1;
    }
    return this.<HDLArrayInit>attachContext(arr_1, context);
  }
  
  protected IHDLObject _toHDL(final PsPrimitiveContext context) {
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psQualifiedName, null);
    if (_notEquals) {
      HDLEnum _hDLEnum = new HDLEnum();
      PsQualifiedNameContext _psQualifiedName_1 = context.psQualifiedName();
      String _name = this.toName(_psQualifiedName_1);
      HDLEnum _setName = _hDLEnum.setName(_name);
      return this.<HDLEnum>attachContext(_setName, context);
    }
    PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
    String _text = _psPrimitiveType.getText();
    String _upperCase = _text.toUpperCase();
    final HDLPrimitiveType pt = HDLPrimitiveType.valueOf(_upperCase);
    PsWidthContext _psWidth = context.psWidth();
    IHDLObject _hDL = _psWidth==null?(IHDLObject)null:this.toHDL(_psWidth);
    final HDLExpression width = ((HDLExpression) _hDL);
    HDLPrimitive _hDLPrimitive = new HDLPrimitive();
    HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
    HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
    HDLPrimitive _setWidth = _setType.setWidth(width);
    return this.<HDLPrimitive>attachContext(_setWidth, context);
  }
  
  protected IHDLObject _toHDL(final PsDeclAssignmentContext context) {
    HDLVariable _hDLVariable = new HDLVariable();
    PsVariableContext _psVariable = context.psVariable();
    String _name = this.toName(_psVariable);
    HDLVariable res = _hDLVariable.setName(_name);
    List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
    for (final PsAnnotationContext anno : _psAnnotation) {
      IHDLObject _hDL = this.toHDL(anno);
      HDLVariable _addAnnotations = res.addAnnotations(((HDLAnnotation) _hDL));
      res = _addAnnotations;
    }
    PsArrayContext _psArray = context.psArray();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psArray, null);
    if (_notEquals) {
      PsArrayContext _psArray_1 = context.psArray();
      List<PsExpressionContext> _psExpression = _psArray_1.psExpression();
      for (final PsExpressionContext arr : _psExpression) {
        IHDLObject _hDL_1 = this.toHDL(arr);
        HDLVariable _addDimensions = res.addDimensions(((HDLExpression) _hDL_1));
        res = _addDimensions;
      }
    }
    PsArrayInitContext _psArrayInit = context.psArrayInit();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psArrayInit, null);
    if (_notEquals_1) {
      PsArrayInitContext _psArrayInit_1 = context.psArrayInit();
      IHDLObject _hDL_2 = this.toHDL(_psArrayInit_1);
      HDLVariable _setDefaultValue = res.setDefaultValue(((HDLExpression) _hDL_2));
      res = _setDefaultValue;
    }
    return this.<HDLVariable>attachContext(res, context);
  }
  
  public String toName(final PsVariableContext context) {
    TerminalNode _RULE_ID = context.RULE_ID();
    return _RULE_ID.getText();
  }
  
  public HDLPrimitiveType getResultingType(final HDLPrimitiveType pt, final HDLExpression width) {
    boolean _notEquals = ObjectExtensions.operator_notEquals(width, null);
    if (_notEquals) {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(pt,HDLPrimitiveType.BIT)) {
          _matched=true;
          return HDLPrimitiveType.BITVECTOR;
        }
      }
    } else {
      boolean _matched_1 = false;
      if (!_matched_1) {
        if (Objects.equal(pt,HDLPrimitiveType.INT)) {
          _matched_1=true;
          return HDLPrimitiveType.INTEGER;
        }
      }
      if (!_matched_1) {
        if (Objects.equal(pt,HDLPrimitiveType.UINT)) {
          _matched_1=true;
          return HDLPrimitiveType.NATURAL;
        }
      }
    }
    return pt;
  }
  
  protected IHDLObject _toHDL(final PsWidthContext context) {
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected IHDLObject _toHDL(final PsValueContext context) {
    TerminalNode _RULE_PS_LITERAL_TERMINAL = context.RULE_PS_LITERAL_TERMINAL();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_RULE_PS_LITERAL_TERMINAL, null);
    if (_notEquals) {
      HDLLiteral _hDLLiteral = new HDLLiteral();
      HDLLiteral _setStr = _hDLLiteral.setStr(false);
      TerminalNode _RULE_PS_LITERAL_TERMINAL_1 = context.RULE_PS_LITERAL_TERMINAL();
      String _text = _RULE_PS_LITERAL_TERMINAL_1.getText();
      HDLLiteral _setVal = _setStr.setVal(_text);
      return this.<HDLLiteral>attachContext(_setVal, context);
    }
    TerminalNode _RULE_STRING = context.RULE_STRING();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_RULE_STRING, null);
    if (_notEquals_1) {
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
    PsVariableRefContext _psVariableRef = context.psVariableRef();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psVariableRef, null);
    if (_notEquals_2) {
      PsVariableRefContext _psVariableRef_1 = context.psVariableRef();
      IHDLObject _hDL = this.toHDL(_psVariableRef_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not correctly implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsValueExpContext context) {
    PsValueContext _psValue = context.psValue();
    IHDLObject _hDL = this.toHDL(_psValue);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected IHDLObject _toHDL(final PsConcatContext context) {
    HDLConcat _hDLConcat = new HDLConcat();
    HDLConcat cat = _hDLConcat;
    List<PsExpressionContext> _psExpression = context.psExpression();
    for (final PsExpressionContext exp : _psExpression) {
      IHDLObject _hDL = this.toHDL(exp);
      HDLConcat _addCats = cat.addCats(((HDLExpression) _hDL));
      cat = _addCats;
    }
    return this.<HDLConcat>attachContext(cat, context);
  }
  
  protected IHDLObject _toHDL(final PsBitLogOrContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.LOGI_OR);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsBitLogAndContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.LOGI_AND);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsBitXorContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.XOR);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsBitOrContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.OR);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsBitAndContext context) {
    HDLBitOp _hDLBitOp = new HDLBitOp();
    HDLBitOp res = _hDLBitOp.setType(HDLBitOpType.AND);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLBitOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLBitOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLBitOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsShiftContext context) {
    String _text = context.op.getText();
    final HDLShiftOpType type = HDLShiftOpType.getOp(_text);
    HDLShiftOp _hDLShiftOp = new HDLShiftOp();
    HDLShiftOp res = _hDLShiftOp.setType(type);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLShiftOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLShiftOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLShiftOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsEqualityCompContext context) {
    String _text = context.op.getText();
    final HDLEqualityOpType type = HDLEqualityOpType.getOp(_text);
    HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
    HDLEqualityOp res = _hDLEqualityOp.setType(type);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsEqualityContext context) {
    String _text = context.op.getText();
    final HDLEqualityOpType type = HDLEqualityOpType.getOp(_text);
    HDLEqualityOp _hDLEqualityOp = new HDLEqualityOp();
    HDLEqualityOp res = _hDLEqualityOp.setType(type);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLEqualityOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLEqualityOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLEqualityOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsMulContext context) {
    String _text = context.op.getText();
    final HDLArithOpType type = HDLArithOpType.getOp(_text);
    HDLArithOp _hDLArithOp = new HDLArithOp();
    HDLArithOp res = _hDLArithOp.setType(type);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsAddContext context) {
    String _text = context.op.getText();
    final HDLArithOpType type = HDLArithOpType.getOp(_text);
    HDLArithOp _hDLArithOp = new HDLArithOp();
    HDLArithOp res = _hDLArithOp.setType(type);
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLArithOp _setLeft = res.setLeft(((HDLExpression) _hDL));
    res = _setLeft;
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLArithOp _setRight = res.setRight(((HDLExpression) _hDL_1));
    res = _setRight;
    return this.<HDLArithOp>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsCastContext context) {
    PsPrimitiveTypeContext _psPrimitiveType = context.psPrimitiveType();
    String _text = _psPrimitiveType.getText();
    String _upperCase = _text.toUpperCase();
    final HDLPrimitiveType pt = HDLPrimitiveType.valueOf(_upperCase);
    PsWidthContext _psWidth = context.psWidth();
    IHDLObject _hDL = _psWidth==null?(IHDLObject)null:this.toHDL(_psWidth);
    final HDLExpression width = ((HDLExpression) _hDL);
    HDLPrimitive _hDLPrimitive = new HDLPrimitive();
    HDLPrimitiveType _resultingType = this.getResultingType(pt, width);
    HDLPrimitive _setType = _hDLPrimitive.setType(_resultingType);
    HDLPrimitive _setWidth = _setType.setWidth(width);
    return this.<HDLPrimitive>attachContext(_setWidth, context);
  }
  
  protected IHDLObject _toHDL(final PsManipContext context) {
    HDLManip _hDLManip = new HDLManip();
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLManip res = _hDLManip.setTarget(((HDLExpression) _hDL));
    PsCastContext _psCast = context.psCast();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psCast, null);
    if (_notEquals) {
      HDLManip _setType = res.setType(HDLManipType.CAST);
      res = _setType;
      PsCastContext _psCast_1 = context.psCast();
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
          HDLManip _setType_1 = res.setType(HDLManipType.LOGIC_NEG);
          res = _setType_1;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,PSHDLLangLexer.ARITH_NEG)) {
          _matched=true;
          HDLManip _setType_2 = res.setType(HDLManipType.ARITH_NEG);
          res = _setType_2;
        }
      }
      if (!_matched) {
        if (Objects.equal(_switchValue,PSHDLLangLexer.BIT_NEG)) {
          _matched=true;
          HDLManip _setType_3 = res.setType(HDLManipType.BIT_NEG);
          res = _setType_3;
        }
      }
    }
    return this.<HDLManip>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsTernaryContext context) {
    HDLTernary _hDLTernary = new HDLTernary();
    PsExpressionContext _psExpression = context.psExpression(0);
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLTernary res = _hDLTernary.setIfExpr(((HDLExpression) _hDL));
    PsExpressionContext _psExpression_1 = context.psExpression(1);
    IHDLObject _hDL_1 = this.toHDL(_psExpression_1);
    HDLTernary _setThenExpr = res.setThenExpr(((HDLExpression) _hDL_1));
    res = _setThenExpr;
    PsExpressionContext _psExpression_2 = context.psExpression(2);
    IHDLObject _hDL_2 = this.toHDL(_psExpression_2);
    HDLTernary _setElseExpr = res.setElseExpr(((HDLExpression) _hDL_2));
    res = _setElseExpr;
    return this.<HDLTernary>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsParensContext context) {
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    return this.<IHDLObject>attachContext(_hDL, context);
  }
  
  protected IHDLObject _toHDL(final PsExpressionContext context) {
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public String toName(final PsEnumContext context) {
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    return this.toName(_psQualifiedName);
  }
  
  public String toName(final PsInterfaceContext context) {
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    return this.toName(_psQualifiedName);
  }
  
  public HDLQualifiedName toFQNName(final PsQualifiedNameContext context) {
    String _text = context.getText();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
    return _hDLQualifiedName;
  }
  
  public String toName(final PsQualifiedNameContext context) {
    String _text = context.getText();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
    return _hDLQualifiedName.toString();
  }
  
  protected IHDLObject _toHDL(final PsTypeDeclarationContext context) {
    PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psEnumDeclaration, null);
    if (_notEquals) {
      PsEnumDeclarationContext _psEnumDeclaration_1 = context.psEnumDeclaration();
      IHDLObject _hDL = this.toHDL(_psEnumDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psInterfaceDeclaration, null);
    if (_notEquals_1) {
      PsInterfaceDeclarationContext _psInterfaceDeclaration_1 = context.psInterfaceDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psInterfaceDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not implemented:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsInterfaceDeclarationContext context) {
    HDLInterface _hDLInterface = new HDLInterface();
    PsInterfaceContext _psInterface = context.psInterface();
    String _name = this.toName(_psInterface);
    HDLInterface hIf = _hDLInterface.setName(_name);
    PsInterfaceDeclContext _psInterfaceDecl = context.psInterfaceDecl();
    List<PsPortDeclarationContext> _psPortDeclaration = _psInterfaceDecl.psPortDeclaration();
    for (final PsPortDeclarationContext port : _psPortDeclaration) {
      IHDLObject _hDL = this.toHDL(port);
      HDLInterface _addPorts = hIf.addPorts(((HDLVariableDeclaration) _hDL));
      hIf = _addPorts;
    }
    HDLInterfaceDeclaration _hDLInterfaceDeclaration = new HDLInterfaceDeclaration();
    HDLInterfaceDeclaration _setHIf = _hDLInterfaceDeclaration.setHIf(hIf);
    return this.<HDLInterfaceDeclaration>attachContext(_setHIf, context);
  }
  
  protected IHDLObject _toHDL(final PsPortDeclarationContext context) {
    PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    IHDLObject _hDL = this.toHDL(_psVariableDeclaration);
    HDLVariableDeclaration res = ((HDLVariableDeclaration) _hDL);
    List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
    for (final PsAnnotationContext anno : _psAnnotation) {
      IHDLObject _hDL_1 = this.toHDL(anno);
      HDLVariableDeclaration _addAnnotations = res.addAnnotations(((HDLAnnotation) _hDL_1));
      res = _addAnnotations;
    }
    return this.<HDLVariableDeclaration>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsBlockContext context) {
    PsDeclarationContext _psDeclaration = context.psDeclaration();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psDeclaration, null);
    if (_notEquals) {
      PsDeclarationContext _psDeclaration_1 = context.psDeclaration();
      IHDLObject _hDL = this.toHDL(_psDeclaration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsInstantiationContext _psInstantiation = context.psInstantiation();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psInstantiation, null);
    if (_notEquals_1) {
      PsInstantiationContext _psInstantiation_1 = context.psInstantiation();
      IHDLObject _hDL_1 = this.toHDL(_psInstantiation_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PsStatementContext _psStatement = context.psStatement();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psStatement, null);
    if (_notEquals_2) {
      PsStatementContext _psStatement_1 = context.psStatement();
      IHDLObject _hDL_2 = this.toHDL(_psStatement_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not correctly implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsDirectGenerationContext context) {
    HDLDirectGeneration _hDLDirectGeneration = new HDLDirectGeneration();
    HDLDirectGeneration gen = _hDLDirectGeneration.setGeneratorContent("");
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.isInclude, null);
    if (_notEquals) {
      HDLDirectGeneration _setInclude = gen.setInclude(true);
      gen = _setInclude;
    }
    PsInterfaceContext _psInterface = context.psInterface();
    IHDLObject _hDL = this.toHDL(_psInterface);
    HDLDirectGeneration _setHIf = gen.setHIf(((HDLInterface) _hDL));
    gen = _setHIf;
    PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL_1 = this.toHDL(_psVariable);
    HDLDirectGeneration _setVar = gen.setVar(((HDLVariable) _hDL_1));
    gen = _setVar;
    TerminalNode _RULE_ID = context.RULE_ID();
    String _text = _RULE_ID.getText();
    HDLDirectGeneration _setGeneratorID = gen.setGeneratorID(_text);
    gen = _setGeneratorID;
    PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psPassedArguments, null);
    if (_notEquals_1) {
      PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
      List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
      for (final PsArgumentContext arg : _psArgument) {
        IHDLObject _hDL_2 = this.toHDL(arg);
        HDLDirectGeneration _addArguments = gen.addArguments(((HDLArgument) _hDL_2));
        gen = _addArguments;
      }
    }
    TerminalNode _RULE_GENERATOR_CONTENT = context.RULE_GENERATOR_CONTENT();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_RULE_GENERATOR_CONTENT, null);
    if (_notEquals_2) {
      TerminalNode _RULE_GENERATOR_CONTENT_1 = context.RULE_GENERATOR_CONTENT();
      String _text_1 = _RULE_GENERATOR_CONTENT_1.getText();
      HDLDirectGeneration _setGeneratorContent = gen.setGeneratorContent(_text_1);
      gen = _setGeneratorContent;
    }
    return this.<HDLDirectGeneration>attachContext(gen, context);
  }
  
  protected IHDLObject _toHDL(final PsVariableContext context) {
    HDLVariable _hDLVariable = new HDLVariable();
    String _name = this.toName(context);
    HDLVariable _setName = _hDLVariable.setName(_name);
    return this.<HDLVariable>attachContext(_setName, context);
  }
  
  protected IHDLObject _toHDL(final PsInterfaceContext context) {
    HDLInterface _hDLInterface = new HDLInterface();
    String _name = this.toName(context);
    HDLInterface _setName = _hDLInterface.setName(_name);
    return this.<HDLInterface>attachContext(_setName, context);
  }
  
  protected IHDLObject _toHDL(final PsInstantiationContext context) {
    PsDirectGenerationContext _psDirectGeneration = context.psDirectGeneration();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psDirectGeneration, null);
    if (_notEquals) {
      PsDirectGenerationContext _psDirectGeneration_1 = context.psDirectGeneration();
      IHDLObject _hDL = this.toHDL(_psDirectGeneration_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsInterfaceInstantiationContext _psInterfaceInstantiation = context.psInterfaceInstantiation();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psInterfaceInstantiation, null);
    if (_notEquals_1) {
      PsInterfaceInstantiationContext _psInterfaceInstantiation_1 = context.psInterfaceInstantiation();
      IHDLObject _hDL_1 = this.toHDL(_psInterfaceInstantiation_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsEnumContext context) {
    HDLEnum _hDLEnum = new HDLEnum();
    String _name = this.toName(context);
    HDLEnum _setName = _hDLEnum.setName(_name);
    return this.<HDLEnum>attachContext(_setName, context);
  }
  
  protected IHDLObject _toHDL(final PsEnumDeclarationContext context) {
    PsEnumContext _psEnum = context.psEnum();
    IHDLObject _hDL = this.toHDL(_psEnum);
    HDLEnum he = ((HDLEnum) _hDL);
    List<PsVariableContext> _psVariable = context.psVariable();
    for (final PsVariableContext hVar : _psVariable) {
      IHDLObject _hDL_1 = this.toHDL(hVar);
      HDLEnum _addEnums = he.addEnums(((HDLVariable) _hDL_1));
      he = _addEnums;
    }
    HDLEnumDeclaration _hDLEnumDeclaration = new HDLEnumDeclaration();
    HDLEnumDeclaration _setHEnum = _hDLEnumDeclaration.setHEnum(he);
    return this.<HDLEnumDeclaration>attachContext(_setHEnum, context);
  }
  
  protected IHDLObject _toHDL(final PsSubstituteFunctionContext context) {
    HDLSubstituteFunction _hDLSubstituteFunction = new HDLSubstituteFunction();
    HDLSubstituteFunction func = _hDLSubstituteFunction;
    PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLSubstituteFunction _setName = func.setName(_name);
    func = _setName;
    List<PsStatementContext> _psStatement = context.psStatement();
    for (final PsStatementContext s : _psStatement) {
      IHDLObject _hDL = this.toHDL(s);
      HDLSubstituteFunction _addStmnts = func.addStmnts(((HDLStatement) _hDL));
      func = _addStmnts;
    }
    PsFuncParamContext _psFuncParam = context.psFuncParam();
    List<PsVariableContext> _psVariable = _psFuncParam.psVariable();
    for (final PsVariableContext arg : _psVariable) {
      IHDLObject _hDL_1 = this.toHDL(arg);
      HDLSubstituteFunction _addArgs = func.addArgs(((HDLVariable) _hDL_1));
      func = _addArgs;
    }
    return this.<HDLSubstituteFunction>attachContext(func, context);
  }
  
  protected IHDLObject _toHDL(final PsNativeFunctionContext context) {
    HDLNativeFunction _hDLNativeFunction = new HDLNativeFunction();
    HDLNativeFunction func = _hDLNativeFunction;
    PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLNativeFunction _setName = func.setName(_name);
    func = _setName;
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.isSim, null);
    HDLNativeFunction _setSimOnly = func.setSimOnly(_notEquals);
    func = _setSimOnly;
    return this.<HDLNativeFunction>attachContext(func, context);
  }
  
  protected IHDLObject _toHDL(final PsInlineFunctionContext context) {
    HDLInlineFunction _hDLInlineFunction = new HDLInlineFunction();
    HDLInlineFunction func = _hDLInlineFunction;
    PsFunctionContext _psFunction = context.psFunction();
    String _name = this.toName(_psFunction);
    HDLInlineFunction _setName = func.setName(_name);
    func = _setName;
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLInlineFunction _setExpr = func.setExpr(((HDLExpression) _hDL));
    func = _setExpr;
    PsFuncParamContext _psFuncParam = context.psFuncParam();
    List<PsVariableContext> _psVariable = _psFuncParam.psVariable();
    for (final PsVariableContext arg : _psVariable) {
      IHDLObject _hDL_1 = this.toHDL(arg);
      HDLInlineFunction _addArgs = func.addArgs(((HDLVariable) _hDL_1));
      func = _addArgs;
    }
    return this.<HDLInlineFunction>attachContext(func, context);
  }
  
  public String toName(final PsFunctionContext context) {
    TerminalNode _RULE_ID = context.RULE_ID();
    return _RULE_ID.getText();
  }
  
  protected IHDLObject _toHDL(final PsFunctionDeclarationContext context) {
    PsInlineFunctionContext _psInlineFunction = context.psInlineFunction();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psInlineFunction, null);
    if (_notEquals) {
      PsInlineFunctionContext _psInlineFunction_1 = context.psInlineFunction();
      IHDLObject _hDL = this.toHDL(_psInlineFunction_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsNativeFunctionContext _psNativeFunction = context.psNativeFunction();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psNativeFunction, null);
    if (_notEquals_1) {
      PsNativeFunctionContext _psNativeFunction_1 = context.psNativeFunction();
      IHDLObject _hDL_1 = this.toHDL(_psNativeFunction_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PsSubstituteFunctionContext _psSubstituteFunction = context.psSubstituteFunction();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psSubstituteFunction, null);
    if (_notEquals_2) {
      PsSubstituteFunctionContext _psSubstituteFunction_1 = context.psSubstituteFunction();
      IHDLObject _hDL_2 = this.toHDL(_psSubstituteFunction_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Not implemented type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsRefPartContext context) {
    HDLUnresolvedFragment frag = null;
    PsFuncArgsContext _psFuncArgs = context.psFuncArgs();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psFuncArgs, null);
    if (_notEquals) {
      HDLUnresolvedFragmentFunction _hDLUnresolvedFragmentFunction = new HDLUnresolvedFragmentFunction();
      TerminalNode _RULE_ID = context.RULE_ID();
      String _text = _RULE_ID.getText();
      HDLUnresolvedFragmentFunction uff = _hDLUnresolvedFragmentFunction.setFrag(_text);
      PsFuncArgsContext _psFuncArgs_1 = context.psFuncArgs();
      List<PsExpressionContext> _psExpression = _psFuncArgs_1.psExpression();
      for (final PsExpressionContext param : _psExpression) {
        IHDLObject _hDL = this.toHDL(param);
        HDLUnresolvedFragmentFunction _addParams = uff.addParams(((HDLExpression) _hDL));
        uff = _addParams;
      }
      frag = uff;
    } else {
      HDLUnresolvedFragment _hDLUnresolvedFragment = new HDLUnresolvedFragment();
      TerminalNode _RULE_ID_1 = context.RULE_ID();
      String _text_1 = _RULE_ID_1.getText();
      HDLUnresolvedFragment _setFrag = _hDLUnresolvedFragment.setFrag(_text_1);
      frag = _setFrag;
      PsArrayContext _psArray = context.psArray();
      boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psArray, null);
      if (_notEquals_1) {
        PsArrayContext _psArray_1 = context.psArray();
        List<PsExpressionContext> _psExpression_1 = _psArray_1.psExpression();
        for (final PsExpressionContext arr : _psExpression_1) {
          IHDLObject _hDL_1 = this.toHDL(arr);
          HDLUnresolvedFragment _addArray = frag.addArray(((HDLExpression) _hDL_1));
          frag = _addArray;
        }
      }
      PsBitAccessContext _psBitAccess = context.psBitAccess();
      boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psBitAccess, null);
      if (_notEquals_2) {
        PsBitAccessContext _psBitAccess_1 = context.psBitAccess();
        List<PsAccessRangeContext> _psAccessRange = _psBitAccess_1.psAccessRange();
        for (final PsAccessRangeContext range : _psAccessRange) {
          IHDLObject _hDL_2 = this.toHDL(range);
          HDLUnresolvedFragment _addBits = frag.addBits(((HDLRange) _hDL_2));
          frag = _addBits;
        }
      }
    }
    return this.<HDLUnresolvedFragment>attachContext(frag, context);
  }
  
  protected IHDLObject _toHDL(final PsVariableRefContext context) {
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.isClk, null);
    if (_notEquals) {
      HDLVariable _defaultClk = HDLRegisterConfig.defaultClk();
      HDLVariableRef _asHDLRef = _defaultClk.asHDLRef();
      return this.<HDLVariableRef>attachContext(_asHDLRef, context);
    }
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(context.isRst, null);
    if (_notEquals_1) {
      HDLVariable _defaultRst = HDLRegisterConfig.defaultRst();
      HDLVariableRef _asHDLRef_1 = _defaultRst.asHDLRef();
      return this.<HDLVariableRef>attachContext(_asHDLRef_1, context);
    }
    HDLUnresolvedFragment current = null;
    List<PsRefPartContext> _psRefPart = context.psRefPart();
    List<PsRefPartContext> _reverseView = ListExtensions.<PsRefPartContext>reverseView(_psRefPart);
    for (final PsRefPartContext sub : _reverseView) {
      {
        IHDLObject _hDL = this.toHDL(sub);
        HDLUnresolvedFragment frag = ((HDLUnresolvedFragment) _hDL);
        boolean _notEquals_2 = ObjectExtensions.operator_notEquals(current, null);
        if (_notEquals_2) {
          HDLUnresolvedFragment _setSub = frag.setSub(current);
          frag = _setSub;
        }
        current = frag;
      }
    }
    return this.<HDLUnresolvedFragment>attachContext(current, context);
  }
  
  protected IHDLObject _toHDL(final PsAccessRangeContext context) {
    HDLRange _hDLRange = new HDLRange();
    IHDLObject _hDL = this.toHDL(context.from);
    HDLRange res = _hDLRange.setTo(((HDLExpression) _hDL));
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.to, null);
    if (_notEquals) {
      IHDLObject _hDL_1 = this.toHDL(context.from);
      HDLRange _setFrom = res.setFrom(((HDLExpression) _hDL_1));
      IHDLObject _hDL_2 = this.toHDL(context.to);
      HDLRange _setTo = _setFrom.setTo(((HDLExpression) _hDL_2));
      res = _setTo;
    }
    return this.<HDLRange>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsCaseStatementsContext context) {
    HDLSwitchCaseStatement _hDLSwitchCaseStatement = new HDLSwitchCaseStatement();
    HDLSwitchCaseStatement hCase = _hDLSwitchCaseStatement;
    PsValueContext _psValue = context.psValue();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psValue, null);
    if (_notEquals) {
      PsValueContext _psValue_1 = context.psValue();
      IHDLObject _hDL = this.toHDL(_psValue_1);
      HDLSwitchCaseStatement _setLabel = hCase.setLabel(((HDLExpression) _hDL));
      hCase = _setLabel;
    }
    List<PsBlockContext> _psBlock = context.psBlock();
    for (final PsBlockContext dos : _psBlock) {
      IHDLObject _hDL_1 = this.toHDL(dos);
      HDLSwitchCaseStatement _addDos = hCase.addDos(((HDLStatement) _hDL_1));
      hCase = _addDos;
    }
    return this.<HDLSwitchCaseStatement>attachContext(hCase, context);
  }
  
  protected IHDLObject _toHDL(final PsSwitchStatementContext context) {
    HDLSwitchStatement _hDLSwitchStatement = new HDLSwitchStatement();
    PsVariableRefContext _psVariableRef = context.psVariableRef();
    IHDLObject _hDL = this.toHDL(_psVariableRef);
    HDLSwitchStatement switchStmnt = _hDLSwitchStatement.setCaseExp(((HDLExpression) _hDL));
    List<PsCaseStatementsContext> _psCaseStatements = context.psCaseStatements();
    for (final PsCaseStatementsContext hCase : _psCaseStatements) {
      IHDLObject _hDL_1 = this.toHDL(hCase);
      HDLSwitchStatement _addCases = switchStmnt.addCases(((HDLSwitchCaseStatement) _hDL_1));
      switchStmnt = _addCases;
    }
    return this.<HDLSwitchStatement>attachContext(switchStmnt, context);
  }
  
  protected IHDLObject _toHDL(final PsInterfaceInstantiationContext context) {
    PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL = this.toHDL(_psVariable);
    HDLVariable hVar = ((HDLVariable) _hDL);
    PsArrayContext _psArray = context.psArray();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psArray, null);
    if (_notEquals) {
      PsArrayContext _psArray_1 = context.psArray();
      List<PsExpressionContext> _psExpression = _psArray_1.psExpression();
      for (final PsExpressionContext arr : _psExpression) {
        IHDLObject _hDL_1 = this.toHDL(arr);
        HDLVariable _addDimensions = hVar.addDimensions(((HDLExpression) _hDL_1));
        hVar = _addDimensions;
      }
    }
    HDLInterfaceInstantiation _hDLInterfaceInstantiation = new HDLInterfaceInstantiation();
    HDLInterfaceInstantiation _setVar = _hDLInterfaceInstantiation.setVar(hVar);
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    HDLQualifiedName _fQNName = this.toFQNName(_psQualifiedName);
    HDLInterfaceInstantiation hii = _setVar.setHIf(_fQNName);
    PsPassedArgumentsContext _psPassedArguments = context.psPassedArguments();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psPassedArguments, null);
    if (_notEquals_1) {
      PsPassedArgumentsContext _psPassedArguments_1 = context.psPassedArguments();
      List<PsArgumentContext> _psArgument = _psPassedArguments_1.psArgument();
      for (final PsArgumentContext pa : _psArgument) {
        IHDLObject _hDL_2 = this.toHDL(pa);
        HDLInterfaceInstantiation _addArguments = hii.addArguments(((HDLArgument) _hDL_2));
        hii = _addArguments;
      }
    }
    return this.<HDLInterfaceInstantiation>attachContext(hii, context);
  }
  
  protected IHDLObject _toHDL(final PsForStatementContext context) {
    HDLForLoop _hDLForLoop = new HDLForLoop();
    PsVariableContext _psVariable = context.psVariable();
    IHDLObject _hDL = this.toHDL(_psVariable);
    HDLForLoop loop = _hDLForLoop.setParam(((HDLVariable) _hDL));
    PsBitAccessContext _psBitAccess = context.psBitAccess();
    List<PsAccessRangeContext> _psAccessRange = _psBitAccess.psAccessRange();
    for (final PsAccessRangeContext range : _psAccessRange) {
      IHDLObject _hDL_1 = this.toHDL(range);
      HDLForLoop _addRange = loop.addRange(((HDLRange) _hDL_1));
      loop = _addRange;
    }
    PsSimpleBlockContext _psSimpleBlock = context.psSimpleBlock();
    List<PsBlockContext> _psBlock = _psSimpleBlock.psBlock();
    for (final PsBlockContext dos : _psBlock) {
      IHDLObject _hDL_2 = this.toHDL(dos);
      HDLForLoop _addDos = loop.addDos(((HDLStatement) _hDL_2));
      loop = _addDos;
    }
    return this.<HDLForLoop>attachContext(loop, context);
  }
  
  protected IHDLObject _toHDL(final PsIfStatementContext context) {
    HDLIfStatement _hDLIfStatement = new HDLIfStatement();
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    HDLIfStatement res = _hDLIfStatement.setIfExp(((HDLExpression) _hDL));
    List<PsBlockContext> _psBlock = context.ifBlk.psBlock();
    for (final PsBlockContext thenBlk : _psBlock) {
      IHDLObject _hDL_1 = this.toHDL(thenBlk);
      HDLIfStatement _addThenDo = res.addThenDo(((HDLStatement) _hDL_1));
      res = _addThenDo;
    }
    boolean _notEquals = ObjectExtensions.operator_notEquals(context.elseBlk, null);
    if (_notEquals) {
      List<PsBlockContext> _psBlock_1 = context.elseBlk.psBlock();
      for (final PsBlockContext elseBlk : _psBlock_1) {
        IHDLObject _hDL_2 = this.toHDL(elseBlk);
        HDLIfStatement _addElseDo = res.addElseDo(((HDLStatement) _hDL_2));
        res = _addElseDo;
      }
    }
    return this.<HDLIfStatement>attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsCompoundStatementContext context) {
    PsForStatementContext _psForStatement = context.psForStatement();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psForStatement, null);
    if (_notEquals) {
      PsForStatementContext _psForStatement_1 = context.psForStatement();
      IHDLObject _hDL = this.toHDL(_psForStatement_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsIfStatementContext _psIfStatement = context.psIfStatement();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psIfStatement, null);
    if (_notEquals_1) {
      PsIfStatementContext _psIfStatement_1 = context.psIfStatement();
      IHDLObject _hDL_1 = this.toHDL(_psIfStatement_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PsSwitchStatementContext _psSwitchStatement = context.psSwitchStatement();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psSwitchStatement, null);
    if (_notEquals_2) {
      PsSwitchStatementContext _psSwitchStatement_1 = context.psSwitchStatement();
      IHDLObject _hDL_2 = this.toHDL(_psSwitchStatement_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsStatementContext context) {
    PsAssignmentOrFuncContext _psAssignmentOrFunc = context.psAssignmentOrFunc();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psAssignmentOrFunc, null);
    if (_notEquals) {
      PsAssignmentOrFuncContext _psAssignmentOrFunc_1 = context.psAssignmentOrFunc();
      IHDLObject _hDL = this.toHDL(_psAssignmentOrFunc_1);
      return this.<IHDLObject>attachContext(_hDL, context);
    }
    PsCompoundStatementContext _psCompoundStatement = context.psCompoundStatement();
    boolean _notEquals_1 = ObjectExtensions.operator_notEquals(_psCompoundStatement, null);
    if (_notEquals_1) {
      PsCompoundStatementContext _psCompoundStatement_1 = context.psCompoundStatement();
      IHDLObject _hDL_1 = this.toHDL(_psCompoundStatement_1);
      return this.<IHDLObject>attachContext(_hDL_1, context);
    }
    PsProcessContext _psProcess = context.psProcess();
    boolean _notEquals_2 = ObjectExtensions.operator_notEquals(_psProcess, null);
    if (_notEquals_2) {
      PsProcessContext _psProcess_1 = context.psProcess();
      IHDLObject _hDL_2 = this.toHDL(_psProcess_1);
      return this.<IHDLObject>attachContext(_hDL_2, context);
    }
    Class<? extends Object> _class = context.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  protected IHDLObject _toHDL(final PsAssignmentOrFuncContext context) {
    PsVariableRefContext _psVariableRef = context.psVariableRef();
    IHDLObject _hDL = this.toHDL(_psVariableRef);
    final HDLReference hVar = ((HDLReference) _hDL);
    PsAssignmentOpContext _psAssignmentOp = context.psAssignmentOp();
    boolean _notEquals = ObjectExtensions.operator_notEquals(_psAssignmentOp, null);
    if (_notEquals) {
      PsAssignmentOpContext _psAssignmentOp_1 = context.psAssignmentOp();
      String _text = _psAssignmentOp_1.getText();
      final HDLAssignmentType type = HDLAssignmentType.getOp(_text);
      HDLAssignment _hDLAssignment = new HDLAssignment();
      HDLAssignment _setLeft = _hDLAssignment.setLeft(hVar);
      HDLAssignment ass = _setLeft.setType(type);
      PsExpressionContext _psExpression = context.psExpression();
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
  
  public HDLUnit toHDLUnit(final PsUnitContext context, final String libURI) {
    HDLUnit _hDLUnit = new HDLUnit();
    PsInterfaceContext _psInterface = context.psInterface();
    String _name = this.toName(_psInterface);
    HDLUnit _setName = _hDLUnit.setName(_name);
    HDLUnit _setLibURI = _setName.setLibURI(libURI);
    HDLUnit unit = _setLibURI.setSimulation(false);
    int _type = context.unitType.getType();
    boolean _equals = (_type == PSHDLLangLexer.TESTBENCH);
    if (_equals) {
      HDLUnit _setSimulation = unit.setSimulation(true);
      unit = _setSimulation;
    }
    List<PsAnnotationContext> _psAnnotation = context.psAnnotation();
    for (final PsAnnotationContext anno : _psAnnotation) {
      IHDLObject _hDL = this.toHDL(anno);
      HDLUnit _addAnnotations = unit.addAnnotations(((HDLAnnotation) _hDL));
      unit = _addAnnotations;
    }
    List<PsImportsContext> _psImports = context.psImports();
    for (final PsImportsContext imp : _psImports) {
      String _name_1 = this.toName(imp);
      HDLUnit _addImports = unit.addImports(_name_1);
      unit = _addImports;
    }
    List<PsBlockContext> _psBlock = context.psBlock();
    for (final PsBlockContext block : _psBlock) {
      IHDLObject _hDL_1 = this.toHDL(block);
      HDLUnit _addStatements = unit.addStatements(((HDLStatement) _hDL_1));
      unit = _addStatements;
    }
    return this.<HDLUnit>attachContext(unit, context);
  }
  
  public String toName(final PsImportsContext context) {
    PsQualifiedNameImportContext _psQualifiedNameImport = context.psQualifiedNameImport();
    return _psQualifiedNameImport.getText();
  }
  
  public IHDLObject toHDL(final Object context) {
    if (context instanceof PsAddContext) {
      return _toHDL((PsAddContext)context);
    } else if (context instanceof PsBitAndContext) {
      return _toHDL((PsBitAndContext)context);
    } else if (context instanceof PsBitLogAndContext) {
      return _toHDL((PsBitLogAndContext)context);
    } else if (context instanceof PsBitLogOrContext) {
      return _toHDL((PsBitLogOrContext)context);
    } else if (context instanceof PsBitOrContext) {
      return _toHDL((PsBitOrContext)context);
    } else if (context instanceof PsBitXorContext) {
      return _toHDL((PsBitXorContext)context);
    } else if (context instanceof PsConcatContext) {
      return _toHDL((PsConcatContext)context);
    } else if (context instanceof PsEqualityCompContext) {
      return _toHDL((PsEqualityCompContext)context);
    } else if (context instanceof PsEqualityContext) {
      return _toHDL((PsEqualityContext)context);
    } else if (context instanceof PsManipContext) {
      return _toHDL((PsManipContext)context);
    } else if (context instanceof PsMulContext) {
      return _toHDL((PsMulContext)context);
    } else if (context instanceof PsParensContext) {
      return _toHDL((PsParensContext)context);
    } else if (context instanceof PsShiftContext) {
      return _toHDL((PsShiftContext)context);
    } else if (context instanceof PsTernaryContext) {
      return _toHDL((PsTernaryContext)context);
    } else if (context instanceof PsValueExpContext) {
      return _toHDL((PsValueExpContext)context);
    } else if (context instanceof PsAccessRangeContext) {
      return _toHDL((PsAccessRangeContext)context);
    } else if (context instanceof PsAnnotationContext) {
      return _toHDL((PsAnnotationContext)context);
    } else if (context instanceof PsArgumentContext) {
      return _toHDL((PsArgumentContext)context);
    } else if (context instanceof PsArrayInitContext) {
      return _toHDL((PsArrayInitContext)context);
    } else if (context instanceof PsArrayInitSubContext) {
      return _toHDL((PsArrayInitSubContext)context);
    } else if (context instanceof PsAssignmentOrFuncContext) {
      return _toHDL((PsAssignmentOrFuncContext)context);
    } else if (context instanceof PsBlockContext) {
      return _toHDL((PsBlockContext)context);
    } else if (context instanceof PsCaseStatementsContext) {
      return _toHDL((PsCaseStatementsContext)context);
    } else if (context instanceof PsCastContext) {
      return _toHDL((PsCastContext)context);
    } else if (context instanceof PsCompoundStatementContext) {
      return _toHDL((PsCompoundStatementContext)context);
    } else if (context instanceof PsDeclAssignmentContext) {
      return _toHDL((PsDeclAssignmentContext)context);
    } else if (context instanceof PsDeclarationContext) {
      return _toHDL((PsDeclarationContext)context);
    } else if (context instanceof PsDeclarationTypeContext) {
      return _toHDL((PsDeclarationTypeContext)context);
    } else if (context instanceof PsDirectGenerationContext) {
      return _toHDL((PsDirectGenerationContext)context);
    } else if (context instanceof PsEnumContext) {
      return _toHDL((PsEnumContext)context);
    } else if (context instanceof PsEnumDeclarationContext) {
      return _toHDL((PsEnumDeclarationContext)context);
    } else if (context instanceof PsExpressionContext) {
      return _toHDL((PsExpressionContext)context);
    } else if (context instanceof PsForStatementContext) {
      return _toHDL((PsForStatementContext)context);
    } else if (context instanceof PsFunctionDeclarationContext) {
      return _toHDL((PsFunctionDeclarationContext)context);
    } else if (context instanceof PsIfStatementContext) {
      return _toHDL((PsIfStatementContext)context);
    } else if (context instanceof PsInlineFunctionContext) {
      return _toHDL((PsInlineFunctionContext)context);
    } else if (context instanceof PsInstantiationContext) {
      return _toHDL((PsInstantiationContext)context);
    } else if (context instanceof PsInterfaceContext) {
      return _toHDL((PsInterfaceContext)context);
    } else if (context instanceof PsInterfaceDeclarationContext) {
      return _toHDL((PsInterfaceDeclarationContext)context);
    } else if (context instanceof PsInterfaceInstantiationContext) {
      return _toHDL((PsInterfaceInstantiationContext)context);
    } else if (context instanceof PsNativeFunctionContext) {
      return _toHDL((PsNativeFunctionContext)context);
    } else if (context instanceof PsPortDeclarationContext) {
      return _toHDL((PsPortDeclarationContext)context);
    } else if (context instanceof PsPrimitiveContext) {
      return _toHDL((PsPrimitiveContext)context);
    } else if (context instanceof PsProcessContext) {
      return _toHDL((PsProcessContext)context);
    } else if (context instanceof PsRefPartContext) {
      return _toHDL((PsRefPartContext)context);
    } else if (context instanceof PsStatementContext) {
      return _toHDL((PsStatementContext)context);
    } else if (context instanceof PsSubstituteFunctionContext) {
      return _toHDL((PsSubstituteFunctionContext)context);
    } else if (context instanceof PsSwitchStatementContext) {
      return _toHDL((PsSwitchStatementContext)context);
    } else if (context instanceof PsTypeDeclarationContext) {
      return _toHDL((PsTypeDeclarationContext)context);
    } else if (context instanceof PsValueContext) {
      return _toHDL((PsValueContext)context);
    } else if (context instanceof PsVariableContext) {
      return _toHDL((PsVariableContext)context);
    } else if (context instanceof PsVariableDeclarationContext) {
      return _toHDL((PsVariableDeclarationContext)context);
    } else if (context instanceof PsVariableRefContext) {
      return _toHDL((PsVariableRefContext)context);
    } else if (context instanceof PsWidthContext) {
      return _toHDL((PsWidthContext)context);
    } else if (context != null) {
      return _toHDL(context);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(context).toString());
    }
  }
}
