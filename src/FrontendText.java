import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class FrontendText {
	
	//This module uses all the back-end modules and makes the application
	//with a text interface
	//ONLY TO BE USED as a prototype and testing back end modules
	
	//This module takes care of everything after the input
	public static void frontend(String cityname, String vacationDate) {
		
		
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println("Loading..");
		//Sorting the data set
		MergeSort.main(null);
		System.out.println("Loading complete.\n");
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter the city name and date(mm/dd/yyyy) separated by a space: ");
		//Reading the input information, city and date
		String inputString = reader.readLine();
		
		//Reading the string with city and date and splitting the city and date
		//Index 0 there is the city name
		String[] input = inputString.split(" ");
		
		//calling the rest of the front end stuff to display information
		frontend(input[0], input[1]);
		
		
	}
}
