import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class vProb {

	public static void main(String[] args) throws IOException {
		String location;
		int date;
		int month;
		
		// Load Stormdata datasets (2003 - 2013)
		String[][] array0 = ReadCSV.readFile("../Data_Sets/stormdata_2003.csv", 8, 19);
		String[][] array1 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv", 8, 19);
		String[][] array2 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv", 8, 19);
		String[][] array3 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv", 8, 19);
		String[][] array4 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv", 8, 19);
		String[][] array5 = ReadCSV.readFile("../Data_Sets/stormdata_2008.csv", 8, 19);
		String[][] array6 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv", 8, 19);
		String[][] array7 = ReadCSV.readFile("../Data_Sets/stormdata_2010.csv", 8, 19);
		String[][] array8 = ReadCSV.readFile("../Data_Sets/stormdata_2011.csv", 8, 19);
		String[][] array9 = ReadCSV.readFile("../Data_Sets/stormdata_2012.csv", 8, 19);
		String[][] array10 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv", 8, 19);
		String[][][] dataSet0 = new String[][][] { array1, array2, array3, array4, array5, array6, array7,
				array8, array9, array10 };

		// Creates HashMap
		HashMap<String, Integer> set = new HashMap<>();
		String key;
		for (int i = 0; i < dataSet0.length; i++) {
			for (int j = 1; j < dataSet0[i].length; j++) {
				String input = dataSet0[i][j][0].replaceAll("\"", "").replaceAll("/", "");
				if (input.length() > 5) {
					//  key = State (Col 9) + Begin Month (Col 4) + Begin Day (Col 1) + Event (Col 13)
					key = dataSet0[i][j][8] + ' ' + String.valueOf(input.charAt(4)) + String.valueOf(input.charAt(5)) + "/" + dataSet0[i][j][1] + ' ' + dataSet0[i][j][12]; // Input
					if (!set.containsKey(key)) {
						set.put(key, 1);
					} else {
						set.replace(key, set.get(key) + 1);
					}
				}

			}
		}

		for (String keys : set.keySet()) {
			System.out.println(keys + " : " + set.get(keys));
		} 
		
		// User inputs   
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the location: ");
		location = scanner.nextLine();
		System.out.print("Enter date (mm/dd/yyyy): ");
		month = Integer.parseInt(scanner.nextLine().substring(0,2));
		date = Integer.parseInt(scanner.nextLine().substring(3, 5));
		System.out.println(location + " " + month + " " + date);

		scanner.close();
	}

}