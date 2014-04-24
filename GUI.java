import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

/**
 * NOTHING FOR YOU TO DO HERE.
 * 
 * The class for the main window of the program.
 *
 */
class GUI extends JFrame implements ActionListener {
	private final int mapWidth = 30;
	private final int mapHeight = 25;

	private MapPanel mapPanel;
	
	private JToggleButton sourceButton;
	private JToggleButton destButton;
	private JToggleButton wallButton;
	private JToggleButton eraseButton;

	private JButton resetButton;
	private JButton dijkstraButton;
	private JButton aStarButton;
	private JButton dfsButton;

	private JLabel msgLabel;
	
	public GUI() {
		super("CS 2110 - Path Planning");
		setLayout(new BorderLayout());

		mapPanel = new MapPanel(this,mapWidth,mapHeight);
		JToolBar toolBar = setUpToolBar();
		
		// Message panel
		JPanel msgPanel = new JPanel();
		msgPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		msgPanel.setPreferredSize(new Dimension(getWidth(),18));
		msgPanel.setLayout(new GridLayout(1,4));
		msgLabel = new JLabel("");
		msgPanel.add(msgLabel);
		
		// Add to window
		add(mapPanel,BorderLayout.WEST);
		add(toolBar,BorderLayout.EAST);
		add(msgPanel,BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setMsg(String msg) {
		String str = "Last message: " + msg;
		msgLabel.setText(str);
	}
	
	// Handle events from toolbar.
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed");
		
		Object s = e.getSource();
		if (s == sourceButton) {
			mapPanel.setOp(Operation.SET_SOURCE);
		}
		else if (s == destButton) {
			mapPanel.setOp(Operation.SET_DEST);
		}
		else if (s == wallButton) {
			mapPanel.setOp(Operation.SET_WALL);
		}
		else if (s == eraseButton) {
			mapPanel.setOp(Operation.CLEAR_WALL);
		}
		else if (s == resetButton) {
			mapPanel.reset();
		}
		else if (s == dijkstraButton) {
			mapPanel.runDijkstra();
		}
		else if (s == aStarButton) {
			mapPanel.runAStar();
		}
		else if (s == dfsButton) {
			mapPanel.runDFS();
		}
		else {
			System.err.println(e);
		}
	}
	
	private JToolBar setUpToolBar() {
		ButtonGroup tools = new ButtonGroup();

		sourceButton = new JToggleButton("Source");
		sourceButton.addActionListener(this);

		destButton = new JToggleButton("Destination");
		destButton.addActionListener(this);

		wallButton = new JToggleButton("Obstacle");
		wallButton.addActionListener(this);

		eraseButton = new JToggleButton("Erase");
		eraseButton.addActionListener(this);
		
		tools.add(sourceButton);
		tools.add(destButton);
		tools.add(wallButton);
		tools.add(eraseButton);

		resetButton = new JButton("Reset");
		resetButton.addActionListener(this);
		dijkstraButton = new JButton("Dijkstra");
		dijkstraButton.addActionListener(this);
		aStarButton = new JButton("A Star");
		aStarButton.addActionListener(this);
		dfsButton = new JButton("DFS");
		dfsButton.addActionListener(this);
		
		JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);

		toolBar.add(sourceButton);
		toolBar.add(destButton);
		toolBar.add(wallButton);
		toolBar.add(eraseButton);
		
		toolBar.add(new JLabel(" "));
		
		toolBar.add(resetButton);
		toolBar.add(dijkstraButton);
		toolBar.add(aStarButton);
		toolBar.add(dfsButton);
		
		return toolBar;
	}
	
	public static void main(String[] args) {
		GUI mainWindow = new GUI();
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
