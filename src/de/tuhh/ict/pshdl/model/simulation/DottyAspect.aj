package de.tuhh.ict.pshdl.model.simulation;
import java.util.*;

import de.tuhh.ict.pshdl.model.*;

public aspect DottyAspect {
	public abstract String HDLExpression.toDottyLabel();
	public String HDLOpExpression.toDottyLabel(){
		return getType().toString();
	}
	public String HDLFunction.toDottyLabel(){
		return getName();
	}
	public String HDLLiteral.toDottyLabel(){
		return getVal();
	}
	public String HDLConcat.toDottyLabel(){
		return "#";
	}
	public String HDLReference.toDottyLabel(){
		return toString();
	}
	public String HDLManip.toDottyLabel(){
		return toString();
	}
	
	
	public static String connect(HDLObject obj){
		return obj.containerID+"->"+obj.getContainer().containerID+"; //"+obj;
	}
	public abstract String HDLExpression.toDottyConnection();
	public String HDLOpExpression.toDottyConnection(){
		int superContainer=getContainer().containerID;
		return getLeft().containerID+"->"+superContainer+";"+getLeft().containerID+"->"+superContainer+"; //"+toString();
	}
	public String HDLFunction.toDottyConnection(){
		StringBuilder sb=new StringBuilder();
		int superContainer=getContainer().containerID;
		for(HDLExpression exp:getParams())
			sb.append(exp.containerID).append("->").append(superContainer).append(';');
		sb.append("//").append(toString());
		return sb.toString();
	}
	public String HDLLiteral.toDottyConnection(){
		return getVal();
	}
	public String HDLConcat.toDottyConnection(){
		return "#";
	}
	public String HDLReference.toDottyConnection(){
		return toString();
	}
	public String HDLManip.toDottyConnection(){
		return toString();
	}
}
