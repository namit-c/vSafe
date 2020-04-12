import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @brief Class computes the danger rating for all cities from the data sets obtained. It will go through each city and assign a specific danger rating based on our interpretation
 */
public class DangerRating{
   private static  HashMap<String,Double > injuryDict = new HashMap<String, Double>();
   private static HashMap<String,Double> deathDict = new HashMap<String,Double>();
   private static  HashMap<String,Double > damageDict = new HashMap<String, Double>();
   private static Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
   private static HashMap<String,Double> earthquakeDict = new HashMap<String,Double>();

   
   private static HashMap<String,Double> listOfAllCitiesHash = new HashMap<String,Double>();

  
   //Calculating the average magintude per city from 2019 to 1985
   
   /**
	 * @brief Finds adds up all the magnitudes for each city in a specific month and store it in earthquakeDict , then it will update the earthquakeDict to compute the avereage which will divide the total by (2019 - 1985)
	 * @param magIndex magnitude column in the dataset
	 * @param cityCol city column in the dataset
	 * @param dateIndex starting date column of the natural disaster in the dataset
	 * @param dataSet is the 2D string array containing information about the CSV file
	 */
   private static void findEarthQuakeData(int magIndex, int cityCol, int dateIndex, String[][] dataSet){ 
     
	   //adding the intial cityNames to the earthquakeDict, listOfAllCitiesHash and also including the month number
       for (int z = 0; z < dataSet.length; z++){
            try{
                
            	//usually each city will have "DISTANCE km from CITYNAME", so below will be taking the substring to obtain the cityName
                //the dates in eqarchives are listed as yyyy-mm-dd ; from index 5 to 6, will give the month value
                if (dataSet[z][cityCol].contains("from") ){ 
                    
                    String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase(); 
                    earthquakeDict.put(cityName + "-" + dataSet[z][dateIndex].substring(5,7), 0.0); 
                    listOfAllCitiesHash.put(cityName + "-" + dataSet[z][dateIndex].substring(5,7) ,0.0);
                }
            }catch(StringIndexOutOfBoundsException e){ //some strings may not contain the "from" phrase which can be ignored as they wont have a specific city
            }
        }

       	//adding up the total magnitudes for each city in a specific month
        for (int z = 0; z < dataSet.length; z++){
            try{
            	//cityName will take the substring of the intial dataset by substringing after the word "from " (the addiotnal space) and including the month number in the index
                String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase()+"-"+dataSet[z][dateIndex].substring(5,7);  
                double currentCityMag = earthquakeDict.get(cityName);
                currentCityMag += Double.parseDouble(dataSet[z][magIndex]);
                earthquakeDict.put(cityName, currentCityMag);


            }catch(NullPointerException | StringIndexOutOfBoundsException e){

            }
        
        }
        
        //updating the earthquakeDict to update each city in a specific month to maintain the average earthquake magintude
        for (Map.Entry mapElement : earthquakeDict.entrySet()){
            String key = (String)mapElement.getKey();  
            double value = ((double)mapElement.getValue()/(2019.0-1985.0));
            
            earthquakeDict.put(key,value); 
                        
        }
        //System.out.println(earthquakeDict.size());

   }

