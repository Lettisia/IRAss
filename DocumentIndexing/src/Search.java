public class Search {
    public static void main(String[] args) {
        if (args.length > 0) {
            String lexiconFile = args[0];
            String indexFile = args[1];
            String mapFile = args[2];

            SearchEngine searchEngine = new SearchEngine(lexiconFile, indexFile, mapFile);
            for (int i = 3; i < args.length; i++) {
                String queryTerm = args[i];

            }
        }
    }
}
