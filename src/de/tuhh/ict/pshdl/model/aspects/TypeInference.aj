package de.tuhh.ict.pshdl.model.aspects;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.HDLArithOpType;
import de.tuhh.ict.pshdl.model.types.builtIn.*;

public aspect TypeInference {
	
	public abstract HDLType HDLExpression.determineType();
	
	/**
	 * Attempt to determine the type of this HDLVariable. For this to work it
	 * needs to have a valid container.
	 * 
	 * @return the HDLType if it could be determined, <code>null</code>
	 *         otherwise.
	 */
	public HDLType HDLVariable.determineType() {
		HDLObject container = getContainer();
		if (container instanceof HDLVariableDeclaration) {
			HDLVariableDeclaration hvd = (HDLVariableDeclaration) container;
			return hvd.determineType();
		}
		if (container instanceof HDLDirectGeneration) {
			HDLDirectGeneration hdg = (HDLDirectGeneration) container;
			return hdg.getHIf();
		}
		if (container instanceof HDLInterfaceInstantiation) {
			HDLInterfaceInstantiation hii = (HDLInterfaceInstantiation) container;
			return hii.resolveHIf();
		}
		throw new IllegalArgumentException("Failed to resolve type of "+this+" caused by an unexpected container"+container);
	}
	
	public HDLType HDLVariableDeclaration.determineType(){
		if (getPrimitive()!=null)
			return getPrimitive();
		return resolveType();
	}
	
	public HDLPrimitive HDLConcat.determineType() {
		Iterator<HDLExpression> iter = getCats().iterator();
		HDLPrimitive type=(HDLPrimitive)iter.next().determineType();
		HDLExpression width = type.getWidth();
		while (iter.hasNext()) {
			type=(HDLPrimitive)iter.next().determineType();
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(type.getWidth());
		}
		return HDLPrimitive.getBitvector().setWidth(width).setContainer(this);
	}

	public HDLPrimitive HDLEnumRef.determineType() {
		return null;
	}
	
	public HDLType HDLManip.determineType() {
		return HDLPrimitives.getInstance().getManipOpType(this).result;
	}

	public HDLPrimitive HDLFunction.determineType() {
		// XXX Really good question!
		return HDLPrimitive.target(false);
	}

	public HDLPrimitive HDLLiteral.determineType() {
		// Actually depends on context
		return HDLPrimitive.target(getVal().charAt(0)!='-');
	}

	public HDLType HDLVariableRef.determineType() {
		if (getBits().size() == 0)
			return resolveVar().determineType();
		Iterator<HDLRange> iter = getBits().iterator();
		HDLExpression width = iter.next().getWidth();
		while (iter.hasNext()) {
			width = new HDLArithOp().setLeft(width).setType(HDLArithOpType.PLUS).setRight(iter.next().getWidth());
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
}
