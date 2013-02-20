package de.tuhh.ict.pshdl.model.parser;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.HDLAnnotation;
import de.tuhh.ict.pshdl.model.HDLArrayInit;
import de.tuhh.ict.pshdl.model.HDLDeclaration;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration;
import de.tuhh.ict.pshdl.model.HDLPackage;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnit;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAnnotationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsAnnotationTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArrayContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsArrayInitContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclAssignmentContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDeclarationTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsDirectionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsEnumDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsExpressionContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsFunctionDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsInterfaceDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsModelContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPortDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPrimitiveContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsPrimitiveTypeContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsQualifiedNameContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsTypeDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsUnitContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsVariableContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsVariableDeclarationContext;
import de.tuhh.ict.pshdl.model.parser.PSHDLLangParser.PsWidthContext;
import de.tuhh.ict.pshdl.model.utils.HDLLibrary;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import java.util.Arrays;
import java.util.List;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class ParserToModelExtension {
  public static ParserToModelExtension INST = new Function0<ParserToModelExtension>() {
    public ParserToModelExtension apply() {
      ParserToModelExtension _parserToModelExtension = new ParserToModelExtension();
      return _parserToModelExtension;
    }
  }.apply();
  
  public static HDLPackage toHDL(final PsModelContext ctx, final String libURI) {
    return ParserToModelExtension.INST.toHDLPkg(ctx, libURI);
  }
  
  public HDLPackage toHDLPkg(final PsModelContext ctx, final String libURI) {
    HDLPackage _hDLPackage = new HDLPackage();
    HDLPackage pkg = _hDLPackage.setLibURI(libURI);
    PsQualifiedNameContext _psQualifiedName = ctx.psQualifiedName();
    boolean _notEquals = (!Objects.equal(_psQualifiedName, null));
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
    IHDLObject _attachContext = this.attachContext(pkg, ctx);
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
    return this.attachContext(res, context);
  }
  
  public IHDLObject attachContext(final IHDLObject obj, final ParserRuleContext context) {
    return null;
  }
  
  protected IHDLObject _toHDL(final PsAnnotationContext context) {
    PsAnnotationTypeContext _psAnnotationType = context.psAnnotationType();
    TerminalNode _RULE_ID = _psAnnotationType.RULE_ID();
    final String name = _RULE_ID.getText();
    TerminalNode _RULE_STRING = context.RULE_STRING();
    final String value = _RULE_STRING.getText();
    HDLAnnotation _hDLAnnotation = new HDLAnnotation();
    HDLAnnotation _setName = _hDLAnnotation.setName(name);
    HDLAnnotation _setValue = _setName.setValue(value);
    return this.attachContext(_setValue, context);
  }
  
  protected IHDLObject _toHDL(final PsDeclarationTypeContext context) {
    PsFunctionDeclarationContext _psFunctionDeclaration = context.psFunctionDeclaration();
    boolean _notEquals = (!Objects.equal(_psFunctionDeclaration, null));
    if (_notEquals) {
      PsFunctionDeclarationContext _psFunctionDeclaration_1 = context.psFunctionDeclaration();
      IHDLObject _hDL = this.toHDL(_psFunctionDeclaration_1);
      return this.attachContext(_hDL, context);
    }
    PsTypeDeclarationContext _psTypeDeclaration = context.psTypeDeclaration();
    boolean _notEquals_1 = (!Objects.equal(_psTypeDeclaration, null));
    if (_notEquals_1) {
      PsTypeDeclarationContext _psTypeDeclaration_1 = context.psTypeDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psTypeDeclaration_1);
      return this.attachContext(_hDL_1, context);
    }
    PsVariableDeclarationContext _psVariableDeclaration = context.psVariableDeclaration();
    boolean _notEquals_2 = (!Objects.equal(_psVariableDeclaration, null));
    if (_notEquals_2) {
      PsVariableDeclarationContext _psVariableDeclaration_1 = context.psVariableDeclaration();
      IHDLObject _hDL_2 = this.toHDL(_psVariableDeclaration_1);
      return this.attachContext(_hDL_2, context);
    }
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Not implemented");
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
    boolean _notEquals = (!Objects.equal(_psDirection, null));
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
    return this.attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsArrayInitContext context) {
    List<PsExpressionContext> _psExpression = context.psExpression();
    boolean _notEquals = (!Objects.equal(_psExpression, null));
    if (_notEquals) {
      List<PsExpressionContext> _psExpression_1 = context.psExpression();
      int _size = _psExpression_1.size();
      boolean _equals = (_size == 1);
      if (_equals) {
        List<PsExpressionContext> _psExpression_2 = context.psExpression();
        PsExpressionContext _get = _psExpression_2.get(0);
        IHDLObject _hDL = this.toHDL(_get);
        return this.attachContext(_hDL, context);
      }
      HDLArrayInit _hDLArrayInit = new HDLArrayInit();
      HDLArrayInit arr = _hDLArrayInit;
      List<PsExpressionContext> _psExpression_3 = context.psExpression();
      for (final PsExpressionContext exp : _psExpression_3) {
        IHDLObject _hDL_1 = this.toHDL(exp);
        HDLArrayInit _addExp = arr.addExp(((HDLExpression) _hDL_1));
        arr = _addExp;
      }
      return this.attachContext(arr, context);
    }
    HDLArrayInit _hDLArrayInit_1 = new HDLArrayInit();
    HDLArrayInit arr_1 = _hDLArrayInit_1;
    List<PsArrayInitContext> _psArrayInit = context.psArrayInit();
    for (final PsArrayInitContext sub : _psArrayInit) {
      IHDLObject _hDL_2 = this.toHDL(sub);
      HDLArrayInit _addExp_1 = arr_1.addExp(((HDLExpression) _hDL_2));
      arr_1 = _addExp_1;
    }
    return this.attachContext(arr_1, context);
  }
  
  protected IHDLObject _toHDL(final PsPrimitiveContext context) {
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    boolean _notEquals = (!Objects.equal(_psQualifiedName, null));
    if (_notEquals) {
      HDLEnum _hDLEnum = new HDLEnum();
      PsQualifiedNameContext _psQualifiedName_1 = context.psQualifiedName();
      String _name = this.toName(_psQualifiedName_1);
      HDLEnum _setName = _hDLEnum.setName(_name);
      return this.attachContext(_setName, context);
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
    return this.attachContext(_setWidth, context);
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
    boolean _notEquals = (!Objects.equal(_psArray, null));
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
    boolean _notEquals_1 = (!Objects.equal(_psArrayInit, null));
    if (_notEquals_1) {
      PsArrayInitContext _psArrayInit_1 = context.psArrayInit();
      IHDLObject _hDL_2 = this.toHDL(_psArrayInit_1);
      HDLVariable _setDefaultValue = res.setDefaultValue(((HDLExpression) _hDL_2));
      res = _setDefaultValue;
    }
    return this.attachContext(res, context);
  }
  
  public String toName(final PsVariableContext context) {
    TerminalNode _RULE_ID = context.RULE_ID();
    return _RULE_ID.getText();
  }
  
  public HDLPrimitiveType getResultingType(final HDLPrimitiveType pt, final HDLExpression width) {
    boolean _notEquals = (!Objects.equal(width, null));
    if (_notEquals) {
      boolean _matched = false;
      if (!_matched) {
        if (Objects.equal(pt,HDLPrimitiveType.BIT)) {
          _matched=true;
          return HDLPrimitiveType.BITVECTOR;
        }
      }
    }
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
    return pt;
  }
  
  protected IHDLObject _toHDL(final PsWidthContext context) {
    PsExpressionContext _psExpression = context.psExpression();
    IHDLObject _hDL = this.toHDL(_psExpression);
    return this.attachContext(_hDL, context);
  }
  
  protected IHDLObject _toHDL(final PsExpressionContext context) {
    return null;
  }
  
  public String toName(final PsInterfaceContext context) {
    PsQualifiedNameContext _psQualifiedName = context.psQualifiedName();
    return this.toName(_psQualifiedName);
  }
  
  public String toName(final PsQualifiedNameContext context) {
    String _text = context.getText();
    HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_text);
    return _hDLQualifiedName.toString();
  }
  
  protected IHDLObject _toHDL(final PsTypeDeclarationContext context) {
    PsEnumDeclarationContext _psEnumDeclaration = context.psEnumDeclaration();
    boolean _notEquals = (!Objects.equal(_psEnumDeclaration, null));
    if (_notEquals) {
      PsEnumDeclarationContext _psEnumDeclaration_1 = context.psEnumDeclaration();
      IHDLObject _hDL = this.toHDL(_psEnumDeclaration_1);
      return this.attachContext(_hDL, context);
    }
    PsInterfaceDeclarationContext _psInterfaceDeclaration = context.psInterfaceDeclaration();
    boolean _notEquals_1 = (!Objects.equal(_psInterfaceDeclaration, null));
    if (_notEquals_1) {
      PsInterfaceDeclarationContext _psInterfaceDeclaration_1 = context.psInterfaceDeclaration();
      IHDLObject _hDL_1 = this.toHDL(_psInterfaceDeclaration_1);
      return this.attachContext(_hDL_1, context);
    }
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Not implemented");
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
    return this.attachContext(_setHIf, context);
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
    return this.attachContext(res, context);
  }
  
  protected IHDLObject _toHDL(final PsEnumDeclarationContext context) {
    return null;
  }
  
  protected IHDLObject _toHDL(final PsFunctionDeclarationContext context) {
    return null;
  }
  
  protected IHDLObject _toHDL(final Object c) {
    Class<? extends Object> _class = c.getClass();
    String _plus = ("Unhandled type:" + _class);
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
    throw _illegalArgumentException;
  }
  
  public HDLUnit toHDLUnit(final PsUnitContext context, final String string) {
    return null;
  }
  
  public IHDLObject toHDL(final Object context) {
    if (context instanceof PsAnnotationContext) {
      return _toHDL((PsAnnotationContext)context);
    } else if (context instanceof PsArrayInitContext) {
      return _toHDL((PsArrayInitContext)context);
    } else if (context instanceof PsDeclAssignmentContext) {
      return _toHDL((PsDeclAssignmentContext)context);
    } else if (context instanceof PsDeclarationContext) {
      return _toHDL((PsDeclarationContext)context);
    } else if (context instanceof PsDeclarationTypeContext) {
      return _toHDL((PsDeclarationTypeContext)context);
    } else if (context instanceof PsEnumDeclarationContext) {
      return _toHDL((PsEnumDeclarationContext)context);
    } else if (context instanceof PsExpressionContext) {
      return _toHDL((PsExpressionContext)context);
    } else if (context instanceof PsFunctionDeclarationContext) {
      return _toHDL((PsFunctionDeclarationContext)context);
    } else if (context instanceof PsInterfaceDeclarationContext) {
      return _toHDL((PsInterfaceDeclarationContext)context);
    } else if (context instanceof PsPortDeclarationContext) {
      return _toHDL((PsPortDeclarationContext)context);
    } else if (context instanceof PsPrimitiveContext) {
      return _toHDL((PsPrimitiveContext)context);
    } else if (context instanceof PsTypeDeclarationContext) {
      return _toHDL((PsTypeDeclarationContext)context);
    } else if (context instanceof PsVariableDeclarationContext) {
      return _toHDL((PsVariableDeclarationContext)context);
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
