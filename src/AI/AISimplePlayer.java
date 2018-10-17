package AI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import game.Card;
import game.Combo;
import game.GameBoard;
import game.Pair;
import game.Player;

/**
 * Logic of this AI:
 * has a master move list and will always play the smallest possible move
 * @author yean.ha
 *
 */
public class AISimplePlayer extends Player {
	private ArrayList<Card> singles;
	private ArrayList<ArrayList<Card>> pairs;
	private ArrayList<ArrayList<Card>> triples;
	private ArrayList<Pair<Pair<Combo,Card>, ArrayList<Card>>> combos; //List of pairs of  (combo&highcard | cards)
	private ArrayList<ArrayList<Card>> masterMoveList;
	private ArrayList<ArrayList<Card>> optimalMoveList;
	private boolean debug = true;

	public AISimplePlayer(ArrayList<Card> hand) {
		super(hand);
		singles = new ArrayList<Card>();
		pairs = new ArrayList<ArrayList<Card>>();
		triples = new ArrayList<ArrayList<Card>>();
		combos = new ArrayList<Pair<Pair<Combo,Card>, ArrayList<Card>>>();
		masterMoveList = new ArrayList<ArrayList<Card>>();
		optimalMoveList = new ArrayList<ArrayList<Card>>();
		getCombos();
		gatherAllMoves();
		test();
	}
	
	@Override
	public ArrayList<Card> getMove(GameBoard gb) {
		if(debug) System.out.println("PLAYER " + (gb.playerToMove+1) + " DEBUGGING");
		if(debug) System.out.println("CARDS: " + this.getHand());
		//if it's first move and somebody has a low hand, make an effort to play a combo
		if(gb.getLowestHand() < 5 && gb.firstMove) {
			if(debug) System.out.println("DEBUG: if it's first move and somebody has a low hand, make an effort to play a combo");
			for(int i = masterMoveList.size() - 1; i>=0; i--) {
				ArrayList<Card> move = new ArrayList<Card>();
				move.addAll(masterMoveList.get(i));
				if(move.size() < gb.getLowestHand() ) continue;
				if(gb.validMove(move)) {
					this.getHand().removeAll(move);
					reevaluateCards();
					return move;
				}
			}
		}
		
		//if somebody has a low hand and not first move, make an effort to play highest ranking combos first
		if(gb.getLowestHand() < 5 && this.getHand().size() >3 && !gb.firstMove) {
			if(debug) System.out.println("DEBUG: if somebody has a low hand and not first move, make an effort to play highest ranking combos first");
			for(int i = 0; i<masterMoveList.size(); i++) {
				ArrayList<Card> move = new ArrayList<Card>();
				move.addAll(masterMoveList.get(i));
				if(gb.validMove(move)) {
					this.getHand().removeAll(move);
					reevaluateCards();
					return move;
				}
			}
		}

//		//default behaviour
//		for(int i = masterMoveList.size() - 1; i>=0; i--) {
//			ArrayList<Card> move = new ArrayList<Card>();
//			move.addAll(masterMoveList.get(i));
//			if(gb.validMove(move)) {
//				this.getHand().removeAll(move);
//				reevaluateCards();
//				return move;
//			}
//		}
		//starting from the lowest rank (3), find moves where the highest card is that rank to play first.
		for(int rank = 13; rank>0; rank--) { 
			for(int i = 0 ; i<masterMoveList.size(); i++) {
				ArrayList<Card> move = new ArrayList<Card>();
				move.addAll(masterMoveList.get(i));
				Collections.sort(move);
				if(move.get(move.size()-1).getValueOrder() == rank) {
					if(gb.validMove(move)) {
						this.getHand().removeAll(move);
						reevaluateCards();
						return move;
					}
				}
			}
		}
		
		//before passing, if someone is about to win, try to play the highest single card no matter what
		if(gb.getLowestHand()<4 && this.getHand().size() >2) {
			if(debug) System.out.println("before passing, if someone is about to win, try to play the highest single card no matter what");
			ArrayList<Card> item = new ArrayList<Card>();
			item.add(this.getHand().get(this.getHand().size()-1));
			if(gb.validMove(item)) {
				this.getHand().removeAll(item);
				reevaluateCards();
				return item;
			}
		}
		
		return null;
	}
	
	

	
	private void reevaluateCards() {
		clearLists();
		getCombos();
		gatherAllMoves();
	}
	
