import java.awt.Toolkit;

class Search {
	private static final String BM25_SIMILARITY_FUNCTION = "-BM25";
	public static void main(String[] args) {
		//"/home/inforet/a1/stoplist";
		String stopFile = "";
		String queryLabel = "";
		String numResults = "";
		String lexiconFile = "";
		String invlistFile = "";
		String mapFile = "";
		SearchEngine searchEngine = null;

		if (args.length > 0) {
			String similarityFunction = args[0];

			if(similarityFunction.equals(BM25_SIMILARITY_FUNCTION)){
				for(int i = 1; i<11;i++){
					switch (args[i]) {
					case "-q":
						i++;
						queryLabel = args[i];
						break;
					case "-n":
						i++;
						numResults = args[i];
						break;
					case "-l":
						i++;
						lexiconFile = args[i];
						break;
					case "-i":
						i++;
						invlistFile = args[i];
						break;
					case "-m":
						i++;
						mapFile = args[i];
						break;
					default:
						System.err.println("Search -BM25 -q <query-label> -n <num-results> -l <lexicon> -i<invlists> -m <map> [-s <stoplist>] <queryterm-1> [<queryterm-2> ...<queryterm-N>]");
						break;
					}
				}
				searchEngine = new SearchEngine(lexiconFile, invlistFile, mapFile);
				String query = "";
				long startTime = System.nanoTime();
				for (int i = 11; i < args.length; i++) {
					switch (args[i]) {
					case "-s":
						stopFile = args[i + 1];
						i++;
						break;
					default:
						query += args[i] + " ";
						break;
					}
				}
				searchEngine.search(Integer.parseInt(queryLabel), Integer.parseInt(numResults), stopFile, query);
				long endTime = System.nanoTime();
				double duration = (endTime - startTime) * 0.000001 / 60.0;
				System.out.println("Running time: " + duration + " ms");
				Toolkit.getDefaultToolkit().beep();
			} 
		}

	}
}
