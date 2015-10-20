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
package org.pshdl.model.validation;

import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.CONSTANT;
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.IN;
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.INOUT;
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.INTERNAL;
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.OUT;
import static org.pshdl.model.HDLVariableDeclaration.HDLDirection.PARAMETER;
import static org.pshdl.model.validation.builtin.ErrorCode.INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN;
import static org.pshdl.model.validation.builtin.ErrorCode.INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ;
import static org.pshdl.model.validation.builtin.ErrorCode.IN_PORT_NEVER_READ;
import static org.pshdl.model.validation.builtin.ErrorCode.OUT_PORT_NEVER_WRITTEN;
import static org.pshdl.model.validation.builtin.ErrorCode.PARAMETER_OR_CONSTANT_NEVER_READ;
import static org.pshdl.model.validation.builtin.ErrorCode.UNUSED_VARIABLE;
import static org.pshdl.model.validation.builtin.ErrorCode.WRITE_ACCESS_TO_IN_PORT;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLAssignment.HDLAssignmentType;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLFunctionParameter.RWType;
import org.pshdl.model.HDLInterface;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLInterfaceInstantiation;
import org.pshdl.model.HDLInterfaceRef;
import org.pshdl.model.HDLObject.GenericMeta;
import org.pshdl.model.HDLPackage;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableDeclaration.HDLDirection;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations;
import org.pshdl.model.utils.HDLQuery;
import org.pshdl.model.utils.MetaAccess;
import org.pshdl.model.validation.builtin.BuiltInValidator;
import org.pshdl.model.validation.builtin.BuiltInValidator.IntegerMeta;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;

public class RWValidation {
	public static void checkVariableUsage(HDLPackage unit, Set<Problem> problems) {
		annotateReadCount(unit);
		annotateWriteCount(unit);
		final HDLVariable[] vars = unit.getAllObjectsOf(HDLVariable.class, true);
		for (final HDLVariable hdlVariable : vars) {
			if ((hdlVariable.getContainer(HDLInterfaceDeclaration.class) != null) || isGlobal(hdlVariable) || BuiltInValidator.skipExp(hdlVariable)) {
				continue;
			}
			final Integer rc = hdlVariable.getMeta(IntegerMeta.READ_COUNT);
			final int readCount = rc == null ? 0 : rc;
			final Integer wc = hdlVariable.getMeta(IntegerMeta.WRITE_COUNT);
			final int writeCount = wc == null ? 0 : wc;
			final Integer ac = hdlVariable.getMeta(IntegerMeta.ACCESS);
			final int accessCount = ac == null ? 0 : ac;
			if ((hdlVariable.getAnnotation(HDLBuiltInAnnotations.clock) != null) || (hdlVariable.getAnnotation(HDLBuiltInAnnotations.reset) != null)) {
				continue;
			}
			if ((readCount == 0) && (writeCount == 0) && (accessCount == 0)) {
				problems.add(new Problem(UNUSED_VARIABLE, hdlVariable));
			} else {
				final HDLDirection dir = hdlVariable.getDirection();
				if ((readCount == 0) && ((dir == IN) || (dir == INOUT))) {
					problems.add(new Problem(IN_PORT_NEVER_READ, hdlVariable));
				}
				if ((writeCount > 0) && (dir == IN)) {
					problems.add(new Problem(WRITE_ACCESS_TO_IN_PORT, hdlVariable));
				}
				if ((writeCount == 0) && ((dir == OUT) || (dir == INOUT))) {
					problems.add(new Problem(OUT_PORT_NEVER_WRITTEN, hdlVariable));
				}
				if ((writeCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(INTERNAL_SIGNAL_READ_BUT_NEVER_WRITTEN, hdlVariable));
				}
				if ((readCount == 0) && (dir == INTERNAL)) {
					problems.add(new Problem(INTERNAL_SIGNAL_WRITTEN_BUT_NEVER_READ, hdlVariable));
				}
				if ((readCount == 0) && ((dir == PARAMETER) || (dir == CONSTANT))) {
					problems.add(new Problem(PARAMETER_OR_CONSTANT_NEVER_READ, hdlVariable));
				}
			}
		}
		final HDLInterfaceInstantiation[] his = unit.getAllObjectsOf(HDLInterfaceInstantiation.class, true);
		for (final HDLInterfaceInstantiation hii : his) {
			Set<String> readList = hii.getVar().getMeta(Init.read);
			if (readList == null) {
				readList = Sets.newLinkedHashSet();
			}
			Set<String> writeList = hii.getVar().getMeta(Init.written);
			if (writeList == null) {
				writeList = Sets.newLinkedHashSet();
			}
			final Optional<HDLInterface> resolveHIf = hii.resolveHIf();
			if (!resolveHIf.isPresent()) {
				continue;
			}
			final ArrayList<HDLVariableDeclaration> ports = resolveHIf.get().getPorts();
			for (final HDLVariableDeclaration hvd : ports) {
				final ArrayList<HDLVariable> variables = hvd.getVariables();
				for (final HDLVariable hdlVariable : variables) {
					final boolean written = writeList.contains(hdlVariable.getName());
					final boolean read = readList.contains(hdlVariable.getName());
					if (isGlobal(hdlVariable)) {
						continue;
					}
					final HDLDirection dir = hdlVariable.getDirection();
					if (!read && !written && !((dir == PARAMETER) || (dir == CONSTANT))) {
						problems.add(new Problem(ErrorCode.INTERFACE_UNUSED_PORT, hii, hdlVariable, null));
					} else {
						if (!read && ((dir == OUT) || (dir == INOUT))) {
							problems.add(new Problem(ErrorCode.INTERFACE_OUT_PORT_NEVER_READ, hii, hdlVariable, null));
						}
						if (written && (dir == OUT)) {
							problems.add(new Problem(ErrorCode.INTERFACE_OUT_WRITTEN, hii, hdlVariable, null));
						}
						if (!written && ((dir == IN) || (dir == INOUT))) {
							problems.add(new Problem(ErrorCode.INTERFACE_IN_PORT_NEVER_WRITTEN, hii, hdlVariable, null));
						}
					}
				}
			}
		}
	}

