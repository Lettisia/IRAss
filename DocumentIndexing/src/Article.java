import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Article {
    private int documentIndex;
    private String docNo;
    private String text;
    private ArrayList<String> terms = new ArrayList<>();
    private static int articleCount = 1;

    Article(String docNo, String headline, String text) {
        this.documentIndex = articleCount++;
        this.docNo = docNo;
        this.text = headline + " " + text;
    }

    void parse(StopwordRemover stopwordRemover) {
        toLowerCase();
        tokenise();
        if (stopwordRemover != null) {
            removeStopwords(stopwordRemover);
        }
    }

    HashMap<String, Integer> countTerms() {
        HashMap<String, Integer> termFrequencyList = new HashMap<>();
        for (String term : terms) {
            if (termFrequencyList.containsKey(term)) {
                termFrequencyList.put(term, termFrequencyList.get((term)) + 1);
            } else {
                termFrequencyList.put(term, 1);
            }
        }
        return termFrequencyList;
    }

    private void tokenise() {
        String[] splitText = text.split("\\s+");
        terms = new ArrayList<>(Arrays.asList(splitText));
    }

    private void toLowerCase() {
        text = text.toLowerCase();
    }

    private void removeStopwords(StopwordRemover stopwordRemover) {
        terms = stopwordRemover.removeStopwords(terms);
    }

    private String getDocNo() {
        return docNo;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return "DocNo:" + getDocNo() + "\nText:" + getText();
    }

    int getDocumentIndex() {
        return documentIndex;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = terms;
    }


}
