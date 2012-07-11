package de.tuhh.ict.pshdl.model.aspects;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import org.aspectj.lang.reflect.*;

public abstract aspect LogginAspect {
	pointcut addClazz() : call (* HDLObject.addClazz(..));
	pointcut addMeta() : call (* HDLObject.addMeta(..));
	pointcut setContainer() : call (* HDLObject.setContainer(..));

	pointcut copy(HDLObject tar) : target(tar) &&call (* HDLObject+.copy(..));
	pointcut copyFiltered(HDLObject tar) : target(tar) && call (* HDLObject+.copyFiltered(..));
	pointcut setters(HDLObject tar):target(tar) && (call(* HDLObject+.set*(..)) && !setContainer());
	pointcut adder(HDLObject tar):target(tar) && (call(* HDLObject+.add*(..)) && !addClazz() && !addMeta());
	pointcut constructors(): call(HDLObject+.new(..));
	pointcut replaced(HDLObject src, HDLObject[] with): call(* ModificationSet.replace(..))&& args(src, with);

	public enum Infos implements MetaAccess<SourceLocation> {
		SETTER, SETTER_CONTAINER, ADDER, CONSTRUCTOR, COPY, COPY_FILTERED;
	}
	
	before(HDLObject src, HDLObject[] with) : replaced(src, with){
		src.addMeta("REPLACED_AT", thisJoinPointStaticPart.getSourceLocation());
		src.addMeta("REPLACED_WITH", with);
		for (HDLObject hdo : with) {
			hdo.addMeta("REPLACED", src);
		}
	}
	
	public static void HDLObject.printInfo(HDLObject obj) {
		if (obj.getMeta("CONSTRUCTOR") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " constructed at:" + obj.getMeta("CONSTRUCTOR"));
		}
		Object meta = obj.getMeta("CONSTRUCTION_SRC");
		if (meta != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " constructed in:" + meta.getClass());
			if (meta instanceof HDLObject)
				printInfo((HDLObject) meta);
		}
		if (obj.getMeta("SETTER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " setter called at:" + obj.getMeta("SETTER"));
		}
		HDLObject[] replaceWith = (HDLObject[])obj.getMeta("REPLACED_WITH");
		if (replaceWith != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " replaced with:" + replaceWith);
			System.out.println(">>>>");
			for (HDLObject hdlObject : replaceWith) {
				HDLObject.printInfo(hdlObject);
			}
			System.out.println("<<<<");
		}
		if (obj.getMeta("REPLACED_AT") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " replaced at:" + obj.getMeta("REPLACED_AT"));
		}
		if (obj.getMeta("SETTER_CONTAINER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " setContainer called at:" + obj.getMeta("SETTER_CONTAINER"));
		}
		if (obj.getMeta("ADDER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " add called at:" + obj.getMeta("ADDER"));
		}
		if (obj.getMeta("COPY") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copy called at:" + obj.getMeta("COPY"));
		}
		if (obj.getMeta("COPY_FILTERED") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copyFiltered called at:" + obj.getMeta("COPY_FILTERED"));
		}
		if (obj.getMeta("COPY_SOURCE") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj.containerID + " copied from:" + obj.getMeta("COPY_SOURCE"));
			printInfo((HDLObject) obj.getMeta("COPY_SOURCE"));
		}
	}

	before(HDLObject tar) : copy(tar){
		tar.addMeta(Infos.COPY, thisJoinPointStaticPart.getSourceLocation());
	}
	before(HDLObject tar) : copyFiltered(tar){
		tar.addMeta(Infos.COPY_FILTERED, thisJoinPointStaticPart.getSourceLocation());
	}
	after(HDLObject src) returning(HDLObject res) :  this(src) && execution (* HDLObject.copyFiltered(..)){
		res.addMeta("COPY_SOURCE", src);
	}
	after(HDLObject src) returning(HDLObject res) : this(src) && execution (* HDLObject.copy(..)){
		res.addMeta("COPY_SOURCE", src);
	}
	before(HDLObject tar) : setters(tar){
		tar.addMeta(Infos.SETTER, thisJoinPointStaticPart.getSourceLocation());
	}
	before(HDLObject tar) : target(tar) && setContainer(){
		tar.addMeta(Infos.SETTER_CONTAINER, thisJoinPointStaticPart.getSourceLocation());
	}
	before(HDLObject tar) : adder(tar){
		tar.addMeta(Infos.ADDER, thisJoinPointStaticPart.getSourceLocation());
	}
	after(HDLObject src) returning(HDLObject tar): this(src) && constructors(){
		tar.addMeta(Infos.CONSTRUCTOR, thisJoinPointStaticPart.getSourceLocation());
		tar.addMeta("CONSTRUCTION_SRC", src);
	}
}
