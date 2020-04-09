import java.util.Hashtable;

public class BreadthFirstPaths {
	private Hashtable<String, Boolean> marked;
	private Hashtable<String, String> edgeTo;
	private final String s;
	
	public BreadthFirstPaths(Digraph G, String s) {
		marked = new Hashtable<String, Boolean>(); // Hashtable where value at city is true if already visited
		edgeTo = new Hashtable<String, String>();
		this.s = s;
		bfs(G, s);
	}
	
	private void bfs(Digraph G, String s) {
		Queue<String> queue = new Queue<String>();
		marked.put(s, true);
		queue.enqueue(s);
		while(!queue.isEmpty()) {
			String v = queue.dequeue();
			for (String w : G.adj(v)) {
				if (marked.get(w) == null) {
					edgeTo.put(w, v);
					marked.put(w, true);
					queue.enqueue(w);
				}
			}
		}
	}
	
	public boolean hasPathTo(String v) {
		if (marked.get(v) == null) {
			return false;
		}
		return marked.get(v);
	}
	
	public Iterable<String> pathTo(String v) {
		if (!hasPathTo(v)) {
			return null;
		}
		Stack<String> path = new Stack<String>();
		for (String x = v; !x.equals(s); x = edgeTo.get(x)) {
			path.push(x);
		}
		path.push(s);
		return path;
	}
}
