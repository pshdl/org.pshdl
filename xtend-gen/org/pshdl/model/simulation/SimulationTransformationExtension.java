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
package org.pshdl.model.simulation;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.pshdl.interpreter.FunctionInformation;
import org.pshdl.interpreter.InternalInformation;
import org.pshdl.interpreter.ParameterInformation;
import org.pshdl.interpreter.VariableInformation;
import org.pshdl.interpreter.utils.Instruction;
import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.HDLArithOp;
import org.pshdl.model.HDLArrayInit;
import org.pshdl.model.HDLAssignment;
import org.pshdl.model.HDLBitOp;
import org.pshdl.model.HDLBlock;
import org.pshdl.model.HDLClass;
import org.pshdl.model.HDLConcat;
import org.pshdl.model.HDLEnum;
import org.pshdl.model.HDLEnumDeclaration;
import org.pshdl.model.HDLEnumRef;
import org.pshdl.model.HDLEqualityOp;
import org.pshdl.model.HDLExpression;
import org.pshdl.model.HDLFunction;
import org.pshdl.model.HDLFunctionCall;
import org.pshdl.model.HDLFunctionParameter;
import org.pshdl.model.HDLIfStatement;
import org.pshdl.model.HDLInterfaceDeclaration;
import org.pshdl.model.HDLLiteral;
import org.pshdl.model.HDLManip;
import org.pshdl.model.HDLPrimitive;
import org.pshdl.model.HDLRange;
import org.pshdl.model.HDLReference;
import org.pshdl.model.HDLRegisterConfig;
import org.pshdl.model.HDLResolvedRef;
import org.pshdl.model.HDLShiftOp;
import org.pshdl.model.HDLStatement;
import org.pshdl.model.HDLSwitchCaseStatement;
import org.pshdl.model.HDLSwitchStatement;
import org.pshdl.model.HDLType;
import org.pshdl.model.HDLUnit;
import org.pshdl.model.HDLUnresolvedFragment;
import org.pshdl.model.HDLVariable;
import org.pshdl.model.HDLVariableDeclaration;
import org.pshdl.model.HDLVariableRef;
import org.pshdl.model.IHDLObject;
import org.pshdl.model.evaluation.ConstantEvaluate;
import org.pshdl.model.evaluation.HDLEvaluationContext;
import org.pshdl.model.extensions.FullNameExtension;
import org.pshdl.model.extensions.TypeExtension;
import org.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider;
import org.pshdl.model.types.builtIn.HDLPrimitives;
import org.pshdl.model.utils.HDLCodeGenerationException;
import org.pshdl.model.utils.HDLQualifiedName;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@SuppressWarnings("all")
public class SimulationTransformationExtension {
	private static SimulationTransformationExtension INST = new SimulationTransformationExtension();

	public final static char ANNO_VALUE_SEP = '|';

	private final static String PSEX_STAGE = "PSEX";

	public static FluidFrame simulationModelOf(final HDLUnit obj, final HDLEvaluationContext context) {
		final HDLEvaluationContext _withEnumAndBool = context.withEnumAndBool(true, true);
		return SimulationTransformationExtension.INST.toSimulationModelUnit(obj, _withEnumAndBool);
	}

	protected FluidFrame _toSimulationModel(final IHDLObject obj, final HDLEvaluationContext context, final String process) {
		final HDLClass _classType = obj.getClassType();
		final String _plus = ("Not implemented! " + _classType);
		final String _plus_1 = (_plus + " ");
		final String _plus_2 = (_plus_1 + obj);
		throw new RuntimeException(_plus_2);
	}

	protected FluidFrame _toSimulationModel(final HDLExpression obj, final HDLEvaluationContext context, final String process) {
		final HDLClass _classType = obj.getClassType();
		final String _plus = ("Not implemented! " + _classType);
		final String _plus_1 = (_plus + " ");
		final String _plus_2 = (_plus_1 + obj);
		throw new RuntimeException(_plus_2);
	}

	protected FluidFrame _toSimulationModel(final HDLStatement obj, final HDLEvaluationContext context, final String process) {
		final HDLClass _classType = obj.getClassType();
		final String _plus = ("Not implemented! " + _classType);
		final String _plus_1 = (_plus + " ");
		final String _plus_2 = (_plus_1 + obj);
		throw new RuntimeException(_plus_2);
	}

	protected FluidFrame _toSimulationModel(final HDLBlock obj, final HDLEvaluationContext context, final String process) {
		String newProcess = process;
		final Boolean _process = obj.getProcess();
		if ((_process).booleanValue()) {
			final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
			final String _lastSegment = _fullNameOf.getLastSegment();
			final String _replaceAll = _lastSegment.replaceAll("@", "");
			newProcess = _replaceAll;
		}
		final FluidFrame frame = new FluidFrame(obj, null, false, newProcess);
		final ArrayList<HDLStatement> _statements = obj.getStatements();
		for (final HDLStatement stmnt : _statements) {
			final FluidFrame _simulationModel = this.toSimulationModel(stmnt, context, frame.simProcess);
			frame.addReferencedFrame(_simulationModel);
		}
		return frame;
	}

	protected FluidFrame _toSimulationModelPred(final HDLBlock obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context, final String process) {
		String newProcess = process;
		final Boolean _process = obj.getProcess();
		if ((_process).booleanValue()) {
			final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
			final String _lastSegment = _fullNameOf.getLastSegment();
			final String _replaceAll = _lastSegment.replaceAll("@", "");
			newProcess = _replaceAll;
		}
		final FluidFrame frame = new FluidFrame(obj, null, false, newProcess);
		final ArrayList<HDLStatement> _statements = obj.getStatements();
		for (final HDLStatement stmnt : _statements) {
			final FluidFrame _simulationModelPred = this.toSimulationModelPred(stmnt, predicate, context, frame.simProcess);
			frame.addReferencedFrame(_simulationModelPred);
		}
		return frame;
	}

	protected FluidFrame _toSimulationModelPred(final HDLStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context,
			final String process) {
		final FluidFrame res = this.toSimulationModel(obj, context, process);
		final boolean _hasInstructions = res.hasInstructions();
		if (_hasInstructions) {
			res.instructions.addFirst(predicate);
		}
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLInterfaceDeclaration obj, final HDLEvaluationContext context, final String process) {
		return new FluidFrame(obj, null);
	}

