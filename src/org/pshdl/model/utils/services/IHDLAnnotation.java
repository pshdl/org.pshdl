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
package org.pshdl.model.utils.services;

import org.pshdl.model.HDLAnnotation;
import org.pshdl.model.utils.services.CompilerInformation.AnnotationInformation;

public interface IHDLAnnotation {
	public abstract static class AbstractAnnotation implements IHDLAnnotation {

		private final String name;
		private final AnnotationInformation info;

		public AbstractAnnotation(String name, Class<?> provider, String summary, String arguments) {
			if (name.charAt(0) != '@') {
				this.name = '@' + name;
			} else {
				this.name = name;
			}
			info = new AnnotationInformation(provider.getName(), name, summary, arguments);
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public AnnotationInformation getAnnotationInformation() {
			return info;
		}

		public HDLAnnotation create(String arg) {
			return new HDLAnnotation().setName(name).setValue(arg);
		}

	}

	/**
	 * Returns the name of the annotation without the @ symbol
	 *
	 * @return
	 */
	String name();

	/**
	 * Validates the contents of the annotation and returns a message to the
	 * user if it is not correct
	 * @param annotation TODO
	 * @return
	 */
	String validate(HDLAnnotation annotation);

	AnnotationInformation getAnnotationInformation();
}
