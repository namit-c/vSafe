import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class Digraph {

    private final int V;
    protected int E;
    private Hashtable<String, ArrayList<String>> adj;

    public Digraph(int V) {
        this.V = V; // Number of vertices
        this.E = 0; // Number of edges
        adj = new Hashtable<String, ArrayList<String>>(); // Adjacency list
        
    }

    public Digraph(int V, Hashtable<String, ArrayList<String>> connectedCities) {
        this(V);
        Enumeration keys = connectedCities.keys();
        String key = new String();
        
        for(int i = 0; keys.hasMoreElements(); i++) { // Add each edge to graph
        	key = (String)keys.nextElement();
        	key = key.toUpperCase();
        
        	for(int o = 0; o < connectedCities.get(key).size(); o++) {
        		String v = key; // First vertex
            	String w = connectedCities.get(key).get(o); // Second vertex
            	addEdge(v, w.toUpperCase());
        	}
        }
       
    }

    public int V() {
        return V;
    }

    public int E() {
        return E;
    }

    public void addEdge(String v, String w) {
    	ArrayList<String> neighbours = new ArrayList<String>(); // Update neighbours of v
    	if (adj.get(v) != null) {
    		neighbours.addAll(adj.get(v));
    	}
    	neighbours.add(w);
    	adj.put(v, neighbours);

    	neighbours = new ArrayList<String>(); // Update neighbours of w
    	if (adj.get(w) != null) {
    		neighbours.addAll(adj.get(w));
    	}
    	adj.put(w, neighbours);
    	
        E++; // Increment number of edges by 1
    }

    public Iterable<String> adj(String v) {
        return adj.get(v); // Return the list of neighbours of vertex v
    }
    
    public String toString() {
    	Enumeration city = adj.keys();
    	String output;
    	String s = V + " vertices, " + E + "edges\n";
    	for (int v = 0; v < V && city.hasMoreElements(); v++) {
    		output = (String)city.nextElement();
    		
    		
    		s += output + ": ";
    		for (String w : this.adj(output)) {
    			s += w + " ";

    		}
    		s += "\n";
    	}
    	return s;
    }
}