	protected FluidFrame _toSimulationModel(final HDLFunctionCall obj, final HDLEvaluationContext context, final String process) {
		final Optional<BigInteger> constVal = ConstantEvaluate.valueOf(obj, context);
		final boolean _isPresent = constVal.isPresent();
		if (_isPresent) {
			final BigInteger _get = constVal.get();
			final HDLLiteral _get_1 = HDLLiteral.get(_get);
			return this.toSimulationModel(_get_1, context, process);
		}
		final FluidFrame res = new FluidFrame(obj, process);
		final ArrayList<HDLExpression> _params = obj.getParams();
		for (final HDLExpression e : _params) {
			final FluidFrame _simulationModel = this.toSimulationModel(e, context, process);
			res.append(_simulationModel);
		}
		final HDLFunction func = obj.resolveFunctionForced(SimulationTransformationExtension.PSEX_STAGE);
		final HDLFunctionParameter _returnType = func.getReturnType();
		final ParameterInformation returnType = this.toParameterInformation(_returnType, context);
		final ArrayList<HDLFunctionParameter> _args = func.getArgs();
		List<ParameterInformation> _map = null;
		if (_args != null) {
			final Function1<HDLFunctionParameter, ParameterInformation> _function = new Function1<HDLFunctionParameter, ParameterInformation>() {
				@Override
				public ParameterInformation apply(final HDLFunctionParameter it) {
					return SimulationTransformationExtension.this.toParameterInformation(it, context);
				}
			};
			_map = ListExtensions.<HDLFunctionParameter, ParameterInformation> map(_args, _function);
		}
		final List<ParameterInformation> args = _map;
		final HDLStatement _container = obj.<HDLStatement> getContainer(HDLStatement.class);
		final boolean isStatement = (_container == null);
		final String _name = func.getName();
		final ArrayList<HDLAnnotation> _annotations = func.getAnnotations();
		final String[] _annoString = this.toAnnoString(_annotations);
		final FunctionInformation fi = new FunctionInformation(_name, isStatement, returnType, ((ParameterInformation[]) Conversions.unwrapArray(args, ParameterInformation.class)),
				_annoString);
		final String sig = fi.signature();
		res.funcs.put(sig, fi);
		String _xifexpression = null;
		if ((args == null)) {
			_xifexpression = "0";
		} else {
			final int _length = ((Object[]) Conversions.unwrapArray(args, Object.class)).length;
			_xifexpression = Integer.valueOf(_length).toString();
		}
		final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.invokeFunction, sig, _xifexpression);
		res.add(_argumentedInstruction);
		return res;
	}

	public ParameterInformation toParameterInformation(final HDLFunctionParameter parameter, final HDLEvaluationContext context) {
		if ((parameter == null))
			return null;
		final HDLFunctionParameter.RWType _rw = parameter.getRw();
		final String _string = _rw.toString();
		final ParameterInformation.RWType rwType = ParameterInformation.RWType.getOp(_string);
		final HDLFunctionParameter.Type _type = parameter.getType();
		final String _string_1 = _type.toString();
		final ParameterInformation.Type type = ParameterInformation.Type.getOp(_string_1);
		final HDLQualifiedName _enumSpecRefName = parameter.getEnumSpecRefName();
		String _string_2 = null;
		if (_enumSpecRefName != null) {
			_string_2 = _enumSpecRefName.toString();
		}
		final String enumSpecRefName = _string_2;
		final HDLQualifiedName _ifSpecRefName = parameter.getIfSpecRefName();
		String _string_3 = null;
		if (_ifSpecRefName != null) {
			_string_3 = _ifSpecRefName.toString();
		}
		final String ifSpec = _string_3;
		final ArrayList<HDLFunctionParameter> _funcSpec = parameter.getFuncSpec();
		List<ParameterInformation> _map = null;
		if (_funcSpec != null) {
			final Function1<HDLFunctionParameter, ParameterInformation> _function = new Function1<HDLFunctionParameter, ParameterInformation>() {
				@Override
				public ParameterInformation apply(final HDLFunctionParameter it) {
					return SimulationTransformationExtension.this.toParameterInformation(it, context);
				}
			};
			_map = ListExtensions.<HDLFunctionParameter, ParameterInformation> map(_funcSpec, _function);
		}
		final List<ParameterInformation> funcSpec = _map;
		final HDLFunctionParameter _funcReturnSpec = parameter.getFuncReturnSpec();
		ParameterInformation _parameterInformation = null;
		if (_funcReturnSpec != null) {
			_parameterInformation = this.toParameterInformation(_funcReturnSpec, context);
		}
		final ParameterInformation funcReturnSpec = _parameterInformation;
		final HDLVariable _name = parameter.getName();
		final String name = _name.getName();
		final HDLExpression _width = parameter.getWidth();
		final Optional<BigInteger> widthOpt = ConstantEvaluate.valueOf(_width, context);
		int _xifexpression = 0;
		final boolean _isPresent = widthOpt.isPresent();
		if (_isPresent) {
			final BigInteger _get = widthOpt.get();
			_xifexpression = _get.intValue();
		} else {
			_xifexpression = (-1);
		}
		final int width = _xifexpression;
		final ArrayList<HDLExpression> _dim = parameter.getDim();
		List<Integer> _map_1 = null;
		if (_dim != null) {
			final Function1<HDLExpression, Integer> _function_1 = new Function1<HDLExpression, Integer>() {
				@Override
				public Integer apply(final HDLExpression it) {
					final BigInteger _valueOfForced = ConstantEvaluate.valueOfForced(it, context, SimulationTransformationExtension.PSEX_STAGE);
					return Integer.valueOf(_valueOfForced.intValue());
				}
			};
			_map_1 = ListExtensions.<HDLExpression, Integer> map(_dim, _function_1);
		}
		final List<Integer> dims = _map_1;
		final boolean const_ = (parameter.getConstant()).booleanValue();
		return new ParameterInformation(rwType, type, enumSpecRefName, ifSpec, ((ParameterInformation[]) Conversions.unwrapArray(funcSpec, ParameterInformation.class)),
				funcReturnSpec, name, width, ((int[]) Conversions.unwrapArray(dims, int.class)), Boolean.valueOf(const_));
	}

	protected FluidFrame _toSimulationModel(final HDLEnumDeclaration obj, final HDLEvaluationContext context, final String process) {
		return new FluidFrame(obj, null);
	}

	protected FluidFrame _toSimulationModelInit(final HDLExpression obj, final HDLEvaluationContext context, final String varName, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final FluidFrame _simulationModel = this.toSimulationModel(obj, context, process);
		res.append(_simulationModel);
		final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
		res.add(_argumentedInstruction);
		return res;
	}

	protected FluidFrame _toSimulationModelInit(final HDLArrayInit obj, final HDLEvaluationContext context, final String varName, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		int pos = 0;
		final ArrayList<HDLExpression> _exp = obj.getExp();
		for (final HDLExpression exp : _exp) {
			{
				final HDLLiteral _get = HDLLiteral.get(pos);
				final FluidFrame _simulationModel = this.toSimulationModel(_get, context, process);
				res.append(_simulationModel);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.pushAddIndex, varName, "0");
				res.add(_argumentedInstruction);
				final String _addDynamicIdx = this.addDynamicIdx(varName);
				final FluidFrame _simulationModelInit = this.toSimulationModelInit(exp, context, _addDynamicIdx, process);
				res.append(_simulationModelInit);
				pos = (pos + 1);
			}
		}
		return res;
	}

	public String addDynamicIdx(final String string) {
		final boolean _endsWith = string.endsWith(InternalInformation.REG_POSTFIX);
		if (_endsWith)
			return string.replace(InternalInformation.REG_POSTFIX, ("[-1]" + InternalInformation.REG_POSTFIX));
		return (string + "[-1]");
	}

	protected FluidFrame _toSimulationModel(final HDLVariableDeclaration obj, final HDLEvaluationContext context, final String process) {
		final HDLType type = obj.resolveTypeForced(SimulationTransformationExtension.PSEX_STAGE);
		Integer _xifexpression = null;
		final HDLClass _classType = type.getClassType();
		final boolean _tripleEquals = (_classType == HDLClass.HDLPrimitive);
		if (_tripleEquals) {
			_xifexpression = HDLPrimitives.getWidth(type, context);
		} else {
			_xifexpression = Integer.valueOf(32);
		}
		final Integer width = _xifexpression;
		final HDLRegisterConfig _register = obj.getRegister();
		final boolean isReg = (_register != null);
		final String _name = HDLSimulator.TB_VAR.getName();
		final HDLAnnotation simAnno = obj.getAnnotation(_name);
		String newProcess = process;
		if (((newProcess == null) && (simAnno != null))) {
			newProcess = "ONCE";
		}
		final FluidFrame res = new FluidFrame(obj, null, false, newProcess);
		final HDLAnnotation _annotation = obj.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.memory);
		final boolean _tripleNotEquals = (_annotation != null);
		if (_tripleNotEquals)
			return res;
		VariableInformation.Direction dir = null;
		final HDLVariableDeclaration.HDLDirection _direction = obj.getDirection();
		if (_direction != null) {
			switch (_direction) {
			case IN:
				dir = VariableInformation.Direction.IN;
				break;
			case OUT:
				dir = VariableInformation.Direction.OUT;
				break;
			case INOUT:
				dir = VariableInformation.Direction.INOUT;
				break;
			default:
				dir = VariableInformation.Direction.INTERNAL;
				break;
			}
		} else {
			dir = VariableInformation.Direction.INTERNAL;
		}
		final ArrayList<HDLVariable> _variables = obj.getVariables();
		for (final HDLVariable hVar : _variables) {
			{
				final HDLAnnotation _annotation_1 = hVar.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.clock);
				final boolean clock = (_annotation_1 != null);
				final HDLAnnotation _annotation_2 = hVar.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.reset);
				final boolean reset = (_annotation_2 != null);
				final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
				final String varName = _fullNameOf.toString();
				final LinkedList<Integer> dims = new LinkedList<Integer>();
				final ArrayList<HDLExpression> _dimensions = hVar.getDimensions();
				for (final HDLExpression dim : _dimensions) {
					final BigInteger _valueOfForced = ConstantEvaluate.valueOfForced(dim, context, SimulationTransformationExtension.PSEX_STAGE);
					final int _intValue = _valueOfForced.intValue();
					dims.add(Integer.valueOf(_intValue));
				}
				final VariableInformation.Type vType = this.asType(type);
				final ArrayList<HDLAnnotation> _annotations = hVar.getAnnotations();
				final ArrayList<HDLAnnotation> _annotations_1 = obj.getAnnotations();
				final Iterable<HDLAnnotation> _plus = Iterables.<HDLAnnotation> concat(_annotations, _annotations_1);
				final ArrayList<HDLAnnotation> allAnnos = Lists.<HDLAnnotation> newArrayList(_plus);
				final HDLClass _classType_1 = type.getClassType();
				final boolean _tripleEquals_1 = (_classType_1 == HDLClass.HDLEnum);
				if (_tripleEquals_1) {
					final HDLEnum hEnum = ((HDLEnum) type);
					final HDLAnnotation _hDLAnnotation = new HDLAnnotation();
					final HDLAnnotation _setName = _hDLAnnotation.setName("@enumNames");
					final ArrayList<HDLVariable> _enums = hEnum.getEnums();
					final Function1<HDLVariable, String> _function = new Function1<HDLVariable, String>() {
						@Override
						public String apply(final HDLVariable it) {
							return it.getName();
						}
					};
					final List<String> _map = ListExtensions.<HDLVariable, String> map(_enums, _function);
					final String _join = IterableExtensions.join(_map, ";");
					final HDLAnnotation enumAnno = _setName.setValue(_join);
					allAnnos.add(enumAnno);
				}
				final String[] _annoString = this.toAnnoString(allAnnos);
				final VariableInformation _variableInformation = new VariableInformation(dir, varName, (width).intValue(), vType, isReg, clock, reset, _annoString,
						((int[]) Conversions.unwrapArray(dims, int.class)));
				res.addVar(_variableInformation);
			}
		}
		if (isReg) {
			final HDLRegisterConfig _register_1 = obj.getRegister();
			final HDLRegisterConfig config = _register_1.normalize();
			final HDLExpression rst = config.getRst();
			final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(rst);
			final String rstName = _fullNameOf.toString();
			final HDLRegisterConfig.HDLRegResetActiveType _resetType = config.getResetType();
			final boolean _tripleEquals_1 = (_resetType == HDLRegisterConfig.HDLRegResetActiveType.HIGH);
			if (_tripleEquals_1) {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, rstName);
				res.add(_argumentedInstruction);
			} else {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, rstName);
				res.add(_argumentedInstruction_1);
			}
			final HDLRegisterConfig.HDLRegSyncType _syncType = config.getSyncType();
			final boolean _tripleEquals_2 = (_syncType == HDLRegisterConfig.HDLRegSyncType.SYNC);
			if (_tripleEquals_2) {
				final HDLExpression clk = config.getClk();
				final HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(clk);
				final String name = _fullNameOf_1.toString();
				final HDLRegisterConfig.HDLRegClockType _clockType = config.getClockType();
				final boolean _tripleEquals_3 = (_clockType == HDLRegisterConfig.HDLRegClockType.RISING);
				if (_tripleEquals_3) {
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.isRisingEdge, name);
					res.add(_argumentedInstruction_2);
				} else {
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.isFallingEdge, name);
					res.add(_argumentedInstruction_3);
				}
			}
			this.createInit(config, obj, context, res, true, process);
			final HDLRegisterConfig.HDLRegSyncType _syncType_1 = config.getSyncType();
			final boolean _tripleEquals_4 = (_syncType_1 == HDLRegisterConfig.HDLRegSyncType.ASYNC);
			if (_tripleEquals_4) {
				this.createInit(config, obj, context, res, false, process);
			}
		}
		return res;
	}

	public VariableInformation.Type asType(final HDLType type) {
		VariableInformation.Type vType = VariableInformation.Type.BIT;
		final HDLClass _classType = type.getClassType();
		final boolean _tripleEquals = (_classType == HDLClass.HDLPrimitive);
		if (_tripleEquals) {
			final HDLPrimitive.HDLPrimitiveType _type = ((HDLPrimitive) type).getType();
			if (_type != null) {
				switch (_type) {
				case ANY_INT:
					vType = VariableInformation.Type.INT;
					break;
				case INT:
					vType = VariableInformation.Type.INT;
					break;
				case INTEGER:
					vType = VariableInformation.Type.INT;
					break;
				case ANY_UINT:
					vType = VariableInformation.Type.UINT;
					break;
				case UINT:
					vType = VariableInformation.Type.UINT;
					break;
				case NATURAL:
					vType = VariableInformation.Type.UINT;
					break;
				case ANY_BIT:
					vType = VariableInformation.Type.BIT;
					break;
				case BIT:
					vType = VariableInformation.Type.BIT;
					break;
				case BITVECTOR:
					vType = VariableInformation.Type.BIT;
					break;
				case BOOL:
					vType = VariableInformation.Type.BOOL;
					break;
				case STRING:
					vType = VariableInformation.Type.STRING;
					break;
				default:
					break;
				}
			}
		}
		final HDLClass _classType_1 = type.getClassType();
		final boolean _tripleEquals_1 = (_classType_1 == HDLClass.HDLEnum);
		if (_tripleEquals_1) {
			vType = VariableInformation.Type.ENUM;
		}
		return vType;
	}

	public String[] toAnnoString(final Iterable<HDLAnnotation> annotations) {
		final Function1<HDLAnnotation, String> _function = new Function1<HDLAnnotation, String>() {
			@Override
			public String apply(final HDLAnnotation it) {
				String _xifexpression = null;
				final String _value = it.getValue();
				final boolean _tripleEquals = (_value == null);
				if (_tripleEquals) {
					final String _name = it.getName();
					_xifexpression = _name.substring(1);
				} else {
					final String _name_1 = it.getName();
					final String _substring = _name_1.substring(1);
					final String _plus = (_substring + Character.valueOf(SimulationTransformationExtension.ANNO_VALUE_SEP));
					final String _value_1 = it.getValue();
					_xifexpression = (_plus + _value_1);
				}
				return _xifexpression;
			}
		};
		return ((String[]) Conversions.unwrapArray(IterableExtensions.<HDLAnnotation, String> map(annotations, _function), String.class));
	}

	public void createInit(final HDLRegisterConfig config, final HDLVariableDeclaration obj, final HDLEvaluationContext context, final FluidFrame res, final boolean toReg,
			final String process) {
		final HDLAnnotation _annotation = obj.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.memory);
		final boolean _tripleNotEquals = (_annotation != null);
		if (_tripleNotEquals)
			return;
		final HDLExpression _resetValue = config.getResetValue();
		if ((_resetValue instanceof HDLArrayInit)) {
			final ArrayList<HDLVariable> _variables = obj.getVariables();
			for (final HDLVariable hVar : _variables) {
				{
					res.add(Instruction.const0);
					final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
					String varName = _fullNameOf.toString();
					if (toReg) {
						varName = (varName + InternalInformation.REG_POSTFIX);
					}
					final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
					res.add(_argumentedInstruction);
					final HDLExpression _resetValue_1 = config.getResetValue();
					final HDLArrayInit arr = ((HDLArrayInit) _resetValue_1);
					final FluidFrame _simulationModelInit = this.toSimulationModelInit(arr, context, varName, process);
					res.append(_simulationModelInit);
				}
			}
		} else {
			final HDLExpression _resetValue_1 = config.getResetValue();
			final FluidFrame resetFrame = this.toSimulationModel(_resetValue_1, context, process);
			final ArrayList<HDLVariable> _variables_1 = obj.getVariables();
			for (final HDLVariable hVar_1 : _variables_1) {
				{
					final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar_1);
					String varName = _fullNameOf.toString();
					if (toReg) {
						varName = (varName + InternalInformation.REG_POSTFIX);
					}
					res.append(resetFrame);
					final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.writeInternal, varName);
					res.add(_argumentedInstruction);
				}
			}
		}
	}

	protected FluidFrame _toSimulationModel(final HDLSwitchStatement obj, final HDLEvaluationContext context, final String process) {
		return this.toSimulationModelPred(obj, null, context, process);
	}

	protected FluidFrame _toSimulationModelPred(final HDLSwitchStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context,
			final String process) {
		final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
		final String name = _fullNameOf.toString();
		final HDLExpression _caseExp = obj.getCaseExp();
		final FluidFrame res = this.toSimulationModel(_caseExp, context, process);
		res.setName(name);
		final HDLExpression _caseExp_1 = obj.getCaseExp();
		final HDLType type = TypeExtension.typeOfForced(_caseExp_1, SimulationTransformationExtension.PSEX_STAGE);
		Integer _xifexpression = null;
		final HDLClass _classType = type.getClassType();
		final boolean _tripleEquals = (_classType == HDLClass.HDLPrimitive);
		if (_tripleEquals) {
			_xifexpression = HDLPrimitives.getWidth(type, context);
		} else {
			_xifexpression = Integer.valueOf(32);
		}
		final Integer width = _xifexpression;
		final VariableInformation _variableInformation = new VariableInformation(VariableInformation.Direction.INTERNAL, name, (width).intValue(), VariableInformation.Type.BIT,
				false, false, false, null);
		res.addVar(_variableInformation);
		final ArrayList<HDLSwitchCaseStatement> _cases = obj.getCases();
		for (final HDLSwitchCaseStatement c : _cases) {
			{
				final HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(c);
				final String cName = _fullNameOf_1.toString();
				final FluidFrame caseFrame = new FluidFrame(obj, (InternalInformation.PRED_PREFIX + cName), false, process);
				if ((predicate != null)) {
					caseFrame.add(predicate);
				}
				caseFrame.createPredVar();
				final HDLExpression _label = c.getLabel();
				final boolean _tripleEquals_1 = (_label == null);
				if (_tripleEquals_1) {
					final ArrayList<HDLSwitchCaseStatement> _cases_1 = obj.getCases();
					for (final HDLSwitchCaseStatement cSub : _cases_1) {
						final boolean _notEquals = (!Objects.equal(cSub, c));
						if (_notEquals) {
							final HDLQualifiedName _fullNameOf_2 = FullNameExtension.fullNameOf(cSub);
							final String _string = _fullNameOf_2.toString();
							final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, _string);
							caseFrame.add(_argumentedInstruction);
						}
					}
					caseFrame.addConstant("true", BigInteger.ONE, VariableInformation.Type.BOOL);
				} else {
					final HDLExpression _label_1 = c.getLabel();
					final Optional<BigInteger> const_ = ConstantEvaluate.valueOf(_label_1, context);
					int l = 0;
					final boolean _isPresent = const_.isPresent();
					if (_isPresent) {
						final BigInteger _get = const_.get();
						final int _intValue = _get.intValue();
						l = _intValue;
					} else {
						final HDLExpression _label_2 = c.getLabel();
						final HDLClass _classType_1 = _label_2.getClassType();
						final boolean _tripleEquals_2 = (_classType_1 == HDLClass.HDLEnumRef);
						if (_tripleEquals_2) {
							final HDLExpression _label_3 = c.getLabel();
							final HDLEnumRef ref = ((HDLEnumRef) _label_3);
							final int _asInt = this.asInt(ref);
							l = _asInt;
						} else
							throw new IllegalArgumentException("Unsupported label type");
					}
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, name);
					caseFrame.add(_argumentedInstruction_1);
					final BigInteger _valueOf = BigInteger.valueOf(l);
					caseFrame.addConstant("label", _valueOf, VariableInformation.Type.UINT);
					caseFrame.add(Instruction.eq);
				}
				final ArrayList<HDLStatement> _dos = c.getDos();
				for (final HDLStatement d : _dos) {
					{
						final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, cName);
						final FluidFrame subDo = this.toSimulationModelPred(d, _argumentedInstruction_2, context, process);
						caseFrame.addReferencedFrame(subDo);
					}
				}
				res.addReferencedFrame(caseFrame);
			}
		}
		return res;
	}

	public int asInt(final HDLEnumRef ref) {
		final HDLEnum hEnum = ref.resolveHEnumForced(SimulationTransformationExtension.PSEX_STAGE);
		final HDLVariable hVar = ref.resolveVarForced(SimulationTransformationExtension.PSEX_STAGE);
		final ArrayList<HDLVariable> _enums = hEnum.getEnums();
		return _enums.indexOf(hVar);
	}

	protected FluidFrame _toSimulationModel(final HDLIfStatement obj, final HDLEvaluationContext context, final String process) {
		final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(obj);
		final String name = _fullNameOf.toString();
		final HDLExpression _ifExp = obj.getIfExp();
		final FluidFrame ifModel = this.toSimulationModel(_ifExp, context, process);
		ifModel.setName((InternalInformation.PRED_PREFIX + name));
		ifModel.createPredVar();
		final ArrayList<HDLStatement> _thenDo = obj.getThenDo();
		for (final HDLStatement s : _thenDo) {
			{
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, name);
				final FluidFrame thenDo = this.toSimulationModelPred(s, _argumentedInstruction, context, process);
				ifModel.addReferencedFrame(thenDo);
			}
		}
		final ArrayList<HDLStatement> _elseDo = obj.getElseDo();
		for (final HDLStatement s_1 : _elseDo) {
			{
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, name);
				final FluidFrame elseDo = this.toSimulationModelPred(s_1, _argumentedInstruction, context, process);
				ifModel.addReferencedFrame(elseDo);
			}
		}
		return ifModel;
	}

	protected FluidFrame _toSimulationModel(final HDLAssignment obj, final HDLEvaluationContext context, final String process) {
		final HDLAssignment.HDLAssignmentType _type = obj.getType();
		final boolean _tripleNotEquals = (_type != HDLAssignment.HDLAssignmentType.ASSGN);
		if (_tripleNotEquals)
			throw new IllegalArgumentException("Did not expect a combined assignment");
		HDLReference left = obj.getLeft();
		if ((left instanceof HDLVariableRef)) {
			final HDLVariableRef _redirectRef = this.redirectRef(((HDLVariableRef) left));
			left = _redirectRef;
		}
		final HDLVariable hVar = this.resolveVar(left);
		final HDLVariableDeclaration.HDLDirection _direction = hVar.getDirection();
		final boolean constant = (_direction == HDLVariableDeclaration.HDLDirection.CONSTANT);
		HDLRegisterConfig config = hVar.getRegisterConfig();
		String assignmentVarName = SimulationTransformationExtension.getVarName(((HDLVariableRef) left), true, context);
		if ((config != null)) {
			assignmentVarName = (assignmentVarName + InternalInformation.REG_POSTFIX);
		}
		final FluidFrame res = new FluidFrame(obj, assignmentVarName, constant, process);
		if ((config != null)) {
			final HDLRegisterConfig _normalize = config.normalize();
			config = _normalize;
			final HDLExpression clk = config.getClk();
			final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(clk);
			final String name = _fullNameOf.toString();
			final HDLRegisterConfig.HDLRegClockType _clockType = config.getClockType();
			final boolean _equals = Objects.equal(_clockType, HDLRegisterConfig.HDLRegClockType.RISING);
			if (_equals) {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.isRisingEdge, name);
				res.add(_argumentedInstruction);
			} else {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.isFallingEdge, name);
				res.add(_argumentedInstruction_1);
			}
			final HDLExpression rst = config.getRst();
			final HDLQualifiedName _fullNameOf_1 = FullNameExtension.fullNameOf(rst);
			final String rstName = _fullNameOf_1.toString();
			final HDLRegisterConfig.HDLRegResetActiveType _resetType = config.getResetType();
			final boolean _tripleEquals = (_resetType == HDLRegisterConfig.HDLRegResetActiveType.HIGH);
			if (_tripleEquals) {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.negPredicate, rstName);
				res.add(_argumentedInstruction_2);
			} else {
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.posPredicate, rstName);
				res.add(_argumentedInstruction_3);
			}
		}
		final HDLExpression _right = obj.getRight();
		final FluidFrame _simulationModel = this.toSimulationModel(_right, context, process);
		res.append(_simulationModel);
		if ((left instanceof HDLVariableRef)) {
			final ArrayList<HDLExpression> _array = ((HDLVariableRef) left).getArray();
			this.createPushIndex(_array, context, res, process, assignmentVarName);
			final ArrayList<HDLRange> _bits = ((HDLVariableRef) left).getBits();
			this.createPushIndexBits(_bits, context, res, process, assignmentVarName);
		}
		return res;
	}

	public void createPushIndexBits(final ArrayList<HDLRange> array, final HDLEvaluationContext context, final FluidFrame res, final String process,
			final String assignmentVarName) {
		boolean fixedBit = true;
		for (final HDLRange idx : array) {
			final HDLExpression _to = idx.getTo();
			final Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(_to, context);
			final boolean _isPresent = _valueOf.isPresent();
			final boolean _not = (!_isPresent);
			if (_not) {
				fixedBit = false;
			}
		}
		if ((!fixedBit)) {
			for (final HDLRange idx_1 : array) {
				{
					final HDLExpression _to_1 = idx_1.getTo();
					final FluidFrame _simulationModel = this.toSimulationModel(_to_1, context, process);
					res.append(_simulationModel);
					final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.pushAddIndex, assignmentVarName, "1");
					res.add(_argumentedInstruction);
				}
			}
		}
	}

	public void createPushIndex(final ArrayList<HDLExpression> array, final HDLEvaluationContext context, final FluidFrame res, final String process,
			final String assignmentVarName) {
		boolean fixedArray = true;
		for (final HDLExpression idx : array) {
			final Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(idx, context);
			final boolean _isPresent = _valueOf.isPresent();
			final boolean _not = (!_isPresent);
			if (_not) {
				fixedArray = false;
			}
		}
		if ((!fixedArray)) {
			for (final HDLExpression idx_1 : array) {
				{
					final FluidFrame _simulationModel = this.toSimulationModel(idx_1, context, process);
					res.append(_simulationModel);
					final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.pushAddIndex, assignmentVarName, "0");
					res.add(_argumentedInstruction);
				}
			}
		}
	}

	public HDLVariable resolveVar(final HDLReference reference) {
		if ((reference instanceof HDLUnresolvedFragment))
			throw new RuntimeException("Can not use unresolved fragments");
		if ((reference instanceof HDLVariableRef)) {
			final HDLVariableRef newRef = this.redirectRef(((HDLVariableRef) reference));
			return newRef.resolveVarForced("PSEX");
		}
		return ((HDLResolvedRef) reference).resolveVarForced("PSEX");
	}

	public static String getVarName(final HDLVariableRef hVar, final boolean withBits, final HDLEvaluationContext context) {
		final StringBuilder sb = new StringBuilder();
		final HDLVariable _resolveVarForced = hVar.resolveVarForced("PSEX");
		final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(_resolveVarForced);
		sb.append(_fullNameOf);
		final ArrayList<HDLExpression> _array = hVar.getArray();
		for (final HDLExpression exp : _array) {
			{
				final Optional<BigInteger> s = ConstantEvaluate.valueOf(exp, context);
				final boolean _isPresent = s.isPresent();
				if (_isPresent) {
					final StringBuilder _append = sb.append("[");
					final BigInteger _get = s.get();
					final StringBuilder _append_1 = _append.append(_get);
					_append_1.append("]");
				} else {
					sb.append("[-1]");
				}
			}
		}
		if (withBits) {
			final ArrayList<HDLRange> _bits = hVar.getBits();
			for (final HDLRange exp_1 : _bits) {
				{
					final HDLExpression _to = exp_1.getTo();
					final Optional<BigInteger> s = ConstantEvaluate.valueOf(_to, context);
					final boolean _isPresent = s.isPresent();
					if (_isPresent) {
						final StringBuilder _append = sb.append("{");
						final StringBuilder _append_1 = _append.append(exp_1);
						_append_1.append("}");
					} else {
						sb.append("{-1}");
					}
				}
			}
		}
		return sb.toString();
	}

	protected FluidFrame _toSimulationModel(final HDLConcat obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final ArrayList<HDLExpression> _cats = obj.getCats();
		final Iterator<HDLExpression> iter = _cats.iterator();
		final HDLExpression init = iter.next();
		final FluidFrame _simulationModel = this.toSimulationModel(init, context, process);
		res.append(_simulationModel);
		final HDLType initType = TypeExtension.typeOfForced(init, SimulationTransformationExtension.PSEX_STAGE);
		int owidth = (HDLPrimitives.getWidth(initType, context)).intValue();
		while (iter.hasNext()) {
			{
				final HDLExpression exp = iter.next();
				final FluidFrame _simulationModel_1 = this.toSimulationModel(exp, context, process);
				res.append(_simulationModel_1);
				final HDLType expType = TypeExtension.typeOfForced(exp, SimulationTransformationExtension.PSEX_STAGE);
				final int width = (HDLPrimitives.getWidth(expType, context)).intValue();
				final String _string = Integer.valueOf(owidth).toString();
				final String _string_1 = Integer.valueOf(width).toString();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.concat, _string, _string_1);
				res.add(_argumentedInstruction);
				owidth = (owidth + width);
			}
		}
		return res;
	}

	public FluidFrame toSimulationModelUnit(final HDLUnit obj, final HDLEvaluationContext context) {
		final FluidFrame res = new FluidFrame(obj, null);
		final ArrayList<HDLStatement> _inits = obj.getInits();
		for (final HDLStatement stmnt : _inits) {
			final FluidFrame _simulationModel = this.toSimulationModel(stmnt, context, null);
			res.addReferencedFrame(_simulationModel);
		}
		final ArrayList<HDLStatement> _statements = obj.getStatements();
		for (final HDLStatement stmnt_1 : _statements) {
			final FluidFrame _simulationModel_1 = this.toSimulationModel(stmnt_1, context, null);
			res.addReferencedFrame(_simulationModel_1);
		}
		final ArrayList<HDLAnnotation> _annotations = obj.getAnnotations();
		final String[] _annoString = this.toAnnoString(_annotations);
		res.annotations = _annoString;
		final HDLRegisterConfig[] regConfigs = obj.<HDLRegisterConfig> getAllObjectsOf(HDLRegisterConfig.class, true);
		final Set<HDLQualifiedName> lst = new LinkedHashSet<HDLQualifiedName>();
		for (final HDLRegisterConfig reg : regConfigs) {
			{
				final HDLExpression _rst = reg.getRst();
				final HDLQualifiedName rstVar = FullNameExtension.fullNameOf(_rst);
				final boolean _contains = lst.contains(rstVar);
				final boolean _not = (!_contains);
				if (_not) {
					lst.add(rstVar);
					final String rstVarName = rstVar.toString();
					final FluidFrame rstFrame = new FluidFrame(obj, (InternalInformation.PRED_PREFIX + rstVarName), false, null);
					final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal, rstVarName);
					rstFrame.add(_argumentedInstruction);
					rstFrame.add(Instruction.const0);
					rstFrame.add(Instruction.not_eq);
					rstFrame.createPredVar();
					res.addReferencedFrame(rstFrame);
				}
			}
		}
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLManip obj, final HDLEvaluationContext context, final String process) {
		final HDLExpression _target = obj.getTarget();
		final FluidFrame res = this.toSimulationModel(_target, context, process);
		final HDLManip.HDLManipType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case ARITH_NEG:
				final int width = this.targetSizeWithType(obj, context);
				final String _string = Integer.valueOf(width).toString();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.arith_neg, _string);
				res.add(_argumentedInstruction);
				break;
			case BIT_NEG:
				final int width_1 = this.targetSizeWithType(obj, context);
				final String _string_1 = Integer.valueOf(width_1).toString();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.bit_neg, _string_1);
				res.add(_argumentedInstruction_1);
				break;
			case LOGIC_NEG:
				res.add(Instruction.logiNeg);
				break;
			case CAST:
				final HDLExpression _target_1 = obj.getTarget();
				final HDLType _typeOfForced = TypeExtension.typeOfForced(_target_1, SimulationTransformationExtension.PSEX_STAGE);
				final HDLPrimitive current = ((HDLPrimitive) _typeOfForced);
				final HDLType _castTo = obj.getCastTo();
				final HDLPrimitive target = ((HDLPrimitive) _castTo);
				final boolean isCurrentInt = (((current.getType() == HDLPrimitive.HDLPrimitiveType.ANY_INT) || (current.getType() == HDLPrimitive.HDLPrimitiveType.INT))
						|| (current.getType() == HDLPrimitive.HDLPrimitiveType.INTEGER));
				final boolean isCurrentBit = (((current.getType() == HDLPrimitive.HDLPrimitiveType.ANY_BIT) || (current.getType() == HDLPrimitive.HDLPrimitiveType.BIT))
						|| (current.getType() == HDLPrimitive.HDLPrimitiveType.BITVECTOR));
				final boolean isCurrentUint = (((current.getType() == HDLPrimitive.HDLPrimitiveType.ANY_UINT) || (current.getType() == HDLPrimitive.HDLPrimitiveType.UINT))
						|| (current.getType() == HDLPrimitive.HDLPrimitiveType.NATURAL));
				final boolean isTargetInt = (((target.getType() == HDLPrimitive.HDLPrimitiveType.ANY_INT) || (target.getType() == HDLPrimitive.HDLPrimitiveType.INT))
						|| (target.getType() == HDLPrimitive.HDLPrimitiveType.INTEGER));
				final boolean isTargetUint = (((target.getType() == HDLPrimitive.HDLPrimitiveType.ANY_UINT) || (target.getType() == HDLPrimitive.HDLPrimitiveType.UINT))
						|| (target.getType() == HDLPrimitive.HDLPrimitiveType.NATURAL));
				final boolean isTargetBit = (((target.getType() == HDLPrimitive.HDLPrimitiveType.ANY_BIT) || (target.getType() == HDLPrimitive.HDLPrimitiveType.BIT))
						|| (target.getType() == HDLPrimitive.HDLPrimitiveType.BITVECTOR));
				final int currentWidth = this.getWidth(current, context);
				final int targetWidth = this.getWidth(target, context);
				if (isCurrentInt) {
					final String _string_2 = Integer.toString(targetWidth);
					final String _string_3 = Integer.toString(currentWidth);
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.cast_int, _string_2, _string_3);
					res.instructions.add(_argumentedInstruction_2);
					if (isTargetUint) {
						final String _string_4 = Integer.toString(targetWidth);
						final String _string_5 = Integer.toString(targetWidth);
						final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.cast_uint, _string_4, _string_5);
						res.instructions.add(_argumentedInstruction_3);
					}
				} else {
					if (isCurrentUint) {
						final String _string_6 = Integer.toString(targetWidth);
						final String _string_7 = Integer.toString(currentWidth);
						final FluidFrame.ArgumentedInstruction _argumentedInstruction_4 = new FluidFrame.ArgumentedInstruction(Instruction.cast_uint, _string_6, _string_7);
						res.instructions.add(_argumentedInstruction_4);
						if (isTargetInt) {
							final String _string_8 = Integer.toString(targetWidth);
							final String _string_9 = Integer.toString(targetWidth);
							final FluidFrame.ArgumentedInstruction _argumentedInstruction_5 = new FluidFrame.ArgumentedInstruction(Instruction.cast_int, _string_8, _string_9);
							res.instructions.add(_argumentedInstruction_5);
						}
					} else {
						if (isCurrentBit) {
							if (isTargetInt) {
								final String _string_10 = Integer.toString(targetWidth);
								final String _string_11 = Integer.toString(currentWidth);
								final FluidFrame.ArgumentedInstruction _argumentedInstruction_6 = new FluidFrame.ArgumentedInstruction(Instruction.cast_int, _string_10,
										_string_11);
								res.instructions.add(_argumentedInstruction_6);
							} else {
								if ((isTargetUint || isTargetBit)) {
									final String _string_12 = Integer.toString(targetWidth);
									final String _string_13 = Integer.toString(currentWidth);
									final FluidFrame.ArgumentedInstruction _argumentedInstruction_7 = new FluidFrame.ArgumentedInstruction(Instruction.cast_uint, _string_12,
											_string_13);
									res.instructions.add(_argumentedInstruction_7);
								}
							}
						} else {
							final HDLPrimitive.HDLPrimitiveType _type_1 = target.getType();
							final String _plus = ("Cast to type:" + _type_1);
							final String _plus_1 = (_plus + " not supported");
							throw new IllegalArgumentException(_plus_1);
						}
					}
				}
				break;
			default:
				break;
			}
		}
		return res;
	}

	private int getWidth(final HDLPrimitive current, final HDLEvaluationContext context) {
		final HDLPrimitive.HDLPrimitiveType _type = current.getType();
		boolean _matched = false;
		if (Objects.equal(_type, HDLPrimitive.HDLPrimitiveType.BIT)) {
			_matched = true;
			return 1;
		}
		if (!_matched) {
			if (((current.getType() == HDLPrimitive.HDLPrimitiveType.INTEGER) || (current.getType() == HDLPrimitive.HDLPrimitiveType.NATURAL))) {
				_matched = true;
				return 32;
			}
		}
		if (!_matched) {
			if ((((current.getType() == HDLPrimitive.HDLPrimitiveType.INT) || (current.getType() == HDLPrimitive.HDLPrimitiveType.UINT))
					|| (current.getType() == HDLPrimitive.HDLPrimitiveType.BITVECTOR))) {
				_matched = true;
				final HDLExpression _width = current.getWidth();
				final BigInteger _valueOfForced = ConstantEvaluate.valueOfForced(_width, context, "PSEX");
				return _valueOfForced.intValue();
			}
		}
		final String _plus = (current + " width is unknown");
		throw new HDLCodeGenerationException(current, _plus, "PSEX");
	}

	protected FluidFrame _toSimulationModel(final HDLEnumRef obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final HDLEnum hEnum = obj.resolveHEnumForced(SimulationTransformationExtension.PSEX_STAGE);
		final HDLVariable hVar = obj.resolveVarForced(SimulationTransformationExtension.PSEX_STAGE);
		final ArrayList<HDLVariable> _enums = hEnum.getEnums();
		final int idx = _enums.indexOf(hVar);
		final HDLQualifiedName _fullNameOf = FullNameExtension.fullNameOf(hVar);
		final String _string = _fullNameOf.toString();
		final BigInteger _valueOf = BigInteger.valueOf(idx);
		res.addConstant(_string, _valueOf, VariableInformation.Type.ENUM);
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLVariableRef varRef, final HDLEvaluationContext context, final String process) {
		final HDLVariableRef obj = this.redirectRef(varRef);
		final FluidFrame res = new FluidFrame(obj, process);
		final String refName = SimulationTransformationExtension.getVarName(obj, false, context);
		final ArrayList<HDLExpression> _array = obj.getArray();
		this.createPushIndex(_array, context, res, process, refName);
		final ArrayList<HDLRange> _bits = obj.getBits();
		this.createPushIndexBits(_bits, context, res, process, refName);
		boolean fixedArray = true;
		final ArrayList<HDLExpression> _array_1 = obj.getArray();
		for (final HDLExpression idx : _array_1) {
			final Optional<BigInteger> _valueOf = ConstantEvaluate.valueOf(idx, context);
			final boolean _isPresent = _valueOf.isPresent();
			final boolean _not = (!_isPresent);
			if (_not) {
				fixedArray = false;
			}
		}
		final ArrayList<HDLRange> _bits_1 = obj.getBits();
		final int _size = _bits_1.size();
		final int _plus = (_size + 1);
		final ArrayList<String> bits = new ArrayList<String>(_plus);
		bits.add(refName);
		final ArrayList<HDLRange> _bits_2 = obj.getBits();
		final boolean _isEmpty = _bits_2.isEmpty();
		final boolean _not_1 = (!_isEmpty);
		if (_not_1) {
			final ArrayList<HDLRange> _bits_3 = obj.getBits();
			for (final HDLRange r : _bits_3) {
				final String _string = r.toString();
				bits.add(_string);
			}
		}
		final HDLVariable resVar = obj.resolveVarForced("PSEX");
		final HDLVariableDeclaration.HDLDirection dir = resVar.getDirection();
		boolean _matched = false;
		if (Objects.equal(dir, HDLVariableDeclaration.HDLDirection.INTERNAL)) {
			_matched = true;
			final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal,
					((String[]) Conversions.unwrapArray(bits, String.class)));
			res.add(_argumentedInstruction);
		}
		if (!_matched) {
			if (((dir == HDLVariableDeclaration.HDLDirection.PARAMETER) || (dir == HDLVariableDeclaration.HDLDirection.CONSTANT))) {
				_matched = true;
				if ((!fixedArray)) {
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal,
							((String[]) Conversions.unwrapArray(bits, String.class)));
					res.add(_argumentedInstruction_1);
				} else {
					final BigInteger bVal = ConstantEvaluate.valueOfForced(obj, context, SimulationTransformationExtension.PSEX_STAGE);
					final HDLType _typeOfForced = TypeExtension.typeOfForced(resVar, SimulationTransformationExtension.PSEX_STAGE);
					final VariableInformation.Type _asType = this.asType(_typeOfForced);
					res.addConstant(refName, bVal, _asType);
				}
			}
		}
		if (!_matched) {
			if (Objects.equal(dir, HDLVariableDeclaration.HDLDirection.IN)) {
				_matched = true;
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal,
						((String[]) Conversions.unwrapArray(bits, String.class)));
				res.add(_argumentedInstruction_2);
			}
		}
		if (!_matched) {
			if (((dir == HDLVariableDeclaration.HDLDirection.OUT) || (dir == HDLVariableDeclaration.HDLDirection.INOUT))) {
				_matched = true;
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.loadInternal,
						((String[]) Conversions.unwrapArray(bits, String.class)));
				res.add(_argumentedInstruction_3);
			}
		}
		if (!_matched)
			throw new HDLCodeGenerationException(obj, ("Failed to resolve direction:" + dir), "PSEX");
		return res;
	}

	public HDLVariableRef redirectRef(final HDLVariableRef varRef) {
		final HDLVariable resVar = varRef.resolveVarForced(SimulationTransformationExtension.PSEX_STAGE);
		final HDLAnnotation memAnno = resVar.getAnnotation(HDLBuiltInAnnotationProvider.HDLBuiltInAnnotations.memory);
		HDLVariableRef obj = varRef;
		if ((memAnno != null)) {
			final String _value = memAnno.getValue();
			final HDLQualifiedName _create = HDLQualifiedName.create(_value);
			final HDLVariableRef _setVar = varRef.setVar(_create);
			obj = _setVar;
			final IHDLObject _container = varRef.getContainer();
			final HDLVariableRef _copyDeepFrozen = obj.copyDeepFrozen(_container);
			obj = _copyDeepFrozen;
		}
		return obj;
	}

	protected FluidFrame _toSimulationModel(final HDLLiteral obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final Boolean _str = obj.getStr();
		final boolean _tripleNotEquals = (_str != null);
		if (_tripleNotEquals) {
			final Boolean _str_1 = obj.getStr();
			if ((_str_1).booleanValue()) {
				final String _val = obj.getVal();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.loadConstantString, _val);
				res.add(_argumentedInstruction);
				return res;
			} else {
				final String _val_1 = obj.getVal();
				final boolean _equals = Objects.equal(HDLLiteral.FALSE, _val_1);
				if (_equals) {
					final String _val_2 = obj.getVal();
					res.addConstant(_val_2, BigInteger.ZERO, VariableInformation.Type.BOOL);
					return res;
				}
				final String _val_3 = obj.getVal();
				final boolean _equals_1 = Objects.equal(HDLLiteral.TRUE, _val_3);
				if (_equals_1) {
					final String _val_4 = obj.getVal();
					res.addConstant(_val_4, BigInteger.ONE, VariableInformation.Type.BOOL);
					return res;
				}
			}
		}
		final BigInteger value = obj.getValueAsBigInt();
		final boolean _equals_2 = BigInteger.ZERO.equals(value);
		if (_equals_2) {
			res.add(Instruction.const0);
			return res;
		}
		final boolean _equals_3 = BigInteger.ONE.equals(value);
		if (_equals_3) {
			res.add(Instruction.const1);
			return res;
		}
		final boolean _equals_4 = BigInteger.valueOf(2L).equals(value);
		if (_equals_4) {
			res.add(Instruction.const2);
			return res;
		}
		final String _string = value.toString();
		res.addConstant(_string, value, VariableInformation.Type.INT);
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLEqualityOp obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final HDLExpression _left = obj.getLeft();
		final FluidFrame _simulationModel = this.toSimulationModel(_left, context, process);
		res.append(_simulationModel);
		final HDLExpression _right = obj.getRight();
		final FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context, process);
		res.append(_simulationModel_1);
		final HDLEqualityOp.HDLEqualityOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case EQ:
				res.add(Instruction.eq);
				break;
			case NOT_EQ:
				res.add(Instruction.not_eq);
				break;
			case GREATER:
				res.add(Instruction.greater);
				break;
			case GREATER_EQ:
				res.add(Instruction.greater_eq);
				break;
			case LESS:
				res.add(Instruction.less);
				break;
			case LESS_EQ:
				res.add(Instruction.less_eq);
				break;
			default:
				break;
			}
		}
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLBitOp obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final HDLExpression _left = obj.getLeft();
		final FluidFrame _simulationModel = this.toSimulationModel(_left, context, process);
		res.append(_simulationModel);
		final HDLExpression _right = obj.getRight();
		final FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context, process);
		res.append(_simulationModel_1);
		final HDLBitOp.HDLBitOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case AND:
				final int width = this.targetSizeWithType(obj, context);
				final String _string = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.and, _string);
				res.add(_argumentedInstruction);
				break;
			case LOGI_AND:
				res.add(Instruction.logiAnd);
				break;
			case OR:
				final int width_1 = this.targetSizeWithType(obj, context);
				final String _string_1 = Integer.toString(width_1);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.or, _string_1);
				res.add(_argumentedInstruction_1);
				break;
			case LOGI_OR:
				res.add(Instruction.logiOr);
				break;
			case XOR:
				final int width_2 = this.targetSizeWithType(obj, context);
				final String _string_2 = Integer.toString(width_2);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.xor, _string_2);
				res.add(_argumentedInstruction_2);
				break;
			default:
				break;
			}
		}
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLArithOp obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final HDLExpression _left = obj.getLeft();
		final FluidFrame _simulationModel = this.toSimulationModel(_left, context, process);
		res.append(_simulationModel);
		final HDLExpression _right = obj.getRight();
		final FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context, process);
		res.append(_simulationModel_1);
		final int width = this.targetSizeWithType(obj, context);
		final HDLArithOp.HDLArithOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case DIV:
				final String _string = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.div, _string);
				res.add(_argumentedInstruction);
				break;
			case MINUS:
				final String _string_1 = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.minus, _string_1);
				res.add(_argumentedInstruction_1);
				break;
			case MOD:
				final String _string_2 = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.mod, _string_2);
				res.add(_argumentedInstruction_2);
				break;
			case MUL:
				final String _string_3 = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.mul, _string_3);
				res.add(_argumentedInstruction_3);
				break;
			case PLUS:
				final String _string_4 = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_4 = new FluidFrame.ArgumentedInstruction(Instruction.plus, _string_4);
				res.add(_argumentedInstruction_4);
				break;
			case POW:
				final String _string_5 = Integer.toString(width);
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_5 = new FluidFrame.ArgumentedInstruction(Instruction.pow, _string_5);
				res.add(_argumentedInstruction_5);
				break;
			default:
				break;
			}
		}
		return res;
	}

	protected FluidFrame _toSimulationModel(final HDLShiftOp obj, final HDLEvaluationContext context, final String process) {
		final FluidFrame res = new FluidFrame(obj, process);
		final HDLExpression _left = obj.getLeft();
		final FluidFrame _simulationModel = this.toSimulationModel(_left, context, process);
		res.append(_simulationModel);
		final HDLExpression _right = obj.getRight();
		final FluidFrame _simulationModel_1 = this.toSimulationModel(_right, context, process);
		res.append(_simulationModel_1);
		final int width = this.targetSizeWithType(obj, context);
		final HDLShiftOp.HDLShiftOpType _type = obj.getType();
		if (_type != null) {
			switch (_type) {
			case SLL:
				final String _string = Integer.valueOf(width).toString();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction = new FluidFrame.ArgumentedInstruction(Instruction.sll, _string);
				res.add(_argumentedInstruction);
				break;
			case SRA:
				final HDLExpression _left_1 = obj.getLeft();
				final HDLType type = TypeExtension.typeOfForced(_left_1, SimulationTransformationExtension.PSEX_STAGE);
				final HDLPrimitive prim = ((HDLPrimitive) type);
				if (((prim.getType() == HDLPrimitive.HDLPrimitiveType.INTEGER) || (prim.getType() == HDLPrimitive.HDLPrimitiveType.INT))) {
					final String _string_1 = Integer.valueOf(width).toString();
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_1 = new FluidFrame.ArgumentedInstruction(Instruction.sra, _string_1);
					res.add(_argumentedInstruction_1);
				} else {
					final String _string_2 = Integer.valueOf(width).toString();
					final FluidFrame.ArgumentedInstruction _argumentedInstruction_2 = new FluidFrame.ArgumentedInstruction(Instruction.srl, _string_2);
					res.add(_argumentedInstruction_2);
				}
				break;
			case SRL:
				final String _string_3 = Integer.valueOf(width).toString();
				final FluidFrame.ArgumentedInstruction _argumentedInstruction_3 = new FluidFrame.ArgumentedInstruction(Instruction.srl, _string_3);
				res.add(_argumentedInstruction_3);
				break;
			default:
				break;
			}
		}
		return res;
	}

	public int targetSizeWithType(final HDLExpression op, final HDLEvaluationContext context) {
		final Optional<? extends HDLType> _typeOf = TypeExtension.typeOf(op);
		final HDLType _get = _typeOf.get();
		final HDLPrimitive type = ((HDLPrimitive) _get);
		final Integer width = HDLPrimitives.getWidth(type, context);
		if (((type.getType() == HDLPrimitive.HDLPrimitiveType.INT) || (type.getType() == HDLPrimitive.HDLPrimitiveType.INTEGER)))
			return (((width).intValue() << 1) | 1);
		return ((width).intValue() << 1);
	}

	public FluidFrame toSimulationModel(final IHDLObject obj, final HDLEvaluationContext context, final String process) {
		if (obj instanceof HDLEnumRef)
			return _toSimulationModel((HDLEnumRef) obj, context, process);
		else if (obj instanceof HDLVariableRef)
			return _toSimulationModel((HDLVariableRef) obj, context, process);
		else if (obj instanceof HDLArithOp)
			return _toSimulationModel((HDLArithOp) obj, context, process);
		else if (obj instanceof HDLBitOp)
			return _toSimulationModel((HDLBitOp) obj, context, process);
		else if (obj instanceof HDLBlock)
			return _toSimulationModel((HDLBlock) obj, context, process);
		else if (obj instanceof HDLEnumDeclaration)
			return _toSimulationModel((HDLEnumDeclaration) obj, context, process);
		else if (obj instanceof HDLEqualityOp)
			return _toSimulationModel((HDLEqualityOp) obj, context, process);
		else if (obj instanceof HDLIfStatement)
			return _toSimulationModel((HDLIfStatement) obj, context, process);
		else if (obj instanceof HDLInterfaceDeclaration)
			return _toSimulationModel((HDLInterfaceDeclaration) obj, context, process);
		else if (obj instanceof HDLShiftOp)
			return _toSimulationModel((HDLShiftOp) obj, context, process);
		else if (obj instanceof HDLSwitchStatement)
			return _toSimulationModel((HDLSwitchStatement) obj, context, process);
		else if (obj instanceof HDLVariableDeclaration)
			return _toSimulationModel((HDLVariableDeclaration) obj, context, process);
		else if (obj instanceof HDLAssignment)
			return _toSimulationModel((HDLAssignment) obj, context, process);
		else if (obj instanceof HDLConcat)
			return _toSimulationModel((HDLConcat) obj, context, process);
		else if (obj instanceof HDLFunctionCall)
			return _toSimulationModel((HDLFunctionCall) obj, context, process);
		else if (obj instanceof HDLLiteral)
			return _toSimulationModel((HDLLiteral) obj, context, process);
		else if (obj instanceof HDLManip)
			return _toSimulationModel((HDLManip) obj, context, process);
		else if (obj instanceof HDLExpression)
			return _toSimulationModel((HDLExpression) obj, context, process);
		else if (obj instanceof HDLStatement)
			return _toSimulationModel((HDLStatement) obj, context, process);
		else if (obj != null)
			return _toSimulationModel(obj, context, process);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, context, process).toString());
	}

	public FluidFrame toSimulationModelPred(final HDLStatement obj, final FluidFrame.ArgumentedInstruction predicate, final HDLEvaluationContext context, final String process) {
		if (obj instanceof HDLBlock)
			return _toSimulationModelPred((HDLBlock) obj, predicate, context, process);
		else if (obj instanceof HDLSwitchStatement)
			return _toSimulationModelPred((HDLSwitchStatement) obj, predicate, context, process);
		else if (obj != null)
			return _toSimulationModelPred(obj, predicate, context, process);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, predicate, context, process).toString());
	}

	public FluidFrame toSimulationModelInit(final HDLExpression obj, final HDLEvaluationContext context, final String varName, final String process) {
		if (obj instanceof HDLArrayInit)
			return _toSimulationModelInit((HDLArrayInit) obj, context, varName, process);
		else if (obj != null)
			return _toSimulationModelInit(obj, context, varName, process);
		else
			throw new IllegalArgumentException("Unhandled parameter types: " + Arrays.<Object> asList(obj, context, varName, process).toString());
	}
}
