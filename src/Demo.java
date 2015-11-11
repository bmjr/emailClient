import java.io.IOException;
 
import javax.mail.MessagingException;
 
public class Demo {
 
    public static void main(String[] args) {
        try {
            IMAPClient client = new IMAPClient("bmjrowe@gmail.com","/");
            client.Connect();
            Inbox inbox = new Inbox(client.fetchStore());
            inbox.processMail();

            GUI gui = new GUI();
            gui.setVisible(true);
            gui.setMessages(inbox.getMessages(),inbox.getSubjects());

            gui.setInboxTable();

            gui.setInbox(inbox);

        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.print("finished");
    }
}
