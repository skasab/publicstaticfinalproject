
import java.util.*;
import java.lang.StringBuilder;

public class Map
{
    //Map will never be constructed, because there would only be one
    //instance of it anyways. Thus, all methods/fields will be static.

    // Misc. Variables
    private static int rows = 30, cols = 61;
    private static List<Bullet> bullets = new ArrayList<Bullet>();
    private static List<Barrier> barriers = new ArrayList<Barrier>();
    private static List<LevelUp> levelUps = new ArrayList<LevelUp>();
    private static List<Portal> portals = new ArrayList<Portal>();
    private static List<ReflectiveBarrier> reflectiveBarriers = new ArrayList<ReflectiveBarrier>();
    private static Player[] players = new Player[4]; //player number is (index - 1)
    private static char[][] background = new char[rows][cols]; //only the map (background)
    private static char[][] fullMap = new char[rows][cols]; //all the GameObjects on the map
    private static char[] symbols = new char[4]; //player i uses symbol symbols[i-1]. 
    private static AIRobot robot = new AIRobot();

    // Turf Line
    private static int turfLine = background[0].length / 2;
    private static int turfChange; //how much the turf line changes by for every death

    // Symbols
    private static final char team1Sym = ' ';
    private static final char team2Sym = ' ';
    private static char turfSym  = '|';
    private static final char unBreakBarSym   = '‖';
    private static final char breakBarSym   = '¦';

    // Sudden Death
    private static boolean isSuddenDeath; //is it currently in sudden death mode?
    private static boolean isSuddenDeathMusicPlaying; //did the sudden death music start yet?
    private static String suddenDeathMusicTime = "0m57s"; //should always be 3 seconds less than suddenDeathTime
    private static String suddenDeathTime = "1m0s";

    //Robot
    private static boolean isRobot;
    private static boolean isRobotMusicPlaying;
    private static String robotMusicTime = "0m36s"; //4 s less than robotTime
    private static String robotTime = "0m40s";
    private static boolean robotFlicker;

    // Options
    private static int maxBSize = 999;
    private static boolean friendlyFire = false;

    // Other
    private static boolean team1Win = false;
    private static boolean team2Win = false;
    private static String ls = System.getProperty("line.separator");

    public static void reset() {
        isSuddenDeath = false;
        isSuddenDeathMusicPlaying = false;
        isRobot = false;
        isRobotMusicPlaying = false;
        robotFlicker = false;
        bullets.clear();
        barriers.clear();
        levelUps.clear();
        players = new Player[4]; //player number is (index - 1)
        background = new char[rows][cols]; //only the map (background)
        fullMap = new char[rows][cols]; //all the GameObjects on the map
        symbols = new char[4]; //player i uses symbol symbols[i-1]. 

        turfChange = 1;
        turfLine = background[0].length / 2;

        team1Win = false;
        team2Win = false;

    }

    public static void setMap(char[][] newMap) {
        fullMap = newMap;
    }

    public static void setSymbols(char[] s) {
        symbols = s;
    }

    public static void transfer()
    {
        for(int r = 0; r < getRows(); r++)
        {
            for(int c = 0 ; c < getCols(); c++)
            {
                fullMap[r][c] = background[r][c];  
            }
        } 
    }

    public static void suddenDeath() {
        turfChange = 5;
        for (Player p : players) {
            p.setFireDelay(2);
        }
        GameController.suddenDeath();
        //         turfSym = '|';
        //         moveTurf(1,1);
        //         moveTurf(2,1);
        //transfer();
        isSuddenDeath = true;
        robot.setHealth(100);
        isRobot = true;
        GameController.setPUPDelay(10000);
        Statistics.upSuddenDeath();
    }

    public static void robot() {
        turfChange = 3;
        //turfSym = '|';
        GameController.robot();
        //         moveTurf(1,1);
        //         moveTurf(2,1);
        isRobot = true;
        GameController.setPUPDelay(65);
        //         Object a = null;
        //         a.equals(a);
        Statistics.upRobotsActivated();
    }

    public static void normal() {
        turfChange = 1;
        turfSym = '|';
        moveTurf(1,1);
        moveTurf(2,1);
        isRobot = false;
        GameController.setPUPDelay(125);
        isRobotMusicPlaying = false;

    }

