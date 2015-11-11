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
    
    public Inbox(Store store){
        this.store=store;
    }
     
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
            				if ((message.getSubject().contains(searchTerm) || message.getContent().toString().contains(searchTerm))) {
            					return true;
            				}
            				
            			} catch (MessagingException ex) {
            				ex.printStackTrace();
            			} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			return false;
            		}
            	};
            	this.messages = folder.search(term);
            }
            System.out.println("message count "+folder.getMessageCount());

            ArrayList<Boolean> read = new ArrayList<Boolean>();
            this.data = new ArrayList<Msg>();

    		for(Message message:messages) {
    			
    				String seen;
    				String subject;
    				
    				Flags flag = message.getFlags();
    				if ((flag.contains(Flag.SEEN))){
    					read.add(Boolean.TRUE);
    					seen="READ";
    				}
    				else{
    					seen="UNREAD";
    					message.setFlag(Flags.Flag.SEEN, false);
    					read.add(Boolean.FALSE);
    					System.out.println("size "+read.size());
    				}
					
					subject=message.getSubject();
					
					
					data.add(new Msg(subject,seen));

				}
      
    		
    	

        }catch (NoSuchProviderException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    

    
    
    
    public Message[] getMessages(){
    	return this.messages;
    }
    
    public ArrayList<Msg> getSubjects(){
    	return this.data;
    }

	public void updateFlag(int index) {
		// TODO Auto-generated method stub
        try{
            folder = (IMAPFolder) this.store.getFolder("inbox"); 
     
            // Step 4: Open the folder
            if(!folder.isOpen())
                folder.open(Folder.READ_WRITE);
            Message messages[] = folder.getMessages();
            int count=0;
            for(Message message:messages) {
    			if(message.getContentType().contains("TEXT/PLAIN")) {
    				if(count==index){
    					message.setFlag(Flags.Flag.SEEN, true);
    					flags[count]=true;
    				}
    			count++;
    			}
    		}
         } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        finally{}
	}
       
    public void setSearchTerm(String searchTerm){
    	this.searchTerm=searchTerm;
    }
            
}
