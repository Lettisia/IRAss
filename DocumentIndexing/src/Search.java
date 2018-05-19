import java.awt.Toolkit;

class Search {
    private static final String BM25_SIMILARITY_FUNCTION = "-BM25";

    public static void main(String[] args) {
        String stopFile = "";
        String queryLabel = "";
        String numResults = "";
        String lexiconFile = "";
        String invlistFile = "";
        String mapFile = "";
        int summaryType;
        String collectionFile;
        SearchEngine searchEngine;
        Summariser summariser = null;

        if (args.length > 0) {
            String similarityFunction = args[0];

            if (similarityFunction.equals(BM25_SIMILARITY_FUNCTION)) {
                for (int i = 1; i < 11; i++) {
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
                            System.err.println("Search -BM25 -q <query-label> -n <num-results>  -l <lexicon> -i <invlists> -m <map> [-s <stoplist>] [-gb <collectionfile>] [-qb <collectionfile>] <queryterm-1> [<queryterm-2> ...<queryterm-N>]");
                            break;
                    }
                }
                searchEngine = new SearchEngine(lexiconFile, invlistFile, mapFile);
                StringBuilder query = new StringBuilder();
                long startTime = System.nanoTime();
                for (int i = 11; i < args.length; i++) {
                    switch (args[i]) {
                        case "-s":
                            stopFile = args[i + 1];
                            i++;
                            break;
                        case "-gb":
                            summaryType = SentenceRanker.GRAPH_SIMILARITY;
                            i++;
                            collectionFile = args[i];
                            summariser = new Summariser(collectionFile, summaryType);
                            break;
                        case "-qb":
                            summaryType = SentenceRanker.QUERY_BIASED;
                            i++;
                            collectionFile = args[i];
                            summariser = new Summariser(collectionFile, summaryType);
                            break;
                        default:
                            query.append(args[i]).append(" ");
                            break;
                    }
                }
                searchEngine.search(Integer.parseInt(queryLabel), Integer.parseInt(numResults), stopFile, query.toString(), summariser);
                long endTime = System.nanoTime();
                double duration = (endTime - startTime) * 0.000001 / 60.0;
                System.out.println("Running time: " + duration + " ms");
                Toolkit.getDefaultToolkit().beep();
            }
        }

    }
}