	public static boolean isGlobal(HDLVariable hdlVariable) {
		final IHDLObject container = hdlVariable.getContainer();
		if (container == null)
			return false;
		final IHDLObject superContainer = container.getContainer();
		if (superContainer instanceof HDLPackage)
			return true;
		return false;
	}

	public static void annotateReadCount(IHDLObject orig) {
		final HDLReference[] list = orig.getAllObjectsOf(HDLReference.class, true);
		for (final HDLReference ref : list) {
			if (BuiltInValidator.skipExp(ref)) {
				continue;
			}
			final IHDLObject container = ref.getContainer();
			if (container instanceof HDLAssignment) {
				final HDLAssignment ass = (HDLAssignment) container;
				if ((ass.getLeft() == ref) && (ass.getType() == HDLAssignmentType.ASSGN)) {
					// If it is a non-trivial assign, it contains a read
					continue;
				}
			}
			// XXX check for interface and ensure that it is only this instance
			if (ref instanceof HDLUnresolvedFragment) {
				continue;
			}
			final Optional<HDLVariable> var = ((HDLResolvedRef) ref).resolveVar();
			if (var.isPresent()) {
				final HDLVariable rVar = var.get();
				if (isGlobal(rVar)) {
					continue;
				}
				if (ref instanceof HDLInterfaceRef) {
					final HDLInterfaceRef hir = (HDLInterfaceRef) ref;
					final Optional<HDLVariable> hVar = hir.resolveHIf();
					if (hVar.isPresent()) {
						incMeta(hVar.get(), IntegerMeta.ACCESS);
						addStringMeta(rVar.getName(), hVar.get(), Init.read);
					}
				} else {
					incMeta(rVar, IntegerMeta.READ_COUNT);
				}
			}
		}
	}

	/**
	 * The last process that wrote to the variable
	 */
	public static final GenericMeta<HDLBlock> BLOCK_META = new GenericMeta<HDLBlock>("BLOCK_META", true);

	/**
	 * Processes that are colliding in write access
	 */
	public static final GenericMeta<Set<HDLBlock>> BLOCK_META_CLASH = new GenericMeta<Set<HDLBlock>>("BLOCK_META_CLASH", true);

	/**
	 * Indicate whether there is some read or write access to a port
	 */
	public static enum Init implements MetaAccess<Set<String>> {
		/**
		 * Ports where each bit, each dimension is fully assigned
		 */
		full,
		/**
		 * Ports that are at least written once, even if just partially
		 */
		written,
		/**
		 * Ports that are at least read once, even if just partially
		 */
		read;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public static HDLBlock UNIT_BLOCK = new HDLBlock().setProcess(false);

