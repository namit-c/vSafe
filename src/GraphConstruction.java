import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class GraphConstruction {
	public static void CloseCitiesHashTable() throws IOException{
		
		String[][] data = ReadCSV.readFile("../Data_Sets/Canada_Cities.csv",0,1);
        
        // Now data has all rows of cities sorted by alphabetical order.
        
        for (int i = 0; i < data.length; i++) {
        	System.out.println(data[i][0]);
        }
        
        Hashtable<String, ArrayList<String>> closeCities = new Hashtable<String, ArrayList<String>>();
        ArrayList<String> close = new ArrayList<String>();
        double distance;
        
        // Create hash key, value, for all cities
        for (int i = 0; i < data.length; i++) {
        	System.out.println("Examining: " + data[i][0] + "Lon: " + data[i][1] + "Lat:" + data[i][2]);
        	
        	// Check all cities to see if nearby
        	for (int j = 0; j < data.length; j++) {
        		if (i != j) { // If not at same row as the one being examined
        			distance = distance(Double.parseDouble(data[i][2]), Double.parseDouble(data[i][1]),
        					Double.parseDouble(data[j][2]), Double.parseDouble(data[i][1]));
        			//System.out.println(data[i][0] + " to " + data[j][0] + distance);
        		}
        	}
        	
        	
        	closeCities.put(data[i][0], close); // Adds the current examined city to hashtable + its nearby cities
        }
        
        /*
        test.add("Close city");
        test.add("Close city 2");
        closeCities.put("Ontario", test);

        System.out.println(closeCities);
        */
	}
	
	private static double distance(double lon1, double lat1, double lon2, double lat2) {
		double R = 6371000;
		double phi1 = Math.toRadians(lat1);
		double phi2 = Math.toRadians(lat2);
		double delta_phi = Math.toRadians(lat2 - lat1);
		double delta_lamb = Math.toRadians(lon2 - lon1);
		
		double a = Math.sin(delta_phi / 2) * Math.sin(delta_phi / 2) +
				Math.cos(phi1) * Math.cos(phi2) *
				Math.sin(delta_lamb / 2) * Math.sin(delta_lamb / 2);
		
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c;
	}
	
    public static void main(String[] args) throws IOException{
        CloseCitiesHashTable();

    }
}