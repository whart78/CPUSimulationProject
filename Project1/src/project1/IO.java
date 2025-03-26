package project1;

import project1.PCB;

public class IO {	
	public static void execute(PCB process, int ioBurst) {
		//simulate the IO execute a ioBurst unit time of that process
		process.getIOBursts().set(0, process.getIOBursts().get(0) - ioBurst);

	}
}
