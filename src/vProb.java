import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class vProb {
    
	public static void main(String[] args) throws IOException {
	    //calling the next module
	    probCDD();

	    System.out.println("Done----------------");//just to know when it finishes

	    
	    String location;
		int date;
		int month;
		
		// Load Stormdata datasets (2003 - 2013)
		String[][] array0 = ReadCSV.readFile("../Data_Sets/stormdata_2003.csv", 8, 19);
		String[][] array1 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv", 8, 19);
		String[][] array2 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv", 8, 19);
		String[][] array3 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv", 8, 19);
		String[][] array4 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv", 8, 19);
		String[][] array5 = ReadCSV.readFile("../Data_Sets/stormdata_2008.csv", 8, 19);
		String[][] array6 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv", 8, 19);
		String[][] array7 = ReadCSV.readFile("../Data_Sets/stormdata_2010.csv", 8, 19);
		String[][] array8 = ReadCSV.readFile("../Data_Sets/stormdata_2011.csv", 8, 19);
		String[][] array9 = ReadCSV.readFile("../Data_Sets/stormdata_2012.csv", 8, 19);
		String[][] array10 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv", 8, 19);
		String[][][] dataSet0 = new String[][][] { array1, array2, array3, array4, array5, array6, array7,
				array8, array9, array10 };

		// Creates HashMap
		HashMap<String, Integer> set = new HashMap<>();
		String key;
		for (int i = 0; i < dataSet0.length; i++) {
			for (int j = 1; j < dataSet0[i].length; j++) {
				String input = dataSet0[i][j][0].replaceAll("\"", "").replaceAll("/", "");
				if (input.length() > 5) {
					//  key = State (Col 9) + Begin Month (Col 4) + Begin Day (Col 1) + Event (Col 13)
					key = dataSet0[i][j][8] + ' ' + String.valueOf(input.charAt(4)) + String.valueOf(input.charAt(5)) + "/" + dataSet0[i][j][1] + ' ' + dataSet0[i][j][12]; // Input
					if (!set.containsKey(key)) {
						set.put(key, 1);
					} else {
						set.replace(key, set.get(key) + 1);
					}
				}

			}
		}

		for (String keys : set.keySet()) {
			System.out.println(keys + " : " + set.get(keys));
		} 
		
		// User inputs   
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the location: ");
		location = scanner.nextLine();
		System.out.print("Enter date (mm/dd/yyyy): ");
		month = Integer.parseInt(scanner.nextLine().substring(0,2));
		date = Integer.parseInt(scanner.nextLine().substring(3, 5));
		System.out.println(location + " " + month + " " + date);

		scanner.close();
	}
    
        //Brief: Calculating probability for CDD data set, country: Canada 
	//Details: uses the formula (total occurrences in specified month)/(total years of data) * 100 
	//to calculate probability of each event in each city for each  month
	public static void probCDD() throws IOException {
		
		//Column numbers of city, date, and event name in the CDD data set
		int cityCol = 4;
		int dateCol = 5;
		int eventCol = 3;
		int totalData = 2016-1900; //Finish - start dates of the data; total years of data in cdd data set
		int occurrences = 0; //Occurrences is the total occurrences of that event in the specified city in the specified month
		
		//Reading the data into a 2d array, specifying city(4) and event(3) columns for sorting
		String[][] cddData = ReadCSV.readFile("../Data_Sets/CDD_csv.csv", cityCol, eventCol);
		
		//for(int i = 0; i < cddData.length; i++)
		//	System.out.println(cddData[i][cityCol] + " " + cddData[i][eventCol] + " " + cddData[i][dateCol]);
		
		//Before creating the hash map, make an array with city, date, event and probability; 4 columns needed
		String[][] necessaryData = new String[cddData.length][4];  
		
		//Need to track the month of the date to calculate probability
		int start = 0;	//doesn't change as the month starts from index 0
		int finish;
		String month;
		String monthNext;
		
		//columns in the necessary array
		int cityCol1 = 0; //city
		int dateCol1 = 1;  //month
		int eventCol1 = 2;  //event
		int probCol = 3;   //probability
		
		//Need to track the index of cddData as it will have multiple occurrences of the same event
		//that will only be added to the necessary data array once
		int dataIndex = 0;
				
		//Copying all the necessary data into the array; probability not yet done
		//The date column will only contain months as years and day do not matter in calculating probability
		//As the data is not sorted by date, need to do extra steps to get all occurrences of the event in the same month
		for(int i = 0; dataIndex < cddData.length; i++, dataIndex++) {
			
			//city name that are null have already been counted
			if(!cddData[dataIndex][cityCol].equals("0")) {
				necessaryData[i][cityCol1] = cddData[dataIndex][cityCol];  //city name
				necessaryData[i][eventCol1] = cddData[dataIndex][eventCol];  //event 
				
				//Need to know whether the month the row after is the same as the current row for the probability
				finish = cddData[dataIndex][dateCol].indexOf("/");  //Gets the index of the where the month is in the date string
				month = cddData[dataIndex][dateCol].substring(start, finish); //string of the month
				
				occurrences++; //occurs once
				
				int temp = dataIndex; //to keep track of the index in array; temp will be used in the loop to determine multiple occurrences
				
				//only go if it is not the last element
				if(dataIndex < cddData.length -1) {
					//Details: as the array has the same city next to one another, it is easy to check whether
					//certain events happened more than once
					//go through all the rows with same city and event, if the same month is encountered, increment the occurrences
					//and replace the city name with "0" (so it is not counted again)
					while(necessaryData[i][cityCol1].equals(cddData[temp+1][cityCol]) && 
							necessaryData[i][eventCol1].equals(cddData[temp+1][eventCol])) {
						
						finish = cddData[temp+1][dateCol].indexOf("/"); //index of the month of the next row
						monthNext = cddData[temp+1][dateCol].substring(start, finish); //string of the month of the next row
						
						if(month.equals(monthNext)) {
							occurrences++;
							cddData[temp+1][cityCol] = "0";		//for cities already counted
						}
						temp++; //Don't need to have multiple occurrences of the same event in the same city for the same month
					}
				}
				necessaryData[i][dateCol1] = month;
				
				//to calculate the probability 
				double probability = ((double)occurrences/(double)totalData)*100;
				
				if(necessaryData[i][cityCol1].equals("Edmonton AB")) {
					System.out.println(necessaryData[i][eventCol1] + " " + necessaryData[i][dateCol1] + " " + occurrences);
				}
				
				//if the probability is over 100 (occurrences more than years of data)
				if(probability > 100)
					probability = 100;
				
				//adding the probability to the corresponding event, city, and month
				necessaryData[i][probCol] = Integer.toString((int)probability);
				
				//resetting the variables for the next 
				occurrences = 0;
			}
			else
				i--; //the index of the necessary row doesn't increment as there should be no missed rows
		}
		
		//Creating a HashMap
		//Details: Hash map contains a string and a value; the string contains the city name, date, and event name
		//The value is the probability of that event happening 
		HashMap<String, Integer> probCDD = new HashMap<>();
		String key; 
		for(int i = 0; necessaryData[i][cityCol1] != null; i++) {
			key = necessaryData[i][cityCol1] + " " +  necessaryData[i][dateCol1] + " " +  necessaryData[i][eventCol1];
			probCDD.put(key, Integer.parseInt(necessaryData[i][probCol]));
		}
		
		System.out.println("\n\n");
		for (String keys : probCDD.keySet())
			System.out.println(keys + ": " + probCDD.get(keys));
	}

    	//Calculates probability for the eqarchive data set (contains earthquakes in north america, mostly canada)
	//Does not include foreshocks and aftershocks as they are part of the same earthquake
	public static void probEq() throws IOException {
		
		//column numbers of the city and date
		int cityCol = 6;
		int dateCol = 0;
		
		double probability;	//temp variable for probability of each data point
		int totalData = 2019-1985; 	//Finish - start dates of the data; total years of data in eqarchive data set
		int occurrences = 0; 	//Occurrences is the total occurrences of that event in the specified city in the specified month
		
		//Reading the data into a 2d array, specifying city(4) and event(2) columns for sorting
		String[][] eqData = ReadCSV.readFile("../Data_Sets/eqarchive-en.csv", cityCol, dateCol);	

		//The data set contains specific locations of the earthquakes, this means it gives distance from cities nearby
		//This array contains the city name and month of the earthquake
		//This array will be used to make the hash map with the probability
		String[][] necessaryData = new String[eqData.length][2];  //so the last row is null; needed for comparing next rows
		
		//column numbers for the necessary data array
		int cityCol1 = 0;
		int dateCol1 = 1;
		
		//Adding only the month of the earthquake to the array
		int startMonth = 5;  	//index at which the month starts
		int finishMonth = 6; 	//index at which the month ends
		
		int start = 0; 		//variables needed for finding the city name
		int dataIndex = 0;  //to keep track of the row in the dataset
		
		//Stores the city and month
		for(int i = 0; dataIndex < eqData.length; i++, dataIndex++) {
			
			//adding month without any preceding zeros
			if(eqData[dataIndex][dateCol].substring(startMonth-1, startMonth+1).equals("-0"))
				necessaryData[i][dateCol1] = eqData[dataIndex][dateCol].substring(startMonth+1, finishMonth+1);
			else
				necessaryData[i][dateCol1] = eqData[dataIndex][dateCol].substring(startMonth, finishMonth+1);
			
			//getting the name
			start = eqData[dataIndex][cityCol].indexOf("from"); //the city appears after the word from
			
			//adding only the city name, different formats with city names
			if(start == -1) {
				start = eqData[dataIndex][cityCol].indexOf("of  ");
				if(start == -1) {
					start = eqData[dataIndex][cityCol].indexOf("of ");
					if(start == -1)
						necessaryData[i][cityCol1] = eqData[dataIndex][cityCol];
					else
						necessaryData[i][cityCol1] = eqData[dataIndex][cityCol].substring(start+3);
				}
				else
					necessaryData[i][cityCol1] = eqData[dataIndex][cityCol].substring(start+4); //4 bc "of" is 2 plus the 3 spaces afterwards
			}
			else
				necessaryData[i][cityCol1] = eqData[dataIndex][cityCol].substring(start+5); //5 because from is 4 plus the space afterwards
		}
		
		//Need to sort the array so the same month and cities are beside each other
		MergeSort.sort(necessaryData, dateCol1);
		MergeSort.sort(necessaryData, cityCol1);
		
		//Creating a HashMap
		//Details: Hash map contains a string and a value; the string contains the city name, date, and event name
		//The value is the probability of that event happening 
		HashMap<String, Integer> probEq = new HashMap<>();
		String key; 
		
		for(int i = 0; i < necessaryData.length; i++) {
			
			//do not count aftershocks and foreshocks as they are part of the same earthquake
			if(!(necessaryData[i][cityCol1].toUpperCase().contains("AFTERSHOCK") || 
					necessaryData[i][cityCol1].toUpperCase().contains("FORESHOCK"))) {
				key = necessaryData[i][cityCol1] + " " +  necessaryData[i][dateCol1] + " Earthquake";
				occurrences++;
				
				//only check for multiple occurrences if it is not the last element
				if(i < necessaryData.length - 1) {
					while(necessaryData[i][cityCol1].equals(necessaryData[i+1][cityCol1]) && necessaryData[i][dateCol1].equals(necessaryData[i+1][dateCol1])) {
						occurrences++;	//increment each time the city and month is the same
						i++;	//don't need multiple occurrences of the same city and month
					}
				}
				//calculating the probability
				probability = ((double)occurrences/(double)totalData)*100;
				
				//if the probability is over 100 (more occurrences than years of data)
				if(probability > 100)
					probability = 100.0;
				
				//adding the data to the hash map
				probEq.put(key, (int)probability);
				
				//resetting the occurrences
				occurrences = 0;
			}
		}
	}
}
