package project1;

import java.util.*;
import java.io.*;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		//read the set of input processes
		Scanner sc = new Scanner(new File("src/proc.txt"));
		String alg = sc.nextLine(); //read the selected algorithm
		String line;
		int id = 0;
		ArrayList<PCB> allProcs = new ArrayList<>(); //list of processes
		while(sc.hasNextLine()) {
			line = sc.nextLine(); //read a line from the file
			String[] arr = line.split(", ");
			String name = arr[0];
			int arrivalTime = Integer.parseInt(arr[1].trim());
			List<Integer> cpuBurstTimes = new ArrayList<>(); //= Integer.parseInt(arr[2].trim());
			List<Integer> ioBurstTimes = new ArrayList<>();
			for(int i = 3; i < arr.length; i++) {
				if(i % 2 != 0) {
					cpuBurstTimes.add(Integer.parseInt(arr[i]));
				}
				else {
					ioBurstTimes.add(Integer.parseInt(arr[i]));
				}
			}
			
			//!!!!!!! Test Statements for burst lists
			System.out.println("CPU Bursts: " + cpuBurstTimes);
			System.out.println("IO Bursts: " + ioBurstTimes);
			
			int priority = Integer.parseInt(arr[3].trim());
			PCB proc = new PCB(name, id++, arrivalTime, cpuBurstTimes, ioBurstTimes, priority);
			allProcs.add(proc);
		}
		
		//ready to simulate the scheduling of those processes
		SchedulingAlgorithm scheduler = null;
		switch(alg) {
		case "FCFS":
			scheduler = new FCFS(allProcs);
			break;
		}
		scheduler.schedule();
	}

}
