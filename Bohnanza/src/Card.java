import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Card {

	//Fields
	private String cardName;
	private int quantity;
	private int coinValue1;
	private int coinValue2;
	private int coinValue3;
	private int coinValue4;

	//Overloaded method
	public Card() {
		super();
		cardName = "";
		quantity = 0;
		coinValue1 = 0;
		coinValue2 = 0;
		coinValue3 = 0;
		coinValue4 = 0;
		
	}

	public Card(int quantity, String cardName, int coinValue1, int coinValue2, int coinValue3, int coinValue4) {
		super();
		this.cardName = cardName;
		this.quantity = quantity;
		this.coinValue1 = coinValue1;
		this.coinValue2 = coinValue2;
		this.coinValue3 = coinValue3;
		this.coinValue4 = coinValue4;

	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public int getQuantity() {
		return quantity;
	}


	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}


	public int getCoinValue1() {
		return coinValue1;
	}


	public void setCoinValue1(int coinValue1) {
		this.coinValue1 = coinValue1;
	}


	public int getCoinValue2() {
		return coinValue2;
	}


	public void setCoinValue2(int coinValue2) {
		this.coinValue2 = coinValue2;
	}


	public int getCoinValue3() {
		return coinValue3;
	}


	public void setCoinValue3(int coinValue3) {
		this.coinValue3 = coinValue3;
	}


	public int getCoinValue4() {
		return coinValue4;
	}


	public void setCoinValue4(int coinValue4) {
		this.coinValue4 = coinValue4;
	}

	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", quantity=" + quantity + ", coinValue1=" + coinValue1 + ", coinValue2="
				+ coinValue2 + ", coinValue3=" + coinValue3 + ", coinValue4=" + coinValue4 + "]";
	}

}