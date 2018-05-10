import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

class Article {
    private final Integer documentIndex;
    private final String docNo;
    private String text;
    private ArrayList<String> terms = new ArrayList<>();
    private static Integer articleCount = 1;
    //Document Length
    private Integer documentLength=0;

    Article(String docNo, String headline, String text) {
        this.documentIndex = articleCount;
        articleCount++;
        this.docNo = docNo;

        if (headline == null) {
            headline = "";
        }
        if (text == null) {
            text = "";
        }

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
            if (!term.equals(" ") && !term.equals("")) {
                if (termFrequencyList.containsKey(term)) {
                    termFrequencyList.put(term, termFrequencyList.get((term)) + 1);
                } else {
                    termFrequencyList.put(term, 1);
                }
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

    String getDocNo() {
        return docNo;
    }

    private String getText() {
        return text;
    }

    public String toString() {
        return "DocNo:" + getDocNo() + "\nText:" + getText();
    }

    Integer getDocumentIndex() {
        return documentIndex;
    }

    public ArrayList<String> getTerms() {
        return terms;
    }

    String printTerms() {
        StringBuilder builder = new StringBuilder();
        for (String term : terms) {
            builder.append(term).append(" ");
        }
        return builder.toString();
    }
    
    //Calculating document length
    public void calculateDocLength(){
    	for(String term : terms){
    		documentLength += term.length();
    	}
    }
    
    public Integer getDocumentLength(){
    	this.calculateDocLength();
    	return documentLength;
    }
}