	private void clearLists() {
		singles.clear();
		pairs.clear();
		triples.clear();
		combos.clear();
		masterMoveList.clear();
	}
	
	private void getCombos(){
		clearLists();
		//singles will be found(removed) along the way by leftovers
		singles.addAll(this.getHand());
		//search for ROYAL FLUSHES / FOURS / FULL / FLUSH / STRAIGHT
		List<Set<Card>> permutations = getSubsets(this.getHand(),5);
		for(Set<Card> permutation : permutations) {
			ArrayList<Card> hand = new ArrayList<Card>();
			hand.addAll(permutation);
			Pair<Combo,Card> combo = comboClassifier(hand);
			if(combo != null) {
				this.combos.add(new Pair(combo, hand));
				singles.removeAll(hand);
			}
		}
		//sort combos by combo rank then high card rank if tie
		//TODO: case where full house extra pairs tie breaker, i.e. 55533 vs 55522
		Collections.sort(combos, new Comparator<Pair<Pair<Combo,Card>,ArrayList<Card>>>(){
			@Override
			public int compare(Pair<Pair<Combo, Card>, ArrayList<Card>> arg0, Pair<Pair<Combo, Card>, ArrayList<Card>> arg1) {
				int result = arg0.getFirst().getFirst().getOrder() - arg1.getFirst().getFirst().getOrder();
				if(result == 0) {
					return arg0.getFirst().getSecond().getValueOrder() - arg1.getFirst().getSecond().getValueOrder();
				}else {
					return result;
				}
			}
		});

		//search for TRIPLES
		permutations.clear();
		permutations = getSubsets(this.getHand(),3);
		for(Set<Card> permutation : permutations) {
			ArrayList<Card> hand = new ArrayList<Card>();
			hand.addAll(permutation);
			if(hand.get(0).getValue() == hand.get(1).getValue() && hand.get(1).getValue() == hand.get(2).getValue()) {
				triples.add(hand);
				singles.removeAll(hand);
			}
		}
		Collections.sort(triples, new Comparator<ArrayList<Card>>() {
			@Override
			public int compare(ArrayList<Card> arg0, ArrayList<Card> arg1) {
				return arg0.get(0).getValueOrder() - arg1.get(0).getValueOrder();
			}
		});
		
		//search for PAIRS
		permutations.clear();
		permutations = getSubsets(this.getHand(),2);
		for(Set<Card> permutation : permutations) {
			ArrayList<Card> hand = new ArrayList<Card>();
			hand.addAll(permutation);
			if(hand.get(0).getValue() == hand.get(1).getValue()) {
				pairs.add(hand);
				singles.removeAll(hand);
			}
		}
		Collections.sort(pairs, new Comparator<ArrayList<Card>>() {
			@Override
			public int compare(ArrayList<Card> arg0, ArrayList<Card> arg1) {
				return arg0.get(0).getValueOrder() - arg1.get(0).getValueOrder();
			}
		});
		
	}
	
	private void gatherAllMoves() {
		masterMoveList.clear();
		for(Pair<Pair<Combo,Card>, ArrayList<Card>> comboitem: combos) {
			masterMoveList.add(comboitem.getSecond());
		}
		masterMoveList.addAll(triples);
		masterMoveList.addAll(pairs);
		Collections.sort(singles);
		for(int i = singles.size()-1; i>=0;i--) {
			ArrayList<Card> item = new ArrayList<Card>();
			item.add(singles.get(i));
			masterMoveList.add(item);
		}
	}
	
	//ncr permutation code
	//call getSubsets(set, r) to use
		private static void getSubsets(List<Card> superSet, int k, int idx, Set<Card> current,List<Set<Card>> solution) {
		    //successful stop clause
		    if (current.size() == k) {
		        solution.add(new HashSet<>(current));
		        return;
		    }
		    //unseccessful stop clause
		    if (idx == superSet.size()) return;
		    Card x = superSet.get(idx);
		    current.add(x);
		    //"guess" x is in the subset
		    getSubsets(superSet, k, idx+1, current, solution);
		    current.remove(x);
		    //"guess" x is not in the subset
		    getSubsets(superSet, k, idx+1, current, solution);
		}

