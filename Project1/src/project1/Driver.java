package project1;

import java.util.*;
import java.io.*;

import java.util.*;
import java.io.*;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		Scanner sc = null;
		boolean inputBoolean;
		
		do {
			try {
				inputBoolean = true;
				//Ask for user to input file name
				System.out.print("Please enter the file name with extension: ");
				String fileName = scanner.nextLine();
				//read the set of input processes
				sc = new Scanner(new File("files_input/" + fileName));
			} catch(FileNotFoundException e) {
				System.out.println("File was not found. Try again.");
				inputBoolean = false;
			}
		} while(inputBoolean == false);
		
		//String alg = sc.nextLine(); //read the selected algorithm
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
		
		String schedulingMode = "";
		while (!schedulingMode.equals("automation") && !schedulingMode.equals("manual")) {
			System.out.print("Choose scheduling mode (Automation or Manual): ");
			schedulingMode = scanner.nextLine().toLowerCase();
			if (!schedulingMode.equals("automation") && !schedulingMode.equals("manual")) {
				System.out.println("Invalid input, please enter 'automation' or 'manual'");
			}
		}
		
		int stepsPerSecond = -1;
		while (stepsPerSecond <= 0) {
			System.out.print("Choose number of simulation steps per second (must be >= 1): ");
			try {
				stepsPerSecond = Integer.parseInt(scanner.nextLine());
				if (stepsPerSecond <= 0) {
					System.out.println("Number of simulation steps must be greater than 0.");
				}
			
			} catch (NumberFormatException e) {
				System.out.println("Invalid input, please enter a valid integer.");
			}

		}	
		
		String alg = "";
		System.out.print("Choose one of the following scheduling algorithms: 'FCFS', 'SJF', 'RR', 'PF': ");
		alg = scanner.nextLine();
		//ready to simulate the scheduling of those processes
		SchedulingAlgorithm scheduler = null;
		switch(alg) {
		case "FCFS":
			scheduler = new FCFS(allProcs, schedulingMode, stepsPerSecond);
			break;
		case "SJF":
			scheduler = new SJF(allProcs, schedulingMode, stepsPerSecond);
			break;
		case "RR":
			int timeQuantum = -1;
			while (timeQuantum <= 0) {
				System.out.print("Enter a quantum time for RR: ");
				try {
					timeQuantum = Integer.parseInt(scanner.nextLine());
					if (timeQuantum <= 0) {
						System.out.println("Invalid input, please enter an integer above 0.");
					}
				} 
				catch (NumberFormatException e) {
					System.out.println("Invalid input, please enter a valid integer.");
				}
			}
			scheduler = new RR(allProcs, schedulingMode, stepsPerSecond, timeQuantum);
			break;
		case "PF":
			scheduler = new PF(allProcs, schedulingMode, stepsPerSecond);
			break;
		default:
			System.out.println("Please enter one of the following 'FCFS', 'SJF', 'RR', 'PF'.");
			break;
		}
		
		//test
		scheduler.schedule();
		sc.close();
		scanner.close();
	}
	
}


