import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.io.*;

public class SaferCities {
	/**
	 * @brief Calculates which neighbouring cities of the input city are
	 * safe, as per the calculated danger rating
	 * @param closeCitiesHashtable of type Hashtable<String, ArrayList<String>> representing a hashtable with keys are cities and values as
	 * a list of cities that are "close" to that city/key
	 * @param dangerRatingHash of type Hashtable<String,Double> representing a hashtable with keys as cities and values 
	 * as their calculated danger rating
	 * @param source of type String representing the city to check. (check its neighbouring cities).
	 * @param monthNum of type int representing the month to calculate the safer cities for.
	 * @return safe of type ArrayList<String> such that all members are the names of safe, neighbouring cities
	 */
	public static ArrayList<String> saferCities(Hashtable<String, ArrayList<String>> closeCitiesHashtable, HashMap<String,Double> dangerRatingHash, String source, int monthNum) throws IOException{

		Digraph cityGraph = new Digraph(closeCitiesHashtable.size(), closeCitiesHashtable);

		BreadthFirstPaths saferCities = new BreadthFirstPaths(cityGraph, source);

		ArrayList<String> vertices = closeCitiesHashtable.get(source); // Check all neighbours of source
		String output_text = new String();
		
		ArrayList<String> safe = new ArrayList<String>();

		for (String vertex : vertices) { // Iterate through all sources neighbours and check if safe
			vertex = vertex.toUpperCase();
			
			/*System.out.println(vertex + "-" + monthNum + ":" + dangerRatingHash.containsKey(vertex + "-" + monthNum));
			if (dangerRatingHash.containsKey(vertex + "-" + monthNum)) {
				System.out.println(dangerRatingHash.get(vertex + "-" + monthNum));
			}*/
			
			// Only suggest neighbouring city if it is safe
			if (saferCities.hasPathTo(vertex) && dangerRatingHash.containsKey(vertex + "-" + monthNum) && dangerRatingHash.get(vertex + "-" + monthNum) < 25) {
				safe.add(vertex);
			}
		}
		
		return safe; // Output safe, neighbouring cities
	}

	public static void main(String[] args) throws IOException {
		Hashtable<String, ArrayList<String>> CAN = GraphConstruction.CloseCitiesHashTable("Canada_Cities.csv", "", 0, 0, 2, 1, 200);
		Hashtable<String, ArrayList<String>> US = GraphConstruction.CloseCitiesHashTable("uscities.csv", "stormdata_2013.csv", 15, 1, 9, 8, 500);
		HashMap<String, Double> dangerRatings = DangerRating.loadAllDangerRating();
		ArrayList<String> test = saferCities(US, dangerRatings, "KENEDY", 6);
		System.out.println(test);

	}

}
