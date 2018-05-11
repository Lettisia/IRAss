
public class Document {
	private int docIndex;
	private String docNo;
	private double kValue;
    
    Document(int docIndex, String docNo, Double kValue){
    	this.docIndex = docIndex;
    	this.docNo = docNo;
    	this.kValue = kValue;
    }

	public int getDocIndex() {
		return docIndex;
	}

	public String getDocNo() {
		return docNo;
	}

	public double getkValue() {
		return kValue;
	}
    
    
}
