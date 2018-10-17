package game;

public enum Combo{ROYAL_FLUSH(1),FOUR_OF_A_KIND(2),FULL_HOUSE(3),FLUSH(4),STRAIGHT(5);
		int order;
		
		Combo(int o){
			order = o;
		}
		
		public int getOrder(){
			return order;
		}
		
};