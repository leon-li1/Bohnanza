import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUI extends JFrame implements ActionListener{

	//Constants
	private final int FRAME_WIDTH = 1400;
	private final int FRAME_HEIGHT = 800;
	private final int CARD_WIDTH = 100;
	private final int CARD_HEIGHT = 120;

	//Instance fields
	private JPanel GUIPanel = new JPanel();
	private JLabel lblDeck = new JLabel();
	private JLabel lblDiscard = new JLabel();
	private JLabel lblTime1 = new JLabel();
	private JLabel lblTime2 = new JLabel();
	private Timer gameTimer = new Timer(1000, this); //game timer
	private JComboBox<String> selection = new JComboBox<String>();
	private JButton confirm = new JButton();
	private JLabel[] lblTrading = new JLabel[2];
	private JButton instructions = new JButton();
	private JButton endGame = new JButton();
	private Font font = new Font ("Calibri", Font.BOLD, 28); //This is the font for the labels
	private Font font2 = new Font ("Calibri", Font.BOLD, 24);
	private Stack <Card> deckStack = new Stack <Card>();
	private ArrayList <Card> discardPile = new ArrayList <Card>();
	private Card[] cardDeck = new Card[104];
	private Card[] tradingArea = new Card[2];
	private Player[] player = new Player[4];
	private PlayerPanel[] playerPanel = new PlayerPanel[4];
	private int[] ties = new int[4];
	private int coinCountWin;
	private int tieWinHandSize;
	private String winnerName;
	private int cntTime;
	private int ap;
	private int apInput;
	private int phase;
	private int tradeAgain;
	private int cardA;
	private int cardB;
	private int tempIndex;
	private int acceptTrade;
	private int fieldNum;
	private int cardQuantity;
	private int cardOffer;
	private boolean decentTrade;
	private boolean isValuable;
	private String cardType;
	private int blue = 20;
	private int chili = 18;
	private int stink = 16;
	private int green = 14;
	private int soy = 12;
	private int blackEyed = 10;
	private int red = 8;
	private int garden = 6;
	private int cardsLeft;
	private Clip clip;

	public GUI() {

		super();

		//runs deck class
		new Deck(cardDeck, deckStack);

		//Shuffles the deck
		Collections.shuffle(deckStack);

		//Call other methods
		frameSetup();
		gameSetup();
		buttonSetup();
		repaint();

		for (int i = 0; i < player.length; i++) {

			player[i].setPlayerName(JOptionPane.showInputDialog(this, "Please enter player " + (i + 1) + "'s name:"));
			playerPanel[i].getLblName().setText(player[i].getPlayerName());

		}

		JOptionPane.showMessageDialog(null, "Use the drop down list and the confirm\nbutton to navigate through the game.\n                     Have fun!");
		gameTimer.start();
		playerPanel[0].getInfo().setIcon(new ImageIcon(new ImageIcon("Images/watering.gif").getImage().getScaledInstance(150, 50, 0)));
		//new BohnanzaGameOver(checkWinner());

	}

	private void nextPhase() {

		apInput = selection.getSelectedIndex();

		if (phase == 0) {

			System.out.println(deckStack.size());
			if (!player[ap].getHand().isEmpty()) 

				plantCard(ap, player[ap].getHand().get(0), apInput + 1);

			selection.addItem("I don't want to plant");
			selection.setSelectedIndex(2);
			phase++;

		}
		else if (phase == 1) {

			if (apInput == 0 || apInput == 1)

				plantCard(ap, player[ap].getHand().get(0), apInput + 1);

			flipCards();
			selection.setSelectedIndex(2);
			JOptionPane.showMessageDialog(this, "Plant trading area 1?");
			phase++;

		}
		else if (phase == 2) {

			if (apInput == 0 || apInput == 1) {

				plantCard(ap, tradingArea[0], apInput + 1);
				tradingArea[0] = null;
				lblTrading[0].setIcon(null);

			}

			selection.setSelectedIndex(2);
			JOptionPane.showMessageDialog(this, "Plant trading area 2?");
			phase++;

		}
		else if (phase == 3) {

			if (apInput == 0 || apInput == 1) {

				plantCard(ap, tradingArea[1], apInput + 1);
				tradingArea[1] = null;
				lblTrading[1].setIcon(null);

			}

			selection.removeAllItems();
			selection.addItem("I don't want to trade");
			selection.addItem("Trade with players");
			JOptionPane.showMessageDialog(this, "Trade?");
			phase++;

		}
		else if (phase == 4) {

			if (apInput == 1) {

				requestTradeHuman();

				tradeAgain = JOptionPane.showConfirmDialog(this, "Do you want to trade again?", "Trade again?", JOptionPane.YES_NO_OPTION);

				if (tradeAgain == JOptionPane.NO_OPTION)

					phase++;

				nextPhase();

			}
			else {

				phase++;
				nextPhase();

			}

		}
		else if (phase == 5) {

			//Plant what is left of the trading area 
			for (int i = 0; i < tradingArea.length; i++) {

				if (tradingArea[i] == null)

					continue;

				fieldNum = Integer.parseInt(JOptionPane.showInputDialog(this, "Which field would you like to plant in?", "Plant trading area " + (i + 1), JOptionPane.INFORMATION_MESSAGE));
				plantCard(ap, tradingArea[i], fieldNum);
				tradingArea[i] = null;
				lblTrading[i].setIcon(null);

			}

			for (int x = player[ap].getHand().size(); x < 5 ; x++)	{

				if (deckStack.size() == 0)

					break;

				player[ap].getHand().add(deckStack.pop());

			}

			playerPanel[ap].updateHand(player[ap]);
			selection.removeAllItems();
			selection.addItem("Plant in field 1");
			selection.addItem("Plant in field 2");
			selection.setSelectedIndex(0);

			if (deckStack.size() > 0) {

				playerPanel[ap].getInfo().setIcon(null);
				ap++;
				phase = 0;
				botTurn();

			}
			else //cards ran out

				new BohnanzaGameOver(checkWinner());

		}

	}

	private void requestTradeHuman() {

		cardA = Integer.parseInt(JOptionPane.showInputDialog(this, "Which card would you like to trade? (1-4 hand, 5-6 trading area)"));
		cardB = Integer.parseInt(JOptionPane.showInputDialog("Which card would you like to get? (1 or 2)"));

		if (cardB == 1) {

			if (cardA < 5)

				offerTrade(player[0].getHand().get(cardA - 1).getCardName(), player[0].getField1().get(0).getCardName());

			else

				offerTrade(tradingArea[cardA - 5].getCardName(), player[0].getField1().get(0).getCardName());

		}
		else {

			if (cardA < 5)

				offerTrade(player[0].getHand().get(cardA - 1).getCardName(), player[0].getField2().get(0).getCardName());

			else

				offerTrade(tradingArea[cardA - 5].getCardName(), player[ap].getField2().get(0).getCardName());

		}

	}

	//Active player is human: card A is what you give, card B is what you want to get
	private void offerTrade(String a, String b) {

		Outer: for (int x = 1; x < player.length; x++) {

			//Scan each card in player x's hand
			for (int m = 0; m < player[x].getHand().size(); m++) {

				//Check if player x has card b
				if (player[x].getHand().get(m).getCardName() == b) {

					//Check if player x's fields are empty
					if (player[x].getField1().isEmpty() && player[x].getField2().isEmpty()) {

						//trade with player
						JOptionPane.showMessageDialog(this, player[0].getPlayerName() + " will traded away his/her " + a + " bean for " + player[x].getPlayerName() + "'s " + b + " bean.");

						//Determine which trade method to use
						if (cardA < 5)

							tradeCard(x, m, 1);

						else

							tradeCard2(x, m, 1);

						break Outer;

					}
					//Check if player x's field1 is empty while field2 is not
					else if (player[x].getField1().isEmpty() && !player[x].getField2().isEmpty()) {

						if (player[x].getField2().get(0).getCardName() == b)

							//Can't trade with player
							break;

						//check if current player wants to trade
						calcBotTrade(x, m, 2, a, b, player[x].getField2().size());
						break Outer;

					}
					//Check if player x's field2 is empty while field1 is not
					else if (player[x].getField2().isEmpty() && !player[x].getField1().isEmpty()) {

						if (player[x].getField1().get(0).getCardName() == b)

							//Can't trade with player
							break;

						//check if current player wants to trade
						calcBotTrade(x, m, 1, a, b, player[x].getField1().size());
						break Outer;

					}
					//Check if both player x's fields aren't empty
					else if (!player[x].getField1().isEmpty() && !player[x].getField2().isEmpty()) {

						if (player[x].getField1().get(0).getCardName() == b || player[x].getField2().get(0).getCardName() == b)

							//Can't trade with player
							break;

						//Determine which field to harvest/trade into
						if (player[tempIndex].getField1().get(0).getQuantity() >= player[tempIndex].getField2().get(0).getQuantity()) {

							//check if current player wants to trade
							calcBotTrade(x, m, 1, a, b, player[x].getField1().size());
							break Outer;

						}
						else {

							//check if current player wants to trade
							calcBotTrade(x, m, 2, a, b, player[x].getField2().size());
							break Outer;

						}

					}

				}

			}

		}

	}

	private void botTurn() {

		playerPanel[ap].getInfo().setIcon(new ImageIcon(new ImageIcon("Images/watering.gif").getImage().getScaledInstance(150, 50, 0)));

		if (!player[ap].getHand().isEmpty())

			botPlant(player[ap].getHand().get(0));

		if (!player[ap].getHand().isEmpty())

			botPlant(player[ap].getHand().get(0));

		flipCards();
		requestTradeBot();

		//Plant what is left of the trading area 
		for (int i = 0; i < tradingArea.length; i++) {

			if (tradingArea[i] == null)

				continue;

			botPlant(tradingArea[i]);
			lblTrading[i].setIcon(null);

		}

		for (int x = player[ap].getHand().size(); x < 5 ; x++)	{

			if (deckStack.size() == 0)

				break;

			player[ap].getHand().add(deckStack.pop());

		}

		playerPanel[ap].updateHand(player[ap]);

		if (deckStack.size() > 0) {

			playerPanel[ap].getInfo().setIcon(null);

			if (ap == 3)

				ap = 0;

			else {

				ap++;
				botTurn();

			}

			playerPanel[ap].getInfo().setIcon(new ImageIcon(new ImageIcon("Images/watering.gif").getImage().getScaledInstance(150, 50, 0)));

		}
		else //cards ran out

			new BohnanzaGameOver(checkWinner());

	}

	private void botPlant(Card card) {

		if (!player[ap].getField1().isEmpty() && !player[ap].getField2().isEmpty()) {

			//Compare payouts between harvesting the 2 fields
			if (calcHarvest(player[ap].getField1(), player[ap].getField1().size()) >= calcHarvest(player[ap].getField2(), player[ap].getField2().size()))

				fieldNum = 1;

			else

				fieldNum = 2;

			plantCard(ap, card, fieldNum);

		}
		else if (player[ap].getField2().isEmpty() && !player[ap].getField1().isEmpty()) {

			if (player[ap].getField1().get(0).getCardName() == card.getCardName())

				plantCard(ap, card, 1);

			else

				plantCard(ap, card, 2);

		}
		else if (player[ap].getField1().isEmpty() && !player[ap].getField2().isEmpty()) {

			if (player[ap].getField2().get(0).getCardName() == card.getCardName())

				plantCard(ap, card, 2);

			else

				plantCard(ap, card, 1);

		}
		else if (player[ap].getField1().isEmpty() && player[ap].getField2().isEmpty())

			plantCard(ap, card, 1);

	}

	//This method plants cards from trading area if reasonable
	private void plantTradingAreaForBot(int index) {

		//Check if field 2 isn't empty
		if (!player[ap].getField2().isEmpty()) {

			if (player[ap].getField1().get(0).getCardName() == tradingArea[index].getCardName()) {

				plantCard(ap, tradingArea[index], 1);
				tradingArea[index] = null;
				lblTrading[index].setIcon(null);

			}
			else if (player[ap].getField2().get(0).getCardName() == tradingArea[index].getCardName()) {

				plantCard(ap, tradingArea[index], 2);
				tradingArea[index] = null;
				lblTrading[index].setIcon(null);
			}

		}
		//Check if field 2 is empty
		else if (player[ap].getField2().isEmpty())

			if (player[ap].getField1().get(0).getCardName() == tradingArea[index].getCardName()) {

				plantCard(ap, tradingArea[index], 2);
				tradingArea[index] = null;
				lblTrading[index].setIcon(null);

			}
	}

	private void requestTradeBot() {

		for (int  a = 0; a < player[ap].getHand().size(); a++) {

			if (!player[ap].getField1().isEmpty() && player[ap].getHand().get(a).getCardName() == player[ap].getField1().get(0).getCardName())

				continue;

			if (!player[ap].getField2().isEmpty() && player[ap].getHand().get(a).getCardName() == player[ap].getField2().get(0).getCardName())

				continue;

			cardA = a + 1;

			//If field 2 is empty, request card in field 1
			if (player[ap].getField2().isEmpty()) {

				cardB = 1;
				offerBotTrade(player[ap].getHand().get(a).getCardName(), player[ap].getField1().get(0).getCardName());


			}
			//Otherwise, request the rarer card
			else {

				if (player[ap].getField1().get(0).getQuantity() <= player[ap].getField1().get(0).getQuantity()) {

					cardB = 1;
					offerBotTrade(player[ap].getHand().get(a).getCardName(), player[ap].getField1().get(0).getCardName());

				}
				else {

					cardB = 2;
					offerBotTrade(player[ap].getHand().get(a).getCardName(), player[ap].getField2().get(0).getCardName());

				}

			}

		}

	}
	//Active player is a bot: card a is what bot gives, card b is what bot wants
	private void offerBotTrade(String a, String b) {

		tempIndex = ap;

		for (int pCheck = 0; pCheck < 3; pCheck++) {

			if (tempIndex == 3) {

				tempIndex = 0;
				offerTradeHuman(a, b);

			}
			else {

				tempIndex++;
				offerTradeAI(a, b);

			}			

		}

	}

	private void offerTradeHuman(String a, String b) {

		//Check with human player
		if (humanTrade()) {

			cardOffer = Integer.parseInt(JOptionPane.showInputDialog(this, "Where is your " + b + " bean? (1-5, 0 for no)"));

			if (cardOffer != 0) {

				fieldNum = Integer.parseInt(JOptionPane.showInputDialog(this, "Which field would you like to plant " + a + " bean in? (1 or 2)"));

				tradeCard(0, cardOffer - 1, fieldNum);

			}

		}

	}

	private void offerTradeAI(String a, String b) {

		//Scan each card in player x's hand
		for (int m = 0; m < player[tempIndex].getHand().size(); m++) {

			//Check if player x has card b
			if (player[tempIndex].getHand().get(m).getCardName() == b) {

				//Check if player x's fields are empty
				if (player[tempIndex].getField1().isEmpty() && player[tempIndex].getField2().isEmpty()) {

					//trade with player
					JOptionPane.showMessageDialog(this, player[ap].getPlayerName() + " will traded away his/her " + a + " bean for " + player[tempIndex].getPlayerName() + "'s " + b + " bean.");
					tradeCard(tempIndex, m, 1);
					break;

				}
				//Check if both player x's fields aren't empty
				else if (!player[tempIndex].getField1().isEmpty() && !player[tempIndex].getField2().isEmpty()) {

					//check if current player needs requestingCard
					if (player[tempIndex].getField1().get(0).getCardName() == b || player[tempIndex].getField2().get(0).getCardName() == b)

						//Can't trade with player so break current loop
						break;

					//Check which field to harvest/trade into
					if (player[tempIndex].getField1().get(0).getQuantity() >= player[tempIndex].getField2().get(0).getQuantity()) {

						//check if current player wants to trade
						calcBotTrade(tempIndex, m, 1, a, b, player[tempIndex].getField1().size());
						break;

					}
					else {

						//check if current player wants to trade
						calcBotTrade(tempIndex, m, 2, a, b, player[tempIndex].getField2().size());
						break;

					}

				}
				//Check if player x's field2 is empty while field1 is not
				else if (player[tempIndex].getField2().isEmpty() && !player[tempIndex].getField1().isEmpty()) {

					if (player[tempIndex].getField1().get(0).getCardName() == b)

						//Can't trade with player so exit loop
						break;

					//Check if current player wants to trade
					calcBotTrade(tempIndex, m, 1, a, b, player[tempIndex].getField1().size());
					break;

				}
				//Check if player x's field1 is empty while field2 is not
				else if (player[tempIndex].getField1().isEmpty() && !player[tempIndex].getField2().isEmpty()) {

					if (player[tempIndex].getField2().get(0).getCardName() == b)

						//Can't trade with player
						break;

					//Check if current player wants to trade
					calcBotTrade(tempIndex, m, 1, a, b, player[tempIndex].getField1().size());
					break;

				}

			}

		}

	}
	//this method allows the human player to accept/decline a trade
	private boolean humanTrade() {

		//If what they give you is going in field1
		if (cardB == 1) {

			acceptTrade = JOptionPane.showConfirmDialog(this, player[ap].getPlayerName() + " wishes to trade his/her " + player[ap].getHand().get(cardA - 1).getCardName() + " bean for your " + player[ap].getField1().get(0).getCardName() + " bean, do you accept?" ,"Trade Request for " + player[ap].getField1().get(0).getCardName(), JOptionPane.YES_NO_OPTION);

		}
		//If what they give you is going in field2
		else

			acceptTrade = JOptionPane.showConfirmDialog(this, player[ap].getPlayerName() + " wishes to trade his/her " + player[ap].getHand().get(cardA - 1).getCardName() + " bean for your " + player[ap].getField2().get(0).getCardName() + " bean, do you accept?" ,"Trade Request for " + player[ap].getField2().get(0).getCardName(), JOptionPane.YES_NO_OPTION);

		if (acceptTrade == JOptionPane.YES_OPTION)

			return true;

		else

			return false;
	}

	private void calcBotTrade(int playerB, int cardBIndex, int field, String a, String b, int fieldQuantity) {

		//Initialize trade variables
		isValuable = false;
		decentTrade = false;

		//Check if the trade is good for player b (what he/she is receiving is worth more or equal to what he/she is giving, greedy Ai)
		if (field == 1) {

			if (cardB == 1) {

				if (calcHarvest(player[playerB].getField1(), player[playerB].getField1().size() + 1) - calcHarvest(player[ap].getField1(), player[ap].getField1().size() + 1) < 2)

					decentTrade = true;

			}
			else {

				if (calcHarvest(player[playerB].getField1(), player[playerB].getField1().size() + 1) - calcHarvest(player[ap].getField2(), player[ap].getField2().size() + 1) < 2)

					decentTrade = true;

			}

		}
		else {

			if (cardB == 1) {

				if (calcHarvest(player[playerB].getField2(), player[playerB].getField2().size() + 1) - calcHarvest(player[ap].getField1(), player[ap].getField1().size() + 1) < 2)

					decentTrade = true;

			}
			else {

				if (calcHarvest(player[playerB].getField2(), player[playerB].getField2().size() + 1) - calcHarvest(player[ap].getField2(), player[ap].getField2().size() + 1) < 2)

					decentTrade = true;

			}

		}

		//Calculate how much card A is worth to them 
		cardQuantity = fieldQuantity;

		//Check the first 2 cards in player b's hand as he will be able to plant them next turn
		for (int i = 0; i < 2; i++)

			if (player[playerB].getHand().get(i).getCardName() == a)

				cardQuantity++;

		//If the trade isn't the best yet, determine if the card that player b will give away is valuable to himself
		//If the ratio is greater or equal to 0.5, it means that player b is already halfway there from harvesting 2 coins
		if (field == 1) {

			if (cardQuantity / player[playerB].getField1().get(0).getCoinValue2() >= 0.5) 

				isValuable = true;

		}
		else {

			if (cardQuantity / player[playerB].getField2().get(0).getCoinValue2() >= 0.5)

				isValuable = true;

		} 

		if (b == "Blue")

			cardsLeft = blue;

		else if (b == "Chili")

			cardsLeft = chili;

		else if (b == "Stink")

			cardsLeft = stink;

		else if (b == "Green")

			cardsLeft = green;

		else if (b == "Soy")

			cardsLeft = soy;

		else if (b == "Black-eyed")

			cardsLeft = blackEyed;

		else if (b == "Red")

			cardsLeft = red;

		else if (b == "Garden")

			cardsLeft = garden;

		if (decentTrade && !isValuable && cardsLeft >= 10 && player[ap].getCoinCount() - player[playerB].getCoinCount() <= 3) {

			JOptionPane.showMessageDialog(this, player[ap].getPlayerName() + " will trade away his/her " + a + " bean for " + player[playerB].getPlayerName() + "'s " + b + " bean");

			if (cardA < 5)

				tradeCard(playerB, cardBIndex, field);

			else

				tradeCard2(playerB, cardBIndex, field);
		}
		else

			JOptionPane.showMessageDialog(this, player[playerB].getPlayerName() + " has rejected a trade from " + player[ap].getPlayerName() + " who wanted to trade his/her " + a + " bean for a " + b + " bean.");

	}

	private void updateDiscardPile(int playerIndex, int fieldNum) {

		if (fieldNum == 1) {

			for (int i = 0; i < player[playerIndex].getField1().size(); i++) {

				cardType = player[playerIndex].getField1().get(i).getCardName();

				if (cardType == "Blue")

					blue--;

				else if (cardType == "Chili")

					chili--;

				else if (cardType == "Stink")

					stink--;

				else if (cardType == "Green")

					green--;

				else if (cardType == "Soy")

					soy--;

				else if (cardType == "Black-eyed")

					blackEyed--;

				else if (cardType == "Red")

					red--;

				else if (cardType == "Garden")

					garden--;

				discardPile.add(player[playerIndex].getField1().get(i));

			}

		}
		else {

			for (int i = 0; i < player[playerIndex].getField2().size(); i++) {

				cardType = player[playerIndex].getField2().get(i).getCardName();

				if (cardType == "Blue")
  
					blue--;

				else if (cardType == "Chili")

					chili--;

				else if (cardType == "Stink")

					stink--;

				else if (cardType == "Green")

					green--;

				else if (cardType == "Soy")

					soy--;

				else if (cardType == "Black-eyed")

					blackEyed--;

				else if (cardType == "Red")

					red--;

				else if (cardType == "Garden")

					garden--;

				discardPile.add(player[playerIndex].getField2().get(i));

			}

		}

	}

	private void tradeCard(int playerBIndex, int cardBIndex, int fieldNumA) {

		plantCard(ap, player[playerBIndex].getHand().get(cardBIndex), cardB);
		plantCard(playerBIndex, player[ap].getHand().get(cardA - 1), fieldNumA);

		//player[ap].getHand().remove(player[ap].getHand().get(cardA - 1));
		//player[playerBIndex].getHand().remove(player[playerBIndex].getHand().get(cardBIndex));
		player[ap].getHand().remove(cardA - 1);
		player[playerBIndex].getHand().remove(cardBIndex);

		playerPanel[ap].updateHand(player[ap]);
		playerPanel[playerBIndex].updateHand(player[playerBIndex]);

	}

	private void tradeCard2(int playerBIndex, int cardBIndex, int fieldNumA) {

		plantCard(ap, player[playerBIndex].getHand().get(cardBIndex), cardB);
		plantCard(playerBIndex, tradingArea[cardA - 5], fieldNumA);

		tradingArea[cardA - 5] = null;
		lblTrading[cardA - 5].setIcon(null);

		//player[playerBIndex].getHand().remove(player[playerBIndex].getHand().get(cardBIndex));
		player[playerBIndex].getHand().remove(cardBIndex);
		playerPanel[playerBIndex].updateHand(player[playerBIndex]);
	}

	private void flipCards() {

		if (deckStack.size() != 0) {

			tradingArea[0] = deckStack.pop();
			lblTrading[0].setIcon(new ImageIcon(new ImageIcon("Images/" + tradingArea[0].getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));

			if (ap != 0) 

				plantTradingAreaForBot(0);
		}
		else 

			new BohnanzaGameOver(checkWinner());

		if (deckStack.size() != 0) {

			tradingArea[1] = deckStack.pop();
			lblTrading[1].setIcon(new ImageIcon(new ImageIcon("Images/" + tradingArea[1].getCardName() + ".png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));

			if (ap != 0)

				plantTradingAreaForBot(1);
		}
		else 

			new BohnanzaGameOver(checkWinner());

	}

	private void plantCard(int playerIndex, Card card, int fieldNum) { 

		if (fieldNum == 1) {

			if (!player[playerIndex].getField1().isEmpty() && card.getCardName() != player[playerIndex].getField1().get(0).getCardName()) {

				player[playerIndex].setCoinCount(player[playerIndex].getCoinCount() + calcHarvest(player[playerIndex].getField1(), player[playerIndex].getField1().size()));
				playerPanel[playerIndex].updateCoins(player[playerIndex]);
				updateDiscardPile(playerIndex, 1);
				player[playerIndex].getField1().removeAll(player[playerIndex].getField1());
				lblDiscard.setIcon(new ImageIcon(new ImageIcon("Images/Back.png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));

			}

			player[playerIndex].getField1().add(card);

		}
		else if (fieldNum == 2) {

			if (!player[playerIndex].getField2().isEmpty() && card.getCardName() != player[playerIndex].getField2().get(0).getCardName()) {

				player[playerIndex].setCoinCount(player[playerIndex].getCoinCount() + calcHarvest(player[playerIndex].getField2(), player[playerIndex].getField2().size()));
				playerPanel[playerIndex].updateCoins(player[playerIndex]);
				updateDiscardPile(playerIndex, 2);
				player[playerIndex].getField2().removeAll(player[playerIndex].getField2());
				lblDiscard.setIcon(new ImageIcon(new ImageIcon("Images/Back.png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));

			}

			player[playerIndex].getField2().add(card);

		}


		player[playerIndex].getHand().remove(card);
		playerPanel[playerIndex].updateField(fieldNum, player[playerIndex]);
		playerPanel[playerIndex].updateHand(player[playerIndex]);

		//Play planting music
		try {

			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("Sounds/coin.wav")));
			clip.start();

		} catch (Exception e) {

			//System.out.println(e);

		}

	}

	private int calcHarvest(ArrayList<Card> field, int quantity) {

		if (quantity / field.get(0).getCoinValue4() >= 1) 

			return 4;

		else if (quantity / field.get(0).getCoinValue3() >= 1)

			return 3;

		else if (quantity / field.get(0).getCoinValue2() >= 1) 

			return 2;

		else if (quantity / field.get(0).getCoinValue1() >= 1) 

			return 1;

		else

			return 0;

	}

	public String checkWinner() {

		//Find the highest coin count
		for (int i = 0; i < player.length;  i++) 

			if (player[i].getCoinCount() >= coinCountWin)

				coinCountWin = player[i].getCoinCount();

		Arrays.fill(ties, -1);

		//Find ties
		for (int i = 0; i < player.length;  i++)

			if (player[i].getCoinCount() == coinCountWin)

				ties[i] = i;

		/* We assume there is a tie in all games because if we do not then we would
		 * still have to check (loop) through the tie array to see who the winner is
		 * and return its name. It is less complicated to just assume there is a tie.
		 */

		//Find highest hand size for ties
		for (int i = 0; i < player.length; i++) {

			if (ties[i] == -1) 

				continue;

			else

				if (player[i].getHand().size() > tieWinHandSize) {

					tieWinHandSize = player[i].getHand().size();
					winnerName = player[i].getPlayerName(); //assume that if there is a tie of a tie, first player wins

				}

		}

		return winnerName;
	}

	private void gameSetup() {

		//Create players
		for (int i = 0; i < player.length; i++) {

			player[i] = new Player();

			for (int x = 0; x < 5; x++)

				player[i].getHand().add(deckStack.pop());

		}

		player[0].setPlayerName("Player 1");
		player[1].setPlayerName("Player 2");
		player[2].setPlayerName("Player 3");
		player[3].setPlayerName("Player 4");

		//Create player panels
		for (int i = 0; i < playerPanel.length; i++) {

			playerPanel[i] = new PlayerPanel(player[i]);
			playerPanel[i].getPlayerIcon().setIcon(new ImageIcon(new ImageIcon("Images/player" + i + ".png").getImage().getScaledInstance(80, 80, 0)));
			GUIPanel.add(playerPanel[i]);

			if (i == 0)
				playerPanel[i].setBounds(20, 50, 600, 300);
			else if (i == 1)
				playerPanel[i].setBounds(775, 50, 600, 300);
			else if (i == 2)
				playerPanel[i].setBounds(20, 400, 600, 300);
			else
				playerPanel[i].setBounds(775, 400, 600, 300);

		}

		//Show the time (fixed part)
		lblTime1.setFont(font);
		lblTime1.setForeground(Color.BLACK);
		lblTime1.setText("TIME:");
		lblTime1.setBounds(1255, 10, 70, 40);
		GUIPanel.add(lblTime1);

		//Show the time (counting part)
		lblTime2.setFont(font);
		lblTime2.setForeground(Color.BLACK);
		lblTime2.setText("0000");
		lblTime2.setBounds(1325, 18, 70, 25);
		GUIPanel.add(lblTime2);

		//Show the deck
		lblDeck.setIcon(new ImageIcon(new ImageIcon("Images/Back.png").getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, 0)));
		lblDeck.setBounds(647, 100, 100, 120);
		GUIPanel.add(lblDeck);

		//Show the discard pile
		lblDiscard.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.RED));
		lblDiscard.setBounds(647, 555, 100, 120);
		GUIPanel.add(lblDiscard);

		//Show trading area
		for (int i = 0; i < lblTrading.length; i++) {

			lblTrading[i] = new JLabel();
			lblTrading[i].setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));
			lblTrading[i].setBounds(647, 255 + i * 150, 100, 120);
			GUIPanel.add(lblTrading[i]);

		}

		//Show the combo boxes
		selection.setFont(font2);
		selection.setBackground(new Color(255, 255, 153));
		selection.setBounds(50, 710, 250, 50);
		GUIPanel.add(selection);

		//Add items
		selection.addItem("Plant in field 1");
		selection.addItem("Plant in field 2");
		selection.setFocusable(false);

	}

	private void buttonSetup() {

		//Add end game button
		endGame.setFont(font);
		endGame.setText("End Game");
		endGame.setBackground(new Color(255, 255, 153));
		endGame.setFocusPainted(false);
		endGame.addActionListener(this);
		endGame.setBounds(1100, 710, 250, 50);
		GUIPanel.add(endGame);

		//Add instructions button
		instructions.setFont(font);
		instructions.setText("Instructions");
		instructions.setBackground(new Color(255, 255, 153));
		instructions.setFocusPainted(false);
		instructions.addActionListener(this);
		instructions.setBounds(825, 710, 250, 50);
		GUIPanel.add(instructions);

		//Show confirm button
		confirm.setFont(font);
		confirm.setText("Confirm");
		confirm.setBackground(new Color(255, 255, 153));
		confirm.setFocusPainted(false);
		confirm.addActionListener(this);
		confirm.setBounds(350, 710, 250, 50);
		GUIPanel.add(confirm);

	}

	//This method sets up the frame
	private void frameSetup() {

		//Frame setup
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setContentPane(new JLabel (new ImageIcon(new ImageIcon("Images/back.jpg").getImage().getScaledInstance(FRAME_WIDTH,FRAME_HEIGHT,0))));
		setVisible(true);

		//Panel setup
		GUIPanel.setBounds(0, 0, FRAME_WIDTH - 5, FRAME_HEIGHT - 28); 
		GUIPanel.setBorder(BorderFactory.createMatteBorder(5, 5, 5, 5, Color.BLACK));
		GUIPanel.setOpaque(false);
		GUIPanel.setLayout(null);
		add(GUIPanel);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == gameTimer) {

			cntTime++;
			lblTime2.setText(String.valueOf(cntTime));


		}

		if (event.getSource() == confirm) 

			if (ap == 0)

				nextPhase();

			else

				botTurn();

		else if (event.getSource() == instructions) {

			try {

				Desktop.getDesktop().browse(new URL("https://drive.google.com/file/d/0B5zP5B188oiSc3FoVldvRmRIb2s/view").toURI());

			} catch (Exception e) {

				System.out.println("Error");

			}

		}
		else if (event.getSource() == endGame)

			System.exit(0);

	}



}