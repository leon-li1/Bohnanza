import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.*;

public class PlayerPanel extends JPanel {

	//Constants
	private final int CARD_WIDTH = 100;
	private final int CARD_HEIGHT = 120;

	//Font for the labels
	private Font font1 = new Font ("Calibri", Font.BOLD, 28);
	private Font font2 = new Font ("Calibri", Font.BOLD, 14);

	private JLabel lblName = new JLabel();
	private JLabel lblCoins = new JLabel();
	private JLabel coinPic = new JLabel();
	private JLabel playerIcon = new JLabel();
	private JLabel[] hand = new JLabel[5];
	private JLabel[] field = new JLabel[2];
	private JLabel[] fieldCount = new JLabel[2];
	private JLabel info = new JLabel();

	public PlayerPanel(Player player) {

		panelSetup();
		handLabelsSetup(player);
		fieldsSetup();
		labelSetup(player);
		
	}

	private void labelSetup(Player player) {

		//Add label for player name
		lblName.setFont(font1);
		lblName.setText(player.getPlayerName());
		lblName.setBounds(20, 5, 150, 50);
		add(lblName);

		//Add label for coin count
		lblCoins.setFont(font1);
		lblCoins.setText("0");
		lblCoins.setBounds(550, 20, 50, 50);
		add(lblCoins);

		//Add label for coin picture
		coinPic.setIcon(new ImageIcon(new ImageIcon("Images/Coin.gif").getImage().getScaledInstance(80, 50, 0)));
		coinPic.setBounds(470, 20, 80, 50);
		add(coinPic);

		//Add label for player icon
		playerIcon.setOpaque(false);
		playerIcon.setBounds(20, 50, 80, 80);
		add(playerIcon);
		
		//Add label for additional info
		//info.setBorder(BorderFactory.createLineBorder(Color.black));
		info.setBounds(435, 82, 150, 65);
		info.setOpaque(false);
		add(info);
		
	}

	private void fieldsSetup() {

		for (int i = 0; i < field.length; i++) {

			field[i] = new JLabel();
			field[i].setBorder(BorderFactory.createDashedBorder(Color.BLACK));
			field[i].setBounds(190 + i * (CARD_WIDTH + 20), 20, CARD_WIDTH + 10, CARD_HEIGHT + 10);
			add(field[i]);

			fieldCount[i] = new JLabel();
			fieldCount[i].setFont(font2);
			fieldCount[i].setText("0");
			fieldCount[i].setBounds(190 + i * (CARD_WIDTH + 20), 152, CARD_WIDTH + 10, 10);
			add(fieldCount[i]);

		}

	}

	public void updateHand(Player player) {

		for (int i = 0; i < hand.length; i++) {

			if (i < player.getHand().size())
				
				hand[i].setIcon(new ImageIcon(new ImageIcon("Images/" + player.getHand().get(i).getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));
			
			else
				
				hand[i].setIcon(null);
			
		}
		
	}

	public void updateField(int fieldNum, Player player) {

		if (fieldNum == 1) {

			field[0].setIcon(new ImageIcon(new ImageIcon("Images/" + player.getField1().get(0).getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH + 10, CARD_HEIGHT + 10, 0)));
			fieldCount[0].setText(String.valueOf(player.getField1().size()));

		}
		else {

			field[1].setIcon(new ImageIcon(new ImageIcon("Images/" + player.getField2().get(0).getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH + 10, CARD_HEIGHT + 10, 0)));
			fieldCount[1].setText(String.valueOf(player.getField2().size()));

		}
	
	}

	public void updateCoins(Player player) {
		
		lblCoins.setText(String.valueOf(player.getCoinCount()));
		
	}
	
	private void handLabelsSetup(Player player) {

		for (int i = 0; i < hand.length; i++) {

			hand[i] = new JLabel();
			hand[i].setIcon(new ImageIcon(new ImageIcon("Images/" + player.getHand().get(i).getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));
			hand[i].setBounds(25 + i * (CARD_WIDTH + 10), 165, CARD_WIDTH, CARD_HEIGHT);
			add(hand[i]);

		}
		
		hand[0].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.CYAN));

	}

	private void panelSetup() {

		setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		setBackground(new Color (255, 255, 153));
		setLayout(null);

	}

	public JLabel getInfo() {
		
		return info;
	
	}

	public JLabel getPlayerIcon() {
	
		return playerIcon;
	
	}

	public JLabel getLblName() {
	
		return lblName;
	
	}
	
	
}