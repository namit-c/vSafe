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


public class DangerRating{
   private static  HashMap<String,Double > injuryDict = new HashMap<String, Double>();
   private static HashMap<String,Double> deathDict = new HashMap<String,Double>();
   private static  HashMap<String,Double > damageDict = new HashMap<String, Double>();
   private static Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
   private static HashMap<String,Double> earthquakeDict = new HashMap<String,Double>();

   
   private static HashMap<String,Double> listOfAllCitiesHash = new HashMap<String,Double>();

  
   //Calculating the average magintude per city from 2019 to 1985
   public static void findEarthQuakeData(int magIndex, int cityCol, int dateIndex, String[][] dataSet){ 
       //int monthCol = 0; //in the 0 col for earthquake csv file
       for (int z = 0; z < dataSet.length; z++){
            try{
                //System.out.println(dataSet[z][cityCol]);
                //
                //the dates in eqarchives are listed as yyyy-mm-dd ; from index 5 to 6, will give the month value
                if (dataSet[z][cityCol].contains("from") ){ 
                    //usually each city will have "DISTANCE km from CITYNAME", so below will be taking the substring to obtain the cityName
                    String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase(); 
                    earthquakeDict.put(cityName + "-" + dataSet[z][dateIndex].substring(5,7), 0.0); 
                    listOfAllCitiesHash.put(cityName + "-" + dataSet[z][dateIndex].substring(5,7) ,0.0);
                }
            }catch(StringIndexOutOfBoundsException e){
            }
        }

        for (int z = 0; z < dataSet.length; z++){
            try{
                String cityName = dataSet[z][cityCol].substring(dataSet[z][cityCol].indexOf("f") + 5).toUpperCase()+"-"+dataSet[z][dateIndex].substring(5,7);  
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
   public static void findDangerStatsOfDataSets(int cityIndex, int eventIndex, int injuryIndex, int deathIndex, int damageIndex, int dateIndex, String[][][] dataSet ){
        //Set<String> city_set = new HashSet<String>(); //city_set contains all unique cities in the csv file
       // int monthCol = 0;
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
                    injuryDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
                    deathDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
                    damageDict.put(dataSet[z][i][cityIndex].toUpperCase()+"-"+dataSet[z][i][eventIndex], 0.0);
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
                        
                        int gettingMonthEndIndex = dataSet[z][i][dateIndex].indexOf("/");
                        int currentMonth = 0;
                        try{
                            currentMonth = Integer.parseInt(dataSet[z][i][dateIndex].substring(0, gettingMonthEndIndex));
                            
                        }catch(StringIndexOutOfBoundsException e){
                            //ontinue;
                        }catch(NumberFormatException e){
                            //continue;
                        }

                        String tempStr = dataSet[z][index][cityIndex].toUpperCase()+"-"+currentMonth+"-"+dataSet[z][index][eventIndex]; //string would be "SOUTH CAROLINA-Heavy Rain"
                        
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
                    }else{
                        break;
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

    private static void determineDangerRatingEarthquake() throws IOException{
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

            String city = key.substring(0,key.indexOf("-")); 
            String monthNum = key.substring(key.indexOf("-")+1);
            if (earthquakeProb.containsKey(city + " " + monthNum + " " + "Earthquake")){
                currentIndexRating *= earthquakeProb.get(city + " " + monthNum + " " + "Earthquake")/100;
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

    public static void determineDangerRatingStormRelated(HashMap<String, Double> stormData) throws IOException{
         int index = 0;
         Iterator<Map.Entry<String, Double>> injury = injuryDict.entrySet().iterator(); 
         Iterator<Map.Entry<String, Double>> death = deathDict.entrySet().iterator();
         Iterator<Map.Entry<String, Double>> damage = damageDict.entrySet().iterator();
         
         
         HashMap<String,Double> stormDataProb = stormData;
         //HashMap<String, Double> earthquakeProb = vProb.probEq();
         
         HashMap<String, Double> canadaNaturalDis = vProb.probCDD();

         while(injury.hasNext() && death.hasNext() && damage.hasNext()){
            //Iterator<Map.Entry<String, String>> index = injuryDict.entrySet().iterator();
            Map.Entry<String, Double> entryInj = injury.next();
            Map.Entry<String, Double> entryDeat = death.next();
            Map.Entry<String, Double> entryDamg = damage.next();
            
            String key = (String)entryInj.getKey();
            int firstDashIndex = ((String)entryInj.getKey()).indexOf("-");
            int secondDashIndex = ((String)entryInj.getKey()).indexOf("-", firstDashIndex + 1); 
             String currentEvent = (String)entryInj.getKey().substring(secondDashIndex + 1);
            String currentCity = (String)entryInj.getKey().substring(0,((String)entryInj.getKey()).indexOf("-") );
            String month = "";
            if (firstDashIndex >= 0 && secondDashIndex >= 0){
                month = key.substring(firstDashIndex + 1, secondDashIndex);
            }
             
            //System.out.println(listOfAllCitiesHash.get(key.substring(0,secondDashIndex)));
            double cityIndexVal = 0.0; 
            try{
                cityIndexVal = listOfAllCitiesHash.get(key.substring(0,secondDashIndex)) ;

            }catch(NullPointerException e){
            }
            catch(StringIndexOutOfBoundsException e){
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
            try{
                listOfAllCitiesHash.put(key.substring(0,secondDashIndex),cityIndexVal);
            }catch(StringIndexOutOfBoundsException e){
            }

         }

        //listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));  
    }

    public static HashMap<String, Double> loadAllDangerRating() throws IOException{
        HashMap<String, Double> stormDataProb = new HashMap<String, Double>(); 
        int cityCol = 15;
        int eventCol = 12;
        int injuryCol = 20;
        int deathCol = 22;
        int damageCol = 24;
        int dateCol = 17;
        String[][] data1 = ReadCSV.readFile("../Data_Sets/stormdata_2013.csv",15,19);
        String[][][] loadData0 = {data1};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData0);
        vProb.probSD(loadData0,stormDataProb);

        data1 = null;
        loadData0 = null;

        String[][] data2 = ReadCSV.readFile("../Data_Sets/stormdata_2012.csv",15,19);
        String[][][] loadData1 = {data2};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData1);
        vProb.probSD(loadData1,stormDataProb);

         data2 = null;
        loadData1 = null;

       
        String[][] data3 = ReadCSV.readFile("../Data_Sets/stormdata_2011.csv",15,19);
        String[][][] loadData2 = {data3};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData2);
        vProb.probSD(loadData2,stormDataProb);

         data3 = null;
        loadData2 = null;

        String[][] data4 = ReadCSV.readFile("../Data_Sets/stormdata_2010.csv",15,19);
        String[][][] loadData3 = {data4};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData3);
        vProb.probSD(loadData3,stormDataProb);

         data4 = null;
        loadData3 = null;
        
        //String[][][] allDataSets = {data1,data2,data3,data4};
        
      
        System.gc(); //calling system gargbage collector *Doesnt always free memory
                     //https://stackoverflow.com/questions/1567979/how-to-free-memory-in-java
        String[][]data5 = ReadCSV.readFile("../Data_Sets/stormdata_2009.csv",15,19);
        String[][][] loadData4 = {data5};
          findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData4);
        vProb.probSD(loadData4,stormDataProb);

         data5 = null;
        loadData4 = null;
        
        String[][] data6 = ReadCSV.readFile("../Data_Sets/stormdata_2008.csv",15,19);
        String[][][] loadData5 = {data6};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData5);
        vProb.probSD(loadData5,stormDataProb);

         data6 = null;
        loadData5 = null;

        String[][] data7 = ReadCSV.readFile("../Data_Sets/stormdata_2007.csv",15,19);
        String[][][] loadData6 = {data7};
        findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData6);
        vProb.probSD(loadData6,stormDataProb);

         data7 = null;
        loadData6 = null;

        String[][] data8 = ReadCSV.readFile("../Data_Sets/stormdata_2006.csv",15,19);
        String[][][] loadData7 = {data8};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData7);
        vProb.probSD(loadData7,stormDataProb);

