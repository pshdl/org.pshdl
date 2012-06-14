package de.tuhh.ict.pshdl.model.simulation;
import de.tuhh.ict.pshdl.model.*;

public aspect SimulationTransformation {
	public HDLExpression HDLExpression.toSimulationModel(){
		return this;
	}
}