    //updates fullMap using positions from the players and bullets lists.
    public static void update() {
        String currentTime = GameController.getTime(GameController.getOriginalTime(), System.currentTimeMillis());
        if (currentTime.equals(suddenDeathMusicTime) && !isSuddenDeathMusicPlaying ) {
            GameController.startSuddenDeathMusic();
        }
        if (currentTime.equals(suddenDeathTime) ) {
            //if the current time is equal to the sudden death starting time:
            suddenDeath();

        }
        if (currentTime.equals(robotMusicTime) && !isRobotMusicPlaying ) {
            GameController.stopAllMusic();
            GameController.getSound(8).play();
        }
        if (currentTime.equals(robotTime) ) {
            //if the current time is equal to the robot starting time:
            GameController.startRobotMusic();
            GameController.getSound(9).play();
            robot();

        }
        for(LevelUp l: levelUps){
            try {
                fullMap[l.getY()][l.getX()] = l.getSymbol();
            }
            catch(ArrayIndexOutOfBoundsException e) { }
        }    
        for(Portal p: portals){
            try {
                fullMap[p.getY()][p.getX()] = p.getSymbol();
            }
            catch(ArrayIndexOutOfBoundsException e) { }
        } 
        for(Player p : players) {
            try{
                fullMap[p.getY() - 1][p.getX()]  = '@';
                fullMap[p.getY()][p.getX()]      = p.getSymbol();
                fullMap[p.getY() + 1][p.getX()]  = '@';

                try{
                    if(p.getTeam() == 1 && p.getX() >= turfLine)
                    {
                        p.setX(turfLine/2);
                    }
                    else if((p.getTeam() == 2 && p.getX() <= turfLine)) {
                        p.setX(turfLine/2 + turfLine);
                    }
                }
                catch(ArrayIndexOutOfBoundsException e) { }
            }
            catch(ArrayIndexOutOfBoundsException e) { }
        }
        for(int bIndex = bullets.size() - 1; bIndex >= 0; bIndex-- ) {
            Bullet b = bullets.get(bIndex);

            if(b.getID() <= 4 && b.getID() >= 0 && players[b.getID() - 1].getHomingShot()) {
                if(b.getTeam() == 1) {
                    b.home(players[b.getID()-1].closer(players[2],players[3]));
                }
                else {
                    b.home(players[b.getID()-1].closer(players[0],players[1]));
                }
            } else {    
                b.update();
            }
            if (b.getReflections() >= 6) {
                bullets.remove(bIndex);
                continue;
            }

            if((b.getID() <= 4 && b.getID() >= 0) && (b.getX() < 0 || b.getX() > getCols() - 1 || b.getY() < 0 || b.getY() > getRows() - 1) 
            && players[b.getID() - 1].getWallReflect()) {
                if(b.getOriginalDirection() == 2 || b.getDirection() == 2) {
                    b.changeOriginalDirection(6);
                    b.changeDirection(6);
                }
                else {
                    b.changeOriginalDirection(2);
                    b.changeDirection(6);
                }
            }            
            else if (b.getX() < 0 || b.getX() > getCols() - 1 || b.getY() < 0 || b.getY() > getRows() - 1) {
                bullets.remove(bIndex);
                continue;
            }
            else if(b.getID() <= 4 && b.getID() >= 0 && players[b.getID() - 1].getWallReflect()) {
                if(b.getX() == turfLine) {
                    if(b.getTeam() + 5 == b.getDirection()) {
                        bullets.remove(bIndex);
                        continue;
                    }
                    else if(b.getTeam() == b.getDirection()) {
                        bullets.remove(bIndex);
                        continue;
                    }
                }  
                fullMap[b.getY()][b.getX()] = b.getSymbol();
            }
            else {
                fullMap[b.getY()][b.getX()] = b.getSymbol();
            }

        }
        for(int bar = barriers.size() - 1; bar >= 0; bar--) {
            Barrier b = barriers.get(bar);
            try {
                if(b.getHealth() <= 0) {    
                    GameController.getSound(5).play();
                    barriers.remove(bar);
                    continue;
                }
                else if(b.getBreak()) {
                    fullMap[b.getY()][b.getX()] = breakBarSym;
                }
                else {
                    fullMap[b.getY()][b.getX()] = unBreakBarSym;
                } 
            }
            catch (Exception e) { } 
        }
        for(int bar = reflectiveBarriers.size() - 1; bar >= 0; bar--){
            ReflectiveBarrier b = reflectiveBarriers.get(bar);
            try{
                fullMap[b.getY()][b.getX()] = reflectiveBarriers.get(bar).getSymbol();
            }
            catch(Exception e){
            }
        }

        try{
            if(robotFlicker) {
                fullMap[robot.getY() - 1][robot.getX()] = ' ';
                fullMap[robot.getY()][robot.getX()] = ' ';
                fullMap[robot.getY() + 1][robot.getX()] = ' ';
                robotFlicker = false;
            }
            else {
                fullMap[robot.getY() - 1][robot.getX()] = 'o';
                fullMap[robot.getY()][robot.getX()] = '!';
                fullMap[robot.getY() + 1][robot.getX()] = '¥';
            }
        }
        catch(Exception e) {}
    }

