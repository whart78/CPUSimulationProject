package project1;

// Round Robin (RR) Scheduling Algorithm
public class RoundRobin extends SchedulingAlgorithm {
    private int timeQuantum;
    private int currentIndex;

    public RoundRobin(List<PCB> queue, int timeQuantum) {
        super("Round Robin", queue);
        this.timeQuantum = timeQuantum;
        this.currentIndex = 0;
    }

    @Override
    public PCB pickNextProcess() {
        if (readyQueue.isEmpty()) return null;
        PCB nextProcess = readyQueue.get(currentIndex);
        currentIndex = (currentIndex + 1) % readyQueue.size();
        return nextProcess;
    }
}
