import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GameBoard {
	public ArrayList<Card> current;
	public boolean firstMove; //first move of a round
	public int turn;
	
	public GameBoard() {
		this.firstMove = true;
	}
	
	public void setFirstMove(boolean val) {
		this.firstMove = val;
	}
	
	public boolean validMove(ArrayList<Card> move) {
		//logic to make sure it's a valid combination
		if(firstMove) {
			switch(move.size()) {
			case 1:return true;
			case 2:return move.get(0).getValue() == move.get(1).getValue();
			case 3:return (move.get(0).getValue() == move.get(1).getValue() && move.get(1).getValue() == move.get(2).getValue());
			case 4:return false;
			case 5:return true; //TODO
			default: return false;
			}
		}else { //if not first move then need to check if it matches the current pattern and if it's higher
			if(move.size() != current.size()) { //not match pattern by size
				return false;
			}else {
				if(move.size()==1) {
					if((move.get(0).getValue().getOrder() < current.get(0).getValue().getOrder()) || //higher value
							(move.get(0).getValue().getOrder() == current.get(0).getValue().getOrder() && //or equal value but higher suit
							move.get(0).getSuit().getOrder() < current.get(0).getSuit().getOrder())) {
						return true;
					}else {
						return false;
					}
				}
				if(move.size()==2) {
					if(move.get(0).getValue() != move.get(1).getValue()) {//not a pair
						return false;
					}
					if(move.get(0).getValue().getOrder() < current.get(0).getValue().getOrder()) {//strictly higher
						return true;
					}//TODO: pairs of same order
					else {
						return false;
					}
				}
				if(move.size()==3) {
					if(move.get(0).getValue() != move.get(1).getValue() || move.get(1).getValue() != move.get(2).getValue()) {
						return false;
					}
					if(move.get(0).getValue().getOrder() < current.get(0).getValue().getOrder()){
						return true;
					}else {
						return false;
					}
				}
			}
			
		}
		
		return false;
	}
	
	public static Combo comboClassifier(ArrayList<Card> played) {
		boolean bflush = false;
		boolean bstraight = false;
		
//		//check for flush using regex
//		String regex = "\\[(?>.D,?){5}\\]|\\[(?>.S,?){5}\\]|\\[(?>.H,?){5}\\]|\\[(?>.C,?){5}\\]";
//		if(played.toString().matches(regex)) {
//			bflush = true;
//		}
		for(int i =0; i<4; i++) {
			if(played.get(i).getSuitOrder() != played.get(i+1).getSuitOrder()) {
				break;
			}
			bflush = true;
		}

		
//		Collections.sort(played);

		
		//making comparator object to sort by face value
		Comparator<Card> faceOrder =  new Comparator<Card>() {
	        public int compare(Card c1, Card c2) {
	            return c1.getValue().getFace() - c2.getValue().getFace();
	        }
	    };
	    Collections.sort(played, faceOrder);
		for(int i =0; i<4; i++) {
			if(played.get(i).getValue().getFace() == played.get(i+1).getValue().getFace()-1) {
				bstraight = true;
			}
			else if(i==0 && played.get(1).getValue().getFace()==10) {//special case where A is used as last card, then check if next is 10,J,Q,K
				bstraight = true;
			}
			else {
				bstraight = false;
				break;
			}
		}
		
		if(bflush && bstraight) {
			return Combo.ROYAL_FLUSH;
		}
		if(bflush) {
			return Combo.FLUSH;
		}
		if(bstraight) {
			return Combo.STRAIGHT;
		}
		
		return null;
		
	}
	
	public void setCurrent(ArrayList<Card> played) {
		this.current = played;
	}
}
