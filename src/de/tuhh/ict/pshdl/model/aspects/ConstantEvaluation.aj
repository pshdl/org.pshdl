package de.tuhh.ict.pshdl.model.aspects;

import java.math.*;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.HDLObject.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.utils.*;

public aspect ConstantEvaluation {

	
	pointcut evaluate(IHDLObject obj): target(obj) && execution(BigInteger constantEvaluate(HDLEvaluationContext));
	
	private static enum EvalResult implements MetaAccess<BigInteger>{
		result;

		@Override
		public boolean inherit() {
			return false;
		}
	}
	private static enum EvalResultContainer implements MetaAccess<IHDLObject>{
		container;

		@Override
		public boolean inherit() {
			return false;
		}
	}

	BigInteger around(IHDLObject obj):evaluate(obj) {
		if (!obj.isFrozen())
			throw new IllegalArgumentException("Only frozen objects can be evaluated");
		BigInteger res=proceed(obj);
		return res; 
	}
	

	public enum ProblemSource implements MetaAccess<IHDLObject> {
		SOURCE;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public enum ProblemDescription implements MetaAccess<ProblemDescription> {
		DESCRIPTION, SUBEXPRESSION_DID_NOT_EVALUATE, SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE, ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS, BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS, SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT, CAN_NOT_USE_PARAMETER, ENUMS_NOT_SUPPORTED_FOR_CONSTANTS, NON_PRIMITVE_TYPE_NOT_EVALUATED, TYPE_NOT_SUPPORTED_FOR_CONSTANTS;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public abstract BigInteger HDLExpression.constantEvaluate(HDLEvaluationContext context);
	
	public BigInteger HDLTernary.constantEvaluate(HDLEvaluationContext context){
		BigInteger res=getIfExpr().constantEvaluate(context);
		if (res!=null){
			if (BigInteger.ZERO.equals(res)){
				return getElseExpr().constantEvaluate(context);
			}
			return getThenExpr().constantEvaluate(context);
		}
		this.addMeta(ProblemSource.SOURCE, getIfExpr());
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
		return null;
	}

	public BigInteger HDLLiteral.constantEvaluate(HDLEvaluationContext context) {
		switch (getPresentation()){
		case STR:
		case BOOL:
			return null;
		default:
			return getValueAsBigInt();
		}
	}

	public BigInteger HDLManip.constantEvaluate(HDLEvaluationContext context) {
		BigInteger eval = subEvaluate(this, getTarget(),context);
		if (eval == null) {
			return null;
		}
		switch (getType()) {
		case ARITH_NEG:
			return eval.negate();
		case BIT_NEG:
			return eval.not();
		case LOGIC_NEG:
			return boolInt(getTarget().constantEvaluate(context).equals(BigInteger.ZERO));
		case CAST:
			HDLType type = getCastTo();
			if (type instanceof HDLPrimitive) {
				HDLPrimitive prim = (HDLPrimitive) type;
				if (prim.getWidth() != null) {
					BigInteger width = prim.getWidth().constantEvaluate(context);
					if (width!=null)
						return eval.mod(BigInteger.ONE.shiftLeft(width.intValue()));
					return null;
				}
				return eval;
			}
			this.addMeta(ProblemSource.SOURCE, getTarget());
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITVE_TYPE_NOT_EVALUATED);
			return null;

		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	public BigInteger HDLConcat.constantEvaluate(HDLEvaluationContext context) {
		BigInteger sum = BigInteger.ZERO;
		for (HDLExpression cat : getCats()) {
			BigInteger im = subEvaluate(this, cat, context);
			if (im == null) {
				return null;
			}
			BigInteger width = cat.determineType().getWidth().constantEvaluate(context);
			if (width == null) {
				this.addMeta(ProblemSource.SOURCE, cat.determineType().getWidth());
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_WIDTH_DID_NOT_EVALUATE);
				return null;

			}
			sum = sum.shiftLeft(width.intValue()).or(im);
		}
		return sum;
	}

	public static BigInteger subEvaluate(HDLExpression container, HDLExpression left, HDLEvaluationContext context) {
		BigInteger leftVal = left.constantEvaluate(context);
		if (leftVal == null) {
			container.addMeta(ProblemSource.SOURCE, left);
			container.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE);
			return null;
		}
		return leftVal;
	}

	public BigInteger HDLArithOp.constantEvaluate(HDLEvaluationContext context) {
		BigInteger leftVal = subEvaluate(this, getLeft(), context);
		if (leftVal == null)
			return null;
		BigInteger rightVal = subEvaluate(this, getRight(), context);
		if (rightVal == null)
			return null;
		switch (getType()) {
		case DIV:
			return leftVal.divide(rightVal);
		case MUL:
			return leftVal.multiply(rightVal);
		case MINUS:
			return leftVal.subtract(rightVal);
		case PLUS:
			return leftVal.add(rightVal);
		case MOD:
			return leftVal.remainder(rightVal);
		case POW:
			return leftVal.pow(rightVal.intValue());
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	public BigInteger HDLBitOp.constantEvaluate(HDLEvaluationContext context) {
		BigInteger leftVal = subEvaluate(this, getLeft(), context);
		if (leftVal == null)
			return null;
		BigInteger rightVal = subEvaluate(this, getRight(), context);
		if (rightVal == null)
			return null;
		switch (getType()) {
		case AND:
			return leftVal.and(rightVal);
		case OR:
			return leftVal.or(rightVal);
		case XOR:
			return leftVal.xor(rightVal);
		case LOGI_AND: {
			boolean l = !BigInteger.ZERO.equals(leftVal);
			boolean r = !BigInteger.ZERO.equals(rightVal);
			return boolInt(l & r);
		}
		case LOGI_OR: {
			boolean l = !BigInteger.ZERO.equals(leftVal);
			boolean r = !BigInteger.ZERO.equals(rightVal);
			return boolInt(l | r);
		}
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	public BigInteger HDLEqualityOp.constantEvaluate(HDLEvaluationContext context) {
		BigInteger leftVal = subEvaluate(this, getLeft(), context);
		if (leftVal == null)
			return null;
		BigInteger rightVal = subEvaluate(this, getRight(), context);
		if (rightVal == null)
			return null;
		switch (getType()) {
		case EQ:
			return boolInt(leftVal.equals(rightVal));
		case NOT_EQ:
			return boolInt(!leftVal.equals(rightVal));
		case GREATER:
			return boolInt(leftVal.compareTo(rightVal) > 0);
		case GREATER_EQ:
			return boolInt(leftVal.compareTo(rightVal) >= 0);
		case LESS:
			return boolInt(leftVal.compareTo(rightVal) < 0);
		case LESS_EQ:
			return boolInt(leftVal.compareTo(rightVal) <= 0);
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	public BigInteger HDLShiftOp.constantEvaluate(HDLEvaluationContext context) {
		BigInteger leftVal = subEvaluate(this, getLeft(), context);
		if (leftVal == null)
			return null;
		BigInteger rightVal = subEvaluate(this, getRight(), context);
		if (rightVal == null)
			return null;
		switch (getType()) {
		case SLL:
			return leftVal.shiftLeft(rightVal.intValue());
		case SRA:
			return leftVal.shiftRight(rightVal.intValue());
		case SRL:
			BigInteger shiftRight = leftVal.shiftRight(rightVal.intValue());
			if (shiftRight.signum() < 0)
				//XXX This is incorrect. We have to know the width of the
				return shiftRight.negate();
			return shiftRight;
		}
		throw new RuntimeException("Incorrectly implemented constant evaluation!");
	}

	public BigInteger HDLFunctionCall.constantEvaluate(HDLEvaluationContext context) {
		List<BigInteger> args = new LinkedList<BigInteger>();
		for (HDLExpression arg : getParams()) {
			BigInteger val = subEvaluate(this, arg, context);
			if (val == null)
				return null;
			args.add(val);
		}
		return HDLFunctions.constantEvaluate(this, args, context);
	}

	public BigInteger HDLVariableRef.constantEvaluate(HDLEvaluationContext context) {
		if (getArray().size() != 0) {
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ARRAY_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
			return null;
		}
		if (getBits().size() != 0) {
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
			return null;
		}
		HDLType type = determineType();
		if (!(type instanceof HDLPrimitive)) {
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.TYPE_NOT_SUPPORTED_FOR_CONSTANTS);
			return null;
		}
		HDLVariable var = resolveVar();
		HDLDirection dir = resolveVar().getDirection();
		if (dir == HDLDirection.CONSTANT)
			return subEvaluate(this, var.getDefaultValue(), context);

		if (dir == HDLDirection.PARAMETER) {
			if (context == null) {
				this.addMeta(ProblemSource.SOURCE, this);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.CAN_NOT_USE_PARAMETER);
				return null;
			}
			HDLExpression cRef = context.get(var);
			BigInteger cRefEval = cRef.constantEvaluate(context);
			if (cRefEval == null) {
				this.addMeta(ProblemSource.SOURCE, cRef);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.SUBEXPRESSION_DID_NOT_EVALUATE_IN_THIS_CONTEXT);
				return null;
			}
			return cRefEval;
		}
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_ACCESS_NOT_SUPPORTED_FOR_CONSTANTS);
		return null;
	}

	public BigInteger HDLEnumRef.constantEvaluate(HDLEvaluationContext context) {
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ENUMS_NOT_SUPPORTED_FOR_CONSTANTS);
		return null;
	}

	private static BigInteger boolInt(boolean b) {
		return b ? BigInteger.ONE : BigInteger.ZERO;
	}
}
