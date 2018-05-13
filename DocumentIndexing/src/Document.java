import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Document {
	
	private static final double k1 = 1.2;
	private static final double b = 0.75;

	private final Integer documentIndex;
	private String docNo;
	private String text;
	private ArrayList<String> terms = new ArrayList<>();
	private Integer documentLength=0;
	private double kValue;
	
	private static int numberOfDocuments = 0;

	private static int avgDocumentLength = 0;
	
	Document(String docNo, String headline, String text) {
		numberOfDocuments++;
		this.documentIndex = numberOfDocuments;
		this.docNo = docNo;

		if (headline == null) {
			headline = "";
		}
		if (text == null) {
			text = "";
		}

		this.text = headline + " " + text;
	}
	
	Document(int docIndex, String docNo, Double kValue){
		this.documentIndex = docIndex;
		this.docNo = docNo;
		this.kValue = kValue;
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

	String printTerms() {
		StringBuilder builder = new StringBuilder();
		for (String term : terms) {
			builder.append(term).append(" ");
		}
		return builder.toString();
	}
	
	public static void setNumberOfDocuments(int numberOfDocuments) {
		Document.numberOfDocuments = numberOfDocuments;
	}

	public static Integer getNumberOfDocuments() {
		return numberOfDocuments;
	}

	//Calculating document length
	public void calculateDocLength(){
		for(String term : terms){
			this.documentLength += term.length();
		}
	}

	//Calculating Average Length
	public static void calculateAvgLength(ArrayList<Document> documents){
		Integer totalDocLength=0;
		for(Document document : documents){
			if(document!=null){
				document.calculateDocLength();
				totalDocLength += document.documentLength;
			}
		}
		avgDocumentLength = totalDocLength/numberOfDocuments;
	}

	//Calculating K-value
	public double calculateKValue(){
		kValue = k1 * ((1 - b) + ((b * this.documentLength))/avgDocumentLength);
		return kValue;
	}
	
	public static double calcSimilarityScore(double kValue, int termFrequency, int documentFrequency){
		double k1 = 1.2;
		double wt = ((k1 + 1) * termFrequency) / (kValue + termFrequency);
		double bm25 = wt * Math.log((numberOfDocuments - documentFrequency + 0.5) / (documentFrequency + 0.5));	
		return bm25;
	}

	public double getkValue() {
		return kValue;
	}
	
	String getDocNo() {
		return docNo;
	}

	private String getText() {
		return text;
	}

	Integer getDocumentIndex() {
		return documentIndex;
	}

	public ArrayList<String> getTerms() {
		return terms;
	}
	
	public String toString() {
		return "DocNo:" + getDocNo() + "\nText:" + getText();
	}
}
