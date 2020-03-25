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

	    System.out.println("Done----------------");

	    //Comment out the rest of this module if running the next module, faster that way

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

        //Calculating probability for CDD data set, country: Canada 
	//PUT THE FORMULA FOR THE CALCULATION
	public static void probCDD() throws IOException {
		
		//Column numbers of city, date, and event name in the CDD data set
		int cityCol = 5;
		int dateCol = 6;
		int eventCol = 3;
		
		boolean flag = false; //to keep track of whether conditions have been met
		
		
		//Reading the data into a 2d array, specifying city(5) and date(6) columns for sorting; only sorts the first column
		//Second column param doesn't matter
		String[][] cddData = ReadCSV.readFile("../Data_Sets/CDD_csv.csv", dateCol, dateCol);
		
		//Need to sort two more times to do inplace sort with cities, then events, then date-----DOES NOT WORK
		MergeSort.sort(cddData, eventCol);
		MergeSort.sort(cddData, cityCol);
		
		for(int i = 0; i < 25; i++)
			System.out.println(cddData[i][cityCol] + " " + cddData[i][dateCol] + " " + cddData[i][eventCol] + "\n");
		
		/*
		//Before creating the hashmap, make an array with city, date, event and probability; 4 columns needed
		String[][] necessaryData = new String[cddData.length][4];  
		
		//Occurrences is the occurrence of the event in the last year of the data
		//Total occurrences is the cumulative occurrences in that month over all years of data
		int occurrences = 0; 
		int totalOccurrences = 0;
		
		//Need to track the month of the date to calculate probability
		int start = 0;
		int finish = 0;
		String month;
		String monthNext;

		
		//THIS ONLY WORKS IF THE ARRAY IS SORTED BY FIRST CITIES, THEN EVENTS, THEN DATES
		//DO WE NEED TO FULL DATE; COULDNT WE JUST HAVE THE DATE WITH MONTH AS THATS THE ONLY THING NEEDED TO CALCULTE THE PROBABILITY
		//YEAR DOESN'T MATTER AS ITS GONNA BE THE CURRENT YEAR, THE DATE IS NOT USED
		
		int FOUR = 4; //last column in the array
		
		//Copying all the necessary data into the array; probability not yet done
		for(int i = 0; i < necessaryData.length; i++) {
			necessaryData[i][cityCol] = cddData[i][cityCol];  //city name
			necessaryData[i][dateCol] = cddData[i][dateCol];  //date
			necessaryData[i][eventCol] = cddData[i][eventCol];  //event 
			
			//Calculating probability of the event
			//Details: as the array has the same city next to one another, it is easy to check whether
			//certain events happened more than once; cut string so year is not included (maybe also use month)
			//Checking whether the next row also has the same city, event, and month
			while(necessaryData[i][cityCol].equals(cddData[i+1][cityCol]) && necessaryData[i][eventCol].equals(cddData[i+1][eventCol])) {
				
				//Need to know whether the month the row after is the same as the current row for the probability
				start = necessaryData[i][dateCol].indexOf("/");  //Gets the index of the where the month is in the date string
				finish = necessaryData[i][dateCol].indexOf("/", start); //index of when the month ends
				month = necessaryData[i][dateCol].substring(finish); //string of the year
				
				start = necessaryData[i+1][dateCol].indexOf("/"); //index of the month of the next row
				finish = necessaryData[i+1][dateCol].indexOf("/", start);
				monthNext = necessaryData[i+1][dateCol].substring(finish); //string of the month of the next row
					
				//if the month in the next row is the same as the current; we increase the total occurrences in that month
				if(month.equals(monthNext)) {
					totalOccurrences++;
					flag = true;
					i++; //Don't need to have multiple occurrences of the same event in the same city for the same month
				}
				
				//if condition above has not been yet, then the current row is the most recent year of data
				if(flag) {
					occurrences++;
					//updated date as only the most recent is needed; the dates of the same event in the same city are used for probability
					necessaryData[i][dateCol] = cddData[i][dateCol]; 
					flag = false; //for the next loop
				}
			}
			
			//to calculate the probability 
			int probability = (occurrences/totalOccurrences)*100;
			
			//adding the probability to the corresponding event, city, and month
			necessaryData[i][FOUR] = Double.toString(probability);
			
			//resetting the variables for the next 
			occurrences = 0;
			totalOccurrences = 0;
		}*/
		
		//Creating a HashMap
		/*
		 * Details: Hash map contains a string and a value; the string contains the city name, date, and event name
		 * The value is the probability of that event happening 
		 */
	}

    
    
    

}
