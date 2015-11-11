
public class Msg {
	String subject;
	String seen;
	
	public Msg(String subject, String seen){

		this.subject=subject;
		this.seen=seen;
	}
	

	public String getSubject(){
		return this.subject;
	}
	

	
	public String getSeen(){
		return this.seen;
	}
	
}
