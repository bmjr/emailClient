import java.io.IOException;
 
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
 
public class Demo {
	/**
	 * Main Demo method to initialise everything.
	 */
    public static void main(String[] args) {
        try {
        	JTextField user = new JTextField(30);
        	int actionUser = JOptionPane.showConfirmDialog(null, user,"Enter Username",JOptionPane.OK_CANCEL_OPTION);  
    		String username=null;
			if(actionUser < 0) {
    			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
    			System.exit(0); 
    		}
    		else 
    			username = (String)user.getText();
        	JPasswordField pwd = new JPasswordField(30);
      
    		int action = JOptionPane.showConfirmDialog(null, pwd,"Enter Password",JOptionPane.OK_CANCEL_OPTION);  
    		String password=null;
			if(action < 0) {
    			JOptionPane.showMessageDialog(null,"Cancel, X or escape key selected"); 
    			System.exit(0); 
    		}
    		else 
    			password = new String(pwd.getPassword());  
            IMAPClient client = new IMAPClient(username,password);
            client.Connect();
            
            //Initial inbox operations
            Inbox inbox = new Inbox(client.fetchStore());
            inbox.processMail();

            //Create gui and populate it
            GUI gui = new GUI(username,password);
            gui.setVisible(true);
            gui.setMessages(inbox.getMessages(),inbox.getSubjects());
            gui.setInboxTable();
            gui.setInbox(inbox);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
