import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import javax.swing.JSpinner;
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
	private JLabel playerItemList = new JLabel("Player Items: ");
	private JLabel opponentItemList = new JLabel("Opponent Items: ");
	private JLabel textLabel = new JLabel(
			"What is a simple goal of your build?");

	private JTextField textBox = new JTextField();
	private JComboBox playerItem1 = new JComboBox();
	private JComboBox playerItem2 = new JComboBox();
	private JComboBox playerItem3 = new JComboBox();
	private JComboBox playerItem4 = new JComboBox();
	private JComboBox playerItem5 = new JComboBox();
	private JComboBox playerItem6 = new JComboBox();
	private JComboBox opponentItem1 = new JComboBox();
	private JComboBox opponentItem2 = new JComboBox();
	private JComboBox opponentItem3 = new JComboBox();
	private JComboBox opponentItem4 = new JComboBox();
	private JComboBox opponentItem5 = new JComboBox();
	private JComboBox opponentItem6 = new JComboBox();

	private JComboBox playerCombo = new JComboBox();
	private JComboBox opponentCombo = new JComboBox();
	SpinnerModel Spinner = new SpinnerNumberModel(0, 0, 10000, 100);
	private JSpinner goldSpinner = new JSpinner(Spinner);

	// Creating the border layout
	private BorderLayout Layout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(10, 2);
	private GridLayout gridLayout2 = new GridLayout(2, 1);
	private GridLayout gridLayout3 = new GridLayout(1, 2);
	private BorderLayout playerLayout = new BorderLayout();
	private BorderLayout opponentLayout = new BorderLayout();
	private BorderLayout goldLayout = new BorderLayout();

	// creating panels
	private JPanel Panel = new JPanel(Layout);
	private JPanel gridPanel = new JPanel(gridLayout);
	private JPanel gridPanel2 = new JPanel(gridLayout2);
	private JPanel gridPanel3 = new JPanel(gridLayout3);
	@SuppressWarnings("unused")
	private JPanel playerPanel = new JPanel(playerLayout);
	@SuppressWarnings("unused")
	private JPanel opponentPanel = new JPanel(opponentLayout);
	@SuppressWarnings("unused")
	private JPanel goldPanel = new JPanel(goldLayout);

	DefaultTableModel model = new DefaultTableModel();

	private CaseData caseData = new CaseData();
	private HashMap<String, LoLItem> itemList;

	@SuppressWarnings("unchecked")
	public UserInterface(HashMap<String, LoLItem> items,
			HashMap<String, String[]> characters,
			final HashMap<String, String[]> itemTree) {

		// Set the default dimension of the node attributes window

		this.setPreferredSize(new Dimension(500, 500));
		final HashMap<String, LoLItem> Items = items;
		final HashMap<String, String[]> Characters = characters;
		final HashMap<String, String[]> ItemTree = itemTree;

		gridLayout.setVgap(3);
		gridLayout.setHgap(10);
		gridLayout3.setHgap(30);
		itemList = items;
		String none = "";

		// / Populate the Item Combo Boxes
		playerItem1.addItem(none);
		playerItem2.addItem(none);
		playerItem3.addItem(none);
		playerItem4.addItem(none);
		playerItem5.addItem(none);
		playerItem6.addItem(none);
		opponentItem1.addItem(none);
		opponentItem2.addItem(none);
		opponentItem3.addItem(none);
		opponentItem4.addItem(none);
		opponentItem5.addItem(none);
		opponentItem6.addItem(none);

		Collection<String> coll = items.keySet();
		for (String item : coll) {
			playerItem1.addItem(item);
			playerItem2.addItem(item);
			playerItem3.addItem(item);
			playerItem4.addItem(item);
			playerItem5.addItem(item);
			playerItem6.addItem(item);
			opponentItem1.addItem(item);
			opponentItem2.addItem(item);
			opponentItem3.addItem(item);
			opponentItem4.addItem(item);
			opponentItem5.addItem(item);
			opponentItem6.addItem(item);
		}

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

		gridPanel.add(playerItemList);
		gridPanel.add(opponentItemList);
		gridPanel.add(playerItem1);
		gridPanel.add(opponentItem1);
		gridPanel.add(playerItem2);
		gridPanel.add(opponentItem2);
		gridPanel.add(playerItem3);
		gridPanel.add(opponentItem3);
		gridPanel.add(playerItem4);
		gridPanel.add(opponentItem4);
		gridPanel.add(playerItem5);
		gridPanel.add(opponentItem5);
		gridPanel.add(playerItem6);
		gridPanel.add(opponentItem6);

		gridPanel2.add(textLabel);
		gridPanel2.add(textBox);
		gridPanel3.add(okButton);
		gridPanel3.add(clearButton);

		Panel.add(gridPanel, BorderLayout.NORTH);
		Panel.add(gridPanel2, BorderLayout.CENTER);
		Panel.add(gridPanel3, BorderLayout.SOUTH);

		// Panel.add(okButton, BorderLayout.SOUTH);

		// Display the Panel
		this.pack();
		this.setModal(true);
		this.setAlwaysOnTop(false);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get data from the form about the current player type.
				String userInput[] = Characters.get(playerCombo
						.getSelectedItem());
				if (userInput != null) {
					for (String userAttribute : userInput) {
						if (userAttribute.equalsIgnoreCase("assassin")) {
							caseData.put("playerChampionRole",
									Expert.ChampionRole.Assasin);
							break;
						} else if (userAttribute.equalsIgnoreCase("tank")) {
							caseData.put("playerChampionRole",
									Expert.ChampionRole.Tank);
							break;
						} else if (userAttribute.equalsIgnoreCase("mage")) {
							caseData.put("playerChampionRole",
									Expert.ChampionRole.Mage);
							break;
						} else if (userAttribute.equalsIgnoreCase("figher")) {
							caseData.put("playerChampionRole",
									Expert.ChampionRole.Fighter);
							break;
						} else if (userAttribute.equalsIgnoreCase("support")) {
							caseData.put("playerChampionRole",
									Expert.ChampionRole.Support);
							break;
						}
					}
				}

				// Get data from the form about the opponent player type
				String userInput2[] = Characters.get(opponentCombo
						.getSelectedItem());
				if (userInput2 != null) {
					for (String userAttribute : userInput2) {
						if (userAttribute.equalsIgnoreCase("assassin")) {
							caseData.put("opponentChampionRole",
									Expert.ChampionRole.Assasin);
							break;
						} else if (userAttribute.equalsIgnoreCase("tank")) {
							caseData.put("opponentChampionRole",
									Expert.ChampionRole.Tank);
							break;
						} else if (userAttribute.equalsIgnoreCase("mage")) {
							caseData.put("opponentChampionRole",
									Expert.ChampionRole.Mage);
							break;
						} else if (userAttribute.equalsIgnoreCase("fighter")) {
							caseData.put("opponentChampionRole",
									Expert.ChampionRole.Fighter);
							break;
						} else if (userAttribute.equalsIgnoreCase("support")) {
							caseData.put("opponentChampionRole",
									Expert.ChampionRole.Support);
							break;
						}
					}
				}
				// Get data from the form on the gold value
				caseData.put("Gold", goldSpinner.getValue());

				// Get an item list for the player from the form.
				LinkedList<LoLItem> lolItems = new LinkedList<LoLItem>();
				if (!playerItem1.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem1.getSelectedItem()
							.toString()));
				}
				if (!playerItem2.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem2.getSelectedItem()
							.toString()));
				}
				if (!playerItem3.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem3.getSelectedItem()
							.toString()));
				}
				if (!playerItem4.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem4.getSelectedItem()
							.toString()));
				}
				if (!playerItem5.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem5.getSelectedItem()
							.toString()));
				}
				if (!playerItem6.getSelectedItem().toString().isEmpty()) {
					lolItems.add(itemList.get(playerItem6.getSelectedItem()
							.toString()));
				}

				caseData.put("playerItems", lolItems);

				// Get an item list for the opponent from the form.
				LinkedList<LoLItem> oplolItems = new LinkedList<LoLItem>();
				if (!opponentItem1.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem1.getSelectedItem()
							.toString()));
				}
				if (!opponentItem2.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem2.getSelectedItem()
							.toString()));
				}
				if (!opponentItem3.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem3.getSelectedItem()
							.toString()));
				}
				if (!opponentItem4.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem4.getSelectedItem()
							.toString()));
				}
				if (!opponentItem5.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem5.getSelectedItem()
							.toString()));
				}
				if (!opponentItem6.getSelectedItem().toString().isEmpty()) {
					oplolItems.add(itemList.get(opponentItem6.getSelectedItem()
							.toString()));
				}

				caseData.put("opponentItems", oplolItems);
				caseData.put("playerGoal", textBox.getText());
				
				LanguageProcessor nlp = new LanguageProcessor();
				LinkedList<Token> ll = nlp.askQuestion(textBox.getText());
				ItemRule r = Token.tokens2ItemRule(ll);
				String tempstring = null;
			
				for(String s : Items.keySet()) {
					LoLItem item = Items.get(s);
					if(r.eval(item)) {
						Vector<String> items = LeagueOfLegendsHelper.getAllChildren((String) item.get("Item"));
						tempstring = "ITEM: " + item.get("Item") + "\n";
						if (items != null){
						   tempstring += "BUILT FROM:\n";
						   for(String it : items){
							   tempstring += "\t" + it +"\n";
						   }
						}
						System.out.print(tempstring + "\n");
					}
					
				}
				
				System.out.println(r);
				
				//Expert.suggestNextItem(caseData, Items, ItemTree);
			}

		});

		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textBox.setText("");
				opponentCombo.setSelectedIndex(0);
				playerCombo.setSelectedIndex(0);
				playerItem1.setSelectedIndex(0);
				playerItem2.setSelectedIndex(0);
				playerItem3.setSelectedIndex(0);
				playerItem4.setSelectedIndex(0);
				playerItem5.setSelectedIndex(0);
				playerItem6.setSelectedIndex(0);
				opponentItem1.setSelectedIndex(0);
				opponentItem2.setSelectedIndex(0);
				opponentItem3.setSelectedIndex(0);
				opponentItem4.setSelectedIndex(0);
				opponentItem5.setSelectedIndex(0);
				opponentItem6.setSelectedIndex(0);
				goldSpinner.setValue(0);
			}
		});

	}

	public void showDialog() {
		this.setVisible(true);
	}
}