		public static List<Set<Card>> getSubsets(List<Card> superSet, int k) {
		    List<Set<Card>> res = new ArrayList<>();
		    getSubsets(superSet, k, 0, new HashSet<Card>(), res);
		    return res;
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
		
		
		public static Set<Set<ArrayList<Card>>> powerSet(Set<ArrayList<Card>> originalSet) {
		    Set<Set<ArrayList<Card>>> sets = new HashSet<Set<ArrayList<Card>>>();
		    if (originalSet.isEmpty()) {
		        sets.add(new HashSet<ArrayList<Card>>());
		        return sets;
		    }
		    List<ArrayList<Card>> list = new ArrayList<ArrayList<Card>>(originalSet);
		    ArrayList<Card> head = list.get(0);
		    Set<ArrayList<Card>> rest = new HashSet<ArrayList<Card>>(list.subList(1, list.size())); 
		    for (Set<ArrayList<Card>> set : powerSet(rest)) {
		        Set<ArrayList<Card>> newSet = new HashSet<ArrayList<Card>>();
			    newSet.add(head);
		        newSet.addAll(set);
		        sets.add(newSet);
		        sets.add(set);
		    }       
		    return sets;
		}  
		
		private void test() {
			ArrayList<ArrayList<ArrayList<Card>>> nonDupeMasterMoveSet = new ArrayList<ArrayList<ArrayList<Card>>>();
			
			Set<ArrayList<Card>> setMasterMoveList = new HashSet<ArrayList<Card>>();
			setMasterMoveList.addAll(masterMoveList);
			Set<Set<ArrayList<Card>>> powerSetMML = powerSet(setMasterMoveList);
			for (Set<ArrayList<Card>> setCombo : powerSetMML) {
				boolean duplicate = false;
				ArrayList<Card> handCopy = new ArrayList<Card>(this.getHand());
				for(ArrayList<Card> move : setCombo) {
				     //if we do this.hand.remove(setCombo(i)) = x, we can check that x.size()== x.size() - setCombo(i).size() for non duplicates
					int prevSize = handCopy.size();
					handCopy.removeAll(move);
					if(handCopy.size() != prevSize - move.size()){
						duplicate = true;
						break;
					}
				}
				if(!duplicate) {
					ArrayList<ArrayList<Card>> moves = new ArrayList<ArrayList<Card>>();
					for(ArrayList<Card> move : setCombo) {
						moves.add(move);
					}
					nonDupeMasterMoveSet.add(moves);
				}
			}
			
			int min = 14;
			for(ArrayList<ArrayList<Card>> plays: nonDupeMasterMoveSet) {
				int singlesCount = this.getHand().size();
				for(ArrayList<Card> move : plays) {
					singlesCount = singlesCount - move.size();
				}
				if(singlesCount<min) {
					min = singlesCount;
					this.optimalMoveList.clear();
					this.optimalMoveList.addAll(plays);
					//optimalMoveList might not be full/complete!!!
					ArrayList<Card> handCopy = new ArrayList<Card>(this.getHand());
					for(ArrayList<Card> play : optimalMoveList) {
						for(Card card : play) {
							handCopy.remove(card);
						}
					}
					for(Card play : handCopy) {
						ArrayList<Card> move = new ArrayList<Card>();
						move.add(play);
						this.optimalMoveList.add(move);
					}
					//TODO: optimalMove list may not find full houses e.g. [[QS], [2S, 2H], [JS, JH], [9D], [KC, KD], [3D], [5C, 5D, 5H], [7C]]
				}
			}
			
			//we want to retain the optimal move list from the master move list to get the sorted version of the optimal one.
			//dont forget to add the singles
			ArrayList<ArrayList<Card>> optimalMoveListCopy = new ArrayList<ArrayList<Card>>(this.masterMoveList);
			optimalMoveListCopy.retainAll(this.optimalMoveList);
			//remove the ones already copied (should now only be the singles)
			this.optimalMoveList.removeAll(optimalMoveListCopy);
			Collections.sort(optimalMoveList, new Comparator<ArrayList<Card>>() {

				@Override
				public int compare(ArrayList<Card> arg0, ArrayList<Card> arg1) {
					return arg1.get(0).compareTo(arg0.get(0));
				}
				
			});
			optimalMoveListCopy.addAll(optimalMoveList);
			optimalMoveList.clear();
			optimalMoveList.addAll(optimalMoveListCopy);
			System.out.println("Optimal move list: " + this.optimalMoveList);

		}
}
