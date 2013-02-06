package de.tuhh.ict.pshdl.model.extensions;

import com.google.common.base.Objects;
import de.tuhh.ict.pshdl.model.HDLAssignment;
import de.tuhh.ict.pshdl.model.HDLBlock;
import de.tuhh.ict.pshdl.model.HDLDirectGeneration;
import de.tuhh.ict.pshdl.model.HDLEnum;
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration;
import de.tuhh.ict.pshdl.model.HDLForLoop;
import de.tuhh.ict.pshdl.model.HDLFunction;
import de.tuhh.ict.pshdl.model.HDLIfStatement;
import de.tuhh.ict.pshdl.model.HDLInlineFunction;
import de.tuhh.ict.pshdl.model.HDLInterface;
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration;
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation;
import de.tuhh.ict.pshdl.model.HDLObject;
import de.tuhh.ict.pshdl.model.HDLObject.GenericMeta;
import de.tuhh.ict.pshdl.model.HDLPackage;
import de.tuhh.ict.pshdl.model.HDLStatement;
import de.tuhh.ict.pshdl.model.HDLSubstituteFunction;
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement;
import de.tuhh.ict.pshdl.model.HDLSwitchStatement;
import de.tuhh.ict.pshdl.model.HDLType;
import de.tuhh.ict.pshdl.model.HDLUnit;
import de.tuhh.ict.pshdl.model.HDLVariable;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration;
import de.tuhh.ict.pshdl.model.IHDLObject;
import de.tuhh.ict.pshdl.model.extensions.FullNameExtension;
import de.tuhh.ict.pshdl.model.utils.HDLLibrary;
import de.tuhh.ict.pshdl.model.utils.HDLProblemException;
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName;
import de.tuhh.ict.pshdl.model.utils.HDLQuery;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.FieldSelector;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Result;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Selector;
import de.tuhh.ict.pshdl.model.utils.HDLQuery.Source;
import de.tuhh.ict.pshdl.model.utils.HDLResolver;
import de.tuhh.ict.pshdl.model.utils.MetaAccess;
import de.tuhh.ict.pshdl.model.validation.Problem;
import de.tuhh.ict.pshdl.model.validation.builtin.ErrorCode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.xtext.xbase.lib.Functions.Function0;

@SuppressWarnings("all")
public class ScopingExtension {
  public static ScopingExtension INST = new Function0<ScopingExtension>() {
    public ScopingExtension apply() {
      ScopingExtension _scopingExtension = new ScopingExtension();
      return _scopingExtension;
    }
  }.apply();
  
  public HDLVariable resolveVariableDefault(final IHDLObject obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      return null;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveVariable(_container_1, hVar);
  }
  
  protected HDLVariable _resolveVariable(final IHDLObject obj, final HDLQualifiedName hVar) {
    return this.resolveVariableDefault(obj, hVar);
  }
  
  protected HDLFunction _resolveFunction(final IHDLObject obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      return null;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveFunction(_container_1, hVar);
  }
  
  protected HDLEnum _resolveEnum(final IHDLObject obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      return null;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveEnum(_container_1, hEnum);
  }
  