    public static void handleCollisions() {
        for (int p = 0; p < players.length; p++) {
            for (int b = bullets.size() - 1; b >= 0; b--) {
                if ( areColliding(bullets.get(b), players[p]) ) {
                    //bullet-player collisions (kill the player)
                    bullets.remove(b);
                    players[p].die();
                    if(friendlyFire && (players[p].getTeam() == bullets.get(b).getTeam())) {
                        Statistics.upFriendlyFire();
                    }
                }
            }

            for (int p2 = p + 1; p2 < players.length; p2++) {
                if ( areColliding(players[p], players[p2]) ) {
                    //player-player collisions (kill both)
                    players[p].die();
                    players[p2].die();
                }
            }

            for(int i = 0; i < levelUps.size(); i++){
                if(areColliding(levelUps.get(i), players[p])){
                    levelUps.remove(i);
                    players[p].changeLevelBy(1);

                }
            }
        }

        for (int b = bullets.size() - 1; b >= 0; b--) {
            for (int b2 = b - 1; b2 >= 0; b2--) {
                try {
                    if (areColliding(bullets.get(b), bullets.get(b2)) ) {
                        //bullet-bullet collisions (remove both bullets)
                        bullets.remove(b);
                        b--;
                        bullets.remove(b2);
                        b--;
                        b2--;
                    }
                } catch ( IndexOutOfBoundsException e ) { }
            }                   
        }

        for (int b = bullets.size() - 1; b >= 0; b--) {
            for(int bar = barriers.size() - 1; bar >= 0; bar--)
            {
                try {
                    if (areColliding(bullets.get(b), barriers.get(bar)) ) {
                        //bullet-barrier collisions (decrement barrier health)
                        bullets.remove(b);
                        barriers.get(bar).decrementHealth();
                        if(!(barriers.get(bar).getBreak())) {
                            GameController.getSound(4).play();
                        }
                        else if(barriers.get(bar).getHealth() > 0) {
                            GameController.getSound(3).play();
                        }

                    }      
                }
                catch ( IndexOutOfBoundsException e ) { }
            }            
        }

        for (int b = bullets.size() - 1; b >= 0; b--) {
            for (int l = levelUps.size() - 1; l >= 0; l--) {
                try {
                    if (areColliding(bullets.get(b), levelUps.get(l))) {
                        bullets.remove(b);
                        levelUps.remove(l);
                        GameController.getSound(2).play();
                    }
                }
                catch ( IndexOutOfBoundsException e ) { }
            }
        }

        for (int b = bullets.size() - 1; b >= 0; b--) {
            for (ReflectiveBarrier r: reflectiveBarriers) {
                if(areColliding(bullets.get(b),r) && !bullets.get(b).isHoming()){
                    bullets.set(b,r.reflect(bullets.get(b)));
                }
            }
        }
        for (int P = portals.size() - 1; P >= 0; P--) {
            for (int p = 0; p < players.length; p++) {
                if ( areColliding(portals.get(P), players[p]) ) {
                    randomPlace(players[p]);
                }
            }
            for (int b = bullets.size() - 1; b >= 0; b--) {
                if ( areColliding(portals.get(P), bullets.get(b)) ) {
                    randomPlace(bullets.get(b));
                }
            }
        }

        for(int b = bullets.size() - 1; b>=0; b--) {
            if(isRobot) {
                if(areColliding(bullets.get(b),robot) && bullets.get(b).getID() >= 0 && bullets.get(b).getID() <= 4)
                {
                    robotFlicker = true;
                    robot.decreaseHealth();
                    GameController.getSound(11).play();
                    if(robot.getHealth() <= 0) {
                        GameController.getSound(12).play();
                        GameController.stopRobotMusic();
                        players[bullets.get(b).getID() - 1].robotReward();
                        normal();
                        Statistics.upRobotsKilled();
                    }
                    bullets.remove(b);
                }
            }
        }
    }

