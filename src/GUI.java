import java.io.IOException;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textBody;
	private JTextArea textFrom;
	public Boolean[] flags;
	private Inbox inbox;
	private Message[] messages;
	private ArrayList<Msg> subjects;
	private  JPanel panel;
	private JTextField textSearch;
	private JTextField textPhrase;
	private JTextField textFlag;
	private String username;
	private String password;

	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 * @param username. The username of the email account.
	 * @param password. The password of the email account.
	 */
	public GUI(String username, String password) {
		this.username=username;
		this.password=password;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(true);
		paintGUI();
	}
	
	/**
	 * Create all the components of GUI, hide those that don't need to be initially visible
	 */
	private void paintGUI(){
		final GUI gui = this;

		JLabel lblTitle = new JLabel("Inbox");
		lblTitle.setBounds(500, 12, 61, 16);
		contentPane.add(lblTitle, "push, align center" );
		
		panel = new JPanel();
		panel.setBounds(320, 50, 500, 240);
		panel.setVisible(false);
		contentPane.add(panel);
		
		textFrom = new JTextArea();
		textFrom.setEditable(false);
		textFrom.setBounds(63, 0, 428, 23);
		panel.add(textFrom);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(21, 7, 51, 16);
		panel.add(lblFrom);
		
		JLabel lblMessage = new JLabel("MESSAGE:");
		lblMessage.setBounds(0, 73, 61, 16);
		panel.add(lblMessage);
		
		textBody = new JTextArea();
		textBody.setEditable(false);
		textBody.setLineWrap(true);
		textBody.setBounds(0, 90, 491, 216);
		panel.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane(textBody);
		scrollPane.setBounds(0, 90, 491, 216);
		panel.add(scrollPane);
		
		
		JButton btnRefreshInbox = new JButton("Refresh Inbox");
		btnRefreshInbox.setFocusPainted(false);
		btnRefreshInbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					contentPane.removeAll();
					panel.setVisible(false);
					inbox.setSearchTerm(null);
					inbox.processMail();
					gui.setMessages(inbox.getMessages(),inbox.getSubjects());
					paintGUI();
					gui.setInboxTable();
				} catch (MessagingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnRefreshInbox.setBounds(6, 7, 117, 29);
		contentPane.add(btnRefreshInbox);
		final GUI inboxGUI = this;
		JButton btnCompose = new JButton("Compose");
		btnCompose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailGUI compose = new EmailGUI(inboxGUI.username,inboxGUI.password);
				compose.setVisible(true);
			}
		});
		btnCompose.setBounds(122, 7, 117, 29);
		contentPane.add(btnCompose);
		
		textSearch = new JTextField();
		textSearch.setBounds(6, 324, 134, 28);
		contentPane.add(textSearch);
		textSearch.setColumns(10);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					inbox.setSearchTerm(textSearch.getText());
					inbox.processMail();
					gui.setMessages(inbox.getMessages(),inbox.getSubjects());
					contentPane.removeAll();
					paintGUI();
					gui.setInboxTable();
				} catch (MessagingException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(145, 325, 117, 29);
		contentPane.add(btnSearch);
		
		textPhrase = new JTextField();
		textPhrase.setBounds(320, 312, 134, 28);
		contentPane.add(textPhrase);
		textPhrase.setColumns(10);
		
		textFlag = new JTextField();
		textFlag.setBounds(320, 350, 134, 28);
		contentPane.add(textFlag);
		textFlag.setColumns(10);
		
		JButton btnFlag = new JButton("Create Flag");
		btnFlag.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(textFlag.getText()!=null){
					inbox.setSearchTerm(textPhrase.getText());
					try {
						inbox.processMail();
					} catch (MessagingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Message[] messagesToFlag = inbox.getMessages();
					Flags processedFlag = new Flags(textFlag.getText());
					for (Message message : messagesToFlag){
						
						try {
							message.setFlags(processedFlag, true);
						} catch (MessagingException e) {
							e.printStackTrace();
						}
					}
		
					try {
						inbox.setSearchTerm(null);
						inbox.processMail();
						gui.setMessages(inbox.getMessages(),inbox.getSubjects());
						contentPane.removeAll();
						paintGUI();
						gui.setInboxTable();
					} catch (MessagingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				}
			
		});
		btnFlag.setBounds(455, 313, 117, 59);
		contentPane.add(btnFlag);
		
		JLabel lblPhraseToFlag = new JLabel("Phrase to flag");
		lblPhraseToFlag.setBounds(320, 300, 108, 16);
		contentPane.add(lblPhraseToFlag);
		
		JLabel lblNewLabel = new JLabel("Flag name");
		lblNewLabel.setBounds(320, 337, 71, 16);
		contentPane.add(lblNewLabel);
	
	}
	
	/**
	 * Initialise the inbox table component with emails
	 * If search term specified only display emails that match search term
	 * Else display all emails in inbox
	 */
	public void setInboxTable(){
		String col[] = {"Subject","Flag"};

		DefaultTableModel tableModel = new DefaultTableModel(col, 0){
			
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int i, int i1) {
		        return false; 
		    }
		
		};
		                                            
		final JTable tablem = new JTable(tableModel);
		tablem.setBounds(112, 203, 312, 69);
		
		for (int i = 0; i < subjects.size(); i++){
			String read = subjects.get(i).getSeen();
			String subject = subjects.get(i).getSubject();
			
			Object[] data = {subject,read};
			tableModel.addRow(data);
		}
		final JScrollPane scrollPane = new JScrollPane(tablem);
		scrollPane.setBounds(5, 50, 990, 250);
		contentPane.add(scrollPane);
		
		tablem.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	        	try{
	        		String plaintext="";
	        		if(messages[tablem.getSelectedRow()].getContentType().contains("TEXT/PLAIN")) {
	        			
	    	            plaintext=((String) messages[tablem.getSelectedRow()].getContent());
					}
					else 
					{
						
						Multipart multipart = (Multipart) messages[tablem.getSelectedRow()].getContent();
						
						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);

							if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
							{
								plaintext=plaintext+bodyPart.getContent().toString();
							}

						}
					}
	        	textFrom.setText((String) messages[tablem.getSelectedRow()].getFrom()[0].toString());
    	        textBody.setText(plaintext);
	            scrollPane.setBounds(5, 50, 300,250 );
	            panel.setVisible(true);
	        	} catch (IOException e) {
					e.printStackTrace();
				} catch (MessagingException e) {
					e.printStackTrace();
				}
	        	String flags = (tablem.getValueAt(tablem.getSelectedRow(), 1)).toString();
	        	String[] seen = flags.split(",");
	        	String newFlagString = "READ,";
	        	for(int i=1;i<seen.length;i++){
	        		   newFlagString+=seen[i].toString()+",";
	        	   }
	        	   tablem.setValueAt(newFlagString, tablem.getSelectedRow(), 1);
	           }
	            	
	        
	    });
	}
	
	/**
	 * Set inbox object in this object
	 */
	public void setInbox(Inbox inbox) {
		this.inbox=inbox;
	}
	
	/**
	 * Set message array in this object
	 */
	public void setMessages(Message[] messages, ArrayList<Msg> subjects) {
		this.messages=messages;
		this.subjects=subjects;
		
	}
}


