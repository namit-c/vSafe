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
		String[][] array0 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv", 8, 19);
		/*String[][] array1 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2004.csv", 16, 20);
		String[][] array2 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2005.csv", 16, 20);
		String[][] array3 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2006.csv", 16, 20);
		String[][] array4 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2007.csv", 16, 20);
		String[][] array5 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2008.csv", 16, 20);
		String[][] array6 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2009.csv", 16, 20);
		String[][] array7 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2010.csv", 16, 20);
		String[][] array8 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2011.csv", 16, 20);
		String[][] array9 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2012.csv", 16, 20);
		String[][] array10 = ReadCSV.readFile("..\\Data_Sets\\stormdata_2013.csv", 16, 20);*/
		
		System.out.println(array0[0][8]);

		
		HashMap<String, Integer> set = new HashMap<>();
		String key;
		for (int i = 0; i < array0.length; i++) {
			key = array0[i][8] + array0[i][19].substring(0, 6) + array0[i][12];
			if (!set.containsKey(key)) {
				set.put(key, 1);
			}
			else {
				set.replace(key, set.get(key) + 1);
			}
		}
		for (String keys : set.keySet()) {
			System.out.println(keys + " : " + set.get(keys));
		}
		System.out.println(set.size());
		
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the location: ");
		location = scanner.nextLine();
		System.out.print("Enter date (mm/dd/yyyy): " );
		date = Integer.parseInt(scanner.nextLine().substring(3,5));
		System.out.println(location + " " + date);
		
		scanner.close();
	}

}
