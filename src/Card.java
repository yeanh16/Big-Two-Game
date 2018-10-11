
public class Card {
	enum Suit{CLUBS, HEARTS, SPADES, DIAMOND};
	public Suit suit;
	public Integer value;
	
	public Card(Integer value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}
	
	public String toString() {
		String valstr = value.toString();
		switch(value) {
			case 1: valstr = "A";
			case 11: valstr = "J";
			case 12: valstr = "Q";
			case 13: valstr = "K";
		}
		return (valstr + " " + this.suit.toString().charAt(0));
	}
}
