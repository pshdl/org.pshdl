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
package org.pshdl.generator.vhdl;

import java.util.*;
import java.util.Map.Entry;

import org.eclipse.xtext.xbase.lib.*;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.pshdl.generator.vhdl.libraries.*;
import org.pshdl.model.*;
import org.pshdl.model.HDLRegisterConfig.HDLRegClockType;
import org.pshdl.model.HDLRegisterConfig.HDLRegResetActiveType;
import org.pshdl.model.HDLRegisterConfig.HDLRegSyncType;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.extensions.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.HDLQuery.FieldSelector;
import org.pshdl.model.utils.HDLQuery.Result;
import org.pshdl.model.utils.HDLQuery.Selector;
import org.pshdl.model.utils.HDLQuery.Source;

import com.google.common.base.*;
import com.google.common.base.Objects;

import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.concurrent.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.libraryunit.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.statement.*;
import de.upb.hni.vmagic.statement.IfStatement.ElsifPart;
import de.upb.hni.vmagic.type.*;

@SuppressWarnings("all")
public class VHDLPackageExtension {
	@Extension
	private VHDLExpressionExtension vee = new Function0<VHDLExpressionExtension>() {
		@Override
		public VHDLExpressionExtension apply() {
			VHDLExpressionExtension _vHDLExpressionExtension = new VHDLExpressionExtension();
			return _vHDLExpressionExtension;
		}
	}.apply();

	@Extension
	private VHDLStatementExtension vse = new Function0<VHDLStatementExtension>() {
		@Override
		public VHDLStatementExtension apply() {
			VHDLStatementExtension _vHDLStatementExtension = new VHDLStatementExtension();
			return _vHDLStatementExtension;
		}
	}.apply();

	public static VHDLPackageExtension INST = new Function0<VHDLPackageExtension>() {
		@Override
		public VHDLPackageExtension apply() {
			VHDLPackageExtension _vHDLPackageExtension = new VHDLPackageExtension();
			return _vHDLPackageExtension;
		}
	}.apply();

