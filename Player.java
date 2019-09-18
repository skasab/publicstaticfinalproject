
public class Player extends GameObject
{
    private int deathCount;
    private int fireDelay;
    private int hasFired;
    private int level;
    private boolean reflectBulletsWall;
    private boolean tripleShot;
    private boolean homingShot;
    private boolean gotReward;
    private String[] abilities;
    private double moveDelay;
    private double hasMoved;
    private int deathAtSmart;

    public Player(char symbol, int id, int x, int y) {
        super(symbol, id, x, y);
        deathCount = 0;
        fireDelay = 25;
        moveDelay = 1;
        hasFired = 0;
        hasMoved = 0.0;
        level = 1;
        reflectBulletsWall = false;
        tripleShot = false;
        homingShot = false;
        abilities = new String[5];
        changeLevelBy(0); //to update the abilities
    }

    public void setFireDelay(int fireDelay) {
        this.fireDelay = fireDelay;
    }

    public boolean canMove()
    {
        return (hasMoved <= 0.0);
    }

    public void up() {
        if(canMove())
        {
            boolean canGo = true;
            super.up();
            if(getY() <= 0) {
                setY(1);
            } 
            for(Barrier b: Map.getBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            for(ReflectiveBarrier b: Map.getReflectiveBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            if(!canGo){
                super.down();
            }
            hasMoved = moveDelay;
        }
    }

    public void down() {
        if(canMove())
        {
            boolean canGo = true;
            super.down();
            if(getY() >= Map.getRows() - 1) {
                setY(Map.getRows() - 2);
            }
            for(Barrier b: Map.getBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            for(ReflectiveBarrier b: Map.getReflectiveBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            if(!canGo){
                super.up();
            }  
            hasMoved = moveDelay;
        }
    }

    public void right() {
        if(canMove())
        {
            boolean canGo = true;
            super.right();
            if(getX() >= Map.getCols())
                setX(Map.getCols() - 1);    
            if(getTeam() == 1 && getX() == Map.getTurfLine()) {
                setX(Map.getTurfLine() - 1);
            }
            for(Barrier b: Map.getBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            for(ReflectiveBarrier b: Map.getReflectiveBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            if(!canGo){
                super.left();
            }    
            hasMoved = moveDelay;
        }
    }

    public void left() {
        if(canMove())
        {
            boolean canGo = true;
            super.left();
            if(getX() < 0)
                setX(0);
            if(getTeam() == 2 && getX() == Map.getTurfLine()) {
                setX(Map.getTurfLine() + 1);
            }
            for(Barrier b: Map.getBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            for(ReflectiveBarrier b: Map.getReflectiveBarriers()){
                if(occupying(b)){
                    canGo = false;
                }
            }
            if(!canGo){
                super.right();
            }    
            hasMoved = moveDelay;
        }
    }

    public boolean occupying(int x, int y){
        return x == getX() && (y - getY() <= 1 && y - getY() >= -1);
    }

    //     public boolean occupying(GameObject input){
    //         return occupying(input.getX(), input.getY());
    //     }

    public void changeLevelBy(int i){      
        level += i;
        if (i > 0) {
            GameController.getSound(13).play();
            Statistics.upLevelUpsPicked();
        } 

        if(level >= 1) {
            abilities[0] = "     [Basic Shot]     ";
        } else {
            abilities[0] = "                      ";
        }

        if(level >= 4) {
            if(fireDelay == 25) {
                GameController.getSound(14).play();
            }
            fireDelay = 15;
            abilities[1] = "     [Speedy Shot]    ";
            //GameController.getSound(14).play();
        } else {
            fireDelay = 25;
            abilities[1] = "                      ";
        }

        if(level >= 8) {
            if(!reflectBulletsWall) {
                GameController.getSound(14).play();
            }
            reflectBulletsWall = true;
            abilities[2] = " [Reflective Bullets] ";
        } else {
            reflectBulletsWall = false;
            abilities[2] = "                      ";
        }

        if (gotReward) {
            homingShot = true;
            tripleShot = false;
            abilities[3] = "   [S.M.A.R.T Shot]   ";
        } else if(level >= 12) {
            if(!tripleShot) {
                GameController.getSound(14).play();
            }
            tripleShot = true;
            homingShot = false;
            abilities[3] = "     [Triple Shot]    ";
            //GameController.getSound(14).play();
        } else {
            tripleShot = false;
            homingShot = false;
            abilities[3] = "                      ";
        } 

        abilities[4] = "                      ";

        // 
        //         if(level >= 18) {
        //             homingShot = true;
        //             tripleShot = false;
        //             abilities[3] = "[S.M.A.R.T Shot]";
        //         } else if(level >= 12) {
        //             homingShot = false;
        //             abilities[3] = " [Triple Shot]  ";
        //         }
        //         else {
        //             homingShot = false;
        //             abilities[3] = "                ";
        //         }

        if(Map.getIsSuddenDeath()) {
            fireDelay = 2;
        }
    }

    public void robotReward() {
        gotReward = true;
        changeLevelBy(0);
        GameController.getSound(14).play();
        deathAtSmart = deathCount;
    }

    public void shoot() {
        changeLevelBy(0);
        if(hasFired <= 0){
            if (tripleShot)
            {
                shoot3Ways();
                Statistics.upBulletsFired();
                Statistics.upBulletsFired();
                Statistics.upBulletsFired();
            }
            else
            {
                if(getTeam() == 1) {
                    Map.addBullet('>', getID(), getX() + 1, getY());
                    GameController.getSound(0).play();
                    hasFired = fireDelay;
                }
                else if(getTeam() == 2){
                    Map.addBullet('<', getID(), getX() - 1, getY());
                    GameController.getSound(1).play();
                    hasFired = fireDelay;
                }
                Statistics.upBulletsFired();
            }
        }
        Map.handleCollisions(); //immediately check if the bullet is fired on a barrier
    }

    public void shoot3Ways() {
        if(getTeam() == 1) {
            Map.addBullet('>', getID(), getX() + 1, getY() - 1);
            Map.addBullet('>', getID(), getX() + 1, getY());
            Map.addBullet('>', getID(), getX() + 1, getY() + 1);
            GameController.getSound(0).play();
            hasFired = fireDelay;
        }
        else if(getTeam() == 2){
            Map.addBullet('<', getID(), getX() - 1, getY() - 1);
            Map.addBullet('<', getID(), getX() - 1, getY());
            Map.addBullet('<', getID(), getX() - 1, getY() + 1);
            GameController.getSound(1).play();
            hasFired = fireDelay;
        }
    }

    public void die() {
        GameController.getSound(3).play();
        setY((int)((Math.random() * (Map.getRows() - 2) + 1)));
        if(level > 1) {
            changeLevelBy(-1);
        }
        if(getTeam() == 1)
            setX(0);
        else if(getTeam() == 2) {
            setX(Map.getCols() - 1);
        }
        if(getTeam() == 1){
            if(getID()==1){
                if(collidesYPlayer(Map.getPlayer(1).getY())||(Math.abs((getY()-((Map.getRows()/2))))<=3))
                {
                    setY((int)((Math.random() * (Map.getRows() - 2) + 1)));
                }
            }else{
                if(collidesYPlayer(Map.getPlayer(0).getY())||(Math.abs((getY()-((Map.getRows()/2))))<=3))
                {
                    setY((int)((Math.random() * (Map.getRows() - 2) + 1)));
                }
            }
        }else if(getTeam() == 2){
            if(getID()==3){
                if(collidesYPlayer(Map.getPlayer(3).getY())||(Math.abs((getY()-((Map.getRows()/2))))<=3))
                {
                    setY((int)((Math.random() * (Map.getRows() - 2) + 1)));

                }
            }else{
                if(collidesYPlayer(Map.getPlayer(2).getY())||(Math.abs((getY()-((Map.getRows()/2))))<=3))
                {
                    setY((int)((Math.random() * (Map.getRows() - 2) + 1)));
                }
            }
        }
        if(getTeam() == 1) {
            Map.moveTurf(2, Map.getTurfChange());
        }
        else if(getTeam() == 2){
            Map.moveTurf(1, Map.getTurfChange());
        }
        //GameController.getSound(2).play();
        deathCount++;
        if(deathCount > deathAtSmart) {
            gotReward = false;
            changeLevelBy(0);
        }
        Statistics.upDeathCount();
    }

    public boolean collidesXPlayer(int x){
        return x == getX();
    }

    public boolean collidesYPlayer(int y){
        return Math.abs(y - getY()) <= 2;
    }

    public boolean collidesX(int x){
        return x == getX();
    }

    public boolean collidesY(int y){
        return Math.abs(y - getY()) <= 1;
    }

    public boolean occupying(GameObject obj) {
        return collidesX(obj.getX()) && collidesY(obj.getY());
    }

    public int getDelay(){return fireDelay;}

    public int getLevel(){return level;}

    public void setDelay(int input){fireDelay = input;}

    public void setMoveDelay(int input){moveDelay = input;}

    public int getHasFired(){return hasFired;}

    public double getHasMoved(){return hasMoved;}

    public void setHasFired(int input){hasFired = input;}

    public void setHasMoved(int input){hasMoved = input;}

    public void resetHasFired(){hasFired = fireDelay;}

    public void resetHasMoved(){hasMoved = moveDelay;}

    public void tickHasFired(){hasFired--;}

    public void tickHasMoved(){hasMoved--;}

    public int getDeathCount() {
        return deathCount;
    }

    public boolean getWallReflect() {
        return reflectBulletsWall;
    }

    public boolean getHomingShot() {
        return homingShot;
    }

    public String[] getAbilities() {
        return abilities;
    }

    public Player closer(Player a, Player b) {
        if(Map.getDistance(this, a) <= Map.getDistance(this, b)) {
            return a;
        }
        else {
            return b;
        }
    }
}
