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
package de.tuhh.ict.pshdl.generator.vhdl

import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLCastsLibrary
import de.tuhh.ict.pshdl.model.HDLAnnotation
import de.tuhh.ict.pshdl.model.HDLArithOp
import de.tuhh.ict.pshdl.model.HDLArithOp$HDLArithOpType
import de.tuhh.ict.pshdl.model.HDLAssignment
import de.tuhh.ict.pshdl.model.HDLBlock
import de.tuhh.ict.pshdl.model.HDLClass
import de.tuhh.ict.pshdl.model.HDLDirectGeneration
import de.tuhh.ict.pshdl.model.HDLEnum
import de.tuhh.ict.pshdl.model.HDLEnumDeclaration
import de.tuhh.ict.pshdl.model.HDLEnumRef
import de.tuhh.ict.pshdl.model.HDLExpression
import de.tuhh.ict.pshdl.model.HDLForLoop
import de.tuhh.ict.pshdl.model.HDLFunction
import de.tuhh.ict.pshdl.model.HDLFunctionCall
import de.tuhh.ict.pshdl.model.HDLIfStatement
import de.tuhh.ict.pshdl.model.HDLInterface
import de.tuhh.ict.pshdl.model.HDLInterfaceDeclaration
import de.tuhh.ict.pshdl.model.HDLInterfaceInstantiation
import de.tuhh.ict.pshdl.model.HDLLiteral
import de.tuhh.ict.pshdl.model.HDLObject
import de.tuhh.ict.pshdl.model.HDLObject$GenericMeta
import de.tuhh.ict.pshdl.model.HDLPrimitive
import de.tuhh.ict.pshdl.model.HDLRange
import de.tuhh.ict.pshdl.model.HDLReference
import de.tuhh.ict.pshdl.model.HDLRegisterConfig
import de.tuhh.ict.pshdl.model.HDLStatement
import de.tuhh.ict.pshdl.model.HDLSwitchCaseStatement
import de.tuhh.ict.pshdl.model.HDLSwitchStatement
import de.tuhh.ict.pshdl.model.HDLType
import de.tuhh.ict.pshdl.model.HDLVariable
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection
import de.tuhh.ict.pshdl.model.HDLVariableRef
import de.tuhh.ict.pshdl.model.evaluation.ConstantEvaluate
import de.tuhh.ict.pshdl.model.extensions.FullNameExtension
import de.tuhh.ict.pshdl.model.extensions.TypeExtension
import de.tuhh.ict.pshdl.model.types.builtIn.HDLFunctions
import de.tuhh.ict.pshdl.model.utils.HDLQualifiedName
import de.tuhh.ict.pshdl.model.utils.HDLQuery
import de.tuhh.ict.pshdl.model.utils.Insulin
import de.upb.hni.vmagic.AssociationElement
import de.upb.hni.vmagic.Choices
import de.upb.hni.vmagic.DiscreteRange
import de.upb.hni.vmagic.Range
import de.upb.hni.vmagic.Range$Direction
import de.upb.hni.vmagic.concurrent.ComponentInstantiation
import de.upb.hni.vmagic.concurrent.ConcurrentStatement
import de.upb.hni.vmagic.concurrent.EntityInstantiation
import de.upb.hni.vmagic.concurrent.ForGenerateStatement
import de.upb.hni.vmagic.declaration.Component
import de.upb.hni.vmagic.declaration.ConstantDeclaration
import de.upb.hni.vmagic.declaration.SignalDeclaration
import de.upb.hni.vmagic.expression.Aggregate
import de.upb.hni.vmagic.expression.Expression
import de.upb.hni.vmagic.expression.TypeConversion
import de.upb.hni.vmagic.libraryunit.Entity
import de.upb.hni.vmagic.literal.CharacterLiteral
import de.upb.hni.vmagic.object.Constant
import de.upb.hni.vmagic.object.Signal
import de.upb.hni.vmagic.object.SignalAssignmentTarget
import de.upb.hni.vmagic.object.VhdlObject$Mode
import de.upb.hni.vmagic.statement.CaseStatement
import de.upb.hni.vmagic.statement.CaseStatement$Alternative
import de.upb.hni.vmagic.statement.ForStatement
import de.upb.hni.vmagic.statement.IfStatement
import de.upb.hni.vmagic.statement.SequentialStatement
import de.upb.hni.vmagic.statement.SignalAssignment
import de.upb.hni.vmagic.type.ConstrainedArray
import de.upb.hni.vmagic.type.EnumerationType
import de.upb.hni.vmagic.type.SubtypeIndication
import de.upb.hni.vmagic.type.UnresolvedType
import java.math.BigInteger
import java.util.ArrayList
import java.util.Collection
import java.util.EnumSet
import java.util.HashSet
import java.util.LinkedHashMap
import java.util.LinkedList
import java.util.List
import java.util.Map
import java.util.Map$Entry
import java.util.Set

