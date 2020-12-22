package zad_3;

import java.util.ArrayList;
import java.util.Random;

import zad_3.Chart;

public class Process {
	
	private Loader loader;
	private FileData[] filesData;
	private Configuration cfg;
	private int iter;
	private int k_size;
	private boolean isSwap;
	private boolean isInversion;
	private int runs;
	private double startTemperature;
	private double alpha;
	
	public Process() {
		this.cfg = new Configuration();
		this.loader = new Loader();
		this.iter = cfg.getIter();
		this.k_size = cfg.getN_size();
		this.isSwap = cfg.isSwap();
		this.isInversion = cfg.isInversion();
		this.runs = cfg.getRuns();
		this.startTemperature = cfg.getStartTemperature();
		this.alpha = cfg.getAlpha();
	}
	
	public void run() {
		long startTime = System.nanoTime();
		this.filesData = this.loader.loadAllFilesData();
		for(FileData fileData : this.filesData) {
			Operators operators = new Operators(fileData, this.k_size);
			double[] bestValues = new double[runs];
			int[] worstValues = new int[runs];
			
			int[] x_data = new int[iter];
			int[] y_data_worst = new int[iter];
			int[] y_data_avg = new int[iter];
			int[] y_data_best = new int[iter];
			int[] y_data_current = new int[iter];
			
			int cnt = 0;
			
			for(int s=0; s<runs; s++) {
				
				System.out.println("run: "+(s+1));
				
			x_data = new int[iter];
			y_data_worst = new int[iter];
			y_data_avg = new int[iter];
			y_data_best = new int[iter];
			y_data_current = new int[iter];
			
			Individual currentIndividual = Individual.createRandomIndividual(fileData);
			int currentValue = (int)currentIndividual.getValue();
			
			int bestValueOfAll = Integer.MAX_VALUE;
			int worstValueOfAll = Integer.MIN_VALUE;
			
			double currentTemperature = this.startTemperature;
			int[] bestArr = new int[runs];
			for(int i=0; i<iter; i++) {
				
				x_data[i] = i+1;
				Individual[] neighbours = new Individual[k_size];
				if(this.isSwap) {
					neighbours = operators.createNeighbourSwap(currentIndividual);
				}
				if(this.isInversion) {
					neighbours = operators.createNeighbourInversion(currentIndividual);
				}
				
				int worstValue = Integer.MIN_VALUE;
				int worstIndex = -1;
				int bestValue = Integer.MAX_VALUE;
				int bestIndex = -1;
				int sum=0;
				for(int j=0; j<neighbours.length; j++) {
					int nValue = (int)neighbours[j].getValue();
					sum+=nValue;
					if(currentValue > worstValue) {
						worstValue = currentValue;
						worstIndex = j;
					}
					if(currentValue < bestValue) {
						bestValue = currentValue;
						bestIndex = j;
					}
					int loss = currentValue - nValue;
					if(loss >= 0) {
						currentIndividual = neighbours[j];
						currentValue = nValue;
					}else{
						if(Math.exp((currentValue - nValue)/currentTemperature) > Math.random()) {
							currentIndividual = neighbours[j];
							currentValue = nValue;
						}
					}
				}
				int averageValue = sum/neighbours.length;
				
				if(bestValue<bestValueOfAll) bestValueOfAll = bestValue;
				if(worstValue>worstValueOfAll) worstValueOfAll=worstValue;
				
				y_data_current[i] = currentValue;
				y_data_avg[i] = averageValue;
				y_data_best[i] = bestValueOfAll;
				y_data_worst[i] = worstValue;

				currentTemperature = currentTemperature * alpha;
				}
				bestValues[s] = bestValueOfAll;
			}
			
			
			StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(fileData.getName());
			nameBuilder.append(",iter="+iter+",n_size="+k_size+",temp_start="+startTemperature+",alpha="+alpha+",");
			if(isSwap) nameBuilder.append("swap");
			if(isInversion) nameBuilder.append("inversion");
			
			String name = nameBuilder.toString();
			System.out.println(name);
			Chart chart = new Chart(name, x_data, y_data_worst, y_data_avg, y_data_best, y_data_current);
			chart.saveChart(name);
			
			int w = Integer.MIN_VALUE;
			int b = (int)Integer.MAX_VALUE;
			
			int sum=0;
			
			for(int z=0; z<runs; z++) {
				if(bestValues[z] > w) w = (int)bestValues[z];
				if(bestValues[z] < b) b = (int)bestValues[z];
				sum+=bestValues[z];
			}
			
			int avg = (int)sum/runs;
			int std = 0;
			
			for(int z=0; z<runs; z++) {
				System.out.println((int)bestValues[z]);
				sum+=bestValues[z];
				std+=Math.pow(bestValues[z]-avg,2);
			}
			std=(int)Math.sqrt(std/runs);
			System.out.println("Symulowane wy�arzanie ... best: "+b+", w: "+w+", avg: "+avg+", std: "+std);
			
			long endTime = System.nanoTime();
			System.out.println("Took "+(endTime - startTime)/1000000000 + " s"); 
			// LOSOWY
			
			/*
			int wl = Integer.MIN_VALUE;
			int bl = Integer.MAX_VALUE;
			
			int suml=0;
			int[] valuesl = new int[10000];
			
			for(int c=0; c<10000; c++) {
				Individual indRandom = Individual.createRandomIndividual(fileData);
				int currentValue = (int)Process.costFunction(fileData, indRandom);
				valuesl[c] = currentValue;
				if(currentValue > wl) wl = currentValue;
				if(currentValue < bl) bl = currentValue;
				suml+=currentValue;
			}
			int avgl = (int)suml/10000;
			
			int stdl = 0;
			
			for(int z=0; z<10000; z++) {
				stdl+=Math.pow(valuesl[z]-avgl,2);
			}
			stdl=(int)Math.sqrt(stdl/10000);
			
			System.out.println("LOSOWY ... best: "+bl+", w: "+wl+", avg: "+avgl+", std: "+stdl);
			

			// GREEDY
			
			
			int best_z = Integer.MAX_VALUE;
			int worst_z = 0;
			
			int size = fileData.getCustomers().length;
			
			int[] bests_z = new int[size];
			int sum_best_z = 0;
			
			for(int g=0; g<size; g++) {
				Location temp = fileData.getDepot();
				temp.setCustomerNumber(fileData.getCustomers()[g].getCustomerNumber());
				temp.setDemand(fileData.getCustomers()[g].getDemand());
				fileData.getCustomers()[g].setCustomerNumber(0);
				fileData.getCustomers()[g].setDemand(0);
				fileData.setDepot(fileData.getCustomers()[g]);
				Location[] customersTemp = fileData.getCustomers();
				customersTemp[g] = temp;
				fileData.setCustomers(customersTemp);
				//for(int j=0; j<fileData.getCustomers().length; j++) {
					//System.out.println(fileData.getCustomers()[j].getDemand());
				//}
				
				Individual indG = Individual.createGreedySolution(fileData);
				int tempValue = (int)Process.costFunction(fileData, indG);
				bests_z[g] = tempValue;
				sum_best_z+=tempValue;
				if(tempValue<best_z) best_z = tempValue;
				if(tempValue>worst_z) worst_z = tempValue;
			}
			int avg_z = (int)sum_best_z/size;
			int stdZ = 0;
			for(int z=0; z<bests_z.length; z++) {
				stdZ+=Math.pow((bests_z[z]-avg_z),2);
			}
			stdZ/=size;
			int dstdZ=(int)Math.sqrt(stdZ);
			System.out.println("ZACH�ANNY (N)....best: "+best_z+", worst: "+worst_z+", avg: "+avg_z+", std: "+dstdZ);
			*/
		}
	}
	
	public static Location getCustomerDataByNumber(FileData fileData, int customerNumber) {
		Location customer = fileData.getCustomers()[customerNumber-1];
		return customer;
	}
	
	public static double costFunction(FileData fileData, Individual individual) {
		double sum = 0.0;
		int[] routes = individual.convertSequenceToRoutes();
		
		Location loc1 = null;
		Location loc2 = null;
		for(int i=0; i<routes.length-1; i++) {
			if(routes[i] == -1) loc1 = fileData.getDepot();
			else loc1 = Process.getCustomerDataByNumber(fileData, routes[i]);
			if(routes[i+1] == -1) loc2 = fileData.getDepot();
			else loc2 = Process.getCustomerDataByNumber(fileData, routes[i+1]);
			sum+=Process.getDistanceBetweenTwoLocations(loc1, loc2);
		}
		
		return sum;
	}
	
	public static double getDistanceBetweenTwoLocations(Location location1, Location location2) {
		return Math.sqrt(Math.pow(location1.getX() - location2.getX(), 2) + Math.pow(location1.getY() - location2.getY(), 2));
	}
}
