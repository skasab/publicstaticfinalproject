
/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bullet extends GameObject
{
    private int direction;//clockwise, 0 is up
    private int originalDirection;
    private int homeDelayCounter;     //only used if homing
    private int homeDelayMax;  //only used if homing
    private boolean homing;
    private int reflections;

    public Bullet(char symbol, int id, int x, int y) {
        super(symbol, id, x, y); 
        if(getTeam() == 1){
            direction = 2;
            originalDirection = 2;
        }else{
            direction = 6;
            originalDirection = 6;
        }
        homeDelayCounter = 1;
        homeDelayMax = 1;
        homing = false;
        reflections = 0;
    }
    public Bullet(char symbol, int id, int x, int y, int direction) {
        super(symbol, id, x, y); 
        this.direction = direction;
        this.originalDirection = direction;
        homeDelayCounter = 1;
        homeDelayMax = 1;
        homing = false;
    }
    public Bullet(char symbol, int id, int x, int y, int direction, int reflections) {
        super(symbol, id, x, y); 
        this.direction = direction;
        this.originalDirection = direction;
        homeDelayCounter = 1;
        homeDelayMax = 1;
        homing = false;
        this.reflections = reflections;
    }
    
    public void update(){
        fixSymbol();
        // 7  0  1
        // 6  +  2
        // 5  4  3
        
        //uncomment to disable bullets
        //direction = 8;
        switch (direction) {
            case 0:   up();             break;
            case 1:   up(); right();    break;
            case 2:   right();          break;
            case 3:   down(); right();  break;
            case 4:   down();           break;
            case 5:   left(); down();   break;
            case 6:   left();           break;
            case 7:   left(); up();     break;
        }
    }
 
    public int getDirection(){
        return direction;
    }

    public void changeDirection(int newDir) {
        direction = newDir;
    }
    
    public int getOriginalDirection() {
        return originalDirection;
    }
    
    public void changeOriginalDirection(int newDir) {
        originalDirection = newDir;
    }

    public void fixSymbol() {
        switch(direction) {
            case 0:  setSymbol('Λ');  break;
            case 2:  setSymbol('>');  break;
            case 4:  setSymbol('V');  break;
            case 6:  setSymbol('<');  break;
        }
    }

    public void homeLR(GameObject toHome) {
        if(getX() < toHome.getX()) {
            direction = 2;
        }
        else if(getX() > toHome.getX()){
            direction = 6;
        }
        else {
            //nothing
        }
    }

    public void homeUD(GameObject toHome) {
        if(getY() < toHome.getY()) {
            down();
        }
        else if(getY() > toHome.getY()) {
            up();
        }
        else {
            //nothing
        }
    }

    public void home(Player toHome) {
        //homeLR(toHome);
        //update();
        homing = true;
        homeDelayCounter--;
        direction = originalDirection;
        if (direction == 2 && Map.checkBarrier(getY(), getX() + 2)) {
            direction = 4;
        } else if (direction == 6 && Map.checkBarrier(getY(), getX() - 2)) {
            direction = 4;
        } else if (homeDelayCounter == 0) {
            homeUD(toHome);
            homeDelayMax += 2;
            homeDelayCounter = homeDelayMax;
        }
        update();
    }

    public boolean isHoming() {
        return homing;
    }
    
    public void setHoming(boolean homing) {
        this.homing = homing;
    }
    
    public int getReflections() {
        return reflections;
    }

}
