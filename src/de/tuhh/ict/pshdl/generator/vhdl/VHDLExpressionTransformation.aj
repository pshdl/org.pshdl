package de.tuhh.ict.pshdl.generator.vhdl;

import java.math.*;
import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.VHDLPackageTransformation.*;
import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.*;
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

	public FunctionCall HDLFunction.toVHDL() {
		FunctionDeclaration fd = new FunctionDeclaration(getName(), UnresolvedType.NO_NAME);
		FunctionCall res = new FunctionCall(fd);
		for (HDLExpression exp : getParams()) {
			res.getParameters().add(new AssociationElement(exp.toVHDL()));
		}
		return res;
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
		Expression<?> vhdl = getTarget().toVHDL();
		switch (getType()) {
		case ARITH_NEG:
			return new Minus(vhdl);
		case LOGIC_NEG:
		case BIT_NEG:
			return new Not(vhdl);
		case CAST:
			HDLPrimitive targetType = (HDLPrimitive) getCastTo();
			if (getTarget().getClassType() == HDLClass.HDLLiteral) {
				HDLLiteral lit = (HDLLiteral) getTarget();
				if (this.getContainer()!=null && this.getContainer().getClassType()==HDLClass.HDLArithOp)
					return lit.toVHDL();
				BigInteger val = lit.getValueAsBigInt();
				BigInteger width=null;
				if (targetType.getWidth()!=null)
					width=targetType.getWidth().constantEvaluate(null);
				FunctionCall resize = null;
				Expression<?> actual = new StringLiteral(val.toString(2));
				switch (targetType.getType()) {
				case BIT:
					if (BigInteger.ZERO.equals(val))
						return new CharacterLiteral('0');
					return new CharacterLiteral('1');
				case NATURAL:
				case INTEGER:
					return lit.toVHDL();
				case INT:{
					resize = new FunctionCall(NumericStd.TO_SIGNED);
					resize.getParameters().add(new AssociationElement(lit.toVHDL()));
					break;
				}
				case UINT:{
					resize = new FunctionCall(NumericStd.TO_UNSIGNED);
					resize.getParameters().add(new AssociationElement(lit.toVHDL()));
					break;
				}
				case BITVECTOR:
					if (width!=null)
						return VHDLUtils.toBinaryLiteral(width.intValue(), val);
					resize = new FunctionCall(VHDLCastsLibrary.RESIZE_SLV);
					resize.getParameters().add(new AssociationElement(actual));
					break;
				case BOOL:
					throw new IllegalArgumentException("Bool is not a literal");
				}
				if (resize==null)
					throw new IllegalArgumentException("Should not get here");
				resize.getParameters().add(new AssociationElement(targetType.getWidth().toVHDL()));
				return resize;
			}
			HDLPrimitive t = (HDLPrimitive) getTarget().determineType();
			Expression<?> exp = VHDLCastsLibrary.cast(vhdl, t.getType(), targetType.getType());
			HDLExpression tw=targetType.getWidth();
			if (tw != null) {
				if (t.getWidth()!=null){
					BigInteger bt=t.getWidth().constantEvaluate(null);
					if (bt!=null){
						BigInteger btw=tw.constantEvaluate(null);
						if (bt.equals(btw)){
							return exp;
						}
					}
				}
				Expression<?> width = targetType.getWidth().toVHDL();
				FunctionCall resize = null;
				switch (targetType.getType()) {
				case BOOL:
				case BIT:
				case INTEGER:
				case NATURAL:
					throw new IllegalArgumentException(targetType + " can't have a width.");
				case INT:
				case UINT:
					resize = new FunctionCall(NumericStd.RESIZE);
					break;
				case BITVECTOR:
					resize = new FunctionCall(VHDLCastsLibrary.RESIZE_SLV);
					break;
				}
				if (resize==null)
					throw new IllegalArgumentException("Should not happen");
				resize.getParameters().add(new AssociationElement(exp));
				resize.getParameters().add(new AssociationElement(width));
				return resize;
			}
			return exp;
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
		String val = getVal();
		if (val.length() == 1)
			return new DecimalLiteral(val);
		switch (val.charAt(1)) {
		case 'b':
			return new BasedLiteral("2#"+val.substring(2)+"#");
		case 'x':
			return new BasedLiteral("16#"+val.substring(2)+"#");
		default:
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
		case GREATER:
			return new Parentheses(new GreaterEquals(getLeft().toVHDL(), getRight().toVHDL()));
		case GREATER_EQ:
			return new Parentheses(new GreaterThan(getLeft().toVHDL(), getRight().toVHDL()));
		case LESS:
			return new Parentheses(new LessEquals(getLeft().toVHDL(), getRight().toVHDL()));
		case LESS_EQ:
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
		throw new IllegalArgumentException("Not supported:" + this);
	}
}
