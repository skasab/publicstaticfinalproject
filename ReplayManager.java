import java.io.*;
import java.util.Scanner;
public class ReplayManager
{
    private static int frameDelay = 40;
    private static final String ls = System.getProperty("line.separator");

    public static void playReplay(String fileName){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("Replays/" + fileName)));
            br.readLine(); //skip first line, this is the track number
            String line = br.readLine();
            StringBuilder sb = new StringBuilder(line);
            while(line != null) {
                if(line.equals("separator")) {
                    System.out.println("\f" + sb);
                    Thread.sleep(frameDelay);
                    sb = new StringBuilder();
                } else {
                    sb.append(line + ls);
                }
                line = br.readLine();
            }
            Thread.sleep(250);
            System.out.println("\fReplay has ended. Hit ENTER to continue to main menu...");
            new Scanner(System.in).nextLine();
        } catch (IOException | InterruptedException e) {
            System.out.println("Error loading replay from .../Replays/" + fileName);
            System.out.println("Hit ENTER to continue to main menu...");
            new Scanner(System.in).nextLine();
        }
    }

    public static void faster() {
        frameDelay /= 2;
        if (frameDelay <= 5) {
            frameDelay = 5;
        }
    }
    
    public static void slower() {
        frameDelay *= 2;
        if (frameDelay > 320) {
            frameDelay = 320;
        }
    }
    
    public static int getTrackNum(String fileName){
        return 1;
    }
}