	public List<LibraryUnit> toVHDL(final HDLUnit obj) {
		LinkedList<LibraryUnit> _linkedList = new LinkedList<LibraryUnit>();
		final List<LibraryUnit> res = _linkedList;
		final HDLQualifiedName entityName = FullNameExtension.fullNameOf(obj);
		String _dashString = this.dashString(entityName);
		Entity _entity = new Entity(_dashString);
		final Entity e = _entity;
		VHDLContext _vHDLContext = new VHDLContext();
		final VHDLContext unit = _vHDLContext;
		final HDLEnumRef[] hRefs = obj.<HDLEnumRef> getAllObjectsOf(HDLEnumRef.class, true);
		for (final HDLEnumRef hdlEnumRef : hRefs) {
			{
				final Optional<HDLEnum> resolveHEnum = hdlEnumRef.resolveHEnum();
				HDLEnum _get = resolveHEnum.get();
				final HDLUnit enumContainer = _get.<HDLUnit> getContainer(HDLUnit.class);
				boolean _or = false;
				boolean _tripleEquals = (enumContainer == null);
				if (_tripleEquals) {
					_or = true;
				} else {
					HDLUnit _container = hdlEnumRef.<HDLUnit> getContainer(HDLUnit.class);
					boolean _equals = enumContainer.equals(_container);
					boolean _not = (!_equals);
					_or = (_tripleEquals || _not);
				}
				if (_or) {
					HDLEnum _get_1 = resolveHEnum.get();
					final HDLQualifiedName type = FullNameExtension.fullNameOf(_get_1);
					String _segment = type.getSegment(0);
					boolean _equals_1 = _segment.equals("pshdl");
					boolean _not_1 = (!_equals_1);
					if (_not_1) {
						String _packageName = this.getPackageName(type);
						HDLQualifiedName _create = HDLQualifiedName.create("work", _packageName, "all");
						unit.addImport(_create);
					}
				}
			}
		}
		final HDLVariableRef[] vRefs = obj.<HDLVariableRef> getAllObjectsOf(HDLVariableRef.class, true);
		for (final HDLVariableRef variableRef : vRefs) {
			HDLClass _classType = variableRef.getClassType();
			boolean _notEquals = (!Objects.equal(_classType, HDLClass.HDLInterfaceRef));
			if (_notEquals) {
				final Optional<HDLVariable> variable = variableRef.resolveVar();
				boolean _isPresent = variable.isPresent();
				boolean _not = (!_isPresent);
				if (_not) {
					String _plus = ("Can not resolve:" + variableRef);
					IllegalArgumentException _illegalArgumentException = new IllegalArgumentException(_plus);
					throw _illegalArgumentException;
				}
				HDLVariable _get = variable.get();
				final HDLUnit enumContainer = _get.<HDLUnit> getContainer(HDLUnit.class);
				boolean _or = false;
				boolean _tripleEquals = (enumContainer == null);
				if (_tripleEquals) {
					_or = true;
				} else {
					HDLUnit _container = variableRef.<HDLUnit> getContainer(HDLUnit.class);
					boolean _equals = enumContainer.equals(_container);
					boolean _not_1 = (!_equals);
					_or = (_tripleEquals || _not_1);
				}
				if (_or) {
					HDLVariable _get_1 = variable.get();
					HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(_get_1);
					final HDLQualifiedName type = _fullNameOf.skipLast(1);
					boolean _and = false;
					boolean _greaterThan = (type.length > 0);
					if (!_greaterThan) {
						_and = false;
					} else {
						String _segment = type.getSegment(0);
						boolean _equals_1 = _segment.equals("pshdl");
						boolean _not_2 = (!_equals_1);
						_and = (_greaterThan && _not_2);
					}
					if (_and) {
						String _packageName = this.getPackageName(type);
						HDLQualifiedName _create = HDLQualifiedName.create("work", _packageName, "all");
						unit.addImport(_create);
					}
				}
			}
		}
		ArrayList<HDLStatement> _inits = obj.getInits();
		for (final HDLStatement stmnt : _inits) {
			VHDLContext _vHDL = this.vse.toVHDL(stmnt, VHDLContext.DEFAULT_CTX);
			unit.merge(_vHDL, false);
		}
		ArrayList<HDLStatement> _statements = obj.getStatements();
		for (final HDLStatement stmnt_1 : _statements) {
			VHDLContext _vHDL_1 = this.vse.toVHDL(stmnt_1, VHDLContext.DEFAULT_CTX);
			unit.merge(_vHDL_1, false);
		}
		VHDLPackageExtension.addDefaultLibs(res, unit);
		boolean _hasPkgDeclarations = unit.hasPkgDeclarations();
		if (_hasPkgDeclarations) {
			final String libName = this.getPackageName(entityName);
			PackageDeclaration _packageDeclaration = new PackageDeclaration(libName);
			final PackageDeclaration pd = _packageDeclaration;
			List<PackageDeclarativeItem> _declarations = pd.getDeclarations();
			_declarations.addAll(((List) unit.externalTypes));
			List<PackageDeclarativeItem> _declarations_1 = pd.getDeclarations();
			_declarations_1.addAll(unit.constantsPkg);
			res.add(pd);
			String _plus_1 = ("work." + libName);
			String _plus_2 = (_plus_1 + ".all");
			UseClause _useClause = new UseClause(_plus_2);
			res.add(_useClause);
			VHDLPackageExtension.addDefaultLibs(res, unit);
		}
		List<VhdlObjectProvider<Signal>> _port = e.getPort();
		_port.addAll(unit.ports);
		List<VhdlObjectProvider<Constant>> _generic = e.getGeneric();
		_generic.addAll(unit.generics);
		List<EntityDeclarativeItem> _declarations_2 = e.getDeclarations();
		_declarations_2.addAll(unit.constants);
		res.add(e);
		Architecture _architecture = new Architecture("pshdlGenerated", e);
		final Architecture a = _architecture;
		List<EntityDeclarativeItem> _declarations_3 = e.getDeclarations();
		_declarations_3.addAll(((List) unit.internalTypes));
		List<BlockDeclarativeItem> _declarations_4 = a.getDeclarations();
		_declarations_4.addAll(((List) unit.internals));
		List<ConcurrentStatement> _statements_1 = a.getStatements();
		_statements_1.addAll(unit.concurrentStatements);
		Set<Entry<Integer, LinkedList<SequentialStatement>>> _entrySet = unit.unclockedStatements.entrySet();
		for (final Entry<Integer, LinkedList<SequentialStatement>> uc : _entrySet) {
			{
				ProcessStatement _processStatement = new ProcessStatement();
				final ProcessStatement ps = _processStatement;
				List<Signal> _sensitivityList = ps.getSensitivityList();
				Integer _key = uc.getKey();
				Collection<? extends Signal> _createSensitivyList = this.createSensitivyList(unit, (_key).intValue());
				_sensitivityList.addAll(_createSensitivyList);
				List<SequentialStatement> _statements_2 = ps.getStatements();
				LinkedList<SequentialStatement> _value = uc.getValue();
				_statements_2.addAll(_value);
				List<ConcurrentStatement> _statements_3 = a.getStatements();
				_statements_3.add(ps);
			}
		}
		Set<Entry<HDLRegisterConfig, LinkedList<SequentialStatement>>> _entrySet_1 = unit.clockedStatements.entrySet();
		for (final Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> pc : _entrySet_1) {
			{
				ProcessStatement _processStatement = new ProcessStatement();
				final ProcessStatement ps = _processStatement;
				List<SequentialStatement> _statements_2 = ps.getStatements();
				HDLRegisterConfig _key = pc.getKey();
				LinkedList<SequentialStatement> _value = pc.getValue();
				SequentialStatement _createIfStatement = this.createIfStatement(obj, ps, _key, _value, unit);
				_statements_2.add(_createIfStatement);
				List<ConcurrentStatement> _statements_3 = a.getStatements();
				_statements_3.add(ps);
			}
		}
		res.add(a);
		return res;
	}

