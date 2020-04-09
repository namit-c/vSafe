import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.io.*;

public class SaferCities {
	public static ArrayList<String> saferCities(Hashtable<String, ArrayList<String>> closeCitiesHashtable, String source, String month) throws IOException{
		
		Digraph cityGraph = new Digraph(closeCitiesHashtable.size(), closeCitiesHashtable);
		BreadthFirstPaths saferCities = new BreadthFirstPaths(cityGraph, source);
		
		System.out.println(cityGraph.toString());

		ArrayList<String> vertices = closeCitiesHashtable.get(source); // Check all neighbours of source
		String output_text = new String();
		HashMap<String,Double> dangerRatingHash = DangerRating.loadAllDangerRating(month);
		
		ArrayList<String> safe = new ArrayList<String>();
		
		for (String vertex : vertices) { // Iterate through all sources neighbours and check if safe
			vertex = vertex.toUpperCase();
			
			// Only suggest neighbouring city if it is safe
			if (saferCities.hasPathTo(vertex) && dangerRatingHash.containsKey(vertex) && dangerRatingHash.get(vertex) < 25) {
				safe.add(vertex);
			}
		}
		
		return safe; // Output safe, neighbouring cities
	}

	public static void main(String[] args) throws IOException {
		Hashtable<String, ArrayList<String>> CAN = GraphConstruction.CloseCitiesHashTable("Canada_Cities.csv", "", 0, 0, 2, 1, 200);
		ArrayList<String> test = saferCities(CAN, "TORONTO", "10");
		System.out.println(test);

	}

}
