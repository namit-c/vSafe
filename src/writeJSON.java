import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONArray;


/**
* @brief Module creating JSON files used for heatmap
*/
public class writeJSON {

	/**
	 * @brief Method to create JSON file of lat, long and danger ratings
	 * @throws FileNotFOundException if file cannot be found
	 */
	public static void createJSON() throws FileNotFoundException {
		// Creating a JSONObject object
		JSONObject[] jsonObject;

		// Reading DangerRatingOuput.txt
		File file = new File("src/DangerRatingOutput.txt");
		Scanner scan = new Scanner(file);
		int count = 0;
		String temp;
		while (scan.hasNextLine()) {
			temp = scan.nextLine();
			count++;
		}
		scan.close();
		jsonObject = new JSONObject[count];

		// Reading DangerRatingOuput.txt
		File file1 = new File("src/DangerRatingOutput.txt");
		Scanner scan1 = new Scanner(file1);

		// Creates JSON objects to be written into output
		String[] input = new String[3];
		JSONArray features = new JSONArray();
		JSONObject geometry;
		JSONArray coordArr;
		JSONObject properties = new JSONObject();
		JSONObject rating;
		JSONObject tempObj;
		while (scan1.hasNextLine()) {
			tempObj = new JSONObject();
			geometry = new JSONObject();
			coordArr = new JSONArray();
			rating = new JSONObject();
			tempObj.put("type", "Feature");
			input = scan1.nextLine().split(" ", 3);
			coordArr.put(Double.parseDouble(input[1]));
			coordArr.put(Double.parseDouble(input[0]));
			geometry.put("type", "Point");
			geometry.put("coordinates", coordArr);
			tempObj.put("geometry", geometry);
			tempObj.put("rating", Double.parseDouble(input[2]));
			tempObj.put("properties", properties);
			features.put(tempObj);
		}
		scan1.close();

		// Inserting key-value pairs into the json object
		JSONObject jsonArr = new JSONObject();
		jsonArr.put("type", "FeatureCollection");
		jsonArr.put("features", features);

		// Writes to output file
		try {
			FileWriter file2 = new FileWriter("src/map.geojson");
			file2.write("eqfeed_callback(");
			file2.write(jsonArr.toString());
			file2.write(");");
			file2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("JSON file created: " + jsonObject);
	}

	public static void main(String args[]) throws FileNotFoundException {
		createJSON();
		System.out.println("Done");
	}
}