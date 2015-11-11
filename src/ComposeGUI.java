import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FileDialog;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ComposeGUI extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ComposeGUI frame = new ComposeGUI();
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
	public ComposeGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblCompose = new JLabel("COMPOSE");
		lblCompose.setBounds(5, 5, 440, 16);
		contentPane.add(lblCompose);
		
		JFormattedTextField formattedTextField = new JFormattedTextField();
		formattedTextField.setBounds(33, 29, 399, 28);
		contentPane.add(formattedTextField);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(33, 126, 399, 146);
		contentPane.add(textArea);
		
		JFormattedTextField formattedTextField_1 = new JFormattedTextField();
		formattedTextField_1.setBounds(33, 69, 399, 28);
		contentPane.add(formattedTextField_1);
		final FileDialog fd = new FileDialog(this, "Choose a file", FileDialog.LOAD);
		JButton btnAddAttachment = new JButton("Add Attachment");
		btnAddAttachment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fd.setDirectory("C:\\");
				fd.setFile("*.xml");
				fd.setVisible(true);
				String filename = fd.getFile();
				if (filename == null)
				  System.out.println("You cancelled the choice");
				else
				  System.out.println("You chose " + filename);
			}
		});
		btnAddAttachment.setBounds(292, 97, 141, 29);
		contentPane.add(btnAddAttachment);
	}
}
