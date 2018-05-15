import java.util.ArrayList;
import java.util.Collections;

public class SentenceRanker {
    private static final int SUMMARY_SENTENCE_COUNT = 3;
    private static final int MIN_NUM_TERMS = 5;
    public static final int QUERY_BIASED = -5;
    public static final int GRAPH_SIMILARITY = 22;
    private static final double SIMILARITY_THRESHOLD = 0.15;

    private ArrayList<Sentence> sentences = new ArrayList<>();
    private String headline;

    public SentenceRanker(ArrayList<String> texts) {
        headline = texts.get(0);
        for (int i = 1; i < texts.size(); i++) {
            sentences.add(new Sentence(texts.get(i), i));
        }
    }

    public String generateSummary(int summaryType) {
        return generateSummary(summaryType, null);
    }

    public String generateSummary(int summaryType, ArrayList<String> query) {
        StringBuilder sb = new StringBuilder();
        sb.append(headline.trim()); // add the headline to the summary.
        sb.append("\n");

        ArrayList<Sentence> longSentences = removeShortSentences(sentences);
        // System.out.println("sentences: " + sentences.size() + ", longSentences: " + longSentences.size());

        int numToInclude;

        if (longSentences.size() < SUMMARY_SENTENCE_COUNT + 1) {
            numToInclude = sentences.size(); // If there aren't enough sentences, include the entire text.
        } else {
            sentences = longSentences;
            numToInclude = SUMMARY_SENTENCE_COUNT;

            if (summaryType == QUERY_BIASED && query != null) {
                calculateQueryBiasedRanks(query);
            } else if (summaryType == GRAPH_SIMILARITY) {
                calculateSimilarity();
            }
            Collections.sort(sentences);
        }

        for (int i = 0; i < numToInclude; i++) {
            sb.append(sentences.get(i).getFullText());
            sb.append("  ");
        }

        return sb.toString().trim();
    }

    private void calculateQueryBiasedRanks(ArrayList<String> query) {
        for (Sentence sentence : sentences) {
            sentence.setRank(sentence.queryBiasedRank(query));
        }
    }

    private ArrayList<Sentence> removeShortSentences(ArrayList<Sentence> sentences) {
        ArrayList<Sentence> longSentences = new ArrayList<>();
        for (Sentence sentence : sentences) {
            if (sentence.getNumTerms() >= MIN_NUM_TERMS) {
                longSentences.add(sentence);
            }
        }
        return longSentences;
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

        SentenceRanker sim = new SentenceRanker(sentences);

        System.out.println(sentences.toString());
        System.out.println(sim.generateSummary(GRAPH_SIMILARITY));
    }
}
