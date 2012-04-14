package de.tuhh.ict.pshdl.generator.vhdl;


import java.util.*;
import java.util.Map.*;

import de.tuhh.ict.pshdl.generator.vhdl.libraries.*;
import de.tuhh.ict.pshdl.model.*;
import de.upb.hni.vmagic.*;
import de.upb.hni.vmagic.Range.Direction;
import de.upb.hni.vmagic.builtin.*;
import de.upb.hni.vmagic.declaration.*;
import de.upb.hni.vmagic.statement.*;
import de.upb.hni.vmagic.expression.*;
import de.upb.hni.vmagic.literal.*;
import de.upb.hni.vmagic.object.*;
import de.upb.hni.vmagic.type.*;

public aspect VHDLStatementTransformation {
	public VHDLContext HDLStatement.toVHDL(){
		return new VHDLContext();
	}
	
	public VHDLContext HDLVariableDeclaration.toVHDL(){
		VHDLContext res=new VHDLContext();
//		HDLPrimitive primitive=getPrimitive();
//		SubtypeIndication type;
//		if (primitive!=null){
//			type = VHDLCastsLibrary.getType(primitive.getType());
//			if (getArray().size()>0){
//				
//			}
//		} else {
//			
//		}
		return res;
	}
	
	public VHDLContext HDLAssignment.toVHDL(){
		VHDLContext context = new VHDLContext();
		SignalAssignment sa=new SignalAssignment((SignalAssignmentTarget)getLeft().toVHDL(),getRight().toVHDL());
		HDLReference ref=getLeft();
		HDLRegisterConfig config = ref.resolveVar().getRegisterConfig();
		if (config!=null)
			context.addClockedStatement(config, sa);
		else
			context.addUnclockedStatement(sa);
		return context;
	}
	
	public VHDLContext HDLForLoop.toVHDL(){
		VHDLContext context=new VHDLContext();
		for(HDLStatement stmnt:getDos()){
			context.merge(stmnt.toVHDL());
		}
		VHDLContext res=new VHDLContext();
		for (Entry<HDLRegisterConfig, LinkedList<SequentialStatement>> e : context.clockedStatements.entrySet()) {
			ForStatement fStmnt=new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(e.getValue());
			res.addClockedStatement(e.getKey(), fStmnt);
		}
		if (context.unclockedStatements.size()>0){
			ForStatement fStmnt=new ForStatement(getParam().getName(), getRange().get(0).toVHDL(Direction.TO));
			fStmnt.getStatements().addAll(context.unclockedStatements);
			res.addUnclockedStatement(fStmnt);
		}
		return res;
	}
	
	public VHDLContext HDLIfStatement.toVHDL(){
		VHDLContext thenCtx=new VHDLContext();
		for(HDLStatement stmnt:getThenDo()){
			thenCtx.merge(stmnt.toVHDL());
		}
		VHDLContext elseCtx=new VHDLContext();
		for(HDLStatement stmnt:getElseDo()){
			elseCtx.merge(stmnt.toVHDL());
		}
		Set<HDLRegisterConfig> configs=new HashSet<HDLRegisterConfig>();
		configs.addAll(thenCtx.clockedStatements.keySet());
		configs.addAll(elseCtx.clockedStatements.keySet());
		VHDLContext res=new VHDLContext();
		Expression<?> ifExp=getIfExp().toVHDL();
		for (HDLRegisterConfig config : configs) {
			IfStatement ifs=new IfStatement(ifExp);
			if (thenCtx.clockedStatements.get(config)!=null)
				ifs.getStatements().addAll(thenCtx.clockedStatements.get(config));
			if (elseCtx.clockedStatements.get(config)!=null)
				ifs.getElseStatements().addAll(elseCtx.clockedStatements.get(config));
			res.addClockedStatement(config, ifs);
		}
		if (thenCtx.unclockedStatements.size()!=0 || elseCtx.unclockedStatements.size()!=0){
			IfStatement ifs=new IfStatement(ifExp);
			ifs.getStatements().addAll(thenCtx.unclockedStatements);
			ifs.getElseStatements().addAll(elseCtx.unclockedStatements);
			res.addUnclockedStatement(ifs);
		}
		return res;
	}
}
