import java.util.ArrayList;

class IndexEntry {
    String term;
    Integer documentFrequency;
    private long byteOffset;
    final ArrayList<TermFrequencyPair> invertedList = new ArrayList<>();

    public void setTerm(String term) {
        this.term = term;
    }

    public int getDocumentFrequency() {
        return documentFrequency;
    }

    public void setDocumentFrequency(int documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    public long getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(long byteOffset) {
        this.byteOffset = byteOffset;
    }

    public ArrayList<TermFrequencyPair> getInvertedList() {
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