  protected HDLType _resolveType(final IHDLObject obj, final HDLQualifiedName type) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      return null;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveType(_container_1, type);
  }
  
  protected HDLInterface _resolveInterface(final IHDLObject obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      return null;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveInterface(_container_1, hIf);
  }
  
  private static MetaAccess<HDLResolver> RESOLVER = new Function0<MetaAccess<HDLResolver>>() {
    public MetaAccess<HDLResolver> apply() {
      GenericMeta<HDLResolver> _genericMeta = new GenericMeta<HDLResolver>("RESOLVER", false);
      return _genericMeta;
    }
  }.apply();
  
  public HDLResolver resolver(final IHDLObject statement, final boolean descent) {
    HDLResolver resolver = statement.<HDLResolver>getMeta(ScopingExtension.RESOLVER);
    boolean _equals = Objects.equal(resolver, null);
    if (_equals) {
      HDLResolver _hDLResolver = new HDLResolver(statement, descent);
      resolver = _hDLResolver;
      statement.<HDLResolver>addMeta(ScopingExtension.RESOLVER, resolver);
    }
    return resolver;
  }
  
  protected HDLType _resolveType(final HDLStatement obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveType(hVar);
  }
  
  protected HDLVariable _resolveVariable(final HDLStatement obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveVariable(hVar);
  }
  
  protected HDLInterface _resolveInterface(final HDLStatement obj, final HDLQualifiedName hIf) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveInterface(hIf);
  }
  
  protected HDLEnum _resolveEnum(final HDLStatement obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveEnum(hEnum);
  }
  
  protected HDLFunction _resolveFunction(final HDLStatement obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveFunction(hEnum);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLIfStatement obj) {
    LinkedList<HDLEnumDeclaration> _linkedList = new LinkedList<HDLEnumDeclaration>();
    final List<HDLEnumDeclaration> res = _linkedList;
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    List<HDLEnumDeclaration> _allEnumDeclarations = HDLResolver.getallEnumDeclarations(_thenDo);
    res.addAll(_allEnumDeclarations);
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    List<HDLEnumDeclaration> _allEnumDeclarations_1 = HDLResolver.getallEnumDeclarations(_elseDo);
    res.addAll(_allEnumDeclarations_1);
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLIfStatement obj) {
    LinkedList<HDLInterface> _linkedList = new LinkedList<HDLInterface>();
    final List<HDLInterface> res = _linkedList;
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    List<HDLInterface> _allInterfaceDeclarations = HDLResolver.getallInterfaceDeclarations(_thenDo);
    res.addAll(_allInterfaceDeclarations);
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    List<HDLInterface> _allInterfaceDeclarations_1 = HDLResolver.getallInterfaceDeclarations(_elseDo);
    res.addAll(_allInterfaceDeclarations_1);
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLIfStatement obj) {
    LinkedList<HDLVariable> _linkedList = new LinkedList<HDLVariable>();
    final List<HDLVariable> res = _linkedList;
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    List<HDLVariable> _allVariableDeclarations = HDLResolver.getallVariableDeclarations(_thenDo);
    res.addAll(_allVariableDeclarations);
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    List<HDLVariable> _allVariableDeclarations_1 = HDLResolver.getallVariableDeclarations(_elseDo);
    res.addAll(_allVariableDeclarations_1);
    return res;
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final IHDLObject gen) {
    return Collections.<HDLEnumDeclaration>emptyList();
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final IHDLObject gen) {
    return Collections.<HDLInterface>emptyList();
  }
  
  protected List<HDLVariable> _doGetVariables(final IHDLObject gen) {
    return Collections.<HDLVariable>emptyList();
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLDirectGeneration gen) {
    return Collections.<HDLEnumDeclaration>emptyList();
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLDirectGeneration gen) {
    HDLInterface _hIf = gen.getHIf();
    return Collections.<HDLInterface>singletonList(_hIf);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLDirectGeneration gen) {
    HDLVariable _var = gen.getVar();
    return Collections.<HDLVariable>singletonList(_var);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLInlineFunction obj) {
    LinkedList<HDLVariable> _linkedList = new LinkedList<HDLVariable>();
    final List<HDLVariable> res = _linkedList;
    ArrayList<HDLVariable> _args = obj.getArgs();
    res.addAll(_args);
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSubstituteFunction obj) {
    LinkedList<HDLVariable> _linkedList = new LinkedList<HDLVariable>();
    final List<HDLVariable> res = _linkedList;
    ArrayList<HDLVariable> _args = obj.getArgs();
    res.addAll(_args);
    return res;
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLForLoop obj) {
    ArrayList<HDLStatement> _dos = obj.getDos();
    return HDLResolver.getallEnumDeclarations(_dos);
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLForLoop obj) {
    ArrayList<HDLStatement> _dos = obj.getDos();
    return HDLResolver.getallInterfaceDeclarations(_dos);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLForLoop obj) {
    LinkedList<HDLVariable> _linkedList = new LinkedList<HDLVariable>();
    final List<HDLVariable> res = _linkedList;
    ArrayList<HDLStatement> _dos = obj.getDos();
    List<HDLVariable> _allVariableDeclarations = HDLResolver.getallVariableDeclarations(_dos);
    res.addAll(_allVariableDeclarations);
    HDLVariable _param = obj.getParam();
    res.add(_param);
    return res;
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLBlock obj) {
    ArrayList<HDLStatement> _statements = obj.getStatements();
    return HDLResolver.getallEnumDeclarations(_statements);
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLBlock obj) {
    ArrayList<HDLStatement> _statements = obj.getStatements();
    return HDLResolver.getallInterfaceDeclarations(_statements);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLBlock obj) {
    ArrayList<HDLStatement> _statements = obj.getStatements();
    return HDLResolver.getallVariableDeclarations(_statements);
  }
  
  protected HDLEnum _resolveEnum(final HDLAssignment obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      String _plus = ("for hEnum:" + hEnum);
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_ENUM, obj, _plus);
      HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
      throw _hDLProblemException;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveEnum(_container_1, hEnum);
  }
  
  protected HDLInterface _resolveInterface(final HDLAssignment obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      String _plus = ("for interface:" + hIf);
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_INTERFACE, obj, _plus);
      HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
      throw _hDLProblemException;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveInterface(_container_1, hIf);
  }
  
  protected HDLType _resolveType(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      String _plus = ("for type:" + hVar);
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_TYPE, obj, _plus);
      HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
      throw _hDLProblemException;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveType(_container_1, hVar);
  }
  
  protected HDLVariable _resolveVariable(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _equals = Objects.equal(_container, null);
    if (_equals) {
      String _plus = ("for hVariable:" + hVar);
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_VARIABLE, obj, _plus);
      HDLProblemException _hDLProblemException = new HDLProblemException(_problem);
      throw _hDLProblemException;
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveVariable(_container_1, hVar);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLEnumDeclaration obj) {
    return Collections.<HDLEnumDeclaration>singletonList(obj);
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLInterfaceDeclaration obj) {
    HDLInterface _hIf = obj.getHIf();
    return Collections.<HDLInterface>singletonList(_hIf);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLVariableDeclaration obj) {
    return obj.getVariables();
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLInterfaceInstantiation obj) {
    HDLVariable _var = obj.getVar();
    return Collections.<HDLVariable>singletonList(_var);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLSwitchCaseStatement obj) {
    ArrayList<HDLStatement> _dos = obj.getDos();
    return HDLResolver.getallEnumDeclarations(_dos);
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLSwitchCaseStatement obj) {
    ArrayList<HDLStatement> _dos = obj.getDos();
    return HDLResolver.getallInterfaceDeclarations(_dos);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSwitchCaseStatement obj) {
    ArrayList<HDLStatement> _dos = obj.getDos();
    return HDLResolver.getallVariableDeclarations(_dos);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLSwitchStatement obj) {
    LinkedList<HDLEnumDeclaration> _linkedList = new LinkedList<HDLEnumDeclaration>();
    final List<HDLEnumDeclaration> res = _linkedList;
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLEnumDeclaration> _doGetEnumDeclarations = this.doGetEnumDeclarations(c);
      res.addAll(_doGetEnumDeclarations);
    }
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLSwitchStatement obj) {
    LinkedList<HDLInterface> _linkedList = new LinkedList<HDLInterface>();
    final List<HDLInterface> res = _linkedList;
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLInterface> _doGetInterfaceDeclarations = this.doGetInterfaceDeclarations(c);
      res.addAll(_doGetInterfaceDeclarations);
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSwitchStatement obj) {
    LinkedList<HDLVariable> _linkedList = new LinkedList<HDLVariable>();
    final List<HDLVariable> res = _linkedList;
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLVariable> _doGetVariables = this.doGetVariables(c);
      res.addAll(_doGetVariables);
    }
    return res;
  }
  
  protected HDLFunction _resolveFunction(final HDLPackage obj, final HDLQualifiedName hFunc) {
    HDLLibrary library = obj.getLibrary();
    boolean _equals = Objects.equal(library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library = HDLLibrary.getLibrary(_libURI);
      library = _library;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    ArrayList<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolveFunction(_asList, hFunc);
  }
  
  protected HDLEnum _resolveEnum(final HDLPackage obj, final HDLQualifiedName hEnum) {
    HDLType _resolveType = this.resolveType(obj, hEnum);
    return ((HDLEnum) _resolveType);
  }
  
  protected HDLInterface _resolveInterface(final HDLPackage obj, final HDLQualifiedName hIf) {
    HDLType _resolveType = this.resolveType(obj, hIf);
    return ((HDLInterface) _resolveType);
  }
  
  protected HDLType _resolveType(final HDLPackage obj, final HDLQualifiedName type) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _equals = Objects.equal(_library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    ArrayList<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolve(_asList, type);
  }
  
  protected HDLVariable _resolveVariable(final HDLPackage obj, final HDLQualifiedName hVar) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _equals = Objects.equal(_library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    ArrayList<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolveVariable(_asList, hVar);
  }
  
  protected HDLEnum _resolveEnum(final HDLUnit obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, false);
    final HDLEnum resolveEnum = _resolver.resolveEnum(hEnum);
    boolean _notEquals = (!Objects.equal(resolveEnum, null));
    if (_notEquals) {
      return resolveEnum;
    }
    HDLType _resolveType = this.resolveType(obj, hEnum);
    return ((HDLEnum) _resolveType);
  }
  
  protected HDLFunction _resolveFunction(final HDLUnit obj, final HDLQualifiedName hFunc) {
    HDLResolver _resolver = this.resolver(obj, false);
    final HDLFunction resolveEnum = _resolver.resolveFunction(hFunc);
    boolean _notEquals = (!Objects.equal(resolveEnum, null));
    if (_notEquals) {
      return resolveEnum;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _equals = Objects.equal(_library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    ArrayList<String> _imports = obj.getImports();
    return library.resolveFunction(_imports, hFunc);
  }
  
  protected HDLInterface _resolveInterface(final HDLUnit obj, final HDLQualifiedName hIf) {
    HDLResolver _resolver = this.resolver(obj, false);
    final HDLInterface resolveInterface = _resolver.resolveInterface(hIf);
    boolean _notEquals = (!Objects.equal(resolveInterface, null));
    if (_notEquals) {
      return resolveInterface;
    }
    HDLType _resolveType = this.resolveType(obj, hIf);
    return ((HDLInterface) _resolveType);
  }
  
  protected HDLType _resolveType(final HDLUnit obj, final HDLQualifiedName type) {
    HDLResolver _resolver = this.resolver(obj, false);
    final HDLType resolveType = _resolver.resolveType(type);
    boolean _notEquals = (!Objects.equal(resolveType, null));
    if (_notEquals) {
      return resolveType;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _equals = Objects.equal(_library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    ArrayList<String> _imports = obj.getImports();
    return library.resolve(_imports, type);
  }
  
  protected HDLVariable _resolveVariable(final HDLUnit obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, false);
    final HDLVariable hdlVariable = _resolver.resolveVariable(hVar);
    boolean _notEquals = (!Objects.equal(hdlVariable, null));
    if (_notEquals) {
      return hdlVariable;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _equals = Objects.equal(_library, null);
    if (_equals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    ArrayList<String> _imports = obj.getImports();
    return library.resolveVariable(_imports, hVar);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLUnit obj) {
    ArrayList<HDLStatement> _inits = obj.getInits();
    final List<HDLEnumDeclaration> res = HDLResolver.getallEnumDeclarations(_inits);
    ArrayList<HDLStatement> _statements = obj.getStatements();
    List<HDLEnumDeclaration> _allEnumDeclarations = HDLResolver.getallEnumDeclarations(_statements);
    res.addAll(_allEnumDeclarations);
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLUnit obj) {
    ArrayList<HDLStatement> _inits = obj.getInits();
    final List<HDLInterface> res = HDLResolver.getallInterfaceDeclarations(_inits);
    ArrayList<HDLStatement> _statements = obj.getStatements();
    List<HDLInterface> _allInterfaceDeclarations = HDLResolver.getallInterfaceDeclarations(_statements);
    res.addAll(_allInterfaceDeclarations);
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLUnit obj) {
    ArrayList<HDLStatement> _inits = obj.getInits();
    final List<HDLVariable> res = HDLResolver.getallVariableDeclarations(_inits);
    ArrayList<HDLStatement> _statements = obj.getStatements();
    List<HDLVariable> _allVariableDeclarations = HDLResolver.getallVariableDeclarations(_statements);
    res.addAll(_allVariableDeclarations);
    return res;
  }
  
  protected HDLVariable _resolveVariable(final HDLInterface hIf, final HDLQualifiedName hVar) {
    String _lastSegment = hVar.getLastSegment();
    final HDLVariable resolved = ScopingExtension.getVariable(hIf, _lastSegment);
    boolean _notEquals = (!Objects.equal(resolved, null));
    if (_notEquals) {
      boolean _equals = (hVar.length == 1);
      if (_equals) {
        return resolved;
      }
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hIf);
      HDLQualifiedName _skipLast = hVar.skipLast(1);
      boolean _equals_1 = _fullNameOf.equals(_skipLast);
      if (_equals_1) {
        return resolved;
      }
    }
    return this.resolveVariableDefault(hIf, hVar);
  }
  
  private static HDLVariable getVariable(final HDLInterface hIf, final String lastSegment) {
    Source<HDLVariable> _select = HDLQuery.<HDLVariable>select(HDLVariable.class);
    Selector<HDLVariable> _from = _select.from(hIf);
    FieldSelector<HDLVariable,String> _where = _from.<String>where(HDLVariable.fName);
    Result<HDLVariable,String> _lastSegmentIs = _where.lastSegmentIs(lastSegment);
    return _lastSegmentIs.getFirst();
  }
  
  protected HDLVariable _resolveVariable(final HDLEnum hEnum, final HDLQualifiedName hVar) {
    boolean _equals = (hVar.length == 1);
    if (_equals) {
      String _lastSegment = hVar.getLastSegment();
      return ScopingExtension.getVariable(hEnum, _lastSegment);
    }
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hEnum);
    HDLQualifiedName _skipLast = hVar.skipLast(1);
    boolean _equals_1 = _fullNameOf.equals(_skipLast);
    if (_equals_1) {
      String _lastSegment_1 = hVar.getLastSegment();
      return ScopingExtension.getVariable(hEnum, _lastSegment_1);
    }
    return this.resolveVariable(hEnum, hVar);
  }
  
  public static HDLVariable getVariable(final HDLEnum hEnum, final String lastSegment) {
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    for (final HDLVariable hVar : _enums) {
      String _name = hVar.getName();
      boolean _equals = _name.equals(lastSegment);
      if (_equals) {
        return hVar;
      }
    }
    return null;
  }
  
  public HDLVariable resolveVariable(final IHDLObject hEnum, final HDLQualifiedName hVar) {
    if (hEnum instanceof HDLEnum) {
      return _resolveVariable((HDLEnum)hEnum, hVar);
    } else if (hEnum instanceof HDLInterface) {
      return _resolveVariable((HDLInterface)hEnum, hVar);
    } else if (hEnum instanceof HDLAssignment) {
      return _resolveVariable((HDLAssignment)hEnum, hVar);
    } else if (hEnum instanceof HDLPackage) {
      return _resolveVariable((HDLPackage)hEnum, hVar);
    } else if (hEnum instanceof HDLUnit) {
      return _resolveVariable((HDLUnit)hEnum, hVar);
    } else if (hEnum instanceof HDLStatement) {
      return _resolveVariable((HDLStatement)hEnum, hVar);
    } else if (hEnum != null) {
      return _resolveVariable(hEnum, hVar);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(hEnum, hVar).toString());
    }
  }
  
  public HDLFunction resolveFunction(final IHDLObject obj, final HDLQualifiedName hFunc) {
    if (obj instanceof HDLPackage) {
      return _resolveFunction((HDLPackage)obj, hFunc);
    } else if (obj instanceof HDLUnit) {
      return _resolveFunction((HDLUnit)obj, hFunc);
    } else if (obj instanceof HDLStatement) {
      return _resolveFunction((HDLStatement)obj, hFunc);
    } else if (obj != null) {
      return _resolveFunction(obj, hFunc);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, hFunc).toString());
    }
  }
  
  public HDLEnum resolveEnum(final IHDLObject obj, final HDLQualifiedName hEnum) {
    if (obj instanceof HDLAssignment) {
      return _resolveEnum((HDLAssignment)obj, hEnum);
    } else if (obj instanceof HDLPackage) {
      return _resolveEnum((HDLPackage)obj, hEnum);
    } else if (obj instanceof HDLUnit) {
      return _resolveEnum((HDLUnit)obj, hEnum);
    } else if (obj instanceof HDLStatement) {
      return _resolveEnum((HDLStatement)obj, hEnum);
    } else if (obj != null) {
      return _resolveEnum(obj, hEnum);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, hEnum).toString());
    }
  }
  
  public HDLType resolveType(final IHDLObject obj, final HDLQualifiedName hVar) {
    if (obj instanceof HDLAssignment) {
      return _resolveType((HDLAssignment)obj, hVar);
    } else if (obj instanceof HDLPackage) {
      return _resolveType((HDLPackage)obj, hVar);
    } else if (obj instanceof HDLUnit) {
      return _resolveType((HDLUnit)obj, hVar);
    } else if (obj instanceof HDLStatement) {
      return _resolveType((HDLStatement)obj, hVar);
    } else if (obj != null) {
      return _resolveType(obj, hVar);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, hVar).toString());
    }
  }
  
  public HDLInterface resolveInterface(final IHDLObject obj, final HDLQualifiedName hIf) {
    if (obj instanceof HDLAssignment) {
      return _resolveInterface((HDLAssignment)obj, hIf);
    } else if (obj instanceof HDLPackage) {
      return _resolveInterface((HDLPackage)obj, hIf);
    } else if (obj instanceof HDLUnit) {
      return _resolveInterface((HDLUnit)obj, hIf);
    } else if (obj instanceof HDLStatement) {
      return _resolveInterface((HDLStatement)obj, hIf);
    } else if (obj != null) {
      return _resolveInterface(obj, hIf);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, hIf).toString());
    }
  }
  
  public List<HDLEnumDeclaration> doGetEnumDeclarations(final IHDLObject gen) {
    if (gen instanceof HDLDirectGeneration) {
      return _doGetEnumDeclarations((HDLDirectGeneration)gen);
    } else if (gen instanceof HDLEnumDeclaration) {
      return _doGetEnumDeclarations((HDLEnumDeclaration)gen);
    } else if (gen instanceof HDLForLoop) {
      return _doGetEnumDeclarations((HDLForLoop)gen);
    } else if (gen instanceof HDLIfStatement) {
      return _doGetEnumDeclarations((HDLIfStatement)gen);
    } else if (gen instanceof HDLSwitchCaseStatement) {
      return _doGetEnumDeclarations((HDLSwitchCaseStatement)gen);
    } else if (gen instanceof HDLSwitchStatement) {
      return _doGetEnumDeclarations((HDLSwitchStatement)gen);
    } else if (gen instanceof HDLBlock) {
      return _doGetEnumDeclarations((HDLBlock)gen);
    } else if (gen instanceof HDLUnit) {
      return _doGetEnumDeclarations((HDLUnit)gen);
    } else if (gen != null) {
      return _doGetEnumDeclarations(gen);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(gen).toString());
    }
  }
  
  public List<HDLInterface> doGetInterfaceDeclarations(final IHDLObject gen) {
    if (gen instanceof HDLDirectGeneration) {
      return _doGetInterfaceDeclarations((HDLDirectGeneration)gen);
    } else if (gen instanceof HDLForLoop) {
      return _doGetInterfaceDeclarations((HDLForLoop)gen);
    } else if (gen instanceof HDLIfStatement) {
      return _doGetInterfaceDeclarations((HDLIfStatement)gen);
    } else if (gen instanceof HDLInterfaceDeclaration) {
      return _doGetInterfaceDeclarations((HDLInterfaceDeclaration)gen);
    } else if (gen instanceof HDLSwitchCaseStatement) {
      return _doGetInterfaceDeclarations((HDLSwitchCaseStatement)gen);
    } else if (gen instanceof HDLSwitchStatement) {
      return _doGetInterfaceDeclarations((HDLSwitchStatement)gen);
    } else if (gen instanceof HDLBlock) {
      return _doGetInterfaceDeclarations((HDLBlock)gen);
    } else if (gen instanceof HDLUnit) {
      return _doGetInterfaceDeclarations((HDLUnit)gen);
    } else if (gen != null) {
      return _doGetInterfaceDeclarations(gen);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(gen).toString());
    }
  }
  
  public List<HDLVariable> doGetVariables(final IHDLObject obj) {
    if (obj instanceof HDLInlineFunction) {
      return _doGetVariables((HDLInlineFunction)obj);
    } else if (obj instanceof HDLSubstituteFunction) {
      return _doGetVariables((HDLSubstituteFunction)obj);
    } else if (obj instanceof HDLDirectGeneration) {
      return _doGetVariables((HDLDirectGeneration)obj);
    } else if (obj instanceof HDLForLoop) {
      return _doGetVariables((HDLForLoop)obj);
    } else if (obj instanceof HDLIfStatement) {
      return _doGetVariables((HDLIfStatement)obj);
    } else if (obj instanceof HDLInterfaceInstantiation) {
      return _doGetVariables((HDLInterfaceInstantiation)obj);
    } else if (obj instanceof HDLSwitchCaseStatement) {
      return _doGetVariables((HDLSwitchCaseStatement)obj);
    } else if (obj instanceof HDLSwitchStatement) {
      return _doGetVariables((HDLSwitchStatement)obj);
    } else if (obj instanceof HDLVariableDeclaration) {
      return _doGetVariables((HDLVariableDeclaration)obj);
    } else if (obj instanceof HDLBlock) {
      return _doGetVariables((HDLBlock)obj);
    } else if (obj instanceof HDLUnit) {
      return _doGetVariables((HDLUnit)obj);
    } else if (obj != null) {
      return _doGetVariables(obj);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj).toString());
    }
  }
}
