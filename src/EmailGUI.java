import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class EmailGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textTo;
	private JTextField textCC;
	private JTextField textSubject;
	private JTextArea textBody;
	private JLabel lblAttachments;
	private MimeMessage message;
	private String username;
	private String password;
	private String smtphost;
	private Session session;
	private int attachmentCount=0;
	private ArrayList<String >attachmentList;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmailGUI frame = new EmailGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EmailGUI() {
		final EmailGUI gui = this;
		attachmentList=new ArrayList<String>();
		createEmail();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textTo = new JTextField();
		textTo.setBounds(37, 19, 270, 28);
		contentPane.add(textTo);
		textTo.setColumns(10);
		
		textCC = new JTextField();
		textCC.setBounds(37, 50, 270, 28);
		contentPane.add(textCC);
		textCC.setColumns(10);
		
		textSubject = new JTextField();
		textSubject.setBounds(37, 90, 270, 28);
		contentPane.add(textSubject);
		textSubject.setColumns(10);
		
		textBody = new JTextArea();
		textBody.setBounds(37, 130, 407, 108);
		contentPane.add(textBody);
		final FileDialog fd = new FileDialog(gui, "Select an attachment", FileDialog.LOAD);
		fd.setDirectory("C:\\");
		fd.setFile("*.xml");
		JButton btnAddAttachments = new JButton("Add Attachments");
		btnAddAttachments.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fd.setVisible(true);
				String filename=fd.getFile();
				if (filename != null){
					attachmentList.add(fd.getDirectory()+fd.getFile());
					attachmentCount++;
					//lblAttachments.setText(attachmentCount+" attachment(s)");
				}
				
				
			}
		});
		btnAddAttachments.setBounds(301, 90, 143, 29);
		contentPane.add(btnAddAttachments);
		
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendEmail();
				gui.dispose();
			}
		});
		btnSend.setBounds(374, 243, 70, 29);
		contentPane.add(btnSend);
		
		lblAttachments = new JLabel("0 attachments");
		lblAttachments.setBounds(36, 248, 102, 16);
		contentPane.add(lblAttachments);
	}
	
	private void createEmail(){
		this.username = "bmjrowe@gmail.com";
		this.password = "/";
		this.smtphost = "smtp.gmail.com";

		// Step 1: Set all Properties
		// Get system properties
		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");

		 

		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		//Step 2: Establish a mail session (java.mail.Session)
		this.session = Session.getDefaultInstance(props);
		message = new MimeMessage(session);
	}
	
	private void sendEmail(){
		// TODO Auto-generated method stub
				
				try {
					
					// Step 3: Create a message
					message.setFrom(new InternetAddress("bmjrowe@gmail.com"));
					message.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(textTo.getText()));
					message.addRecipients(RecipientType.CC, InternetAddress.parse(textCC.getText()));
					message.setSubject(textSubject.getText());
					
					Multipart multipart = new MimeMultipart("mixed");
					MimeBodyPart text = new MimeBodyPart();
					text.setText(textBody.getText());
					multipart.addBodyPart(text);
					for (String path : attachmentList) {
					    MimeBodyPart messageBodyPart = new MimeBodyPart();
					    DataSource source = new FileDataSource(path);
					    messageBodyPart.setDataHandler(new DataHandler(source));
					    messageBodyPart.setFileName(source.getName());
					    multipart.addBodyPart(messageBodyPart);
					}
					message.setContent(multipart);
					message.saveChanges();

					// Step 4: Send the message by javax.mail.Transport .			
					Transport tr = session.getTransport("smtp");	// Get Transport object from session		
					tr.connect(smtphost, username, password); // We need to connect
					tr.sendMessage(message, message.getAllRecipients()); // Send message


					System.out.println("Done");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			}


	}