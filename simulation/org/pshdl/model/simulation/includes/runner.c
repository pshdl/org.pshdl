//
//  main.c
//  TestRunner
//
//  Created by Karsten Becker on 28.07.14.
//  Copyright (c) 2014 PSHDL. All rights reserved.
//

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>
#include <assert.h>
#include "pshdl_generic_sim.h"

void pshdl_assertThat_bool_EAssert_s(bool assumption, uint64_t assertLevel,
		const char* message) {
	if (!assumption) {
		fprintf(stderr, "as %llu '%s'\n", assertLevel, message);
		if (assertLevel < 2) {
			exit(assertLevel+1);
		}
	}
}

int main(int argc, const char *argv[]) {
  uint32_t idx, offset;
  uint64_t value;
  char line[1024];
  printf("#Ready\n");
  fflush(stdout);
  while (fgets(line, 1024, stdin)) {
    switch (line[0]) {
    case '#':
      continue;
    case 'a':
      switch (line[1]) {
      case 'n':
        sscanf(&line[2], "%d %llx", &idx, &value);
        assert(value == pshdl_sim_getOutput(idx));
        printf(">an\n");
        break;
      case 'a':
        sscanf(&line[2], "%d %d %llx", &idx, &offset, &value);
        assert(value == pshdl_sim_getOutputArray(idx, offset));
        printf(">aa\n");
        break;
      }
      break;
    case 'd':
      switch (line[1]) {
      case 'j':
        printf(">dj %s\n", pshdl_sim_getJsonDesc());
        break;
      case 'c':
        printf(">dc %llu\n", pshdl_sim_getDeltaCycle());
        break;
      case 'e':
        sscanf(&line[2], "%d", &idx);
        if (idx > 0) {
          pshdl_sim_setDisableEdges(true);
          printf("#set disabled edges to true\n");
        } else {
          pshdl_sim_setDisableEdges(false);
          printf("#set disabled edges to false\n");
        }
        printf(">de\n");
        break;
      case 'r':
        sscanf(&line[2], "%d", &idx);
        if (idx > 0) {
          pshdl_sim_setDisableRegOutputlogic(true);
          printf("#set disabled register output logic to true\n");
        } else {
          pshdl_sim_setDisableRegOutputlogic(false);
          printf("#set disabled register output logic to false\n");
        }
        printf(">dr\n");
        break;
      }
      break;
    case 'g':
      switch (line[1]) {
      case 'n':
        sscanf(&line[2], "%d", &idx);
        printf(">gn %d %llx\n", idx, pshdl_sim_getOutput(idx));
        break;
      case 'a':
        sscanf(&line[2], "%d %d", &idx, &offset);
        printf(">ga %d %d %llx\n", idx, offset,
               pshdl_sim_getOutputArray(idx, offset));
        break;
      }
      break;
    case 'i':
      switch (line[1]) {
      case 'c':
        pshdl_sim_initConstants();
        printf(">ic\n");
        break;
      }
      break;
    case 'r':
      switch (line[1]) {
      case 'r':
        pshdl_sim_run();
        printf(">rr\n");
        break;
      }
      break;
    case 's':
      switch (line[1]) {
      case 'n':
        sscanf(&line[2], "%d %llx", &idx, &value);
        printf("#Setting %d to %llx\n", idx, value);
        pshdl_sim_setInput(idx, value);
        printf(">sn\n");
        break;
      case 'a':
        sscanf(&line[2], "%d %d %llx", &idx, &offset, &value);
        printf("#Setting %d[%d] to %llx\n", idx, offset, value);
        pshdl_sim_setInputArray(idx, value, offset);
        printf(">sa\n");
        break;
      }
      break;
    case 'x':
      switch (line[1]) {
      case 'n':
        printf(">xn\n");
        fflush(stdout);
        return 0;
      case 'e':
        printf(">xe\n");
        fflush(stdout);
        return 1;
      }
      break;
    }
    fflush(stdout);
  }
  return 2;
}
