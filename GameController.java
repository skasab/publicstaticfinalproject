import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.awt.AWTException;
public class GameController
{
    // Game 
    private static char[] playerSymbols;
    private static long originalTime; 
    private static int trackNum;
    private static boolean playingGame;
    private static int teamWhoWon;
    private static long endTime;
    private static int frameCount;
    private static int pupDelay;
    private static int robDelay;
    public static int robotFrameCount;
    public static int robotFrameDelay; // lower number = FASTER, 1 is fastest
    private static boolean inReplay;
    private static char[][] gameMap;

    // Audio
    private static List<Sound> music;
    private static List<Sound> soundFX;
    private static String filePathChar;

    // Options
    private static boolean soundOpt = true;
    private static boolean musicOpt = true;
    private static boolean barrierOpt = true;
    private static boolean portalOpt = false;

    // Menu
    private static List<String> menu;
    private static int menuSelect;
    private static String menuIdentifier;
    private static boolean onMenu;

    private static PrintWriter pw;

    private static String ls = System.getProperty("line.separator");

    private static StringAlignUtils centerMe = new StringAlignUtils();
    public static void main(String[] args) throws InterruptedException{
        Map.reset();
        reset();
        getOSChar();
        initialize();
        loadSounds();
        resize();
        KeyBinder.main();
        while (true) {
            restartMusic();
            menu();
            while(onMenu) {
                Thread.sleep(10);
            } 
            if(musicOpt) {
                stopAllMusic();
            }
            loadSounds();
            Map.reset();
            reset();
            doGame();
        }
    }

    public static void suddenDeath() {
        robotFrameDelay = 1;
        if(soundOpt) {
            for (Sound s : soundFX) {
                s.changeVolume(-4f);
            }
        }
    }

    public static void robot() {
        if(musicOpt) {
        music.get(8).loop();
    }
    }

    public static void stopAllMusic() {
        for (Sound s : music) {
            s.stop();
        }
    }

    public static void restartMusic() {
        for(Sound s: music) {
            s.setToBeginning();
        }

    }

    public static void startSuddenDeathMusic() {
        if(musicOpt) {
            stopAllMusic();
            music.get(7).changeVolume(6f);
            music.get(7).loop(3_800_000l);
        }
        if(soundOpt) {
            soundFX.get(7).play();
        }
        Map.setIsSuddenDeathMusicPlaying(true);
    }

    public static void startRobotSound()  {
        if(soundOpt) {
            stopAllMusic();
            soundFX.get(8).play();
        }
    }

    public static void startRobotMusic()  {
        if(musicOpt  && !Map.getIsRobotMusicPlaying()) {
            stopAllMusic();
            //music.get(8).changeVolume(4f);
            music.get(8).loop();
        }
        Map.setIsRobotMusicPlaying(true);
    }

    public static void stopRobotMusic()  {
        if(musicOpt  && Map.getIsRobotMusicPlaying()) {
            stopAllMusic();
            music.get(6).loop();
        }
        Map.setIsRobotMusicPlaying(false);
    }

    public static void initialize() {

        music = new ArrayList<Sound>();
        soundFX = new ArrayList<Sound>();
        if(musicOpt) {
            music.add(new Sound("Music" + filePathChar + "trackMainMenu.wav")); //0
            music.add(new Sound("Music" + filePathChar + "trackLoad.wav")); //1
            music.add(new Sound("Music" + filePathChar + "trackGameScreen.wav")); //2
            music.add(new Sound("Music" + filePathChar + "trackWin.wav")); //3
            music.add(new Sound("Music" + filePathChar + "trackQuit.wav")); //4
            music.add(new Sound("Music" + filePathChar + "track1.wav")); //5
        }
        for (Sound s : music) {
            s.changeVolume(-2f);
        }
        try {
            pw = new PrintWriter("Replays/current.replay");
        } catch (IOException e) {
        }
        //music.get(0).changeVolume(4.0f);
    }

    public static void getOSChar() {
        //OS.getOSName();
        if(OSCheck.isWindows()) {
            filePathChar = "\\";
        }
        else {
            filePathChar = "/";
        }

    }

    public static void reset() throws InterruptedException {
        playerSymbols = new char[4];
        frameCount = 0;
        pupDelay = 90;
        playingGame = false;
        inReplay = false;

        trackNum = 0;
        menu = new ArrayList<String>();
        menuIdentifier = "main";
        menu.clear();
        menu.add("  Play Game"); 
        menu.add("  How To Play");
        menu.add("  Options");
        menu.add("  Load & Play Replay");
        menu.add("  Statistics");
        menu.add("  Exit");
        menuSelect = 0;

        robotFrameCount = 0;
        robotFrameDelay = 5; // 1 is fastest, to slow down, incease number.

    }

    public static void doGame() throws InterruptedException  {
        Statistics.upGamesPlayed();
        playingGame = true;
        intro();        
        symbolInput();
        //createPlayers();
        if(barrierOpt) {
            createBarriers(gameMap);
        }
        if(portalOpt){
            createPortals(gameMap);
        }
        //Map.setMap(gameMap);
        originalTime = System.currentTimeMillis();
        Map.buildBackgroundArray();
        Map.transfer();

        // KeyListenerInput.requestFocus();
        // focuses on the KeyListener window automatically to avoid accidental
        // input into the console, which would otherwise mess up taking input from
        // Scanner later on, due to the fact that you cannot flush standard input.

        //playingGame = true;
        while(playingGame) {
            updateGame();
        }
    }

