import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class FrontendText {
	
	//This module uses all the back-end modules and makes the application
	//with a text interface
	//ONLY TO BE USED as a prototype and testing back end modules
	
	//This module takes care of everything after the input
	public static void frontend(String cityname, String month) throws IOException{
		
		int flag = 0;
		if(Integer.parseInt(month) < 0 && Integer.parseInt(month) > 13) {
			System.out.println("\nInvalid Month");
			return;
		}
		
		//graph construction
		Hashtable<String, ArrayList<String>> CAN = GraphConstruction.CloseCitiesHashTable("Canada_Cities.csv", "", 0, 0, 2, 1, 200);
    	Hashtable<String, ArrayList<String>> US = GraphConstruction.CloseCitiesHashTable("uscities.csv", "stormdata_2013.csv", 8, 1, 9, 8, 500);
    	
    	// Example usage of hashtables
    	// System.out.println(US.get("WYOMING"));
    	// System.out.println(CAN.get("TORONTO"));
    	
		
		//print danger rating (key is cities, value is danger rating value)
        HashMap<String,Double> dangerRatingHash = DangerRating.loadAllDangerRating(month); //wouldn't matter if month is 01 or 1
		
        //printing the results
        
        System.out.print("\nThe danger rating of " + cityname + " in: ");
        
        switch(month) {
        	case "1":
        		System.out.print("January");
        	case "2":
        		System.out.print("February");
        	case "3":
        		System.out.print("March");
        	case "4":
        		System.out.print("April");
        	case "5":
        		System.out.print("May");
        	case "6":
        		System.out.print("June");
        	case "7":
        		System.out.print("July");
        	case "8":
        		System.out.print("August");
        	case "9":
        		System.out.print("September");
        	case "10":
        		System.out.print("October");
        	case "11":
        		System.out.print("November");
        	case "12":
        		System.out.print("December");
        }
        
        System.out.print(" is" + dangerRatingHash.get(cityname.toUpperCase()));
        System.out.println("\nNeighbouring cities are: " + CAN.get(cityname.toUpperCase()));
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Loading..");
		
		//Sorting the data set
		//sort here
		
		System.out.println("Loading complete.\n");
		
		String flag = "y";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		//Loop so multiple cities can be entered
		while(flag.equals("y")) {
			System.out.print("Enter the city name and month number separated by a space: ");
			//Reading the input information, city and date
			String inputString = reader.readLine();
			
			//Reading the string with city and date and splitting the city and date
			//Index 0 there is the city name
			String[] input = inputString.split(" ");
			
			//calling the rest of the front end stuff to display information
			frontend(input[0], input[1]);
			
			//Flag to trigger the loop to enter multiple cities
			System.out.print("Enter y to enter a new city, n otherwise: ");
			flag = reader.readLine();
		}
		reader.close();
		System.out.println("\nDone.");
	}
}
