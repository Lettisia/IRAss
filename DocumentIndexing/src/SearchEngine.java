import java.util.HashMap;

public class SearchEngine {
    private HashMap<String, IndexEntry> lexicon = new HashMap<>();
    private HashMap<Integer, String> documentIDMap = new HashMap<>();
    private String indexFile;

    public SearchEngine(String lexiconFile, String indexFile, String mapFile) {
        readLexiconFile(lexiconFile);
        readMapFile(mapFile);
        this.indexFile = indexFile;
    }

    private void readMapFile(String mapFile) {

    }

    private void readLexiconFile(String lexiconFile) {

    }

    public String search(String query) {
        return "Haha, not found!";
    }
}
