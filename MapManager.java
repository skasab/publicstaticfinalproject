import java.io.*;
import java.util.*;
public class MapManager
{
    private static int rows = 30, cols = 61;
    private static Scanner sc = new Scanner(System.in);
    private static StringAlignUtils centerMe = new StringAlignUtils();
    public static char[][] convertMap(String fName) {
        char[][] map = new char[rows][cols];
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("Maps/" + fName)));
            String line = br.readLine();
            for(int r = 0; r < map.length && line != null; r++) {
                for(int c = 0; c < line.length() && c < map[0].length; c++) {
                    map[r][c] = line.charAt(c);
                }
                line = br.readLine();
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(centerMe.format("Error loading from Maps/" + fName + ".map"));
            System.out.println(centerMe.format("Press ENTER to continue..."));
            sc.nextLine();
        } catch (IOException e) {
            System.out.println(centerMe.format("Error with map loading."));
            System.out.println(centerMe.format("Press ENTER to continue..."));
            sc.nextLine();
        }
        return map;
    }

    public static void saveMap(char[][] map) {
        System.out.print("\n");
        System.out.print("Enter a filename for this map: ");
        String fName = sc.nextLine();
        try {
            PrintWriter pw = new PrintWriter("Maps/" + fName + ".map");
            for(int i = 0; i < map.length; i++) {
                for(int j = 0; j < map[0].length; j++) {
                    pw.write(map[i][j]);
                }
                pw.println();
            }
            pw.close();
            System.out.println("Map successfully saved at .../Maps/" + fName + ".map");
            System.out.println("Goodbye!");
            System.exit(0);
        } catch(IOException e) {
            System.out.println("Error with map saving.");
            System.out.println("Press ENTER to continue...");
            sc.nextLine();
        }
        sc.close();
    }

    public static void printMap(String fName) {
        char[][] map = convertMap(fName);
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[0].length; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

}
