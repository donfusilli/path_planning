/**
 * NOTHING FOR YOU TO DO HERE.
 *
 * A boolean rectangular map.
 *
 */
public class Map {
	/** Width. */
	private int width;
	/** Height. */
	private int height;
	/** Boolean map. */
	private boolean[][] obstacle;
	
	public int getN() {
		return width*height;
	}
	
	public Map(int w, int h) {
		if (w <= 0 || h <= 0)
			throw new IllegalArgumentException();
		width = w;
		height = h;
		obstacle = new boolean[h][w];
	}
	
	public Map(int w, int h, boolean[][] o) {
		if (w <= 0 || h <= 0)
			throw new IllegalArgumentException();
		if (o.length != h)
			throw new IllegalArgumentException();
		for (int i=0; i<h; i++) {
			if (o[i].length != w)
				throw new IllegalArgumentException();
		}
		
		width = w;
		height = h;
		obstacle = o;
	}
	
	public void reset() {
		for (int i=0; i<height; i++)
			for (int j=0; j<width; j++)
				obstacle[i][j] = false;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean getObstacle(int i, int j) {
		if (i < 0 || i >= height || j < 0 || j >= width)
			throw new IllegalArgumentException();
		
		return obstacle[i][j];
	}

	public void setObstacle(int i, int j, boolean o) {
		if (i < 0 || i >= height || j < 0 || j >= width)
			throw new IllegalArgumentException();
		
		obstacle[i][j] = o;
	}
	
	/**
	 * 
	 * This method defines a bijection from {0,...,height-1} x {0,...,width-1} to {0,...,height*width-1}.
	 * 
	 * @param i Row index.
	 * @param j Column index.
	 * @return (i*width + j)
	 */
	public int getIndex(int i, int j) {
		return i*width + j;
	}

	/**
	 * 
	 * Calculates heuristic distances and updates the graph accordingly.
	 * 
	 * @param graph A graph.
	 */
	private void setHeurDist(DigraphW graph) {

		// Calculate heuristic distances.
		for (int i1=0; i1<height; i1++) {
			for (int j1=0; j1<width; j1++) {
				for (int i2=0; i2<height; i2++) {
					for (int j2=0; j2<width; j2++) {
						int u = getIndex(i1,j1);
						int v = getIndex(i2,j2);
						int di = Math.abs(i1-i2);
						int dj = Math.abs(j1-j2);
						
						// Tighter than straight-line heuristic:
						/*
						double heurD = -1;
						if (di >= dj) {
							heurD = dj*Math.sqrt(2) + di-dj;
						}
						else { // dj > di
							heurD = di*Math.sqrt(2) + dj-di;
						}
						graph.setHeurDist(u,v,heurD);
						*/
						
						// Straight-line heuristic:
						graph.setHeurDist(u,v,Math.sqrt(di*di+dj*dj));
					}
				}
			}
		}
		
	}
	

	/**
	 * 
	 * Movement allowed horizontally, vertically, and diagonally.
	 *
	 * @return Graph generated from map.
	 */
	public DigraphW getGraph() {
		int n = width*height;
		
		DigraphW graph = new DigraphW(n);
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (obstacle[i][j]) continue;
				int u = getIndex(i,j);
				
				if (j-1 >= 0 && !obstacle[i][j-1]) { // left
					int v = getIndex(i,j-1);
					graph.addEdge(u,v,1.0);
				}
				if (j+1 < width && !obstacle[i][j+1]) { // right
					int v = getIndex(i,j+1);
					graph.addEdge(u,v,1.0);
				}
				if (i-1 >= 0 && !obstacle[i-1][j]) { // up
					int v = getIndex(i-1,j);
					graph.addEdge(u,v,1.0);
				}
				if (i+1 < height && !obstacle[i+1][j]) { // down
					int v = getIndex(i+1,j);
					graph.addEdge(u,v,1.0);
				}

				double sqrt2 = Math.sqrt(2);
				if (j-1 >= 0 && i-1 >= 0 && !obstacle[i-1][j-1] && !obstacle[i][j-1] && !obstacle[i-1][j]) { // up-left
					int v = getIndex(i-1,j-1);
					graph.addEdge(u,v,sqrt2);
				}
				if (j+1 < width && i-1 >= 0 && !obstacle[i-1][j+1] && !obstacle[i][j+1] && !obstacle[i-1][j]) { // up-right
					int v = getIndex(i-1,j+1);
					graph.addEdge(u,v,sqrt2);
				}
				if (j-1 >= 0 && i+1 < height && !obstacle[i+1][j-1] && !obstacle[i][j-1] && !obstacle[i+1][j]) { // down-left
					int v = getIndex(i+1,j-1);
					graph.addEdge(u,v,sqrt2);
				}
				if (j+1 < width && i+1 < height && !obstacle[i+1][j+1] && !obstacle[i][j+1] && !obstacle[i+1][j]) { // down-right
					int v = getIndex(i+1,j+1);
					graph.addEdge(u,v,sqrt2);
				}

			}
		}
		
		// Set heuristic distances.
		setHeurDist(graph);
		
		// Sanity check for graph.
		/*
		if (graph.hasParallelEdges()) {
			System.err.println("getGraph: Graph has parallel edges.");
			System.exit(-1);
		}
		if (!graph.isSymmetric()) {
			System.err.println("getGraph: Graph is not symmetric.");
			System.exit(-1);
		}
		*/
		
		return graph;
	}

	// Movement allowed only horizontally and vertically.
	public DigraphW getGraphHV() {
		int n = width*height;
		
		DigraphW graph = new DigraphW(n);
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				if (obstacle[i][j]) continue;
				int u = getIndex(i,j);
				
				if (j-1 >= 0 && !obstacle[i][j-1]) { // left
					int v = getIndex(i,j-1);
					graph.addEdge(u,v,1.0);
				}
				if (j+1 < width && !obstacle[i][j+1]) { // right
					int v = getIndex(i,j+1);
					graph.addEdge(u,v,1.0);
				}
				if (i-1 >= 0 && !obstacle[i-1][j]) { // up
					int v = getIndex(i-1,j);
					graph.addEdge(u,v,1.0);
				}
				if (i+1 < height && !obstacle[i+1][j]) { // down
					int v = getIndex(i+1,j);
					graph.addEdge(u,v,1.0);
				}
			}
		}
		
		// Calculate heuristic distances.
		for (int i1=0; i1<height; i1++) {
			for (int j1=0; j1<width; j1++) {
				for (int i2=0; i2<height; i2++) {
					for (int j2=0; j2<width; j2++) {
						int u = getIndex(i1,j1);
						int v = getIndex(i2,j2);
						// Manhattan distance.
						int heurD = Math.abs(i1-i2) + Math.abs(j1-j2);
						graph.setHeurDist(u,v,heurD);
					}
				}
			}
		}
		
		return graph;
	}
	
	public String toString() {
		String str = "map: width = " + width + ", height = " + height + "\n";
		for (int i=0; i<height; i++) {
			String line = "";
			for (int j=0; j<width; j++) {
				line += obstacle[i][j] ? "X" : "_";
			}
			str += line + "\n";
		}
		
		return str;
	}
	
}
