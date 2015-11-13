
public class Msg {
	String subject;
	String seen;
	

	/**
	 * Object to store subject and flag data for emails.
	 * @param subject. The subject of the message that this is an object of.
	 * @param seen. The flag data of the message that this is an object of. 
	 */
	public Msg(String subject, String seen){
		this.subject=subject;
		this.seen=seen;
	}
	
	/**
	 * Method to get the subject of a Msg.
	 * @return this.subject. The subject of the Msg.
	 */
	public String getSubject(){
		return this.subject;
	}
	
	/**
	 * Method to get the flags of a Msg.
	 * @return this.seen. The flag data of a Msg.
	 */
	public String getSeen(){
		return this.seen;
	}
	
}