         data8 = null;
        loadData7 = null;

        
        
        String[][] data9 = ReadCSV.readFile("../Data_Sets/stormdata_2005.csv",15,19);
        String[][][] loadData8 = {data9};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData8);
        vProb.probSD(loadData8,stormDataProb);

         data9 = null;
        loadData8 = null;

        String[][] data10 = ReadCSV.readFile("../Data_Sets/stormdata_2004.csv",15,19);
        String[][][] loadData9 = {data10};
          findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData9);
        vProb.probSD(loadData9,stormDataProb);

         data10 = null;
        loadData9 = null;

        String[][] data11 = ReadCSV.readFile("../Data_Sets/stormdata_2003.csv",15,19);
        String[][][] loadData10 = {data11};
         findDangerStatsOfDataSets(cityCol,eventCol,injuryCol,deathCol,damageCol,dateCol,loadData10);
        vProb.probSD(loadData10,stormDataProb);

         data11 = null;
        loadData10 = null;
       
  
        
        determineDangerRatingStormRelated( stormDataProb);
        

         
        String[][] data12 = ReadCSV.readFile("../Data_Sets/eqarchive-en.csv",1,6);
        findEarthQuakeData(4,6,0,data12); 
        
        data12 = null;
        
        determineDangerRatingEarthquake();
        
        String[][] data13 = ReadCSV.readFile("../Data_Sets/CDD_csv.csv",4,1);
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
    public static void writeDangerRateTxt(String month) throws IOException{
        String[][] US = ReadCSV.readFile("../Data_Sets/uscities.csv",1,1);

        String[][] CAD = ReadCSV.readFile("../Data_Sets/Canada_Cities.csv",0,0);

            

        ArrayList<String> USList = new ArrayList<String>();
        ArrayList<String> CADList = new ArrayList<String>();

        for (int i = 0; i < US.length; i++){
            USList.add(US[i][1].toUpperCase());
        }
        for (int i = 0; i < CAD.length; i++){
            CADList.add(CAD[i][0].toUpperCase());
        }

        
       OutputStream output = new FileOutputStream("DangerRatingOutput.txt");
        PrintStream printstream = new PrintStream(output);
        for (Map.Entry mapElement : listOfAllCitiesHash.entrySet()) { 
            String key = (String)mapElement.getKey(); 
            String currentMonth = key.substring(key.indexOf("-")+1); 
            key = key.substring(0,key.indexOf("-")); 
           
            if (USList.indexOf(key) >= 0 && currentMonth.equals(month)){
                double currentDangerRating = 0.0;
                if (listOfAllCitiesHash.get(key) != null){
                   currentDangerRating = listOfAllCitiesHash.get(key);    
                }
                printstream.print(US[USList.indexOf(key)][8] + " " + US[USList.indexOf(key)][9] + " " + currentDangerRating+"\n");
            }else if (USList.indexOf(key)  >= 0 && currentMonth.equals(month)){

                double currentDangerRating = 0.0;
                if (listOfAllCitiesHash.get(key) != null){
                   currentDangerRating = listOfAllCitiesHash.get(key);    
                }
                printstream.print(CAD[CADList.indexOf(key)][1] + " " + CAD[CADList.indexOf(key)][2] + " " + currentDangerRating+"\n");

            }
            //int value = ((int)mapElement.getValue() + 10);  
            //System.out.println(key + " : " + value); 
        } 

       

        //printstream.close();
        //output.close();

    }
   

       public static void main(String[] args) throws IOException{
        System.out.println("/");
        //writeDangerRateTxt();
        loadAllDangerRating();
        writeDangerRateTxt("06"); 
        //multiplyDangerRatingWithProb();
        listOfAllCitiesHash.forEach((key, value) -> System.out.println(key + ": " + value));
        System.out.println(listOfAllCitiesHash.size());
    }
}
