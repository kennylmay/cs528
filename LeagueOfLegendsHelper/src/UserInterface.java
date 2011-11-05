import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class UserInterface extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton okButton = new JButton("Help Me Find Items");
	private JButton clearButton = new JButton("Clear the Form");
	private JLabel playerLabel = new JLabel("Player: ");
	private JLabel opponentLabel = new JLabel("Opponent: ");
	private JLabel goldLabel = new JLabel("Gold (0) = N/A:");
	private JLabel textLabel = new JLabel(
			"What is a simple goal of your build?");

	private JTextArea textBox = new JTextArea(4, 20);
	private JComboBox playerCombo = new JComboBox();
	private JComboBox opponentCombo = new JComboBox();
	SpinnerModel Spinner = new SpinnerNumberModel(0, 0, 10000, 100);
	private JSpinner goldSpinner = new JSpinner(Spinner);
	private JScrollPane scroller;
	private JScrollPane scroller2;

	// Creating the border layout
	private BorderLayout Layout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(3, 3);
	private BorderLayout borderLayout2 = new BorderLayout();
	private BorderLayout borderLayout3 = new BorderLayout();
	private GridLayout gridLayout4 = new GridLayout(2, 1);
	private BorderLayout tableLayout = new BorderLayout();
	private BorderLayout playerLayout = new BorderLayout();
	private BorderLayout opponentLayout = new BorderLayout();
	private BorderLayout goldLayout = new BorderLayout();

	// creating panels
	private JPanel Panel = new JPanel(Layout);
	private JPanel gridPanel = new JPanel(gridLayout);
	private JPanel borderPanel2 = new JPanel(borderLayout2);
	private JPanel borderPanel3 = new JPanel(borderLayout3);
	private JPanel gridPanel4 = new JPanel(gridLayout4);
	private JPanel tablePanel = new JPanel(tableLayout);
	
	@SuppressWarnings("unused")
	private JPanel playerPanel = new JPanel(playerLayout);
	@SuppressWarnings("unused")
	private JPanel opponentPanel = new JPanel(opponentLayout);
	@SuppressWarnings("unused")
	private JPanel goldPanel = new JPanel(goldLayout);

	DefaultTableModel model = new DefaultTableModel();
	JTable outputTable = new JTable(model) {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	

 	
	private CaseData caseData = new CaseData();
	@SuppressWarnings("unused")
	private HashMap<String, LoLItem> itemList;

	@SuppressWarnings("unchecked")
	public UserInterface(final HashMap<String, LoLItem> items,
			HashMap<String, String[]> characters,
			final HashMap<String, String[]> itemTree) {

		// Set the default dimension of the node attributes window

		this.setPreferredSize(new Dimension(750, 750));
		final HashMap<String, LoLItem> Items = items;
		final HashMap<String, String[]> Characters = characters;

		gridLayout.setVgap(3);
		gridLayout.setHgap(10);
		itemList = items;
		scroller = new JScrollPane(textBox);
		scroller2 = new JScrollPane(outputTable,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		String none = "";
		// Set the model columns
		model.addColumn("Item");
		model.addColumn("Description");
		
		outputTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		// Populate the Character Combo Boxes
		playerCombo.addItem(none);
		opponentCombo.addItem(none);

		Collection<String> coll2 = characters.keySet();
		for (String character : coll2) {
			playerCombo.addItem(character);
			opponentCombo.addItem(character);
		}

		// Set the Alignment for the objects.
		getContentPane().add(Panel);
		gridPanel.add(playerLabel);
		gridPanel.add(playerCombo);
		gridPanel.add(opponentLabel);
		gridPanel.add(opponentCombo);
		gridPanel.add(goldLabel);
		gridPanel.add(goldSpinner);
	
		borderPanel2.add(textLabel, BorderLayout.NORTH);
		borderPanel2.add(scroller, BorderLayout.SOUTH);
		borderPanel3.add(okButton, BorderLayout.EAST);
		borderPanel3.add(clearButton, BorderLayout.WEST);

		tablePanel.add(borderPanel3, BorderLayout.NORTH);
		tablePanel.add(scroller2, BorderLayout.SOUTH);
		
		Panel.add(gridPanel, BorderLayout.NORTH);
		Panel.add(borderPanel2, BorderLayout.CENTER);
		Panel.add(tablePanel, BorderLayout.SOUTH);

		// Panel.add(okButton, BorderLayout.SOUTH);

		// Display the Panel
		this.pack();
		this.setModal(true);
		this.setAlwaysOnTop(false);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get data from the form about the current player type.
				caseData.put("playerChampionRole",
						Characters.get(playerCombo.getSelectedItem()));
				// Get data from the form about the opponent player type
				caseData.put("opponentChampionRole",
						Characters.get(opponentCombo.getSelectedItem()));
				// Get data from the form on the gold value
				caseData.put("Gold", goldSpinner.getValue());
			
				caseData.put("playerGoal", textBox.getText());

				LanguageProcessor nlp = new LanguageProcessor();
				LinkedList<Token> masterLL = new LinkedList<Token>();
				LinkedList<Token> ll = GUIProcessor.buildTokenList(caseData, Items);
				LinkedList<Token> ll2 = nlp.askQuestion(textBox.getText());

				if(ll != null){
					masterLL.addAll(ll);
				}else if(ll2 != null){
					masterLL.addAll(ll2);
				}else{
					return;
				}

				
				if (!Token.isValidGrammar(masterLL)) {
					System.out.println("Bad grammar: " + masterLL);
					return;
				}
				
				ItemRule r = Token.tokens2ItemRule(masterLL);
				
				LinkedList<LoLItem> items = new LinkedList<LoLItem>();
				for (String s : Items.keySet()) {
					
					LoLItem item = Items.get(s);	
					if(r.eval(item)){
						items.add(item);
								
					}
				}
				updateTable(items);
				System.out.println(r);
			}

		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textBox.setText("");
				opponentCombo.setSelectedIndex(0);
				playerCombo.setSelectedIndex(0);
				goldSpinner.setValue(0);
				while (model.getRowCount() > 0) {
					model.removeRow(0);
				}
				resizeTable();
			}
		});
		resizeTable();

	}

	public void resizeTable() {
		updateTable(new LinkedList<LoLItem>());
	}
	
	public void showDialog() {
		this.setVisible(true);
	}

	private void updateTable(LinkedList<LoLItem> items) {
		// Clean the table out to refresh the table
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		int maxCol1Width = Integer.MIN_VALUE, maxCol2Width = Integer.MIN_VALUE;
		String maxCol1 = ""; String maxCol2 = "";
		for (LoLItem lolitem : items) {
			
			String col1 = (String) lolitem.get("Item");
			String col2 = (String) lolitem.get("Description");
			
			if(col1.length() > maxCol1Width) {
				maxCol1Width = col1.length();
				maxCol1 = col1;
			}
			if(col2.length() > maxCol2Width) {
				maxCol2Width = col2.length();
				maxCol2 = col2;
			}
			
			model.addRow(new String[] { col1, col2});
		}

		String header1 =(String)outputTable.getColumnModel().getColumn(0).getHeaderValue();
		String header2 =(String)outputTable.getColumnModel().getColumn(1).getHeaderValue();
		if(header1.length() > maxCol1Width) {
			maxCol1Width = header1.length();
			maxCol1 = header1;
		}
		if(header2.length() > maxCol2Width) {
			maxCol2Width = header2.length();
			maxCol2 = header2;
		}
	
		
		FontMetrics fm = outputTable.getFontMetrics(outputTable.getFont());
		outputTable.getColumnModel().getColumn(0).setPreferredWidth(fm.stringWidth(maxCol1) + 20);
		outputTable.getColumnModel().getColumn(1).setPreferredWidth(fm.stringWidth(maxCol2) + 20);
		return;
	}
}
