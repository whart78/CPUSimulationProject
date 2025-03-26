package project1;
import java.util.*;

// Priority Scheduling Algorithm
public class PF extends SchedulingAlgorithm {
    public PF(List<PCB> queue, String schedulingMode, int stepsPerSecond) {
        super("Priority Scheduling", queue);
        this.schedulingMode = schedulingMode;
		this.stepsPerSecond = stepsPerSecond;
    }

    @Override
    public PCB pickNextProcess() {
        if (readyQueue.isEmpty()) return null;
        PCB highestPriorityProcess = readyQueue.get(0);
        for (PCB proc : readyQueue) {
            if (proc.getPriority() < highestPriorityProcess.getPriority()) {
                highestPriorityProcess = proc;
            }
        }
        return highestPriorityProcess;
    }
}

