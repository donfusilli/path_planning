import static org.junit.Assert.*;

import java.util.*;
import org.junit.*;

public class DigraphWTest {

	int n1 = 2, n2 = 5, n3 = 10, n4 = 6, n5 = 5, n6 = 4, n7 = 7;

	int[] left2  = { 0, 1, 1, 2, 4, 4 };
	int[] right2 = { 1, 2, 3, 0, 2, 3 };
	
	int[] left3  = { 0, 0, 3, 3, 4, 5, 6, 7, 9, 9};
	int[] right3 = { 1, 2, 1, 2, 5, 6, 4, 8, 7, 8};

	int[] left4  = { 0, 0, 0, 1, 1, 2, 2, 4, 5 };
	int[] right4 = { 1, 2, 5, 2, 3, 3, 5, 3, 4 };
	double[] w4  = { 7, 9,14,10,15,12, 2, 6, 9 };
	
	int[] left5  = { 0, 0, 1, 1, 2, 2, 3, 3 };
	int[] right5 = { 1, 2, 3, 4, 3, 4, 2, 4 };
	double[] w5  = { 1, 5, 2, 8, 2, 2, 1, 4 };
	
	int[] left6  = { 0, 0, 1, 2 };
	int[] right6 = { 1, 2, 2, 3 };
	double[] w6  = { 1, 2, 1, 1 };
	
	int[] left7  = { 0, 0, 1, 1, 1, 2, 3, 3, 4, 4, 5 };
	int[] right7 = { 1, 3, 2, 3, 4, 4, 4, 5, 5, 6, 6 };
	double[] w7  = { 7, 5, 8, 9, 7, 5,15, 6, 8, 9, 11};
	
	DigraphW g1, g2, g3, g4, g5, g6, g7;
	
	@Before
	public void setUp() {
		g1 = new DigraphW(n1);
		g1.addEdge(0,1,1.0);
		
		g2 = new DigraphW(n2);
		for (int i=0; i<left2.length; i++) {
			g2.addEdge(left2[i],right2[i],1.0);
		}

		g3 = new DigraphW(n3);
		for (int i=0; i<left3.length; i++) {
			g3.addEdge(left3[i],right3[i],1.0);
		}

		g4 = new DigraphW(n4);
		for (int i=0; i<left4.length; i++) {
			g4.addEdge(left4[i],right4[i],w4[i]);
		}

		g5 = new DigraphW(n5);
		for (int i=0; i<left5.length; i++) {
			g5.addEdge(left5[i],right5[i],w5[i]);
		}

		// Undirected
		g6 = new DigraphW(n6);
		for (int i=0; i<left6.length; i++) {
			g6.addEdge(left6[i],right6[i],w6[i]);
			g6.addEdge(right6[i],left6[i],w6[i]);
		}

		// Undirected
		g7 = new DigraphW(n7);
		for (int i=0; i<left7.length; i++) {
			g7.addEdge(left7[i],right7[i],w7[i]);
			g7.addEdge(right7[i],left7[i],w7[i]);
		}
	}
	
	@Test
	public void testGetN() {
		assertEquals(n1,g1.getN());
		assertEquals(n2,g2.getN());
		assertEquals(n3,g3.getN());
		assertEquals(n4,g4.getN());
		assertEquals(n5,g5.getN());
		assertEquals(n6,g6.getN());
		assertEquals(n7,g7.getN());
	}
	
	@Test
	public void testShortestPath() {
		System.out.println("\n----- Shortest Path -----\n");
		
		double[] expDist = new double[]{0, 7, 9, 21, 20, 11 };
		double[] calcDist = new double[6];
		for(int i=0; i<6;i++){
			ArrayList<Integer> visited = new ArrayList<Integer>();
			ArrayList<Integer> path = new ArrayList<Integer>();
			calcDist[i] = g4.shortestPath(0,i,visited,path);
		}
		
		System.out.println("Expected outputs:");
		System.out.println(Arrays.toString(expDist));
		System.out.println("Calculated outputs:");
		System.out.println(Arrays.toString(calcDist));
		
		for(int i=0; i<6;i++){
			assertEquals(expDist[i],calcDist[i],0.01);
		}
	}

}
