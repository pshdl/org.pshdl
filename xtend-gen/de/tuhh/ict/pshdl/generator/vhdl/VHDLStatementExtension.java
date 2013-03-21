package de.tuhh.ict.pshdl.generator.vhdl;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLContext;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLExpressionExtension;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLOutputValidator;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLPackageExtension;
import de.tuhh.ict.pshdl.generator.vhdl.VHDLUtils;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLCastsLibrary;
import de.tuhh.ict.pshdl.model.HDLAnnotation;
import de.tuhh.ict.pshdl.model.HDLArithOp;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLAssignment;
import de.tuhh.ict.pshdl.model.HDLBlock;
import de.tuhh.ict.pshdl.model.HDLClass;
import de.tuhh.ict.pshdl.model.HDLDirectGeneration;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration;
import de.tuhh.ict.pshdl.model.HDLEnumRef;
import de.tuhh.ict.pshdl.model.HDLExpression;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLFunction;
import de.tuhh.ict.pshdl.model.HDLFunctionCall;
import de.tuhh.ict.pshdl.model.HDLIfStatement;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration;
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation;
import de.tuhh.ict.pshdl.model.HDLLiteral;
import de.tuhh.ict.pshdl.model.HDLObject;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPrimitive;
import de.tuhh.ict.pshdl.model.HDLRange;
import de.tuhh.ict.pshdl.model.HDLReference;
import de.tuhh.ict.pshdl.model.HDLRegisterConfig;
import de.tuhh.ict.pshdl.model.HDLResolvedRef;
import de.tuhh.ict.pshdl.model.HDLStatement;
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement;
import de.tuhh.ict.pshdl.model.HDLSwitchStatement;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.HDLVariableRef;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.evaluation.ConstantEvaluate;
import de.tuhh.ict.pshdl.model.extensions.FullNameExtension;
import de.tuhh.ict.pshdl.model.extensions.TypeExtension;
import de.tuhh.ict.pshdl.model.parser.SourceInfo;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import de.tuhh.ict.pshdl.model.utils.HDLQuery;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.FieldSelector;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Result;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Selector;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Source;
import de.tuhh.ict.pshdl.model.utils.Insulin;
import de.upb.hni.vmagic.AssociationElement;
import de.upb.hni.vmagic.Choices;
import de.upb.hni.vmagic.DiscreteRange;
import de.upb.hni.vmagic.Range;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.concurrent.ComponentInstantiation;
import de.upb.hni.vmagic.concurrent.ConcurrentStatement;
import de.upb.hni.vmagic.concurrent.EntityInstantiation;
import de.upb.hni.vmagic.concurrent.ForGenerateStatement;
import de.upb.hni.vmagic.declaration.Component;
import de.upb.hni.vmagic.declaration.ConstantDeclaration;
import de.upb.hni.vmagic.declaration.SignalDeclaration;
import de.upb.hni.vmagic.expression.Aggregate;
import de.upb.hni.vmagic.expression.Expression;
import de.upb.hni.vmagic.expression.Literal;
import de.upb.hni.vmagic.expression.TypeConversion;
import de.upb.hni.vmagic.libraryunit.Entity;
import de.upb.hni.vmagic.literal.CharacterLiteral;
import de.upb.hni.vmagic.object.Constant;
import de.upb.hni.vmagic.object.Signal;
import de.upb.hni.vmagic.object.SignalAssignmentTarget;
import de.upb.hni.vmagic.object.VhdlObject.Mode;
import de.upb.hni.vmagic.object.VhdlObjectProvider;
import de.upb.hni.vmagic.statement.CaseStatement;
import de.upb.hni.vmagic.statement.CaseStatement.Alternative;
import de.upb.hni.vmagic.statement.ForStatement;
import de.upb.hni.vmagic.statement.IfStatement;
import de.upb.hni.vmagic.statement.SequentialStatement;
import de.upb.hni.vmagic.statement.SignalAssignment;
import de.upb.hni.vmagic.type.ConstrainedArray;
import de.upb.hni.vmagic.type.EnumerationType;
import de.upb.hni.vmagic.type.SubtypeIndication;
import de.upb.hni.vmagic.type.UnresolvedType;
import de.upb.hni.vmagic.util.Comments;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.eclipse.xtext.xbase.lib.CollectionExtensions;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class VHDLStatementExtension {
  public static VHDLStatementExtension INST = new Function0<VHDLStatementExtension>() {
    public VHDLStatementExtension apply() {
      VHDLStatementExtension _vHDLStatementExtension = new VHDLStatementExtension();
      return _vHDLStatementExtension;
    }
  }.apply();
  
  public static VHDLContext vhdlOf(final HDLStatement stmnt, final int pid) {
    return VHDLStatementExtension.INST.toVHDL(stmnt, pid);
  }
  
  @Extension
  private VHDLExpressionExtension vee = new Function0<VHDLExpressionExtension>() {
    public VHDLExpressionExtension apply() {
      VHDLExpressionExtension _vHDLExpressionExtension = new VHDLExpressionExtension();
      return _vHDLExpressionExtension;
    }
  }.apply();
  
  private static GenericMeta<HDLQualifiedName> ORIGINAL_FULLNAME = new Function0<GenericMeta<HDLQualifiedName>>() {
    public GenericMeta<HDLQualifiedName> apply() {
      GenericMeta<HDLQualifiedName> _genericMeta = new GenericMeta<HDLQualifiedName>(
        "ORIGINAL_FULLNAME", true);
      return _genericMeta;
    }
  }.apply();
  
  public static GenericMeta<Boolean> EXPORT = new Function0<GenericMeta<Boolean>>() {
    public GenericMeta<Boolean> apply() {
      GenericMeta<Boolean> _genericMeta = new GenericMeta<Boolean>("EXPORT", true);
      return _genericMeta;
    }
  }.apply();
  
  protected VHDLContext _toVHDL(final HDLDirectGeneration obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    return _vHDLContext;
  }
  
  protected VHDLContext _toVHDL(final HDLFunctionCall obj, final int pid) {
    return HDLFunctions.toVHDL(obj, pid);
  }
  
  protected VHDLContext _toVHDL(final HDLBlock obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext res = _vHDLContext;
    boolean process = false;
    boolean _and = false;
    Boolean _process = obj.getProcess();
    boolean _tripleNotEquals = (_process != null);
    if (!_tripleNotEquals) {
      _and = false;
    } else {
      Boolean _process_1 = obj.getProcess();
      _and = (_tripleNotEquals && (_process_1).booleanValue());
    }
    if (_and) {
      process = true;
    }
    int _xifexpression = (int) 0;
    if (process) {
      int _newProcessID = res.newProcessID();
      _xifexpression = _newProcessID;
    } else {
      _xifexpression = pid;
    }
    final int newPid = _xifexpression;
    ArrayList<HDLStatement> _statements = obj.getStatements();
    for (final HDLStatement stmnt : _statements) {
      VHDLContext _vHDL = this.toVHDL(stmnt, newPid);
      res.merge(_vHDL, false);
    }
    return this.attachComment(res, obj);
  }
  
  public VHDLContext attachComment(final VHDLContext context, final IHDLObject block) {
    try {
      final SourceInfo srcInfo = block.<SourceInfo>getMeta(SourceInfo.INFO);
      boolean _and = false;
      boolean _tripleNotEquals = (srcInfo != null);
      if (!_tripleNotEquals) {
        _and = false;
      } else {
        SequentialStatement _statement = context.getStatement();
        boolean _tripleNotEquals_1 = (_statement != null);
        _and = (_tripleNotEquals && _tripleNotEquals_1);
      }
      if (_and) {
        ArrayList<String> _arrayList = new ArrayList<String>();
        final ArrayList<String> newComments = _arrayList;
        for (final String comment : srcInfo.comments) {
          boolean _startsWith = comment.startsWith("//");
          if (_startsWith) {
            int _length = comment.length();
            int _minus = (_length - 1);
            String _substring = comment.substring(2, _minus);
            newComments.add(_substring);
          } else {
            int _length_1 = comment.length();
            int _minus_1 = (_length_1 - 2);
            final String newComment = comment.substring(2, _minus_1);
            String[] _split = newComment.split("\n");
            CollectionExtensions.<String>addAll(newComments, _split);
          }
        }
        SequentialStatement _statement_1 = context.getStatement();
        Comments.setComments(_statement_1, newComments);
      }
    } catch (final Throwable _t) {
      if (_t instanceof Exception) {
        final Exception e = (Exception)_t;
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    return context;
  }
  
  protected VHDLContext _toVHDL(final HDLEnumDeclaration obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext res = _vHDLContext;
    final HDLEnum hEnum = obj.getHEnum();
    LinkedList<String> _linkedList = new LinkedList<String>();
    final List<String> enums = _linkedList;
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    for (final HDLVariable hVar : _enums) {
      String _name = hVar.getName();
      enums.add(_name);
    }
    final String[] enumArr = ((String[])Conversions.unwrapArray(enums, String.class));
    String _name_1 = hEnum.getName();
    EnumerationType _enumerationType = new EnumerationType(_name_1, enumArr);
    res.addInternalTypeDeclaration(_enumerationType);
    return this.attachComment(res, obj);
  }
  
  protected VHDLContext _toVHDL(final HDLInterfaceDeclaration obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    return _vHDLContext;
  }
  
  private static EnumSet<HDLDirection> inAndOut = new Function0<EnumSet<HDLDirection>>() {
    public EnumSet<HDLDirection> apply() {
      EnumSet<HDLDirection> _of = EnumSet.<HDLDirection>of(HDLDirection.IN, HDLDirection.INOUT, HDLDirection.OUT);
      return _of;
    }
  }.apply();
  
  protected VHDLContext _toVHDL(final HDLInterfaceInstantiation obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext res = _vHDLContext;
    Optional<HDLInterface> _resolveHIf = obj.resolveHIf();
    final HDLInterface hIf = _resolveHIf.get();
    final HDLVariable hVar = obj.getVar();
    HDLVariable _var = obj.getVar();
    final String ifName = _var.getName();
    final HDLQualifiedName asRef = hIf.asRef();
    final HDLInterfaceDeclaration hid = hIf.<HDLInterfaceDeclaration>getContainer(HDLInterfaceDeclaration.class);
    List<AssociationElement> portMap = null;
    List<AssociationElement> genericMap = null;
    ConcurrentStatement instantiation = null;
    final ArrayList<HDLVariableDeclaration> ports = hIf.getPorts();
    boolean _and = false;
    boolean _tripleNotEquals = (hid != null);
    if (!_tripleNotEquals) {
      _and = false;
    } else {
      HDLAnnotation _annotation = hid.getAnnotation(HDLBuiltInAnnotations.VHDLComponent);
      boolean _tripleNotEquals_1 = (_annotation != null);
      _and = (_tripleNotEquals && _tripleNotEquals_1);
    }
    if (_and) {
      final HDLAnnotation anno = hid.getAnnotation(HDLBuiltInAnnotations.VHDLComponent);
      String _value = anno.getValue();
      boolean _equals = "declare".equals(_value);
      if (_equals) {
        String _lastSegment = asRef.getLastSegment();
        String _string = _lastSegment.toString();
        Component _component = new Component(_string);
        final Component c = _component;
        VHDLContext _vHDLContext_1 = new VHDLContext();
        final VHDLContext cContext = _vHDLContext_1;
        for (final HDLVariableDeclaration port : ports) {
          int _minus = (-1);
          VHDLContext _vHDL = this.toVHDL(port, _minus);
          cContext.merge(_vHDL, true);
        }
        for (final Signal signal : cContext.ports) {
          List<VhdlObjectProvider<Signal>> _port = c.getPort();
          _port.add(signal);
        }
        for (final ConstantDeclaration cd : cContext.constants) {
          List<Constant> _objects = cd.getObjects();
          for (final Object vobj : _objects) {
            List<VhdlObjectProvider<Constant>> _generic = c.getGeneric();
            _generic.add(((Constant) vobj));
          }
        }
        for (final Constant constant : cContext.generics) {
          List<VhdlObjectProvider<Constant>> _generic_1 = c.getGeneric();
          _generic_1.add(constant);
        }
        res.addComponent(c);
      } else {
        HDLQualifiedName _nameRef = VHDLPackageExtension.INST.getNameRef(asRef);
        res.addImport(_nameRef);
      }
      String _lastSegment_1 = asRef.getLastSegment();
      String _string_1 = _lastSegment_1.toString();
      Component _component_1 = new Component(_string_1);
      final Component entity = _component_1;
      ComponentInstantiation _componentInstantiation = new ComponentInstantiation(ifName, entity);
      final ComponentInstantiation inst = _componentInstantiation;
      List<AssociationElement> _portMap = inst.getPortMap();
      portMap = _portMap;
      List<AssociationElement> _genericMap = inst.getGenericMap();
      genericMap = _genericMap;
      instantiation = inst;
    } else {
      HDLQualifiedName _nameRef_1 = VHDLPackageExtension.INST.getNameRef(asRef);
      String _string_2 = _nameRef_1.toString();
      Entity _entity = new Entity(_string_2);
      final Entity entity_1 = _entity;
      EntityInstantiation _entityInstantiation = new EntityInstantiation(ifName, entity_1);
      final EntityInstantiation inst_1 = _entityInstantiation;
      List<AssociationElement> _portMap_1 = inst_1.getPortMap();
      portMap = _portMap_1;
      List<AssociationElement> _genericMap_1 = inst_1.getGenericMap();
      genericMap = _genericMap_1;
      instantiation = inst_1;
    }
    for (final HDLVariableDeclaration hvd : ports) {
      HDLDirection _direction = hvd.getDirection();
      boolean _contains = VHDLStatementExtension.inAndOut.contains(_direction);
      if (_contains) {
        Source<HDLAnnotation> _select = HDLQuery.<HDLAnnotation>select(HDLAnnotation.class);
        Selector<HDLAnnotation> _from = _select.from(hvd);
        FieldSelector<HDLAnnotation,String> _where = _from.<String>where(
          HDLAnnotation.fName);
        String _string_3 = HDLBuiltInAnnotations.VHDLType.toString();
        Result<HDLAnnotation,String> _isEqualTo = _where.isEqualTo(_string_3);
        final Collection<HDLAnnotation> typeAnno = _isEqualTo.getAll();
        ArrayList<HDLVariable> _variables = hvd.getVariables();
        for (final HDLVariable hvar : _variables) {
          {
            String _plus = (ifName + "_");
            String _name = hvar.getName();
            String _plus_1 = (_plus + _name);
            HDLVariable sigVar = hvar.setName(_plus_1);
            HDLVariableRef ref = sigVar.asHDLRef();
            int i = 0;
            ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
            for (final HDLExpression exp : _dimensions) {
              {
                HDLVariableRef _hDLVariableRef = new HDLVariableRef();
                String _asIndex = this.asIndex(Integer.valueOf(i));
                HDLQualifiedName _create = HDLQualifiedName.create(_asIndex);
                HDLVariableRef _setVar = _hDLVariableRef.setVar(_create);
                HDLVariableRef _addArray = ref.addArray(_setVar);
                ref = _addArray;
                int _plus_2 = (i + 1);
                i = _plus_2;
              }
            }
            ArrayList<HDLExpression> _dimensions_1 = hVar.getDimensions();
            for (final HDLExpression exp_1 : _dimensions_1) {
              HDLVariable _addDimensions = sigVar.addDimensions(exp_1);
              sigVar = _addDimensions;
            }
            ArrayList<HDLExpression> _dimensions_2 = hvar.getDimensions();
            int _size = _dimensions_2.size();
            boolean _notEquals = (_size != 0);
            if (_notEquals) {
              boolean _isEmpty = typeAnno.isEmpty();
              if (_isEmpty) {
                HDLQualifiedName _packageNameRef = VHDLPackageExtension.INST.getPackageNameRef(asRef);
                String _arrayRefName = VHDLStatementExtension.getArrayRefName(hvar, true);
                final HDLQualifiedName name = _packageNameRef.append(_arrayRefName);
                res.addImport(name);
                HDLVariableDeclaration _setDirection = hvd.setDirection(HDLDirection.INTERNAL);
                HDLVariable _setDimensions = sigVar.setDimensions(null);
                String _string_4 = name.toString();
                HDLAnnotation _create = HDLBuiltInAnnotations.VHDLType.create(_string_4);
                HDLVariable _addAnnotations = _setDimensions.addAnnotations(_create);
                Iterable<HDLVariable> _asList = HDLObject.<HDLVariable>asList(_addAnnotations);
                HDLVariableDeclaration _setVariables = _setDirection.setVariables(_asList);
                final HDLVariableDeclaration newHVD = _setVariables.copyDeepFrozen(obj);
                VHDLContext _vHDL_1 = this.toVHDL(newHVD, pid);
                res.merge(_vHDL_1, false);
              } else {
                HDLVariableDeclaration _setDirection_1 = hvd.setDirection(HDLDirection.INTERNAL);
                HDLVariable _setDimensions_1 = sigVar.setDimensions(null);
                Iterable<HDLVariable> _asList_1 = HDLObject.<HDLVariable>asList(_setDimensions_1);
                HDLVariableDeclaration _setVariables_1 = _setDirection_1.setVariables(_asList_1);
                final HDLVariableDeclaration newHVD_1 = _setVariables_1.copyDeepFrozen(obj);
                VHDLContext _vHDL_2 = this.toVHDL(newHVD_1, pid);
                res.merge(_vHDL_2, false);
              }
            } else {
              HDLVariableDeclaration _setDirection_2 = hvd.setDirection(HDLDirection.INTERNAL);
              Iterable<HDLVariable> _asList_2 = HDLObject.<HDLVariable>asList(sigVar);
              HDLVariableDeclaration _setVariables_2 = _setDirection_2.setVariables(_asList_2);
              final HDLVariableDeclaration newHVD_2 = _setVariables_2.copyDeepFrozen(obj);
              VHDLContext _vHDL_3 = this.toVHDL(newHVD_2, pid);
              res.merge(_vHDL_3, false);
            }
            String _name_1 = hvar.getName();
            String _vHDLName = VHDLOutputValidator.getVHDLName(_name_1);
            Expression<? extends Object> _vHDL_4 = this.vee.toVHDL(ref);
            AssociationElement _associationElement = new AssociationElement(_vHDLName, _vHDL_4);
            portMap.add(_associationElement);
          }
        }
      } else {
        HDLDirection _direction_1 = hvd.getDirection();
        boolean _equals_1 = Objects.equal(_direction_1, HDLDirection.PARAMETER);
        if (_equals_1) {
          ArrayList<HDLVariable> _variables_1 = hvd.getVariables();
          for (final HDLVariable hvar_1 : _variables_1) {
            {
              HDLVariable sigVar = hvar_1;
              String _meta = hvar_1.<String>getMeta(HDLInterfaceInstantiation.ORIG_NAME);
              boolean _tripleNotEquals_2 = (_meta != null);
              if (_tripleNotEquals_2) {
                String _meta_1 = hvar_1.<String>getMeta(HDLInterfaceInstantiation.ORIG_NAME);
                HDLVariable _setName = hvar_1.setName(_meta_1);
                sigVar = _setName;
              }
              final HDLVariableRef ref = hvar_1.asHDLRef();
              String _name = sigVar.getName();
              Expression<? extends Object> _vHDL_1 = this.vee.toVHDL(ref);
              AssociationElement _associationElement = new AssociationElement(_name, _vHDL_1);
              genericMap.add(_associationElement);
            }
          }
        }
      }
    }
    ForGenerateStatement forLoop = null;
    ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
    int _size = _dimensions.size();
    boolean _equals_2 = (_size == 0);
    if (_equals_2) {
      res.addConcurrentStatement(instantiation);
    } else {
      int i = 0;
      ArrayList<HDLExpression> _dimensions_1 = hVar.getDimensions();
      for (final HDLExpression exp : _dimensions_1) {
        {
          HDLArithOp _hDLArithOp = new HDLArithOp();
          ArrayList<HDLExpression> _dimensions_2 = hVar.getDimensions();
          HDLExpression _get = _dimensions_2.get(i);
          HDLArithOp _setLeft = _hDLArithOp.setLeft(_get);
          HDLArithOp _setType = _setLeft.setType(
            HDLArithOpType.MINUS);
          HDLLiteral _get_1 = HDLLiteral.get(1);
          final HDLExpression to = _setType.setRight(_get_1);
          HDLRange _hDLRange = new HDLRange();
          HDLLiteral _get_2 = HDLLiteral.get(0);
          HDLRange _setFrom = _hDLRange.setFrom(_get_2);
          HDLRange _setTo = _setFrom.setTo(to);
          final HDLRange range = _setTo.setContainer(obj);
          String _plus = ("generate_" + ifName);
          String _asIndex = this.asIndex(Integer.valueOf(i));
          Range _vHDL_1 = this.vee.toVHDL(range, Direction.TO);
          ForGenerateStatement _forGenerateStatement = new ForGenerateStatement(_plus, _asIndex, _vHDL_1);
          final ForGenerateStatement newFor = _forGenerateStatement;
          boolean _tripleNotEquals_2 = (forLoop != null);
          if (_tripleNotEquals_2) {
            List<ConcurrentStatement> _statements = forLoop.getStatements();
            _statements.add(newFor);
          } else {
            res.addConcurrentStatement(newFor);
          }
          forLoop = newFor;
          int _plus_1 = (i + 1);
          i = _plus_1;
        }
      }
      boolean _tripleEquals = (forLoop == null);
      if (_tripleEquals) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Should not get here");
        throw _illegalArgumentException;
      }
      List<ConcurrentStatement> _statements = forLoop.getStatements();
      _statements.add(instantiation);
    }
    return this.attachComment(res, obj);
  }
  
  public String asIndex(final Integer integer) {
    final int i = "I".charAt(0);
    int _plus = (i + (integer).intValue());
    return Character.toString(((char) _plus));
  }
  
  public static String getArrayRefName(final HDLVariable hvar, final boolean external) {
    if (external) {
      HDLQualifiedName fullName = null;
      HDLQualifiedName _meta = hvar.<HDLQualifiedName>getMeta(VHDLStatementExtension.ORIGINAL_FULLNAME);
      boolean _tripleNotEquals = (_meta != null);
      if (_tripleNotEquals) {
        HDLQualifiedName _meta_1 = hvar.<HDLQualifiedName>getMeta(VHDLStatementExtension.ORIGINAL_FULLNAME);
        fullName = _meta_1;
      } else {
        HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hvar);
        fullName = _fullNameOf;
      }
      char _charAt = "_".charAt(0);
      String _string = fullName.toString(_charAt);
      return (_string + "_array");
    }
    String _name = hvar.getName();
    return (_name + "_array");
  }
  
  protected VHDLContext _toVHDL(final HDLVariableDeclaration obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext res = _vHDLContext;
    final HDLPrimitive primitive = obj.getPrimitive();
    SubtypeIndication type = null;
    HDLExpression resetValue = null;
    Source<HDLAnnotation> _select = HDLQuery.<HDLAnnotation>select(HDLAnnotation.class);
    Selector<HDLAnnotation> _from = _select.from(obj);
    FieldSelector<HDLAnnotation,String> _where = _from.<String>where(HDLAnnotation.fName);
    String _string = HDLBuiltInAnnotations.VHDLType.toString();
    Result<HDLAnnotation,String> _isEqualTo = _where.isEqualTo(_string);
    final HDLAnnotation typeAnno = _isEqualTo.getFirst();
    boolean _tripleNotEquals = (typeAnno != null);
    if (_tripleNotEquals) {
      String _value = typeAnno.getValue();
      HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_value);
      final HDLQualifiedName value = _hDLQualifiedName;
      res.addImport(value);
      String _lastSegment = value.getLastSegment();
      EnumerationType _enumerationType = new EnumerationType(_lastSegment);
      type = _enumerationType;
    } else {
      boolean _tripleNotEquals_1 = (primitive != null);
      if (_tripleNotEquals_1) {
        SubtypeIndication _type = VHDLCastsLibrary.getType(primitive);
        type = _type;
        HDLRegisterConfig _register = obj.getRegister();
        boolean _tripleNotEquals_2 = (_register != null);
        if (_tripleNotEquals_2) {
          HDLRegisterConfig _register_1 = obj.getRegister();
          HDLExpression _resetValue = _register_1.getResetValue();
          resetValue = _resetValue;
        }
      } else {
        Optional<? extends HDLType> _resolveType = obj.resolveType();
        final HDLType hType = _resolveType.get();
        if ((hType instanceof HDLEnum)) {
          final HDLEnum hEnum = ((HDLEnum) hType);
          String _name = hEnum.getName();
          EnumerationType _enumerationType_1 = new EnumerationType(_name);
          type = _enumerationType_1;
          HDLEnumRef _hDLEnumRef = new HDLEnumRef();
          HDLQualifiedName _asRef = hEnum.asRef();
          HDLEnumRef _setHEnum = _hDLEnumRef.setHEnum(_asRef);
          ArrayList<HDLVariable> _enums = hEnum.getEnums();
          HDLVariable _get = _enums.get(0);
          HDLQualifiedName _asRef_1 = _get.asRef();
          HDLEnumRef _setVar = _setHEnum.setVar(_asRef_1);
          resetValue = _setVar;
          resetValue.setContainer(obj);
        }
      }
    }
    boolean _tripleNotEquals_3 = (type != null);
    if (_tripleNotEquals_3) {
      ArrayList<HDLVariable> _variables = obj.getVariables();
      for (final HDLVariable hvar : _variables) {
        {
          HDLAnnotation _annotation = hvar.getAnnotation(HDLBuiltInAnnotations.VHDLNoExplicitReset);
          final boolean noExplicitResetVar = (_annotation != null);
          SubtypeIndication varType = type;
          ArrayList<HDLExpression> _dimensions = hvar.getDimensions();
          int _size = _dimensions.size();
          boolean _notEquals = (_size != 0);
          if (_notEquals) {
            LinkedList<DiscreteRange<? extends Object>> _linkedList = new LinkedList<DiscreteRange<? extends Object>>();
            final LinkedList<DiscreteRange<? extends Object>> ranges = _linkedList;
            ArrayList<HDLExpression> _dimensions_1 = hvar.getDimensions();
            for (final HDLExpression arrayWidth : _dimensions_1) {
              {
                HDLArithOp _hDLArithOp = new HDLArithOp();
                HDLArithOp _setLeft = _hDLArithOp.setLeft(arrayWidth);
                HDLArithOp _setType = _setLeft.setType(
                  HDLArithOpType.MINUS);
                HDLLiteral _get_1 = HDLLiteral.get(1);
                final HDLExpression newWidth = _setType.setRight(_get_1);
                HDLRange _hDLRange = new HDLRange();
                HDLLiteral _get_2 = HDLLiteral.get(0);
                HDLRange _setFrom = _hDLRange.setFrom(_get_2);
                HDLRange _setTo = _setFrom.setTo(newWidth);
                HDLRange _copyDeepFrozen = _setTo.copyDeepFrozen(obj);
                final Range range = this.vee.toVHDL(_copyDeepFrozen, Direction.TO);
                ranges.add(range);
              }
            }
            final boolean external = obj.isExternal();
            final DiscreteRange[] arrRangs = ((DiscreteRange[])Conversions.unwrapArray(ranges, DiscreteRange.class));
            String _arrayRefName = VHDLStatementExtension.getArrayRefName(hvar, external);
            ConstrainedArray _constrainedArray = new ConstrainedArray(_arrayRefName, type, arrRangs);
            final ConstrainedArray arrType = _constrainedArray;
            res.addTypeDeclaration(arrType, external);
            varType = arrType;
          }
          boolean _and = false;
          boolean _tripleNotEquals_4 = (resetValue != null);
          if (!_tripleNotEquals_4) {
            _and = false;
          } else {
            boolean _not = (!noExplicitResetVar);
            _and = (_tripleNotEquals_4 && _not);
          }
          if (_and) {
            boolean synchedArray = false;
            if ((resetValue instanceof HDLVariableRef)) {
              final HDLVariableRef ref = ((HDLVariableRef) resetValue);
              Optional<HDLVariable> _resolveVar = ref.resolveVar();
              HDLVariable _get_1 = _resolveVar.get();
              ArrayList<HDLExpression> _dimensions_2 = _get_1.getDimensions();
              int _size_1 = _dimensions_2.size();
              boolean _notEquals_1 = (_size_1 != 0);
              synchedArray = _notEquals_1;
            }
            ArrayList<HDLExpression> _dimensions_3 = hvar.getDimensions();
            HDLVariableRef _hDLVariableRef = new HDLVariableRef();
            HDLQualifiedName _asRef_2 = hvar.asRef();
            HDLVariableRef _setVar_1 = _hDLVariableRef.setVar(_asRef_2);
            HDLStatement _createArrayForLoop = Insulin.createArrayForLoop(_dimensions_3, 0, resetValue, _setVar_1, synchedArray);
            final HDLStatement initLoop = _createArrayForLoop.copyDeepFrozen(obj);
            final VHDLContext vhdl = this.toVHDL(initLoop, pid);
            HDLRegisterConfig _register_2 = obj.getRegister();
            SequentialStatement _statement = vhdl.getStatement();
            res.addResetValue(_register_2, _statement);
          }
          String _name_1 = hvar.getName();
          Signal _signal = new Signal(_name_1, varType);
          final Signal s = _signal;
          String _name_2 = hvar.getName();
          Constant _constant = new Constant(_name_2, varType);
          final Constant constant = _constant;
          HDLExpression _defaultValue = hvar.getDefaultValue();
          boolean _tripleNotEquals_5 = (_defaultValue != null);
          if (_tripleNotEquals_5) {
            HDLExpression _defaultValue_1 = hvar.getDefaultValue();
            Expression<? extends Object> _vHDL = this.vee.toVHDL(_defaultValue_1);
            constant.setDefaultValue(_vHDL);
          }
          if (noExplicitResetVar) {
            char _charAt = "0".charAt(0);
            CharacterLiteral _characterLiteral = new CharacterLiteral(_charAt);
            Aggregate assign = Aggregate.OTHERS(_characterLiteral);
            ArrayList<HDLExpression> _dimensions_4 = hvar.getDimensions();
            for (final HDLExpression exp : _dimensions_4) {
              Aggregate _OTHERS = Aggregate.OTHERS(assign);
              assign = _OTHERS;
            }
            s.setDefaultValue(assign);
          }
          HDLDirection _direction = obj.getDirection();
          final HDLDirection _switchValue = _direction;
          boolean _matched = false;
          if (!_matched) {
            if (Objects.equal(_switchValue,HDLDirection.IN)) {
              _matched=true;
              s.setMode(Mode.IN);
              res.addPortDeclaration(s);
            }
          }
          if (!_matched) {
            if (Objects.equal(_switchValue,HDLDirection.OUT)) {
              _matched=true;
              s.setMode(Mode.OUT);
              res.addPortDeclaration(s);
            }
          }
          if (!_matched) {
            if (Objects.equal(_switchValue,HDLDirection.INOUT)) {
              _matched=true;
              s.setMode(Mode.INOUT);
              res.addPortDeclaration(s);
            }
          }
          if (!_matched) {
            if (Objects.equal(_switchValue,HDLDirection.INTERNAL)) {
              _matched=true;
              SignalDeclaration _signalDeclaration = new SignalDeclaration(s);
              final SignalDeclaration sd = _signalDeclaration;
              res.addInternalSignalDeclaration(sd);
            }
          }
          if (!_matched) {
            boolean _or = false;
            HDLDirection _direction_1 = obj.getDirection();
            boolean _equals = Objects.equal(_direction_1, HDLDirection.HIDDEN);
            if (_equals) {
              _or = true;
            } else {
              HDLDirection _direction_2 = obj.getDirection();
              boolean _equals_1 = Objects.equal(_direction_2, HDLDirection.CONSTANT);
              _or = (_equals || _equals_1);
            }
            if (_or) {
              _matched=true;
              ConstantDeclaration _constantDeclaration = new ConstantDeclaration(constant);
              final ConstantDeclaration cd = _constantDeclaration;
              boolean _hasMeta = hvar.hasMeta(VHDLStatementExtension.EXPORT);
              if (_hasMeta) {
                res.addConstantDeclarationPkg(cd);
              } else {
                res.addConstantDeclaration(cd);
              }
            }
          }
          if (!_matched) {
            if (Objects.equal(_switchValue,HDLDirection.PARAMETER)) {
              _matched=true;
              res.addGenericDeclaration(constant);
            }
          }
        }
      }
    }
    return this.attachComment(res, obj);
  }
  
  protected VHDLContext _toVHDL(final HDLSwitchStatement obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext context = _vHDLContext;
    final HDLExpression hCaseExp = obj.getCaseExp();
    Optional<BigInteger> width = Optional.<BigInteger>absent();
    final Optional<? extends HDLType> type = TypeExtension.typeOf(hCaseExp);
    boolean _and = false;
    boolean _isPresent = type.isPresent();
    if (!_isPresent) {
      _and = false;
    } else {
      HDLType _get = type.get();
      _and = (_isPresent && (_get instanceof HDLPrimitive));
    }
    if (_and) {
      HDLType _get_1 = type.get();
      HDLExpression _width = ((HDLPrimitive) _get_1).getWidth();
      Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(_width, null);
      width = _valueOf;
      boolean _isPresent_1 = width.isPresent();
      boolean _not = (!_isPresent_1);
      if (_not) {
        IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("HDLPrimitive switch case needs to have constant width");
        throw _illegalArgumentException;
      }
    }
    final Expression<?> caseExp = this.vee.toVHDL(hCaseExp);
    LinkedHashMap<HDLSwitchCaseStatement,VHDLContext> _linkedHashMap = new LinkedHashMap<HDLSwitchCaseStatement,VHDLContext>();
    final Map<HDLSwitchCaseStatement,VHDLContext> ctxs = _linkedHashMap;
    HashSet<HDLRegisterConfig> _hashSet = new HashSet<HDLRegisterConfig>();
    final Set<HDLRegisterConfig> configs = _hashSet;
    boolean hasUnclocked = false;
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement cs : _cases) {
      {
        final VHDLContext vhdl = this.toVHDL(cs, pid);
        ctxs.put(cs, vhdl);
        int _size = vhdl.unclockedStatements.size();
        boolean _greaterThan = (_size > 0);
        if (_greaterThan) {
          hasUnclocked = true;
        }
        Set<HDLRegisterConfig> _keySet = vhdl.clockedStatements.keySet();
        configs.addAll(_keySet);
      }
    }
    for (final HDLRegisterConfig hdlRegisterConfig : configs) {
      {
        CaseStatement _caseStatement = new CaseStatement(caseExp);
        final CaseStatement cs_1 = _caseStatement;
        Set<Entry<HDLSwitchCaseStatement,VHDLContext>> _entrySet = ctxs.entrySet();
        for (final Entry<HDLSwitchCaseStatement,VHDLContext> e : _entrySet) {
          {
            final Alternative alt = this.createAlternative(cs_1, e, width);
            VHDLContext _value = e.getValue();
            final LinkedList<SequentialStatement> clockCase = _value.clockedStatements.get(hdlRegisterConfig);
            boolean _tripleNotEquals = (clockCase != null);
            if (_tripleNotEquals) {
              List<SequentialStatement> _statements = alt.getStatements();
              _statements.addAll(clockCase);
            }
          }
        }
        context.addClockedStatement(hdlRegisterConfig, cs_1);
      }
    }
    if (hasUnclocked) {
      CaseStatement _caseStatement = new CaseStatement(caseExp);
      final CaseStatement cs_1 = _caseStatement;
      Set<Entry<HDLSwitchCaseStatement,VHDLContext>> _entrySet = ctxs.entrySet();
      for (final Entry<HDLSwitchCaseStatement,VHDLContext> e : _entrySet) {
        {
          final Alternative alt = this.createAlternative(cs_1, e, width);
          VHDLContext _value = e.getValue();
          LinkedList<SequentialStatement> _get_2 = _value.unclockedStatements.get(Integer.valueOf(pid));
          boolean _tripleNotEquals = (_get_2 != null);
          if (_tripleNotEquals) {
            List<SequentialStatement> _statements = alt.getStatements();
            VHDLContext _value_1 = e.getValue();
            LinkedList<SequentialStatement> _get_3 = _value_1.unclockedStatements.get(Integer.valueOf(pid));
            _statements.addAll(_get_3);
          }
        }
      }
      context.addUnclockedStatement(pid, cs_1, obj);
    }
    return this.attachComment(context, obj);
  }
  
  private Alternative createAlternative(final CaseStatement cs, final Entry<HDLSwitchCaseStatement,VHDLContext> e, final Optional<BigInteger> bits) {
    Alternative alt = null;
    HDLSwitchCaseStatement _key = e.getKey();
    final HDLExpression label = _key.getLabel();
    boolean _tripleNotEquals = (label != null);
    if (_tripleNotEquals) {
      final Optional<BigInteger> eval = ConstantEvaluate.valueOf(label, null);
      boolean _isPresent = eval.isPresent();
      if (_isPresent) {
        boolean _isPresent_1 = bits.isPresent();
        boolean _not = (!_isPresent_1);
        if (_not) {
          IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("The width needs to be known for primitive types!");
          throw _illegalArgumentException;
        }
        BigInteger _get = bits.get();
        int _intValue = _get.intValue();
        BigInteger _get_1 = eval.get();
        Literal<? extends Object> _binaryLiteral = VHDLUtils.toBinaryLiteral(_intValue, _get_1);
        Alternative _createAlternative = cs.createAlternative(_binaryLiteral);
        alt = _createAlternative;
      } else {
        Expression<? extends Object> _vHDL = this.vee.toVHDL(label);
        Alternative _createAlternative_1 = cs.createAlternative(_vHDL);
        alt = _createAlternative_1;
      }
    } else {
      Alternative _createAlternative_2 = cs.createAlternative(Choices.OTHERS);
      alt = _createAlternative_2;
    }
    return alt;
  }
  
  protected VHDLContext _toVHDL(final HDLSwitchCaseStatement obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext res = _vHDLContext;
    ArrayList<HDLStatement> _dos = obj.getDos();
    for (final HDLStatement stmnt : _dos) {
      VHDLContext _vHDL = this.toVHDL(stmnt, pid);
      res.merge(_vHDL, false);
    }
    return this.attachComment(res, obj);
  }
  
  protected VHDLContext _toVHDL(final HDLAssignment obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext context = _vHDLContext;
    SignalAssignment sa = null;
    final HDLReference ref = obj.getLeft();
    final HDLVariable hvar = this.resolveVar(ref);
    final ArrayList<HDLExpression> dim = hvar.getDimensions();
    boolean _and = false;
    int _size = dim.size();
    boolean _notEquals = (_size != 0);
    if (!_notEquals) {
      _and = false;
    } else {
      HDLClass _classType = ref.getClassType();
      boolean _equals = Objects.equal(_classType, HDLClass.HDLVariableRef);
      _and = (_notEquals && _equals);
    }
    if (_and) {
      final HDLVariableRef varRef = ((HDLVariableRef) ref);
      ArrayList<HDLExpression> _array = varRef.getArray();
      for (final HDLExpression exp : _array) {
        dim.remove(0);
      }
      boolean _and_1 = false;
      int _size_1 = dim.size();
      boolean _notEquals_1 = (_size_1 != 0);
      if (!_notEquals_1) {
        _and_1 = false;
      } else {
        HDLExpression _right = obj.getRight();
        HDLClass _classType_1 = _right.getClassType();
        boolean _notEquals_2 = (!Objects.equal(_classType_1, HDLClass.HDLArrayInit));
        _and_1 = (_notEquals_1 && _notEquals_2);
      }
      if (_and_1) {
        final HDLAnnotation typeAnno = hvar.getAnnotation(HDLBuiltInAnnotations.VHDLType);
        boolean _tripleNotEquals = (typeAnno != null);
        if (_tripleNotEquals) {
          Expression<? extends Object> _vHDL = this.vee.toVHDL(ref);
          String _value = typeAnno.getValue();
          UnresolvedType _unresolvedType = new UnresolvedType(_value);
          HDLExpression _right_1 = obj.getRight();
          Expression<? extends Object> _vHDL_1 = this.vee.toVHDL(_right_1);
          TypeConversion _typeConversion = new TypeConversion(_unresolvedType, _vHDL_1);
          SignalAssignment _signalAssignment = new SignalAssignment(((SignalAssignmentTarget) _vHDL), _typeConversion);
          sa = _signalAssignment;
        } else {
          final HDLVariableDeclaration hvd = hvar.<HDLVariableDeclaration>getContainer(HDLVariableDeclaration.class);
          Expression<? extends Object> _vHDL_2 = this.vee.toVHDL(ref);
          boolean _isExternal = hvd.isExternal();
          String _arrayRefName = VHDLStatementExtension.getArrayRefName(hvar, _isExternal);
          UnresolvedType _unresolvedType_1 = new UnresolvedType(_arrayRefName);
          HDLExpression _right_2 = obj.getRight();
          Expression<? extends Object> _vHDL_3 = this.vee.toVHDL(_right_2);
          TypeConversion _typeConversion_1 = new TypeConversion(_unresolvedType_1, _vHDL_3);
          SignalAssignment _signalAssignment_1 = new SignalAssignment(((SignalAssignmentTarget) _vHDL_2), _typeConversion_1);
          sa = _signalAssignment_1;
        }
      } else {
        Expression<? extends Object> _vHDL_4 = this.vee.toVHDL(ref);
        HDLExpression _right_3 = obj.getRight();
        Expression<? extends Object> _vHDL_5 = this.vee.toVHDL(_right_3);
        SignalAssignment _signalAssignment_2 = new SignalAssignment(((SignalAssignmentTarget) _vHDL_4), _vHDL_5);
        sa = _signalAssignment_2;
      }
    } else {
      Expression<? extends Object> _vHDL_6 = this.vee.toVHDL(ref);
      HDLExpression _right_4 = obj.getRight();
      Expression<? extends Object> _vHDL_7 = this.vee.toVHDL(_right_4);
      SignalAssignment _signalAssignment_3 = new SignalAssignment(((SignalAssignmentTarget) _vHDL_6), _vHDL_7);
      sa = _signalAssignment_3;
    }
    final HDLRegisterConfig config = hvar.getRegisterConfig();
    boolean _tripleNotEquals_1 = (config != null);
    if (_tripleNotEquals_1) {
      context.addClockedStatement(config, sa);
    } else {
      context.addUnclockedStatement(pid, sa, obj);
    }
    return this.attachComment(context, obj);
  }
  
  public HDLVariable resolveVar(final HDLReference reference) {
    if ((reference instanceof HDLUnresolvedFragment)) {
      RuntimeException _runtimeException = new RuntimeException("Can not use unresolved fragments");
      throw _runtimeException;
    }
    Optional<HDLVariable> _resolveVar = ((HDLResolvedRef) reference).resolveVar();
    return _resolveVar.get();
  }
  
  protected VHDLContext _toVHDL(final HDLForLoop obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext context = _vHDLContext;
    ArrayList<HDLStatement> _dos = obj.getDos();
    for (final HDLStatement stmnt : _dos) {
      VHDLContext _vHDL = this.toVHDL(stmnt, pid);
      context.merge(_vHDL, false);
    }
    VHDLContext _vHDLContext_1 = new VHDLContext();
    final VHDLContext res = _vHDLContext_1;
    res.merge(context, true);
    Set<Entry<HDLRegisterConfig,LinkedList<SequentialStatement>>> _entrySet = context.clockedStatements.entrySet();
    for (final Entry<HDLRegisterConfig,LinkedList<SequentialStatement>> e : _entrySet) {
      {
        HDLVariable _param = obj.getParam();
        String _name = _param.getName();
        ArrayList<HDLRange> _range = obj.getRange();
        HDLRange _get = _range.get(0);
        Range _vHDL_1 = this.vee.toVHDL(_get, Direction.TO);
        ForStatement _forStatement = new ForStatement(_name, _vHDL_1);
        final ForStatement fStmnt = _forStatement;
        List<SequentialStatement> _statements = fStmnt.getStatements();
        LinkedList<SequentialStatement> _value = e.getValue();
        _statements.addAll(_value);
        HDLRegisterConfig _key = e.getKey();
        res.addClockedStatement(_key, fStmnt);
      }
    }
    LinkedList<SequentialStatement> _get = context.unclockedStatements.get(Integer.valueOf(pid));
    boolean _tripleNotEquals = (_get != null);
    if (_tripleNotEquals) {
      HDLVariable _param = obj.getParam();
      String _name = _param.getName();
      ArrayList<HDLRange> _range = obj.getRange();
      HDLRange _get_1 = _range.get(0);
      Range _vHDL_1 = this.vee.toVHDL(_get_1, Direction.TO);
      ForStatement _forStatement = new ForStatement(_name, _vHDL_1);
      final ForStatement fStmnt = _forStatement;
      List<SequentialStatement> _statements = fStmnt.getStatements();
      LinkedList<SequentialStatement> _get_2 = context.unclockedStatements.get(Integer.valueOf(pid));
      _statements.addAll(_get_2);
      res.addUnclockedStatement(pid, fStmnt, obj);
    }
    return this.attachComment(res, obj);
  }
  
  protected VHDLContext _toVHDL(final HDLIfStatement obj, final int pid) {
    VHDLContext _vHDLContext = new VHDLContext();
    final VHDLContext thenCtx = _vHDLContext;
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    for (final HDLStatement stmnt : _thenDo) {
      VHDLContext _vHDL = this.toVHDL(stmnt, pid);
      thenCtx.merge(_vHDL, false);
    }
    VHDLContext _vHDLContext_1 = new VHDLContext();
    final VHDLContext elseCtx = _vHDLContext_1;
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    for (final HDLStatement stmnt_1 : _elseDo) {
      VHDLContext _vHDL_1 = this.toVHDL(stmnt_1, pid);
      elseCtx.merge(_vHDL_1, false);
    }
    HashSet<HDLRegisterConfig> _hashSet = new HashSet<HDLRegisterConfig>();
    final Set<HDLRegisterConfig> configs = _hashSet;
    Set<HDLRegisterConfig> _keySet = thenCtx.clockedStatements.keySet();
    configs.addAll(_keySet);
    Set<HDLRegisterConfig> _keySet_1 = elseCtx.clockedStatements.keySet();
    configs.addAll(_keySet_1);
    VHDLContext _vHDLContext_2 = new VHDLContext();
    final VHDLContext res = _vHDLContext_2;
    res.merge(thenCtx, true);
    res.merge(elseCtx, true);
    HDLExpression _ifExp = obj.getIfExp();
    final Expression<?> ifExp = this.vee.toVHDL(_ifExp);
    for (final HDLRegisterConfig config : configs) {
      {
        IfStatement _ifStatement = new IfStatement(ifExp);
        final IfStatement ifs = _ifStatement;
        LinkedList<SequentialStatement> _get = thenCtx.clockedStatements.get(config);
        boolean _tripleNotEquals = (_get != null);
        if (_tripleNotEquals) {
          List<SequentialStatement> _statements = ifs.getStatements();
          LinkedList<SequentialStatement> _get_1 = thenCtx.clockedStatements.get(config);
          _statements.addAll(_get_1);
        }
        LinkedList<SequentialStatement> _get_2 = elseCtx.clockedStatements.get(config);
        boolean _tripleNotEquals_1 = (_get_2 != null);
        if (_tripleNotEquals_1) {
          List<SequentialStatement> _elseStatements = ifs.getElseStatements();
          LinkedList<SequentialStatement> _get_3 = elseCtx.clockedStatements.get(config);
          _elseStatements.addAll(_get_3);
        }
        res.addClockedStatement(config, ifs);
      }
    }
    boolean _or = false;
    int _size = thenCtx.unclockedStatements.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      _or = true;
    } else {
      int _size_1 = elseCtx.unclockedStatements.size();
      boolean _notEquals_1 = (_size_1 != 0);
      _or = (_notEquals || _notEquals_1);
    }
    if (_or) {
      IfStatement _ifStatement = new IfStatement(ifExp);
      final IfStatement ifs = _ifStatement;
      LinkedList<SequentialStatement> _get = thenCtx.unclockedStatements.get(Integer.valueOf(pid));
      boolean _tripleNotEquals = (_get != null);
      if (_tripleNotEquals) {
        List<SequentialStatement> _statements = ifs.getStatements();
        LinkedList<SequentialStatement> _get_1 = thenCtx.unclockedStatements.get(Integer.valueOf(pid));
        _statements.addAll(_get_1);
      }
      LinkedList<SequentialStatement> _get_2 = elseCtx.unclockedStatements.get(Integer.valueOf(pid));
      boolean _tripleNotEquals_1 = (_get_2 != null);
      if (_tripleNotEquals_1) {
        List<SequentialStatement> _elseStatements = ifs.getElseStatements();
        LinkedList<SequentialStatement> _get_3 = elseCtx.unclockedStatements.get(Integer.valueOf(pid));
        _elseStatements.addAll(_get_3);
      }
      res.addUnclockedStatement(pid, ifs, obj);
    }
    return this.attachComment(res, obj);
  }
  
  protected VHDLContext _toVHDL(final HDLFunction obj, final int pid) {
    IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Not supported");
    throw _illegalArgumentException;
  }
  
  public VHDLContext toVHDL(final IHDLObject obj, final int pid) {
    if (obj instanceof HDLDirectGeneration) {
      return _toVHDL((HDLDirectGeneration)obj, pid);
    } else if (obj instanceof HDLEnumDeclaration) {
      return _toVHDL((HDLEnumDeclaration)obj, pid);
    } else if (obj instanceof HDLForLoop) {
      return _toVHDL((HDLForLoop)obj, pid);
    } else if (obj instanceof HDLFunction) {
      return _toVHDL((HDLFunction)obj, pid);
    } else if (obj instanceof HDLIfStatement) {
      return _toVHDL((HDLIfStatement)obj, pid);
    } else if (obj instanceof HDLInterfaceDeclaration) {
      return _toVHDL((HDLInterfaceDeclaration)obj, pid);
    } else if (obj instanceof HDLInterfaceInstantiation) {
      return _toVHDL((HDLInterfaceInstantiation)obj, pid);
    } else if (obj instanceof HDLSwitchCaseStatement) {
      return _toVHDL((HDLSwitchCaseStatement)obj, pid);
    } else if (obj instanceof HDLSwitchStatement) {
      return _toVHDL((HDLSwitchStatement)obj, pid);
    } else if (obj instanceof HDLVariableDeclaration) {
      return _toVHDL((HDLVariableDeclaration)obj, pid);
    } else if (obj instanceof HDLAssignment) {
      return _toVHDL((HDLAssignment)obj, pid);
    } else if (obj instanceof HDLBlock) {
      return _toVHDL((HDLBlock)obj, pid);
    } else if (obj instanceof HDLFunctionCall) {
      return _toVHDL((HDLFunctionCall)obj, pid);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, pid).toString());
    }
  }
}
