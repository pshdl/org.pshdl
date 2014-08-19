/*******************************************************************************
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
 ******************************************************************************/
package org.pshdl.model.utils;

import static org.pshdl.model.extensions.FullNameExtension.fullNameOf;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.extensions.ScopingExtension;

import com.google.common.base.Optional;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public class HDLResolver {

	private final boolean descent;

	private Map<HDLQualifiedName, HDLEnum> enumCache;

	private Map<HDLQualifiedName, HDLInterface> ifCache;

	private Multimap<HDLQualifiedName, HDLFunction> funcCache;

	private final IHDLObject resolveTo;

	private Map<HDLQualifiedName, HDLType> typeCache;

	private Map<HDLQualifiedName, HDLVariable> variableCache;

	private IHDLObject resolveContainer;

	private HDLQualifiedName resolveName;

	public HDLResolver(IHDLObject resolveTo, boolean descent) {
		super();
		this.resolveTo = resolveTo;
		this.resolveContainer = resolveTo.getContainer();
		this.resolveName = fullNameOf(resolveTo);
		this.descent = descent;
	}

	public HDLResolver(IHDLObject resolveTo, boolean descent, String libURI) {
		super();
		this.resolveTo = resolveTo;
		this.descent = descent;
	}

	protected List<HDLType> doGetTypeDeclarations() {
		final List<HDLType> types = new LinkedList<HDLType>();
		for (final HDLEnumDeclaration hEnumDecl : ScopingExtension.INST.doGetEnumDeclarations(resolveTo)) {
			types.add(hEnumDecl.getHEnum());
		}
		for (final HDLVariable varDecl : ScopingExtension.INST.doGetVariables(resolveTo)) {
			final IHDLObject container = varDecl.getContainer();
			if (container instanceof HDLVariableDeclaration) {
				final HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
				if (hvd.getPrimitive() != null) {
					types.add(hvd.getPrimitive());
				}
			}
		}
		for (final HDLInterface ifDecl : ScopingExtension.INST.doGetInterfaceDeclarations(resolveTo)) {
			types.add(ifDecl);
		}
		return types;
	}

	public Optional<HDLEnum> resolveEnum(HDLQualifiedName hEnum) {
		if (enumCache == null) {
			synchronized (this) {
				final HDLEnumDeclaration[] enumDecl = resolveTo.getAllObjectsOf(HDLEnumDeclaration.class, false);
				enumCache = Maps.newLinkedHashMap();
				for (final HDLEnumDeclaration hdlEnumDeclaration : enumDecl) {
					enumCache.put(fullNameOf(hdlEnumDeclaration.getHEnum()), hdlEnumDeclaration.getHEnum());
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		final HDLEnum checkCache = checkCache(hEnum, enumCache);
		if (checkCache != null)
			return Optional.of(checkCache);
		if ((resolveContainer == null) || !descent)
			return Optional.absent();
		return ScopingExtension.INST.resolveEnum(resolveContainer, hEnum);
	}

	public Optional<Iterable<HDLFunction>> resolveFunction(HDLQualifiedName hEnum) {
		if (funcCache == null) {
			synchronized (this) {
				final HDLFunction[] funcDecl = resolveTo.getAllObjectsOf(HDLFunction.class, false);
				funcCache = LinkedHashMultimap.create();
				for (final HDLFunction hdlfuncDeclaration : funcDecl) {
					funcCache.put(fullNameOf(hdlfuncDeclaration), hdlfuncDeclaration);
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		final Iterable<HDLFunction> checkCache = checkCache(hEnum, funcCache);
		if ((checkCache != null) && checkCache.iterator().hasNext())
			return Optional.of(checkCache);
		if ((resolveContainer == null) || !descent)
			return Optional.absent();
		return ScopingExtension.INST.resolveFunctionName(resolveContainer, hEnum);
	}

	public Optional<HDLInterface> resolveInterface(HDLQualifiedName hIf) {
		if (ifCache == null) {
			synchronized (this) {
				final List<HDLInterface> ifDecl = ScopingExtension.INST.doGetInterfaceDeclarations(resolveTo);
				ifCache = Maps.newLinkedHashMap();
				for (final HDLInterface hdlIfDeclaration : ifDecl) {
					final HDLQualifiedName fqn = fullNameOf(hdlIfDeclaration);
					// Usually this should not happen, but when the interface
					// can not be resolved, it might happen.
					if (fqn != null) {
						ifCache.put(fqn, hdlIfDeclaration);
					}
				}
			}
		}
		// XXX Check if the qualifier does either match the pkg name, or is not
		// existant
		final HDLInterface checkCache = checkCache(hIf, ifCache);
		if (checkCache != null)
			return Optional.of(checkCache);
		if ((resolveContainer == null) || !descent)
			return Optional.absent();
		return ScopingExtension.INST.resolveInterface(resolveContainer, hIf);
	}

	public Optional<? extends HDLType> resolveType(HDLQualifiedName var) {
		if (typeCache == null) {
			synchronized (this) {
				final List<HDLType> typeDecl = doGetTypeDeclarations();
				typeCache = Maps.newLinkedHashMap();
				for (final HDLType hdlTypeDeclaration : typeDecl) {
					if (hdlTypeDeclaration.getClassType() != HDLClass.HDLPrimitive) {
						typeCache.put(fullNameOf(hdlTypeDeclaration), hdlTypeDeclaration);
					}
				}
			}
		}
		final HDLType checkCache = checkCache(var, typeCache);
		if (checkCache != null)
			return Optional.of(checkCache);
		if ((resolveContainer == null) || !descent) {
			if (resolveTo instanceof HDLUnit) {
				final HDLUnit unit = (HDLUnit) resolveTo;
				final String uri = unit.getLibURI();
				if (uri != null) {
					final HDLLibrary library = HDLLibrary.getLibrary(uri);
					if (library != null) {
						final List<String> imports = unit.getImports();
						imports.add(fullNameOf(unit).skipLast(1).append("*").toString());
						return library.resolve(imports, var);
					}
				}
			}
			return Optional.absent();
		}
		return ScopingExtension.INST.resolveType(resolveContainer, var);
	}

	public Optional<HDLVariable> resolveVariable(HDLQualifiedName var) {
		if (variableCache == null) {
			synchronized (this) {
				final List<HDLVariable> varDecl = ScopingExtension.INST.doGetVariables(resolveTo);
				variableCache = Maps.newLinkedHashMap();
				for (final HDLVariable declVars : varDecl) {
					variableCache.put(fullNameOf(declVars), declVars);
				}
			}
		}
		final HDLVariable checkCache = checkCache(var, variableCache);
		if (checkCache != null)
			return Optional.of(checkCache);
		if (var.length > 1) {
			// Using lastSgement if $for0.I or ThisObject.I
			if (var.getSegment(0).startsWith("$") || var.getTypePart().equals(resolveName.getTypePart())) {
				final String string = var.getLastSegment();
				for (final Entry<HDLQualifiedName, HDLVariable> entry : variableCache.entrySet())
					if (entry.getKey().getLastSegment().equals(string))
						return Optional.of(entry.getValue());
			}
		}
		if (HDLRegisterConfig.DEF_CLK.equals(var.getLastSegment()))
			return Optional.of(HDLRegisterConfig.defaultClk(true));
		if (HDLRegisterConfig.DEF_RST.equals(var.getLastSegment()))
			return Optional.of(HDLRegisterConfig.defaultRst(true));
		final IHDLObject container = resolveContainer;
		if ((container == null) || !descent)
			return Optional.absent();
		return ScopingExtension.INST.resolveVariable(container, var);
	}

	private <T> Iterable<T> checkCache(HDLQualifiedName var, Multimap<HDLQualifiedName, T> map) {
		if (map.get(var) != null)
			return map.get(var);
		if (var.length == 1) {
			final HDLQualifiedName fqn = resolveName.append(var);
			if (map.get(fqn) != null)
				return map.get(fqn);
		}
		return null;
	}

	private <T> T checkCache(HDLQualifiedName var, Map<HDLQualifiedName, T> map) {
		if (map.get(var) != null)
			return map.get(var);
		if (var.length == 1) {
			final HDLQualifiedName fqn = resolveName.append(var);
			if (map.get(fqn) != null)
				return map.get(fqn);
		}
		return null;
	}

	public static List<HDLEnumDeclaration> getallEnumDeclarations(List<HDLStatement> stmnts) {
		final List<HDLEnumDeclaration> res = new LinkedList<HDLEnumDeclaration>();
		for (final HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetEnumDeclarations(hdlStatement));
		}
		return res;
	}

	public static List<HDLInterface> getallInterfaceDeclarations(List<HDLStatement> stmnts) {
		final List<HDLInterface> res = new LinkedList<HDLInterface>();
		for (final HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetInterfaceDeclarations(hdlStatement));
		}
		return res;
	}

	public static List<HDLVariable> getallVariableDeclarations(List<HDLStatement> stmnts) {
		final List<HDLVariable> res = new LinkedList<HDLVariable>();
		for (final HDLStatement hdlStatement : stmnts) {
			res.addAll(ScopingExtension.INST.doGetVariables(hdlStatement));
		}
		return res;
	}

}
