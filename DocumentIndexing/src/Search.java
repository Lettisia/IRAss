class Search {
    public static void main(String[] args) {
        if (args.length > 0) {
            String lexiconFile = args[0];
            String indexFile = args[1];
            String mapFile = args[2];

            if (args.length < 4) {
                System.out.println("You need to pass in a Search term!\n");
            } else {
                SearchEngine searchEngine = new SearchEngine(lexiconFile, indexFile, mapFile);

                for (int i = 3; i < args.length; i++) {
                    String queryTerm = args[i];
                    System.out.println(searchEngine.search(queryTerm));
                }
            }
        }
    }
}
