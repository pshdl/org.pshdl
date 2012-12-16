package de.tuhh.ict.pshdl.model.aspects;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.*;
import de.tuhh.ict.pshdl.model.validation.builtin.*;

public aspect TypeInference {

	
	pointcut type(IHDLObject obj): target(obj) && execution(HDLType determineType());
	
	HDLType around(IHDLObject obj): type(obj) {
		if (!obj.isFrozen())
			throw new IllegalArgumentException("Target needs to be frozen");
		HDLType res = proceed(obj);
		if (res!=null)
			return res.copyDeepFrozen(obj);
		return res;
	}
	
	public abstract HDLType HDLExpression.determineType();

	/**
	 * Attempt to determine the type of this HDLVariable. For this to work it
	 * needs to have a valid container.
	 * 
	 * @return the HDLType if it could be determined, <code>null</code>
	 *         otherwise.
	 */
	public HDLType HDLVariable.determineType() {
		IHDLObject container = getContainer();
		switch (container.getClassType()) {
		case HDLVariableDeclaration:
			HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
			return hvd.determineType();
		case HDLDirectGeneration:
			HDLDirectGeneration hdg = (HDLDirectGeneration) container;
			return hdg.getHIf();
		case HDLInterfaceInstantiation:
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) container;
			return hii.resolveHIf();
		case HDLForLoop:
			return HDLPrimitive.getNatural();
		case HDLInlineFunction:
			throw new HDLProblemException(new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, this));
		default:
			throw new IllegalArgumentException("Failed to resolve type of " + this + " caused by an unexpected container: " + container);
		}
	}

	public HDLType HDLVariableDeclaration.determineType() {
		if (getPrimitive() != null)
			return getPrimitive();
		return resolveType();
	}

	public HDLType HDLConcat.determineType() {
		Iterator<HDLExpression> iter = getCats().iterator();
		HDLPrimitive type = (HDLPrimitive) iter.next().determineType();
		HDLExpression width = getWidth(type);
		while (iter.hasNext()) {
			type = (HDLPrimitive) iter.next().determineType();
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(getWidth(type));
			width = HDLPrimitives.simplifyWidth(this, width).copy();
		}
		return HDLPrimitive.getBitvector().setWidth(width).setContainer(this);
	}

	private static HDLExpression getWidth(HDLPrimitive type) {
		HDLExpression width = type.getWidth();
		if (type.getType() == HDLPrimitiveType.BIT)
			width = HDLLiteral.get(1);
		return width;
	}

	public HDLType HDLEnumRef.determineType() {
		return resolveHEnum();
	}

	public HDLType HDLManip.determineType() {
		return HDLPrimitives.getInstance().getManipOpType(this).result;
	}

	public HDLType HDLFunctionCall.determineType() {
		return HDLFunctions.getInferenceInfo(this).result;
	}

	public HDLType HDLLiteral.determineType() {
		// Actually depends on context
		return HDLPrimitive.target(getVal().charAt(0) != '-');
	}

	public HDLType HDLVariableRef.determineType() {
		List<HDLRange> bits = getBits();
		if (bits.size() == 0)
			return resolveVar().determineType();
		if (bits.size() == 1 && bits.get(0).getFrom() == null)
			return HDLPrimitive.getBit();
		Iterator<HDLRange> iter = bits.iterator();
		HDLExpression width = HDLPrimitives.simplifyWidth(this, iter.next().getWidth());
		while (iter.hasNext()) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(iter.next().getWidth());
			width = HDLPrimitives.simplifyWidth(this, width);
		}
		return HDLPrimitive.getBitvector().setWidth(width).setContainer(this);
	}

	public HDLType HDLArithOp.determineType() {
		return HDLPrimitives.getInstance().getArithOpType(this).result;
	}

	public HDLType HDLBitOp.determineType() {
		return HDLPrimitives.getInstance().getBitOpType(this).result;
	}

	public HDLType HDLShiftOp.determineType() {
		return HDLPrimitives.getInstance().getShiftOpType(this).result;
	}

	public HDLType HDLEqualityOp.determineType() {
		return HDLPrimitives.getInstance().getEqualityOpType(this).result;
	}
	
	public HDLType HDLTernary.determineType(){
		return getThenExpr().determineType();
	}
	
	public HDLType HDLInlineFunction.determineType() {
		throw new HDLProblemException(new Problem(ErrorCode.INLINE_FUNCTION_NO_TYPE, this));
	}
}
