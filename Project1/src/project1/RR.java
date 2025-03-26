package project1;

import java.util.List;

// Round Robin (RR) Scheduling Algorithm
public class RR extends SchedulingAlgorithm {
    private int timeQuantum;
    private int timeQuantumCounter;
    private int currentIndex;
    private int lastSeenPID;

    public RR(List<PCB> queue, String schedulingMode, int stepsPerSecond, int timeQuantum) {
        super("RR", queue);
        this.timeQuantum = timeQuantum;
        this.timeQuantumCounter = 0;
        this.currentIndex = 0;
        lastSeenPID = 0;
        this.schedulingMode = schedulingMode;
		this.stepsPerSecond = stepsPerSecond;
		
    }

    
    @Override
    public PCB pickNextProcess() {
    	PCB nextProcess = null;
        if (readyQueue.isEmpty()) {
        	return null;
        }
        else {
        	//if first time seeing, set as last seen and set counter to 1.
        	if(readyQueue.getFirst().getId() != lastSeenPID) {
        		nextProcess = readyQueue.getFirst();
        		lastSeenPID = nextProcess.getId();
        		timeQuantumCounter = 1;
        	}
        	else if(readyQueue.getFirst().getId() == lastSeenPID && timeQuantumCounter < timeQuantum) {
        		nextProcess = readyQueue.getFirst();
        		timeQuantumCounter++;
        	}
        	else if(readyQueue.getFirst().getId() == lastSeenPID && timeQuantumCounter >= timeQuantum) {
        		PCB tempProcess = readyQueue.getFirst();
        		readyQueue.removeFirst();
        		readyQueue.addLast(tempProcess);
        		nextProcess = readyQueue.getFirst();
        		lastSeenPID = nextProcess.getId();
        		timeQuantumCounter = 1;
        	}
        }
   
        return nextProcess;
    }
    
    /*
    @Override
    public PCB pickNextProcess() {
        if (readyQueue.isEmpty()) return null;
        PCB nextProcess = readyQueue.get(currentIndex);
        currentIndex = (currentIndex + 1) % readyQueue.size();
        return nextProcess;
    }
    */
}