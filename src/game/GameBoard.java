package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import AI.AISimplePlayer;

public class GameBoard {
	public ArrayList<Card> current;
	public Combo currentCombo;
	public Card helperComboHighCard;
	public boolean firstMove; //first move of a round
	public int numberOfPlayers;
	public int playerToMove;
	public ArrayList<Player> playerList;
	
	public Scanner reader = new Scanner(System.in);  // Reading from System.in for player
	public Scanner getReader() {
		return this.reader;
	}
	
	public GameBoard(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
		this.firstMove = true;
		this.currentCombo = null;
		//TODO: alternate first player to move
		this.playerToMove = 0;
		
		Deck deck = new Deck();

		this.playerList = new ArrayList<Player>();
		this.playerList.add(new Player(deck.dealHand()));
		for(int i = 1; i<numberOfPlayers;i++) {
			this.playerList.add(new AISimplePlayer(deck.dealHand()));
		}
	}
	
	public int getLowestHand() {
		int min = 999;
		for(int i=0;i<numberOfPlayers;i++) {
			if(playerList.get(i).getHand().size() < min) {
				min = playerList.get(i).getHand().size();
			}
		}
		return min;
	}
	
	public void startGame() {
		int passCount = 0;
		while(true) {
			if(passCount != numberOfPlayers - 1) {
				ArrayList<Card> move = playerList.get(playerToMove).getMove(this);
				if(move != null) {
					System.out.println("Player " + (playerToMove+1) + ": " + move);
					setMove(move);
					passCount = 0;
					resetPasses();
					if(playerList.get(playerToMove).getHand().size() == 0) {
						System.out.println("Player " + (playerToMove+1) + " wins!!!!");
						resetGame();
						this.playerToMove = playerToMove - 1; //so that the winner plays first next game
					}
				}else {
					System.out.println("Player " + (playerToMove+1) + " passes");
					passCount++;
				}
				this.playerToMove = (playerToMove+1) % (numberOfPlayers);
			}else {
				System.out.println("Player " + (playerToMove+1) + " wins set.");
				passCount = 0;
				resetSet();
			}
		}
	}
	
	public void resetGame() {
		resetSet();
		Deck deck = new Deck();
		for(int i = 0; i<numberOfPlayers;i++) {
			this.playerList.get(i).setHand(deck.dealHand());
		}
	}
	
	public void resetSet() {
		this.firstMove = true;
		this.currentCombo = null;
	}
	
	public void resetPasses() {
		for(int i=0;i<numberOfPlayers;i++) {
			this.playerList.get(i).resetPass();
		}
	}
	
	public void setMove(ArrayList<Card> move) {
		if(validMove(move)) {
			if(firstMove) {
				firstMove = false;
			}
			setCurrent(move);
		}
	}
	
	
	public Player getPlayer(int index) {
		return this.playerList.get(index);
	}
	
	public void setFirstMove(boolean val) {
		this.firstMove = val;
	}
	
	public boolean validMove(ArrayList<Card> move) {
		//logic to make sure it's a valid combination and if it beats current card
		if(firstMove) {
			switch(move.size()) {
			case 1:return true;
			case 2:return move.get(0).getValue() == move.get(1).getValue();
			case 3:return (move.get(0).getValue() == move.get(1).getValue() && move.get(1).getValue() == move.get(2).getValue());
			case 4:return false;
			case 5:return comboClassifier(move) != null;
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
				if(move.size()==4) {
					return false;
				}
				if(move.size()==5) {
					Pair<Combo,Card> movePair = comboClassifier(move);
					if(this.currentCombo.getOrder() > movePair.getFirst().getOrder()) {
						return true;
					}else if(movePair.getFirst() == Combo.FULL_HOUSE && this.helperComboHighCard.getValueOrder() > movePair.getSecond().getValueOrder()) {//logic for full house disputes
						return true;
					}
					//logic for flush
					//logic for straight
					//logic for royal flush
					//logic for four of a kind
				}
			}
			
		}
		
		return false;
	}
	
	/**
	 * given a move, it returns if it is a valid combo along with the high card in the combo
	 * @param played
	 * @return
	 */
	public static Pair<Combo,Card> comboClassifier(ArrayList<Card> played) {
		boolean bflush = false;
		boolean bstraight = false;
		
		//FLUSH check
		for(int i =0; i<4; i++) {
			if(played.get(i).getSuitOrder() != played.get(i+1).getSuitOrder()) {
				bflush = false;
				break;
			}
			bflush = true;
		}

		
		//making comparator object to sort by face value STRAIGHT check
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
			else if(i==0 && played.get(0).getValue().getFace() ==1 && played.get(1).getValue().getFace()==10) {//special case where A is used as last card, then check if next is 10,J,Q,K
				bstraight = true;
			}
			else {
				bstraight = false;
				break;
			}
		}
		
		Collections.sort(played);

		if(bflush && bstraight) {
			return new Pair(Combo.ROYAL_FLUSH, played.get(4));
		}
		if(bflush) {
			return new Pair(Combo.FLUSH,played.get(4));
		}
		if(bstraight) {
			return new Pair(Combo.STRAIGHT,played.get(4));
		}
		
		//full house, four of a kind check
		int sameCount = 1;
		int i = 0;
		while(played.get(i).getValue() == played.get(i+1).getValue()) {
				sameCount++;
				i++;
		}
		switch(sameCount) {
			case 1: if(played.get(1).getValue()==played.get(2).getValue()&&played.get(2).getValue()==played.get(3).getValue()&&played.get(3).getValue()==played.get(4).getValue()) {
				return new Pair(Combo.FOUR_OF_A_KIND,played.get(4));
			}break;
			case 2: if(played.get(2).getValue()==played.get(3).getValue()&&played.get(3).getValue()==played.get(4).getValue()) {
				return new Pair(Combo.FULL_HOUSE,played.get(4));
			}break;
			case 3: if(played.get(3).getValue()==played.get(4).getValue()) {
				return new Pair(Combo.FULL_HOUSE,played.get(2));
			}break;
			case 4:return new Pair(Combo.FOUR_OF_A_KIND,played.get(3));
			default: break;
		}
		return null;
	}

	
	public void setCurrent(ArrayList<Card> played) {
		this.current = played;
		if(played.size() == 5) {
			Pair<Combo,Card> pair = comboClassifier(played);
			this.currentCombo = pair.getFirst();
			this.helperComboHighCard = pair.getSecond();
		}
	}
}
