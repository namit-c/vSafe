import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;

public class DangerRating{

   public static void findDangerStatsStormData(int cityIndex, int eventIndex, int injuryIndex, int deathIndex, int damageIndex, String[][] dataSet ){


        Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
        HashMap<String,Double > injuryDict = new HashMap<String, Double>(); //injuryDict will store all unique city with its events such as 
                                                                              //such as "cityName-eventName"
                                                                              //i.e "ARKANSAS" - "Winter Weather"        
        HashMap<String,Double> deathDict = new HashMap<String,Double>();
        HashMap<String,Double> damageDict = new HashMap<String,Double>();
        for (int i = 0; i < dataSet.length; i++){ //collecting all unique city-event and setting the key values to 0.0
            city_set.add(dataSet[i][cityIndex]);
            injuryDict.put(dataSet[i][cityIndex]+"-"+dataSet[i][eventIndex], 0.0);
            deathDict.put(dataSet[i][cityIndex]+"-"+dataSet[i][eventIndex], 0.0);
            damageDict.put(dataSet[i][cityIndex]+"-"+dataSet[i][eventIndex], 0.0);
        }
        
        String[] cityLst = new String[city_set.size()]; //converting the city_set into an array
        city_set.toArray(cityLst);


        System.out.println(city_set);
        ArrayList<String[]> listArr = convertTwoDToArrayList(dataSet);

        int index = 0;
        for (int i = 0; i < cityLst.length; i++){ //itterating through all cities
            while (index >= 0){
                index = BinarySearch.binarySearch(listArr,cityLst[i],cityIndex); 
                if (index >=0){ //Current City is found
                    
                    String tempStr = dataSet[index][cityIndex]+"-"+dataSet[index][eventIndex]; //string would be "SOUTH CAROLINA-Heavy Rain"
                    double injury_counter = injuryDict.get(tempStr);
                    double death_counter = deathDict.get(tempStr);
                    double damage_counter = damageDict.get(tempStr);
                    
                   
                    if (dataSet[index][injuryIndex].length() != 0 && dataSet[index][deathIndex].length() != 0 && dataSet[index][damageIndex].length() != 0){
                        
                        try{

                            injury_counter += Double.parseDouble(dataSet[index][injuryIndex]) ;
                            death_counter += Double.parseDouble(dataSet[index][deathIndex]) ;
                            
                            if (dataSet[index][damageIndex].contains("K")){ //if the cell contains a K
                                damage_counter += Double.parseDouble(dataSet[index][damageIndex].substring(0,dataSet[index][damageIndex].indexOf("K"))) *1000;
                            
                            }else if (dataSet[index][damageIndex].contains("M")){ //if the cecll contains M
                                damage_counter += Double.parseDouble(dataSet[index][damageIndex].substring(0,dataSet[index][damageIndex].indexOf("M"))) *1000000;
                            
                            }else{
                                damage_counter += Double.parseDouble(dataSet[index][damageIndex]) ;
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

        //System.out.println(Arrays.asList(damageDict));
        damageDict.forEach((key, value) -> System.out.println(key + ": " + value));
   }

    private static ArrayList<String[]> convertTwoDToArrayList (String[][] arr){
        ArrayList<String[]> convert = new ArrayList<String[]>();
        for (int i = 0; i < arr.length; i ++){
            convert.add(arr[i]);
        }
        return convert;
    }

    public static void main(String[] args) throws IOException{
        String[][] data1 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv",8,19);

        findDangerStatsStormData(8,12,20,22,24,data1);
    }
}
