package de.tuhh.ict.pshdl.model.validation;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

public class Problem {
	public enum ProblemAccess implements MetaAccess<Problem> {
		PROBLEM;
	}

	public static enum ProblemSeverity {
		INFO, WARNING, ERROR;
	}

	public final ProblemSeverity severity;
	public final ErrorCode code;
	public final HDLObject node;
	public final HDLObject context;
	public final String info;

	public Problem(ErrorCode code, HDLObject node) {
		this(code, node, null, null);
	}

	public Problem(ErrorCode code, HDLObject node, HDLObject context) {
		this(code, node, context, null);
	}

	public Problem(ErrorCode code, HDLObject node, String info) {
		this(code, node, null, info);
	}

	public Problem(ErrorCode code, HDLObject node, HDLObject context, String info) {
		if (node == null)
			throw new IllegalArgumentException("Node can not be null!");
		this.context = context;
		this.severity = code.severity;
		this.code = code;
		this.node = node;
		this.info = info;
		node.addMeta(ProblemAccess.PROBLEM, this);
		if (context != null)
			context.addMeta(ProblemAccess.PROBLEM, this);
	}

	@Override
	public String toString() {
		return severity + ": " + toStringWithoutSeverity();
	}

	public String toStringWithoutSeverity() {
		String string = code.name().toLowerCase() + " for: " + node;
		if (context != null)
			string += " @ " + context;
		if (info != null)
			string += " info:" + info;
		return string;
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
		Problem other = (Problem) obj;
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

}