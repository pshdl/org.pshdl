package de.tuhh.ict.pshdl.model.validation;

import java.util.*;
import java.util.concurrent.atomic.*;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;
import de.tuhh.ict.pshdl.model.validation.HDLAdvisor.HDLAdvise;

public class Problem {
	public enum ProblemAccess implements MetaAccess<Problem> {
		PROBLEM;

		@Override
		public boolean inherit() {
			return true;
		}
	}

	public static enum ProblemSeverity {
		INFO, WARNING, ERROR;
	}

	public final ProblemSeverity severity;
	public final ErrorCode code;
	public final IHDLObject node;
	public final IHDLObject context;
	public final String info;
	public final int pid;
	public final Map<String, Object> meta = new HashMap<String, Object>();
	private static AtomicInteger uid = new AtomicInteger();

	public Problem(ErrorCode code, IHDLObject node) {
		this(code, node, null, null);
	}

	public Problem(ErrorCode code, IHDLObject node, IHDLObject context) {
		this(code, node, context, null);
	}

	public Problem(ErrorCode code, IHDLObject node, String info) {
		this(code, node, null, info);
	}

	public Problem(ErrorCode code, IHDLObject node, IHDLObject context, String info) {
		if (node == null)
			throw new IllegalArgumentException("Node can not be null!");
		this.context = context;
		this.severity = code.severity;
		this.code = code;
		this.node = node;
		this.info = info;
		this.pid = uid.incrementAndGet();
		node.addMeta(ProblemAccess.PROBLEM, this);
		if (context != null)
			context.addMeta(ProblemAccess.PROBLEM, this);
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
		String preText = "";
		IHDLObject inlineType = checkInlineType(node);
		if (inlineType != null) {
			preText = "The inline function: " + inlineType + " caused the following issue: ";
		}
		HDLAdvise advise = HDLAdvisor.getAdvise(this);
		if (advise != null)
			return preText + advise.message;
		String string = preText + code.name().toLowerCase() + " for: " + node;
		if (context != null)
			string += " @ " + context;
		if (info != null)
			string += " info:" + info;
		return string;
	}

	private IHDLObject checkInlineType(IHDLObject node) {
		if (node == null)
			return null;
		Object meta = node.getMeta(HDLInlineFunction.META);
		if (meta != null) {
			return (IHDLObject) meta;
		}
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