package AI;

import game.GameBoard;

/**
 * Implementation of a state machine of the {@link IStateMachine} interface
 * @author Yean
 *
 * @param <E> The entity that owns this state machine
 * @param <S> The type of the states of state machine
 */
public class StateMachine<E, S extends State<E>> implements IStateMachine<E,S> {
	protected E owner;
	protected S currentState;
	protected S previousState;
	
	public StateMachine(E owner, S initialState){
		this.owner = owner;
		this.setInitialState(initialState);
	}
	
	/**
	 * Updates the state machine by invoking the current state's code
	 */
	@Override
	public void update(GameBoard gb) {
		if(currentState!=null) currentState.update(this.owner, gb);
	}
	
	public E getOwner(){
		return owner;
	}
	
	@Override
	public void changeState(S newState) {
		previousState = currentState;
		if(currentState!=null) currentState.exit(owner);
		currentState=newState;
		if(currentState!=null) currentState.enter(owner);		
	}

	@Override
	public void setInitialState(S state){
		this.previousState = null;
		this.currentState = state;
	}
	
	@Override
	public S getCurrentState(){
		return currentState;
	}
	
	@Override
	public boolean isInState(S state) {
		return state == currentState;
	}
	
	
	@Override
	public S getPreviousState(){
		return previousState;
	}
	
	@Override
	public boolean revertToPreviousState(){
		if (previousState==null) return false;
		changeState(previousState);
		return true;
	}
	
	public void setOwner(E owner){
		this.owner = owner;
	}
}