import static de.tuhh.ict.pshdl.generator.vhdl.VHDLStatementExtension.*
import static de.tuhh.ict.pshdl.model.HDLVariableDeclaration$HDLDirection.*
import static de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider$HDLBuiltInAnnotations.*
import de.tuhh.ict.pshdl.model.HDLUnresolvedFragment
import de.tuhh.ict.pshdl.model.HDLResolvedRef
import de.tuhh.ict.pshdl.model.IHDLObject
import de.tuhh.ict.pshdl.model.parser.SourceInfo
import de.upb.hni.vmagic.util.Comments
import com.google.common.base.Optional
import de.tuhh.ict.pshdl.model.utils.HDLLibrary
import de.tuhh.ict.pshdl.model.HDLArrayInit

class VHDLStatementExtension {
	public static VHDLStatementExtension INST = new VHDLStatementExtension

	def static vhdlOf(HDLStatement stmnt, int pid) {
		return INST.toVHDL(stmnt, pid)
	}

	extension VHDLExpressionExtension vee = new VHDLExpressionExtension

	private static GenericMeta<HDLQualifiedName> ORIGINAL_FULLNAME = new GenericMeta<HDLQualifiedName>(
		"ORIGINAL_FULLNAME", true);

	public static GenericMeta<Boolean> EXPORT = new GenericMeta<Boolean>("EXPORT", true)

	def dispatch VHDLContext toVHDL(HDLDirectGeneration obj, int pid) {
		return new VHDLContext
	}

	def dispatch VHDLContext toVHDL(HDLFunctionCall obj, int pid) {
		return HDLFunctions::toVHDL(obj, pid)
	}

	def dispatch VHDLContext toVHDL(HDLBlock obj, int pid) {
		val VHDLContext res = new VHDLContext
		var boolean process = false
		if (obj.process !== null && obj.process) {
			process = true
		}
		val newPid = if(process) res.newProcessID else pid
		for (HDLStatement stmnt : obj.statements) {
			res.merge(stmnt.toVHDL(newPid), false)
		}
		return res.attachComment(obj)
	}

	def VHDLContext attachComment(VHDLContext context, IHDLObject block) {
		try {
			val srcInfo = block.getMeta(SourceInfo::INFO)
			if (srcInfo !== null && context.statement !== null) {
				val newComments = new ArrayList<String>
				for (String comment : srcInfo.comments) {
					if (comment.startsWith("//"))
						newComments.add(comment.substring(2, comment.length - 1))
					else {
						val newComment = comment.substring(2, comment.length - 2)
						newComments.addAll(newComment.split("\n"))
					}
				}
				Comments::setComments(context.statement, newComments)
			}
		} catch (Exception e) {
		}
		return context
	}

	def dispatch VHDLContext toVHDL(HDLEnumDeclaration obj, int pid) {
		val VHDLContext res = new VHDLContext
		val HDLEnum hEnum = obj.HEnum
		val List<String> enums = new LinkedList<String>
		for (HDLVariable hVar : hEnum.enums) {
			enums.add(hVar.name)
		}
		val String[] enumArr = enums
		res.addInternalTypeDeclaration(new EnumerationType(hEnum.name, enumArr))
		return res.attachComment(obj)
	}