    public static void menu() throws InterruptedException {     
        try {
            KeyBinder.getFocus();
        }
        catch(AWTException e) { }
        playingGame = false;
        inReplay = false;
        if(!playingGame) {
            System.out.print("\f");
            //stopAllMusic();
            if(musicOpt) {
                //
                if(menuIdentifier.equals("main") || menuIdentifier.equals("options") || menuIdentifier.equals("howtoplay") || menuIdentifier.equals("stats")) {
                    music.get(0).loop();
                }
                else if(menuIdentifier.equals("end")) {
                    music.get(3).loop();
                }

                else if(menuIdentifier.equals("yesno")) {
                    music.get(4).loop();
                }
            }
            else { 
                stopAllMusic();
            }
            //System.out.println();
            if(menuIdentifier.equals("main")) {
                //System.out.print("↖ Click the Key Listener in the corner!");
                printTitle();
                System.out.print(centerMe.format("Public Static Final Project™ 2016"));
                System.out.println(centerMe.format("Select any option and hit ENTER!"));
            }
            else if(menuIdentifier.equals("options")) {
                printOptions();
                System.out.print(centerMe.format("Public Static Final Project™ 2016"));
                System.out.println(centerMe.format("To toggle an option, select it and hit ENTER!"));
            }
            else if(menuIdentifier.equals("howtoplay")) {
                printHowToPlay();
                System.out.println(centerMe.format("Public Static Final Project™ 2016"));
                System.out.println(centerMe.format("Welcome to Turf Wars! Your goal in this game is to move the middle line"));
                System.out.print(centerMe.format("to the other side. Once you do that, you win!"));
            }
            else if(menuIdentifier.equals("stats")) {
                printStats();
                System.out.print(centerMe.format("Public Static Final Project™ 2016"));
                System.out.print("\n\n");
                try {
                    for(int i = 0; i < Statistics.getStatsSize() - 2 ; i++) {
                        String a = "";

                        System.out.print(centerMe.format(a + Statistics.getStatName(i) + Statistics.getStat(i)));
                    }
                }
                catch(IndexOutOfBoundsException e) { }
            }
            else if(menuIdentifier.equals("end")) {
                System.out.print("\n\n");
                printTitle();
                if(teamWhoWon == 1) {
                    System.out.println(centerMe.format("Team 1 (Left) has won!"));
                }
                else {
                    System.out.println(centerMe.format("Team 2 (Right) has won!"));
                }
                System.out.println();
                System.out.print(getStatusEnd());
                System.out.println("\n");                
            }

            else if(menuIdentifier.equals("yesno")) {
                printQuit();
                System.out.print(centerMe.format("Public Static Final Project™ 2016"));
                System.out.print(centerMe.format("Are you sure you want to quit?"));
            }

            if(!menuIdentifier.equals("end")) {
                System.out.println("\n");
            }

            onMenu = true;  
            for(int i = 0; i < menu.size(); i++ ) {
                if(menuIdentifier.equals("end")) {
                    System.out.print("\t\t\t\t\t\t\t\t\t ");
                }
                else {
                    System.out.print("\t\t\t\t\t\t\t\t\t\t");
                }
                if(i == menuSelect) {
                    System.out.print("▶ ");
                } else {
                    if (menuIdentifier.equals("options")) {
                        System.out.print(StringAlignUtils.trim(centerMe.format("")));
                    }
                }
                if(menuIdentifier.equals("options")) {
                    System.out.print("\r");
                    switch(i) {
                        case 0:
                        if(Map.getFriendlyFire()) {
                            System.out.print("☑ " + menu.get(i));
                        }
                        else {
                            System.out.print("☐ " + menu.get(i));
                        }
                        break;

                        case 1:
                        if(soundOpt) {
                            System.out.print("☑ " + menu.get(i));
                        }
                        else {
                            System.out.print("☐ " + menu.get(i));
                        }
                        break;

                        case 2:
                        if(musicOpt) {
                            System.out.print("☑ " + menu.get(i));
                        }
                        else {
                            System.out.print("☐ " + menu.get(i));
                        }
                        break;

                        case 3:
                        if(barrierOpt) {
                            System.out.print("☑ " + menu.get(i));
                        }
                        else {
                            System.out.print("☐ " + menu.get(i));
                        }
                        break;

                        case 4:
                        if(portalOpt) {
                            System.out.print("☑ " + menu.get(i));
                        }
                        else {
                            System.out.print("☐ " + menu.get(i));
                        }
                        break;

                        case 5:
                        System.out.print("    Back");
                        break;
                    }

                } else {
                    System.out.print("" + menu.get(i));
                }

                System.out.println();
            }

            if(menuIdentifier.equals("main")) {
                System.out.print("\n\n" + centerMe.format("Random Tip: " + getRandomTip() + "\n"));
            }
        }

    }

    public static void menuController(int keycode) throws InterruptedException  {
        if(!playingGame) {
            //System.out.print("\f");
            if(keycode == 38) {
                menuSelect--;
                if(menuSelect < 0) {
                    menuSelect = menu.size() - 1;
                }
            }
            if(keycode == 40) {
                menuSelect++;
                if(menuSelect >= menu.size()) {
                    menuSelect = 0;
                }
            }
            menu();
        }
    }

