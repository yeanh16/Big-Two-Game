public class Pair<Combo, Card> {         
    public Combo t;
    public Card u;

    public Pair(Combo t, Card u) {         
        this.t= t;
        this.u= u;
     }
    
    public Combo getCombo() {
    		return this.t;
    }
    
    public Card getCard() {
    		return this.u;
    }
 }