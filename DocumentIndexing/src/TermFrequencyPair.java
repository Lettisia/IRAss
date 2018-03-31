public class TermFrequencyPair {
    private Integer docID;
    private Integer termFrequency;

    TermFrequencyPair(Integer docID, Integer termFrequency) {
        this.docID = docID;
        this.termFrequency = termFrequency;
    }

    public Integer getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public Integer getTermFrequency() {
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
