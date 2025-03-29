package project1;

import java.util.*;
import java.io.*;

public class Driver {

	public static void main(String[] args) throws IOException {
		Scanner scanner = new Scanner(System.in);
		Scanner sc = null;
		boolean inputBoolean;
		String fileName = "";
		do {
			try {
				inputBoolean = true;
				// Ask for user to input file name
				System.out.print("Please enter the file path with extension: ");
				fileName = scanner.nextLine();
				// read the set of input processes
				sc = new Scanner(new File(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("File was not found. Try again.");
				inputBoolean = false;
			}
		} while (inputBoolean == false);

		while (true) {
		
			sc = new Scanner(new File(fileName));
			// String alg = sc.nextLine(); //read the selected algorithm
			String line;
			int id = 1;
			ArrayList<PCB> allProcs = new ArrayList<>(); // list of processes
			while (sc.hasNextLine()) {
				line = sc.nextLine(); // read a line from the file
				String[] arr = line.split(", ");
				if (arr.length % 2 == 0 && arr.length >= 4) {
					String name = arr[0];
					int arrivalTime = Integer.parseInt(arr[1].trim());
					// int cpuBurstTime = Integer.parseInt(arr[2].trim());
	
					// add cpu bursts into a list
					List<Integer> cpuBursts = new ArrayList<>();
					for (int i = 3; i < arr.length; i++) {
						if (i % 2 != 0) {
							cpuBursts.add(Integer.parseInt(arr[i]));
						}
					}
	
					// add io bursts into a list
					List<Integer> ioBursts = new ArrayList<>();
					for (int i = 4; i < arr.length; i++) {
						if (i % 2 == 0) {
							ioBursts.add(Integer.parseInt(arr[i]));
						}
					}
	
					int priority = Integer.parseInt(arr[2].trim());
					PCB proc = new PCB(name, id++, arrivalTime, cpuBursts, ioBursts, priority);
					allProcs.add(proc);
				}
			}
		
			//ask user for scheduling mode
			String schedulingMode = "";
			while (!schedulingMode.equals("automation") && !schedulingMode.equals("manual")) {
				System.out.print("Choose scheduling mode (Automation or Manual): ");
				schedulingMode = scanner.nextLine().toLowerCase();
				if (!schedulingMode.equals("automation") && !schedulingMode.equals("manual")) {
					System.out.println("Invalid input, please enter 'automation' or 'manual'");
				}
			}

			//check whether mode is automation to ask for steps per second
			int stepsPerSecond = -1;
			if(schedulingMode.equals("automation")) {
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
			}

			String alg = "";
			System.out.print("Choose one of the following scheduling algorithms: 'FCFS', 'SJF', 'RR', 'PF': ");
			alg = scanner.nextLine().toUpperCase();
			
			// ready to simulate the scheduling of those processes
			SchedulingAlgorithm scheduler = null;
			
			switch (alg) {
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
						} catch (NumberFormatException e) {
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
			
			System.out.print("Press enter to start the program: ");
			scanner.nextLine();

			// test
			scheduler.schedule();
			String userInput = "";
			do {
				inputBoolean = true;
				System.out.print("Would you like to continue? (Y/N): ");
				userInput = scanner.nextLine().toLowerCase();
				if (!userInput.equals("y") && !userInput.equals("n")) {
					System.out.println("Invalid input. Please try again.");
					inputBoolean = false;
				}
			} while (inputBoolean == false);
			if (userInput.equals("y")) {
				System.out.println();
			} else {
				break;
			}

		}
		sc.close();
		scanner.close();
		System.out.print("Program has terminated.");
	}

}


