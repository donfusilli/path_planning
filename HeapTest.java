import static org.junit.Assert.*;

import org.junit.Test;


public class HeapTest {
	private static final double epsilon = 0.00001;
	
	@Test
	public void testHeapInt() {
		MinHeap h = new MinHeap(10);
		assertEquals(0,h.size());
	}

	@Test
	public void testHeapHeapEntryArray() {
		int n = 10;
		HeapEntry[] entries = new HeapEntry[n];
		for (int i=n-1; i>=0; i--) {
			entries[n-i-1] = new HeapEntry(i,i);
		}
		MinHeap h = new MinHeap(entries);
		assertTrue(h.checkHeap());
	}

	@Test
	public void testAdd() {
		int n = 10;
		MinHeap h = new MinHeap(n);
		for (int i=n-1; i>=0; i--) {
			h.add(new HeapEntry(i,i));
			assertTrue(h.checkHeap());
		}
	}

	@Test
	public void testPeekMin() {
		int n = 10;
		HeapEntry[] entries = new HeapEntry[n];
		for (int i=n-1; i>=0; i--) {
			entries[i] = new HeapEntry(i,i);
		}
		MinHeap h = new MinHeap(entries);
		assertEquals(0,h.peekMin().value,epsilon);
	}

	@Test
	public void testExtractMin() {
		int n = 10;
		HeapEntry[] entries = new HeapEntry[n];
		for (int i=n-1; i>=0; i--) {
			entries[i] = new HeapEntry(i,i);
		}
		MinHeap h = new MinHeap(entries);
		for (int i=0; i<n; i++) {
			assertEquals(n-i,h.size());
			assertEquals(i,h.extractMin().value,epsilon);
		}
		assertEquals(0,h.size());
	}

	@Test
	public void testUpdate() {
		int n = 10;
		HeapEntry[] entries = new HeapEntry[n];
		HeapEntry[] entries2 = new HeapEntry[n];
		for (int i=n-1; i>=0; i--) {
			entries[i] = new HeapEntry(i,i);
			entries2[i] = entries[i];
		}
		MinHeap h = new MinHeap(entries);
		for (int i=n-1; i>=0; i--) {
			h.update(entries2[i],i-n);
			assertEquals(i-n,h.peekMin().value,epsilon);
		}
		
	}

}
