import java.util.Properties;
 
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
 
public class IMAPClient {
    String username;
    String password;
    Store store;
     
    /**
	 * Initialise the user credentials to later connect to IMAP server using them.
	 * @param username. The users email username.
	 * @param password. The users email password.
	 */
    public IMAPClient(String username, String password){
        this.username=username;
        this.password=password; 
    }
    
    /**
	 * Method to connect to IMAP server
	 */
    public void Connect(){
        this.store = null;
 
        //get properties
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps"); 
        props.setProperty("mail.user", this.username);
        props.setProperty("mail.password", this.password);
 
       //create session
        Session session = Session.getDefaultInstance(props);
 
        try
        {
            //use session to set store object  
            this.store = session.getStore("imaps");
            this.store.connect("imap.googlemail.com",username, password);
        } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
     
    /**
	 * Method to return the store object
	 * @return this.store. The store object currently held by this instance of IMAPClient. 
	 */
    public Store fetchStore(){
        return this.store;
    }
}