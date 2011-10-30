import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class UserInterface extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton okButton = new JButton("Help Me Find Items");
	private String[] playerTypes = { "Assassin", "Tank", "Mage", "Fighter",
			"Support" };
	private JLabel playerLabel = new JLabel("Player Type: ");
	private JLabel opponentLabel = new JLabel("Opponent Type: ");
	private JLabel goldLabel = new JLabel("Gold (0) = N/A:");
	private JLabel playerItemList = new JLabel("Player Items: ");
	private JLabel opponentItemList = new JLabel("Opponent Items: ");

	private JComboBox playerItem1 = new JComboBox<String>();
	private JComboBox playerItem2 = new JComboBox<String>();
	private JComboBox playerItem3 = new JComboBox<String>();
	private JComboBox playerItem4 = new JComboBox<String>();
	private JComboBox playerItem5 = new JComboBox<String>();
	private JComboBox playerItem6 = new JComboBox<String>();
	private JComboBox opponentItem1 = new JComboBox<String>();
	private JComboBox opponentItem2 = new JComboBox<String>();
	private JComboBox opponentItem3 = new JComboBox<String>();
	private JComboBox opponentItem4 = new JComboBox<String>();
	private JComboBox opponentItem5 = new JComboBox<String>();
	private JComboBox opponentItem6 = new JComboBox<String>();

	private JComboBox playerCombo = new JComboBox<String>(playerTypes);
	private JComboBox opponentCombo = new JComboBox<String>(playerTypes);
	SpinnerModel Spinner = new SpinnerNumberModel(0, 0, 10000, 100);
	private JSpinner goldSpinner = new JSpinner(Spinner);

	// Creating the border layout
	private BorderLayout Layout = new BorderLayout();
	private GridLayout gridLayout = new GridLayout(10, 2);
	private BorderLayout playerLayout = new BorderLayout();
	private BorderLayout opponentLayout = new BorderLayout();
	private BorderLayout goldLayout = new BorderLayout();

	// creating panels
	private JPanel Panel = new JPanel(Layout);
	private JPanel gridPanel = new JPanel(gridLayout);
	private JPanel playerPanel = new JPanel(playerLayout);
	private JPanel opponentPanel = new JPanel(opponentLayout);
	private JPanel goldPanel = new JPanel(goldLayout);

	DefaultTableModel model = new DefaultTableModel();

	private CaseData caseData = new CaseData();
	private HashMap<String, LoLItem> itemList;

	public UserInterface(HashMap<String, LoLItem> items) {

		// Set the default dimension of the node attributes window
		this.setPreferredSize(new Dimension(500, 500));
		itemList = items;
		String none = "";
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

		Panel.add(gridPanel, BorderLayout.NORTH);
		Panel.add(okButton, BorderLayout.SOUTH);

		// Display the Panel
		this.pack();
		this.setModal(true);
		this.setAlwaysOnTop(false);

		okButton.addActionListener(this);
		
	}

	public CaseData showDialog() {
		this.setVisible(true);
		return caseData;
	}

	public void actionPerformed(ActionEvent arg0) {
		// Get data from the form about the current player type.
		String userInput = (String) playerCombo.getSelectedItem();
		if (userInput.equalsIgnoreCase("assassin")) {
			caseData.put("playerChampionRole",
					Expert.ChampionRole.Assasin);
		} else if (userInput.equalsIgnoreCase("tank")) {
			caseData.put("playerChampionRole", Expert.ChampionRole.Tank);
		} else if (userInput.equalsIgnoreCase("mage")) {
			caseData.put("playerChampionRole", Expert.ChampionRole.Mage);
		} else if (userInput.equalsIgnoreCase("figher")) {
			caseData.put("playerChampionRole",
					Expert.ChampionRole.Fighter);
		} else if (userInput.equalsIgnoreCase("support")) {
			caseData.put("playerChampionRole",
					Expert.ChampionRole.Support);
		}
		
		//Get data from the form about the opponent player type
		userInput = (String) opponentCombo.getSelectedItem();
		if (userInput.equalsIgnoreCase("assassin")) {
			caseData.put("opponentChampionRole", Expert.ChampionRole.Assasin);
		} else if (userInput.equalsIgnoreCase("tank")) {
			caseData.put("opponentChampionRole", Expert.ChampionRole.Tank);
		} else if (userInput.equalsIgnoreCase("mage")) {
			caseData.put("opponentChampionRole", Expert.ChampionRole.Mage);
		} else if (userInput.equalsIgnoreCase("fighter")) {
			caseData.put("opponentChampionRole", Expert.ChampionRole.Fighter);
		} else if (userInput.equalsIgnoreCase("support")) {
			caseData.put("opponentChampionRole", Expert.ChampionRole.Support);
		}
		
		// Get data from the form on the gold value
		caseData.put("Gold", goldSpinner.getValue());
		
		
		// Get an item list for the player from the form.
		LinkedList<LoLItem> lolItems = new LinkedList<LoLItem>();
		if(!playerItem1.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem1.getSelectedItem().toString()));
		}
		if(!playerItem2.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem2.getSelectedItem().toString()));
		}
		if(!playerItem3.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem3.getSelectedItem().toString()));
		}
		if(!playerItem4.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem4.getSelectedItem().toString()));
		}
		if(!playerItem5.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem5.getSelectedItem().toString()));
		}
		if(!playerItem6.getSelectedItem().toString().isEmpty()){
			lolItems.add(itemList.get(playerItem6.getSelectedItem().toString()));
		}
						
		caseData.put("playerItems", lolItems);
		
		// Get an item list for the opponent from the form.
		LinkedList<LoLItem> oplolItems = new LinkedList<LoLItem>();
		if(!opponentItem1.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem1.getSelectedItem().toString()));
		}
		if(!opponentItem2.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem2.getSelectedItem().toString()));
		}
		if(!opponentItem3.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem3.getSelectedItem().toString()));
		}
		if(!opponentItem4.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem4.getSelectedItem().toString()));
		}
		if(!opponentItem5.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem5.getSelectedItem().toString()));
		}
		if(!opponentItem6.getSelectedItem().toString().isEmpty()){
			oplolItems.add(itemList.get(opponentItem6.getSelectedItem().toString()));
		}
						
		caseData.put("opponentItems", oplolItems);
		setVisible(false);
		dispose();
	}
}
