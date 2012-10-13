package de.tuhh.ict.pshdl.model.aspects;

import java.math.*;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.aspects.ConstantEvaluation.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.HDLObject.*;
import de.tuhh.ict.pshdl.model.HDLArithOp.*;
import de.tuhh.ict.pshdl.model.HDLManip.*;
import de.tuhh.ict.pshdl.model.HDLBitOp.*;
import de.tuhh.ict.pshdl.model.HDLShiftOp.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.*;
import de.tuhh.ict.pshdl.model.types.builtIn.*;
import de.tuhh.ict.pshdl.model.types.builtIn.HDLBuiltInAnnotationProvider.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.*;

public aspect RangeApsect {

	public enum ProblemSource implements MetaAccess<HDLObject> {
		SOURCE;
	}

	public enum ProblemDescription implements MetaAccess<ProblemDescription> {
		DESCRIPTION, NON_PRIMITIVE_TYPE, POSSIBLY_ZERO_DIVIDE, BOOL_TYPE, ZERO_DIVIDE, BIT_TYPE;
	}

	//First we check whether this Expression might be Constant. If so, we do exactly know the range
	ValueRange around(HDLExpression from, HDLEvaluationContext context):execution(ValueRange HDLExpression+.determineRange(HDLEvaluationContext)) && this(from) && args(context){
		BigInteger constant=from.constantEvaluate(context);
		if (constant!=null)
			return new ValueRange(constant, constant);
		ValueRange res = proceed(from, context);
//		System.out.println("Result of evaluating:" + thisJoinPoint.getTarget() + " was " + res);
		return res;
	}

	public ValueRange HDLExpression.determineRange(HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public ValueRange HDLLiteral.determineRange(HDLEvaluationContext context) {
		return new ValueRange(getValueAsBigInt(), getValueAsBigInt());
	}

	public ValueRange HDLVariableRef.determineRange(HDLEvaluationContext context) {
		BigInteger val = this.constantEvaluate(context);
		if (val != null)
			return new ValueRange(val, val);
		HDLVariable var=resolveVar();
		HDLAnnotation range=var.getAnnotation(HDLBuiltInAnnotations.range);
		if (range!=null){
			String value[]=range.getValue().split(";");
			//TODO Allow simple references
			return new ValueRange(new BigInteger(value[0]), new BigInteger(value[1]));
		}
		if (var.getContainer()!=null) {
			if (var.getContainer() instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hvd=(HDLVariableDeclaration)var.getContainer();
				range=hvd.getAnnotation(HDLBuiltInAnnotations.range);
				if (range!=null){
					String value[]=range.getValue().split(";");
					//TODO Allow simple references
					return new ValueRange(new BigInteger(value[0]), new BigInteger(value[1]));
				}
			}
			if (var.getContainer() instanceof HDLForLoop){
				HDLForLoop loop=(HDLForLoop)var.getContainer();
				ValueRange res=loop.getRange().get(0).determineRange(context);
				for (HDLRange r:loop.getRange()){
					res=res.or(r.determineRange(context));
				}
				return res;
			}
		}
		if (getBits().size()>0){
			BigInteger bitWidth=BigInteger.ZERO;
			for (HDLRange r : getBits()) {
				HDLExpression width=r.getWidth();
				width.setContainer(r);
				BigInteger cw=width.constantEvaluate(context);
				if (cw==null) {
					bitWidth=null;
					break;
				}
				bitWidth=bitWidth.add(cw);
			}
			if (bitWidth!=null){
				return new ValueRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE));
			}
		}
		HDLType type = var.determineType();
		if (type instanceof HDLPrimitive) {
			return HDLPrimitives.getInstance().getValueRange((HDLPrimitive) type, context);
		}
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITIVE_TYPE);
		return null;
	}

	public ValueRange HDLRange.determineRange(HDLEvaluationContext context) {
		if (getFrom()!=null){
			return new ValueRange(getFrom().constantEvaluate(context), getTo().constantEvaluate(context));
		}
		return new ValueRange(getTo().constantEvaluate(context),getTo().constantEvaluate(context));
	}
	
	public ValueRange HDLEqualityOp.determineRange(HDLEvaluationContext context) {
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
		return new ValueRange(BigInteger.ZERO, BigInteger.ONE);
	}

	public ValueRange HDLShiftOp.determineRange(HDLEvaluationContext context) {
		ValueRange leftRange = getLeft().determineRange(context);
		ValueRange rightRange = getRight().determineRange(context);
		switch (getType()) {
		case SLL: {
			BigInteger ff = leftRange.from.shiftLeft(rightRange.from.intValue());
			BigInteger ft = leftRange.from.shiftLeft(rightRange.to.intValue());
			BigInteger tf = leftRange.to.shiftLeft(rightRange.from.intValue());
			BigInteger tt = leftRange.to.shiftLeft(rightRange.to.intValue());
			return new ValueRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case SRA: {
			BigInteger ff = leftRange.from.shiftRight(rightRange.from.intValue());
			BigInteger ft = leftRange.from.shiftRight(rightRange.to.intValue());
			BigInteger tf = leftRange.to.shiftRight(rightRange.from.intValue());
			BigInteger tt = leftRange.to.shiftRight(rightRange.to.intValue());
			return new ValueRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case SRL: {
			BigInteger ff = srl(leftRange.from, rightRange.from);
			BigInteger ft = srl(leftRange.from, rightRange.to);
			BigInteger tf = srl(leftRange.to, rightRange.from);
			BigInteger tt = srl(leftRange.to, rightRange.to);
			return new ValueRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	private static BigInteger srl(BigInteger a, BigInteger b) {
		BigInteger res = a.shiftRight(b.intValue());
		if (res.signum() < 0)
			return res.negate();
		return res;
	}

	public ValueRange HDLBitOp.determineRange(HDLEvaluationContext context) {
		ValueRange leftRange = getLeft().determineRange(context);
		ValueRange rightRange = getRight().determineRange(context);
		switch (getType()) {
		case OR:
		case XOR:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_TYPE);
			return new ValueRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(leftRange.to.bitLength()).subtract(BigInteger.ONE));
		case AND:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_TYPE);
			return new ValueRange(BigInteger.ZERO, leftRange.to.min(BigInteger.ONE.shiftLeft(rightRange.to.bitLength()).subtract(BigInteger.ONE)));
		case LOGI_AND:
		case LOGI_OR:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
			return new ValueRange(BigInteger.ZERO, BigInteger.ONE);
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	// See http://en.wikipedia.org/wiki/Interval_arithmetic#Simple_arithmetic
	@SuppressWarnings("fallthrough")
	public ValueRange HDLArithOp.determineRange(HDLEvaluationContext context) {
		ValueRange leftRange = getLeft().determineRange(context);
		ValueRange rightRange = getRight().determineRange(context);
		switch (getType()) {
		case PLUS:
			return new ValueRange(leftRange.from.add(rightRange.from), leftRange.to.add(rightRange.to));
		case MINUS:
			return new ValueRange(leftRange.from.subtract(rightRange.from), leftRange.to.subtract(rightRange.to));
		case DIV:
			if (rightRange.from.equals(BigInteger.ZERO) || rightRange.to.equals(BigInteger.ZERO)) {
				this.addMeta(ProblemSource.SOURCE, this);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
				return null;
			}
			if (rightRange.from.signum() * rightRange.to.signum() < 0 || rightRange.to.signum() == 0) {
				this.addMeta(ProblemSource.SOURCE, this);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
			}
			rightRange = new ValueRange(BigInteger.ONE.divide(rightRange.from), BigInteger.ONE.divide(rightRange.to));
		case MUL: {
			BigInteger ff = leftRange.from.multiply(rightRange.from);
			BigInteger ft = leftRange.from.multiply(rightRange.to);
			BigInteger tf = leftRange.to.multiply(rightRange.from);
			BigInteger tt = leftRange.to.multiply(rightRange.to);
			return new ValueRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case MOD:
			return new ValueRange(BigInteger.ZERO, rightRange.to.subtract(BigInteger.ONE).min(leftRange.to));
		case POW: {
			BigInteger ff = leftRange.from.pow(rightRange.from.intValue());
			BigInteger ft = leftRange.from.pow(rightRange.to.intValue());
			BigInteger tf = leftRange.to.pow(rightRange.from.intValue());
			BigInteger tt = leftRange.to.pow(rightRange.to.intValue());
			return new ValueRange(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public ValueRange HDLEnumRef.determineRange(HDLEvaluationContext context) {
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITIVE_TYPE);
		return null;
	}

	public ValueRange HDLManip.determineRange(HDLEvaluationContext context) {
		ValueRange right = getTarget().determineRange(context);
		switch (getType()) {
		case CAST:
			HDLType type = getCastTo();
			if (type instanceof HDLPrimitive) {
				ValueRange castRange = HDLPrimitives.getInstance().getValueRange((HDLPrimitive) type, context);
				return castRange.and(right);
			}
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITIVE_TYPE);
			return null;
		case ARITH_NEG:
			return new ValueRange(right.to.negate(), right.from.negate());
		case BIT_NEG:
			return new ValueRange(BigInteger.ZERO, BigInteger.ONE.shiftLeft(right.to.bitLength()).subtract(BigInteger.ONE));
		case LOGIC_NEG:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
			return new ValueRange(BigInteger.ZERO, BigInteger.ONE);
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public ValueRange HDLFunction.determineRange(HDLEvaluationContext context) {
		return HDLFunctions.determineRange(this, context);
	}

	public ValueRange HDLConcat.determineRange(HDLEvaluationContext context) {
		return HDLPrimitives.getInstance().getValueRange(this.determineType(), context);
	}
}
