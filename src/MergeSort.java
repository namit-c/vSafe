import java.io.IOException;
import java.util.Arrays;
public class MergeSort{
    
    /*
     * From Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne 
     * on [Pg 271 - 273]
    */

    private static String[][] aux;
    
    /**
 	 * @brief sort the 2D string array in lexiographic order by sorting in a specific column of the array such that it wil sort the rows based on this comparison
 	 * @param a is the 2D string array to be sorted
 	 * @param index is the index to compare elements in a specificed column 
 	 */
    public static void sort(String[][] a,int index){
        aux = new String[a.length][a[0].length];
        sort(a,0,a.length -1,index);
    }
    
    /**
 	 * @brief merge the current array by using the aux array to make a current copy of the merged array to merge elements back in order 
 	 * @param a 2D string array
 	 * @param low representing the pointer to beginning of the array
 	 * @param mid representing the pointer to middle of the array
 	 * @param hi representing the pointer to the end of the array
 	 * @param index representing the column to specifically compare elements by
 	 */
    private static void merge(String[][] a, int low, int mid, int hi,int index){
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= hi; k++){
            aux[k] = a[k].clone();
        }
        for (int k = low; k <= hi; k++){
            if (i > mid){
                //a[k][index] = aux[j++][index];
                //insertRow(a,aux,k,j);
                a[k] = aux[j].clone();
                j++;
            }
            else if (j > hi){
                //a[k][index] = aux[i++][index];
                //insertRow(a,aux,k,i);
                a[k] = aux[i].clone();
                i++;
            }
            else if (aux[j][index].compareTo(aux[i][index]) < 0){
                //a[k][index] = aux[j++][index];
                //insertRow(a,aux,k,j);
                a[k] = aux[j].clone();
                j++;
            }
            else{
                //a[k][index] = aux[i++][index];
                //insertRow(a,aux,k,i);
                a[k] = aux[i].clone();
                i+=1;
            }
        }

    }

    /*
    //The following does a shallow copy of the contents of row in the second matrix and stores it in the firstMatrix
    //Previously in the merge function, if a[k] = aux[j++] for example, this would lose pointers to the previous rows and be lossed
    //lossed in memory. This solution prevents it from happening, however this heavily impacts performance.
    //
    //Need to find an efficient way to clone rows.
    private static void insertRow(String[][] firstMatrix, String[][] secondMatrix,int firstRow, int secondRow){
        for (int i = 0; i < secondMatrix[secondRow].length; i++){
            String temp = new String(secondMatrix[secondRow][i]);
            firstMatrix[firstRow][i] = temp;
        }
    }
    */
    
    /**
 	 * @brief Recursively call back until you have subarrays of size 1, then merge those subarrays in order
	 * @param a 2D string array
 	 * @param low representing the pointer to beginning of the array
 	 * @param hi representing the pointer to the end of the array
 	 * @param index representing the column to specifically compare elements by
 	 */
    private static void sort(String[][] a, int low, int hi,int index){
        if (hi <= low){
            return;
        }
        int mid = low + (hi - low)/2;
        sort(a,low,mid,index);
        sort(a,mid+1,hi,index);
        merge(a,low,mid,hi,index);
    }
 

}