    public static void menuEnter() throws InterruptedException, IOException{
        Scanner sc = new Scanner(System.in);
        if(!playingGame) { 
            if(menuIdentifier.equals("main")) {
                if(menuSelect == 0) {
                    onMenu = false;
                }
                else if(menuSelect == 1) {
                    howToPlay();
                }
                else if(menuSelect == 2) {
                    options();
                }
                else if(menuSelect == 3) {
                    //load replay
                    System.out.print(StringAlignUtils.trim(centerMe.format("Click here and enter the replay's file name: ")) + " ");
                    String loadedFileName = sc.nextLine();

                    System.out.println(centerMe.format("Replay load attempted! Press ENTER to play."));
                    sc.nextLine();
                    System.out.print("\f");
                    if(musicOpt) {
                        music.set(5,new Sound("Music" + filePathChar + "track" + ReplayManager.getTrackNum(loadedFileName) + ".wav")); 
                        stopAllMusic();
                        music.get(5).loop(0);
                    }
                    inReplay = true;
                    ReplayManager.playReplay(loadedFileName + ".replay");

                    loadingScreen();
                    menu();

                }
                else if(menuSelect == 4) {
                    Statistics.readFile();
                    statistics();
                }
                else if(menuSelect == 5) {
                    stopAllMusic();
                    yesNoExit();
                }
            }
            else if(menuIdentifier.equals("options")) {
                if (menuSelect == 0) {
                    Map.toggleFriendlyFire();
                } else if (menuSelect == 1) {
                    soundOpt = !soundOpt;
                }
                else if (menuSelect == 2) {
                    musicOpt = !musicOpt;
                    if(!musicOpt) {
                        stopAllMusic();
                    }
                }
                else if (menuSelect == 3) {
                    barrierOpt = !barrierOpt;
                }
                else if (menuSelect == 4) {
                    portalOpt = !portalOpt;
                }
                else if (menuSelect == 5) {
                    returnToMainMenu();
                }
                menu();
            }
            else if(menuIdentifier.equals("howtoplay")) {
                if (menuSelect == 0) {
                    printControls();
                } else if (menuSelect == 1) {
                    System.out.println(centerMe.format("Your goal is to get the middle line, the \"turf line\", to the other side."));
                    Thread.sleep(1000);
                    System.out.println(centerMe.format("By shooting your opponents, the turf line will move towards them, giving you more territory."));
                    Thread.sleep(1000);
                    System.out.println(centerMe.format("The more you shoot, the more turf you win!"));
                }
                else if (menuSelect == 2) {
                    System.out.println(centerMe.format("This is a bullet: >"));  
                    System.out.println(centerMe.format("They are shot from players, and move horizontally."));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("A bullet will kill any enemy player it comes into contact with."));
                    System.out.println(centerMe.format("They will disappear if they collide with other bullets, or reach the edge of the map."));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("Bullets have a firing delay of approximately half a second, so don't attempt to spam them!"));
                }
                else if (menuSelect == 3) {
                    System.out.println(centerMe.format("This is an unbreakable barrier: " + Map.getUnBreakBarSym()));
                    System.out.println(centerMe.format("They cannot be destroyed, and there are only six on the map."));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("This is a reflective barrier: \\"));
                    System.out.println(centerMe.format("They reflect bullets in the direction of their orientation."));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("Barriers can be turned off in Options from the Main Menu."));
                }
                else if (menuSelect == 4) {
                    System.out.println(centerMe.format("This is a Level Up: " + Symbols.upArrow));
                    System.out.println(centerMe.format("Collecting these will enhance your abilities and maybe even unlock new ones."));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("Be careful, though - if they get shot, they're kaput!"));
                    Thread.sleep(300);
                    System.out.println(centerMe.format("And if you get shot? Say goodbye to your levels!"));
                }
                else if (menuSelect == 5) {
                    System.out.println(centerMe.format("Public Static Final Project™ is a group of four students. They are: "));
                    Thread.sleep(700);
                    System.out.println(centerMe.format("Solly Kasab, Group Leader."));
                    Thread.sleep(700);
                    System.out.println(centerMe.format("Michael Jan, Head Programmer."));
                    Thread.sleep(700);
                    System.out.println(centerMe.format("David Liu, Librarian."));
                    Thread.sleep(700);
                    System.out.println(centerMe.format("Gary Yao, Head Tester."));
                }
                else if (menuSelect == 6) {
                    returnToMainMenu();
                }
            }
            else if (menuIdentifier.equals("stats")) {
                if(menuSelect == 0) {
                    returnToMainMenu();
                }
            }

            else if(menuIdentifier.equals("end")) {
                if (menuSelect == 0) {
                    System.out.print("\f");
                    loadingScreen();
                    returnToMainMenu(1);
                    if(musicOpt) {
                        stopAllMusic();
                    }
                    music.get(0).loop();
                } else if (menuSelect == 1) {
                    //save replay
                    System.out.print(StringAlignUtils.trim(centerMe.format("Enter a filename for the replay: "))+ " ");
                    String fName = sc.next();
                    new File("Replays/current.replay").renameTo( new File("Replays/" + fName + ".replay"));
                    System.out.println(centerMe.format("Replay successfully saved in file: " + ".../Replays/" + fName + ".replay"));
                    Statistics.upReplaysSaved();
                    System.out.println(centerMe.format("Press ENTER to continue..."));
                    sc.nextLine();
                    sc.nextLine();
                    System.out.print("\f");
                    loadingScreen();
                    returnToMainMenu(1);
                    //KeyListenerInput.requestFocus();
                } else if (menuSelect == 2) {
                    stopAllMusic();
                    yesNoExit();
                } 
            }
            else if(menuIdentifier.equals("yesno")) {
                stopAllMusic();
                if (menuSelect == 0) {
                    Statistics.saveStats();
                    System.exit(0);
                } else if (menuSelect == 1) {
                    //stopAllMusic();
                    returnToMainMenu(1);
                } 
            }
        }
    }

    public static void yesNoExit() throws InterruptedException  {
        music.get(4).setToBeginning();
        menuIdentifier = "yesno";
        menu.clear();
        menu.add("  Yes"); 
        menu.add("  No");
        menu();
    }

    public static void printQuit() {
        //         System.out.print(centerMe.format(" _______          __________________     _____   "));
        //         System.out.print(centerMe.format("(  ___  ) |\\     /|\\__   __/\\__   __// ___ \\ "));
        //         System.out.print(centerMe.format("| (   ) | | )    ( |   ) (      ) (    ( (   ) ) "));
        //         System.out.print(centerMe.format("| |   | | | |    | |   | |      | |    \\/  / /  "));
        //         System.out.print(centerMe.format("| |   | | | |    | |   | |      | |      (  )    "));
        //         System.out.print(centerMe.format("| | /\\|| | |    | |   | |      | |      | |     "));
        //         System.out.print(centerMe.format("| (_\\ \\ | |(___) |___) (___   | |      (_)     "));
        //         System.out.print(centerMe.format("(____\\/_)(_______)\\_______/   )_(       _      "));
        //         System.out.print(centerMe.format("                                         (_)     "));

        System.out.print(centerMe.format("   ___               _   _     _____     "));
        System.out.print(centerMe.format(" .'   `.            (_) / |_  / ___ `.   "));
        System.out.print(centerMe.format("/  .-.  \\  __   _   __ `| |-'|_/___) |   "));
        System.out.print(centerMe.format("| |   | | [  | | | [  | | |    /  __.'   "));
        System.out.print(centerMe.format("\\  `-'  \\_ | \\_/ |, | | | |,   |_|       "));
        System.out.print(centerMe.format(" `.___.\\__|'.__.'_/[___]\\__/   (_)       "));

    }

    public static void options() throws InterruptedException {
        menuIdentifier = "options";
        menu.clear();
        menu.add("  Friendly Fire"); 
        menu.add("  Sound FX");
        menu.add("  Music");
        menu.add("  Barriers");
        menu.add("  Portals");
        menu.add("  Back");

        menu();        
    }

    public static void howToPlay() throws InterruptedException {
        //printControls();

        menuIdentifier = "howtoplay";
        menu.clear();
        menu.add("  Controls"); 
        menu.add("  Goal");
        menu.add("  About Bullets");
        menu.add("  About Barriers");
        menu.add("  About Levelling up");
        menu.add("  About Creators");
        menu.add("  Back");
        //menuSelect = 0;
        menu();
    }

    public static void printOptions() {
        System.out.print(centerMe.format(" ____        _   _                      "));
        System.out.print(centerMe.format(" / __ \\      | | (_)                   "));
        System.out.print(centerMe.format("| |  | |_ __  | |_ _  ___  _ __  ___    "));
        System.out.print(centerMe.format("| |  | | '_ \\| __| |/ _ \\| '_ \\/ __| "));
        System.out.print(centerMe.format("| |__| | |_) ||_| | (_) | | | \\__ \\   "));
        System.out.print(centerMe.format(" \\____/| .__/ \\__|_|\\___/|_| |_|___/ "));
        System.out.print(centerMe.format("       | |                              "));
        System.out.print(centerMe.format("       |_|                              "));
    }

    public static void printStats() {
        System.out.print(centerMe.format(" ______  _______  ______  _______ _____  ______  _______ _____  ______  ______  "));
        System.out.print(centerMe.format("/ |        | |   | |  | |   | |    | |  / |        | |    | |  | |     / |      "));
        System.out.print(centerMe.format("'------.   | |   | |__| |   | |    | |  '------.   | |    | |  | |     '------. "));
        System.out.print(centerMe.format(" ____|_/   |_|   |_|  |_|   |_|   _|_|_  ____|_/   |_|   _|_|_ |_|____  ____|_/ "));

    }

    public static void printHowToPlay() {
        System.out.print(centerMe.format(" _   _                         _____           ___     _                  "));
        System.out.print(centerMe.format("( ) ( )                       (_   _)         (  _`\\ (_ )                "));
        System.out.print(centerMe.format("| |_| |   _      _   _   _      | |   _       | |_) ) | |    _ _  _   _   "));
        System.out.print(centerMe.format("|  _  | /'_`\\  ( ) ( ) ( )     | | /'_`\\    | ,__/' | |  /'_` )( ) ( )  "));
        System.out.print(centerMe.format("| | | |( (_) )| \\_/ \\_/ |     | | ( (_) )   | |     | | ( (_| || (_) |  "));
        System.out.print(centerMe.format("(_) (_)`\\___/'`\\___x___/'     (_)`\\___/'   (_)    (___)`\\__,_)`\\__,| "));
        System.out.print(centerMe.format("                                                                ( )_| |   "));
        System.out.print(centerMe.format("                                                               `\\___/'   "));
    }

    public static void statistics() throws InterruptedException {
        menuIdentifier = "stats";
        menu.clear();
        menu.add("  Back");
        //menuSelect = 0;
        menu();
    }

    public static void returnToMainMenu() throws InterruptedException {
        menuIdentifier = "main";
        menu.clear();
        menu.add("Play Game"); 
        menu.add("How To Play");
        menu.add("Options");
        menu.add("Load & Play Replay");
        menu.add("Statistics");
        menu.add("Exit");
        menuSelect = 0;
        menu();
    }

    public static void returnToMainMenu(int a) throws InterruptedException {
        music.get(0).setToBeginning();
        returnToMainMenu();
    }

    public static void resize() {
        System.out.print("\f");
        Scanner sc = new Scanner(System.in);
        System.out.println(centerMe.format("Please resize your window and press ENTER."));
        for(int i = 0; i < Map.getRows() + 14; i++) {
            for(int j = 0; j < Map.getCols() + 30; j++) {
                System.out.print(". ");
            }
            System.out.println();
        }
        sc.nextLine();
        System.out.print("\f");
    }

    public static void intro() throws InterruptedException{
        Scanner sc = new Scanner(System.in);
        System.out.print("\f");
        loadingScreen();
        if(musicOpt) {
            //music.get(4).stop();
            music.get(2).loop(0);
        }

        System.out.print("\f");
        printTitle();
        System.out.println("\n");
        //printControls();
        System.out.println(centerMe.format("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-"));
        System.out.println(centerMe.format("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-="));
        System.out.println();
    }

    public static void loadingScreen() throws InterruptedException {

        if(musicOpt) {
            stopAllMusic();
            music.get(1).loop(0);
        }
        System.out.println("\n\n\n\n\n");
        //         System.out.println("                                                                        __                    _                       ");       
        //         System.out.println("                                                                       / /   ____  ____ _____/ (_)___  ____ _         ");
        //         System.out.println("                                                                      / /   / _ \\/ __ `/ __  / / __ \\/ __   `/        ");
        //         System.out.println("                                                                     / /___/ /_/ / /_/ / /_/ / / / / / /_/ / _ _ _    ");
        //         System.out.println("                                                                    /_____/\\____/\\__,_/\\__,_/_/_/_/\\__,     (_|_|_)   ");
        //         System.out.println("                                                                                                   /____/           ");

        System.out.println("                                                                      __                 _ _                      ");
        System.out.println("                                                                     / /  ___   __ _  __| (_)_ __   __ _          ");
        System.out.println("                                                                    / /  / _ \\ / _` |/ _` | | '_ \\ / _` |         ");
        System.out.println("                                                                   / /__| (_) | (_| | (_| | | | | | (_| |_ _ _    ");
        System.out.println("                                                                   \\____/\\___/ \\__,_|\\__,_|_|_| |_|\\__, (_|_|_)   ");
        System.out.println("                                                                                                   |___/          ");
        System.out.println("\n\n");  
        System.out.print("\t\t\t\t\t\t\t\t");
        for(int i = 1; i <= 55; i++) {
            Thread.sleep(15);
            System.out.print("➤");
            Thread.sleep(15);
            System.out.print(" ");
            if(i % 19 == 0)
                System.out.print("\n\t\t\t\t\t\t\t\t");
        }
        if(soundOpt) {
            soundFX.get(6).play();
        }
        for(int i = 1; i <= 2; i++) {
            Thread.sleep(15);
            System.out.print(" ");
            System.out.print("➤");
            Thread.sleep(15);
            System.out.print(" ");            
        }
        if(musicOpt) {
            stopAllMusic();
        }
        //music.remove(1);
    }

    public static void printTitle() {
        System.out.print("\n\n");
        System.out.print(centerMe.format("     _____             __   __    __                  "));
        System.out.print(centerMe.format("  /__   \\_   _ _ __ / _| / / /\\ \\ \\__ _ _ __ ___  "));
        System.out.print(centerMe.format("    / /\\/ | | | '__| |_  \\ \\/  \\/ / _` | '__/ __| "));
        System.out.print(centerMe.format("   / /  | |_| | |  |  _|  \\  /\\  / (_| | |  \\__ \\ "));
        System.out.print(centerMe.format("   \\/    \\__,_|_|  |_|     \\/  \\/ \\__,_|_|  |___/"));                
    }

    public static String getTitle() {
        return ("\n\n")
        + (centerMe.format("     _____             __   __    __                  "))
        + (centerMe.format("  /__   \\_   _ _ __ / _| / / /\\ \\ \\__ _ _ __ ___  "))
        + (centerMe.format("    / /\\/ | | | '__| |_  \\ \\/  \\/ / _` | '__/ __| "))
        + (centerMe.format("   / /  | |_| | |  |  _|  \\  /\\  / (_| | |  \\__ \\ "))
        + (centerMe.format("   \\/    \\__,_|_|  |_|     \\/  \\/ \\__,_|_|  |___/"));  
    }

    public static void printControls() throws InterruptedException {
        System.out.println("\n");
        System.out.print(centerMe.format("      Player 1        Player 2        Player 3        Player 4     "));
        System.out.print(centerMe.format("                                                                   "));
        System.out.print(centerMe.format("       +---+           +---+           +---+           +---+       "));
        System.out.print(centerMe.format("       | 3 |           | - |           | 7 |           | 8 |       "));
        System.out.print(centerMe.format("       +---+           +---+           +---+           +---+       "));
        System.out.print(centerMe.format("       Fire!           Fire!           Fire!           Fire!       "));
        System.out.print(centerMe.format("                                                                   "));
        System.out.print(centerMe.format("       +---+           +---+           +---+           +---+       "));
        System.out.print(centerMe.format("       | W |           | P |           | Y |           | 5 |       "));
        System.out.print(centerMe.format("   +---+---+---+   +---+---+---+   +---+---+---+   +---+---+---+   "));
        System.out.print(centerMe.format("   | A | S | D |   | L | ; | ' |   | G | H | J |   | 1 | 2 | 3 |   "));
        System.out.print(centerMe.format("   +---+---+---+   +---+---+---+   +---+---+---+   +---+---+---+   "));
        System.out.print(centerMe.format("                                                     (numpad)      "));
        System.out.print(centerMe.format(" Do not hold buttons down, instead press them repeatedly for the same effect."));

        //listenerThread.wait();
        //menu();
    }

    public static void symbolInput() throws InterruptedException {

        Scanner sc = new Scanner(System.in);
        System.out.println(centerMe.format("Please click back into this window! "));
        System.out.print(StringAlignUtils.trim(centerMe.format("Enter symbol for player 1: ")) + " ");
        playerSymbols[0] = sc.next().charAt(0);
        System.out.print(StringAlignUtils.trim(centerMe.format("Enter symbol for player 2: "))+ " ");
        playerSymbols[1] = sc.next().charAt(0);
        while(playerSymbols[0]==playerSymbols[1]){
            System.out.print(StringAlignUtils.trim(centerMe.format("Symbol used, enter another symbol: "))+ " ");
            playerSymbols[1] = sc.next().charAt(0);
        }
        System.out.print(StringAlignUtils.trim(centerMe.format("Enter symbol for player 3: "))+ " ");
        playerSymbols[2] = sc.next().charAt(0);
        while(playerSymbols[2]==playerSymbols[1]||playerSymbols[0]==playerSymbols[2]) {
            System.out.print(StringAlignUtils.trim(centerMe.format("Symbol used, enter another symbol: "))+ " ");
            playerSymbols[2] = sc.next().charAt(0);
        }
        System.out.print(StringAlignUtils.trim(centerMe.format("Enter symbol for player 4: "))+ " ");
        playerSymbols[3] = sc.next().charAt(0);  
        while(playerSymbols[3]==playerSymbols[1]||playerSymbols[0]==playerSymbols[3] ||playerSymbols[2]==playerSymbols[3]) {
            System.out.print(StringAlignUtils.trim(centerMe.format("Symbol used, enter another symbol: "))+ " ");
            playerSymbols[3] = sc.next().charAt(0);
        }
        Map.setSymbols(playerSymbols);

        if(musicOpt) {

            while(trackNum <= 0 || trackNum >= 36) {
                System.out.print(StringAlignUtils.trim(centerMe.format("Enter track number, 1-35: "))+ " ");
                sc.nextLine();
                try{
                    trackNum = sc.nextInt();
                }
                catch(InputMismatchException e) {
                    System.out.println(StringAlignUtils.trim(centerMe.format("That's not a number! "))+ " ");
                }
                if(trackNum <= 0) {
                    System.out.println(StringAlignUtils.trim(centerMe.format("No negatives! "))+ " ");
                }
                if(trackNum >= 36) {
                    System.out.println(StringAlignUtils.trim(centerMe.format("Too high, too high! "))+ " ");
                }
            }

            if(Math.random() <= .03) {
                trackNum = 35;
            }

            //Statistics.addTrack(trackNum);
            stopAllMusic();
            music.add(new Sound("Music" + filePathChar + "track" + trackNum + ".wav")); //6
            music.get(6).changeVolume(0);
            music.get(6).loop(0);

            music.add(new Sound("Music" + filePathChar + "trackSuddenDeath.wav")); //7
            music.add(new Sound("Music" + filePathChar + "trackRobot.wav")); //8
        }

        System.out.println(StringAlignUtils.trim(centerMe.format("If you know the name of a saved map, enter it here.")));
        System.out.print(StringAlignUtils.trim(centerMe.format("Otherwise, leave blank for a list of maps: "))+ " ");
        System.out.print(StringAlignUtils.trim(centerMe.format(" "))+ " ");
        sc.nextLine();
        String answer = sc.nextLine();
        if(answer.equals("")) {

            File[] files = new File("Maps/").listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return !name.equals(".DS_Store") && !name.equals("_DS_Store") ;
                        }
                    });

            System.out.println(StringAlignUtils.trim(centerMe.format("List of maps: "))+" ");

            if( files.length == 0) {
                System.out.println(centerMe.format("No maps found in folder .../Maps"));
            } else {
                String printTitles = "";
                for(int i = 0; i < files.length; i++ )
                {
                    if(i%8 == 0)
                    {
                        printTitles = files[i].toString().substring(5, files[i].toString().length() - 4);
                    }
                    else
                    {
                        printTitles += ", " + files[i].toString().substring(5, files[i].toString().length() - 4);
                    }
                    if((i%8 == 0 && i !=0) || i == files.length - 1)
                    {
                        System.out.print(centerMe.format(printTitles));

                    }

                }
                System.out.print("");
            }

            System.out.print(StringAlignUtils.trim(centerMe.format("I want:"))+ " ");
            answer = sc.nextLine();
        }

        gameMap = MapManager.convertMap(answer + ".map");
        createPlayers(gameMap);
        //KeyBinder.closeFrame();
        try {
            KeyBinder.getFocus();
        }
        catch(AWTException e) { }

        //System.out.print(centerMe.format("Make sure to click back into the Key Binder!"));
        Thread.sleep(500);
        System.out.println(centerMe.format("3..."));
        Thread.sleep(500);
        System.out.println(centerMe.format("2..."));
        Thread.sleep(500);
        System.out.println(centerMe.format("1..."));
        Thread.sleep(500);
    }

    public static void createPlayers(char[][] map){
        //         Map.addPlayer(playerSymbols[0], 1, 0, 1);
        //         Map.addPlayer(playerSymbols[1], 2, 0, Map.getRows() - 2);
        //         Map.addPlayer(playerSymbols[2], 3, Map.getCols() - 1, 1);
        //         Map.addPlayer(playerSymbols[3], 4, Map.getCols() - 1, Map.getRows() - 2);
        boolean[] hasNotSpawn = new boolean[4];
        Arrays.fill(hasNotSpawn,true);
        for(int i = 1; i <= 4; i++) {
            for(int r = 0; r < map.length; r++) {
                for(int c = 0; c < map[r].length; c++) {
                    if(Character.getNumericValue(map[r][c]) == i) {
                        Map.addPlayer(playerSymbols[i - 1], i, c, r);
                        hasNotSpawn[i-1] = false;
                    }
                }
            }
        }
        for(int i = 0; i < hasNotSpawn.length; i++) {
            if(hasNotSpawn[i]) {
                switch(i) {
                    case 0: Map.addPlayer(playerSymbols[0], 1, 0, 1);
                    break;

                    case 1: Map.addPlayer(playerSymbols[1], 2, 0, Map.getRows() - 2);
                    break;

                    case 2: Map.addPlayer(playerSymbols[2], 3, Map.getCols() - 1, 1);
                    break;

                    case 3: Map.addPlayer(playerSymbols[3], 4, Map.getCols() - 1, Map.getRows() - 2);
                    break;

                }
            }
        }

    }

    public static void createPortals(char[][] map){
         for(int r = 0; r < map.length; r++) {
                for(int c = 0; c < map[r].length; c++) {
                    if(map[r][c] == '?' ) {
                        Map.addPortal(c, r);
                    }
                }
            }

    }

    public static void createBarriers(char[][] map) {

        //         Map.addBarrier(15,2);
        //         Map.addBarrier(15,3);
        //         Map.addReflectiveBarrier(15,4,1);
        //         Map.addReflectiveBarrier(15,14,3);
        //         Map.addBarrier(15,15);
        //         Map.addBarrier(15,16);
        //         Map.addReflectiveBarrier(15,17,1);
        //         Map.addReflectiveBarrier(15,25,3);
        //         Map.addBarrier(15,26);
        //         Map.addBarrier(15,27);
        //         Map.addBarrier(45,2);
        //         Map.addBarrier(45,3);
        //         Map.addReflectiveBarrier(45,4,3);
        //         Map.addReflectiveBarrier(45,14,1);
        //         Map.addBarrier(45,15);
        //         Map.addBarrier(45,16);
        //         Map.addReflectiveBarrier(45,17,3);
        //         Map.addReflectiveBarrier(45,25,1);
        //         Map.addBarrier(45,26);
        //         Map.addBarrier(45,27);

        for(int r = 0; r < map.length; r++) {
            for(int c = 0; c < map[0].length; c++) {
                if(map[r][c] == '‖') {
                    Map.addBarrier(c, r);
                }
                else if(map[r][c] == '/') {
                    Map.addReflectiveBarrier(c, r, 1);
                }
                else if(map[r][c] == '\\') {
                    Map.addReflectiveBarrier(c, r, 3);
                }
                else if(map[r][c] == '-') {
                    Map.addReflectiveBarrier(c, r, 2);
                }
                else if(map[r][c] == '|' && c!= 30) {
                    Map.addReflectiveBarrier(c, r, 0);
                }
            }
        }

    }    

    public static void addRandomLevelUps(){
        if(frameCount % pupDelay == 0) {
            Map.addLevelUp(Map.getRandX(1), Map.getRandY());
            Map.addLevelUp(Map.getRandX(2), Map.getRandY());
        }

    }

    public static void checkRobotMove() {
        Map.getRobot().setX(Map.getTurfLine());
        robotFrameCount++;
        if (robotFrameCount >= robotFrameDelay) {
            Map.getRobot().nextMove();
            robotFrameCount = 0;
        }
    }

    public static String getRobotHealth() {
        if (Map.isRobot()) {
            return "\t\t\t\t\t\t\t\t       Robot Health: " + Map.getRobot().getHealth();
        }
        return "";
    }

    public static void updateGame() throws InterruptedException  { 
        frameCount++;
        Map.transfer();
        Map.update();
        Map.handleCollisions();
        String printThis = getTitle() + Map.getString(Map.getMap()) + "\n\n" + getTime() + "\n" + getRobotHealth();
        System.out.println( "\f" + printThis );
        pw.println( printThis + ls + "separator" );
        //System.out.print(getStatusGame());
        addRandomLevelUps();
        checkRobotMove();
        if(Map.getWinner() == 0){
            int[] deaths = {Map.getPlayer(0).getDeathCount(), Map.getPlayer(1).getDeathCount(), Map.getPlayer(2).getDeathCount(), Map.getPlayer(3).getDeathCount()};
            Thread.sleep(40);
            for(int i = 0; i < 4; i++) {
                Map.getPlayer(i).tickHasFired();
                Map.getPlayer(i).tickHasMoved();
            }
        }
        else {
            endGame(Map.getWinner());
            playingGame = false;
        }
    }

    public static void endGame(int teamWhoWonA) throws InterruptedException {    
        if(musicOpt) {
            stopAllMusic();
            music.get(3).loop(0);
        }    
        pw.close();
        //System.out.println(Map.getPlayer(1));
        menu.clear();
        menu.add("Return to Main Menu");
        menu.add("Save replay to file");
        menu.add("Exit");
        menuIdentifier = "end";
        //menuSelect = 0;

        teamWhoWon = teamWhoWonA;
        endTime = System.currentTimeMillis();
        //endPrint = getEndPrint();

        menu();
        //while (onMenu) {
        //    Thread.sleep(10);
        //}
        //music.get(2).stop();
    }

    public static String getTime()
    {
        return centerMe.format("< Time Elapsed: " + formatTime(originalTime, System.currentTimeMillis()) + " >");        
    }

    public static String getStatusEnd()
    {
        return ("\n\t\t\t\t\t\t\t\t\t P1 [" + playerSymbols[0] + "] Death Count: " + Map.getPlayer(0).getDeathCount() + ", + Level: " +  Map.getPlayer(0).getLevel())
        + ("\n\t\t\t\t\t\t\t\t\t P2 [" + playerSymbols[1] + "] Death Count: " + Map.getPlayer(1).getDeathCount() + ", + Level: " +  Map.getPlayer(1).getLevel())
        + ("\n\t\t\t\t\t\t\t\t\t P3 [" + playerSymbols[2] + "] Death Count: " + Map.getPlayer(2).getDeathCount() + ", + Level: " +  Map.getPlayer(2).getLevel())
        + ("\n\t\t\t\t\t\t\t\t\t P4 [" + playerSymbols[3] + "] Death Count: " + Map.getPlayer(3).getDeathCount() + ", + Level: " +  Map.getPlayer(3).getLevel())
        + ("\n\n\t\t\t\t\t\t\t\t\t Time Elapsed: " + formatTime(originalTime, endTime))
        + ("\n\t\t\t\t\t\t\t\t\t Music Track Number " + trackNum);
    }

    public static char[][] copy2DArray(char[][] in) {
        char[][] out = new char[in.length][in[0].length];
        for (int i = 0; i < in.length; i++) {
            for (int j = 0; j < in[0].length; j++) {
                out[i][j] = in[i][j];
            }
        }
        return out;
    }

    public static String formatTime(long o, long e) {
        String result = "";
        long diff = e - o;
        long seconds = diff/1000;
        long minutes = seconds/60;
        seconds = seconds % 60;
        result += minutes + " minutes ";
        result += " ";
        result += seconds + " seconds ";
        return result;
    }

    public static String getTime(long o, long e) {
        //to check when to start Sudden Death Mode
        String result = "";
        long diff = e - o;
        long seconds = diff/1000;
        long minutes = seconds/60;
        seconds = seconds % 60;
        result += minutes + "m" + seconds + "s";
        return result;
    }

    public static int getSeconds(long o, long e) {
        //to check when to start Sudden Death Mode
        String result = "";
        long diff = e - o;
        long seconds = diff/1000;
        long minutes = seconds/60;
        seconds = seconds % 60;
        return (int)seconds;
    }

    public static void loadSounds()
    {
        if(soundOpt) {
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "team1Gun.wav")); //0
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "team2Gun.wav")); //1
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "playerDeath.wav")); //2
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "barrierHitBr.wav")); //3
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "barrierHitUn.wav")); //4
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "barrierDeath.wav")); //5
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "recordScratch.wav")); //6
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "startStalemate.wav")); //7
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "Robot" + filePathChar + "robotEvacuate.wav")); //8
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "Robot" + filePathChar + "robotEntrance.wav")); //9
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "Robot" + filePathChar + "robotShoot.wav")); //10
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "Robot" + filePathChar + "robotDamage.wav")); //11
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "soundExplosion.wav")); //12
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "levelUp.wav")); //13
            soundFX.add(new Sound("Music" + filePathChar + "soundFX" + filePathChar + "abilityUnlock.wav")); //14
            setVolumes();
        }
    }

    public static void setVolumes()
    {
        if(soundOpt) {
            for(Sound s : soundFX) 
            {
                s.changeVolume(2f);
            }
        }
        if(soundOpt) {
            soundFX.get(11).changeVolume(4f);
        }
    }

    public static long getOriginalTime() {
        return originalTime;
    }

    public static Sound getSound(int index)
    {
        return soundFX.get(index);
    }

    public static boolean getGameBool() {
        return playingGame;        
    }

    public static boolean getReplayBool() {
        return inReplay;        
    }

    public static void setPUPDelay(int del) {
        pupDelay = del;
    }

    public static void setRobotFrameDelay(int i) {
        robotFrameDelay = i;
    }

    public static String getRandomTip() {
        int temp = (int) (Math.random() * 30);

        switch(temp) {
            case 0:
            return "Two players that collide will kill each other, so watch out!";

            case 1:
            return "Double lined barriers will never break, so don't shoot them!";

            case 2:
            return "If friendly fire is enabled, try to get the enemy to shoot themselves!";

            case 3: 
            return "Barriers will randomly appear on your side. Use them to your advantage!";

            case 4: 
            return "Bullets have a firing delay. Don't spam them!";

            case 5: 
            return "A player can be hit on any of his three character symbols!";

            case 6: 
            return "Listen to the sounds to understand what your bullet has done!";

            case 7: 
            return "You can be trapped very easily. Always leave an escape route!";

            case 8: 
            return "Make sure to save replays of fun games when they are over!";

            case 9: 
            return "You can learn lots of info from the How To Play Section!";

            case 10: 
            return "Music and Sound Effects can be turned off in the Options!";

            case 11: 
            return "During the game, always make sure you are clicked into the Key Binder on the top left!";

            case 12: 
            return "Try not to type into the console, because that input won't move your player!";

            case 13: 
            return "Stats such as time elapsed and death count are displayed at the bottom of the screen!";

            case 14: 
            return "You can check your lifetime Statistics on the Main Menu!";

            case 15: 
            return "For extra fun, plug in two keyboards!";

            case 16: 
            return "During Sudden Death, there is no firing delay!";

            case 17: 
            return "During Sudden Death, the turf lines moves 5 times as fast!";

            case 18: 
            return "If the game goes on for too long, Sudden Death will occur!";

            case 19: 
            return "Level Ups decrease bullet firing delay!";

            case 20: 
            return "At higher levels, bullets travel in many directions!";

            case 21: 
            return "Try to pick up as many Level Ups as possible!";

            case 22:
            return "Make sure to exit using the proper menus to save your statistics!";

            case 23:
            return "Watch out for the sneaky robot!";

            case 24:
            return "If you destroy the robot, you gain a special power!";

            case 25:
            return "Reflective barriers will allow you to take those hard-to-aim shots!";

            case 26:
            return "Portals can be a teleportative lifesaver!";

            case 27:
            return "Try out the Map Editor!";

            case 28:
            return "You can play your own maps with the Map Editor!";

            case 29:
            return "You can share your .map files with your friends!";
        }
        return "All bullets travel horizontally!";
    }

}
