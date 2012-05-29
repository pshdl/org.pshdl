package de.tuhh.ict.pshdl.model.aspects;

import de.tuhh.ict.pshdl.model.*;
import de.tuhh.ict.pshdl.model.utils.*;

public aspect InterfaceInserter {
	declare parents : HDLUnit implements IStatementContainer;
	declare parents : HDLStatement implements IStatementContainer;
}
