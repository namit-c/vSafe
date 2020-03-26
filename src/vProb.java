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
		int occurrences = 0; //Occurrences is the occurrence of the event in the last year of the data
		
		
		//Reading the data into a 2d array, specifying city(4) and event(2) columns for sorting
		String[][] cddData = ReadCSV.readFile("../Data_Sets/CDD_csv.csv", cityCol, eventCol);
		
		//Before creating the hash map, make an array with city, date, event and probability; 4 columns needed
		String[][] necessaryData = new String[cddData.length][4];  
		
		//Need to track the month of the date to calculate probability
		int start = 0;
		int finish = 0;
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
		for(int i = 0; dataIndex < cddData.length - 1; i++, dataIndex++) {
			necessaryData[i][cityCol1] = cddData[dataIndex][cityCol];  //city name
			necessaryData[i][eventCol1] = cddData[dataIndex][eventCol];  //event 
			
			//Need to know whether the month the row after is the same as the current row for the probability
			start = cddData[dataIndex+1][dateCol].indexOf("/");  //Gets the index of the where the month is in the date string
			finish = cddData[dataIndex+1][dateCol].indexOf("/", start+1); //index of when the month ends
			month = cddData[dataIndex+1][dateCol].substring(start+1, finish); //string of the month
			
			start = cddData[dataIndex+1][dateCol].indexOf("/"); //index of the month of the next row
			finish = cddData[dataIndex+1][dateCol].indexOf("/", start+1);
			monthNext = cddData[dataIndex+1][dateCol].substring(start+1, finish); //string of the month of the next row
			
			//Details: as the array has the same city next to one another, it is easy to check whether
			//certain events happened more than once; cut string so only month is included
			//Checking whether the next row also has the same city, event, and month
			while(necessaryData[i][cityCol1].equals(cddData[dataIndex+1][cityCol]) && necessaryData[i][eventCol1].equals(cddData[dataIndex+1][eventCol])
					&& month.equals(monthNext)) {
				
				occurrences++;
				dataIndex++; //Don't need to have multiple occurrences of the same event in the same city for the same month
			}
			necessaryData[i][dateCol1] = month; //overriding date to only have month
			
			//to calculate the probability 
			int probability = (occurrences/totalData)*100;
			
			//if the probability is somehow over 100, round down to 100
			if(probability > 100)
				probability = 100;
			
			//adding the probability to the corresponding event, city, and month
			necessaryData[i][probCol] = Integer.toString(probability);
			
			//resetting the variables for the next 
			occurrences = 0;
		}
		
		//Creating a HashMap
		/*
		 * Details: Hash map contains a string and a value; the string contains the city name, date, and event name
		 * The value is the probability of that event happening 
		 */
		HashMap<String, Integer> probCDD = new HashMap<>();
		String key; 
		for(int i = 0; necessaryData[i][cityCol1] != null; i++) {
			key = necessaryData[i][cityCol1] + " " +  necessaryData[i][dateCol1] + " " +  necessaryData[i][eventCol1];
			probCDD.put(key, Integer.parseInt(necessaryData[i][probCol]));
		}
		
		for(String keys: probCDD.keySet())
			System.out.println(keys + " " + probCDD.get(keys));
	}
}
