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

#include <stdbool.h>
#include <stdint.h>

/**
 * This methods allows the access of any variable that is not an array, declared 
 * within the PSHDL module.
 * @param idx the index of the variable as declared in the header file specific
 *				to your module. The variable may NOT be an array. Accessing an 
 *				invalid index will return 0.
 * @retval the value returned is masked to the width of the variable. A sign-extension
 * 			is NOT performed. Predicates return either 0 or 1
 */
uint64_t pshdl_sim_getOutput(uint32_t idx);
void     pshdl_sim_setInput (uint32_t idx, uint64_t value);

/**
 * This methods allows the access of any variable declared within the PSHDL module.
 * @param idx the index of the variable as declared in the header file specific
 *				to your module. Accessing an invalid index will return 0.
 * @param offset when the variable that is accessed is an array, this offset is used
 * 				 as index within the array. For multi dimensional variables some calculation
 *				 may be required. See the header files for macros for that.
 * @retval the value returned is masked to the width of the variable. A sign-extension
 * 			is NOT performed. Predicates return either 0 or 1
 */
uint64_t pshdl_sim_getOutputArray(uint32_t idx, uint32_t offset);
void     pshdl_sim_setInputArray (uint32_t idx, uint64_t value, uint32_t offset);

void     pshdl_sim_setDisableRegOutputlogic(bool enable);
void     pshdl_sim_setDisableEdges(bool enable);

void     pshdl_sim_run(void);
void     pshdl_sim_initConstants(void);

int      pshdl_sim_getIndex(char* name);
char*    pshdl_sim_getName(uint32_t idx);
char*    pshdl_sim_getJsonDesc(void);
uint64_t pshdl_sim_getDeltaCycle(void);
uint32_t pshdl_sim_getVarCount(void);

void pshdl_assertThat_bool_EAssert_s(bool assumption, uint64_t assertLevel, const char* message);

#endif
