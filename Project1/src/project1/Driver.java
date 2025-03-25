package project1;

import java.util.*;
import java.io.*;

import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws FileNotFoundException {
		//read the set of input processes
		Scanner sc = new Scanner(new File("src/proc.txt"));
		String alg = sc.nextLine(); //read the selected algorithm
		String line;
		int id = 1;
		ArrayList<PCB> allProcs = new ArrayList<>(); //list of processes
		while(sc.hasNextLine()) {
			line = sc.nextLine(); //read a line from the file
			String[] arr = line.split(", ");
			if(arr.length % 2 == 0 && arr.length >= 4) {
				String name = arr[0];
				int arrivalTime = Integer.parseInt(arr[1].trim());
				//int cpuBurstTime = Integer.parseInt(arr[2].trim());
				
				//add cpu bursts into a list
				List<Integer> cpuBursts = new ArrayList<>();
				for(int i = 3; i < arr.length; i++) {
					if(i % 2 != 0) {
						cpuBursts.add(Integer.parseInt(arr[i]));
					}
				}
				
				//add io bursts into a list
				List<Integer> ioBursts = new ArrayList<>();
				for(int i = 4; i < arr.length; i++) {
					if(i % 2 == 0) {
						ioBursts.add(Integer.parseInt(arr[i]));
					}
				}
				
				int priority = Integer.parseInt(arr[2].trim());
				PCB proc = new PCB(name, id++, arrivalTime, cpuBursts, ioBursts, priority);
				allProcs.add(proc);
			}
		}
		
		//ready to simulate the scheduling of those processes
		SchedulingAlgorithm scheduler = null;
		switch(alg) {
		case "FCFS":
			scheduler = new FCFS(allProcs, "Automation", 10);
			break;
		}
		scheduler.schedule();
	}

}

