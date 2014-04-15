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

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInlineFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLNativeFunction;
import org.pshdl.model.HDLObject;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSubstituteFunction;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.utils.HDLLibrary;
import org.pshdl.model.utils.HDLProblemException;
import org.pshdl.model.utils.HDLQualifiedName;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.HDLResolver;
import org.pshdl.model.utils.MetaAccess;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

/**
 * The ScopingExtension allows the resolution of Enums, Interface, Variables by name
 */
@SuppressWarnings("all")
public class ScopingExtension {
  public static ScopingExtension INST = new ScopingExtension();
  
  public Optional<HDLVariable> resolveVariableDefault(final IHDLObject obj, final HDLQualifiedName hVar) {
    boolean _or = false;
    boolean _tripleEquals = (obj == null);
    if (_tripleEquals) {
      _or = true;
    } else {
      IHDLObject _container = obj.getContainer();
      boolean _tripleEquals_1 = (_container == null);
      _or = _tripleEquals_1;
    }
    if (_or) {
      return Optional.<HDLVariable>absent();
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveVariable(_container_1, hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final IHDLObject obj, final HDLQualifiedName hVar) {
    return this.resolveVariableDefault(obj, hVar);
  }
  
  protected Optional<HDLFunction> _resolveFunction(final IHDLObject obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLFunction>absent();
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveFunction(_container_1, hVar);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final IHDLObject obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLEnum>absent();
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveEnum(_container_1, hEnum);
  }
  
  protected Optional<? extends HDLType> _resolveType(final IHDLObject obj, final HDLQualifiedName type) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLType>absent();
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveType(_container_1, type);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final IHDLObject obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLInterface>absent();
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveInterface(_container_1, hIf);
  }
  
  private static MetaAccess<HDLResolver> RESOLVER = new HDLObject.GenericMeta<HDLResolver>("RESOLVER", false);
  
  private HDLResolver resolver(final IHDLObject statement, final boolean descent) {
    HDLResolver resolver = statement.<HDLResolver>getMeta(ScopingExtension.RESOLVER);
    boolean _tripleEquals = (resolver == null);
    if (_tripleEquals) {
      HDLResolver _hDLResolver = new HDLResolver(statement, descent);
      resolver = _hDLResolver;
      statement.<HDLResolver>addMeta(ScopingExtension.RESOLVER, resolver);
    }
    return resolver;
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLStatement obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveType(hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLStatement obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveVariable(hVar);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLStatement obj, final HDLQualifiedName hIf) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveInterface(hIf);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLStatement obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveEnum(hEnum);
  }
  
  protected Optional<HDLFunction> _resolveFunction(final HDLStatement obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, true);
    return _resolver.resolveFunction(hEnum);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLIfStatement obj) {
    final List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    List<HDLEnumDeclaration> _allEnumDeclarations = HDLResolver.getallEnumDeclarations(_thenDo);
    res.addAll(_allEnumDeclarations);
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    List<HDLEnumDeclaration> _allEnumDeclarations_1 = HDLResolver.getallEnumDeclarations(_elseDo);
    res.addAll(_allEnumDeclarations_1);
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLIfStatement obj) {
    final List<HDLInterface> res = new LinkedList<HDLInterface>();
    ArrayList<HDLStatement> _thenDo = obj.getThenDo();
    List<HDLInterface> _allInterfaceDeclarations = HDLResolver.getallInterfaceDeclarations(_thenDo);
    res.addAll(_allInterfaceDeclarations);
    ArrayList<HDLStatement> _elseDo = obj.getElseDo();
    List<HDLInterface> _allInterfaceDeclarations_1 = HDLResolver.getallInterfaceDeclarations(_elseDo);
    res.addAll(_allInterfaceDeclarations_1);
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLIfStatement obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
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
    boolean _tripleEquals = (_hIf == null);
    if (_tripleEquals) {
      return Collections.<HDLInterface>emptyList();
    }
    HDLInterface _hIf_1 = gen.getHIf();
    return Collections.<HDLInterface>singletonList(_hIf_1);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLDirectGeneration gen) {
    HDLVariable _var = gen.getVar();
    return Collections.<HDLVariable>singletonList(_var);
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLInlineFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      HDLVariable _name = v.getName();
      res.add(_name);
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLNativeFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      HDLVariable _name = v.getName();
      res.add(_name);
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSubstituteFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      HDLVariable _name = v.getName();
      res.add(_name);
    }
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
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
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
  
