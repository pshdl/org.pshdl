package de.tuhh.ict.pshdl.model.types.builtIn;

import java.util.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.services.*;
import de.tuhh.ict.pshdl.model.validation.*;

public class HDLAnnotations {
	private static Map<String, IHDLAnnotation> annotations;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		annotations = new HashMap<String, IHDLAnnotation>();
		for (IHDLAnnotation anno : sp.getAllAnnotations()) {
			annotations.put(anno.name(), anno);
			info.registeredAnnotations.put(anno.name(), anno.getAnnotationInformation());
		}
	}

	public static Problem[] validate(HDLAnnotation hdlAnnotation) {
		IHDLAnnotation iAnno = annotations.get(hdlAnnotation.getName().substring(1));
		if (iAnno == null)
			return new Problem[] { new Problem(ErrorCode.ANNOTATION_UNKNOWN, hdlAnnotation) };
		String valid = iAnno.validate(hdlAnnotation.getValue());
		if (valid != null)
			return new Problem[] { new Problem(ErrorCode.ANNOTATION_INVALID, hdlAnnotation, valid) };
		return new Problem[0];
	}

	public static String[] knownAnnotations() {
		return annotations.keySet().toArray(new String[0]);
	}
}
