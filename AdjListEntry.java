/**
 *
 * NOTHING FOR YOU TO DO HERE.
 * 
 * Node of the adjacency list for a weighted digraph.
 *
 */
public class AdjListEntry {
	/** Target vertex. */
	public int vtx;
	/** Non-negative weight of edge. */
	public double weight;
	/** Next entry. **/
	public AdjListEntry next;

	/**
	 * 
	 * @param n Number of vertices in the graph.
	 * @param v Target vertex index.
	 * @param w Weight of edge.
	 */
	public AdjListEntry(int n, int v, double w) {
		if (n < 0 || v < 0 || v >= n || w < 0)
			throw new IllegalArgumentException();
		
		vtx = v;
		weight = w;
	}
	
	public String toString() {
		return vtx + ":" + weight;
	}
	
}
