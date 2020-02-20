import java.util.Arrays;
public class MergeSort{
    
    /*
     * From Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne 
     * on [Pg 271 - 273]
    */

    private static String[] aux;
    public static void sort(String[] a){
        aux = new String[a.length];
        sort(a,0,a.length -1);
    }
    
    private static void merge(String[] a, int low, int mid, int hi){
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= hi; k++){
            aux[k] = a[k];
        }
        for (int k = low; k <= hi; k++){
            if (i > mid){
                a[k] = aux[j++];
            }
            else if (j > hi){
                a[k] = aux[i++];
            }
            else if (aux[j].compareTo(aux[i]) < 0){
                a[k] = aux[j++];
            }
            else{
                a[k] = aux[i++];
            }
        }
    }
    private static void sort(String[] a, int low, int hi){
        if (hi <= low){
            return;
        }
        int mid = low + (hi - low)/2;
        sort(a,low,mid);
        sort(a,mid+1,hi);
        merge(a,low,mid,hi);
    }
    
    public static void main(String[] args){
       String[] unsortedArr = {"E","Z","F","A","B","D"};
       sort(unsortedArr);
       System.out.println(Arrays.toString(unsortedArr));

    }

}
