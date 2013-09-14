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

import java.math.*;

import org.pshdl.model.*;
import org.pshdl.model.utils.services.CompilerInformation.AnnotationInformation;
import org.pshdl.model.utils.services.*;

public class HDLBuiltInAnnotationProvider implements IHDLAnnotationProvider {
	public static enum HDLBuiltInAnnotations implements IHDLAnnotation {
		/**
		 * Automatically generate an interface for the HDLUnit
		 */
		autoInterface,
		/**
		 * Generated signal
		 */
		genSignal,
		/**
		 * Designates a bit signal as clock
		 */
		clock,
		/**
		 * Designates a bit signal as reset
		 */
		reset,
		/**
		 * Indicates a range of values that are allowed for this variable. The
		 * value are the lower bound separated by a semicolon and the upper
		 * bound. For example: -1;6 indicates that the variable can have a value
		 * of either -1,0,1,2,3,4,5,6
		 */
		range,
		/**
		 * The name of the type that should be used instead of an automatically
		 * created type during VHDL code generation. The name should start with
		 * VHDL.
		 */
		VHDLType,
		/**
		 * Indicates that this interface should be instantiated as component
		 * rather entity.
		 */
		VHDLComponent,
		/**
		 * This annotation causes the default initialization to 0 to be omitted.
		 * This MAY cause a latch to be created.
		 */
		VHDLLatchable,
		/**
		 * This annotation causes the reset value of a register to be assigned
		 * as default in the value in the declaration, instead of describing a
		 * reset behaviour.
		 */
		VHDLNoExplicitReset;
		@Override
		public String toString() {
			return "@" + name();
		}

		public boolean is(HDLAnnotation anno) {
			return anno.getName().substring(1).equals(name());
		}

		public HDLAnnotation create(String value) {
			return new HDLAnnotation().setName(toString()).setValue(value);
		}

		@Override
		public String validate(String value) {
			switch (this) {
			case autoInterface:
			case genSignal:
			case VHDLLatchable:
			case VHDLNoExplicitReset:
			case reset:
			case clock:
				if (value != null)
					return this + " does not expect any arguments";
				break;
			case VHDLComponent:
				if (!((value == null) || "declare".equals(value) || "import".equals(value)))
					return this + " only accepts 'declare' or 'import' as parameter. If no parameter is supplied import is assumed.";
				break;
			case range:
				if (value == null)
					return this + " expects an argument with the expected range of the variable. The format is from;to";
				final String[] split = value.split(";");
				if (split.length != 2)
					return this + " expects an argument with the expected range of the variable. The given value:" + value + " is not valid. The format is from;to";
				try {
					new BigInteger(split[0]);
				} catch (final Exception e) {
					return "The given from value:" + split[0] + " is not a valid number.";
				}
				try {
					new BigInteger(split[1]);
				} catch (final Exception e) {
					return "The given to value:" + split[1] + " is not a valid number.";
				}
				break;
			case VHDLType:
				if (value == null)
					return this + " expects an argument with the name of the VHDL entity to use.";
				break;
			}
			return null;
		}

		@Override
		public AnnotationInformation getAnnotationInformation() {
			switch (this) {
			case autoInterface:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Units that have this Annotation will get an interface declaration atop it's declaration", null);
			case VHDLLatchable:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"This annotation causes the default initialization to 0 to be omitted. This MAY cause a latch to be created.", null);
			case VHDLNoExplicitReset:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"This annotation causes the reset value of a register to be assigned as default in the value in the declaration, instead of describing a reset behaviour.",
						null);
			case VHDLType:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"The name of the type that should be used instead of an automatically created type during VHDL code generation. The name should start with VHDL.",
						"The name that should be used instead. It usually starts with VHDL.");
			case clock:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates a bit signal to be used for $clk, which also is used by registers", null);
			case genSignal:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"When a signal is automatically generated by a generator, then it has this annotation", "The instance that caused this signal");
			case range:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Indicates a range of values that are allowed for this variable. The value are the lower bound separated by a semicolon and the upper bound.",
						"The range in the format: 'from;to'. For example: -1;6 indicates that the variable can have a value of either -1,0,1,2,3,4,5,6");
			case reset:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates a bit signal to be used for $rst, which also is used by registers", null);
			case VHDLComponent:
				return new AnnotationInformation(HDLBuiltInAnnotationProvider.class.getSimpleName(), toString(),
						"Designates an interface that should be instantiated as component rather than as entity", null);
			}
			throw new IllegalArgumentException("Forgot to implement AnnotationInformation for:" + this);
		}
	}

	@Override
	public IHDLAnnotation[] getAnnotations() {
		return HDLBuiltInAnnotations.values();
	}

}
