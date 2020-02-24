import java.util.Arrays;
import java.util.ArrayList;
 import java.io.BufferedReader;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.io.FileReader;
 /***
  * Notes:
  *     For stormdata_yyyy.csv, the start dates are listed as 
  *     ["BEGIN_YEARMONTH", "BEGIN_DAY", "BEGIN_TIME", "END_YEARMONTH", "END_DAY", "END_TIME",...]
  *
 ***/
 public class ReadCSV{
    public static void readFile(String pathToCSV) throws IOException{
        BufferedReader csvReader = new BufferedReader(new FileReader(pathToCSV));
        String row = "";
        int count = 0;
        ArrayList<String[] > dataSetDates = new ArrayList<String[]>();
        ArrayList<String> dataSetLocationStates = new ArrayList<String>();
        ArrayList<String[]> dataSet = new ArrayList<String[]>(); 
        while ((row = csvReader.readLine()) != null) {
            
            String[] data = row.split(",");
            //System.out.println(Arrays.toString(data));
            if (count > 0){
                //dataSetDates.add(parseDates(data,0,0,1,3,3,4));
                //dataSetLocationStates.add(parseLocationsByStateOrProvince(data,8));
                dataSet.add(data);
            }    
            count += 1;
        }
        csvReader.close();
        //String[] LocationStates = new String[dataSetLocationStates.size()];
        //String[] LocationStates = convertArrayListToStringArr(dataSetLocationStates);
        String[][] data = convertArrayListToStringArr(dataSet);
        System.out.println(Arrays.toString(dataSet.get(0)));
        System.out.println("*"+dataSet.get(0)[1]);
        MergeSort.sort(data,8);
        
        for (int i = 0;i < data.length; i++){
            //System.out.println(Arrays.toString(data[i]));
            System.out.println(data[i][8]);
        }
    
    }
    /*
    private static String[] convertArrayListToStringArr(ArrayList<String> arr){
        String[] newArr = new String[arr.size()];
        for (int i = 0;i < arr.size(); i++){
            newArr[i] = arr.get(i);
           
        }
        return newArr;
    }
    */
    
     private static String[][] convertArrayListToStringArr(ArrayList<String[]> arr){
        String[][] newArr = new String[arr.size()][arr.get(0).length];
        for (int i = 0;i < arr.size(); i++){
            for (int j = 0; j < arr.get(0).length;j++){

                newArr[i][j] = arr.get(i)[j];
            }   
        }
        return newArr;
    }
    
    public static String[] parseDates(String[] row, int startYr, int startMth, int startDy, int endYr, int endMth, int endDy){
        //For stormData_yyyy.csv : row[0] = yyyymm (start), row[1] = d (start), row[3] = yyyymm (end), row[4] = d (end)
        String startYear = row[startYr].substring(0,4);
        String startMonth = row[startMth].substring(3,5);
        String startDay = row[startDy];

        String endYear = row[endYr].substring(0,4);
        String endMonth = row[endMth].substring(3,5);
        String endDay = row[endDy];

        String[] dateArray = {startYear,startMonth,startDay,endYear,endMonth,endDay};
        return dateArray;
       // System.out.println("Start: " + startYear+ "//"+ startMonth+"//"+ startDay+ " - End: " + endYear + "//" + endMonth + "//" + endDay);
    }
    public static String parseLocationsByStateOrProvince(String[] row,int location){
        //System.out.println(row[location]);
        return new String(row[location]);
    }
        
    public static void  main(String[] args) throws IOException{
       readFile("../Data_Sets/stormdata_2013.csv");
    }
 }
