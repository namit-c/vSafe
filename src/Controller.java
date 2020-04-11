import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @brief The controller for the FrontendText module and all the back end modules
 * @details Calls the necessary methods for specific modules to print the danger rating
 * of the specified city and month using the FrontendText module
 *
 */
public class Controller {

	public static void infoAndDisplay(String cityname, String month) {
		
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Loading..");
		
		//Sorting the data set
		//sort here
		
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
			infoAndDisplay(input[0], input[1]);
			
			//Flag to trigger the loop to enter multiple cities
			System.out.print("Enter y or yes to enter a new city, anything else to quit: ");
			flag = reader.readLine();
		}
		reader.close();
		System.out.println("\nDone.");
	}

}