	public String getPackageName(final HDLQualifiedName entityName) {
		String _dashString = this.dashString(entityName);
		return (_dashString + "Pkg");
	}

	public String dashString(final HDLQualifiedName name) {
		char _charAt = "_".charAt(0);
		return name.toString(_charAt);
	}

	public HDLQualifiedName getPackageNameRef(final HDLQualifiedName entityName) {
		String _segment = entityName.getSegment(0);
		boolean _equals = _segment.equals("VHDL");
		if (_equals)
			return entityName.skipFirst(1);
		String _dashString = this.dashString(entityName);
		String _plus = (_dashString + "Pkg");
		return HDLQualifiedName.create("work", _plus);
	}

	public HDLQualifiedName getNameRef(final HDLQualifiedName entityName) {
		String _segment = entityName.getSegment(0);
		boolean _equals = _segment.equals("VHDL");
		if (_equals)
			return entityName.skipFirst(1);
		String _dashString = this.dashString(entityName);
		return HDLQualifiedName.create("work", _dashString);
	}

	private static void addDefaultLibs(final List<LibraryUnit> res, final VHDLContext unit) {
		LibraryClause _libraryClause = new LibraryClause("ieee");
		res.add(_libraryClause);
		res.add(StdLogic1164.USE_CLAUSE);
		res.add(NumericStd.USE_CLAUSE);
		LibraryClause _libraryClause_1 = new LibraryClause("pshdl");
		res.add(_libraryClause_1);
		res.add(VHDLCastsLibrary.USE_CLAUSE);
		res.add(VHDLShiftLibrary.USE_CLAUSE);
		UseClause _useClause = new UseClause("pshdl.types.all");
		res.add(_useClause);
		HashSet<String> _hashSet = new HashSet<String>();
		final Set<String> usedLibs = _hashSet;
		usedLibs.add("pshdl");
		usedLibs.add("ieee");
		usedLibs.add("work");
		for (final HDLQualifiedName i : unit.imports) {
			{
				final String lib = i.getSegment(0);
				boolean _contains = usedLibs.contains(lib);
				boolean _not = (!_contains);
				if (_not) {
					LibraryClause _libraryClause_2 = new LibraryClause(lib);
					res.add(_libraryClause_2);
					usedLibs.add(lib);
				}
				HDLQualifiedName _append = i.append("all");
				String _string = _append.toString();
				UseClause _useClause_1 = new UseClause(_string);
				res.add(_useClause_1);
			}
		}
	}

	private static EnumSet<HDLDirection> notSensitive = new Function0<EnumSet<HDLDirection>>() {
		@Override
		public EnumSet<HDLDirection> apply() {
			EnumSet<HDLDirection> _of = EnumSet.<HDLDirection> of(HDLDirection.HIDDEN, HDLDirection.PARAMETER, HDLDirection.CONSTANT);
			return _of;
		}
	}.apply();

