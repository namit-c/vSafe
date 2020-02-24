import java.util.Arrays;

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.io.FileReader;
 public class ReadCSV{
    public static void readFile(String pathToCSV) throws IOException{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCSV));
        String row = "";
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            System.out.println(Arrays.toString(data));
			break;
        }
        csvReader.close();
    }
    public static void  main(String[] args) throws IOException{
       readFile("../Data_Sets/stormdata_2013.csv");
    }
 }
