package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import AI.AISimplePlayer;

public class Main {
	public final static int numberOfPlayers = 2;

	public static void main(String[] args) {

//		Deck deck = new Deck();
//		AISimplePlayer ai = new AISimplePlayer(deck.dealHand());
		
		
		GameBoard board = new GameBoard(numberOfPlayers);		
		board.startGame();

	}
		
	
	
	 

}
