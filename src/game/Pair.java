package game;

public class Pair<T, U>{         
    public T t;
    public U u;

    public Pair(T t, U u) {         
        this.t= t;
        this.u= u;
     }
    
    public T getFirst() {
    		return this.t;
    }
    
    public U getSecond() {
    		return this.u;
    }
 }