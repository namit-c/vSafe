import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class DangerRating{

   public static void findDangerStatsStormData(int cityIndex, int eventIndex, int injuryIndex, int deathIndex, int damageIndex, String[][][] dataSet ){


        Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
        HashMap<String,Double > injuryDict = new HashMap<String, Double>(); //injuryDict will store all unique city with its events such as 
                                                                              //such as "cityName-eventName"
                                                                              //i.e "ARKANSAS" - "Winter Weather"        
        HashMap<String,Double> deathDict = new HashMap<String,Double>();
        HashMap<String,Double> damageDict = new HashMap<String,Double>();
        for (int z = 0; z < dataSet.length; z++){

            for (int i = 0; i < dataSet[z].length; i++){ //collecting all unique city-event and setting the key values to 0.0
                city_set.add(dataSet[z][i][cityIndex]);
                injuryDict.put(dataSet[z][i][cityIndex]+"-"+dataSet[z][i][eventIndex], 0.0);
                deathDict.put(dataSet[z][i][cityIndex]+"-"+dataSet[z][i][eventIndex], 0.0);
                damageDict.put(dataSet[z][i][cityIndex]+"-"+dataSet[z][i][eventIndex], 0.0);
            }
        }
        
        String[] cityLst = new String[city_set.size()]; //converting the city_set into an array
        city_set.toArray(cityLst);


        System.out.println(city_set);
        //ArrayList<String[]> listArr = convertTwoDToArrayList(dataSet);

        int index = 0;
        for (int z = 0; z < dataSet.length; z++){
            ArrayList<String[]> listArr = convertTwoDToArrayList(dataSet[z]);
            for (int i = 0; i < cityLst.length; i++){ //itterating through all cities
                while (index >= 0){
                    index = BinarySearch.binarySearch(listArr,cityLst[i],cityIndex); 
                    if (index >=0){ //Current City is found
                        
                        String tempStr = dataSet[z][index][cityIndex]+"-"+dataSet[z][index][eventIndex]; //string would be "SOUTH CAROLINA-Heavy Rain"
                        
                        double injury_counter = 0.0;
                        double death_counter = 0.0;      
                        double damage_counter = 0.0;
                        try{

                           injury_counter = injuryDict.get(tempStr);
                            death_counter = deathDict.get(tempStr);
                            damage_counter = damageDict.get(tempStr);
                        }catch(NullPointerException e){
                        }
                        
                       
                        if (dataSet[z][index][injuryIndex].length() != 0 && dataSet[z][index][deathIndex].length() != 0 && dataSet[z][index][damageIndex].length() != 0){
                            try{

                                injury_counter += Double.parseDouble(dataSet[z][index][injuryIndex]) ;
                                death_counter += Double.parseDouble(dataSet[z][index][deathIndex]) ;
                                if (dataSet[z][index][damageIndex].contains("K")){ //if the cell contains a K
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex].substring(0,dataSet[z][index][damageIndex].indexOf("K"))) *1000;
                                
                                }else if (dataSet[z][index][damageIndex].contains("M")){ //if the cecll contains M
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex].substring(0,dataSet[z][index][damageIndex].indexOf("M"))) *1000000;
                                
                                }else{
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex]) ;
                                }
                            }catch(NumberFormatException e){
                             
                            }

                        }
                        injuryDict.put(tempStr,injury_counter);
                        deathDict.put(tempStr,death_counter);
                        damageDict.put(tempStr,damage_counter);
                         
                        listArr.remove(index); //removing the current row will prevent binarySearch from searching the same row again
                    }
                    
                }
                index =0;
                //System.out.println(cityLst[i] + " " +average);
            }
        }

        //System.out.println(Arrays.asList(damageDict));
        damageDict.forEach((key, value) -> System.out.println(key + ": " + value));
   }

    private static ArrayList<String[]> convertTwoDToArrayList (String[][] arr){
        ArrayList<String[]> convert = new ArrayList<String[]>();
        for (int i = 0; i < arr.length; i ++){
            String[] temp = new String[arr[i].length];
            for (int j = 0; j < temp.length; j++){
                
                if (arr[i][j].startsWith("\"")){
                    temp[j] = arr[i][j].substring(1,arr[i][j].length());
                }else{
                    temp[j] = arr[i][j];
                    //convert.add(arr[i]);
                }
            }
            convert.add(temp);
        }
        return convert;
    }

    public static void main(String[] args) throws IOException{
        String[][] data1 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv",8,19);
        String[][] data2 = ReadCSV.readFile("../Data_Sets/stormdata_2012.csv",8,19);
        String[][] data3 = ReadCSV.readFile("../Data_Sets/stormdata_2011.csv",8,19);
        String[][] data4 = ReadCSV.readFile("../Data_Sets/stormdata_2010.csv",8,19);
        //String[][] data5 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv",8,19);
        //String[][] data6 = ReadCSV.readFile("../Data_Sets/stormdata_2008.csv",8,19);
        //String[][] data7 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv",8,19);
        //String[][] data8 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv",8,19);
        //String[][] data9 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv",8,19);
        //String[][] data10 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv",8,19);
        //String[][] data11= ReadCSV.readFile("../Data_Sets/stormdata_2003.csv",8,19);
        String[][][] allDataSets = {data1,data2,data3,data4};
        findDangerStatsStormData(8,12,20,22,24,allDataSets);
    }
}
