/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2013 Karsten Becker (feedback (at) pshdl (dot) org)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *     This License does not grant permission to use the trade names, trademarks,
 *     service marks, or product names of the Licensor, except as required for
 *     reasonable and customary use in describing the origin of the Work.
 *
 * Contributors:
 *     Karsten Becker - initial API and implementation
 ******************************************************************************/
package org.pshdl.model.types.builtIn;

import java.util.Map;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.utils.services.CompilerInformation;
import org.pshdl.model.utils.services.CompilerInformation.AnnotationInformation;
import org.pshdl.model.utils.services.IHDLAnnotation;
import org.pshdl.model.utils.services.IServiceProvider;
import org.pshdl.model.validation.Problem;
import org.pshdl.model.validation.builtin.ErrorCode;

import com.google.common.collect.Maps;

public class HDLAnnotations {
	private static Map<String, IHDLAnnotation> annotations;

	public static void init(CompilerInformation info, IServiceProvider sp) {
		annotations = Maps.newLinkedHashMap();
		for (final IHDLAnnotation anno : sp.getAllAnnotations()) {
			annotations.put(anno.name(), anno);
			final AnnotationInformation annoInfo = anno.getAnnotationInformation();
			if (annoInfo == null) {
				throw new IllegalArgumentException(anno.name() + " does not provide annotation info!");
			}
			info.registeredAnnotations.put(anno.name(), annoInfo);
		}
	}

	public static Problem[] validate(HDLAnnotation hdlAnnotation) {
		final IHDLAnnotation iAnno = annotations.get(hdlAnnotation.getName().substring(1));
		if (iAnno == null) {
			return new Problem[] { new Problem(ErrorCode.ANNOTATION_UNKNOWN, hdlAnnotation) };
		}
		final String valid = iAnno.validate(hdlAnnotation);
		if (valid != null) {
			return new Problem[] { new Problem(ErrorCode.ANNOTATION_INVALID, hdlAnnotation, valid) };
		}
		return new Problem[0];
	}

	public static String[] knownAnnotations() {
		return annotations.keySet().toArray(new String[0]);
	}
}
