import java.util.Arrays;
import java.util.ArrayList;
public class BinarySearch{
	 /*
     * From Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne 
     * on [Pg 9]
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

