package project1;
import java.io.File;
import java.util.*;
import java.util.Scanner;

public abstract class SchedulingAlgorithm {
      protected String name;		      //scheduling algorithm name
	protected List<PCB> allProcs;		//the initial list of processes
	protected List<PCB> readyQueue;	//ready queue of ready processes
	protected List<PCB> finishedProcs;	//list of terminated processes
	protected List<PCB> ioWaitingQueue;
	protected PCB curProcess; //current selected process by the scheduler
	protected PCB curIO;
	protected int systemTime; //system time or simulation time steps
	protected int cpuIdleTime; //track CPU idle time to calculate utilization
	protected String schedulingMode; //system time or simulation time steps
	protected int stepsPerSecond; //steps per second for automation
	protected int unitsCompleted;
 
	  public SchedulingAlgorithm(String name, List<PCB> queue) {
		      this.name = name;
		      this.allProcs = queue;
		      this.readyQueue = new ArrayList<>();
		      this.finishedProcs = new ArrayList<>();
		      this.ioWaitingQueue = new ArrayList<>();
		      cpuIdleTime = 0;
		      unitsCompleted = 0;
	  }     
	
	public void schedule() {
		
		Scanner sc = new Scanner(System.in);
		float interval = 0;
		long targetTime = 0;

		//if mode is automation, set a time interval.
		if(schedulingMode.equals("Automation")) {
			interval = ((float) 1) / stepsPerSecond;
		}
		
		//  add code to complete the method		
		System.out.println("Scheduling algorithm: " + name);
		//- Print the name of the scheduling algorithm
		//- while (allProcs is not empty or readyQueue is not empty) {
		
		while(!allProcs.isEmpty() || !readyQueue.isEmpty() || curIO != null) {
			
			if(schedulingMode.equals("Automation")) {
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
				}
			}
			
			//remove ready processes from arrived processes
			allProcs.removeAll(readyQueue);
			//curProcess = pickNextProcess() //call pickNextProcess() to choose next process
			
			
			//if there are processes in the ready queue, continue to processing
			if(!readyQueue.isEmpty()) {
				
				if(curProcess == null) {
					//select a process
					curProcess = pickNextProcess();
					
					//set start time if first time executing; start time would be 0.
					if(curProcess.getStartTime() == 0) {
						curProcess.setStartTime(systemTime);
					}
				}
				
				//remove the selected process from the ready queue.
				//readyQueue.remove(curProcess);
				
				//print system metrics
				//System.out.printf("CPU Utilization: %.2f %%\n", ((double) (systemTime - cpuIdleTime) / systemTime) * 100);
				//System.out.printf("Throughout: %.2f\n", (double) unitsCompleted / systemTime);
				//System.out.printf("Average Wait: %.2f\n", getAverageWaitTime());
				
				//call print() to print simulation state at the beginning of this simulation step.
				print();
				
				//if the current process is not null (the system is not idle), continue to process.
				if(curProcess != null) {
					//update the start time of the selected process (curProcess)
					if(curProcess.getStartTime() < 0) 
						curProcess.setStartTime(systemTime);
					
						//Call CPU.execute() to let the CPU execute 1 CPU unit time of curProcess
						CPU.execute(curProcess, 1);
						unitsCompleted += 1;
						curProcess.setStatus("Started");
						
						//Increase 1 to the waiting time of other processes in the ready queue
						for(PCB proc: readyQueue) 
							if(proc != curProcess) proc.increaseWaitingTime(1);
						
						//add the current process back to ready queue to be assessed.
						//readyQueue.add(curProcess);
						
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
								//Print to the console a message displaying the process name, terminated time, startTime, turnaroundTime, waitingTime
								//System.out.println("Process " + curProcess.getId() + " terminated at " + systemTime
								//		+ ". Turnaround time: " +curProcess.getTurnaroundTime()
								//		+ ". Waiting time: " + curProcess.getWaitingTime());
							}
							//if process has ioBurst, add to io waiting queue.
							else if(!curProcess.getIOBursts().isEmpty()) {
								ioWaitingQueue.add(curProcess);
								readyQueue.remove(curProcess);
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
			if(schedulingMode.equals("Manual")) {
				System.out.println("Press enter to continue: ");
				sc.nextLine();
			}
			else if (schedulingMode.equals("Automation")) {
				while(System.currentTimeMillis() < targetTime) {/*wait*/}
			}
		}
		//Print out last iteration for program
		System.out.println("\n\nSystem time: " + systemTime);
		print();
		
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
				if(!ioWaitingQueue.isEmpty()) {
					curIO = ioWaitingQueue.getFirst();
				}
				else {
					curIO = null;
				}
			}
      }
      
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

      //print simulation step
      public void print() {
		System.out.println("CPU: " + ((curProcess == null) ? "idle" : curProcess.getName()));
		System.out.println("IO: " + ((curIO == null) ? "idle" : curIO.getName()));
		
		//print system metrics
		System.out.printf("CPU Utilization: %.2f %%\n", ((systemTime != 0) ? ((double) (systemTime - cpuIdleTime) / systemTime) * 100 : 0));
		System.out.printf("Throughout: %.2f\n", ((systemTime != 0) ? (double) finishedProcs.size() / systemTime : 0));
		System.out.printf("Average Turnaround: %.2f\n", getAverageTurnaroundTime());
		System.out.printf("Average Wait: %.2f\n", getAverageWaitTime());
		
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
    }
}


