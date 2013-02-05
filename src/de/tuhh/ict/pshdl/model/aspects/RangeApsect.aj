package de.tuhh.ict.pshdl.model.aspects;

import java.math.*;

import java.util.*;

import com.google.common.collect.*;

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

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public enum ProblemDescription implements MetaAccess<ProblemDescription> {
		DESCRIPTION, NON_PRIMITIVE_TYPE, POSSIBLY_ZERO_DIVIDE, BOOL_TYPE, ZERO_DIVIDE, BIT_TYPE;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	//First we check whether this Expression might be Constant. If so, we do exactly know the range
	Range<BigInteger> around(HDLExpression from, HDLEvaluationContext context):execution(ValueRange HDLExpression+.determineRange(HDLEvaluationContext)) && this(from) && args(context){
		BigInteger constant=from.constantEvaluate(context);
		if (constant!=null)
			return Ranges.closed(constant, constant);
		Range<BigInteger> res = proceed(from, context);
//		System.out.println("Result of evaluating:" + thisJoinPoint.getTarget() + " was " + res);
		return res;
	}

	public Range<BigInteger>HDLExpression.determineRange(HDLEvaluationContext context) {
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public Range<BigInteger>HDLLiteral.determineRange(HDLEvaluationContext context) {
		return Ranges.closed(getValueAsBigInt(), getValueAsBigInt());
	}

	public Range<BigInteger>HDLVariableRef.determineRange(HDLEvaluationContext context) {
		BigInteger val = this.constantEvaluate(context);
		if (val != null)
			return Ranges.closed(val, val);
		HDLVariable var=resolveVar();
		HDLAnnotation range=var.getAnnotation(HDLBuiltInAnnotations.range);
		if (range!=null){
			String value[]=range.getValue().split(";");
			//TODO Allow simple references
			return Ranges.closed(new BigInteger(value[0]), new BigInteger(value[1]));
		}
		if (var.getContainer()!=null) {
			if (var.getContainer() instanceof HDLVariableDeclaration) {
				HDLVariableDeclaration hvd=(HDLVariableDeclaration)var.getContainer();
				range=hvd.getAnnotation(HDLBuiltInAnnotations.range);
				if (range!=null){
					String value[]=range.getValue().split(";");
					//TODO Allow simple references
					return Ranges.closed(new BigInteger(value[0]), new BigInteger(value[1]));
				}
			}
			if (var.getContainer() instanceof HDLForLoop){
				HDLForLoop loop=(HDLForLoop)var.getContainer();
				Range<BigInteger> res=loop.getRange().get(0).determineRange(context);
				for (HDLRange r:loop.getRange()){
					res=res.span(r.determineRange(context));
				}
				return res;
			}
		}
		if (getBits().size()>0){
			BigInteger bitWidth=BigInteger.ZERO;
			for (HDLRange r : getBits()) {
				HDLExpression width=r.getWidth();
				width=width.copyDeepFrozen(r);
				BigInteger cw=width.constantEvaluate(context);
				if (cw==null) {
					bitWidth=null;
					break;
				}
				bitWidth=bitWidth.add(cw);
			}
			if (bitWidth!=null){
				return Ranges.closed(BigInteger.ZERO, BigInteger.ONE.shiftLeft(bitWidth.intValue()).subtract(BigInteger.ONE));
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

	public Range<BigInteger>HDLRange.determineRange(HDLEvaluationContext context) {
		BigInteger to = getTo().constantEvaluate(context);
		if (getFrom()!=null){
			BigInteger from = getFrom().constantEvaluate(context);
			if (from.compareTo(to)>0)
				return Ranges.closed(to, from);
			return Ranges.closed(from, to);
		}
		return Ranges.closed(to,to);
	}
	
	public Range<BigInteger>HDLEqualityOp.determineRange(HDLEvaluationContext context) {
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
		return Ranges.closed(BigInteger.ZERO, BigInteger.ONE);
	}

	public Range<BigInteger>HDLShiftOp.determineRange(HDLEvaluationContext context) {
		Range<BigInteger> leftRange = getLeft().determineRange(context);
		Range<BigInteger> rightRange = getRight().determineRange(context);
		switch (getType()) {
		case SLL: {
			BigInteger ff = leftRange.lowerEndpoint().shiftLeft(rightRange.lowerEndpoint().intValue());
			BigInteger ft = leftRange.lowerEndpoint().shiftLeft(rightRange.upperEndpoint().intValue());
			BigInteger tf = leftRange.upperEndpoint().shiftLeft(rightRange.lowerEndpoint().intValue());
			BigInteger tt = leftRange.upperEndpoint().shiftLeft(rightRange.upperEndpoint().intValue());
			return Ranges.closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case SRA: {
			BigInteger ff = leftRange.lowerEndpoint().shiftRight(rightRange.lowerEndpoint().intValue());
			BigInteger ft = leftRange.lowerEndpoint().shiftRight(rightRange.upperEndpoint().intValue());
			BigInteger tf = leftRange.upperEndpoint().shiftRight(rightRange.lowerEndpoint().intValue());
			BigInteger tt = leftRange.upperEndpoint().shiftRight(rightRange.upperEndpoint().intValue());
			return Ranges.closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case SRL: {
			BigInteger ff = srl(leftRange.lowerEndpoint(), rightRange.lowerEndpoint());
			BigInteger ft = srl(leftRange.lowerEndpoint(), rightRange.upperEndpoint());
			BigInteger tf = srl(leftRange.upperEndpoint(), rightRange.lowerEndpoint());
			BigInteger tt = srl(leftRange.upperEndpoint(), rightRange.upperEndpoint());
			return Ranges.closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
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

	public Range<BigInteger>HDLBitOp.determineRange(HDLEvaluationContext context) {
		Range<BigInteger> leftRange = getLeft().determineRange(context);
		Range<BigInteger> rightRange = getRight().determineRange(context);
		switch (getType()) {
		case OR:
		case XOR:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_TYPE);
			return Ranges.closed(BigInteger.ZERO, BigInteger.ONE.shiftLeft(leftRange.upperEndpoint().bitLength()).subtract(BigInteger.ONE));
		case AND:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BIT_TYPE);
			return Ranges.closed(BigInteger.ZERO, leftRange.upperEndpoint().min(BigInteger.ONE.shiftLeft(rightRange.upperEndpoint().bitLength()).subtract(BigInteger.ONE)));
		case LOGI_AND:
		case LOGI_OR:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
			return Ranges.closed(BigInteger.ZERO, BigInteger.ONE);
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	// See http://en.wikipedia.org/wiki/Interval_arithmetic#Simple_arithmetic
	@SuppressWarnings("fallthrough")
	public Range<BigInteger>HDLArithOp.determineRange(HDLEvaluationContext context) {
		Range<BigInteger> leftRange = getLeft().determineRange(context);
		Range<BigInteger> rightRange = getRight().determineRange(context);
		switch (getType()) {
		case PLUS:
			return Ranges.closed(leftRange.lowerEndpoint().add(rightRange.lowerEndpoint()), leftRange.upperEndpoint().add(rightRange.upperEndpoint()));
		case MINUS:
			return Ranges.closed(leftRange.lowerEndpoint().subtract(rightRange.lowerEndpoint()), leftRange.upperEndpoint().subtract(rightRange.upperEndpoint()));
		case DIV:
			if (rightRange.lowerEndpoint().equals(BigInteger.ZERO) || rightRange.upperEndpoint().equals(BigInteger.ZERO)) {
				this.addMeta(ProblemSource.SOURCE, this);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.ZERO_DIVIDE);
				return null;
			}
			if (rightRange.lowerEndpoint().signum() * rightRange.upperEndpoint().signum() < 0 || rightRange.upperEndpoint().signum() == 0) {
				this.addMeta(ProblemSource.SOURCE, this);
				this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.POSSIBLY_ZERO_DIVIDE);
			}
			rightRange = Ranges.closed(BigInteger.ONE.divide(rightRange.lowerEndpoint()), BigInteger.ONE.divide(rightRange.upperEndpoint()));
		case MUL: {
			BigInteger ff = leftRange.lowerEndpoint().multiply(rightRange.lowerEndpoint());
			BigInteger ft = leftRange.lowerEndpoint().multiply(rightRange.upperEndpoint());
			BigInteger tf = leftRange.upperEndpoint().multiply(rightRange.lowerEndpoint());
			BigInteger tt = leftRange.upperEndpoint().multiply(rightRange.upperEndpoint());
			return Ranges.closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		case MOD:
			return Ranges.closed(BigInteger.ZERO, rightRange.upperEndpoint().subtract(BigInteger.ONE).min(leftRange.upperEndpoint()));
		case POW: {
			BigInteger ff = leftRange.lowerEndpoint().pow(rightRange.lowerEndpoint().intValue());
			BigInteger ft = leftRange.lowerEndpoint().pow(rightRange.upperEndpoint().intValue());
			BigInteger tf = leftRange.upperEndpoint().pow(rightRange.lowerEndpoint().intValue());
			BigInteger tt = leftRange.upperEndpoint().pow(rightRange.upperEndpoint().intValue());
			return Ranges.closed(ff.min(ft).min(tf).min(tt), ff.max(ft).max(tf).max(tt));
		}
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public Range<BigInteger>HDLEnumRef.determineRange(HDLEvaluationContext context) {
		this.addMeta(ProblemSource.SOURCE, this);
		this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITIVE_TYPE);
		return null;
	}

	public Range<BigInteger>HDLManip.determineRange(HDLEvaluationContext context) {
		Range<BigInteger> right = getTarget().determineRange(context);
		switch (getType()) {
		case CAST:
			HDLType type = getCastTo();
			if (type instanceof HDLPrimitive) {
				Range<BigInteger> castRange = HDLPrimitives.getInstance().getValueRange((HDLPrimitive) type, context);
				return castRange.intersection(right);
			}
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.NON_PRIMITIVE_TYPE);
			return null;
		case ARITH_NEG:
			return Ranges.closed(right.upperEndpoint().negate(), right.lowerEndpoint().negate());
		case BIT_NEG:
			return Ranges.closed(BigInteger.ZERO, BigInteger.ONE.shiftLeft(right.upperEndpoint().bitLength()).subtract(BigInteger.ONE));
		case LOGIC_NEG:
			this.addMeta(ProblemSource.SOURCE, this);
			this.addMeta(ProblemDescription.DESCRIPTION, ProblemDescription.BOOL_TYPE);
			return Ranges.closed(BigInteger.ZERO, BigInteger.ONE);
		}
		throw new RuntimeException("Incorrectly implemented this op");
	}

	public Range<BigInteger>HDLFunctionCall.determineRange(HDLEvaluationContext context) {
		return HDLFunctions.determineRange(this, context);
	}

	public Range<BigInteger>HDLConcat.determineRange(HDLEvaluationContext context) {
		return HDLPrimitives.getInstance().getValueRange((HDLPrimitive)this.determineType(), context);
	}
}
