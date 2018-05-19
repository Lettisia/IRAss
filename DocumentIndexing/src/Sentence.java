import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import static java.lang.Math.sqrt;

class Sentence implements Comparable<Sentence> {
    private final String fullText;
    private final HashMap<String, Integer> termFrequency;
    private double norm = -1;
    private final ArrayList<Sentence> similarSentences = new ArrayList<>();
    private static final Pattern FILTER_REGEX = Pattern.compile("[\\p{Punct}]");
    private double rank = 0.0;

    Sentence(String fullText) {
        this.fullText = fullText;
        this.termFrequency = calculateTermFrequency(fullText);
    }

    private HashMap<String, Integer> calculateTermFrequency(String fullText) {
        String text = removePunctuation(fullText);
        ArrayList<String> terms = tokenise(text);
        return countTerms(terms);
    }

    private HashMap<String, Integer> countTerms(ArrayList<String> terms) {
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

    private ArrayList<String> tokenise(String text) {
        String[] splitText = text.split("\\s+");
        return new ArrayList<>(Arrays.asList(splitText));
    }

    private String removePunctuation(String text) {
        return FILTER_REGEX.matcher(text).replaceAll(" ");
    }

    void addSimilarSentence(Sentence newSentence) {
        similarSentences.add(newSentence);
        rank = similarSentences.size();
    }

    private double getNorm() {
        if (norm > 0) {
            return norm;
        } else {
            norm = 0;
            for (Integer i : termFrequency.values()) {
                norm += i ^ 2;
            }
            norm = sqrt(norm);
            return norm;
        }
    }

    private double dot(Sentence other) {
        double result = 0.0;
        for (String term : termFrequency.keySet()) {
            Integer otherValue = other.getTermFrequency().get(term);
            if (otherValue != null) {
                result += otherValue * termFrequency.get(term);
            }
        }
        return result;
    }

    double cosineSimilarity(Sentence other) {
        return this.dot(other) / (this.getNorm() * other.getNorm());
    }

    double queryBiasedRank(ArrayList<String> queryTerms) {
        double count = 0.0;
        for (String term :
                queryTerms) {
            if (termFrequency.get(term) != null) {
                count++;
            }
        }
        rank = count * count / queryTerms.size();
        return rank;
    }

    private double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    public int getNumTerms() {
        return termFrequency.size();
    }

    String getFullText() {
        return fullText;
    }


    private HashMap<String, Integer> getTermFrequency() {
        return termFrequency;
    }


    @Override
    public int compareTo(Sentence o) {
        return (int) Math.signum(o.getRank() - rank);
    }

    @Override
    public String toString() {
        return fullText;
    }
}
