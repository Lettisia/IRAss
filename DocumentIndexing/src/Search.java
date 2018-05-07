class Search {
    public static void main(String[] args) {
        String stopFile = ""; //"/home/inforet/a1/stoplist";
        boolean removeStopWords = false;

        if (args.length > 0) {
            String lexiconFile = args[0];
            String indexFile = args[1];
            String mapFile = args[2];
            SearchEngine searchEngine = new SearchEngine(lexiconFile, indexFile, mapFile);

            for (int i = 3; i < args.length; i++) {
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
        }
    }
}
