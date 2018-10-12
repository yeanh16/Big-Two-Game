import java.util.ArrayList;

public class Player {
	private ArrayList<Card> hand;
	
	public Player(ArrayList<Card> hand) {
		this.hand = hand;
	}
	
	public ArrayList<Card> getHand(){
		return this.hand;
	}
	
	public void setHand(ArrayList<Card> newHand) {
		this.hand = newHand;
	}
}
