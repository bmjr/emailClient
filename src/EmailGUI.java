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
import java.awt.Font;
import javax.swing.JScrollPane;

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
	 * Method to set the components up for EmailGUI
	 * @param username. The username of the email account.
	 * @param password. The password of the email account.
	 */
	public EmailGUI(String username, String password) {
		final EmailGUI gui = this;
		this.username=username;
		this.password=password;
		attachmentList=new ArrayList<String>();
		createEmail(username,password);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textTo = new JTextField();
		textTo.setBounds(37, 6, 270, 28);
		contentPane.add(textTo);
		textTo.setColumns(10);
		
		textCC = new JTextField();
		textCC.setBounds(37, 40, 270, 28);
		contentPane.add(textCC);
		textCC.setColumns(10);
		
		textSubject = new JTextField();
		textSubject.setBounds(37, 90, 270, 28);
		contentPane.add(textSubject);
		textSubject.setColumns(10);
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
					lblAttachments.setText(attachmentCount+" attachment(s)");
				}
				
				
			}
		});
		btnAddAttachments.setBounds(301, 90, 143, 29);
		contentPane.add(btnAddAttachments);
		
		
		JButton btnSend = new JButton("SEND");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (textTo.getText()!=null){
				sendEmail();
				gui.dispose();
				}
			}
		});
		btnSend.setBounds(374, 243, 70, 29);
		contentPane.add(btnSend);
		
		lblAttachments = new JLabel("0 attachments");
		lblAttachments.setBounds(36, 248, 102, 16);
		contentPane.add(lblAttachments);
		
		JLabel lblTo = new JLabel("TO:");
		lblTo.setBounds(16, 12, 61, 16);
		contentPane.add(lblTo);
		
		JLabel lblCc = new JLabel("CC:");
		lblCc.setBounds(16, 46, 61, 16);
		contentPane.add(lblCc);
		
		JLabel lblSubject = new JLabel("SUBJECT");
		lblSubject.setFont(new Font("Lucida Grande", Font.PLAIN, 10));
		lblSubject.setBounds(0, 97, 61, 16);
		contentPane.add(lblSubject);
		
		textBody = new JTextArea();
		textBody.setLineWrap(true);
		
		
		JScrollPane scrollPane = new JScrollPane(textBody);
		scrollPane.setBounds(16, 130, 418, 109);
		contentPane.add(scrollPane);
		
		
	}
	
	/**
	 * Method to initialise the mimeMessage object so it's ready to be used
	 * @param username. The username of the email account.
	 * @param password. The password of the email account.
	 */
	private void createEmail(String username, String password){
		
		this.smtphost = "smtp.gmail.com";

		Properties props = System.getProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", smtphost);
		props.put("mail.smtp.port", "587");

		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		this.session = Session.getDefaultInstance(props);
		message = new MimeMessage(session);
	}
	
	/**
	 * Send email created by user within the GUI.
	 */
	private void sendEmail(){

				try {

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

							
					Transport tr = session.getTransport("smtp");
					tr.connect(smtphost, username, password); 
					tr.sendMessage(message, message.getAllRecipients());

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			}
}