import java.util.Arrays;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;

/***
  * Notes:
  *     For stormdata_yyyy.csv, the start dates are listed as
  *     ["BEGIN_YEARMONTH", "BEGIN_DAY", "BEGIN_TIME", "END_YEARMONTH", "END_DAY", "END_TIME",...]
  *
 ***/

/*
 * NOTE* Right now each entry/element in the 2D matrix that is returned in the CSV file will start and end with a quote
 *  i.e "\"some-String-\"" (this is how you would write a this specific string in java to include quotes)
*/


 public class ReadCSV{

    /**
     * @param pathToCSV path to CSV file
     * @param col1 column index for city
     * @param col2 column index for date
     */
    public static String[][] readFile(String pathToCSV, int col1, int col2) throws IOException{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCSV));
        String row = "";
        int count = 0;
        ArrayList<String[]> dataSet = new ArrayList<String[]>();
        while ((row = csvReader.readLine()) != null) {

            String[] data = row.split(",");
            if (count > 0){ //used to skip the first line

                dataSet.add(data);
            }
            count += 1;
        }
        csvReader.close();
        String[][] data = convertArrayListToStringArr(dataSet);
        MergeSort.sort(data,col2);
        long startTime = System.currentTimeMillis();
        MergeSort.sort(data,col1);
        long endTime = System.currentTimeMillis();

        System.out.println("SORTING LOCATION TAKES : " + (endTime - startTime) + " ms");
        
       
        startTime = System.currentTimeMillis();
        //MergeSort.sort(data,col2);
        endTime = System.currentTimeMillis();

        System.out.println(pathToCSV + " = " +data.length);

        return data;

    }
    private static String[][] convertArrayListToStringArr(ArrayList<String[]> arr){
        String[][] newArr = new String[arr.size()][arr.get(0).length];
        
        for (int i = 0;i < arr.size(); i++){
            int j;
            for (j = 0; j < arr.get(0).length && j < arr.get(i).length;j++){
                if (arr.get(i)[j].length() == 0){
                    newArr[i][j] = " ";
                }else{
                    newArr[i][j] = arr.get(i)[j].replaceAll("\"",""); 
                }
            }
            while (j < arr.get(0).length){
                newArr[i][j++] = " ";
            }

        }
        return newArr;
    }

 

    public static void  main(String[] args) throws IOException{
       long startTime = System.currentTimeMillis();
       readFile("../Data_Sets/stormdata_2013.csv",8,19);
       readFile("../Data_Sets/stormdata_2012.csv",8,19);
       readFile("../Data_Sets/stormdata_2011.csv",8,19);
       readFile("../Data_Sets/stormdata_2010.csv",8,19);
       readFile("../Data_Sets/stormdata_2009.csv",8,19);
       readFile("../Data_Sets/stormdata_2008.csv",8,19);
       readFile("../Data_Sets/stormdata_2007.csv",8,19);
       readFile("../Data_Sets/stormdata_2006.csv",8,19);
       readFile("../Data_Sets/stormdata_2005.csv",8,19);
       readFile("../Data_Sets/stormdata_2004.csv",8,19);
       readFile("../Data_Sets/stormdata_2003.csv",8,19);
       readFile("../Data_Sets/eqarchive-en.csv",0,6);
       String[][] city = readFile("../Data_Sets/Canada_Cities.csv",0,1);
       long endTime = System.currentTimeMillis();
       System.out.println("\n\nThis took = " + (endTime - startTime) + " ms");
       System.out.println("DONE"); 
    }
 }
