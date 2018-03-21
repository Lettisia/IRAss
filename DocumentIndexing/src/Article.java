public class Article {
	private int documentIndex;
	private String docNo;
	private String text;
	private String headline;
	
	public Article(int documentIndex, String docNo, String headline, String text){
		this.documentIndex = documentIndex;
		this.docNo = docNo;
		this.text = text;
		this.headline = headline;	
	}
	
	public String getDocNo() {
		return docNo;
	}
	
	public String getText() {
		return text;
	}
	
	public String getHeadline() {
		return headline;
	}
	
	public String toString(){ 
		return "DocNo:" + getDocNo() + "\nHeadline:" + getHeadline() + "\nText:" + getText();
	}

	public int getDocumentIndex() {
		return documentIndex;
	}
}
