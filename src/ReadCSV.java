import java.util.Arrays;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;




 public class ReadCSV{

    /**
     * @brief given a path to a CSV file, read this file and store its contents into a 2D String and also sorting this array at the same time (sorting first by one field followed by another sub field to 
     *        sort the 2D array by). For example, if you want to sort by city names, then sort event name
     * @param pathToCSV path to CSV file
     * @param col1 column index for the major field to be sorted by (i.e using the example in the brief description: city)
     * @param col2 column index for the minor subfield to be sorted by (i.e using the example in the brief description: event name)
     * @return 2D string array that is the sorted based on the two columns in the array
     * @throws IOException
     */
    public static String[][] readFile(String pathToCSV, int col1, int col2) throws IOException{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCSV));
        String row = "";
        int count = 0;
        
        ArrayList<String[]> dataSet = new ArrayList<String[]>();
        while ((row = csvReader.readLine()) != null) { //reading each row in the CSV file and storing into an 1D string array that is also added to a arrayList where each element is a 1D string array
            String[] data = row.split(",");
            if (count > 0){ //used to skip the first line
                dataSet.add(data);
            }
            count += 1;
        }
        csvReader.close();
        
        
        String[][] data = convertArrayListToStringArr(dataSet); //convert ArrayList<String[]> into 2D string array
        
        MergeSort.sort(data,col2); //sorting the inner subfields first)
        long startTime = System.currentTimeMillis();
        MergeSort.sort(data,col1); //sorting the outter field/major field to be sorted by
        long endTime = System.currentTimeMillis();

        System.out.println("SORTING LOCATION TAKES : " + (endTime - startTime) + " ms");
        

        System.out.println(pathToCSV + " = " +data.length);

        return data;

    }
    
    /**
 	 * @brief Convert a 2D String array into an ArrayList with each row containing the a single 1D String array (making it easier to delete rows). Also some dataSets may not be fixed columns so it will fix this
 	 * @param arr is the 2D string array containing information about the CSV file 
 	 * @return returns the String[][] of the fixed columns of the CSV file
 	 */
    private static String[][] convertArrayListToStringArr(ArrayList<String[]> arr){
        String[][] newArr = new String[arr.size()][arr.get(0).length];
        
        for (int i = 0;i < arr.size(); i++){
            int j;
            for (j = 0; j < arr.get(0).length && j < arr.get(i).length;j++){
                if (arr.get(i)[j].length() == 0){ //if the current contents in the cell are void
                    newArr[i][j] = " ";
                }else{
                    newArr[i][j] = arr.get(i)[j].replaceAll("\"","");  //removing any double quotations in the CSV file
                }
            }
            while (j < arr.get(0).length){ //if column length is still not the same as the first row, make sure to make adding an empty string to make sure that all columns in the array are fixed
                newArr[i][j++] = " ";
            }

        }
        return newArr;
    }

 
    /*
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
    */
 }
