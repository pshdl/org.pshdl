package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.VHDLPackageTransformation.*;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.VHDLCastsLibrary.TargetType;
import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLLiteral.HDLLiteralPresentation;
import de.tuhh.ict.pshdl.model.HDLPrimitive.HDLPrimitiveType;
import de.tuhh.ict.pshdl.model.HDLPrimitive.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.type.*;

public aspect VHDLExpressionTransformation {
	public abstract Expression<?> HDLExpression.toVHDL();

	public abstract Name<?> HDLReference.toVHDL();

	
	public String HDLVariableRef.getVHDLName(){
		return getVarRefName().getLastSegment();
	}
	public String HDLInterfaceRef.getVHDLName(){
		return getHIfRefName().getLastSegment()+"_"+getVarRefName().getLastSegment();
	}
	
	public Name<?> HDLVariableRef.toVHDL() {
		Name<?> result = new Signal(getVHDLName(), UnresolvedType.NO_NAME);
		result = getRef(result, this);
		return result;
	}

	private static Name<?> getRef(Name<?> result, HDLVariableRef ref) {
		if (ref.getArray().size() != 0) {
			@SuppressWarnings("rawtypes")
			List<Expression> indices = new LinkedList<Expression>();
			for (HDLExpression arr : ref.getArray()) {
				indices.add(arr.toVHDL());
			}
			result = new ArrayElement<Name<?>>(result, indices);
		}
		if (ref.getBits().size() > 0) {
			//TODO Make any directional access work (5:0, 0:5)
			if (ref.getBits().size() > 1)
				throw new IllegalArgumentException("Multi bit access not supported");
			HDLRange r = ref.getBits().get(0);
			if (r.getFrom() == null) {
				result = new ArrayElement<Name<?>>(result, r.getTo().toVHDL());
			} else {
				result = new Slice<Name<?>>(result, r.toVHDL(Direction.DOWNTO));
			}
		}
		return result;
	}

	public Name<?> HDLInterfaceRef.toVHDL() {
		Name<?> result = new Signal(getVHDLName(), UnresolvedType.NO_NAME);
		if (getIfArray().size() != 0) {
			@SuppressWarnings("rawtypes")
			List<Expression> indices = new LinkedList<Expression>();
			for (HDLExpression arr : getIfArray()) {
				indices.add(arr.toVHDL());
			}
			result = new ArrayElement<Name<?>>(result, indices);
		}
		return getRef(result, this);
	}

	public Expression<?> HDLFunctionCall.toVHDL() {
		return HDLFunctions.toVHDLExpression(this);
	}

	public Signal HDLEnumRef.toVHDL() {
		return new Signal(getVarRefName().getLastSegment(), UnresolvedType.NO_NAME);
	}

	public Expression<?> HDLConcat.toVHDL() {
		List<HDLExpression> cats = getCats();
		Expression<?> res = cats.get(0).toVHDL();
		for (int i = 1; i < cats.size(); i++) {
			res = new Concatenate(res, cats.get(i).toVHDL());
		}
		return res;
	}

	public Expression<?> HDLManip.toVHDL() {
		switch (getType()) {
		case ARITH_NEG:
			return new Minus(getTarget().toVHDL());
		case LOGIC_NEG:
		case BIT_NEG:
			return new Not(getTarget().toVHDL());
		case CAST:
			HDLPrimitive targetType = (HDLPrimitive) getCastTo();
			if (targetType.getType()==HDLPrimitiveType.STRING)
				return getTarget().toVHDL();
			HDLExpression tWidth = targetType.getWidth();
			if (getTarget().getClassType() == HDLClass.HDLLiteral) {
				return VHDLCastsLibrary.handleLiteral(getContainer(), (HDLLiteral) getTarget(), targetType, tWidth);
			}
			HDLPrimitive t = (HDLPrimitive) getTarget().determineType();
			Expression<?> exp = getTarget().toVHDL();
			HDLPrimitiveType actualType=t.getType();
			if (tWidth != null) {
				TargetType resized=VHDLCastsLibrary.getResize(exp, t, tWidth);
				exp=resized.resized;
				actualType=resized.newType;
			}
			return VHDLCastsLibrary.cast(exp, actualType, targetType.getType());
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}
	

	public Range HDLRange.toVHDL(Direction dir) {
		Expression<?> to = HDLPrimitives.simplifyWidth(this, getTo()).toVHDL();
		if (getFrom() == null)
			return new Range(to, dir, to);
		return new Range(HDLPrimitives.simplifyWidth(this, getFrom()).toVHDL(), dir, to);
	}

	public Literal<?> HDLLiteral.toVHDL() {
		int length=-1;
		if(getValueAsBigInt()!=null)
			length=getValueAsBigInt().bitLength();
		return toVHDL(length, false);
	}
	public Literal<?> HDLLiteral.toVHDL(int length, boolean asString) {
		String val = getVal();
		if (length==0)
			length=1;
		BigInteger dec=getValueAsBigInt();
		switch (getPresentation()){
		case STR:
			return new StringLiteral(val);
		case BOOL:
			if ("true".equals(val))
				return Standard.BOOLEAN_TRUE;
			return Standard.BOOLEAN_FALSE;
		case HEX:
			if (asString)
				return VHDLUtils.toHexLiteral(length, dec);
			return new BasedLiteral("16#"+val.substring(2)+"#");
		case BIN:
			if (asString)
				return VHDLUtils.toBinaryLiteral(length, dec);
			return new BasedLiteral("2#"+val.substring(2)+"#");
		default:
			//VHDL isn't smart enough to allow uints with 32 bit
			if (dec.bitLength()>31 || asString)
				return VHDLUtils.toBinaryLiteral(length, dec);
			return new DecimalLiteral(val);
		}
	}

	public Expression<?> HDLShiftOp.toVHDL() {
		HDLPrimitive type = (HDLPrimitive) getLeft().determineType();
		return VHDLShiftLibrary.shift(getLeft().toVHDL(), getRight().toVHDL(), type.getType(), getType());
	}

	public Expression<?> HDLEqualityOp.toVHDL() {
		switch (getType()) {
		case EQ:
			return new Parentheses(new Equals(getLeft().toVHDL(), getRight().toVHDL()));
		case GREATER_EQ:
			return new Parentheses(new GreaterEquals(getLeft().toVHDL(), getRight().toVHDL()));
		case GREATER:
			return new Parentheses(new GreaterThan(getLeft().toVHDL(), getRight().toVHDL()));
		case LESS_EQ:
			return new Parentheses(new LessEquals(getLeft().toVHDL(), getRight().toVHDL()));
		case LESS:
			return new Parentheses(new LessThan(getLeft().toVHDL(), getRight().toVHDL()));
		case NOT_EQ:
			return new Parentheses(new NotEquals(getLeft().toVHDL(), getRight().toVHDL()));
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Expression<?> HDLBitOp.toVHDL() {
		switch (getType()) {
		case AND:
		case LOGI_AND:
			return new Parentheses(new And(getLeft().toVHDL(), getRight().toVHDL()));
		case OR:
		case LOGI_OR:
			return new Parentheses(new Or(getLeft().toVHDL(), getRight().toVHDL()));
		case XOR:
			return new Parentheses(new Xor(getLeft().toVHDL(), getRight().toVHDL()));
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Expression<?> HDLArithOp.toVHDL() {
		switch (getType()) {
		case PLUS:
			return new Parentheses(new Add(getLeft().toVHDL(), getRight().toVHDL()));
		case MINUS:
			return new Parentheses(new Subtract(getLeft().toVHDL(), getRight().toVHDL()));
		case DIV:
			return new Parentheses(new Divide(getLeft().toVHDL(), getRight().toVHDL()));
		case MUL:
			return new Parentheses(new Multiply(getLeft().toVHDL(), getRight().toVHDL()));
		case MOD:
			return new Parentheses(new Rem(getLeft().toVHDL(), getRight().toVHDL()));
		case POW:
			return new Parentheses(new Pow(getLeft().toVHDL(), getRight().toVHDL()));
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}
	
	public Expression<?> HDLTernary.toVHDL(){
		FunctionCall fc = new FunctionCall(VHDLTypesLibrary.TERNARY_SLV);
		List<AssociationElement> parameters = fc.getParameters();
		parameters.add(new AssociationElement(getIfExpr().toVHDL()));
		parameters.add(new AssociationElement(getThenExpr().toVHDL()));
		parameters.add(new AssociationElement(getElseExpr().toVHDL()));
		return fc;
	}
	public Expression<?> HDLFunction.toVHDL(){
		throw new IllegalArgumentException("Not supported:" + this);
	}
}
