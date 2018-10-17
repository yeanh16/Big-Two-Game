package game;

import java.util.ArrayList;
import java.util.Collections;

public class Player {
	private ArrayList<Card> hand;
	public boolean passed;
	
	public Player(ArrayList<Card> hand) {
		this.passed = false;
		this.hand = hand;
		Collections.sort(this.hand);
	}
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	public void setHand(ArrayList<Card> newHand) {
		this.hand = newHand;
		Collections.sort(this.hand);
	}
	
	public void pass() {
		this.passed = true;
	}
	
	public void resetPass() {
		this.passed = false;
	}
	
	public ArrayList<Card> getMove(GameBoard gb){
		System.out.println("Your hand is: \n" + hand);
		System.out.println("Enter your move e.g 3H,3S");
		while(true) {
			String movestr = gb.reader.nextLine();
			if(movestr.equalsIgnoreCase("pass")) {
				pass();
				return null;
			}
			ArrayList<Card> move = parseMove(movestr);
			if(gb.validMove(move)) {
				this.hand.removeAll(move);
				return move;
			}else {
				System.out.println("Invalid move, try again");
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
