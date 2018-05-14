import java.util.HashMap;

public class TopNResults
{
	private final Integer numResults;
	private int docIndices[];
	private double docScores[];
	private int currentPos = -1;

	TopNResults(Integer numResults, HashMap<Integer, Double> accumulativeScore){
		if(numResults > accumulativeScore.size())
			this.numResults = accumulativeScore.size();
		else
			this.numResults = numResults;
		this.docIndices = new int[numResults];
		this.docScores = new double[numResults];
		insert(accumulativeScore);
	}

	private void insert(HashMap<Integer, Double> accumulativeScore){
		int index;
		for(index=0; index<numResults; index++){
			Integer key = (Integer) accumulativeScore.keySet().toArray()[index];
			currentPos++;
			docScores[currentPos] = accumulativeScore.get(key);
			docIndices[currentPos] = key;
		}
		heapifyArray();
		while(index<accumulativeScore.size()){
			Integer key = (Integer) accumulativeScore.keySet().toArray()[index];
			if(accumulativeScore.get(key) > docScores[currentPos]){
				docScores[currentPos] = accumulativeScore.get(key);
				docIndices[currentPos] = key;
				heapifyArray();
			}
			index++;
		}
	}

	private void heapify(int size, int position)
	{
		//Initialize smallest as root
		int smallest = position;
		int lChild = 2 * position + 1;
		int rChild = 2 * position + 2;

		// If left child is smaller than root
		if (lChild < size && docScores[lChild] < docScores[smallest])
			smallest = lChild;

		// If right child is smaller than smallest so far
		if (rChild < size && docScores[rChild] < docScores[smallest])
			smallest = rChild;

		// If smallest is not root
		if (smallest != position) {
			double tempDocScores = docScores[position];
			docScores[position] = docScores[smallest];
			docScores[smallest] = tempDocScores;

			int tempDocIndices = docIndices[position];
			docIndices[position] = docIndices[smallest];
			docIndices[smallest] = tempDocIndices;

			// Recursively heapify the affected sub-tree
			heapify(size, smallest);
		}
	}

	// Main function to do heap sort
	private void heapifyArray()
	{
		// Build heap (rearrange array)
		for (int i = numResults / 2 - 1; i >= 0; i--)
			heapify(numResults, i);

		// One by one extract an element from heap
		for (int i = numResults-1; i >= 0; i--) {
			// Move current root to end
			double tempDocScores = docScores[0];
			docScores[0] = docScores[i];
			docScores[i] = tempDocScores;

			int tempDocIndices = docIndices[0];
			docIndices[0] = docIndices[i];
			docIndices[i] = tempDocIndices;

			// Heapifying each sub heap
			heapify(i, 0);
		}
	}
	
	public HashMap<Integer, Integer> getTopNResults(){
		HashMap<Integer, Integer> topNResults = new HashMap<>();
		for(int i=0;i<numResults;i++){
			topNResults.put(i+1,docIndices[i]);
		}
		return topNResults;
	}

	public int[] getDocIndices() {
		return docIndices;
	}

	public double[] getDocScores() {
		return docScores;
	}
}