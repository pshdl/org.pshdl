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
import java.util.SortedSet;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLDirectGeneration;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLForLoop;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
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
import org.pshdl.model.impl.AbstractHDLFunctionCall;
import org.pshdl.model.types.builtIn.HDLFunctions;
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
    if (((obj == null) || (obj.getContainer() == null))) {
      return Optional.<HDLVariable>absent();
    }
    return this.resolveVariable(obj.getContainer(), hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final IHDLObject obj, final HDLQualifiedName hVar) {
    return this.resolveVariableDefault(obj, hVar);
  }
  
  public Optional<HDLFunction> resolveFunctionByName(final AbstractHDLFunctionCall obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLFunction>absent();
    }
    final HDLFunctionCall call = ((HDLFunctionCall) obj);
    final Optional<? extends Iterable<HDLFunction>> candidates = this.resolveFunctionCall(obj.getContainer(), call, hVar);
    if (((!candidates.isPresent()) || IterableExtensions.isEmpty(candidates.get()))) {
      return Optional.<HDLFunction>absent();
    }
    return Optional.<HDLFunction>of(IterableExtensions.<HDLFunction>head(candidates.get()));
  }
  
  public Optional<HDLFunction> resolveFunction(final AbstractHDLFunctionCall obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLFunction>absent();
    }
    final HDLFunctionCall call = ((HDLFunctionCall) obj);
    final Optional<? extends Iterable<HDLFunction>> candidates = this.resolveFunctionCall(obj.getContainer(), call, hVar);
    if (((!candidates.isPresent()) || IterableExtensions.isEmpty(candidates.get()))) {
      return Optional.<HDLFunction>absent();
    }
    final SortedSet<HDLFunctions.FunctionScore> scored = HDLFunctions.scoreList(candidates.get(), call);
    if ((IterableExtensions.isNullOrEmpty(scored) || (scored.first().score > 1000))) {
      return Optional.<HDLFunction>absent();
    }
    return Optional.<HDLFunction>of(scored.first().function);
  }
  
  protected Optional<? extends Iterable<HDLFunction>> _resolveFunctionCall(final IHDLObject obj, final HDLFunctionCall call, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<Iterable<HDLFunction>>absent();
    }
    return this.resolveFunctionCall(obj.getContainer(), call, hVar);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final IHDLObject obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLEnum>absent();
    }
    return this.resolveEnum(obj.getContainer(), hEnum);
  }
  
  protected Optional<? extends HDLType> _resolveType(final IHDLObject obj, final HDLQualifiedName type) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLType>absent();
    }
    return this.resolveType(obj.getContainer(), type);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final IHDLObject obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      return Optional.<HDLInterface>absent();
    }
    return this.resolveInterface(obj.getContainer(), hIf);
  }
  
  private static MetaAccess<HDLResolver> RESOLVER = new HDLObject.GenericMeta<HDLResolver>("RESOLVER", false);
  
  private HDLResolver resolver(final IHDLObject statement, final boolean descent) {
    HDLResolver resolver = statement.<HDLResolver>getMeta(ScopingExtension.RESOLVER);
    if ((resolver == null)) {
      HDLResolver _hDLResolver = new HDLResolver(statement, descent);
      resolver = _hDLResolver;
      statement.<HDLResolver>addMeta(ScopingExtension.RESOLVER, resolver);
    }
    return resolver;
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLStatement obj, final HDLQualifiedName hVar) {
    return this.resolver(obj, true).resolveType(hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLStatement obj, final HDLQualifiedName hVar) {
    return this.resolver(obj, true).resolveVariable(hVar);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLStatement obj, final HDLQualifiedName hIf) {
    return this.resolver(obj, true).resolveInterface(hIf);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLStatement obj, final HDLQualifiedName hEnum) {
    return this.resolver(obj, true).resolveEnum(hEnum);
  }
  
  protected Optional<? extends Iterable<HDLFunction>> _resolveFunctionCall(final HDLStatement obj, final HDLFunctionCall call, final HDLQualifiedName hEnum) {
    return this.resolver(obj, true).resolveFunctionCall(call, hEnum);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLIfStatement obj) {
    final List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
    res.addAll(HDLResolver.getallEnumDeclarations(obj.getThenDo()));
    res.addAll(HDLResolver.getallEnumDeclarations(obj.getElseDo()));
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLIfStatement obj) {
    final List<HDLInterface> res = new LinkedList<HDLInterface>();
    res.addAll(HDLResolver.getallInterfaceDeclarations(obj.getThenDo()));
    res.addAll(HDLResolver.getallInterfaceDeclarations(obj.getElseDo()));
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLIfStatement obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    res.addAll(HDLResolver.getallVariableDeclarations(obj.getThenDo()));
    res.addAll(HDLResolver.getallVariableDeclarations(obj.getElseDo()));
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
    return Collections.<HDLInterface>singletonList(gen.getHIf());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLDirectGeneration gen) {
    return Collections.<HDLVariable>singletonList(gen.getVar());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLInlineFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      res.add(v.getName());
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLNativeFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      res.add(v.getName());
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSubstituteFunction obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLFunctionParameter> _args = obj.getArgs();
    for (final HDLFunctionParameter v : _args) {
      res.add(v.getName());
    }
    res.addAll(HDLResolver.getallVariableDeclarations(obj.getStmnts()));
    return res;
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLForLoop obj) {
    return HDLResolver.getallEnumDeclarations(obj.getDos());
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLForLoop obj) {
    return HDLResolver.getallInterfaceDeclarations(obj.getDos());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLForLoop obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    res.addAll(HDLResolver.getallVariableDeclarations(obj.getDos()));
    res.add(obj.getParam());
    return res;
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLBlock obj) {
    return HDLResolver.getallEnumDeclarations(obj.getStatements());
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLBlock obj) {
    return HDLResolver.getallInterfaceDeclarations(obj.getStatements());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLBlock obj) {
    return HDLResolver.getallVariableDeclarations(obj.getStatements());
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLAssignment obj, final HDLQualifiedName hEnum) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_ENUM, obj, ("for hEnum:" + hEnum));
      throw new HDLProblemException(_problem);
    }
    return this.resolveEnum(obj.getContainer(), hEnum);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLAssignment obj, final HDLQualifiedName hIf) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_INTERFACE, obj, ("for interface:" + hIf));
      throw new HDLProblemException(_problem);
    }
    return this.resolveInterface(obj.getContainer(), hIf);
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_TYPE, obj, ("for type:" + hVar));
      throw new HDLProblemException(_problem);
    }
    return this.resolveType(obj.getContainer(), hVar);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLAssignment obj, final HDLQualifiedName hVar) {
    IHDLObject _container = obj.getContainer();
    boolean _tripleEquals = (_container == null);
    if (_tripleEquals) {
      Problem _problem = new Problem(ErrorCode.UNRESOLVED_VARIABLE, obj, ("for hVariable:" + hVar));
      throw new HDLProblemException(_problem);
    }
    return this.resolveVariable(obj.getContainer(), hVar);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLEnumDeclaration obj) {
    return Collections.<HDLEnumDeclaration>singletonList(obj);
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLInterfaceDeclaration obj) {
    return Collections.<HDLInterface>singletonList(obj.getHIf());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLVariableDeclaration obj) {
    return obj.getVariables();
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLInterfaceInstantiation obj) {
    return Collections.<HDLVariable>singletonList(obj.getVar());
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLSwitchCaseStatement obj) {
    return HDLResolver.getallEnumDeclarations(obj.getDos());
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLSwitchCaseStatement obj) {
    return HDLResolver.getallInterfaceDeclarations(obj.getDos());
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSwitchCaseStatement obj) {
    return HDLResolver.getallVariableDeclarations(obj.getDos());
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLSwitchStatement obj) {
    final List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      res.addAll(this.doGetEnumDeclarations(c));
    }
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLSwitchStatement obj) {
    final List<HDLInterface> res = new LinkedList<HDLInterface>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      res.addAll(this.doGetInterfaceDeclarations(c));
    }
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLSwitchStatement obj) {
    final List<HDLVariable> res = new LinkedList<HDLVariable>();
    ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
    for (final HDLSwitchCaseStatement c : _cases) {
      res.addAll(this.doGetVariables(c));
    }
    return res;
  }
  
  protected Optional<? extends Iterable<HDLFunction>> _resolveFunctionCall(final HDLPackage obj, final HDLFunctionCall call, final HDLQualifiedName hFunc) {
    HDLLibrary library = obj.getLibrary();
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    return library.resolveFunction(HDLObject.<String>asList(_plus), call, hFunc);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLPackage obj, final HDLQualifiedName hEnum) {
    final Optional<? extends HDLType> res = this.resolveType(obj, hEnum);
    if ((res.isPresent() && (res.get() instanceof HDLEnum))) {
      return ((Optional<HDLEnum>) res);
    }
    return Optional.<HDLEnum>absent();
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLPackage obj, final HDLQualifiedName hIf) {
    final Optional<? extends HDLType> res = this.resolveType(obj, hIf);
    if ((res.isPresent() && (res.get() instanceof HDLInterface))) {
      return ((Optional<HDLInterface>) res);
    }
    return Optional.<HDLInterface>absent();
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLPackage obj, final HDLQualifiedName type) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      library = HDLLibrary.getLibrary(obj.getLibURI());
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    return library.resolve(HDLObject.<String>asList(_plus), type);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLPackage obj, final HDLQualifiedName hVar) {
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      library = HDLLibrary.getLibrary(obj.getLibURI());
    }
    String _pkg = obj.getPkg();
    String _plus = (_pkg + ".*");
    return library.resolveVariable(HDLObject.<String>asList(_plus), hVar);
  }
  
  protected Optional<HDLEnum> _resolveEnum(final HDLUnit obj, final HDLQualifiedName hEnum) {
    final Optional<HDLEnum> resolveEnum = this.resolver(obj, false).resolveEnum(hEnum);
    boolean _isPresent = resolveEnum.isPresent();
    if (_isPresent) {
      return resolveEnum;
    }
    final Optional<? extends HDLType> res = this.resolveType(obj, hEnum);
    if ((res.isPresent() && (res.get() instanceof HDLEnum))) {
      return ((Optional<HDLEnum>) res);
    }
    return Optional.<HDLEnum>absent();
  }
  
  protected Optional<? extends Iterable<HDLFunction>> _resolveFunctionCall(final HDLUnit obj, final HDLFunctionCall call, final HDLQualifiedName hFunc) {
    final Optional<? extends Iterable<HDLFunction>> resolveEnum = this.resolver(obj, false).resolveFunctionCall(call, hFunc);
    boolean _isPresent = resolveEnum.isPresent();
    if (_isPresent) {
      return resolveEnum;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      library = HDLLibrary.getLibrary(obj.getLibURI());
    }
    final ArrayList<String> newImports = obj.getImports();
    newImports.add(FullNameExtension.fullNameOf(obj).skipLast(1).append("*").toString());
    return library.resolveFunction(newImports, call, hFunc);
  }
  
  protected Optional<HDLInterface> _resolveInterface(final HDLUnit obj, final HDLQualifiedName hIf) {
    final Optional<HDLInterface> resolveInterface = this.resolver(obj, false).resolveInterface(hIf);
    boolean _isPresent = resolveInterface.isPresent();
    if (_isPresent) {
      return resolveInterface;
    }
    final Optional<? extends HDLType> res = this.resolveType(obj, hIf);
    if ((res.isPresent() && (res.get() instanceof HDLInterface))) {
      return ((Optional<HDLInterface>) res);
    }
    return Optional.<HDLInterface>absent();
  }
  
  protected Optional<? extends HDLType> _resolveType(final HDLUnit obj, final HDLQualifiedName type) {
    final Optional<? extends HDLType> resolveType = this.resolver(obj, false).resolveType(type);
    boolean _isPresent = resolveType.isPresent();
    if (_isPresent) {
      return resolveType;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      library = HDLLibrary.getLibrary(obj.getLibURI());
    }
    final ArrayList<String> newImports = obj.getImports();
    newImports.add(FullNameExtension.fullNameOf(obj).skipLast(1).append("*").toString());
    return library.resolve(newImports, type);
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLUnit obj, final HDLQualifiedName hVar) {
    final Optional<HDLVariable> hdlVariable = this.resolver(obj, false).resolveVariable(hVar);
    boolean _isPresent = hdlVariable.isPresent();
    if (_isPresent) {
      return hdlVariable;
    }
    HDLLibrary library = obj.getLibrary();
    HDLLibrary _library = obj.getLibrary();
    boolean _tripleEquals = (_library == null);
    if (_tripleEquals) {
      library = HDLLibrary.getLibrary(obj.getLibURI());
    }
    final ArrayList<String> newImports = obj.getImports();
    newImports.add(FullNameExtension.fullNameOf(obj).skipLast(1).append("*").toString());
    return library.resolveVariable(newImports, hVar);
  }
  
  protected List<HDLEnumDeclaration> _doGetEnumDeclarations(final HDLUnit obj) {
    final List<HDLEnumDeclaration> res = HDLResolver.getallEnumDeclarations(obj.getInits());
    res.addAll(HDLResolver.getallEnumDeclarations(obj.getStatements()));
    return res;
  }
  
  protected List<HDLInterface> _doGetInterfaceDeclarations(final HDLUnit obj) {
    final List<HDLInterface> res = HDLResolver.getallInterfaceDeclarations(obj.getInits());
    res.addAll(HDLResolver.getallInterfaceDeclarations(obj.getStatements()));
    return res;
  }
  
  protected List<HDLVariable> _doGetVariables(final HDLUnit obj) {
    final List<HDLVariable> res = HDLResolver.getallVariableDeclarations(obj.getInits());
    res.addAll(HDLResolver.getallVariableDeclarations(obj.getStatements()));
    return res;
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLInterface hIf, final HDLQualifiedName hVar) {
    final HDLVariable resolved = ScopingExtension.getVariable(hIf, hVar.getLastSegment());
    if ((resolved != null)) {
      if ((hVar.length == 1)) {
        return Optional.<HDLVariable>of(resolved);
      }
      boolean _equals = FullNameExtension.fullNameOf(hIf).equals(hVar.skipLast(1));
      if (_equals) {
        return Optional.<HDLVariable>of(resolved);
      }
    }
    return this.resolveVariableDefault(hIf, hVar);
  }
  
  private static HDLVariable getVariable(final HDLInterface hIf, final String lastSegment) {
    return HDLQuery.<HDLVariable>select(HDLVariable.class).from(hIf).<String>where(HDLVariable.fName).lastSegmentIs(lastSegment).getFirst();
  }
  
  protected Optional<HDLVariable> _resolveVariable(final HDLEnum hEnum, final HDLQualifiedName hVar) {
    if ((hVar.length == 1)) {
      return ScopingExtension.getVariable(hEnum, hVar.getLastSegment());
    }
    boolean _equals = FullNameExtension.fullNameOf(hEnum).equals(hVar.skipLast(1));
    if (_equals) {
      return ScopingExtension.getVariable(hEnum, hVar.getLastSegment());
    }
    return this.resolveVariable(hEnum, hVar);
  }
  
  public static Optional<HDLVariable> getVariable(final HDLEnum hEnum, final String lastSegment) {
    ArrayList<HDLVariable> _enums = hEnum.getEnums();
    for (final HDLVariable hVar : _enums) {
      boolean _equals = hVar.getName().equals(lastSegment);
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
  
  public Optional<? extends Iterable<HDLFunction>> resolveFunctionCall(final IHDLObject obj, final HDLFunctionCall call, final HDLQualifiedName hFunc) {
    if (obj instanceof HDLPackage) {
      return _resolveFunctionCall((HDLPackage)obj, call, hFunc);
    } else if (obj instanceof HDLUnit) {
      return _resolveFunctionCall((HDLUnit)obj, call, hFunc);
    } else if (obj instanceof HDLStatement) {
      return _resolveFunctionCall((HDLStatement)obj, call, hFunc);
    } else if (obj != null) {
      return _resolveFunctionCall(obj, call, hFunc);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj, call, hFunc).toString());
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
  
  public List<HDLEnumDeclaration> doGetEnumDeclarations(final IHDLObject obj) {
    if (obj instanceof HDLBlock) {
      return _doGetEnumDeclarations((HDLBlock)obj);
    } else if (obj instanceof HDLDirectGeneration) {
      return _doGetEnumDeclarations((HDLDirectGeneration)obj);
    } else if (obj instanceof HDLEnumDeclaration) {
      return _doGetEnumDeclarations((HDLEnumDeclaration)obj);
    } else if (obj instanceof HDLForLoop) {
      return _doGetEnumDeclarations((HDLForLoop)obj);
    } else if (obj instanceof HDLIfStatement) {
      return _doGetEnumDeclarations((HDLIfStatement)obj);
    } else if (obj instanceof HDLSwitchCaseStatement) {
      return _doGetEnumDeclarations((HDLSwitchCaseStatement)obj);
    } else if (obj instanceof HDLSwitchStatement) {
      return _doGetEnumDeclarations((HDLSwitchStatement)obj);
    } else if (obj instanceof HDLUnit) {
      return _doGetEnumDeclarations((HDLUnit)obj);
    } else if (obj != null) {
      return _doGetEnumDeclarations(obj);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj).toString());
    }
  }
  
  public List<HDLInterface> doGetInterfaceDeclarations(final IHDLObject obj) {
    if (obj instanceof HDLBlock) {
      return _doGetInterfaceDeclarations((HDLBlock)obj);
    } else if (obj instanceof HDLDirectGeneration) {
      return _doGetInterfaceDeclarations((HDLDirectGeneration)obj);
    } else if (obj instanceof HDLForLoop) {
      return _doGetInterfaceDeclarations((HDLForLoop)obj);
    } else if (obj instanceof HDLIfStatement) {
      return _doGetInterfaceDeclarations((HDLIfStatement)obj);
    } else if (obj instanceof HDLInterfaceDeclaration) {
      return _doGetInterfaceDeclarations((HDLInterfaceDeclaration)obj);
    } else if (obj instanceof HDLSwitchCaseStatement) {
      return _doGetInterfaceDeclarations((HDLSwitchCaseStatement)obj);
    } else if (obj instanceof HDLSwitchStatement) {
      return _doGetInterfaceDeclarations((HDLSwitchStatement)obj);
    } else if (obj instanceof HDLUnit) {
      return _doGetInterfaceDeclarations((HDLUnit)obj);
    } else if (obj != null) {
      return _doGetInterfaceDeclarations(obj);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(obj).toString());
    }
  }
  
  public List<HDLVariable> doGetVariables(final IHDLObject obj) {
    if (obj instanceof HDLInlineFunction) {
      return _doGetVariables((HDLInlineFunction)obj);
    } else if (obj instanceof HDLNativeFunction) {
      return _doGetVariables((HDLNativeFunction)obj);
    } else if (obj instanceof HDLSubstituteFunction) {
      return _doGetVariables((HDLSubstituteFunction)obj);
    } else if (obj instanceof HDLBlock) {
      return _doGetVariables((HDLBlock)obj);
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
