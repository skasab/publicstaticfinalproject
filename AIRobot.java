
public class AIRobot extends GameObject
{
    private int target;
    private char leftSym = '>';
    private char rightSym = '~';
    private int health;    
    public AIRobot() {
        super('╋',5,30, Map.getRows()/2); 
        target = 0;
        health = 20;
    }

    public void nextMove() {
        if(Map.isRobot()) {
            //GameController.getSound(10).stop();
            Player p = Map.getPlayer(target);
            if( getY() == p.getY() ) {
                if (target == 0 || target == 1) {
                    Map.addBullet('<', 5, getX() + 2, getY(),6);
                } else {
                    Map.addBullet('>', 5, getX() + 2, getY(),2);
                }
                GameController.getSound(10).stop();
                GameController.getSound(10).play();
                nextTarget();
            } else {
                if (getY() < p.getY()) {
                    setY(getY() + 1);
                } else {
                    setY(getY() - 1);
                }
            }
        }
    }

    public void nextTarget() {
        //switch off targets between teams
        double t = Math.random();
        if(t <= .60) {
            if(Map.getTurfLine() > Map.getCols()/2) {
                if (target == 0) { // 1423 -> 0312
                    target = 1;
                } else if (target == 1) {
                    target = 0;
                }  
                else if(target == 2) {
                    target = 1;
                }
                else {
                    target = 0;
                }
                switchSym();
            }
            else if(Map.getTurfLine() < Map.getCols()/2 ){
                if (target == 2) { // 1423 -> 0312
                    target = 3;
                } else if (target == 3) {
                    target = 2;
                }  
                else if(target == 0) {
                    target = 2;
                }
                else {
                    target = 3;
                }
                switchSym();
            }
        }
        else {
            if (target == 0) { // 1423 -> 0312
                target = 2;
            } else if (target == 1) {
                target = 3;
            }  
            else if(target == 3) {
                target = 1;
            }
            else {
                target = 0;
            }

        }

    }

    public char getLeftSym() {
        return leftSym;
    }

    public char getRightSym() {
        return rightSym;
    }

    public void switchSym() {
        if (leftSym == '>') {
            leftSym = '~';
            rightSym = '<';
        } else {
            leftSym = '>';
            rightSym = '~';
        }
    }

    public void decreaseHealth() {
        health--;
        if(health < 0) {
            health = 0;
        }
    }

    public void setHealth(int h) {
        health = h;
    }

    public int getHealth() {
        return health;
    }
}
