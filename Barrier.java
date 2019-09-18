
/**
 * Write a description of class Barrier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Barrier extends GameObject
{
    private int health; 
    private boolean isBreakable;
    private int direction;
    public Barrier(char symbol, int id, int x, int y, boolean breakable)
    {
        super(symbol,id,x,y);
        if(breakable) {
            health = 3;
        }
        else {
            health = Integer.MAX_VALUE;
        }
        isBreakable = breakable;
    }
    public Barrier(int x, int y){
        super(Symbols.b0, 10, x, y);
        health = Integer.MAX_VALUE;
        isBreakable = false;
    }
    
    public Barrier(char symbol, int x, int y){
        super(symbol, 10, x, y);
        health = Integer.MAX_VALUE;
        isBreakable = false;
    }

    public int getHealth() {
        return health;          
    }

    public boolean getBreak() {
        return isBreakable;
    }
    
    public void decrementHealth() {
        health--;
    }
}
