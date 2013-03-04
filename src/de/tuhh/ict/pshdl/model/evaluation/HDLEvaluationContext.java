package de.tuhh.ict.pshdl.model.evaluation;

import static de.tuhh.ict.pshdl.model.extensions.FullNameExtension.*;
import static de.tuhh.ict.pshdl.model.utils.HDLQuery.*;

import java.util.*;
import java.util.Map.Entry;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.HDLVariableDeclaration.HDLDirection;
import de.tuhh.ict.pshdl.model.utils.*;

public class HDLEvaluationContext {

	private Map<String, HDLExpression> context;

	public HDLEvaluationContext(Map<String, HDLExpression> context) {
		this.context = context;
	}

	public HDLExpression get(HDLVariable ref) {
		return context.get(ref.getName());
	}

	public static Map<HDLQualifiedName, HDLEvaluationContext> createDefault(HDLPackage pkg) {
		Map<HDLQualifiedName, HDLEvaluationContext> res = new HashMap<HDLQualifiedName, HDLEvaluationContext>();
		for (HDLUnit unit : pkg.getUnits()) {
			HDLEvaluationContext hec = createDefault(unit);
			HDLQualifiedName fullName = fullNameOf(unit);
			res.put(fullName, hec);
		}
		return res;
	}

	/**
	 * Generates a default context where all parameter are assumed to be the
	 * constant they are initialized with
	 * 
	 * @param unit
	 *            the unit to create the context for
	 * @return a HDLEvaluationContext with all parameters set to their default
	 */
	public static HDLEvaluationContext createDefault(HDLUnit unit) {
		Map<String, HDLExpression> c = new HashMap<String, HDLExpression>();
		Collection<HDLVariableDeclaration> constants = HDLQuery.select(HDLVariableDeclaration.class)//
				.from(unit).where(HDLVariableDeclaration.fDirection)//
				.matches(isEqualTo(HDLDirection.CONSTANT))//
				.or(isEqualTo(HDLDirection.PARAMETER)) //
				.getAll();
		for (HDLVariableDeclaration hvd : constants) {
			for (HDLVariable var : hvd.getVariables()) {
				c.put(var.getName(), var.getDefaultValue());
			}
		}
		return new HDLEvaluationContext(c);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String spacer = "";
		for (Entry<String, HDLExpression> unit : context.entrySet()) {
			sb.append(spacer).append(unit.getKey()).append(':').append(unit.getValue());
			spacer = ",";
		}
		return sb.toString();
	}

	public int getPrevious(String string) {
		throw new RuntimeException("Not implemented");
	}

}
