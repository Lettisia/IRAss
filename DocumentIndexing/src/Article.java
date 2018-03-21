import java.util.ArrayList;
import java.util.Arrays;

public class Article {
    private int documentIndex;
    private String docNo;
    private String text;
    private ArrayList<String> terms = new ArrayList<>();

    public Article(int documentIndex, String docNo, String headline, String text) {
        this.documentIndex = documentIndex;
        this.docNo = docNo;
        this.text = headline + text;
    }

    public void tokenise() {
        String[] headlineStrings = text.split(" ");
        terms = new ArrayList<>(Arrays.asList(headlineStrings));
    }

    public String getDocNo() {
        return docNo;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return "DocNo:" + getDocNo() + "\nText:" + getText();
    }

    public int getDocumentIndex() {
        return documentIndex;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    public void setTerms(ArrayList<String> terms) {
        this.terms = terms;
    }
}
