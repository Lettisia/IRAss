import java.util.ArrayList;

public class DocumentParser {
    private ArrayList<Document> documents = new ArrayList<Document>();
    private boolean removeStopwords = true;
    private boolean printTerms = false;

    public DocumentParser(String filename, boolean removeStopwords, boolean printTerms) {

    }

    public DocumentParser(String filename) {

    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public ArrayList<Document> parse() {

    }
}
