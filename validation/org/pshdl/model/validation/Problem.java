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
package org.pshdl.model.validation;

import java.util.*;
import java.util.concurrent.atomic.*;

import javax.xml.bind.annotation.*;

import org.pshdl.model.*;
import org.pshdl.model.parser.*;
import org.pshdl.model.utils.*;
import org.pshdl.model.utils.services.IHDLValidator.IErrorCode;
import org.pshdl.model.validation.HDLValidator.HDLAdvise;

public class Problem {
	public enum ProblemAccess implements MetaAccess<Problem> {
		PROBLEM;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	@XmlEnum(String.class)
	public static enum ProblemSeverity {
		INFO, WARNING, ERROR;
	}

	public final ProblemSeverity severity;
	public final IErrorCode code;
	public final IHDLObject node;
	public final IHDLObject context;
	public final String info;
	public final int pid;
	public final Map<String, Object> meta = new HashMap<String, Object>();
	public final int line;
	public final int length;
	public final boolean isSyntax;
	public final int offsetInLine;
	public final int totalOffset;
	private static AtomicInteger uid = new AtomicInteger();

	public Problem(IErrorCode code, String msg, int line, int offsetInLine, int length, int totalOffset) {
		this.code = code;
		this.node = null;
		this.context = null;
		this.info = msg;
		this.pid = uid.incrementAndGet();
		this.severity = code.getSeverity();
		this.line = line;
		this.offsetInLine = offsetInLine;
		this.length = length;
		this.totalOffset = totalOffset;
		isSyntax = true;
	}

	public Problem(IErrorCode code, IHDLObject node) {
		this(code, node, null, null);
	}

	public Problem(IErrorCode code, IHDLObject node, IHDLObject context) {
		this(code, node, context, null);
	}

	public Problem(IErrorCode code, IHDLObject node, String info) {
		this(code, node, null, info);
	}

	public Problem(IErrorCode code, IHDLObject node, IHDLObject context, String info) {
		if (node == null)
			throw new IllegalArgumentException("Node can not be null!");
		this.context = context;
		this.severity = code.getSeverity();
		this.code = code;
		this.node = node;
		this.info = info;
		this.pid = uid.incrementAndGet();
		this.isSyntax = false;
		node.addMeta(ProblemAccess.PROBLEM, this);
		if (context != null) {
			context.addMeta(ProblemAccess.PROBLEM, this);
		}
		final SourceInfo sInfo = findMeta(node);
		if (sInfo != null) {
			line = sInfo.startLine;
			offsetInLine = sInfo.startPosInLine;
			length = sInfo.length;
			totalOffset = sInfo.totalOffset;
		} else {
			offsetInLine = -1;
			line = -1;
			length = -1;
			totalOffset = -1;
		}
	}

	public static SourceInfo findMeta(IHDLObject node) {
		final SourceInfo sInfo = node.getMeta(SourceInfo.INFO);
		if ((sInfo == null) && (node.getContainer() != null))
			return findMeta(node.getContainer());
		return sInfo;
	}

	public <T> Problem addMeta(MetaAccess<T> key, T value) {
		meta.put(key.name(), value);
		return this;
	}

	public Problem addMeta(String key, Object value) {
		meta.put(key, value);
		return this;
	}

	@Override
	public String toString() {
		return severity + ": " + toStringWithoutSeverity();
	}

	public String toStringWithoutSeverity() {
		if (isSyntax)
			return info;

		String preText = "";
		final IHDLObject inlineType = checkInlineType(node);
		if (inlineType != null) {
			preText = "The inline function: " + inlineType + " caused the following issue: ";
		}
		final HDLAdvise advise = HDLValidator.advise(this);
		if (advise != null)
			return preText + advise.message;
		String string = preText + code.name().toLowerCase() + " for: " + node;
		if (context != null) {
			string += " @ " + context;
		}
		if (info != null) {
			string += " info:" + info;
		}
		return string;
	}

	private IHDLObject checkInlineType(IHDLObject node) {
		if (node == null)
			return null;
		final Object meta = node.getMeta(HDLFunction.META);
		if (meta != null)
			return (IHDLObject) meta;
		return checkInlineType(node.getContainer());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((code == null) ? 0 : code.hashCode());
		result = (prime * result) + ((severity == null) ? 0 : severity.hashCode());
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
		final Problem other = (Problem) obj;
		if (code != other.code)
			return false;
		if (context != other.context)
			return false;
		if (node != other.node)
			return false;
		if (severity != other.severity)
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	public <T> T getMeta(MetaAccess<T> m) {
		return (T) meta.get(m.name());
	}

}
