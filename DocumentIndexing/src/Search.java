import java.awt.Toolkit;

class Search {
	private static final String BM25_SIMILARITY_FUNCTION = "-BM25";
	public static void main(String[] args) {
		//"/home/inforet/a1/stoplist";
		String stopFile = "";
		String queryLabel = "";
		String numResults = "";
		String lexiconFile = "";
		String indexFile = "";
		String mapFile = "";

		boolean removeStopWords = false;

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
						indexFile = args[i];
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
				long startTime = System.nanoTime();
				SearchEngine searchEngine = new SearchEngine(Integer.parseInt(queryLabel), Integer.parseInt(numResults), lexiconFile, indexFile, mapFile);

				for (int i = 11; i < args.length; i++) {
					switch (args[i]) {
					case "-s":
						removeStopWords = true;
						stopFile = args[i + 1];
						i++;
						break;
					default:
						String queryTerm = args[i];

						if (removeStopWords) {
							searchEngine.processQueryTerms(queryTerm, stopFile);
						} else {
							searchEngine.processQueryTerms(queryTerm);
						}
						break;
					}
				}
				long endTime = System.nanoTime();
				double duration = (endTime - startTime) * 0.000001 / 60.0;
				Toolkit.getDefaultToolkit().beep();
				System.out.println("Running time: " + duration + " ms");
			} 
		}

	}
}
