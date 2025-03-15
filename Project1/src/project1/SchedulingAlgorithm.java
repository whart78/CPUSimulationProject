package project1;
import java.util.*;

public abstract class SchedulingAlgorithm {
      protected String name;		      //scheduling algorithm name
	protected List<PCB> allProcs;		//the initial list of processes
	protected List<PCB> readyQueue;	//ready queue of ready processes
	protected List<PCB> finishedProcs;	//list of terminated processes
	protected List<PCB> ioWaitingQueue;
	protected PCB curProcess; //current selected process by the scheduler
	protected PCB curIO;
	protected int systemTime; //system time or simulation time steps
 
      public SchedulingAlgorithm(String name, List<PCB> queue) {
    	      this.name = name;
    	      this.allProcs = queue;
    	      this.readyQueue = new ArrayList<>();
    	      this.finishedProcs = new ArrayList<>();
    	      this.ioWaitingQueue = new ArrayList<>();
      }
	
	public void schedule() {
		//  add code to complete the method		
		System.out.println("Scheduling algorithm: " + name);
		//- Print the name of the scheduling algorithm
		//- while (allProcs is not empty or readyQueue is not empty) {
		while(!allProcs.isEmpty() || !readyQueue.isEmpty() || curIO != null) {
			//Print the current system time
			System.out.println("\n\nSystem time: " + systemTime);
			
			//Move arrived processes from allProcs to readyQueue (arrivalTime = systemTime)
			for(PCB proc : allProcs) {
				if(proc.getArrivalTime() == systemTime)
					readyQueue.add(proc);
					proc.setStatus("Arrived");
			}
			
			//remove ready processes from arrived processes
			allProcs.removeAll(readyQueue);
			//curProcess = pickNextProcess() //call pickNextProcess() to choose next process
			
			
			//if there are processes in the ready queue, continue to processing
			if(!readyQueue.isEmpty()) {
				
				//select a process
				curProcess = pickNextProcess();
				curProcess.setStatus("Started");
				
				//remove the selected process from the ready queue.
				readyQueue.remove(curProcess);
				
				//call print() to print simulation state at the beginning of this simulation step.
				print();
				
				//if the current process is not null (the system is not idle), continue to process.
				if(curProcess != null) {
					//update the start time of the selected process (curProcess)
					if(curProcess.getStartTime() < 0) 
						curProcess.setStartTime(systemTime);
					
						//Call CPU.execute() to let the CPU execute 1 CPU unit time of curProcess
						CPU.execute(curProcess, 1);
						
						//Increase 1 to the waiting time of other processes in the ready queue
						for(PCB proc: readyQueue) 
							if(proc != curProcess) proc.increaseWaitingTime(1);
						
						//Increase systemTime by 1
						systemTime += 1;
						
						//add the current process back to ready queue to be assessed.
						readyQueue.add(curProcess);
						
						//Check if the remaining CPU burst of curProcess = 0
						if(curProcess.getCpuBursts().get(0) == 0) {
							curProcess.getCpuBursts().removeFirst();
							
							if(curProcess.getCpuBursts().isEmpty()) {
								//remove curProcess from readyQueue
								readyQueue.remove(curProcess);
								//add curProcess to the finished queue (finishedProcs)
								finishedProcs.add(curProcess);
								//Update finishTime of curProcess
								curProcess.setFinishTime(systemTime);
								//Print to the console a message displaying the process name, terminated time, startTime, turnaroundTime, waitingTime
								System.out.println("Process " + curProcess.getId() + " terminated at " + systemTime
										+ ". Turnaround time: " +curProcess.getTurnaroundTime()
										+ ". Waiting time: " + curProcess.getWaitingTime());
							}
							//if process has ioBurst, add to io waiting queue.
							else if(!curProcess.getIOBursts().isEmpty()) {
								ioWaitingQueue.add(curProcess);
								readyQueue.remove(curProcess);
							}
					
						}
						//handle IO 
						//IOHandler();
				}
				//else no current process, iterate system time.
				else {
					systemTime +=1;
				}
				//handle IO 
				IOHandler();
			}
			//else there are no processes in the ready queue. Handle IO, thus a process cannot be selected for CPU execution. Print system state and iterate system time.
			else {
				IOHandler();
				curProcess = null;
				print();
				systemTime += 1;
			}
		}
	}
	
	  //Selects the next task using the appropriate scheduling algorithm
      public abstract PCB pickNextProcess();
      
      //IO handler selects next IO burst if none from waiting queue. If one is already selected, execute.
      //If the burst is completed, remove the burst, send it back the ready queue and set the current IO to null.
      private void IOHandler() {
    	  
    	  	if(!ioWaitingQueue.isEmpty() && curIO == null) {
    	  		curIO = ioWaitingQueue.get(0);
    	  	}
    	  	else if (curIO != null){
    	  		IO.execute(curIO, 1);
    	  	}
			
			if(curIO != null && curIO.getIOBursts().get(0) == 0) {
				curIO.getIOBursts().removeFirst();
				ioWaitingQueue.removeFirst();
				readyQueue.add(curIO);
				curIO = null;
			}
      }

      //print simulation step
      public void print() {
		System.out.println("CPU: " + ((curProcess == null) ? "idle" : curProcess.getName()));
		System.out.println("IO: " + ((curIO == null) ? "idle" : curIO.getName()));
		
		if (curProcess != null) {
			System.out.println("Current Process: \n  CPU " + curProcess);
			if(curIO != null) {
				System.out.println("  IO " + curIO);
			}
		}
		
		if (!readyQueue.isEmpty()) {
			System.out.println("Ready Processes: ");
			for(PCB proc : readyQueue) {
				
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


