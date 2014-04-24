import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.geom.*;

/**
 * NOTHING FOR YOU TO DO HERE.
 *
 * This class represents the map panel.
 *
 */
public class MapPanel extends JPanel implements MouseListener, MouseMotionListener  {
	private final Color backColor = new Color(208,208,208);
	private final Color sepColor = new Color(64,64,64);
	private final Color wallColor = Color.BLACK;
	private final Color sourceColor = new Color(0,128,0);
	private final Color destColor = new Color(160,0,0);
	private final Color visitedColor = new Color(128,128,255);
	private final Color pathColor = new Color(192,192,64);

	private final int cellSize = 20;
	private final int sepSize = 1;	

	/** The width of the panel (in pixels). */
	private int width;
	/** The height of the panel (in pixels). */
	private int height;
	/** The map. */
	private Map map;

	private GUI window;
	private Operation op;

	// Source
	private boolean sourceSet;
	private int iSource;
	private int jSource;
	// Destination

	private boolean destSet;
	private int iDest;
	private int jDest;

	// Mouse position
	private Point pMouse;
	private Point pMousePrev;
	// Position of mouse in grid
	private int iMouse;
	private int jMouse;


	private Map visitedMap;
	private Map pathMap;

	// Animation
	private Timer timer;
	private boolean animation;
	private final int animDelay = 5; // milliseconds
	private ArrayList<Integer> animVisited;
	private int indexVisited;
	private ArrayList<Integer> animPath;
	private int indexPath;

