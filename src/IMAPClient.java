import java.io.IOException;
import java.util.Properties;
 
import javax.mail.Flags.Flag;
import javax.mail.Folder;
 
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.mail.Multipart;
 
import com.sun.mail.imap.IMAPFolder;
 
public class IMAPClient {
    String username;
    String password;
    Store store;
     
    public IMAPClient(String username, String password){
        this.username=username;
        this.password=password; 
    }
    public void Connect() throws MessagingException, IOException {
        // TODO Auto-generated method stub
        
        this.store = null;
 
        // Step 1.1:  set mail user properties using Properties object
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
         
        // Get user password using JPasswordField 
        //JPasswordField pwd = new JPasswordField(10);  
        //int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
        //if(action < 0) {
        //  JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
        //  System.exit(0); 
        //}
        //else 
        //password = new String(pwd.getPassword());  
         
        // Set Property with username and password for authentication  
        props.setProperty("mail.user", this.username);
        props.setProperty("mail.password", this.password);
 
        //Step 1.2: Establish a mail session (java.mail.Session)
        Session session = Session.getDefaultInstance(props);
 
        try
        {
            // Step 2: Get the Store object from the mail session
            // A store needs to connect to the IMAP server  
            this.store = session.getStore("imaps");
            this.store.connect("imap.googlemail.com",username, password);
        }
        finally {}
            // Step 3: Choose a folder, in this case, we chose inbox
    }
     
    public Store fetchStore(){
        return this.store;
    }
}