	public static void annotateWriteCount(IHDLObject orig) {
		final HDLAssignment[] list = orig.getAllObjectsOf(HDLAssignment.class, true);
		for (final HDLAssignment ass : list) {
			final HDLReference left = ass.getLeft();
			if (left instanceof HDLVariableRef) {
				final HDLVariableRef ref = (HDLVariableRef) left;
				if (BuiltInValidator.skipExp(ref)) {
					continue;
				}
				final Optional<HDLVariable> var = ref.resolveVar();
				if (!var.isPresent()) {
					continue;
				}
				incrementWrite(var.get(), ref, ass);
			}
		}
		final Collection<HDLVariable> defVal = HDLQuery.select(HDLVariable.class).from(orig).where(HDLVariable.fDefaultValue).isNotEqualTo(null).getAll();
		for (final HDLVariable var : defVal) {
			incMeta(var, IntegerMeta.WRITE_COUNT);
		}
		final HDLFunctionCall[] functions = orig.getAllObjectsOf(HDLFunctionCall.class, true);
		for (final HDLFunctionCall call : functions) {
			final Optional<HDLFunction> functionOpt = call.resolveFunction();
			if (functionOpt.isPresent()) {
				final HDLFunction function = functionOpt.get();
				final ArrayList<HDLFunctionParameter> args = function.getArgs();
				for (int i = 0; i < args.size(); i++) {
					final HDLFunctionParameter hdlFunctionParameter = args.get(i);
					if ((hdlFunctionParameter.getRw() == RWType.READWRITE) || (hdlFunctionParameter.getRw() == RWType.WRITE)) {
						final HDLExpression param = call.getParams().get(i);
						if (param instanceof HDLVariableRef) {
							final HDLVariableRef ref = (HDLVariableRef) param;
							final Optional<HDLVariable> varOpt = ref.resolveVar();
							if (varOpt.isPresent()) {
								final HDLStatement container = call.getContainer(HDLStatement.class);
								if (container == null) {
									incrementWrite(varOpt.get(), ref, call);
								} else {
									incrementWrite(varOpt.get(), ref, container);
								}
							}
						}
					}
				}
			}
		}
	}

	private static void incrementWrite(HDLVariable hdlVariable, HDLVariableRef ref, HDLStatement stmnt) {
		final IHDLObject container = stmnt.getContainer();
		if (ref instanceof HDLInterfaceRef) {
			final HDLInterfaceRef hir = (HDLInterfaceRef) ref;
			final Optional<HDLVariable> hVar = hir.resolveHIf();
			if (hVar.isPresent()) {
				incMeta(hVar.get(), IntegerMeta.ACCESS);
				addStringMeta(hdlVariable.getName(), hVar.get(), Init.written);
				if ((container != null) && (container.getClassType() == HDLClass.HDLUnit) && (ref.getArray().size() == 0) && (ref.getBits().size() == 0)) {
					addStringMeta(hir.getVarRefName().getLastSegment(), hVar.get(), Init.full);
				}
			}
		} else {
			if ((container != null) && (container.getClassType() == HDLClass.HDLUnit) && (ref.getArray().size() == 0) && (ref.getBits().size() == 0)) {
				hdlVariable.addMeta(Init.full, Collections.singleton(hdlVariable.getName()));
			}
			incMeta(hdlVariable, IntegerMeta.WRITE_COUNT);
		}
		HDLBlock block = getProcess(stmnt);
		if (block == null) {
			block = UNIT_BLOCK;
		}
		if ((hdlVariable.getMeta(BLOCK_META) != null) && ((hdlVariable.getMeta(BLOCK_META) != block) && block.getProcess())) {
			Set<HDLBlock> meta = hdlVariable.getMeta(BLOCK_META_CLASH);
			if (meta == null) {
				meta = Sets.newLinkedHashSet();
			}
			meta.add(block);
			hdlVariable.addMeta(BLOCK_META_CLASH, meta);
		}
		hdlVariable.addMeta(BLOCK_META, block);

	}

	private static HDLBlock getProcess(HDLStatement stmnt) {
		final HDLBlock container = stmnt.getContainer(HDLBlock.class);
		if ((container != null) && (container.getProcess() != null) && (container.getProcess() == false))
			return getProcess(container);
		return container;
	}

	private static void addStringMeta(String value, HDLVariable hVar, Init init) {
		Set<String> written = hVar.getMeta(init);
		if (written == null) {
			written = Sets.newLinkedHashSet();
		}
		written.add(value);
		hVar.addMeta(init, written);
	}

	private static void incMeta(HDLVariable var, MetaAccess<Integer> metaAccess) {
		Integer meta = var.getMeta(metaAccess);
		if (meta == null) {
			meta = 1;
		} else {
			meta++;
		}
		var.addMeta(metaAccess, meta);
	}
}
