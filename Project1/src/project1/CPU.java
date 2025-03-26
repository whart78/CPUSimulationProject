package project1;

import project1.PCB;

public class CPU {	
	public static void execute(PCB process, int cpuBurst) {
		//simulate the CPU execute a cpuBurst unit time of that process
		process.getCpuBursts().set(0, process.getCpuBursts().get(0) - cpuBurst);
	}
}
