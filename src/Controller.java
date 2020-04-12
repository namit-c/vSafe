import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * @brief The controller for the FrontendText module and all the back end modules
 * @details Calls the necessary methods for specific modules to print the danger rating
 * of the specified city and month using the FrontendText module
 *
 */
public class Controller {

	/**
	 * @brief Calls the necessary methods to compute and display the danger rating and nearby safe cities of the specified city and month
	 * @param cityname is the input city name for which danger rating is required
	 * @param month is the input month for which the danger rating is required
	 * @param dangerRatingHash is a hash table with city, month and the corresponding danger rating
	 * @throws IOException
	 */
	public static void infoAndDisplay(String cityname, String month, HashMap<String,Double> dangerRatingHash) throws IOException {
		
		System.out.println("Entering method");
		
		//if invalid month number is entered
		try {
			if(Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
				System.out.println("\nInvalid Month");
				return;
			}
		}catch(NumberFormatException e) {
			System.out.println("Invalid Input! Make sure to enter the number of the month (1 to 12).");
			return;
		}
		
		//checking whether the specified city is in the data set
		//checking whether the specified city is in the data set
				if(!dangerRatingHash.containsKey(cityname.toUpperCase() + "-" + month)) {
					System.out.println(cityname + " is not in the dataset.");
					return;
				}
				
				//call the print danger rating module
				FrontendText.printDangerRating(cityname, month, dangerRatingHash.get(cityname.toUpperCase() + "-" + month));
		
		//graph construction
		int sampleCityCol = 0;
		int cityCol = 0;
		int lonCol = 2;
		int latCol = 1;
		int range = 200;
		
		Hashtable<String, ArrayList<String>> canGraph = GraphConstruction.CloseCitiesHashTable("Canada_Cities.csv", "", 
				sampleCityCol, cityCol, lonCol, latCol, range);
		
		//changing variables for the us graph
		sampleCityCol = 15;
		cityCol = 1;
		lonCol = 9;
		latCol = 8;
		range = 500;
    	Hashtable<String, ArrayList<String>> usGraph = GraphConstruction.CloseCitiesHashTable("uscities.csv", "stormdata_2013.csv", 
    			sampleCityCol, cityCol, lonCol, latCol, range);
		
		//call the safer cities method
    	ArrayList<String> safeCities;
    	if(canGraph.contains(cityname.toUpperCase())) {
    		safeCities = SaferCities.saferCities(canGraph, dangerRatingHash, cityname.toUpperCase(), Integer.parseInt(month));
    	}
    	else {
    		safeCities = SaferCities.saferCities(usGraph, dangerRatingHash, cityname.toUpperCase(), Integer.parseInt(month));
    	}
    	
		
		//call the print all the close cities that are safe
	        DangerRating.writeDangerRateTxt(month);
	        FrontendText.printSafeCities(safeCities);
    	
		//make the heatmap
		
		//call the display heatmap method
		System.out.println("\nRedirecting to another window...");
		
	}

	/**
	 * @brief The main method calls the necessary methods to display the necessary information and takes user inputs
	 * @details Reads the user inputs and calls methods that print the necessary information until the user is done
	 * @param args are arguments from command line, not used
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
		System.out.println("Loading..");
		
		//Loading in all the danger ratings
		HashMap<String, Double> dangerRatings = DangerRating.loadAllDangerRating();
		
		System.out.println("Loading complete.\n");
		
		//Reading the input--------------
		String flag = "y";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		//Loop so multiple cities can be entered
		while(flag.toLowerCase().equals("y") || flag.toLowerCase().equals("yes")) {
			System.out.print("Enter the city name and month number separated by a space: ");
			//Reading the input information, city and date
			String inputString = reader.readLine();
			
			//Reading the string with city and date and splitting the city and date
			//Index 0 there is the city name
			String[] input = inputString.split(" ");
			
			//calling the rest of the front end stuff to display information
			infoAndDisplay(input[0], input[1], dangerRatings);
			
			//Flag to trigger the loop to enter multiple cities
			System.out.print("\nEnter y or yes to enter a new city, anything else to quit: ");
			flag = reader.readLine();
		}
		reader.close();
		System.out.println("\n\nProgram Closed.");
	}

}
