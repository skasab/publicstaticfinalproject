import java.util.*;
import java.awt.AWTException;
public class MapEditor
{
    public static int rows = 30, cols = 61;
    public static int selectorPos = 1;
    public static int xPos = 0;
    public static int yPos = 0;
    public static char[][] map = new char[rows][cols];
    public static char[] symbols = {'?','1','2','3','4','‖','|','\\','/','-'};
    public static char charUnderCursor;
    public static boolean saved = false;
    public static long start = System.currentTimeMillis();
    public static boolean printSelection = true;

    public static void main() {
        // GameController.stopAllMusic();
        Sound song = new Sound("Music/trackEditor.wav");
        song.loop();

        for(char[] line : map) {
            Arrays.fill(line,' ');
        }
        for(int i = 0; i < map.length; i++) {
            map[i][30] = '|';
        }

        //KeyBinder.runLevelEditor();

        TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    printBoard();
                    printSelection = !printSelection;
                }};
        Timer timer = new Timer("selectionTimer");
        timer.scheduleAtFixedRate(timerTask, 0, 350);

        LevelKey.main();
        try {
            LevelKey.getFocus();
        }
        catch(AWTException e) { }
        while(true) {  
        }

    }

    public static String getObjList() {
        return ("\n\n")
        + ("\t\t\t\t\t\t   Instructions: \n\n")
        + ("\n\t\t\t\t  Use Enter to add objects and Backspace to remove them. \n\t\t\t\t  Use the arrow keys to move around your cursor. \n\t\t\t\t  Create your desired map and then press S to save!\n")
        + ("\n\t\t\t\t The numbers represent spawn points. Make sure to place only one of each!\n")
        + ("\t\t\t\t If you do not specify spawn points, they will be set to their defaults, in the corners.\n")
        + ("\n\t\t Selected Object:     Object:     P1 P2 P3 P4 ‖  |  \\  /  -  ?            Current Position:\n")
        + ("\t\t         " + symbols[selectorPos] + "            Press Key:  1  2  3  4  5  6  7  8  9  0           Row = " + yPos + ", Column = " + xPos + "\n");
    }

    public static void reflect() {
        for (int r = 0; r < rows; r++) {
            for(int c = 0; c <= cols/2; c++) {
                char c1 = map[r][c];
                char c2 = map[r][cols - c];
                if(c1 == '1') {
                    c2 = '3';
                } else if(c1 == '2') {
                    c2 = '4';
                } else if(c1 == '3') {
                    c2 = '1';
                } else if(c1 == '4') {
                    c2 = '2';
                } else if(c1 == '\\') {
                    c2 = '/';
                } else if(c1 == '/') {
                    c2 = '\\';
                } else {
                    c2 = c1;
                }
            }
        }
    }

    public static void printBoard() {
        if (saved) {
            return;
        }

        String total = "\f" + getObjList();

        String topBorder = "+ ";
        for (int i = 0; i < map[0].length; i++) {
            topBorder += "- ";
        }
        topBorder += "+";

        total += (topBorder + "\n");
        for(int r = 0; r < map.length; r++) 
        {
            total += "| ";
            for(int c = 0; c < map[0].length; c++)
            {

                if(r == yPos && c == xPos && printSelection) {
                    total += symbols[selectorPos] + " ";
                } else if (r == yPos && c == xPos - 1 && printSelection) {
                    total += "> ";
                } else if (r == yPos && c == xPos + 1 && printSelection) {
                    total += "< ";
                } else {
                    total += map[r][c] + " ";
                }
            }
            total += "|\n";
        }
        total += topBorder;
        System.out.println(total);
    }

    public static void select(int which) {
        selectorPos = which;
        printBoard();
    }

    public static void up() {
        yPos--;
        if(yPos < 0) {
            yPos = map.length - 1;
        }
        printSelection = true;
        printBoard();
    }

    public static void down() {
        yPos++;
        if(yPos > map.length - 1) {
            yPos = 0;
        }
        printSelection = true;
        printBoard();
    }

    public static void left() {
        xPos--;
        if(xPos < 0) {
            xPos = map[0].length - 1;;
        }
        printSelection = true;
        printBoard();
    }

    public static void right() {
        xPos++;
        if(xPos > map[0].length - 1) {
            xPos = 0;
        }
        printSelection = true;
        printBoard();
    }

    public static void add() {
        map[yPos][xPos] = symbols[selectorPos];
        printBoard();
    }

    public static void remove() {
        map[yPos][xPos] = ' ';
        printBoard();
    }

    public static void save() {
        saved = true;
        MapManager.saveMap(map);

    }
}
