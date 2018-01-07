import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Player {

	private String playerName;
	private int coinCount;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private ArrayList<Card> field1 = new ArrayList<Card>();
	private ArrayList<Card> field2 = new ArrayList<Card>();

	public Player() {
		super();

	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}


	public int getCoinCount() {
		return coinCount;
	}

	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}

	public ArrayList<Card> getHand() {
		return hand;
	}

//	public void setHand(ArrayList<Card> hand) {
//		this.hand = hand;
//	}

	public ArrayList<Card> getField1() {
		return field1;
	}

//	public void setField1(ArrayList<Card> field1) {
//		this.field1 = field1;
//	}

	public ArrayList<Card> getField2() {
		return field2;
	}

//	public void setField2(ArrayList<Card> field2) {
//		this.field2 = field2;
//	}

	@Override
	public String toString() {
		return "Player [playerName=" + playerName + ", coinCount=" + coinCount + ", hand=" + hand
				+ ", field1=" + field1 + ", field2=" + field2 + "]";
	}
	
}
