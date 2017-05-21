/*******************************************************************************
 * PSHDL is a library and (trans-)compiler for PSHDL input. It generates
 *     output suitable for implementation or simulation of it.
 *
 *     Copyright (C) 2014 Karsten Becker (feedback (at) pshdl (dot) org)
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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.google.common.io.ByteStreams;

/**
 * Side files are used for transporting additional generated content into the output of the compiler.
 */
public class AuxiliaryContent {
	/**
	 * This singleton can be used to indicate that the resulting VHDL (or other output) of the PSHDL should be placed at this given
	 * location. This can be useful for ip core directory structures.
	 */
	public static final byte[] THIS = new byte[] { 'T', 'H', 'I', 'S' };
	/**
	 * The directory to which the contents should be written. This will always be below the general output folder
	 */
	public final String relPath;
	public final byte[] contents;
	public final boolean isString;

	public AuxiliaryContent(String relPath, InputStream contents, boolean isString) throws IOException {
		this(relPath, ByteStreams.toByteArray(contents), isString);
	}

	public AuxiliaryContent(String relPath, String contents) {
		this(relPath, contents.getBytes(StandardCharsets.UTF_8), true);
	}

	public AuxiliaryContent(String relPath, byte[] contents, boolean isString) {
		super();
		if (contents == null) {
			throw new IllegalArgumentException("Contents can not be null");
		}
		this.relPath = relPath;
		this.contents = contents;
		this.isString = isString;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AuxiliaryContent other = (AuxiliaryContent) obj;
		if (relPath == null) {
			if (other.relPath != null) {
				return false;
			}
		} else if (!relPath.equals(other.relPath)) {
			return false;
		}
		return true;
	}

}