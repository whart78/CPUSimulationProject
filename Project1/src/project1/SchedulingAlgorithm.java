package project1;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SchedulingAlgorithm {
    protected String name;		      //scheduling algorithm name
	protected List<PCB> allProcs;		//the initial list of processes
	protected List<PCB> readyQueue;	//ready queue of ready processes
	protected List<PCB> finishedProcs;	//list of terminated processes
	protected List<PCB> ioWaitingQueue; //list of processes waiting for IO
	protected List<String[]> logMessages; //list of log messages to store and print 
	protected List<String[]> performanceLogs;
	protected PCB curProcess; //current selected process by the scheduler
	protected PCB curIO; //current selected process for IO
	protected int systemTime; //system time or simulation time steps
	protected int cpuIdleTime; //track CPU idle time to calculate utilization
	protected String schedulingMode; //system time or simulation time steps
	protected int stepsPerSecond; //steps per second for automation
 
	public SchedulingAlgorithm(String name, List<PCB> queue) {
		this.name = name;
		this.allProcs = queue;
		this.readyQueue = new ArrayList<>();
		this.finishedProcs = new ArrayList<>();
		this.ioWaitingQueue = new ArrayList<>();
		this.logMessages = new ArrayList<>();
		this.performanceLogs = new ArrayList<>();
		cpuIdleTime = 0;
	}     
	
	public void schedule() throws IOException {
		
		Scanner sc = new Scanner(System.in);
		float interval = 0;
		long targetTime = 0;

		//if mode is automation, set a time interval.
		if(schedulingMode.equals("automation")) {
			interval = ((float) 1) / stepsPerSecond;
		}
		
		//  add code to complete the method		
		System.out.println("Scheduling algorithm: " + name);
		//- Print the name of the scheduling algorithm
		//- while (allProcs is not empty or readyQueue is not empty) {
		
		while(!allProcs.isEmpty() || !readyQueue.isEmpty() || curIO != null) {
			
			if(schedulingMode.equals("automation")) {
				long startTime = System.currentTimeMillis();
				targetTime = startTime + (long) (1000 * interval);
			}
		
			//Print the current system time
			System.out.println("\n\nSystem time: " + systemTime);
			
			//Move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
			for(PCB proc : allProcs) {
				if(proc.getArrivalTime() == systemTime) {
					readyQueue.add(proc);
					proc.setStatus("Arrived");
					addLogMessage(systemTime, "Process " + proc.getId() + " is created at " + systemTime);
				}
			}
			
			//remove ready processes from arrived processes
			allProcs.removeAll(readyQueue);
			//curProcess = pickNextProcess() //call pickNextProcess() to choose next process
			
			
			//if there are processes in the ready queue, continue to processing
			if(!readyQueue.isEmpty()) {
				
				//if(curProcess == null) {
					//select a process
					curProcess = pickNextProcess();
					
					//set start time if first time executing; start time would be 0.
					if(curProcess.getStartTime() == 0) {
						curProcess.setStartTime(systemTime);
					}
				//}
				
				//call print() to print simulation state at the beginning of this simulation step.
				print();
				
				//if the current process is not null (the system is not idle), continue to process.
				if(curProcess != null) {
					//update the start time of the selected process (curProcess)
					if(curProcess.getStartTime() < 0) 
						curProcess.setStartTime(systemTime);
					
						//Call CPU.execute() to let the CPU execute 1 CPU unit time of curProcess
						CPU.execute(curProcess, 1);
						curProcess.setStatus("Started");
						
						//Increase 1 to the waiting time of other processes in the ready queue
						for(PCB proc: readyQueue) 
							if(proc != curProcess) proc.increaseWaitingTime(1);
						
						//Check if the remaining CPU burst of curProcess = 0
						if(curProcess.getCpuBursts().get(0) == 0) {
							curProcess.getCpuBursts().removeFirst();
							if(curProcess.getCpuBursts().isEmpty()) {
								//remove curProcess from readyQueue
								readyQueue.remove(curProcess);
								//add curProcess to the finished queue (finishedProcs)
								finishedProcs.add(curProcess);
								//Update finishTime of curProcess 
								curProcess.setFinishTime(systemTime + 1);
								//terminates at systemTime + 1 because this will be printed on the next/correct iteration.
								addLogMessage(systemTime + 1, "Process " + curProcess.getId() + " terminated at " + (systemTime + 1));
							}
							//if process has ioBurst, add to io waiting queue.
							else if(!curProcess.getIOBursts().isEmpty()) {
								ioWaitingQueue.add(curProcess);
								readyQueue.remove(curProcess);
								addLogMessage(systemTime + 1, "Process " + curProcess.getId() + ": CPU to IO queue at " + (systemTime + 1));
							}
							curProcess = null;
						}
				}
				IOHandler();
				systemTime += 1;
			}
			//else there are no processes in the ready queue, thus a process cannot be selected for CPU execution. Handle IO. Print system state and iterate system time.
			else {
				print();
				IOHandler();
				systemTime += 1;
				cpuIdleTime +=1;
			}	
			
			//iteration method dependent on mode
			if(schedulingMode.equals("manual")) {
				System.out.println("Press enter to continue: ");
				sc.nextLine();
			}
			else if (schedulingMode.equals("automation")) {
				while(System.currentTimeMillis() < targetTime) {/*wait*/}
			}
		}
				
		//Print out last iteration for program
		System.out.println("\n\nSystem time: " + systemTime);
		print();
		
		//ask user about savings 
		boolean inputBoolean = true;
		String userInput = "";
		
		do {
			inputBoolean = true;
			System.out.print("\nWould you like to save your execution logs and system performance to a text file? (Y/N): ");
			userInput = sc.nextLine().toLowerCase();
			if(!userInput.equals("y") && !userInput.equals("n")) {
				System.out.println("Invalid input. Please try again.");
				inputBoolean = false;
			}
		} while(inputBoolean == false);
		
		
		
		if(userInput.equals("y")) {	
			String filename = "";
			do {
				System.out.print("Enter file name without extension: ");
				filename = sc.nextLine();
				Pattern pattern = Pattern.compile("[#%&{}\\<>*?/$!\'\":@+`|=]", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(filename);
				inputBoolean = matcher.find();
				if(inputBoolean == true) {
					System.out.println("Detected a possible illegal character for filename. Try something simple with [a-z] and [0-9].");
				}
		
			} while(inputBoolean == true);
			
			FileWriter writer1 = new FileWriter(filename + ".txt");
			writer1.write("Execution Logs and Performance\n\n");
			writer1.write("Scheduling Algorithm: " + name + "\n"); 
			writer1.write(String.format("CPU Utilization: %.2f %%\n", getUtilization())); 
			writer1.write(String.format("Throughput: %.2f\n", getThroughput())); 
			writer1.write(String.format("Average Turnaround: %.2f\n", getAverageTurnaroundTime())); 
			writer1.write(String.format("Average Wait: %.2f\n\n", getAverageWaitTime())); 
			
			for(String[] message : logMessages) {
				writer1.write(message[1] + "\n");
			}
			
			writer1.close();
		}
		
		do {
			inputBoolean = true;
			System.out.print("Would you like to save your raw performance data to a csv file? (Y/N): ");
			userInput = sc.nextLine().toLowerCase();
			if(!userInput.equals("y") && !userInput.equals("n")) {
				System.out.println("Invalid input. Please try again.");
				inputBoolean = false;
			}
		} while(inputBoolean == false);
		
		if(userInput.equals("y")) {	
			String filename = "";
			do {
				System.out.print("Enter file name without extension: ");
				filename = sc.nextLine();
				Pattern pattern = Pattern.compile("[#%&{}\\<>*?/$!\'\":@+`|=]", Pattern.CASE_INSENSITIVE);
				Matcher matcher = pattern.matcher(filename);
				inputBoolean = matcher.find();
				if(inputBoolean == true) {
					System.out.println("Detected a possible illegal character for filename. Try something simple with [a-z] and [0-9].");
				}
			} while(inputBoolean == true);
			FileWriter writer2 = new FileWriter(filename + ".csv");
			for(String[] log : performanceLogs) {
				writer2.write(log[0] + "," + log[1] + "," + log[2] + "," + log[3] + "," + log[4] + "\n");
			}
			writer2.close();
		}
			
	}
	
	  //Selects the next task using the appropriate scheduling algorithm
      public abstract PCB pickNextProcess();
      
      //IO handler selects next IO burst if none from waiting queue. If one is already selected, execute.
      //If the burst is completed, remove the burst, send it back the ready queue and set the current IO to null.
      private void IOHandler() {
    	  
    	  	//Is there are processes waiting for IO and there is no current IO, pick the first.
    	  	if(!ioWaitingQueue.isEmpty() && curIO == null) {
    	  		curIO = ioWaitingQueue.getFirst();
    	  	}
    	  	//Else if there a process already selected for IO, execute.
    	  	else if (curIO != null){
    	  		IO.execute(curIO, 1);
    	  		//Increase 1 to the waiting time of other processes in the waiting queue
				for(PCB proc: ioWaitingQueue) 
					if(proc != curIO) proc.increaseIOWaitingTime(1);
    	  	}
    	  	
    	  	//A check to see if the process is done with IO. If done, move it to readyQueue.
    	  	if(curIO != null && curIO.getIOBursts().get(0) == 0) {
				curIO.getIOBursts().removeFirst();
				ioWaitingQueue.removeFirst();
				readyQueue.add(curIO);
				addLogMessage(systemTime + 1, "Process " + curIO.getId() + ": IO to ready queue at " + (systemTime + 1));
				if(!ioWaitingQueue.isEmpty()) {
					curIO = ioWaitingQueue.getFirst();
				}
				else {
					curIO = null;
				}
			}
      }
      
      //when called, returns the average waiting time for the entire program.
      public double getAverageWaitTime() {
    	  
		  //metric variables
		  int totalProcesses = 0;
		  int totalWaitTime = 0;
		  
		  totalProcesses = finishedProcs.size() + readyQueue.size() + ioWaitingQueue.size();
		  
		  // if no processes, return 0.
		  if(totalProcesses == 0) {
			  return 0;
		  }
    	  
    	  //get total wait time
		  for(PCB proc : finishedProcs) {
				totalWaitTime += proc.getWaitingTime();
		  }
		  
		  for(PCB proc : readyQueue) {
			  	totalWaitTime += proc.getWaitingTime();
		  }
		  
		  for(PCB proc : ioWaitingQueue) {
			  	totalWaitTime += proc.getWaitingTime();
		  }
		  
		  return (double) totalWaitTime / totalProcesses;
      }
      
    //when called, returns the average turnaround time for the entire program.
      public double getAverageTurnaroundTime() {
    	  int sumCompletionTime = 0;
    	  int sumArrivalTime = 0;
    	  
    	  if(finishedProcs.size() == 0) {
    		  return 0;
    	  }
    	  
    	  for(PCB proc : finishedProcs) {
    		  sumCompletionTime += proc.getFinishTime();
    		  sumArrivalTime += proc.getArrivalTime();
    	  }
    	  
    	  return (double) (sumCompletionTime - sumArrivalTime) / finishedProcs.size();
      }
      
      public double getUtilization() {
    	  if(systemTime == 0) {
    		  return 0;
    	  }
    	  return (double) (systemTime - cpuIdleTime) / systemTime * 100;
      }
      
      public double getThroughput() {
    	  if(systemTime == 0) {
    		  return 0;
    	  }
    	  return (double) finishedProcs.size() / systemTime;
      }
      
      public void addLogMessage(int systemTime, String message) {
    	  String[] logMessage = {Integer.toString(systemTime), message};
    	  logMessages.add(logMessage);
      }
      
      //performance log is an array with the structure of [system time, utilization, throughput, turnaround, waiting time]
      public void addPerformanceLog(int systemTime, double cpuUtilization, double throughput, double turnaround, double waitingTime) {
    	  String[] performanceLog = {Integer.toString(systemTime).trim(), Double.toString(cpuUtilization).trim(), Double.toString(throughput).trim(), Double.toString(turnaround).trim(), Double.toString(waitingTime).trim()};
    	  performanceLogs.add(performanceLog);
      }

      //print simulation step
      public void print() {
		System.out.println("CPU: " + ((curProcess == null) ? "idle" : curProcess.getName()));
		System.out.println("IO: " + ((curIO == null) ? "idle" : curIO.getName()));
		
		//get performance metrics
		double cpuUtilization = getUtilization();
		double throughput = getThroughput();
		double turnaround = getAverageTurnaroundTime();
		double waitingTime = getAverageWaitTime();
		
		//print performance metrics
		System.out.printf("CPU Utilization: %.2f %%\n", cpuUtilization);
		System.out.printf("Throughput: %.2f\n", throughput);
		System.out.printf("Average Turnaround: %.2f\n", turnaround);
		System.out.printf("Average Wait: %.2f\n", waitingTime);
	
		//add performance log
		addPerformanceLog(systemTime, cpuUtilization, throughput, turnaround, waitingTime);
		
		if(curProcess != null) {
			System.out.println("Current Process: \n  CPU " + curProcess);
		}
		if(curIO != null) {
			System.out.println("  IO " + curIO);
		}
		
		if (readyQueue.size() > 1) {
			System.out.println("Ready Processes: ");
			for(PCB proc : readyQueue) {
				if(proc != curProcess) {
					System.out.println("  " + proc);
				}
			}
		}
		
		if(ioWaitingQueue.size() > 1) {
			System.out.println("Processes Waiting for IO: ");
			for(PCB proc : ioWaitingQueue) {
				if(proc != curIO )
					System.out.println("  " + proc);
			}
		}
		//print finished processes
		if(!finishedProcs.isEmpty()) {
			System.out.println("Finished Processes: ");
			for(PCB proc : finishedProcs) {
				System.out.println("  " + proc);
			}
		}
		
		//print log messages
		if(!logMessages.isEmpty()) {
			for(String[] message : logMessages) {
				if(Integer.parseInt(message[0]) == systemTime) {
					System.out.println(message[1]);
				}
			}
		}
    }
}


