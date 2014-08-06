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

#ifndef _pshdl_generic_sim_h_
#define _pshdl_generic_sim_h_

uint64_t pshdl_sim_getOutput(int idx);
void     pshdl_sim_setInput (int idx, uint64_t value);
uint64_t pshdl_sim_getOutputArray(int idx, int arrayIdx[]);
void     pshdl_sim_setInputArray (int idx, uint64_t value, int arrayIdx[]);

void     pshdl_sim_setDisableRegOutputlogic(bool enable);
void     pshdl_sim_setDisableEdges(bool enable);

void     pshdl_sim_run(void);
void     pshdl_sim_initConstants(void);

char*    pshdl_sim_getName(int idx);
char*    pshdl_sim_getJsonDesc(void);
uint64_t pshdl_sim_getDeltaCycle(void);
int      pshdl_sim_getVarCount(void);

#endif
