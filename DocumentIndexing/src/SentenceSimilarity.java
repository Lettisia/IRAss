import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Integer.min;

public class SentenceSimilarity {
    private static final int SUMMARY_SENTENCE_COUNT = 3;
    private ArrayList<Sentence> sentences = new ArrayList<>();
    private static final double SIMILARITY_THRESHOLD = 0.15;
    private String headline;

    public SentenceSimilarity(ArrayList<String> texts) {
        headline = texts.get(0);
        for (int i = 1; i < texts.size(); i++) {
            sentences.add(new Sentence(texts.get(i), i));
        }
    }

    public String generateSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append(headline.trim()); // add the headline to the summary.

        if (sentences.size() > SUMMARY_SENTENCE_COUNT) {
            calculateSimilarity();
            Collections.sort(sentences);
        }

        for (int i = 0; i < min(sentences.size(), SUMMARY_SENTENCE_COUNT); i++) {
            sb.append("\n");
            sb.append(sentences.get(i).getFullText());
        }

        return sb.toString().trim();
    }

    private void calculateSimilarity() {
        for (int i = 1; i < sentences.size() - 1; i++) {
            for (int j = i + 1; j < sentences.size(); j++) {
                double similarity = sentences.get(i).cosineSimilarity(sentences.get(j));
                //System.out.println("Sentences (" + i + ", " + j + ") Similarity: " + similarity + "\n");
                if (similarity > SIMILARITY_THRESHOLD) {
                    sentences.get(i).addSimilarSentence(sentences.get(j));
                    sentences.get(j).addSimilarSentence(sentences.get(i));
                }
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<String> sentences = new ArrayList<>();
        sentences.add("The lazy cow.");
        sentences.add("This is a sentence.");
        sentences.add("This is another sentence.");
        sentences.add("Yet another sentence.");
        sentences.add("Sentences are cool!");

        SentenceSimilarity sim = new SentenceSimilarity(sentences);

        System.out.println(sentences.toString());
        System.out.println(sim.generateSummary());
    }
}
