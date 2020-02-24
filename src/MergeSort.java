import java.util.Arrays;
public class MergeSort{
    
    /*
     * From Algorithms 4th Edition Textbook by Robert Sedgewick and Kevin Wayne 
     * on [Pg 271 - 273]
    */

    private static String[][] aux;
    public static void sort(String[][] a,int index){
        aux = new String[a.length][a[0].length];
        sort(a,0,a.length -1,index);
    }
    
    private static void merge(String[][] a, int low, int mid, int hi,int index){
        int i = low;
        int j = mid + 1;
        for (int k = low; k <= hi; k++){
            aux[k][index] = a[k][index];
        }
        for (int k = low; k <= hi; k++){
            if (i > mid){
                a[k][index] = aux[j++][index];
            }
            else if (j > hi){
                a[k][index] = aux[i++][index];
            }
            else if (aux[j][index].compareTo(aux[i][index]) < 0){
                a[k][index] = aux[j++][index];
            }
            else{
                a[k][index] = aux[i++][index];
            }
        }
    }
    private static void sort(String[][] a, int low, int hi,int index){
        if (hi <= low){
            return;
        }
        int mid = low + (hi - low)/2;
        sort(a,low,mid,index);
        sort(a,mid+1,hi,index);
        merge(a,low,mid,hi,index);
    }
    
    public static void main(String[] args){
       String[] unsortedArr = {"E","Z","F","A","B","D"};
       //sort(unsortedArr);
       //System.out.println(Arrays.toString(unsortedArr));

    }

}
