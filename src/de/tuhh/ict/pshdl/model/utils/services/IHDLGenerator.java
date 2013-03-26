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
package de.tuhh.ict.pshdl.model.utils.services;

import java.util.*;

import javax.annotation.*;

import com.google.common.base.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.evaluation.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.utils.services.CompilerInformation.GeneratorInformation;
import de.tuhh.ict.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import de.tuhh.ict.pshdl.model.validation.*;

/**
 * This interface can be implemented to add an additional generator to the
 * compiler. It has to be made known via the {@link IServiceProvider}. It is a
 * good idea to provide at least a registration for the {@link ServiceLoader}
 * via the META-INF/sevices mechanism, which is the default
 * {@link IServiceProvider}.
 * 
 * @author Karsten Becker
 * 
 */
public interface IHDLGenerator {
	/**
	 * Side files are used for transporting additional generated content into
	 * the output of the compiler.
	 */
	public static class SideFile {
		/**
		 * This singleton can be used to indicate that the resulting VHDL (or
		 * other output) of the PSHDL should be placed at this given location.
		 * This can be useful for ip core directory structures.
		 */
		public static final byte[] THIS = new byte[] { 'T', 'H', 'I', 'S' };
		/**
		 * The directory to which the contents should be written. This will
		 * always be below the general output folder
		 */
		public final String relPath;
		public final byte[] contents;

		public SideFile(String relPath, byte[] contents) {
			super();
			this.relPath = relPath;
			this.contents = contents;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = (prime * result) + ((relPath == null) ? 0 : relPath.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SideFile other = (SideFile) obj;
			if (relPath == null) {
				if (other.relPath != null)
					return false;
			} else if (!relPath.equals(other.relPath))
				return false;
			return true;
		}

	}

	/**
	 * The {@link HDLUnit}, which, if specified to be included, will be merged
	 * into the containing {@link HDLUnit} via {@link Insulin}. Also the given
	 * side-files will be registered to be placed in the output folder.
	 */
	public static class HDLGenerationInfo {
		public List<SideFile> files = new LinkedList<SideFile>();
		public final HDLUnit unit;

		public HDLGenerationInfo(HDLUnit unit) {
			super();
			this.unit = unit;
		}
	}

	/**
	 * Attempts to get an interface for this generator. If is should fail,
	 * caused by invalid parameters for example, it should mark it as absent.
	 * 
	 * @param hdl
	 *            the generator element
	 * @return a non null {@link Optional} which should contain the interface
	 */
	@Nonnull
	public Optional<HDLInterface> getInterface(HDLDirectGeneration hdl);

	/**
	 * Attempts to get the implementation of this generator. If is should fail,
	 * caused by invalid parameters for example, it should mark it as absent.
	 * 
	 * @param hdl
	 *            the generator element
	 * @return a non null {@link Optional} which should contain the interface
	 */
	@Nonnull
	public Optional<HDLGenerationInfo> getImplementation(HDLDirectGeneration hdl);

	/**
	 * Returns the generator IDs that are supported by this Generator. Those IDs
	 * are what can be found when a generator is instantiated. That is
	 * <code>=generate ID()</code>. Methods of this generator will only be
	 * invoked for these certain IDs.
	 * 
	 * @return a list of ids that this generator supports
	 */
	@Nonnull
	public String[] getNames();

	/**
	 * Ensures that the configuration of this generator is correct.<br>
	 * <b>Note:</b> if you use your own {@link IErrorCode} you should also
	 * provide an implementation of {@link IHDLValidator}.
	 * 
	 * @param hdg
	 *            the direct generation node
	 * @param problems
	 *            if a problem should arise, this is the place to put them.
	 * @param context
	 *            a context for constant evaluations
	 */
	@Nonnull
	public void validate(HDLDirectGeneration hdg, Set<Problem> problems, HDLEvaluationContext context);

	/**
	 * Retrieves a (maybe empty List) of {@link HDLVariableDeclaration} that
	 * will be placed in the outer {@link HDLUnit}. These ports are used by
	 * {@link HDLValidator} to ensure that no duplicate variables/ports exists.
	 * The minimum return are the {@link HDLVariableDeclaration} of the
	 * generated {@link HDLInterface}.
	 * 
	 * @param hdl
	 *            the direct generation node
	 * @return a non null, but potentially empty list of
	 *         {@link HDLVariableDeclaration} that are added to the containing
	 *         {@link HDLUnit} if included.
	 */
	@Nonnull
	public List<HDLVariableDeclaration> getPortAdditions(HDLDirectGeneration hdl);

	/**
	 * Provide information about this generator. This is used to display the
	 * user some information about what this generator can do for him and who
	 * provided it.
	 * 
	 * @param name
	 *            the generator ID to get the information for.
	 * @return
	 */
	@Nonnull
	public GeneratorInformation getGeneratorInfo(String name);

}
