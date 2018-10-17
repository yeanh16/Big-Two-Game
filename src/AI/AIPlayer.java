package AI;

import java.util.ArrayList;

import game.Card;
import game.GameBoard;
import game.Player;

public class AIPlayer extends Player{
	public transient IStateMachine<AIPlayer, State<AIPlayer>> stateMachine;

	public AIPlayer(ArrayList<Card> hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrayList<Card> getMove(GameBoard gb) {
		return null;
	}

}
