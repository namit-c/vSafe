import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;



public class DangerRating{
   private static  HashMap<String,Double > injuryDict = new HashMap<String, Double>();
   private static HashMap<String,Double> deathDict = new HashMap<String,Double>();
   private static  HashMap<String,Double > damageDict = new HashMap<String, Double>();
   private static Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
   private static HashMap<String,Double> earthquakeDict = new HashMap<String,Double>();

   
   private static HashMap<String,Double> listOfAllCitiesHash = new HashMap<String,Double>();

  
   //Calculating the average magintude per city from 2019 to 1985
   public static void findEarthQuakeData(int magIndex, int cityCol, int dateIndex, String month, String[][] dataSet){ 
       //int monthCol = 0; //in the 0 col for earthquake csv file
       for (int z = 0; z < dataSet.length; z++){
            try{
                //System.out.println(dataSet[z][cityCol]);
                //
                //the dates in eqarchives are listed as yyyy-mm-dd ; from index 5 to 6, will give the month value
                if (dataSet[z][cityCol].contains("from") && dataSet[z][dateIndex].substring(5,7).equals(month)){ 
                    //usually each city will have "DISTANCE km from CITYNAME", so below will be taking the substring to obtain the cityName
                    String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase(); 
                    earthquakeDict.put(cityName, 0.0); 
                    listOfAllCitiesHash.put(cityName,0.0);
                }
            }catch(StringIndexOutOfBoundsException e){
            }
        }

        for (int z = 0; z < dataSet.length; z++){
            try{
                String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase();  
                double currentCityMag = earthquakeDict.get(cityName);
                currentCityMag += Double.parseDouble(dataSet[z][magIndex]);
                earthquakeDict.put(cityName, currentCityMag);


            }catch(NullPointerException | StringIndexOutOfBoundsException e){

            }
        
        }
        
        //earthquakeDict.forEach((key, value) -> System.out.println(key + ": " + value));
        //
        for (Map.Entry mapElement : earthquakeDict.entrySet()){
            String key = (String)mapElement.getKey();  
            double value = ((double)mapElement.getValue()/(2019.0-1985.0));
            
            earthquakeDict.put(key,value); 
                        
        }
        System.out.println(earthquakeDict.size());

   }

