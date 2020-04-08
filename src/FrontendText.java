import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;

public class FrontendText {
	
	//This module uses all the back-end modules and makes the application
	//with a text interface
	//ONLY TO BE USED as a prototype and testing back end modules
	
	//This module takes care of everything after the input
	public static void frontend(String cityname, String vacationDate) throws IOException{
		
		//call the searching module
				
		//assign danger rating
		
		//graph construction
		Hashtable<String, ArrayList<String>> CAN = GraphConstruction.CloseCitiesHashTable("Canada_Cities.csv", "", 0, 0, 2, 1, 200);
    	Hashtable<String, ArrayList<String>> US = GraphConstruction.CloseCitiesHashTable("uscities.csv", "stormdata_2013.csv", 8, 1, 9, 8, 500);
    	
    	// Example usage of hashtables
    	// System.out.println(US.get("WYOMING"));
    	// System.out.println(CAN.get("TORONTO"));
    	
		
		//print danger rating
		
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
			System.out.println("Enter the city name and date(mm/dd/yyyy) separated by a space. ");
			//Reading the input information, city and date
			String inputString = reader.readLine();
			
			//Reading the string with city and date and splitting the city and date
			//Index 0 there is the city name
			String[] input = inputString.split(" ");
			
			//calling the rest of the front end stuff to display information
			frontend(input[0], input[1]);
			
			//Flag to trigger the loop to enter multiple cities
			System.out.println("Enter y to enter a new city, n otherwise: ");
			flag = reader.readLine();
		}
		reader.close();
		
	}
}
