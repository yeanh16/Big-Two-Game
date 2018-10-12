import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Deck deck = new Deck();
		System.out.println(deck);
		GameBoard board = new GameBoard();
		Player one = new Player(deck.dealHand());
		
		
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		while(true){
			ArrayList<Card> playerHand = one.getHand();
			Collections.sort(playerHand);		    
			System.out.println("Your hand is: \n" + playerHand);
			System.out.println("Enter your move e.g 3H,3S");
			String move = reader.nextLine();
			ArrayList<Card> played = parseMove(move);
			if(board.validMove(played)) {
				System.out.println("Player played " + played);
				//System.out.println(GameBoard.comboClassifier(played));
				board.setFirstMove(false);
				board.setCurrent(played);
				one.getHand().removeAll(played);
			}else {
				System.out.println("Invalid Move");
			}
			
		}

	}
		
	public static ArrayList<Card> parseMove(String movestr){
		ArrayList<Card> cards = new ArrayList<Card>();
		String[] moves = movestr.split(",");
		for(String move: moves) {
			String test = move.substring(0,1);
			Value val;
			switch(test) {
			case "A": val = Value.ACE;break;
			case "1": val = Value.TEN;break;
			case "J": val = Value.JACK;break;
			case "Q": val = Value.QUEEN;break;
			case "K": val = Value.KING;break;
			case "2": val = Value.TWO;break;
			case "3": val = Value.THREE;break;
			case "4": val = Value.FOUR;break;
			case "5": val = Value.FIVE;break;
			case "6": val = Value.SIX;break;
			case "7": val = Value.SEVEN;break;
			case "8": val = Value.EIGHT;break;
			case "9": val = Value.NINE;break;
			default : val = Value.ACE;break;
			}
			Suit suit;
			if(val != Value.TEN) {
				suit = Suit.getSuit(move.substring(1,2));
			}else {
				suit = Suit.getSuit(move.substring(2,3));
			}
			cards.add(new Card(val,suit));
		}
		return cards;
	}
	
	
	 

}
