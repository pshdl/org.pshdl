package main

import (
	"bufio"
	"fmt"
	"log"
	"os"
	"runtime"
)

func main() {
	sim := NewTestUnit()

	fmt.Println("#Ready")
	scanner := bufio.NewScanner(os.Stdin)

	var (
		idx, offset int
		value       uint64
		intValue		int64
	)

	for scanner.Scan() {
		line := scanner.Text()
		if (len(line)==0){
			continue
		}
		switch line[0] {
		case '#':
			continue

		case 'a': // assert
			switch line[1] {

			case 'n':
				fmt.Sscanf(line[2:], "%d %x", &idx, &value)
				intValue=int64(value)
				assert(intValue == sim.GetOutput(idx))
				fmt.Println(">an")

			case 'a':
				fmt.Sscanf(line[2:], "%d %d %x", &idx, &offset, &value)
				intValue=int64(value)
				assert(intValue == sim.GetOutput(idx, offset))
				fmt.Println(">aa")
			}

		case 'd':
			switch line[1] {

			case 'j':
				fmt.Println(">dj", sim.GetJsonDesc())

			case 'c':
				fmt.Printf(">dc %d\n", sim.GetDeltaCycle())

			case 'e':
				fmt.Sscanf(line[2:], "%d", &idx)
				sim.SetDisableEdge(idx > 0)
				fmt.Printf("#set disabled edges to %v\n", idx > 0)
				fmt.Println(">de")

			case 'r':
				fmt.Sscanf(line[2:], "%d", &idx)
				sim.SetDisableRegOutputLogic(idx > 0)
				fmt.Printf("#setdisabled register output logic to %v\n", idx > 0)
				fmt.Println(">dr")
			}

		case 'g': // get
			switch line[1] {

			case 'n':
				fmt.Sscanf(line[2:], "%d", &idx)
				fmt.Printf(">gn %d %x\n", idx, sim.GetOutput(idx))

			case 'a':
				fmt.Sscanf(line[2:], "%d %d", &idx, &offset)
				fmt.Printf(">ga %d %d %x\n", idx, offset, sim.GetOutput(idx, offset))

			}

		case 'i': // init
			switch line[1] {
			case 'c':
				sim.InitConstants()
				fmt.Println(">ic")
			}

		case 'r': // run
			switch line[1] {
			case 'r':
				sim.Run()
				fmt.Println(">rr")
			}

		case 's': // set
			switch line[1] {

			case 'n':
				fmt.Sscanf(line[2:], "%d %x", &idx, &value)
				intValue=int64(value)
				fmt.Printf("#Setting %d to %x\n", idx, intValue)
				sim.SetInput(idx, intValue)
				fmt.Println(">sn")

			case 'a':
				fmt.Sscanf(line[2:], "%d %d %x", &idx, &offset, &value)
				intValue=int64(value)
				fmt.Printf("#Setting %d[%d] to %x\n", idx, offset, intValue)
				sim.SetInput(idx, intValue, offset)
				fmt.Println(">sa")
			}

		case 'x':

			switch line[1] {
			case 'n':
				fmt.Println(">xn")
				os.Exit(0)
			case 'e':
				fmt.Println(">xe")
				os.Exit(1)
			}
		}
	}

	if err := scanner.Err(); err != nil {
		fmt.Fprintf(os.Stderr, "Error during Scan(): %s\n", err)
		os.Exit(-1)
	}

	os.Exit(2)
}

func assert(expr bool) {
	if !expr {
		_, file, line, _ := runtime.Caller(1)
		log.Fatalf("assert failed on <%s:%d>\n", file, line)
	}
}
