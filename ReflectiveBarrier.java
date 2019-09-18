
/**
 * Write a description of class ReflectiveBarrier here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ReflectiveBarrier extends Barrier
{
    // instance variables - replace the example below with your own
    private int tilt;//2 = -, 0 = |, 1 = /, 3 = \;
    /**
     * Constructor for objects of class ReflectiveBarrier
     */
    public ReflectiveBarrier(int x, int y, int tilt)
    {
        super(x,y);
        char symbol;
        this.tilt = tilt;
        if(tilt == 0){
            symbol = Symbols.tilt0;
        }else if(tilt == 1){
            symbol = Symbols.tilt1;
        }else if(tilt == 2){
            symbol = Symbols.tilt2;
        }else if(tilt == 3){
            symbol = Symbols.tilt3;
        }else{
            symbol = 'E';
        }
        setSymbol(symbol);
    }

    public Bullet reflect(Bullet b){
        Bullet output;
        int oldDirection = b.getDirection();
        int angleInflection = (oldDirection-tilt)%4;
        int newDirection = oldDirection + calculateChange(oldDirection);
        if(newDirection<0){
            newDirection += 8;
        }
        newDirection %= 8;
        output = new Bullet(b.getSymbol(),b.getID(),b.getX(),b.getY(),newDirection, b.getReflections() + 1);
        output.update();
        return output;
    }

    public int calculateChange(int coming){
        if(tilt == 0 || tilt == 2){
            coming -= tilt;
            if(coming < 0 ){
                coming = 8 + coming;
            }
            if(coming == 1 || coming == 5){
                return 2;
            }
            if(coming == 2 || coming == 6){
                return 4;
            }
            if(coming == 3 || coming == 7){
                return -2;
            } 
            return 0;
        }
        else
        if(tilt == 1){
            if(coming == 1 || coming == 5){
                return 0;
            }
            if(coming == 2 || coming == 6){
                return -2;
            }
            if(coming == 3 || coming == 7){
                return 4;
            } 
            if(coming == 4 || coming == 0){
                return 2;
            } 
        }
        else if(tilt == 3){
            if(coming == 1 || coming == 5){
                return 0;
            }
            if(coming == 4 || coming == 0){
                return 6;
            }
            if(coming == 3 || coming == 7){
                return 4;
            } 
            if(coming == 6 || coming == 2){
                return 2;
            } 
        }
        return 0;
    }
}
