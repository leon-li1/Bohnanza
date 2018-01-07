import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

public class Deck {

	public Deck(Card[] cardDeck, Stack<Card> deckStack)  {

		String cardName;
		int quantity;
		int coinValue1;
		int coinValue2;
		int coinValue3;
		int coinValue4;
		int index = 0;

		try {

			//reads in Bean types CSV file
			Scanner input = new Scanner(new File("Bean types.csv"));
			input.useDelimiter(","); //seperates items using comma

			input.nextLine();  //gets rid of the header row

			while (input.hasNextLine()) {

				cardName = input.next().replaceAll("\r\n", ""); //ignores the auto generated text at the start of each line
				quantity = input.nextInt();
				coinValue1 = input.nextInt();
				coinValue2 = input.nextInt();
				coinValue3 = input.nextInt();
				coinValue4 = input.nextInt();

				for (int i = 0; i < quantity; i++) {

					cardDeck[index] = new Card();

					cardDeck[index].setCardName(cardName); 
					cardDeck[index].setQuantity(quantity);
					cardDeck[index].setCoinValue1(coinValue1);
					cardDeck[index].setCoinValue2(coinValue2);
					cardDeck[index].setCoinValue3(coinValue3);
					cardDeck[index].setCoinValue4(coinValue4);

					deckStack.add(cardDeck[index]);
					
					index++;
					
				}

			}

			input.close();

		} catch (FileNotFoundException e) { //checks if there is an error in reading the excel file and prints an error out if there is.

			System.out.println("Sorry error with file- pls check name");

		}
		

	}

}