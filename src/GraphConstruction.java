import java.io.IOException;
import java.util.*;
import java.lang.Math;

public class GraphConstruction {
	
	/**
	 * @brief Creates a hashtable with all fileName's cities as keys, and values being that city's nearby cities.
	 * Only examines cities that are in the data set, sampleName (or examines all cities if no data set given)
	 * @param fileName of type String representing the data set containing city information
	 * @param sampleName of type String representing the data set being used. Essentially uses this to filter out irrelevant cities.
	 * If empty string input, no filter used.
	 * @param sampleCityCol of type int representing the city column for data set sampleName
	 * @param cityCol of type int representing the city column for data set fileName
	 * @param lonCol of type int representing the longitude column for data set fileName
	 * @param latCol of type int representing the latitude column for data set fileName
	 * @param range of type int representing the range of distance to be considered "close"
	 * @return closeCities of type Hashtable<String, ArrayList<String>> such that keys are cities and the values are ArrayList<String>
	 * such that all members are "close" to that city/key
	 */
	public static Hashtable<String, ArrayList<String>> CloseCitiesHashTable(String fileName, String sampleName, int sampleCityCol, int cityCol, int lonCol, int latCol, int range) throws IOException{
		
		String[][] data = ReadCSV.readFile("Data_Sets/" + fileName, 0, 1);
		boolean ignore = true; // ignore is true if ignore irrelevant cities (not in a data set), else, false
		
		ArrayList<String> citiesToInclude = new ArrayList<String>();
		if (sampleName.length() > 0) { // If want only cities in a given data set...
			String[][] sampleData = ReadCSV.readFile("Data_Sets/" + sampleName, sampleCityCol, sampleCityCol);

			for (int i = 1 ; i < sampleData.length; i++) {
				if (!citiesToInclude.contains(sampleData[i][sampleCityCol].toUpperCase().replace("\"", ""))) {
					citiesToInclude.add(sampleData[i][sampleCityCol].toUpperCase().replace("\"", ""));
					//System.out.println(sampleData[i][sampleCityCol].toUpperCase().replace("\"", "")); // Prints cities to include from data set
				}
			}
		}
		else {
			ignore = false;
		}
        
        // Now data has all rows of cities sorted by alphabetical order.
      
        /*for (int i = 0; i < data.length; i++) {
        	System.out.println(data[i][0]);
        }*/
        
        Hashtable<String, ArrayList<String>> closeCities = new Hashtable<String, ArrayList<String>>();
        ArrayList<String> close = new ArrayList<String>();
        double distance;
        
        // Create hash key, value, for all cities
        for (int i = 0; i < data.length; i++) {
        	//System.out.println("Examining: " + data[i][0] + "Lon: " + data[i][1] + "Lat:" + data[i][2]);
        	//System.out.println("hey" + data[i][cityCol].toUpperCase());
        	if (citiesToInclude.contains(data[i][cityCol].toUpperCase()) || ignore == false) {
        		for (int j = 0; j < data.length; j++) {
            		if (i != j && !data[j][cityCol].trim().equals("") && !data[i][cityCol].trim().equals("")) { // If not at same row as the one being examined
            			
            			
            			distance = distance(Double.parseDouble(data[i][lonCol]), Double.parseDouble(data[i][latCol]),
            					Double.parseDouble(data[j][lonCol]), Double.parseDouble(data[j][latCol]));
            			//System.out.println(data[i][0] + " to " + data[j][0] + distance);
            			
            			// ADJUST DEFINITION OF "CLOSE" HERE
            			if (distance <= range && !close.contains(data[j][cityCol]) && (citiesToInclude.contains(data[j][cityCol].toUpperCase()) || ignore == false)) { // If this city is "close", add to array list
            				close.add(data[j][cityCol]);
            				//System.out.println(close);
            			}
            			
            		}
            	}
        		// Check all cities to see if nearby
        		closeCities.put(data[i][cityCol].toUpperCase(), close); // Adds the current examined city to hashtable + its nearby cities
            	close= new ArrayList<String>(); // Clear "close" cities to initialize for next check
            	
        	}

        }
        
        // Prints all cities and their nearby cities
        /*
        int count = 0;
        ArrayList<String> empty = new ArrayList<String>();
        for (int i = 0; i < closeCities.size(); i++) {
        	System.out.println(data[i][0] + ": " + closeCities.get(data[i][0]));
        	if (closeCities.get(data[i][0]).size() == 0) {
        		empty.add(data[i][0]);
        		count++;
        	}
        }
        System.out.println(empty);
        System.out.println(count);
        */

        return closeCities;
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

		return R * c / 1000; // Converted to km
	}
	
    public static void main(String[] args) throws IOException{
    	Hashtable<String, ArrayList<String>> CAN = CloseCitiesHashTable("Canada_Cities.csv", "", 0, 0, 2, 1, 200);
    	Hashtable<String, ArrayList<String>> US = CloseCitiesHashTable("uscities.csv", "stormdata_2013.csv", 15, 1, 9, 8, 500);
    	System.out.println(US.get("KENEDY"));
    	System.out.println(CAN.get("TORONTO"));

    }
}