	private Collection<? extends Signal> createSensitivyList(final VHDLContext ctx, final int pid) {
		boolean _containsKey = ctx.noSensitivity.containsKey(Integer.valueOf(pid));
		if (_containsKey)
			return Collections.<Signal> emptyList();
		LinkedList<Signal> _linkedList = new LinkedList<Signal>();
		final List<Signal> sensitivity = _linkedList;
		TreeSet<String> _treeSet = new TreeSet<String>();
		final Set<String> vars = _treeSet;
		LinkedList<HDLStatement> _get = ctx.sensitiveStatements.get(Integer.valueOf(pid));
		for (final HDLStatement stmnt : _get) {
			{
				final HDLVariableRef[] refs = stmnt.<HDLVariableRef> getAllObjectsOf(HDLVariableRef.class, true);
				for (final HDLVariableRef ref : refs) {
					{
						final Optional<HDLVariable> hvar = ref.resolveVar();
						HDLVariable _get_1 = hvar.get();
						final IHDLObject container = _get_1.getContainer();
						if ((container instanceof HDLVariableDeclaration)) {
							final HDLVariableDeclaration hdv = ((HDLVariableDeclaration) container);
							HDLDirection _direction = hdv.getDirection();
							boolean _contains = VHDLPackageExtension.notSensitive.contains(_direction);
							boolean _not = (!_contains);
							if (_not) {
								IHDLObject _container = ref.getContainer();
								if ((_container instanceof HDLAssignment)) {
									IHDLObject _container_1 = ref.getContainer();
									final HDLAssignment hAss = ((HDLAssignment) _container_1);
									HDLReference _left = hAss.getLeft();
									HDLVariable _resolveVar = this.resolveVar(_left);
									HDLRegisterConfig _registerConfig = _resolveVar.getRegisterConfig();
									boolean _tripleNotEquals = (_registerConfig != null);
									if (_tripleNotEquals) {
									} else {
										HDLReference _left_1 = hAss.getLeft();
										boolean _notEquals = (!Objects.equal(_left_1, ref));
										if (_notEquals) {
											String _vHDLName = this.vee.getVHDLName(ref);
											vars.add(_vHDLName);
										}
									}
								} else {
									String _vHDLName_1 = this.vee.getVHDLName(ref);
									vars.add(_vHDLName_1);
								}
							}
						}
					}
				}
			}
		}
		for (final String string : vars) {
			Signal _signal = new Signal(string, UnresolvedType.NO_NAME);
			sensitivity.add(_signal);
		}
		return sensitivity;
	}

	public HDLVariable resolveVar(final HDLReference reference) {
		if ((reference instanceof HDLUnresolvedFragment)) {
			RuntimeException _runtimeException = new RuntimeException("Can not use unresolved fragments");
			throw _runtimeException;
		}
		Optional<HDLVariable> _resolveVar = ((HDLResolvedRef) reference).resolveVar();
		return _resolveVar.get();
	}