    //returns true if two players are overlapping, false otherwise.
    private static boolean areColliding(Player p1, Player p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) < 3;
    }

    private static boolean areColliding(GameObject p1, Player p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getX() == p2.getX() && Math.abs(p1.getY() - p2.getY()) < 3;
    }
    
    private static boolean areColliding(Portal p1, Bullet p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getX() == p2.getX() && p1.getY() == p2.getY();
    }
    //returns true if a bullet and a player are overlapping, false otherwise.
    //friendly fire prevents same team kills
    private static boolean areColliding(Bullet b, Player p) {
        if (b == null || p == null) {
            return false;
        }
        boolean bool = p.getX() == b.getX() && Math.abs(p.getY() - b.getY()) < 2;
        if(friendlyFire) {
            return bool;
        }
        else {
            return bool && b.getTeam() != p.getTeam();
        }
    }

    //returns true if a bullet and a bullet are overlapping, false otherwise.
    private static boolean areColliding(Bullet b1, Bullet b2) {
        if (b1 == null || b2 == null) {
            return false;
        }
        boolean bool = Math.abs(b1.getX() - b2.getX()) < 2 && b1.getY() == b2.getY();
        if(friendlyFire) {
            return bool;
        }
        else {
            return bool && b1.getTeam() != b2.getTeam();
        }
    }

    private static boolean areColliding(Bullet b, Barrier bar) {
        if (b == null || bar == null) {
            return false;
        }
        return Math.abs(b.getX() - bar.getX()) < 1 && Math.abs(b.getY() - bar.getY()) < 1;
    }

    private static boolean areColliding(Bullet b, LevelUp l) {
        if (b == null || l == null) {
            return false;
        }
        return b.getX() == l.getX() && b.getY() == l.getY();
    }

    private static boolean areColliding(Bullet b, AIRobot r) {
        if (b == null || r == null) {
            return false;
        }
        return Math.abs(b.getY() - r.getY()) <= 1 && Math.abs(b.getX() - r.getX()) <= 1;
    }

    public static boolean checkBarrier(int y, int x) {
        for (Barrier b : barriers) {
            if (b.getX() == x && b.getY() == y) {
                return true;
            }
        }
        for (ReflectiveBarrier b : reflectiveBarriers) {
            if (b.getX() == x && b.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public static int getRandX(int team) {
        int randX;
        if(team == 1) {
            randX = ((int) (Math.random() * turfLine));
            while(players[0].collidesX(randX) || players[1].collidesX(randX))  {
                randX = (int) (Math.random() * turfLine);
            }
        }
        else {
            randX = turfLine + 1 + (int)(Math.random() * (((Map.getCols() - 1 - (turfLine + 1)) + 1)));
            while(players[2].collidesX(randX) || players[3].collidesX(randX))  {
                randX = (int) (Math.random() * turfLine);
            }
        }

        return randX;
    }

    public static int getRandY() {
        int randY;
        randY = ((int) (Math.random() * Map.getRows() - 2) + 1);
        while(players[0].collidesY(randY) || players[1].collidesY(randY) || players[2].collidesY(randY) || players[3].collidesY(randY)) {
            randY = ((int) (Math.random() * Map.getRows() - 2) + 1);
        }
        return randY;
    }
    //returns int value - 1 means team 1 has won, 2 means team 2 has won, and 3 if neither.
    public static int getWinner() {
        if(team1Win) {
            return 1;
        }
        else if(team2Win) {
            return 2;
        }
        return 0;
    }

    //builds the original array with a line in the middle
    public static void buildBackgroundArray() {
        for(int r = 0; r < background.length; r++)
        {
            for(int c = 0; c < background[0].length; c++)
            {
                if(c == turfLine) {
                    background[r][c] = turfSym;
                } else if(c < turfLine) {
                    background[r][c] = team1Sym;
                } else {
                    background[r][c] = team2Sym;
                }

            }
        }
    }

    //moves the turf in direction of int team
    public static void moveTurf(int team, int numLeft) {
        if (numLeft == 0) {
            return;
        }
        if(++turfLine >= getCols() - 2) {
            team1Win = true;
        }
        else if(--turfLine <= 1) {
            team2Win = true;
        }

        else if(team == 1) {
            for(int r = 0 ; r < background.length; r++) {
                background[r][turfLine] = team1Sym;
                if (turfLine < getCols() - 1) {
                    background[r][turfLine + 1] = turfSym;
                    if(players[2].occupying(r,turfLine + 1)){
                        players[2].right();
                    }
                    if(players[3].occupying(r,turfLine + 1)){
                        players[3].right();
                    }
                }
            }
            turfLine += 1;

        }
        else {
            for(int r = 0 ; r < background.length; r++) {
                background[r][turfLine] = team2Sym;
                if (turfLine > 0) {
                    background[r][turfLine - 1] = turfSym;
                    if(players[1].occupying(r,turfLine - 1)){
                        players[1].left();
                    }
                    if(players[0].occupying(r,turfLine - 1)){
                        players[0].left();
                    }
                }
            }
            turfLine -= 1;
        }
        moveTurf(team, numLeft - 1);
    }

    //cannot override toString() in Object because it is not originally static
    //returns the entire map as a String
    public static String getString(char[][] map) {

        String borderCorner = "+";
        if(isRobot) {
            borderCorner = "Ω";
        }
        if(isSuddenDeath) {
            borderCorner = "☯";
        }

        StringBuilder topBorder = new StringBuilder("                          " + borderCorner + " ");
        int seconds = GameController.getSeconds(GameController.getOriginalTime(), System.currentTimeMillis());
        if (isSuddenDeath) {
            if ( seconds % 2 == 0) {
                for (int i = 0; i < map[0].length; i++) {
                    topBorder.append("X ");
                }
            } else {
                for (int i = 0; i < map[0].length; i++) {
                    topBorder.append(" x");
                }
            }
        } 
        else if(isRobot) {
            if(seconds % 2 == 0) {
                for (int i = 0; i < map[0].length; i++) {
                    topBorder.append("# ");
                }
            }
            else {
                for (int i = 0; i < map[0].length; i++) {
                    topBorder.append(" #");
                }
            }
        } else {
            for (int i = 0; i < map[0].length; i++) {
                topBorder.append("- ");
            }
        }
        topBorder.append(borderCorner);

        StringBuilder out = new StringBuilder();
        char horizBorder = '|';   
        if(isRobot) {
            horizBorder = '∫';
        }
        if (isSuddenDeath) {
            horizBorder = '‖';
        }

        int player, line;
        for (int i = 0; i < map.length; i++) {
            player = 1;
            line = i;
            if (line >= 14) {
                player = 2;
                line = i - 14;
            }
            out.append(getStatsLine(line, player) + "  ");
            out.append(horizBorder + " ");
            for(int j = 0; j < map[0].length; j++) {
                out.append(map[i][j]);
                out.append(" ");
            }
            player = 3;
            line = i;
            if (line >= 14) {
                player = 4;
                line = i - 14;
            }
            out.append(horizBorder);
            out.append("  " + getStatsLine(line, player) + ls);
        }

        if(!robotFlicker) {
            int t =  out.indexOf(" o ");
            if (isRobot && t != -1) {
                out.replace(t, t + 3, "*o*");
            }
            t = out.indexOf(" ! ");
            if(t != -1) {
                if(robot.getHealth() <= 9) {
                    out.replace(t, t + 3, "" + Map.getRobot().getRightSym() + robot.getHealth() + Map.getRobot().getLeftSym());
                } else {
                    out.replace(t, t + 3, Map.getRobot().getRightSym() + "!" + Map.getRobot().getLeftSym());
                }
            }

            t =  out.indexOf(" ¥ ");
            if (isRobot  && t != -1) {
                out.replace(t, t + 3, ".¥.");
            }

        }
        return (topBorder.append(ls + out.append(topBorder)) ).toString();
        //    *o*
        //    ~!~
        //    .¥.
    }

    public static String getStatsLine(int line, int player) {
        int index = player - 1;
        switch (line) {
            case 0:  return "+ - - - - - - - - - - -+";
            case 1:  return "|     Player " + player + " [" + symbols[index] + "]     |";
            case 2:  return "|                      |";
            case 3:  return "|      Deaths: " + format3(players[index].getDeathCount()) + "     |";
            case 4:  return "|      Level:  " + format3(players[index].getLevel()) + "     |";
            case 5:  return "|                      |";
            case 6:  return "|  Abilities Unlocked: |";
            case 7:  return "|" + players[index].getAbilities()[0] + "|";
            case 8:  return "|" + players[index].getAbilities()[1] + "|";
            case 9:  return "|" + players[index].getAbilities()[2] + "|";
            case 10: return "|" + players[index].getAbilities()[3] + "|";
            case 11: return "|" + players[index].getAbilities()[4] + "|";
            case 12: return "+- - - - - - - - - - - +";
        }
        return "                        ";
    }

    public static String format3(int x) { //formats a number to take up 3 total char spaces
        if (x <= 9) {
            return x + "  ";
        } else if (x <= 99){
            return x + " ";
        } else if (x <= 999) {
            return x + "";
        }
        return "999";
    }

    public static int getDistance(GameObject o1, GameObject o2) { // uses distance formula to get distance, rounded to nearest int
        return (int) ( Math.sqrt( (o1.getX() - o2.getX())*(o1.getX() - o2.getX()) + (o1.getY() - o2.getY())*(o1.getY() - o2.getY()) ) + 0.5 );
    }

    public static void stopAllMusic() {
        GameController.stopAllMusic();
    }

    public static void toggleFriendlyFire() {
        friendlyFire = !friendlyFire;
    }

    public static boolean getFriendlyFire() {
        return friendlyFire;
    }

    //accessors and mutators
    public static char getSymbol(int x) {
        return symbols[x];
    }

    public static void addPlayer(char playerSymbol, int num, int x, int y) {
        Map.players[num - 1] = new Player(playerSymbol, num, x, y);
    }

    public static void addBarrier(char barrierSymbol, int num, int x, int y, boolean breakable) {
        if(barriers.size() <= maxBSize) {
            barriers.add(new Barrier(barrierSymbol, num, x, y, breakable));
        }
    }

    public static void addBarrier(int x, int y) {
        if(barriers.size() <= maxBSize) {
            barriers.add(new Barrier(x, y));
        }
    }

    public static void addPortal(int x, int y) {
        portals.add(new Portal(x,y));
    }

    public static void addReflectiveBarrier(int x, int y, int tilt){
        reflectiveBarriers.add(new ReflectiveBarrier(x, y, tilt));
    }

    public static void addLevelUp(int x, int y){
        levelUps.add(new LevelUp(x,y));
    }

    public static List<Barrier> getBarriers(){
        return barriers;
    }

    public static List<ReflectiveBarrier> getReflectiveBarriers(){
        return reflectiveBarriers;
    }

    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public static Player getPlayer(int x) {
        return players[x];
    }

    public static void addBullet(char direction, int id, int x, int y) {
        bullets.add(new Bullet(direction, id, x, y));
    }

    public static void addBullet(char direction, int id, int x, int y, int dir) {
        bullets.add(new Bullet(direction, id, x, y, dir));
    }

    public static int getTurfLine() {
        return turfLine;
    }

    public static char[][] getMap() {
        return fullMap;
    }

    public static char getTeamSym(int team)
    {
        if(team == 1){
            return team1Sym;
        }
        return team2Sym;
    }

    public static char getBreakBarSym() {
        return breakBarSym;
    }

    public static char getUnBreakBarSym() {
        return unBreakBarSym;
    }

    public static int getTurfChange() {
        return turfChange;
    }

    public static boolean getIsSuddenDeath() {
        return isSuddenDeath;
    }

    public static void setIsSuddenDeathMusicPlaying(boolean b) {
        isSuddenDeathMusicPlaying = b;
    }

    public static AIRobot getRobot() {
        return robot;
    }

    public static void setIsRobotMusicPlaying(boolean b) {
        isRobotMusicPlaying = b;
    }

    public static boolean getIsRobotMusicPlaying() {
        return isRobotMusicPlaying;
    }

    public static boolean isRobot() {
        return isRobot;
    }

    public static void randomPlace(Player p)
    {
        if(p.getID() == 1 || p.getID() == 2)
        {
            p.setX(getRandX(1));
            p.setY(getRandY());
        }
        else 
        {
            p.setX(getRandX(2));
            p.setY(getRandY());
        }
    }

    public static void randomPlace(Bullet b)
    {
        if(b.getID() == 1 || b.getID() == 2)
        {
            b.setX(getRandX(1));
            b.setY(getRandY());
        }
        else 
        {
            b.setX(getRandX(2));
            b.setY(getRandY());
        }
    }

}
