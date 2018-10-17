package AI;

import game.GameBoard;

public interface IStateMachine<E, S extends State<E>> {
	void update(GameBoard gb);
	void changeState(S newState);
	void setInitialState(S state);
	S getCurrentState();
    boolean isInState(S state);
	S getPreviousState();
	boolean revertToPreviousState();
}