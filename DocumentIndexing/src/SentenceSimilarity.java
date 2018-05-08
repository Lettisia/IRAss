import java.util.ArrayList;
import java.util.PriorityQueue;

public class SentenceSimilarity {
    private ArrayList <Sentence> sentences;
    private PriorityQueue<Sentence> rankedSentences;

    public SentenceSimilarity(ArrayList<String> texts) {
        for (int i=0; i<texts.size(); i++) {
            sentences.add(new Sentence(texts.get(i), i));
        }
    }


    public String generateSummary() {
        calculateSimilarity();
        return null;
    }

    private void calculateSimilarity() {

    }
}