   /**
  	 * @brief Compute the sum up all injuries, deaths and damage values that occur for each city for all months avaiable
  	 * @param cityIndex city column in the dataset
  	 * @param eventIndex event column in the dataset
  	 * @param injuryIndex injury column in the dataset
  	 * @param deathIndex death column in the dataSet
  	 * @param damageIndex property damage index in the dataset
  	 * @param dateIndex starting date column in the index
  	 * @param dataSet is the 3D string array containing information about the CSV file (each row will be pointing to a 2D string array of a csv file)
  	 */
   private static void findDangerStatsOfDataSets(int cityIndex, int eventIndex, int injuryIndex, int deathIndex, int damageIndex, int dateIndex, String[][][] dataSet ){
	   
	   //adding the intial cityNames to the city_set, injuryDict, deathDict, damageDict , listOfAllCitiesHash and also including the month number (and the dict will include the event name)
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
                    city_set.add(dataSet[z][i][cityIndex]);
                    listOfAllCitiesHash.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+currentMonth,0.0);
                    injuryDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+currentMonth+"-"+dataSet[z][i][eventIndex], 0.0);
                    deathDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+currentMonth+"-"+dataSet[z][i][eventIndex], 0.0);
                    damageDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+currentMonth+"-"+dataSet[z][i][eventIndex], 0.0);
            }
        }
        
        String[] cityLst = new String[city_set.size()]; //converting the city_set into an array
        city_set.toArray(cityLst);


        int index = 0;
        for (int z = 0; z < dataSet.length; z++){
            ArrayList<String[]> listArr = convertTwoDToArrayList(dataSet[z]);
            for (int i = 0; i < cityLst.length; i++){ //itterating through all cities
                while (index >= 0){
                    index = BinarySearch.binarySearch(listArr,cityLst[i],cityIndex);
                    

                    if (index >=0 ){ //Current City is found
                        
                        int gettingMonthEndIndex = dataSet[z][i][dateIndex].indexOf("/");
                        int currentMonth = 0;
                        try{
                            currentMonth = Integer.parseInt(dataSet[z][i][dateIndex].substring(0, gettingMonthEndIndex));
                            
                        }catch(StringIndexOutOfBoundsException e){
                 
                        }catch(NumberFormatException e){
                    
                        }
                       
                        //tempstr will be the key to access the injuryDict, deathDict, damageDict values in the format of "CITY-monthNumber-EventName"
                        String tempStr = dataSet[z][index][cityIndex].toUpperCase()+"-"+currentMonth+"-"+dataSet[z][index][eventIndex]; 
                        
                        double injury_counter = 0.0;
                        double death_counter = 0.0;      
                        double damage_counter = 0.0;
                        
                        //collecting the current value in the hashtable from the key 
                        try{

                           injury_counter = injuryDict.get(tempStr);
                            death_counter = deathDict.get(tempStr);
                            damage_counter = damageDict.get(tempStr);
                        }catch(NullPointerException e){
                        }
                        
                       
                        //sum up  the current value with the previous counter stored in the hasmap
                        if (dataSet[z][index][injuryIndex].length() != 0 && dataSet[z][index][deathIndex].length() != 0 && dataSet[z][index][damageIndex].length() != 0){
                            try{

                                injury_counter += Double.parseDouble(dataSet[z][index][injuryIndex]); 
                                death_counter += Double.parseDouble(dataSet[z][index][deathIndex]) ;
                                
                                //the following conditions are use to consider cells that have either "K" or "M"
                                if (dataSet[z][index][damageIndex].contains("K")){ //if the cell contains a K - thousand
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex].substring(0,dataSet[z][index][damageIndex].indexOf("K"))) *1000;
                                
                                }else if (dataSet[z][index][damageIndex].contains("M")){ //if the cecll contains M - million
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex].substring(0,dataSet[z][index][damageIndex].indexOf("M"))) *1000000;
                                
                                }else{
                                    damage_counter += Double.parseDouble(dataSet[z][index][damageIndex]) ;
                                }
                            }catch(NumberFormatException e){
                             
                            }

                        }
                        
                        //update the values back in the diconaries
                        injuryDict.put(tempStr,injury_counter);
                        deathDict.put(tempStr,death_counter);
                        damageDict.put(tempStr,damage_counter);
                         
                        listArr.remove(index); //removing the current row will prevent binarySearch from searching the same row again
                    }else{
                        break;
                    }
                    
                }
                index =0;
             
            }
        }

   
        dataSet = null;
        System.gc();
        //damageDict.forEach((key, value) -> System.out.println(key + ": " + value));
    
   }

   /**
 	 * @brief Convert a 2D String array into an ArrayList with each row containing the a single 1D String array (making it easier to delete rows)
 	 * @param dataSet is the 2D string array containing information about the CSV file 
 	 * @return returns the ArrayList<String[]> representing the same information in the CSV file while exlcuding some qutotiaotns that may appear in certain cells
 	 */
    private static ArrayList<String[]> convertTwoDToArrayList (String[][] arr){
        ArrayList<String[]> convert = new ArrayList<String[]>();
        
        for (int i = 0; i < arr.length; i ++){
            String[] temp = new String[arr[i].length];
            for (int j = 0; j < temp.length; j++){
                
                if (arr[i][j].startsWith("\"")){	//removing any unncessary quotations in the strings
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

    /**
 	 * @brief Take the current earthquakeDict and using vProb.probEq() to determine danger rating for all cities in the data set for a given month
 	 * @throws IOException
 	 */
    private static void determineDangerRatingEarthquake() throws IOException{
        HashMap<String, Double> earthquakeProb = vProb.probEq();
        for (Map.Entry mapElement : earthquakeDict.entrySet()) { 
            String key = (String)mapElement.getKey(); 
            
            double getCurrentMag = earthquakeDict.get(key);
     
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

            //collecting specific cityname and month number
            String city = key.substring(0,key.indexOf("-")); 
            String monthNum = key.substring(key.indexOf("-")+1);
            
            //determining if there is a probability value to multiply with the current indexrating
            if (earthquakeProb.containsKey(city + " " + monthNum + " " + "Earthquake")){
                currentIndexRating *= earthquakeProb.get(city + " " + monthNum + " " + "Earthquake")/100; //mutlplying the percentage value with the current indexrating
            }
            if (currentIndexRating > 100){
                currentIndexRating = 100;
            }

            currentIndexRating = Math.round(currentIndexRating);
            listOfAllCitiesHash.put(key,currentIndexRating);
         
        } 

        //listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value)); 
         
    }

    /**
 	 * @brief Take the current injuryDict, deathDict and DamageDict and using vProb.probEq() to accumulate various danger rating values that different from natural disasters
 	 * @throws IOException
 	 */
    private static void determineDangerRatingStormRelated(HashMap<String, Double> stormData) throws IOException{
         
         Iterator<Map.Entry<String, Double>> injury = injuryDict.entrySet().iterator(); 
         Iterator<Map.Entry<String, Double>> death = deathDict.entrySet().iterator();
         Iterator<Map.Entry<String, Double>> damage = damageDict.entrySet().iterator();
         
         
         HashMap<String,Double> stormDataProb = stormData;      
         HashMap<String, Double> canadaNaturalDis = vProb.probCDD();

         while(injury.hasNext() && death.hasNext() && damage.hasNext()){
      
        	//the following entries will be used to update map to go to the next item in the dictionary
            Map.Entry<String, Double> entryInj = injury.next(); 
            Map.Entry<String, Double> entryDeat = death.next();
            Map.Entry<String, Double> entryDamg = damage.next();
            
            String key = (String)entryInj.getKey(); //all keys in the injuryDict, deathDict and damageDict will be the same
            
            //since the format of the keys are cityName-monthNum-eventName
            int firstDashIndex = ((String)entryInj.getKey()).indexOf("-");	
            int secondDashIndex = ((String)entryInj.getKey()).indexOf("-", firstDashIndex + 1); 
            
            
            String currentEvent = (String)entryInj.getKey().substring(secondDashIndex + 1);
            String currentCity = (String)entryInj.getKey().substring(0,((String)entryInj.getKey()).indexOf("-") );
            String month = "";
            
            if (firstDashIndex >= 0 && secondDashIndex >= 0){ //some cities may not have a dash
                month = key.substring(firstDashIndex + 1, secondDashIndex);
                if (month.startsWith("0")) {
                	month = month.substring(1);
                }
            }
             
            
            double cityIndexVal = 0.0; 
            try{
                cityIndexVal = listOfAllCitiesHash.get(key.substring(0,secondDashIndex)) ;
               
            }catch(NullPointerException e){
            }
            catch(StringIndexOutOfBoundsException e){
            }
            
            if (currentEvent.contains("Heat") || currentEvent.contains("Drought")){
                
               
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
                
                //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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
                
              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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
                
              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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

              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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

              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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
                
              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
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

              //multiply the probability that is avaiable from the stormdata hashMap or candaNaturalDis hasmap)
               if (stormDataProb.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= stormDataProb.get(currentCity + " " + month + " " + currentEvent)/100;
                }
                else if (canadaNaturalDis.containsKey(currentCity + " " + month + " " + currentEvent)){
                    cityIndexVal *= canadaNaturalDis.get(currentCity + " " + month + " " + currentEvent)/100;
                } 
        
            }
            
            //cap the maximum rating  to be 100
            if (cityIndexVal > 100){
                cityIndexVal = 100;
            }
            cityIndexVal = Math.round(cityIndexVal); //rooudning the current indexrating
            
            try{
                listOfAllCitiesHash.put(currentCity+"-"+month ,cityIndexVal); //updating the hasmap
            }catch(StringIndexOutOfBoundsException e){
            }

         }

        //listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));  
    }


    /**
 	 * @brief load all the data sets we have to find the danger stats first, then calling determinDangerRating for both storm data and earthquakes
 	 * @return returns the listOfCitiesHashMap
 	 * @throws IOException
 	 */
    public static HashMap<String, Double> loadAllDangerRating() throws IOException{
        HashMap<String, Double> stormDataProb = new HashMap<String, Double>(); 
        int cityCol = 15;
        int eventCol = 12;
        int injuryCol = 20;
        int deathCol = 22;
        int damageCol = 24;
        int dateCol = 17;
        String[][] data1 = ReadCSV.readFile("Data_Sets/stormdata_2013.csv",15,19);
        String[][][] loadData0 = {data1};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData0);
        vProb.probSD(loadData0,stormDataProb);

        data1 = null;	//setting the string[] arrays to null will help with reducing overhead (“java.lang.OutOfMemoryError: GC overhead limit exceeded error”.)
        loadData0 = null;

        String[][] data2 = ReadCSV.readFile("Data_Sets/stormdata_2012.csv",15,19);
        String[][][] loadData1 = {data2};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData1);
        vProb.probSD(loadData1,stormDataProb);

         data2 = null;
        loadData1 = null;

       
        String[][] data3 = ReadCSV.readFile("Data_Sets/stormdata_2011.csv",15,19);
        String[][][] loadData2 = {data3};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData2);
        vProb.probSD(loadData2,stormDataProb);

         data3 = null;
        loadData2 = null;

        String[][] data4 = ReadCSV.readFile("Data_Sets/stormdata_2010.csv",15,19);
        String[][][] loadData3 = {data4};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData3);
        vProb.probSD(loadData3,stormDataProb);

         data4 = null;
        loadData3 = null;
        
      
        
      
        System.gc(); //calling system gargbage collector *Doesnt always free memory
                     //https://stackoverflow.com/questions/1567979/how-to-free-memory-in-java
        String[][]data5 = ReadCSV.readFile("Data_Sets/stormdata_2009.csv",15,19);
        String[][][] loadData4 = {data5};
          findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData4);
        vProb.probSD(loadData4,stormDataProb);

         data5 = null;
        loadData4 = null;
        
        String[][] data6 = ReadCSV.readFile("Data_Sets/stormdata_2008.csv",15,19);
        String[][][] loadData5 = {data6};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData5);
        vProb.probSD(loadData5,stormDataProb);

         data6 = null;
        loadData5 = null;

        String[][] data7 = ReadCSV.readFile("Data_Sets/stormdata_2007.csv",15,19);
        String[][][] loadData6 = {data7};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData6);
        vProb.probSD(loadData6,stormDataProb);

         data7 = null;
        loadData6 = null;

        String[][] data8 = ReadCSV.readFile("Data_Sets/stormdata_2006.csv",15,19);
        String[][][] loadData7 = {data8};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData7);
        vProb.probSD(loadData7,stormDataProb);

         data8 = null;
        loadData7 = null;

        
        
        String[][] data9 = ReadCSV.readFile("Data_Sets/stormdata_2005.csv",15,19);
        String[][][] loadData8 = {data9};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData8);
        vProb.probSD(loadData8,stormDataProb);

         data9 = null;
        loadData8 = null;

        String[][] data10 = ReadCSV.readFile("Data_Sets/stormdata_2004.csv",15,19);
        String[][][] loadData9 = {data10};
          findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData9);
        vProb.probSD(loadData9,stormDataProb);

         data10 = null;
        loadData9 = null;

        String[][] data11 = ReadCSV.readFile("Data_Sets/stormdata_2003.csv",15,19);
        String[][][] loadData10 = {data11};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData10);
        vProb.probSD(loadData10,stormDataProb);

         data11 = null;
        loadData10 = null;
       
  
        
        determineDangerRatingStormRelated( stormDataProb);
        

         
        String[][] data12 = ReadCSV.readFile("Data_Sets/eqarchive-en.csv",1,6);
        findEarthQuakeData(4,6,0,data12); 
        
        data12 = null;
        
        determineDangerRatingEarthquake();
        
        String[][] data13 = ReadCSV.readFile("Data_Sets/CDD_csv.csv",4,1);
        String[][][] newData2 = {data13};
        cityCol = 4;
        eventCol = 1;
        injuryCol = 8;
        deathCol = 7;
        damageCol = 10;
        dateCol = 12;
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,newData2);

        return listOfAllCitiesHash;
    }
    
    /**
  	 * @brief takes the current listOfCitiesHash and prdoduces a txt file for a specific month where each row has the cities for latidude and longitude and its associated danger rating 
  	 * @throws IOException
  	 */
    public static void writeDangerRateTxt(String month) throws IOException{
        String[][] US = ReadCSV.readFile("Data_Sets/uscities.csv",1,1);

        String[][] CAD = ReadCSV.readFile("Data_Sets/Canada_Cities.csv",0,0);

            

        ArrayList<String> USList = new ArrayList<String>();
        ArrayList<String> CADList = new ArrayList<String>();

        //BinarySearch cannot be used here because there are some cases where cities have mixed special characters to find
        //so 
        for (int i = 0; i < US.length; i++){
            USList.add(US[i][1].toUpperCase());
        }
        for (int i = 0; i < CAD.length; i++){
            CADList.add(CAD[i][0].toUpperCase());
        }

        //setting up to write to the DangerRatingOutput.txt file
        OutputStream output = new FileOutputStream("src/DangerRatingOutput.txt");
        PrintStream printstream = new PrintStream(output);
        
        //Iterate through all the hashmaps and if there is a city that exist in the US, get the coordinates of lattidue and longitude and output those with the cities danger rating. If it isn't in the US
        //also check to see if its in list belonging to cities in Canada
        for (Map.Entry mapElement : listOfAllCitiesHash.entrySet()) { 
            String key = (String)mapElement.getKey(); 
            String currentMonth = key.substring(key.indexOf("-")+1); 
            String currentCity = key.substring(0,key.indexOf("-")); 
           
            double currentDangerRating = listOfAllCitiesHash.get(key);
            
            if (USList.indexOf(currentCity) >= 0 && currentMonth.equals(month)){
               
               //Latitude col of US is index 8, Longitude col of us is index 9
                printstream.print(US[USList.indexOf(currentCity)][8] + " " + US[USList.indexOf(currentCity)][9] + " " + currentDangerRating+"\n");
            }else if (CADList.indexOf(currentCity)  >= 0 && currentMonth.equals(month)){

            	 //Latitude col of CAD is index 1, Longitude CAD of us is index 2
                printstream.print(CAD[CADList.indexOf(currentCity)][1] + " " + CAD[CADList.indexOf(currentCity)][2] + " " + currentDangerRating+"\n");

            }
     
        } 


    }
   

       public static void main(String[] args) throws IOException{
        System.out.println("/");
        //writeDangerRateTxt();
        loadAllDangerRating();
        writeDangerRateTxt("1"); 
        //multiplyDangerRatingWithProb();
        listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println(listOfAllCitiesHash.size());
    }
}
