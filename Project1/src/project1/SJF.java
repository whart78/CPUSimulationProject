package project1;
import java.util.*;

// Shortest Job First (SJF) Scheduling Algorithm
public class SJF extends SchedulingAlgorithm {
    public SJF(List<PCB> queue, String schedulingMode, int stepsPerSecond) {
        super("SJF", queue);
        this.schedulingMode = schedulingMode;
		this.stepsPerSecond = stepsPerSecond;
    }

    @Override
    public PCB pickNextProcess() {
        if (readyQueue.isEmpty()) return null;
        PCB shortestJob = readyQueue.get(0);
        for (PCB proc : readyQueue) {
            if (proc.getCpuBursts().get(0) < shortestJob.getCpuBursts().get(0)) {
                shortestJob = proc;
            }
        }
        return shortestJob;
    }
}