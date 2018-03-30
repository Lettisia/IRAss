public class TermFrequencyPair {
    private int docID;
    private int termFrequency;

    TermFrequencyPair(int docID, int termFrequency) {
        this.docID = docID;
        this.termFrequency = termFrequency;
    }

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public int getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(int termFrequency) {
        this.termFrequency = termFrequency;
    }

    @Override
    public String toString() {
        return "TermFrequencyPair{" +
                "docID=" + docID +
                ", termFrequency=" + termFrequency +
                '}';
    }
}
