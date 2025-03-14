package project1;

import java.util.List;

public class FCFS extends SchedulingAlgorithm {
      public FCFS(List<PCB> queue) {
		super("FCFS", queue);
	}


      public PCB pickNextProcess() {
		// TODO Auto-generated method stub
    	  PCB earliestArrivalTime = readyQueue.get(0);
    	  
    	  if(readyQueue.size() > 1) {
	    	  for(int i = 1; i < readyQueue.size(); i++) {
	    		  if(readyQueue.get(i).getArrivalTime() < earliestArrivalTime.getArrivalTime()) {
	    			  earliestArrivalTime = readyQueue.get(i);
	    		  }
	    	  }
    	  }
    	  return earliestArrivalTime;
      }
}
