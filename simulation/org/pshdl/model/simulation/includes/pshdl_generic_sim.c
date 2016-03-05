#include <assert.h>
#include <stdio.h>
#include <stdint.h>
#include <stdbool.h>

static const char *ASSERT_NAMES[]={"FATAL", "ERROR", "WARNING", "INFO"};

void pshdl_assertThat_bool_EAssert_s(bool assumption, uint64_t assertLevel,
		const char* message) {
	if (!assumption) {
		fprintf(stderr, "%s: %s\n", ASSERT_NAMES[assertLevel], message);
		if (assertLevel < 2) {
			assert(assumption);
		}
	}
}