	def dispatch VHDLContext toVHDL(HDLInterfaceDeclaration obj, int pid) {
		return new VHDLContext
	}

	private static EnumSet<HDLDirection> inAndOut = EnumSet::of(HDLDirection::IN, HDLDirection::INOUT, HDLDirection::OUT)

	def dispatch VHDLContext toVHDL(HDLInterfaceInstantiation obj, int pid) {
		val VHDLContext res = new VHDLContext
		val HDLInterface hIf = obj.resolveHIf.get
		val HDLVariable hVar = obj.^var
		val String ifName = obj.^var.name
		val HDLQualifiedName asRef = hIf.asRef
		val HDLInterfaceDeclaration hid = hIf.getContainer(typeof(HDLInterfaceDeclaration))
		var List<AssociationElement> portMap
		var List<AssociationElement> genericMap
		var ConcurrentStatement instantiation

		// Perform instantiation as Component rather than Entity if
		// VHDLComponent Annotation is present
		val ArrayList<HDLVariableDeclaration> ports = hIf.ports
		if (hid !== null && hid.getAnnotation(VHDLComponent) !== null) {
			val HDLAnnotation anno = hid.getAnnotation(VHDLComponent)
			if ("declare".equals(anno.value)) {
				val Component c = new Component(asRef.lastSegment.toString)
				val VHDLContext cContext = new VHDLContext
				for (HDLVariableDeclaration port : ports) {
					cContext.merge(port.toVHDL(-1), true)
				}
				for (Signal signal : cContext.ports) {
					c.port.add(signal)
				}
				for (ConstantDeclaration cd : cContext.constants) {
					for (Object vobj : cd.objects)
						c.generic.add(vobj as Constant)
				}
				for (Constant constant : cContext.generics) {
					c.generic.add(constant)
				}
				res.addComponent(c)
			} else
				res.addImport(VHDLPackageExtension::INST.getNameRef(asRef))
			val Component entity = new Component(asRef.lastSegment.toString)
			val ComponentInstantiation inst = new ComponentInstantiation(ifName, entity)
			portMap = inst.portMap
			genericMap = inst.genericMap
			instantiation = inst
		} else {
			val Entity entity = new Entity(VHDLPackageExtension::INST.getNameRef(asRef).toString)
			val EntityInstantiation inst = new EntityInstantiation(ifName, entity)
			portMap = inst.portMap
			genericMap = inst.genericMap
			instantiation = inst
		}
		for (HDLVariableDeclaration hvd : ports) {
			if (inAndOut.contains(hvd.direction)) {
				val Collection<HDLAnnotation> typeAnno = HDLQuery::select(typeof(HDLAnnotation)).from(hvd).where(
					HDLAnnotation::fName).isEqualTo(VHDLType.toString).all
				for (HDLVariable hvar : hvd.variables) {
					var HDLVariable sigVar = hvar.setName(ifName + "_" + hvar.name)
					var HDLVariableRef ref = sigVar.asHDLRef
					var i = 0
					for (HDLExpression exp : hVar.dimensions) {
						ref = ref.addArray(new HDLVariableRef().setVar(HDLQualifiedName::create(i.asIndex)))
						i = i + 1;
					}
					for (HDLExpression exp : hVar.dimensions) {
						sigVar = sigVar.addDimensions(exp)
					}
					if (hvar.dimensions.size != 0) {

						//Arrays are always named in VHDL, so the type annotation should be present
						if (typeAnno.isEmpty) {
							val HDLQualifiedName name = VHDLPackageExtension::INST.getPackageNameRef(asRef).append(
								getArrayRefName(hvar, true))
							res.addImport(name)
							val HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection::INTERNAL).
								setVariables(
									HDLObject::asList(
										sigVar.setDimensions(null).addAnnotations(VHDLType.create(name.toString)))).
								copyDeepFrozen(obj)
							res.merge(newHVD.toVHDL(pid), false)
						} else {
							val HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection::INTERNAL).
								setVariables(HDLObject::asList(sigVar.setDimensions(null))).copyDeepFrozen(obj)
							res.merge(newHVD.toVHDL(pid), false)
						}
					} else {
						val HDLVariableDeclaration newHVD = hvd.setDirection(HDLDirection::INTERNAL).
							setVariables(HDLObject::asList(sigVar)).copyDeepFrozen(obj)
						res.merge(newHVD.toVHDL(pid), false)
					}
					portMap.add(new AssociationElement(VHDLOutputValidator::getVHDLName(hvar.name), ref.toVHDL))
				}
			} else {

				//Parameter get a special treatment because they have been renamed by HDLInterfaceInstantiation resolveIF
				if (hvd.direction == HDLDirection::PARAMETER) {
					for (HDLVariable hvar : hvd.variables) {
						var HDLVariable sigVar = hvar
						if (hvar.getMeta(HDLInterfaceInstantiation::ORIG_NAME) !== null)
							sigVar = hvar.setName(hvar.getMeta(HDLInterfaceInstantiation::ORIG_NAME))

						val HDLVariableRef ref = hvar.asHDLRef
						genericMap.add(new AssociationElement(sigVar.name, ref.toVHDL))
					}
				}
			}
		}
		var ForGenerateStatement forLoop = null
		if (hVar.dimensions.size == 0)
			res.addConcurrentStatement(instantiation)
		else {
			var i = 0;
			for (HDLExpression exp : hVar.dimensions) {
				val HDLExpression to = new HDLArithOp().setLeft(hVar.dimensions.get(i)).setType(
					HDLArithOp$HDLArithOpType::MINUS).setRight(HDLLiteral::get(1))
				val HDLRange range = new HDLRange().setFrom(HDLLiteral::get(0)).setTo(to).setContainer(obj)
				val ForGenerateStatement newFor = new ForGenerateStatement("generate_" + ifName, i.asIndex,
					range.toVHDL(Range$Direction::TO))
				if (forLoop !== null)
					forLoop.statements.add(newFor)
				else
					res.addConcurrentStatement(newFor)
				forLoop = newFor
				i = i + 1;
			}
			if (forLoop === null)
				throw new IllegalArgumentException("Should not get here")
			forLoop.statements.add(instantiation)
		}
		return res.attachComment(obj)
	}

	def String asIndex(Integer integer) {
		val int i = 'I'.charAt(0)
		return Character::toString((i + integer) as char);
	}

	def static String getArrayRefName(HDLVariable hvar, boolean external) {
		if (external) {
			var HDLQualifiedName fullName
			if (hvar.getMeta(ORIGINAL_FULLNAME) !== null)
				fullName = hvar.getMeta(ORIGINAL_FULLNAME)
			else
				fullName = FullNameExtension::fullNameOf(hvar)
			return fullName.toString('_'.charAt(0)) + "_array"
		}
		return hvar.name + "_array"
	}

	def dispatch VHDLContext toVHDL(HDLVariableDeclaration obj, int pid) {
		val VHDLContext res = new VHDLContext
		val HDLPrimitive primitive = obj.primitive
		var SubtypeIndication type = null
		var HDLExpression resetValue = null
		val HDLAnnotation typeAnno = HDLQuery::select(typeof(HDLAnnotation)).from(obj).where(HDLAnnotation::fName).
			isEqualTo(VHDLType.toString).first
		if (typeAnno !== null) {
			val HDLQualifiedName value = new HDLQualifiedName(typeAnno.value)
			res.addImport(value)
			type = new EnumerationType(value.lastSegment)
		} else {
			if (primitive !== null) {
				type = VHDLCastsLibrary::getType(primitive)
				if (obj.register !== null) {
					resetValue = obj.register.resetValue
				}
			} else {
				val HDLType hType = obj.resolveType.get
				if (hType instanceof HDLEnum) {
					val HDLEnum hEnum = hType as HDLEnum
					type = new EnumerationType(hEnum.name)
					resetValue = new HDLEnumRef().setHEnum(hEnum.asRef).setVar(hEnum.enums.get(0).asRef)
					resetValue.setContainer(obj)
				}
			}
		}
		if (type !== null) {
			for (HDLVariable hvar : obj.variables) {
				val boolean noExplicitResetVar = hvar.getAnnotation(VHDLNoExplicitReset) !== null
				var SubtypeIndication varType = type
				if (hvar.dimensions.size != 0) {
					val ranges = new LinkedList<DiscreteRange<?>>
					for (HDLExpression arrayWidth : hvar.dimensions) {
						val HDLExpression newWidth = new HDLArithOp().setLeft(arrayWidth).setType(
							HDLArithOp$HDLArithOpType::MINUS).setRight(HDLLiteral::get(1))
						val Range range = new HDLRange().setFrom(HDLLiteral::get(0)).setTo(newWidth).copyDeepFrozen(obj).
							toVHDL(Range$Direction::TO)
						ranges.add(range)
					}
					val boolean external = obj.isExternal
					val DiscreteRange[] arrRangs = ranges
					val ConstrainedArray arrType = new ConstrainedArray(getArrayRefName(hvar, external), type, arrRangs)
					res.addTypeDeclaration(arrType, external)
					varType = arrType
				}
				if (resetValue !== null && !noExplicitResetVar) {
					var boolean synchedArray = false
					if (resetValue instanceof HDLVariableRef) {
						val HDLVariableRef ref = resetValue as HDLVariableRef
						synchedArray = ref.resolveVar.get.dimensions.size != 0
					}
					val HDLStatement initLoop = Insulin::createArrayForLoop(hvar.dimensions, 0, resetValue,
						new HDLVariableRef().setVar(hvar.asRef), synchedArray).copyDeepFrozen(obj)
					val VHDLContext vhdl = initLoop.toVHDL(pid)
					res.addResetValue(obj.register, vhdl.statement)
				}
				val Signal s = new Signal(hvar.name, varType)
				val Constant constant = new Constant(hvar.name, varType)
				if (hvar.defaultValue !== null)
					constant.setDefaultValue(hvar.defaultValue.toVHDL)
				if (noExplicitResetVar) {
					var Aggregate assign = Aggregate::OTHERS(new CharacterLiteral('0'.charAt(0)))
					for (HDLExpression exp : hvar.dimensions)
						assign = Aggregate::OTHERS(assign)
					s.setDefaultValue(assign)
				}
				switch (obj.direction) {
					case IN: {
						s.setMode(VhdlObject$Mode::IN)
						res.addPortDeclaration(s)
					}
					case OUT: {
						s.setMode(VhdlObject$Mode::OUT)
						res.addPortDeclaration(s)
					}
					case INOUT: {
						s.setMode(VhdlObject$Mode::INOUT)
						res.addPortDeclaration(s)
					}
					case INTERNAL: {
						val SignalDeclaration sd = new SignalDeclaration(s)
						res.addInternalSignalDeclaration(sd)
					}
					case obj.direction == HIDDEN || obj.direction == CONSTANT: {
						val ConstantDeclaration cd = new ConstantDeclaration(constant)
						if (hvar.hasMeta(EXPORT))
							res.addConstantDeclarationPkg(cd)
						else
							res.addConstantDeclaration(cd)
					}
					case PARAMETER:
						res.addGenericDeclaration(constant)
				}
			}
		}
		return res.attachComment(obj)
	}

	def dispatch VHDLContext toVHDL(HDLSwitchStatement obj, int pid) {
		val VHDLContext context = new VHDLContext
		val HDLExpression hCaseExp = obj.caseExp
		var Optional<BigInteger> width = Optional::absent
		val type = TypeExtension::typeOf(hCaseExp)
		if (type.present && type.get instanceof HDLPrimitive) {
			width = ConstantEvaluate::valueOf((type.get as HDLPrimitive).width, null)
			if (!width.present)
				throw new IllegalArgumentException("HDLPrimitive switch case needs to have constant width")
		}
		val Expression<?> caseExp = hCaseExp.toVHDL
		val Map<HDLSwitchCaseStatement, VHDLContext> ctxs = new LinkedHashMap<HDLSwitchCaseStatement, VHDLContext>
		val Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>
		var boolean hasUnclocked = false
		for (HDLSwitchCaseStatement cs : obj.cases) {
			val VHDLContext vhdl = cs.toVHDL(pid)
			ctxs.put(cs, vhdl)
			if (vhdl.unclockedStatements.size > 0)
				hasUnclocked = true
			configs.addAll(vhdl.clockedStatements.keySet)
		}
		for (HDLRegisterConfig hdlRegisterConfig : configs) {
			val CaseStatement cs = new CaseStatement(caseExp)
			for (Map$Entry<HDLSwitchCaseStatement, VHDLContext> e : ctxs.entrySet) {
				val Alternative alt = createAlternative(cs, e, width)
				val LinkedList<SequentialStatement> clockCase = e.value.clockedStatements.get(hdlRegisterConfig)
				if (clockCase !== null) {
					alt.statements.addAll(clockCase)
				}
			}
			context.addClockedStatement(hdlRegisterConfig, cs)
		}
		if (hasUnclocked) {
			val CaseStatement cs = new CaseStatement(caseExp)
			for (Map$Entry<HDLSwitchCaseStatement, VHDLContext> e : ctxs.entrySet) {
				val Alternative alt = createAlternative(cs, e, width)
				if (e.value.unclockedStatements.get(pid) !== null)
					alt.statements.addAll(e.value.unclockedStatements.get(pid))
			}
			context.addUnclockedStatement(pid, cs, obj)
		}
		return context.attachComment(obj)
	}

	def private Alternative createAlternative(CaseStatement cs, Map$Entry<HDLSwitchCaseStatement, VHDLContext> e,
		Optional<BigInteger> bits) {
		var Alternative alt
		val HDLExpression label = e.key.label
		if (label !== null) {
			val Optional<BigInteger> eval = ConstantEvaluate::valueOf(label, null)
			if (eval.present) {
				if (!bits.present)
					throw new IllegalArgumentException("The width needs to be known for primitive types!")
				alt = cs.createAlternative(VHDLUtils::toBinaryLiteral(bits.get.intValue, eval.get))
			} else
				alt = cs.createAlternative(label.toVHDL); // The only valid

			// reason here is an
			// Enum
			} else {
				alt = cs.createAlternative(Choices::OTHERS)
			}
			return alt
		}

		def dispatch VHDLContext toVHDL(HDLSwitchCaseStatement obj, int pid) {
			val VHDLContext res = new VHDLContext
			for (HDLStatement stmnt : obj.dos) {
				res.merge(stmnt.toVHDL(pid), false)
			}
			return res.attachComment(obj)
		}

		def dispatch VHDLContext toVHDL(HDLAssignment obj, int pid) {
			val VHDLContext context = new VHDLContext
			var SignalAssignment sa = null
			val HDLReference ref = obj.left
			val HDLVariable hvar = ref.resolveVar
			val ArrayList<HDLExpression> dim = hvar.dimensions
			if (dim.size != 0 && ref.classType == HDLClass::HDLVariableRef) {
				val HDLVariableRef varRef = ref as HDLVariableRef
				for (HDLExpression exp : varRef.array) {
					dim.remove(0)
				}
				if (dim.size != 0 && obj.right.classType != HDLClass::HDLArrayInit) {

					//XXX Implement correct array assignment for non full assignments
					val HDLAnnotation typeAnno = hvar.getAnnotation(VHDLType)
					if (typeAnno !== null) {
						sa = new SignalAssignment(ref.toVHDL as SignalAssignmentTarget,
							new TypeConversion(new UnresolvedType(typeAnno.value), obj.right.toVHDL))
					} else {
						val HDLVariableDeclaration hvd = hvar.getContainer(typeof(HDLVariableDeclaration))
						sa = new SignalAssignment(ref.toVHDL as SignalAssignmentTarget,
							new TypeConversion(new UnresolvedType(getArrayRefName(hvar, hvd.isExternal)),
								obj.right.toVHDL))
					}
				} else {
					sa = new SignalAssignment(ref.toVHDL as SignalAssignmentTarget, obj.right.toVHDL)
				}
			} else
				sa = new SignalAssignment(ref.toVHDL as SignalAssignmentTarget, obj.right.toVHDL)
			val HDLRegisterConfig config = hvar.registerConfig
			if (config !== null)
				context.addClockedStatement(config, sa)
			else
				context.addUnclockedStatement(pid, sa, obj)
			return context.attachComment(obj)
		}

		def HDLVariable resolveVar(HDLReference reference) {
			if (reference instanceof HDLUnresolvedFragment)
				throw new RuntimeException("Can not use unresolved fragments")
			return (reference as HDLResolvedRef).resolveVar.get
		}

		def dispatch VHDLContext toVHDL(HDLForLoop obj, int pid) {
			val VHDLContext context = new VHDLContext
			for (HDLStatement stmnt : obj.dos) {
				context.merge(stmnt.toVHDL(pid), false)
			}
			val VHDLContext res = new VHDLContext
			res.merge(context, true)
			for (Map$Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : context.clockedStatements.entrySet) {
				val ForStatement fStmnt = new ForStatement(obj.param.name, obj.range.get(0).toVHDL(Range$Direction::TO))
				fStmnt.statements.addAll(e.value)
				res.addClockedStatement(e.key, fStmnt)
			}
			if (context.unclockedStatements.get(pid) !== null) {
				val ForStatement fStmnt = new ForStatement(obj.param.name, obj.range.get(0).toVHDL(Range$Direction::TO))
				fStmnt.statements.addAll(context.unclockedStatements.get(pid))
				res.addUnclockedStatement(pid, fStmnt, obj)
			}
			return res.attachComment(obj)
		}

		def dispatch VHDLContext toVHDL(HDLIfStatement obj, int pid) {
			val VHDLContext thenCtx = new VHDLContext
			for (HDLStatement stmnt : obj.thenDo) {
				thenCtx.merge(stmnt.toVHDL(pid), false)
			}
			val VHDLContext elseCtx = new VHDLContext
			for (HDLStatement stmnt : obj.elseDo) {
				elseCtx.merge(stmnt.toVHDL(pid), false)
			}
			val Set<HDLRegisterConfig> configs = new HashSet<HDLRegisterConfig>
			configs.addAll(thenCtx.clockedStatements.keySet)
			configs.addAll(elseCtx.clockedStatements.keySet)
			val VHDLContext res = new VHDLContext
			res.merge(thenCtx, true)
			res.merge(elseCtx, true)
			val Expression<?> ifExp = obj.ifExp.toVHDL
			for (HDLRegisterConfig config : configs) {
				val IfStatement ifs = new IfStatement(ifExp)
				if (thenCtx.clockedStatements.get(config) !== null)
					ifs.statements.addAll(thenCtx.clockedStatements.get(config))
				if (elseCtx.clockedStatements.get(config) !== null)
					ifs.elseStatements.addAll(elseCtx.clockedStatements.get(config))
				res.addClockedStatement(config, ifs)
			}
			if (thenCtx.unclockedStatements.size != 0 || elseCtx.unclockedStatements.size != 0) {
				val IfStatement ifs = new IfStatement(ifExp)
				if (thenCtx.unclockedStatements.get(pid) !== null)
					ifs.statements.addAll(thenCtx.unclockedStatements.get(pid))
				if (elseCtx.unclockedStatements.get(pid) !== null)
					ifs.elseStatements.addAll(elseCtx.unclockedStatements.get(pid))
				res.addUnclockedStatement(pid, ifs, obj)
			}
			return res.attachComment(obj)
		}

		def dispatch VHDLContext toVHDL(HDLFunction obj, int pid) {
			throw new IllegalArgumentException("Not supported")
		}
	}
	