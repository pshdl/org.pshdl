package de.tuhh.ict.pshdl.generator.vhdl;

import java.util.*;

import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.*;
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

	public Name<?> HDLVariableRef.toVHDL() {
		Name<?> result = new Signal(getVarRefName().getLastSegment(), UnresolvedType.NO_NAME);
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
		Name<?> result = new Signal(getHIfRefName().getLastSegment() + "_" + getVarRefName().getLastSegment(), UnresolvedType.NO_NAME);
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
//			if (getTarget().getClassType() == HDLClass.HDLLiteral) {
//				switch (targetType.getType()){
//				
//				}
//			} else {
				HDLPrimitive t=(HDLPrimitive)getTarget().determineType();
				Expression<?> exp = VHDLCastsLibrary.cast(vhdl, t.getType(), targetType.getType());
				if (targetType.getWidth() != null) {
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
					resize.getParameters().add(new AssociationElement(exp));
					resize.getParameters().add(new AssociationElement(width));
					return resize;
				}
				return exp;
//			}
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Range HDLRange.toVHDL(Direction dir) {
		Expression<?> to = getTo().toVHDL();
		if (getFrom() == null)
			return new Range(to, dir, to);
		return new Range(getFrom().toVHDL(), dir, to);
	}

	public Literal<?> HDLLiteral.toVHDL() {
		String val = getVal();
		if (val.length() == 1)
			return new DecimalLiteral(val);
		switch (val.charAt(1)) {
		case 'b':
			return new BinaryLiteral(val.substring(2));
		case 'x':
			return new HexLiteral(val.substring(2));
		default:
			return new DecimalLiteral(val);
		}
	}

	public Expression<?> HDLShiftOp.toVHDL() {
		switch (getType()) {
		case SLL:
			return new Sll(getLeft().toVHDL(), getRight().toVHDL());
		case SRA:
			return new Sra(getLeft().toVHDL(), getRight().toVHDL());
		case SRL:
			return new Srl(getLeft().toVHDL(), getRight().toVHDL());
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Expression<?> HDLEqualityOp.toVHDL() {
		switch (getType()) {
		case EQ:
			return new Equals(getLeft().toVHDL(), getRight().toVHDL());
		case GREATER:
			return new GreaterEquals(getLeft().toVHDL(), getRight().toVHDL());
		case GREATER_EQ:
			return new GreaterThan(getLeft().toVHDL(), getRight().toVHDL());
		case LESS:
			return new LessEquals(getLeft().toVHDL(), getRight().toVHDL());
		case LESS_EQ:
			return new LessThan(getLeft().toVHDL(), getRight().toVHDL());
		case NOT_EQ:
			return new NotEquals(getLeft().toVHDL(), getRight().toVHDL());
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Expression<?> HDLBitOp.toVHDL() {
		switch (getType()) {
		case AND:
		case LOGI_AND:
			return new And(getLeft().toVHDL(), getRight().toVHDL());
		case OR:
		case LOGI_OR:
			return new Or(getLeft().toVHDL(), getRight().toVHDL());
		case XOR:
			return new Xor(getLeft().toVHDL(), getRight().toVHDL());
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}

	public Expression<?> HDLArithOp.toVHDL() {
		switch (getType()) {
		case PLUS:
			return new Add(getLeft().toVHDL(), getRight().toVHDL());
		case MINUS:
			return new Subtract(getLeft().toVHDL(), getRight().toVHDL());
		case DIV:
			return new Divide(getLeft().toVHDL(), getRight().toVHDL());
		case MUL:
			return new Multiply(getLeft().toVHDL(), getRight().toVHDL());
		case MOD:
			return new Mod(getLeft().toVHDL(), getRight().toVHDL());
		case POW:
			return new Pow(getLeft().toVHDL(), getRight().toVHDL());
		}
		throw new IllegalArgumentException("Not supported:" + this);
	}
}
