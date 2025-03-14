package project1;

import java.util.ArrayList;
import java.util.List;

public class PCB {
	// the representation of each process
	private String name;     // process name
	private int id;          // process id
	private int arrivalTime; // arrival time of the process
	//private int cpuBurst;    // CPU burst length in unit time
	private int priority;    // priority level of the process
	private int numOfCpuBursts;
	private int numOfIOBursts;
	private List<Integer> cpuBursts;
	private List<Integer> ioBursts;
	
	// the statistics of process execution
	private int startTime, finishTime, turnaroundTime, waitingTime;

	// constructor
	public PCB(String name, int id, int arrivalTime, List<Integer> cpuBursts, List<Integer> ioBursts,
                                                       int priority) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBursts = cpuBursts;
		this.ioBursts = ioBursts;
		this.numOfCpuBursts = cpuBursts.size();
		this.numOfIOBursts = ioBursts.size();
		this.priority = priority;
		this.startTime = 0;
		this.finishTime = 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	//public int getCpuBurst() {
	//	return cpuBurst;
	//}

	//public void setCpuBurst(int cpuBurst) {
	//	this.cpuBurst = cpuBurst;
	//}
	
	public List<Integer> getCpuBursts() {
		return cpuBursts;
	}
	
	public void setCpuBursts(List<Integer> cpuBursts) {
		this.cpuBursts = cpuBursts;
	}
	
	public List<Integer> getIOBursts() {
		return ioBursts;
	}

	public void setIOBursts(List<Integer> ioBursts) {
		this.ioBursts = ioBursts;
	}

	public int getNumOfCpuBursts() {
		return numOfCpuBursts;
	}

	public void setNumOfCpuBursts(int numOfCpuBursts) {
		this.numOfCpuBursts = numOfCpuBursts;
	}

	public int getNumOfIOBursts() {
		return numOfIOBursts;
	}

	public void setNumOfIOBursts(int numOfIOBursts) {
		this.numOfIOBursts = numOfIOBursts;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(int finishTime) {
		this.finishTime = finishTime;
		this.turnaroundTime = finishTime - arrivalTime;
	}

	public int getTurnaroundTime() {
		return turnaroundTime;
	}

	public void setTurnaroundTime(int turnaroundTime) {
		this.turnaroundTime = turnaroundTime;
	}

	public int getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(int waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	public void increaseWaitingTime(int burst) {
		//Increase the waitingTime variable with burst.
		this.waitingTime += burst;
	}
	
	public String toString() {
		return "Process [ID: " + id 
			    + " | Arrival Time: " + arrivalTime + " | Priority: " + priority + " | CPU Bursts: " + cpuBursts +
			      " | IO Bursts: " + ((ioBursts.isEmpty()) ? "none" : ioBursts) + " | Finish Time: " + finishTime + 
			      " | Wait Time: " + waitingTime + "]";
	}

} 

