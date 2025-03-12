package project1;

import java.util.ArrayList;
import java.util.List;

public class PCB {
	// the representation of each process
	private String name;     // process name
	private int id;          // process id
	private String state;
	private int arrivalTime; // arrival time of the process
	private int priority;    // priority level of the process
	
	// lists to hold cpu and io bursts
	List<Integer> cpuBursts = new ArrayList<>();
	List<Integer> ioBursts = new ArrayList<>();
	
	// the statistics of process execution
	private int startTime, finishTime, turnaroundTime, waitingTime, ioWaitTime;

	// constructor
	public PCB(String name, int id, int arrivalTime, List<Integer> cpuBursts, 
										List<Integer> ioBursts, int priority) {
		super();
		this.name = name;
		this.id = id;
		this.arrivalTime = arrivalTime;
		this.cpuBursts = cpuBursts;
		this.ioBursts = ioBursts;
		this.priority = priority;
		this.startTime = -1;
		this.finishTime = -1;
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
	
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(int arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public List<Integer> getCpuBursts() {
		return cpuBursts;
	}

	public void setCpuBursts(List<Integer> cpuBursts) {
		this.cpuBursts = cpuBursts;
	}

	public List<Integer> getIoBursts() {
		return ioBursts;
	}

	public void setIoBursts(List<Integer> ioBursts) {
		this.ioBursts = ioBursts;
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
	
	public int getIoWaitTime() {
		return ioWaitTime;
	}

	public void setIoWaitTime(int ioWaitTime) {
		this.ioWaitTime = ioWaitTime;
	}

	public String toString() {
		return "Process [name=" + name + ", id=" + id 
			    + ", arrivalTime=" + arrivalTime + ", cpuBurst=" + cpuBursts.get(0) 
			    + ", priority=" + priority + "]";
	}

} 
