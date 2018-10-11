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
			for(Integer i=1;i<14;i++) {
				this.deck.add(new Card(i,suit));
			}
		}
		Collections.shuffle(this.deck);
	}
	
	public String toString() {
		return this.deck.toString();
	}
}
