package game;

public enum Suit{SPADES(1), HEARTS(2), CLUBS(3), DIAMOND(4);
	
	int order;
	
	Suit(int o){
		order = o;
	}
	
	int getOrder(){
		return order;
	}
		 
	 public static Suit getSuit(String letter) {
		 switch(letter) {
		 case "S": return SPADES;
		 case "C": return CLUBS;
		 case "D": return DIAMOND;
		 case "H": return HEARTS;
		 default: return null;
		 }
	 }
	 


}


