package chatbot;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class UserInterface extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton okButton = new JButton("Send");
	private JTextArea chatArea = new JTextArea(4, 20);
	private JTextArea inputArea = new JTextArea(1, 20);
	private JScrollPane scroller2;

    public static void standardBorder(JPanel jp) {
      jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }


	public UserInterface() {

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setPreferredSize(new Dimension(600, 400));

		JPanel top = new JPanel();
		BorderLayout layout = new BorderLayout();
		top.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
		
		layout.setHgap(10);
		layout.setVgap(10);
		top.setLayout(layout);
		
		
		
		JPanel chatPanel = new JPanel(new BorderLayout());
		standardBorder(chatPanel);
		chatPanel.add(chatArea);
		top.add(chatPanel);
		
	    inputArea.getDocument().addDocumentListener(
				  new DocumentListener() {
				    public void insertUpdate(DocumentEvent e) {
				      
				    }
				    public void removeUpdate(DocumentEvent e) {
				    }
				    public void changedUpdate(DocumentEvent e) {
				      // watch for newline/enter here
				    }

				  });
				
		JPanel inputPanel = new JPanel(new BorderLayout());
		standardBorder(inputPanel);
		inputPanel.add(inputArea);
		top.add(inputPanel, BorderLayout.SOUTH);
		
	
		add(top);
		// Display the Panel
		this.pack();
		this.setAlwaysOnTop(false);
		this.setVisible(true);
	}

}
