package game;

public enum Value{TWO(1),ACE(2),KING(3),QUEEN(4),JACK(5),TEN(6),NINE(7),EIGHT(8),SEVEN(9),SIX(10),FIVE(11),FOUR(12),THREE(13);
		int order;
		
		Value(int o){
			order = o;
		}
		
		int getOrder(){
			return order;
		}
		
		public int getFace() {
			switch(order) {
			case 1: return 2;
			case 2: return 1;
			case 3: return 13;
			case 4: return 12;
			case 5: return 11;
			case 6: return 10;
			case 7: return 9;
			case 8: return 8;
			case 9: return 7;
			case 10: return 6;
			case 11: return 5;
			case 12: return 4;
			case 13: return 3;
			default: return 0;
			}
		}

	};