	public MapPanel(GUI window, int mapWidth, int mapHeight) {
		this.window = window;

		map = new Map(mapWidth,mapHeight);
		visitedMap = new Map(mapWidth,mapHeight);
		pathMap = new Map(mapWidth,mapHeight);

		width = mapWidth*cellSize + (mapWidth+1)*sepSize;
		height = mapHeight*cellSize + (mapHeight+1)*sepSize;

		sourceSet = true;
		iSource = mapHeight-1;
		jSource = 0;

		destSet = true;
		iDest = 0;
		jDest = mapWidth-1;

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void reset() {
		System.out.println("MapPanel: reset");

		sourceSet = false;
		destSet = false;
		map.reset();
		visitedMap.reset();
		pathMap.reset();

		animation = false;

		repaint();
	}

	public void setOp(Operation newOp) {
		op = newOp;
	}

	public void startAnimation(ArrayList<Integer> visited, ArrayList<Integer> path) {
		animVisited = visited;
		indexVisited = 0;
		animPath = path;
		indexPath = 0;
		animation = true;

		ActionListener taskPerformer = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//System.out.println("tick");

				if (!animation) return;
				if (indexVisited < animVisited.size()) {
					int vtx = animVisited.get(indexVisited);
					indexVisited++;
					int mapWidth = map.getWidth();
					int i = vtx / mapWidth;
					int j = vtx % mapWidth;
					visitedMap.setObstacle(i,j,true);
					repaint();
					return;
				}
				timer.setDelay(2*animDelay); // display path slower
				if (indexPath < animPath.size()) {
					int vtx = animPath.get(indexPath);
					indexPath++;
					int mapWidth = map.getWidth();
					int i = vtx / mapWidth;
					int j = vtx % mapWidth;
					pathMap.setObstacle(i,j,true);
					repaint();
					return;
				}
				animation = false;
				timer.stop();
			}
		};
		timer = new Timer(animDelay,taskPerformer);
		timer.start();
	}

	public void runDijkstra() {
		window.setMsg("Run Dijkstra.");

		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		//System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		//System.out.println(graph);
		int source = map.getIndex(iSource,jSource);
		int dest = map.getIndex(iDest,jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = - System.currentTimeMillis();
		double distance = graph.shortestPath(source,dest,visited,path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance+", time="+t0+"ms");

		startAnimation(visited,path);
	}

	public void runAStar() {
		window.setMsg("Run A Star.");
		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		//System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		//System.out.println(graph);
		int source = map.getIndex(iSource,jSource);
		int dest = map.getIndex(iDest,jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = - System.currentTimeMillis();
		double distance = graph.shortestPathHeur(source,dest,visited,path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance+", time="+t0+"ms");

		startAnimation(visited,path);

	}

	public void runDFS() {
		window.setMsg("Run DFS");

		if (!sourceSet) {
			window.setMsg("Source vertex not set.");
			return;
		}
		if (!destSet) {
			window.setMsg("Destination vertex not set.");
			return;
		}

		//System.out.println(map);
		visitedMap.reset();
		pathMap.reset();

		DigraphW graph = map.getGraph();
		//System.out.println(graph);
		int source = map.getIndex(iSource,jSource);
		int dest = map.getIndex(iDest,jDest);

		ArrayList<Integer> visited = new ArrayList<Integer>();
		ArrayList<Integer> path = new ArrayList<Integer>();
		long t0 = - System.currentTimeMillis();
		double distance = graph.DFS(source,dest,visited,path);
		t0 += System.currentTimeMillis();
		window.setMsg("distance = " + distance+", time="+t0+"ms");

		startAnimation(visited,path);

	}



	public Dimension getPreferredSize() {
		return new Dimension(width,height);
	}

	public void paintComponent(Graphics g) {
		//System.out.println("Paint map panel.");

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Paint with background color.
		g2d.setColor(backColor);
		g2d.fillRect(0,0,width,height);

		// Draw the lines.
		g2d.setColor(sepColor);
		// Draw the horizontal lines.
		for (int y=0, i=0; i<=map.getHeight(); i++) {
			g2d.fillRect(0,y,width,sepSize);
			y += sepSize + cellSize;
		}
		// Draw the vertical lines.
		for (int x=0, i=0; i<=map.getWidth(); i++) {
			g2d.fillRect(x,0,sepSize,height);
			x += sepSize + cellSize;
		}

		// Paint visited
		g2d.setColor(visitedColor);
		for (int i=0; i<visitedMap.getHeight(); i++) {
			for (int j=0; j<visitedMap.getWidth(); j++) {
				if (visitedMap.getObstacle(i,j)) {
					int x = j*cellSize + (j+1)*sepSize;
					int y = i*cellSize + (i+1)*sepSize;
					g2d.fillRect(x,y,cellSize,cellSize);
				}
			}
		}

		// Paint path
		g2d.setColor(pathColor);
		for (int i=0; i<pathMap.getHeight(); i++) {
			for (int j=0; j<pathMap.getWidth(); j++) {
				if (pathMap.getObstacle(i,j)) {
					int x = j*cellSize + (j+1)*sepSize;
					int y = i*cellSize + (i+1)*sepSize;
					g2d.fillRect(x,y,cellSize,cellSize);
				}
			}
		}

		// Paint obstacles
		g2d.setColor(wallColor);
		for (int i=0; i<map.getHeight(); i++) {
			for (int j=0; j<map.getWidth(); j++) {
				if (map.getObstacle(i,j)) {
					int x = j*cellSize + (j+1)*sepSize;
					int y = i*cellSize + (i+1)*sepSize;
					g2d.fillRect(x,y,cellSize,cellSize);
				}
			}
		}

		if (sourceSet) {
			g2d.setColor(sourceColor);
			int x = jSource*cellSize + (jSource+1)*sepSize;
			int y = iSource*cellSize + (iSource+1)*sepSize;
			g2d.fillRect(x,y,cellSize,cellSize);
		}

		if (destSet) {
			g2d.setColor(destColor);
			int x = jDest*cellSize + (jDest+1)*sepSize;
			int y = iDest*cellSize + (iDest+1)*sepSize;
			g2d.fillRect(x,y,cellSize,cellSize);
		}

	}

	public void mouseClicked(MouseEvent e) {
		// Nothing to do here.
	}

	public void mouseEntered(MouseEvent e) {
		// Nothing to do here.
	}

	private Point screenToGridCoords(Point p) {
		int x = p.x;
		int y = p.y;

		// Search for iPos.
		int iPos = -1;
		for (int i=0; i<height; i++) {
			y -= sepSize;
			if (y < 0) break;
			if (y < cellSize) {
				iPos = i;
				break;
			}
			y -= cellSize;
		}

		// Search for jPos.
		int jPos = -1;
		for (int j=0; j<height; j++) {
			x -= sepSize;
			if (x < 0) break;
			if (x < cellSize) {
				jPos = j;
				break;
			}
			x -= cellSize;
		}

		return new Point(iPos,jPos);
	}

	private boolean updateMousePos(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();

		pMousePrev = pMouse;
		pMouse = new Point(x,y);

		Point gridP = screenToGridCoords(pMouse);
		int iPos = gridP.x;
		int jPos = gridP.y;
		if (iPos != -1 && jPos != -1) {
			iMouse = iPos;
			jMouse = jPos;
			return true;
		}

		return false;
	}

	public void mousePressed(MouseEvent e) {
		boolean OK = updateMousePos(e);
		if (!OK)
			System.out.println("Clicked on separator");
		System.out.println("mousePressed: " + iMouse + "," + jMouse);
		if (!OK) return;

		if (op == Operation.SET_SOURCE) {
			if (map.getObstacle(iMouse,jMouse)) return;
			sourceSet = true;
			iSource = iMouse;
			jSource = jMouse;
			repaint();
		}
		else if (op == Operation.SET_DEST) {
			if (map.getObstacle(iMouse,jMouse)) return;
			destSet = true;
			iDest = iMouse;
			jDest = jMouse;
			repaint();
		}
		else if (op == Operation.SET_WALL) {
			if (sourceSet && iSource == iMouse && jSource == jMouse) return;
			if (destSet && iDest == iMouse && jDest == jMouse) return;
			map.setObstacle(iMouse,jMouse,true);
			repaint();
		}
		else if (op == Operation.CLEAR_WALL) {
			map.setObstacle(iMouse,jMouse,false);
			repaint();
		}
	}

	public void mouseExited(MouseEvent e) {
		// Nothing to do here.
	}

	public void mouseReleased(MouseEvent e) {
		// Nothing to do here.
	}

	private void genPoints(Point2D.Double a, Point2D.Double b, ArrayList<Point2D.Double> list) {
		double dx = a.x - b.x;
		double dy = a.y - b.y;
		double dist = Math.abs(dx*dx+dy*dy);
		if (dist < 0.1) return;

		double mx = (a.x + b.x) / 2;
		double my = (a.y + b.y) / 2;
		Point2D.Double m = new Point2D.Double(mx,my);
		list.add(m);
		genPoints(a,m,list);
		genPoints(m,b,list);
	}

	public void mouseDragged(MouseEvent e) {
		updateMousePos(e);
		System.out.println("mouseDragged: " + iMouse + "," + jMouse);

		if (op == Operation.SET_WALL) {
			if (pMousePrev != null) {
				// paint all intermediate blocks
				ArrayList<Point2D.Double> points = new ArrayList<Point2D.Double>();
				Point2D.Double a = new Point2D.Double(pMousePrev.x,pMousePrev.y);
				Point2D.Double b = new Point2D.Double(pMouse.x,pMouse.y);
				genPoints(a,b,points);
				System.out.println("list size = " + points.size());
				for (Point2D.Double p: points) {
					Point intP = new Point((int) Math.round(p.getX()),(int) Math.round(p.getY()));
					Point gridP = screenToGridCoords(intP);
					int iPos = gridP.x;
					int jPos = gridP.y;
					if (iPos < 0 || iPos >= map.getHeight() || jPos < 0 || jPos >= map.getWidth()) continue;
					//if (iPos == -1 || jPos == -1) continue;
					map.setObstacle(iPos,jPos,true);
				}
			}
			else {
				if (iMouse < 0 || iMouse >= map.getHeight() || jMouse < 0 || jMouse >= map.getWidth()) return;
				map.setObstacle(iMouse,jMouse,true);
			}

			repaint();
		}
		else if (op == Operation.CLEAR_WALL) {
			if (iMouse < 0 || iMouse >= map.getHeight() || jMouse < 0 || jMouse >= map.getWidth()) return;
			map.setObstacle(iMouse,jMouse,false);
			repaint();
		}
	}

	public void mouseMoved(MouseEvent e) {
		// Nothing to do here.
	}

}
