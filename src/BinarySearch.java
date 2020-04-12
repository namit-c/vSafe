import java.util.Arrays;
import java.io.IOException;
import java.util.ArrayList;
public class BinarySearch{
	 /*
     * From Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne 
     * on [Pg 9]
    */

	
	/**
	  * @brief using binary search algorithm on a 2D string array, to find a specific string in a given column
	  * @param arr is the 2D string array
	  * @param str is the string to be searched for 
	  * @param indexCol is the specific column to be searhcing through
	  * @return integer of the current index if its found, otherwise return -1 if the provided string doesnt exist
	  */
    public static int binarySearch(String[][] arr, String str, int indexCol){
        int low = 0;
        int hi = arr.length -1;
        while (low <= hi){
            int mid = low + (hi-low)/2;
            if (str.compareTo(arr[mid][indexCol]) < 0){
                hi = mid - 1;
            }
            else if (str.compareTo(arr[mid][indexCol]) > 0){
                low = mid +1;
            }
            else{
                return mid;
            }
        }

        return -1;
    }

    /**
	  * @brief using binary search algorithm on a ArrayList with each element in the ArrayList being a 1D string array, to find a specific string in a given column
	  * @param arr is the ArrayList with each element in the ArrayList being a 1D string array
	  * @param str is the string to be searched for 
	  * @param indexCol is the specific column to be searhcing through
	  * @return integer of the current index if its found, otherwise return -1 if the provided string doesnt exist
	  */
    public static int binarySearch(ArrayList<String[]> arr, String str, int indexCol){
        int low = 0;
        int hi = arr.size() -1;
        while (low <= hi){
            int mid = low + (hi-low)/2;
            if (str.compareTo(arr.get(mid)[indexCol]) < 0){
                hi = mid - 1;
            }
            else if (str.compareTo(arr.get(mid)[indexCol]) > 0){
                low = mid +1;
            }
            else{
                return mid;
            }
        }

        return -1;
    }
    public static void main(String[] args){
       //System.out.println("HELLO WORLD");
       
       //String[] arr = {"ABC","DEF","GEF","Z"};
       //System.out.println(binarySeach(arr,"GEF"));
       //System.out.println(binarySeach(arr,"ABCD"));
      
    } 
}

