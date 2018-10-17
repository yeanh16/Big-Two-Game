package game;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
	public ArrayList<Card> deck;
	
	public Deck() {
		createDeck();
	}
	
	private void createDeck() {
		this.deck = new ArrayList<Card>();
		for(Suit suit: Suit.values()) {
			for(Value val: Value.values()) {
				this.deck.add(new Card(val,suit));
			}
		}
		Collections.shuffle(this.deck);
	}
	
	public ArrayList<Card> dealHand(){
		ArrayList<Card> dealt = new ArrayList<Card>();
		for(int i = 0; i<13; i++) {
			dealt.add(this.deck.get(0));
			deck.remove(0);
		}
		return dealt;
	}
	
	public ArrayList<Card> getDeck(){
		return this.deck;
	}
	
	public String toString() {
		return this.deck.toString();
	}
}
