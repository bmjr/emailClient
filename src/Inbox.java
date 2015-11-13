import java.io.IOException;
import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.search.SearchTerm;

import com.sun.mail.imap.IMAPFolder;
 
public class Inbox {
    Store store;
    private Boolean[] flags;
    private IMAPFolder folder;
    private ArrayList<Msg> data;
    private Message messages[];
	private String searchTerm=null;
    
	/**
	 * Object of Inbox, includes operations such as
	 * - Processing mail from inbox based on a search term
	 * - Processing mail from inbox based on no search term
	 * - Processing the content of a message in mail
	 * - Processing flags from each message in mail
	 */
    public Inbox(Store store){
        this.store=store;
    }
    
    /**
	 * Method to process mail from inbox and break each message into components.
	 */
    public void processMail() throws MessagingException, IOException{
        try{
            folder = (IMAPFolder) this.store.getFolder("inbox"); 
     
            // Step 4: Open the folder
            if(!folder.isOpen())
                folder.open(Folder.READ_WRITE);
            if(searchTerm==null){ 
            	this.messages = folder.getMessages();
            }
            else{
            	SearchTerm term = new SearchTerm() {
            		public boolean match(Message message) {
            			try {
            				if (((message.getSubject()).toLowerCase().contains(searchTerm.toLowerCase()) || message.getContent().toString().toLowerCase().contains(searchTerm.toLowerCase()))) {
            					return true;
            				}
            				
            			} catch (MessagingException ex) {
            				ex.printStackTrace();
            			} catch (IOException e) {
							e.printStackTrace();
						}
            			return false;
            		}
            	};
            	this.messages = folder.search(term);
            }
            ArrayList<Boolean> read = new ArrayList<Boolean>();
            this.data = new ArrayList<Msg>();

    		for(Message message:messages) {
    			
    				String seen;
    				String subject;
    				
    				Flags flag = message.getFlags();
    				if ((flag.contains(Flag.SEEN))){
    					read.add(Boolean.TRUE);
    					seen="READ,";
    				}
    				else{
    					seen="UNREAD,";
    					message.setFlag(Flags.Flag.SEEN, false);
    					read.add(Boolean.FALSE);
    				}
    				
    				String[] userFlags = flag.getUserFlags();
    				for(String userFlag : userFlags){
    				seen+=userFlag+",";			
    				}				
					subject=message.getSubject();
					data.add(new Msg(subject,seen));
				}

        }catch (NoSuchProviderException e) {
        	e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    

    /**
	 * Method to return the messages sourced from processMail() method.
	 * @return this.messages. The array of messages from folder.
	 */  
    public Message[] getMessages(){
    	return this.messages;
    }
    
    /**
	 * Method to return array of msg which contains subject and flag data.
	 * @return this.data. The ArrayList of Msg contain each messages subject and flags.
	 */
    public ArrayList<Msg> getSubjects(){
    	return this.data;
    }
    
    /**
	 * Set the term which you can process mail by i.e. only present mail that matches term.
	 * @param searchTerm. The given string from user that emails must contain in body/subject to be displayed.
	 */
    public void setSearchTerm(String searchTerm){
    	this.searchTerm=searchTerm;
    }        
}