   //Sum up all injuries, deaths and damage values that occur for each city and it's corresponding event for data over 10 years
   public static void findDangerStatsOfDataSets(int cityIndex, int eventIndex, int injuryIndex, int deathIndex, int damageIndex, int dateIndex, String month, String[][][] dataSet ){
        //Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
       // int monthCol = 0;
        int intOfMonth = Integer.parseInt(month);
        for (int z = 0; z < dataSet.length; z++){

            for (int i = 0; i < dataSet[z].length; i++){ //collecting all unique city-event and setting the key values to 0.0
                int gettingMonthEndIndex = dataSet[z][i][dateIndex].indexOf("/");
                int currentMonth = 0;
                try{
                    currentMonth = Integer.parseInt(dataSet[z][i][dateIndex].substring(0, gettingMonthEndIndex));
                    
                }catch(StringIndexOutOfBoundsException e){
                    continue;
                }catch(NumberFormatException e){
                    continue;
                }
                if (currentMonth == intOfMonth){
                    city_set.add(dataSet[z][i][cityIndex].toUpperCase());
                    listOfAllCitiesHash.put(dataSet[z][i][cityIndex].toUpperCase(),0.0);
                    injuryDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
                    deathDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
                    damageDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
                }
            }
        }
        
        String[] cityLst = new String[city_set.size()]; //converting the city_set into an array
        city_set.toArray(cityLst);
        //listOfAllCities.addAll(Arrays.asList(cityLst));

        //System.out.println(city_set);

        int index = 0;
        for (int z = 0; z < dataSet.length; z++){
            ArrayList<String[]> listArr = convertTwoDToArrayList(dataSet[z]);
            for (int i = 0; i < cityLst.length; i++){ //itterating through all cities
                while (index >= 0){
                    index = BinarySearch.binarySearch(listArr,cityLst[i],cityIndex);
                    

                    if (index >=0 ){ //Current City is found
                        
                            
                        String tempStr = dataSet[z][index][cityIndex].toUpperCase()+"-"+dataSet[z][index][eventIndex]; //string would be "SOUTH CAROLINA-Heavy Rain"
                        
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
                                
                                //the following conditions are use to consider cells that have either "K" or "M"
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
        dataSet = null;
        System.gc();
        //damageDict.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println(damageDict.size());
        System.out.println(injuryDict.size());
        System.out.println(deathDict.size());
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

    private static void determineDangerRatingEarthquake(String month) throws IOException{
        HashMap<String, Double> earthquakeProb = vProb.probEq();
        for (Map.Entry mapElement : earthquakeDict.entrySet()) { 
            String key = (String)mapElement.getKey(); 
            
            double getCurrentMag = earthquakeDict.get(key);
            //System.out.println(getCurrentMag);
            double currentIndexRating = 0.0;
            if (getCurrentMag < 2.5){
                currentIndexRating += 0.0;
            } else if (2.5 < getCurrentMag && getCurrentMag < 4.9){
                currentIndexRating += 5;
            }else if (5 < getCurrentMag && getCurrentMag < 5.9){
                currentIndexRating += 10;
            }else{
               currentIndexRating += 50;
            }

            
            if (earthquakeProb.containsKey(key + " " + month + " " + "Earthquake")){
                currentIndexRating *= earthquakeProb.get(key + " " + month + " " + "Earthquake")/100;
            }
            if (currentIndexRating > 100){
                currentIndexRating = 100;
            }

            currentIndexRating = Math.round(currentIndexRating);
            listOfAllCitiesHash.put(key,currentIndexRating);
            //int value = ((int)mapElement.getValue() + 10);  
            //System.out.println(key + " : " + value); 
        } 

        //listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value)); 
         
    }

    public static void determineDangerRatingStormRelated(String month) throws IOException{
         int index = 0;
         Iterator<Map.Entry<String, Double>> injury = injuryDict.entrySet().iterator(); 
         Iterator<Map.Entry<String, Double>> death = deathDict.entrySet().iterator();
         Iterator<Map.Entry<String, Double>> damage = damageDict.entrySet().iterator();
         
         
         HashMap<String,Double> stormDataProb = vProb.determineAllProbSD();
         //HashMap<String, Double> earthquakeProb = vProb.probEq();
         HashMap<String, Double> canadaNaturalDis = vProb.probCDD();

         while(injury.hasNext() && death.hasNext() && damage.hasNext()){
            //Iterator<Map.Entry<String, String>> index = injuryDict.entrySet().iterator();
            Map.Entry<String, Double> entryInj = injury.next();
            Map.Entry<String, Double> entryDeat = death.next();
            Map.Entry<String, Double> entryDamg = damage.next();
            
            String key = (String)entryInj.getKey();     
            String currentEvent = (String)entryInj.getKey().substring(((String)entryInj.getKey()).indexOf("-") + 1);
            String currentCity = (String)entryInj.getKey().substring(0,((String)entryInj.getKey()).indexOf("-") );
             
            //System.out.println(currentCity);
            double cityIndexVal = 0.0; 
            try{
                cityIndexVal = listOfAllCitiesHash.get(currentCity);
            }catch(NullPointerException e){
            }
            
            if (currentEvent.contains("Heat") || currentEvent.contains("Drought")){
                
                //deaths
                if (deathDict.get(key) > 5.0){
                    cityIndexVal += 20;

                }
                if (injuryDict.get(key) < 25.0){
                    cityIndexVal += 10;
                }
                else if (25.0 < injuryDict.get(key) && injuryDict.get(key) < 50.0){
                    cityIndexVal += 20;
                }else{
                    cityIndexVal += 50;
                }
                if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                }
            }
            else if (currentEvent.contains("Avalach")){
                if (deathDict.get(key) > 5.0){
                    cityIndexVal += 20;
                }

                if (injuryDict.get(key) < 10.0){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 10.0){
                    cityIndexVal += 10;
                }
                if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                } 

            }

            else if (currentEvent.contains("Tornado")){
                if (deathDict.get(key) > 0){
                    cityIndexVal += 20;
                }

                if (injuryDict.get(key) < 10){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 10){
                    cityIndexVal += 10;
                }
                
                if (damageDict.get(key) < 10000.0){
                    cityIndexVal += 5;
                }
                else if (100000 <= damageDict.get(key) && damageDict.get(key) < 250000){
                    cityIndexVal += 10;
                }else{
                    cityIndexVal += 20;
                }
                if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                }  
            }

            else if (currentEvent.contains("Wildfire")){
               if (deathDict.get(key) > 0){
                    cityIndexVal += 25;
                }

                if (injuryDict.get(key) < 10){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 10){
                    cityIndexVal += 15;
                }
                
                if (damageDict.get(key) < 10000.0){
                    cityIndexVal += 10;
                }
                else if (100000 <= damageDict.get(key) && damageDict.get(key) < 250000){
                    cityIndexVal += 15;
                }else{
                    cityIndexVal += 25;
                }

                if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                } 
            }

            else if (currentEvent.contains("Heavy Snow") || currentEvent.contains("Blizzard") || currentEvent.contains("Winter Storm")){
                if (deathDict.get(key) > 0){
                    cityIndexVal += 15;
                }

                if (injuryDict.get(key) < 10){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 10){
                    cityIndexVal += 15;
                }
                
                if (damageDict.get(key) < 10000.0){
                    cityIndexVal += 5;
                }
                else if (100000 <= damageDict.get(key) && damageDict.get(key) < 250000){
                    cityIndexVal += 10;
                }else{
                    cityIndexVal += 15;
                }

                if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                } 

            }

            else if (currentEvent.contains("Flood") || currentEvent.contains("Flash Flood")){
               if (deathDict.get(key) > 0){
                    cityIndexVal += 20;
                }

                if (injuryDict.get(key) < 25){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 25){
                    cityIndexVal += 10;
                }
                
                if (damageDict.get(key) < 10000.0){
                    cityIndexVal += 5;
                }
                else if (100000 <= damageDict.get(key) && damageDict.get(key) < 30000){
                    cityIndexVal += 15;
                }else{
                    cityIndexVal += 25;
                }

                 if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                }
            }

            else{
               if (deathDict.get(key) > 0){
                    cityIndexVal += 20;
                }

                if (injuryDict.get(key) < 10){
                    cityIndexVal += 5;
                }
                else if (injuryDict.get(key) >= 10){
                    cityIndexVal += 10;
                }
                
                if (damageDict.get(key) < 10000.0){
                    cityIndexVal += 5;
                }
                else if (100000 <= damageDict.get(key) && damageDict.get(key) < 250000){
                    cityIndexVal += 10;
                }else{
                    cityIndexVal += 20;
                }

               if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                } 
        
            }
            if (cityIndexVal > 100){
                cityIndexVal = 100;
            }
            cityIndexVal = Math.round(cityIndexVal);
            listOfAllCitiesHash.put(currentCity,cityIndexVal);

         }

        //listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));  
    }

    public static HashMap<String, Double> loadAllDangerRating(String month) throws IOException{
        
        String[][] data1 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv",15,19);
        String[][] data2 = ReadCSV.readFile("../Data_Sets/stormdata_2012.csv",15,19);
        String[][] data3 = ReadCSV.readFile("../Data_Sets/stormdata_2011.csv",15,19);
        String[][] data4 = ReadCSV.readFile("../Data_Sets/stormdata_2010.csv",15,19);
        //String[][] data5 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv",15,19);
        //String[][] data6 = ReadCSV.readFile("../Data_Sets/stormdata_20015.csv",15,19);
        //String[][] data7 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv",15,19);
        //String[][] data15 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv",15,19);
        //String[][] data9 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv",15,19);
        //String[][] data10 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv",15,19);
        //String[][] data11= ReadCSV.readFile("../Data_Sets/stormdata_2003.csv",15,19);
        String[][][] allDataSets = {data1,data2,data3,data4};
        
        int cityCol = 15;
        int eventCol = 12;
        int injuryCol = 20;
        int deathCol = 22;
        int damageCol = 24;
        int dateCol = 17;
        
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,month,allDataSets);
        data1 = null;
        data2 = null;
        data3 = null;
        data4 = null;
        allDataSets = null;
        System.gc(); //calling system gargbage collector *Doesnt always free memory
                     //https://stackoverflow.com/questions/1567979/how-to-free-memory-in-java
        String[][]data5 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv",15,19);
        String[][] data6 = ReadCSV.readFile("../Data_Sets/stormdata_2008.csv",15,19);
        String[][] data7 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv",15,19);
        String[][] data8 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv",15,19);
        String[][][] newData = {data5,data6,data7,data8};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,month,newData);

        data5 = null;
        data6 = null;
        data7 = null;
        data8 = null;
        newData = null;
        
        
        String[][] data9 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv",15,19);
        String[][] data10 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv",15,19);
        String[][] data11 = ReadCSV.readFile("../Data_Sets/stormdata_2003.csv",15,19);
        String[][][] newData1 = {data9,data10,data11};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,month,newData1);
        
        determineDangerRatingStormRelated(month);
        
        data9 = null;
        data10 = null;
        data11 = null;
        newData1 = null;
        
        String[][] data12 = ReadCSV.readFile("../Data_Sets/eqarchive-en.csv",1,6);
        findEarthQuakeData(4,6,0,month,data12); 
        
        data12 = null;
        
        determineDangerRatingEarthquake(month);
        
        String[][] data13 = ReadCSV.readFile("../Data_Sets/CDD_csv.csv",4,1);
        String[][][] newData2 = {data13};
        cityCol = 4;
        eventCol = 1;
        injuryCol = 8;
        deathCol = 7;
        damageCol = 10;
        dateCol = 12;
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,month,newData2);

        return listOfAllCitiesHash;
    }
   

       public static void main(String[] args) throws IOException{
        System.out.println("/");
        loadAllDangerRating("6");
        //multiplyDangerRatingWithProb();
        listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));
        /*
        String[][] test = ReadCSV.readFile("../Data_Sets/uscities.csv",1,8); 
        for (int i = 0; i < test.length; i++){
            if (test[i][9].contains("Monroe") || test[i][8].contains("Monroe")){
                System.out.println(test[i][1] + " Lat:" + test[i][8] + " Lon:" + test[i][9]);
            }
        }
        */
    }
}
