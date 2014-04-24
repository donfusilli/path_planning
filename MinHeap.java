// DO NOT IMPORT ANYTHING.

/**
 *
 * A min binary heap.
 * 
 * MIN HEAP INVARIANT:
 * For every i = 1, ..., n-1: heap[getParent(i)] <= heap[i].
 * 
 * The data structure should always satisfy the invariant.
 *
 */
public class MinHeap {
	/** Number of entries in the heap. **/
	int n;
	/** The array that holds the entries of the heap. **/
	HeapEntry[] heap;

	/**
	 * Construct an empty heap.
	 * 
	 * @param nMax Maximum number of entries the heap can hold.
	 */
	public MinHeap(int nMax) {
		heap = new HeapEntry[nMax];
	}
	
	/**
	 * Can be O(n), but even if you do it in O(n log n) it is fine.
	 * 
	 * Constructs a heap from a collection of entries.
	 * 
	 * Assume: All the references in the collection are distinct.
	 * 
	 * @param entries A collection of heap entries.
	 */
	public MinHeap(HeapEntry[] entries) {
		if (entries == null)
			throw new IllegalArgumentException();
		heap = new HeapEntry[entries.length];
		for (HeapEntry e : entries) {
			add(e);
		}
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Adds a new entry to the heap.
	 * 
	 * @param e A heap entry.
	 */
	public void add(HeapEntry e) {
		if (e == null)
			throw new IllegalArgumentException();
		if (n == heap.length) // heap full
			throw new RuntimeException();
		heap[n] = e;
		e.heapIndex = n;
		n++;
		while (getParent(e.heapIndex) != -1 && e.compareTo(heap[getParent(e.heapIndex)]) < 0) {
			int i = e.heapIndex;
			int parentI = getParent(i);
			HeapEntry temp = heap[parentI];
			heap[parentI] = e;
			heap[i] = temp;
			e.heapIndex = parentI;
			heap[i].heapIndex = i;
		}
	}
	
	/**
	 * 
	 * @return Number of elements in the heap.
	 */
	public int size() {
		return n;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the parent of the subtree (-1 if there isn't one).
	 */
	public int getParent(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (i == 0)
			return -1;
		return (i - 1)/2;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return True iff the subtree rooted at i is a leaf.
	 */
	public boolean isLeaf(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (2*i + 1 >= n)
			return true;
		return false;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the left child of the subtree (-1 if there isn't one).
	 */
	public int getLeft(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (isLeaf(i))
			return -1;
		return 2*i + 1;
	}
	
	/**
	 * Should be O(1).
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 * @return The index for the right child of the subtree (-1 if there isn't one).
	 */
	public int getRight(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (isLeaf(i))
			return -1;
		if (2*i + 2 >= n)
			return -1;
		return 2*i + 2;
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Assume:
	 * - The subtree rooted at getLeft(i) satisfies the heap invariant.
	 * - The subtree rooted at getRight(i) satisfies the heap invariant.
	 * 
	 * After the method terminates, the subtree rooted at i must
	 * satisfy the heap invariant.
	 * 
	 * @param i Index in the heap array that specifies a subtree of the heap.
	 */
	private void heapify(int i) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		if (!isLeaf(i)) {
			int leftI = getLeft(i);
			int rightI = getRight(i);
			if (rightI == -1 || heap[leftI].compareTo(heap[rightI]) <= 0) {
				if (heap[i].compareTo(heap[leftI]) > 0) {
					HeapEntry temp = heap[leftI];
					heap[leftI] = heap[i];
					heap[i] = temp;
					heap[leftI].heapIndex = leftI;
					heap[i].heapIndex = i;
					heapify(leftI);
				} else if (rightI != -1 && heap[i].compareTo(heap[rightI]) > 0) {
					HeapEntry temp = heap[rightI];
					heap[rightI] = heap[i];
					heap[i] = temp;
					heap[rightI].heapIndex = rightI;
					heap[i].heapIndex = i;
					heapify(rightI);
				}
			} else {
				if (heap[i].compareTo(heap[rightI]) > 0) {
					HeapEntry temp = heap[rightI];
					heap[rightI] = heap[i];
					heap[i] = temp;
					heap[rightI].heapIndex = rightI;
					heap[i].heapIndex = i;
					heapify(rightI);
				} else if (heap[i].compareTo(heap[leftI]) > 0) {
					HeapEntry temp = heap[leftI];
					heap[leftI] = heap[i];
					heap[i] = temp;
					heap[leftI].heapIndex = leftI;
					heap[i].heapIndex = i;
					heapify(leftI);
				}
			}
		}
	}
	
	/**
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry peekMin() {
		if (n == 0)
			throw new IllegalArgumentException();
		return heap[0];
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Removes the entry from the top.
	 * 
	 * @return The entry at the top of the heap.
	 */
	public HeapEntry extractMin() {
		if (n == 0)
			throw new IllegalArgumentException();
		HeapEntry min = peekMin();
		min.heapIndex = -1;
		n--;
		if (n == 0) {
			heap[0] = null;
		} else {
			heap[0] = heap[n];
			heap[0].heapIndex = 0;
			heap[n] = null;
			heapify(0);
		}
		return min;
	}
	
	/**
	 * Should be O(log n).
	 * 
	 * Updates a tuple with a new value <= current value.
	 * 
	 * @param e An entry that is inside the heap.
	 * @param newValue New value, has to be <= e.value.
	 */
	public void update(HeapEntry e, double newValue) {
		if (e.value < newValue) {
			System.err.println("e.value = " + e.value + ", newValue = " + newValue);
			throw new IllegalArgumentException();
		}
		e.value = newValue;
		while (getParent(e.heapIndex) != -1 && e.compareTo(heap[getParent(e.heapIndex)]) < 0) {
			int i = e.heapIndex;
			int parentI = getParent(i);
			HeapEntry temp = heap[parentI];
			heap[parentI] = e;
			heap[i] = temp;
			e.heapIndex = parentI;
			heap[i].heapIndex = i;
		}
	}
	
	// YOU CAN USE THE METHODS BELOW TO DEBUG YOUR PROGRAM.
	// Of course, passing the sanity check does not mean your program is correct.
	// You don't have to use these methods or understand them, if you don't want to.
	
	/**
	 * Checks the heap.
	 */
	public boolean checkHeap() {
		// Check heap indexes.
		for (int i=0; i<n; i++) {
			if (heap[i].heapIndex != i) {
				System.err.println("Heap indexes not maintained correctly.");
				return false;
			}
		}
		
		// Check heap invariant.
		for (int i=1; i<n; i++) {
			if (heap[i].compareTo(heap[getParent(i)]) < 0) {
				System.err.println("Heap invariant violated @" + i + ".");
				System.err.println("Current: " + heap[i] + ".");
				System.err.println("Parent: " + heap[getParent(i)] + ".");
				return false;
			}
		}
		
		return true;
	}
	
	private final String indentation = "   ";
	public String toString() {
		if (n == 0) return "";
		
		return toStringHelper(0,"");
	}
	
	private String toStringHelper(int i, String indent) {
		if (i < 0 || i >= n)
			throw new IllegalArgumentException();
		
		String str = indent + heap[i] + "\n";
		
		int left = getLeft(i);
		if (left == -1) return str;
		String newIndent = indent+indentation;
		str += toStringHelper(left,newIndent);
		
		int right = getRight(i);
		if (right == -1) return str;
		str += toStringHelper(right,newIndent);
		
		return str;
	}
}
