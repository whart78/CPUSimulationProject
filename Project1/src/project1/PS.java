package project1;
import java.util.*;

// Priority Scheduling Algorithm
public class PS extends SchedulingAlgorithm {
    public PS(List<PCB> queue) {
        super("Priority Scheduling", queue);
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

