
public class GameObject
{
    private char symbol;
    private int id;
    private int x;
    private int y;
    
    public GameObject(char symbol, int id, int x, int y) {
        this.symbol = symbol;
        this.id = id;
        this.x = x;
        this. y = y;
    }
    public void up() {
        y--;        
    }
    public void down() {
        y++;       
    }
    public void right() {
        x++;        
    }
    public void left() {
        x--;       
    }
    
    public void setID(int id) {
        this.id = id;
    }
    public int getID() {
        return id;
    }
    public int getTeam() {
        if (id == 1 || id == 2) {
            return 1;
        } else if (id == 3 || id == 4) {
            return 2;
        }
        else {
            return 0;
        }
    }
    
    
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public char getSymbol() {
        return symbol;
    }
}
