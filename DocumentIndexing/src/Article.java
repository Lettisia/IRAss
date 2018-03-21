public class Article {
	private String docNo;
	private String text;
	private String headline;
	
	public Article(String docNo, String headline, String text){
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

}
