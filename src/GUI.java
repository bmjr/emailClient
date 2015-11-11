import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
	//private String[] subjects = {"hello","bye"};
	/**
	 * Launch the application.
	 */


	/**
	 * Create the frame.
	 * @param messages 
	 * @throws MessagingException 
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setVisible(true);
		paintGUI();
	}
	
	private void paintGUI(){
		System.out.println(contentPane.getWidth());
		JLabel lblTitle = new JLabel("Inbox");
		lblTitle.setBounds(500, 12, 61, 16);
		contentPane.add(lblTitle, "push, align center" );
		
		panel = new JPanel();
		panel.setBounds(320, 50, 500, 250);
		panel.setVisible(false);
		contentPane.add(panel);
		
		textFrom = new JTextArea();
		textFrom.setBounds(63, 0, 428, 23);
		
		textBody = new JTextArea();
		textBody.setLineWrap(true);
		textBody.setBounds(0, 90, 491, 216);
		panel.setLayout(null);
		panel.add(textFrom);
		panel.add(textBody);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(63, 33, 428, 23);
		panel.add(textArea);
		
		JLabel lblFrom = new JLabel("FROM:");
		lblFrom.setBounds(21, 7, 51, 16);
		panel.add(lblFrom);
		
		JLabel lblSubject = new JLabel("SUBJECT:");
		lblSubject.setBounds(0, 40, 61, 16);
		panel.add(lblSubject);
		
		JLabel lblMessage = new JLabel("MESSAGE:");
		lblMessage.setBounds(0, 73, 61, 16);
		panel.add(lblMessage);
		final GUI gui = this;
		JButton btnRefreshInbox = new JButton("Refresh Inbox");
		btnRefreshInbox.setFocusPainted(false);
		btnRefreshInbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					inbox.setSearchTerm(null);
					inbox.processMail();
					gui.setMessages(inbox.getMessages(),inbox.getSubjects());
					contentPane.removeAll();
					paintGUI();
					gui.setInboxTable();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnRefreshInbox.setBounds(6, 7, 117, 29);
		contentPane.add(btnRefreshInbox);
		
		JButton btnCompose = new JButton("Compose");
		btnCompose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailGUI compose = new EmailGUI();
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
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSearch.setBounds(145, 325, 117, 29);
		contentPane.add(btnSearch);
	
	}
	
	public void setInboxTable(){
		String col[] = {"Subject","Flag"};

		DefaultTableModel tableModel = new DefaultTableModel(col, 0){
			
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int i, int i1) {
		        return false; //To change body of generated methods, choose Tools | Templates.
		    }
		
		};
		                                            // The 0 argument is number rows.
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
						// How to get parts from multiple body parts of MIME message
						Multipart multipart = (Multipart) messages[tablem.getSelectedRow()].getContent();
						
						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);
							// If the part is a plan text message, then print it out.
							if(bodyPart.getContentType().contains("TEXT/PLAIN")) 
							{
								System.out.println(bodyPart.getContentType());
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
	        	finally{}
	           if ((tablem.getValueAt(tablem.getSelectedRow(), 1)=="UNREAD")){
	        	   tablem.setValueAt("READ", tablem.getSelectedRow(), 1);
	           }
	            	//updateFlag(tablem.getSelectedRow());
	             // }
	        }
	    });
	}
	
	
	public void setInbox(Inbox inbox) {
		this.inbox=inbox;
	}
	
	public void updateFlag(int index){
		this.inbox.updateFlag(index);
	}

	public void setMessages(Message[] messages, ArrayList<Msg> subjects) {
		this.messages=messages;
		this.subjects=subjects;
		
	}
}


