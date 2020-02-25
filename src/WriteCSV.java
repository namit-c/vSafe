public class WriteCSV{
    public static void writeToCSV(String file){
    	 FileWriter csvWriter = new FileWriter("new1.csv");                
		 for (int i = 0; i < data.length; i++){                                
			for (int j = 0; j < data[0].length; j++){                             
				csvWriter.append(data[i][j]);                                 
			}                                                                 
			csvWriter.append("\n");                                       
		}                                                                 
		csvWriter.flush();                                                
		csvWriter.close();                                               
    }