	private SequentialStatement createIfStatement(final HDLUnit hUnit, final ProcessStatement ps, final HDLRegisterConfig key, final LinkedList<SequentialStatement> value,
			final VHDLContext unit) {
		HDLVariableRef _hDLVariableRef = new HDLVariableRef();
		HDLQualifiedName _clkRefName = key.getClkRefName();
		HDLVariableRef _setVar = _hDLVariableRef.setVar(_clkRefName);
		Expression<? extends Object> _vHDL = this.vee.toVHDL(_setVar);
		Signal clk = ((Signal) _vHDL);
		HDLVariableRef _hDLVariableRef_1 = new HDLVariableRef();
		HDLQualifiedName _rstRefName = key.getRstRefName();
		HDLVariableRef _setVar_1 = _hDLVariableRef_1.setVar(_rstRefName);
		Expression<? extends Object> _vHDL_1 = this.vee.toVHDL(_setVar_1);
		Signal rst = ((Signal) _vHDL_1);
		String _libURI = hUnit.getLibURI();
		HDLLibrary _library = HDLLibrary.getLibrary(_libURI);
		final HDLConfig config = _library.getConfig();
		List<Signal> _sensitivityList = ps.getSensitivityList();
		_sensitivityList.add(clk);
		EnumerationLiteral activeRst = null;
		HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hUnit);
		HDLRegResetActiveType _resetType = key.getResetType();
		HDLRegResetActiveType _regResetType = config.getRegResetType(_fullNameOf, _resetType);
		boolean _equals = Objects.equal(_regResetType, HDLRegResetActiveType.HIGH);
		if (_equals) {
			activeRst = StdLogic1164.STD_LOGIC_1;
		} else {
			activeRst = StdLogic1164.STD_LOGIC_0;
		}
		Equals _equals_1 = new Equals(rst, activeRst);
		IfStatement _ifStatement = new IfStatement(_equals_1);
		IfStatement rstIfStmnt = _ifStatement;
		final LinkedList<SequentialStatement> resets = unit.resetStatements.get(key);
		boolean _tripleNotEquals = (resets != null);
		if (_tripleNotEquals) {
			List<SequentialStatement> _statements = rstIfStmnt.getStatements();
			_statements.addAll(resets);
		}
		FunctionCall edge = null;
		HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(hUnit);
		HDLRegClockType _clockType = key.getClockType();
		HDLRegClockType _regClockType = config.getRegClockType(_fullNameOf_1, _clockType);
		boolean _equals_2 = Objects.equal(_regClockType, HDLRegClockType.RISING);
		if (_equals_2) {
			FunctionCall _functionCall = new FunctionCall(StdLogic1164.RISING_EDGE);
			edge = _functionCall;
		} else {
			FunctionCall _functionCall_1 = new FunctionCall(StdLogic1164.FALLING_EDGE);
			edge = _functionCall_1;
		}
		List<AssociationElement> _parameters = edge.getParameters();
		AssociationElement _associationElement = new AssociationElement(clk);
		_parameters.add(_associationElement);
		HDLQualifiedName _fullNameOf_2 = FullNameExtension.fullNameOf(hUnit);
		HDLRegSyncType _syncType = key.getSyncType();
		HDLRegSyncType _regSyncType = config.getRegSyncType(_fullNameOf_2, _syncType);
		boolean _equals_3 = Objects.equal(_regSyncType, HDLRegSyncType.ASYNC);
		if (_equals_3) {
			List<Signal> _sensitivityList_1 = ps.getSensitivityList();
			_sensitivityList_1.add(rst);
			final ElsifPart elsifPart = rstIfStmnt.createElsifPart(edge);
			List<SequentialStatement> _statements_1 = elsifPart.getStatements();
			_statements_1.addAll(value);
			return rstIfStmnt;
		}
		IfStatement _ifStatement_1 = new IfStatement(edge);
		final IfStatement clkIf = _ifStatement_1;
		List<SequentialStatement> _statements_2 = clkIf.getStatements();
		_statements_2.add(rstIfStmnt);
		List<SequentialStatement> _elseStatements = rstIfStmnt.getElseStatements();
		_elseStatements.addAll(value);
		return clkIf;
	}

	public VhdlFile toVHDL(final HDLPackage obj) {
		VhdlFile _vhdlFile = new VhdlFile();
		final VhdlFile res = _vhdlFile;
		ArrayList<HDLUnit> _units = obj.getUnits();
		for (final HDLUnit unit : _units) {
			{
				ModificationSet _modificationSet = new ModificationSet();
				final ModificationSet ms = _modificationSet;
				final HDLVariableDeclaration[] hvds = unit.<HDLVariableDeclaration> getAllObjectsOf(HDLVariableDeclaration.class, true);
				for (final HDLVariableDeclaration hvd : hvds) {
					ArrayList<HDLVariable> _variables = hvd.getVariables();
					for (final HDLVariable hvar : _variables) {
						{
							final HDLVariableRef[] refs = hvar.<HDLVariableRef> getAllObjectsOf(HDLVariableRef.class, true);
							for (final HDLVariableRef ref : refs) {
								Optional<HDLVariable> _resolveVar = ref.resolveVar();
								HDLVariable _get = _resolveVar.get();
								_get.setMeta(VHDLStatementExtension.EXPORT);
							}
							final String origName = hvar.getName();
							final String name = VHDLOutputValidator.getVHDLName(origName);
							boolean _equals = origName.equals(name);
							boolean _not = (!_equals);
							if (_not) {
								final HDLVariable newVar = hvar.setName(name);
								ms.replace(hvar, newVar);
								Source<HDLVariableRef> _select = HDLQuery.<HDLVariableRef> select(HDLVariableRef.class);
								Selector<HDLVariableRef> _from = _select.from(obj);
								FieldSelector<HDLVariableRef, HDLQualifiedName> _where = _from.<HDLQualifiedName> where(HDLResolvedRef.fVar);
								HDLQualifiedName _asRef = hvar.asRef();
								Result<HDLVariableRef, HDLQualifiedName> _isEqualTo = _where.isEqualTo(_asRef);
								final Collection<HDLVariableRef> varRefs = _isEqualTo.getAll();
								final HDLQualifiedName newVarRef = newVar.asRef();
								for (final HDLVariableRef ref_1 : varRefs) {
									HDLVariableRef _setVar = ref_1.setVar(newVarRef);
									ms.replace(ref_1, _setVar);
								}
							}
						}
					}
				}
				final HDLUnit newUnit = ms.<HDLUnit> apply(unit);
				List<LibraryUnit> _elements = res.getElements();
				List<LibraryUnit> _vHDL = this.toVHDL(newUnit);
				_elements.addAll(_vHDL);
			}
		}
		PackageDeclaration pd = null;
		ArrayList<HDLDeclaration> _declarations = obj.getDeclarations();
		for (final HDLDeclaration decl : _declarations) {
			{
				HDLClass _classType = decl.getClassType();
				boolean _equals = Objects.equal(_classType, HDLClass.HDLVariableDeclaration);
				if (_equals) {
					final HDLVariableDeclaration hvd = ((HDLVariableDeclaration) decl);
					boolean _tripleEquals = (pd == null);
					if (_tripleEquals) {
						String _pkg = obj.getPkg();
						HDLQualifiedName _hDLQualifiedName = new HDLQualifiedName(_pkg);
						String _packageName = this.getPackageName(_hDLQualifiedName);
						PackageDeclaration _packageDeclaration = new PackageDeclaration(_packageName);
						pd = _packageDeclaration;
						List<LibraryUnit> _elements = res.getElements();
						_elements.add(pd);
					}
					final VHDLContext vhdl = this.vse.toVHDL(hvd, VHDLContext.DEFAULT_CTX);
					ConstantDeclaration first = vhdl.constants.getFirst();
					boolean _tripleEquals_1 = (first == null);
					if (_tripleEquals_1) {
						ConstantDeclaration _first = vhdl.constantsPkg.getFirst();
						first = _first;
						boolean _tripleEquals_2 = (first == null);
						if (_tripleEquals_2) {
							IllegalArgumentException _illegalArgumentException = new IllegalArgumentException("Expected constant declaration but found none!");
							throw _illegalArgumentException;
						}
					}
					List<PackageDeclarativeItem> _declarations_1 = pd.getDeclarations();
					_declarations_1.add(first);
				}
				HDLClass _classType_1 = decl.getClassType();
				boolean _equals_1 = Objects.equal(_classType_1, HDLClass.HDLEnumDeclaration);
				if (_equals_1) {
					final HDLEnumDeclaration hvd_1 = ((HDLEnumDeclaration) decl);
					HDLEnum _hEnum = hvd_1.getHEnum();
					HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(_hEnum);
					String _packageName_1 = this.getPackageName(_fullNameOf);
					PackageDeclaration _packageDeclaration_1 = new PackageDeclaration(_packageName_1);
					final PackageDeclaration enumPd = _packageDeclaration_1;
					List<LibraryUnit> _elements_1 = res.getElements();
					_elements_1.add(enumPd);
					final VHDLContext vhdl_1 = this.vse.toVHDL(hvd_1, VHDLContext.DEFAULT_CTX);
					BlockDeclarativeItem _first_1 = vhdl_1.internalTypes.getFirst();
					final Type first_1 = ((Type) _first_1);
					boolean _tripleEquals_3 = (first_1 == null);
					if (_tripleEquals_3) {
						IllegalArgumentException _illegalArgumentException_1 = new IllegalArgumentException("Expected enum type declaration but found none!");
						throw _illegalArgumentException_1;
					}
					List<PackageDeclarativeItem> _declarations_2 = enumPd.getDeclarations();
					_declarations_2.add(first_1);
				}
			}
		}
		return res;
	}
}
