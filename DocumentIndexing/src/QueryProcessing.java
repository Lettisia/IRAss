import java.util.ArrayList;
import java.util.Arrays;

class QueryProcessing {
	
	private String query;
	private StopwordRemover stopwordRemover = null;
    private ArrayList<String> queryTerms = new ArrayList<>();

	QueryProcessing(String query, boolean removeStopwords){
		this.query = query;
		if (removeStopwords) {
			stopwordRemover = new StopwordRemover("stoplist");
		}
		parse();
	}

	private void parse() {
		filteringQuery();
		toLowerCase();
		tokenise();
		removeStopwords();
	}

	private void filteringQuery() {
		query = query.replaceAll("[\\p{Punct}]", " ").replaceAll("\\d+", "");
	}

	private void toLowerCase() {
		query = query.toLowerCase();
	}

	private void tokenise() {
		String[] splitText = query.split("\\s+");
		queryTerms = new ArrayList<>(Arrays.asList(splitText));
	}

	private void removeStopwords() {
		if (stopwordRemover != null) {
			queryTerms = stopwordRemover.removeStopwords(queryTerms);
		}
	}

	ArrayList<String> getQueryTerms() {
		return queryTerms;
	}
}