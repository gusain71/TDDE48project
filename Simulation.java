package simulator;

public class Simulation {

	public static void main(String[] args) {
		
		int totalTime = 1000000;
		int[] AP = new int[totalTime];
		int energyUsed = 0;
		int transmissionCounter = 0;
		int wakeUpTime = 256;
		int internalCounter = 0;
		int energyUsageTrans = 10;
		int energyUsageWakeSleep = 5;
		int latencyIndexCounter = 0;
		int totalTransCounter = 0;
		
		int[] latencyPerTrans = new int[totalTime];
		int[] tempIndexStorage = new int[totalTime];
		
		AP = loadArray(totalTime, AP);
		
		for(int i = 0; i < totalTime; i++) {
			
			if(AP[i] == 1) {
				totalTransCounter++;
				transmissionCounter++;
				tempIndexStorage[latencyIndexCounter] = i;
				latencyIndexCounter++;
			}
			
			if(internalCounter == wakeUpTime) {
				if(transmissionCounter != 0) {
					energyUsed += (transmissionCounter * energyUsageTrans);
					transmissionCounter = 0;
					latencyPerTrans = latencyCalculator(tempIndexStorage, i, latencyIndexCounter, totalTransCounter, latencyPerTrans);
					tempIndexStorage = clearArray(tempIndexStorage, latencyIndexCounter);
					latencyIndexCounter = 0;
				}
				else {
					energyUsed += energyUsageWakeSleep;
				}
				
				internalCounter = 0;
			}
			internalCounter++;
		}
		
		System.out.println("Energy used: " + energyUsed);
		System.out.println("Average latency per transmission: " + latencyPerTransCalc(latencyPerTrans, totalTransCounter));
		
		/*
		for(int i = 0; i < totalTime; i++) {
			System.out.println(AP[i]);
		}
		*/
		
	}
	
	public static int[] loadArray(int totalTime, int[] AP) {
		
		int counter = 0;
		
		for(int i = 0; i < totalTime; i++) {
			if(counter == 0) {
				AP[i] = 1;
				counter++;
			}
			else {
				AP[i] = 0;
				counter++;
				if(counter == 100) {
					counter = 0;
				}
			}
		}
		
		return AP;
	}
	
	//Adds newly arrived latency-values to the existing array of all latency-values
	public static int[] latencyCalculator(int[] array, int arrayPos, int newTransmissions, int totalTrans, int[] latencyArray) {
		
		for(int i = 0; i < newTransmissions; i++) {
			latencyArray[(totalTrans-newTransmissions+i)] = (arrayPos-array[i]);
		}
		
		return latencyArray;
	}
	
	//Clears an array
	public static int[] clearArray(int[] array, int counter) {
		
		for(int i = 0; i <= counter; i++) {
			array[i] = 0;
		}
		return array;
	}
	
	//Calculates the average latency per transmission
	public static int latencyPerTransCalc(int[] array, int totalTrans) {
		
		int latency = 0;
		
		for(int i = 0; i < totalTrans; i++) {
			latency += array[i];
		}
		
		return (latency/totalTrans);
	}

}