  protected Optional<HDLEnum> _resolveEnum(final HDLAssignment obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_ENUM, obj, ("for hEnum:" + hEnum));
      throw new HDLProblemException(_problem);
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveEnum(_container_1, hEnum);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLAssignment obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_INTERFACE, obj, ("for interface:" + hIf));
      throw new HDLProblemException(_problem);
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveInterface(_container_1, hIf);
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_TYPE, obj, ("for type:" + hVar));
      throw new HDLProblemException(_problem);
    }
    IHDLObject _container_1 = obj.getContainer();
    return this.resolveType(_container_1, hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_VARIABLE, obj, ("for hVariable:" + hVar));
      throw new HDLProblemException(_problem);
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
    final List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLEnumDeclaration> _doGetEnumDeclarations = this.doGetEnumDeclarations(c);
      res.addAll(_doGetEnumDeclarations);
    }
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLSwitchStatement obj) {
    final List<HDLInterface> res = new LinkedList<HDLInterface>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLInterface> _doGetInterfaceDeclarations = this.doGetInterfaceDeclarations(c);
      res.addAll(_doGetInterfaceDeclarations);
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSwitchStatement obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      List<HDLVariable> _doGetVariables = this.doGetVariables(c);
      res.addAll(_doGetVariables);
    }
    return res;
  }
  
  protected Optional<HDLFunction> _resolveFunction(final HDLPackage obj, final HDLQualifiedName hFunc) {
    HDLLibrary library = obj.getLibrary();
    boolean _tripleEquals = (library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library = HDLLibrary.getLibrary(_libURI);
      library = _library;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    Iterable<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolveFunction(_asList, hFunc);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLPackage obj, final HDLQualifiedName hEnum) {
    final Optional<? extends HDLType> res = this.resolveType(obj, hEnum);
    boolean _and = false;
    boolean _isPresent = res.isPresent();
    if (!_isPresent) {
      _and = false;
    } else {
      HDLType _get = res.get();
      _and = (_get instanceof HDLEnum);
    }
    if (_and) {
      return ((Optional<HDLEnum>) res);
    }
    return Optional.<HDLEnum>absent();
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLPackage obj, final HDLQualifiedName hIf) {
    final Optional<? extends HDLType> res = this.resolveType(obj, hIf);
    boolean _and = false;
    boolean _isPresent = res.isPresent();
    if (!_isPresent) {
      _and = false;
    } else {
      HDLType _get = res.get();
      _and = (_get instanceof HDLInterface);
    }
    if (_and) {
      return ((Optional<HDLInterface>) res);
    }
    return Optional.<HDLInterface>absent();
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLPackage obj, final HDLQualifiedName type) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    Iterable<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolve(_asList, type);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLPackage obj, final HDLQualifiedName hVar) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    Iterable<String> _asList = HDLObject.<String>asList(_plus);
    return library.resolveVariable(_asList, hVar);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLUnit obj, final HDLQualifiedName hEnum) {
    HDLResolver _resolver = this.resolver(obj, false);
    final Optional<HDLEnum> resolveEnum = _resolver.resolveEnum(hEnum);
    boolean _isPresent = resolveEnum.isPresent();
    if (_isPresent) {
      return resolveEnum;
    }
    final Optional<? extends HDLType> res = this.resolveType(obj, hEnum);
    boolean _and = false;
    boolean _isPresent_1 = res.isPresent();
    if (!_isPresent_1) {
      _and = false;
    } else {
      HDLType _get = res.get();
      _and = (_get instanceof HDLEnum);
    }
    if (_and) {
      return ((Optional<HDLEnum>) res);
    }
    return Optional.<HDLEnum>absent();
  }
  
  protected Optional<HDLFunction> _resolveFunction(final HDLUnit obj, final HDLQualifiedName hFunc) {
    HDLResolver _resolver = this.resolver(obj, false);
    final Optional<HDLFunction> resolveEnum = _resolver.resolveFunction(hFunc);
    boolean _isPresent = resolveEnum.isPresent();
    if (_isPresent) {
      return resolveEnum;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    final ArrayList<String> newImports = obj.getImports();
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    HDLQualifiedName _skipLast = _fullNameOf.skipLast(1);
    HDLQualifiedName _append = _skipLast.append("*");
    String _string = _append.toString();
    newImports.add(_string);
    return library.resolveFunction(newImports, hFunc);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLUnit obj, final HDLQualifiedName hIf) {
    HDLResolver _resolver = this.resolver(obj, false);
    final Optional<HDLInterface> resolveInterface = _resolver.resolveInterface(hIf);
    boolean _isPresent = resolveInterface.isPresent();
    if (_isPresent) {
      return resolveInterface;
    }
    final Optional<? extends HDLType> res = this.resolveType(obj, hIf);
    boolean _and = false;
    boolean _isPresent_1 = res.isPresent();
    if (!_isPresent_1) {
      _and = false;
    } else {
      HDLType _get = res.get();
      _and = (_get instanceof HDLInterface);
    }
    if (_and) {
      return ((Optional<HDLInterface>) res);
    }
    return Optional.<HDLInterface>absent();
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLUnit obj, final HDLQualifiedName type) {
    HDLResolver _resolver = this.resolver(obj, false);
    final Optional<? extends HDLType> resolveType = _resolver.resolveType(type);
    boolean _isPresent = resolveType.isPresent();
    if (_isPresent) {
      return resolveType;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    final ArrayList<String> newImports = obj.getImports();
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    HDLQualifiedName _skipLast = _fullNameOf.skipLast(1);
    HDLQualifiedName _append = _skipLast.append("*");
    String _string = _append.toString();
    newImports.add(_string);
    return library.resolve(newImports, type);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLUnit obj, final HDLQualifiedName hVar) {
    HDLResolver _resolver = this.resolver(obj, false);
    final Optional<HDLVariable> hdlVariable = _resolver.resolveVariable(hVar);
    boolean _isPresent = hdlVariable.isPresent();
    if (_isPresent) {
      return hdlVariable;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      String _libURI = obj.getLibURI();
      HDLLibrary _library_1 = HDLLibrary.getLibrary(_libURI);
      library = _library_1;
    }
    final ArrayList<String> newImports = obj.getImports();
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
    HDLQualifiedName _skipLast = _fullNameOf.skipLast(1);
    HDLQualifiedName _append = _skipLast.append("*");
    String _string = _append.toString();
    newImports.add(_string);
    return library.resolveVariable(newImports, hVar);
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
  
  protected Optional<HDLVariable> _resolveVariable(final HDLInterface hIf, final HDLQualifiedName hVar) {
    String _lastSegment = hVar.getLastSegment();
    final HDLVariable resolved = ScopingExtension.getVariable(hIf, _lastSegment);
    boolean _tripleNotEquals = (resolved != null);
    if (_tripleNotEquals) {
      if ((hVar.length == 1)) {
        return Optional.<HDLVariable>of(resolved);
      }
      HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hIf);
      HDLQualifiedName _skipLast = hVar.skipLast(1);
      boolean _equals = _fullNameOf.equals(_skipLast);
      if (_equals) {
        return Optional.<HDLVariable>of(resolved);
      }
    }
    return this.resolveVariableDefault(hIf, hVar);
  }
  
  private static HDLVariable getVariable(final HDLInterface hIf, final String lastSegment) {
    HDLQuery.Source<HDLVariable> _select = HDLQuery.<HDLVariable>select(HDLVariable.class);
    HDLQuery.Selector<HDLVariable> _from = _select.from(hIf);
    HDLQuery.FieldSelector<HDLVariable,String> _where = _from.<String>where(HDLVariable.fName);
    HDLQuery.Result<HDLVariable,String> _lastSegmentIs = _where.lastSegmentIs(lastSegment);
    return _lastSegmentIs.getFirst();
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLEnum hEnum, final HDLQualifiedName hVar) {
    if ((hVar.length == 1)) {
      String _lastSegment = hVar.getLastSegment();
      return ScopingExtension.getVariable(hEnum, _lastSegment);
    }
    HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hEnum);
    HDLQualifiedName _skipLast = hVar.skipLast(1);
    boolean _equals = _fullNameOf.equals(_skipLast);
    if (_equals) {
      String _lastSegment_1 = hVar.getLastSegment();
      return ScopingExtension.getVariable(hEnum, _lastSegment_1);
    }
    return this.resolveVariable(hEnum, hVar);
  }
  
  public static Optional<HDLVariable> getVariable(final HDLEnum hEnum, final String lastSegment) {
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    for (final HDLVariable hVar : _enums) {
      String _name = hVar.getName();
      boolean _equals = _name.equals(lastSegment);
      if (_equals) {
        return Optional.<HDLVariable>of(hVar);
      }
    }
    return Optional.<HDLVariable>absent();
  }
  
  public Optional<HDLVariable> resolveVariable(final IHDLObject hEnum, final HDLQualifiedName hVar) {
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
  
  public Optional<HDLFunction> resolveFunction(final IHDLObject obj, final HDLQualifiedName hFunc) {
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
  
  public Optional<HDLEnum> resolveEnum(final IHDLObject obj, final HDLQualifiedName hEnum) {
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
  
  public Optional<? extends HDLType> resolveType(final IHDLObject obj, final HDLQualifiedName hVar) {
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
  
  public Optional<HDLInterface> resolveInterface(final IHDLObject obj, final HDLQualifiedName hIf) {
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
    } else if (obj instanceof HDLNativeFunction) {
      return _doGetVariables((HDLNativeFunction)obj);
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
