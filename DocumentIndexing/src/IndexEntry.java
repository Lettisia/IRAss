import java.util.ArrayList;

class IndexEntry {
    String term;
    int documentFrequency;
    int byteOffset;
    ArrayList<TermFrequencyPair> invertedList = new ArrayList<>();

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getDocumentFrequency() {
        return documentFrequency;
    }

    public void setDocumentFrequency(int documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public void setByteOffset(int byteOffset) {
        this.byteOffset = byteOffset;
    }

    public ArrayList<TermFrequencyPair> getInvertedList() {
        return invertedList;
    }

    public void setInvertedList(ArrayList<TermFrequencyPair> invertedList) {
        this.invertedList = invertedList;
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
