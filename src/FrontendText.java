import java.util.ArrayList;

/**
 * @brief The view module that prints the necessary information to the console
 *
 */
public class FrontendText {
	
	/**
	 * @brief Prints the danger rating of the specified month and city
	 * @param cityname is the name of the city for which danger rating is required
	 * @param month is the number of the month for which danger rating is required
	 */
	public static void printDangerRating(String cityname, String month, double dangerRating){
    	        
        System.out.print("\nThe danger rating of " + cityname + " in ");
        
        switch(month) {
        	case "1":
        		System.out.print("January");
        		break;
        	case "2":
        		System.out.print("February");
        		break;
        	case "3":
        		System.out.print("March");
        		break;
        	case "4":
        		System.out.print("April");
        		break;
        	case "5":
        		System.out.print("May");
        		break;
        	case "6":
        		System.out.print("June");
        		break;
        	case "7":
        		System.out.print("July");
        		break;
        	case "8":
        		System.out.print("August");
        		break;
        	case "9":
        		System.out.print("September");
        		break;
        	case "10":
        		System.out.print("October");
        		break;
        	case "11":
        		System.out.print("November");
        		break;
        	case "12":
        		System.out.print("December");
        		break;
       		default:
       			System.out.println("Invalid Month");
       			return;
        }
        
        System.out.print(" is: " + dangerRating + "\n");
	}

	/**
	 * @brief Prints all the neighboring safe cities of the specified city 
	 * @param safeCities is an ArrayList of strings that contains all the
	 * neighboring safe cities
	 */
	public static void printSafeCities(ArrayList<String> safeCities) {
		System.out.println("Other safe cities nearby: ");
		
		//printing the arraylist with all the nearby safe cities
		for(int i = 0, max = safeCities.size(); i < max; i++) {
			
			//only do this if it is not the last element, for comma purposes
			if(i != max-1)
				System.out.print(safeCities.get(i) + ", ");
			else
				System.out.print(safeCities.get(i) + ".");
		}
	}
}
