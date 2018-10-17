package game;

public class Card implements Comparable<Card>{
	
	private Suit suit;
	private Value value;
	
	public Card(Value value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}
	
	
	public Suit getSuit() {
		return suit;
	}
	
	public int getSuitOrder() {
		return suit.getOrder();
	}

	public Value getValue() {
		return value;
	}
	
	public int getValueOrder() {
		return value.getOrder();
	}

	public String toString() {
		String valstr = Integer.toString(value.getFace());
		switch(value) {
			case ACE: valstr = "A";break;
			case JACK: valstr = "J";break;
			case QUEEN: valstr = "Q";break;
			case KING: valstr = "K";break;
		default:
			break;
		}
		return (valstr + this.suit.toString().charAt(0));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Card other = (Card) obj;
		if (suit != other.suit)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public int compareTo(Card card) {
	    int result = card.getValueOrder() - this.getValueOrder();
	    if(result==0) {
	        return Integer.valueOf(card.getSuitOrder()).compareTo(this.getSuitOrder());
	    }
	    else {
	        return result;
	    }
	}




	
}
