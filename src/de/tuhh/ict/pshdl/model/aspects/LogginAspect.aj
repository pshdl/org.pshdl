package de.tuhh.ict.pshdl.model.aspects;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import org.aspectj.lang.reflect.*;

public abstract aspect LogginAspect {
	pointcut addClazz() : call (* IHDLObject.addClazz(..));
	pointcut addMeta() : call (* IHDLObject.addMeta(..));
	pointcut setContainer() : call (* IHDLObject.setContainer(..));

	pointcut copy(IHDLObject tar) : target(tar) &&call (* IHDLObject+.copy(..));
	pointcut copyFiltered(IHDLObject tar) : target(tar) && call (* IHDLObject+.copyFiltered(..));
	pointcut setters(IHDLObject tar):target(tar) && (call(* IHDLObject+.set*(..)) && !setContainer());
	pointcut adder(IHDLObject tar):target(tar) && (call(* IHDLObject+.add*(..)) && !addClazz() && !addMeta());
	pointcut constructors(): call(IHDLObject+.new(..));
	pointcut replaced(IHDLObject src, IHDLObject[] with): call(* ModificationSet.replace(..))&& args(src, with);

	public enum Infos implements MetaAccess<SourceLocation> {
		SETTER, SETTER_CONTAINER, ADDER, CONSTRUCTOR, COPY, COPY_FILTERED;
	}
	
	before(IHDLObject src, IHDLObject[] with) : replaced(src, with){
		src.addMeta("REPLACED_AT", thisJoinPointStaticPart.getSourceLocation());
		src.addMeta("REPLACED_WITH", with);
		for (IHDLObject hdo : with) {
			hdo.addMeta("REPLACED", src);
		}
	}
	
	public static void HDLObject.printInfo(IHDLObject iobj) {
		HDLObject obj=(HDLObject) iobj;
		if (obj.getMeta("CONSTRUCTOR") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " constructed at:" + obj.getMeta("CONSTRUCTOR"));
		}
		Object meta = obj.getMeta("CONSTRUCTION_SRC");
		if (meta != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " constructed in:" + meta.getClass());
			if (meta instanceof HDLObject)
				printInfo((HDLObject) meta);
		}
		if (obj.getMeta("SETTER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " setter called at:" + obj.getMeta("SETTER"));
		}
		IHDLObject[] replaceWith = (IHDLObject[])obj.getMeta("REPLACED_WITH");
		if (replaceWith != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " replaced with:" + replaceWith);
			System.out.println(">>>>");
			for (IHDLObject hdlObject : replaceWith) {
				HDLObject.printInfo(hdlObject);
			}
			System.out.println("<<<<");
		}
		if (obj.getMeta("REPLACED_AT") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " replaced at:" + obj.getMeta("REPLACED_AT"));
		}
		if (obj.getMeta("SETTER_CONTAINER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " setContainer called at:" + obj.getMeta("SETTER_CONTAINER"));
		}
		if (obj.getMeta("ADDER") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " add called at:" + obj.getMeta("ADDER"));
		}
		if (obj.getMeta("COPY") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " copy called at:" + obj.getMeta("COPY"));
		}
		if (obj.getMeta("COPY_FILTERED") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " copyFiltered called at:" + obj.getMeta("COPY_FILTERED"));
		}
		if (obj.getMeta("COPY_SOURCE") != null) {
			System.out.println("HDLObject.resolveVariable()" + obj + " copied from:" + obj.getMeta("COPY_SOURCE"));
			printInfo((HDLObject) obj.getMeta("COPY_SOURCE"));
		}
	}

	before(IHDLObject tar) : copy(tar){
		tar.addMeta(Infos.COPY, thisJoinPointStaticPart.getSourceLocation());
	}
	before(IHDLObject tar) : copyFiltered(tar){
		tar.addMeta(Infos.COPY_FILTERED, thisJoinPointStaticPart.getSourceLocation());
	}
	after(IHDLObject src) returning(IHDLObject res) :  this(src) && execution (* IHDLObject.copyFiltered(..)){
		res.addMeta("COPY_SOURCE", src);
	}
	after(IHDLObject src) returning(IHDLObject res) : this(src) && execution (* IHDLObject.copy(..)){
		res.addMeta("COPY_SOURCE", src);
	}
	before(IHDLObject tar) : setters(tar){
		tar.addMeta(Infos.SETTER, thisJoinPointStaticPart.getSourceLocation());
	}
	before(IHDLObject tar) : target(tar) && setContainer(){
		tar.addMeta(Infos.SETTER_CONTAINER, thisJoinPointStaticPart.getSourceLocation());
	}
	before(IHDLObject tar) : adder(tar){
		tar.addMeta(Infos.ADDER, thisJoinPointStaticPart.getSourceLocation());
	}
	after(IHDLObject src) returning(IHDLObject tar): this(src) && constructors(){
		tar.addMeta(Infos.CONSTRUCTOR, thisJoinPointStaticPart.getSourceLocation());
		tar.addMeta("CONSTRUCTION_SRC", src);
	}
}
