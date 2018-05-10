/**
 * 
 * The reason we use a min-heap is to ensure we need no more than k items stored in the heap (for space efficiency).
For example, assume we have n accumulators. The way the heap is used is as follows:


1. Push the first k items into the min-heap
2. For items from k+1 to n, if the score of the current item is larger than the 
root of the min-heap (that is, the lowest scoring item of the min-heap), 
we pop the min-heap (which removes the smallest element) and we push the current element in.

At the end of processing, we have a heap of size k with the top-k items within.  
For example, assuming we wanted the top-100 items and we had 2 million accumulators,
 we would only need to store a min-heap of 100 items, and we would need to consider each of the 2 million 
 accumulators once in order to find the top-100 items. Hopefully you can see that this approach wont work if we used a max-heap.

Let me know if you are still confused, and I can try to provide a concrete example.
 *
 */

public class minHeap {
	
}