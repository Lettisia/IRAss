import java.util.ArrayList;

class IndexEntry {
    String term;
    Integer documentFrequency;
    private long byteOffset;
    final ArrayList<TermFrequencyPair> invertedList = new ArrayList<>();

    public void setTerm(String term) {
        this.term = term;
    }

    int getDocumentFrequency() {
        return documentFrequency;
    }

    void setDocumentFrequency(int documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    long getByteOffset() {
        return byteOffset;
    }

    void setByteOffset(long byteOffset) {
        this.byteOffset = byteOffset;
    }

    ArrayList<TermFrequencyPair> getInvertedList() {
        return invertedList;
    }

    @Override
    public String toString() {
        return "IndexEntry{" +
                "term='" + term + '\'' +
                ", documentFrequency=" + documentFrequency +
                ", byteOffset=" + byteOffset +
                ", invertedList=" + invertedList +
                "}";
    }
}
