class TermFrequencyPair {
    private final Integer docID;
    private final Integer termFrequency;

    TermFrequencyPair(Integer docID, Integer termFrequency) {
        this.docID = docID;
        this.termFrequency = termFrequency;
    }

    Integer getDocID() {
        return docID;
    }

    Integer getTermFrequency() {
        return termFrequency;
    }

    @Override
    public String toString() {
        return "TermFrequencyPair{" +
                "docID=" + docID +
                ", termFrequency=" + termFrequency +
                '}';
    }
}
