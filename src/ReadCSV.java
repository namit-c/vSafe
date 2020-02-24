import java.util.Arrays;

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
        while ((row = csvReader.readLine()) != null) {
            
            String[] data = row.split(",");
            //System.out.println(Arrays.toString(data));
		    if (count > 0){
                parseDates(data);
            }    
            count += 1;
            /***
            count +=1;
            if (count > 2){
              break;
            }
            ***/  
            //break;
        }
        csvReader.close();
    }
    public static void parseDates(String[] row){
        //row[0] = yyyymm (start), row[1] = d (start), row[2] = yyyymm (end), row[3] = d (end)
        String startYear = row[0].substring(0,4);
        String startMonth = row[0].substring(3,5);
        String startDay = row[1];

        String endYear = row[3].substring(0,4);
        String endMonth = row[3].substring(3,5);
        String endDay = row[4];


        System.out.println("Start: " + startYear+ "//"+ startMonth+"//"+ startDay+ " - End: " + endYear + "//" + endMonth + "//" + endDay);
    }
    public static void  main(String[] args) throws IOException{
       readFile("../Data_Sets/stormdata_2013.csv");
    }
 }
