
public class Card {
	public Suit suit;
	public Integer value;
	
	public Card(Integer value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}
	
	public String toString() {
		String valstr = value.toString();
		switch(value.intValue()) {
			case 1: valstr = "A";break;
			case 11: valstr = "J";break;
			case 12: valstr = "Q";break;
			case 13: valstr = "K";break;
		}
		return (valstr + this.suit.toString().charAt(0));
	}
}
