import java.io.*;
import java.util.*;

public class Statistics  {
    // total death count, total bullets fired, games played, 
    // sudden deaths activated, friendly fire shots, barriers destroyed
    // favorite music track, favorite player symbol
    private static String fileName = "stats.txt";
    //private static String line = null;

    private static long deathCount = 0;
    private static long bulletsFired = 0;
    private static long gamesPlayed = 0;
    private static long suddenDeath = 0;
    private static long friendlyFire = 0;
    private static long levelUpsPicked = 0;
    private static long robotsKilled = 0;
    private static long robotsActivated = 0;
    private static long replaysSaved = 0;
    //private static long[] favTrack = new long[35];
    //private static List<Character> favSymbol = new ArrayList<Character>();
    //private static List<String> arrayOfThings = new ArrayList<String>();

    private static List<Long> stats = new ArrayList<Long>();
    private static List<String> printStats = new ArrayList<String>();
    private static List<String> statsNames = new ArrayList<String>();

    public static void readFile() {
        String line = null;

        statsNames.addAll(Arrays.asList("Death Count: ","Bullets Fired: ","Games Played: ","Sudden Death Activated: ",
                "Friendly Fire Kills: ","Level Ups Picked Up: ","Robots Activated: ","Robots Killed: ","Replays Saved: ", "Favorite Track: ","Favorite Symbol: "));
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                printStats.add(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }

//         readAndUpdateArrays("trackList.txt");
//         printStats.add("" + getFavTrack());
//         readAndUpdateArrays("symbols.txt");
//         printStats.add("" + getFavSymbol());

    }

    public static void upDeathCount() {
        deathCount++;
    }

    public static void upBulletsFired() {
        bulletsFired++;
    }

    public static void upGamesPlayed() {
        gamesPlayed++;
    }

    public static void upSuddenDeath() {
        suddenDeath++;
    }

    public static void upFriendlyFire() {
        friendlyFire++;
    }

    public static void upLevelUpsPicked() {
        levelUpsPicked++;
    }

    public static void upRobotsKilled() {
        levelUpsPicked++;
    }

    public static void upRobotsActivated() {
        levelUpsPicked++;
    }

    public static void upReplaysSaved() {
        levelUpsPicked++;
    }

//     public static void addTrack(int track) {
//         favTrack[track - 1]++;
//     }
// 
//     public static void addSym(char sym) {
//         favSymbol.add(sym);
//     }

    public static long getDeathCount() {
        return deathCount;
    }

    public static long getBulletsFired() {
        return bulletsFired;
    }

    public static long getGamesPlayed() {
        return gamesPlayed;
    }

    public static long getSuddenDeath() {
        return suddenDeath;
    }

    public static long getFriendlyFire() {
        return friendlyFire;
    }

    public static long getLevelUpsPicked() {
        return levelUpsPicked;
    }

    public static void addCounts() {     
        stats.addAll(Arrays.asList(deathCount,bulletsFired,gamesPlayed,suddenDeath,friendlyFire,levelUpsPicked,robotsActivated,robotsKilled,replaysSaved));
        //System.out.println(stats);
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            //while((bufferedReader.readLine()) != null) {
            for(int i = 0; i < stats.size(); i++)
            {
                stats.set(i,stats.get(i) + Integer.parseInt(bufferedReader.readLine()));
            }     
            //}   
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"  + fileName + "'");                  
        }
        catch(NumberFormatException ex)
        {
            ex.printStackTrace();
        }

    }

    public static void updateFile() {    

        try {
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for(int i = 0; i < stats.size(); i++)
            {
                bufferedWriter.write(("" + stats.get(i)));
                bufferedWriter.newLine();           
            }     

            bufferedWriter.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println("Error reading file '"  + fileName + "'");                  
        }

    }

    public static void saveStats() {
        addCounts();
        updateFile();
//         readAndUpdateArrays("trackList.txt");
//         readAndUpdateArrays("symbols.txt");
    }

    public static int getStatsSize() {
        return printStats.size();
    }

    public static String getStat(int i) {
        return printStats.get(i);
    }

    public static String getStatName(int i) {
        return statsNames.get(i);
    }

    //     public static void readAndUpdateArrays(String arrName) {
    //         String line = null;
    // 
    //         try {
    //             FileReader fileReader = new FileReader(arrName);
    //             BufferedReader bufferedReader = new BufferedReader(fileReader);
    // 
    //             while((line = bufferedReader.readLine()) != null) {
    //                 arrayOfThings.add((line));
    //             }   
    // 
    //             bufferedReader.close();         
    //         }
    //         catch(FileNotFoundException ex) {
    //             System.out.println( "Unable to open file '" +  arrName + "'");                
    //         }
    //         catch(IOException ex) {
    //             System.out.println( "Error reading file '" + arrName + "'");                  
    //         }
    // 
    //         updateArrays(arrName);
    // 
    //         try {
    //             FileWriter fileWriter = new FileWriter(arrName);
    //             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    // 
    //             if(arrName.equals("trackList.txt")) {
    //                 for(int i = 0; i < favTrack.length ; i++)
    //                 {
    //                     bufferedWriter.write("" + favTrack[i]);
    //                     bufferedWriter.newLine();
    //                 }  
    //             }
    //             else if(arrName.equals("symbols.txt")) {
    //                 for(int i = 0; i < favSymbol.size() ; i++)
    //                 {
    //                     bufferedWriter.write(("" + favSymbol.get(i)));
    //                     bufferedWriter.newLine();
    //                 }   
    //             }
    // 
    //             bufferedWriter.close();         
    //         }
    //         catch(FileNotFoundException ex) {
    //             System.out.println("Unable to open file '" + arrName + "'");                
    //         }
    //         catch(IOException ex) {
    //             System.out.println("Error reading file '"  + arrName + "'");                  
    //         }
    // 
    //     }
    // 
    //     public static void updateArrays(String arrName) {
    //         if(arrName.equals("trackList.txt")) {
    //             for(int i = 0; i < favTrack.length; i++) {
    //                 Integer a = (Integer) Integer.parseInt(arrayOfThings.get(i));
    //                 favTrack[i] += (Long)(a.longValue());
    //             }
    //         }
    //         else if(arrName.equals("symbols.txt")) {
    //             for(int i = 0; i < arrayOfThings.size(); i++) {
    //                 favSymbol.add(arrayOfThings.get(i).charAt(0));
    //             }
    //         }
    //         arrayOfThings.clear();
    //     }
    // 
    //     public static int getFavTrack()
    //     {
    //         int count = 1, tempCount;
    //         long fav = favTrack[0];
    //         long temp = 0;
    //         int favIndex = 0;
    //         for (int i = 0; i < (favTrack.length); i++)
    //         {
    //             temp = favTrack[i];
    //             tempCount = 0;
    //             for (int j = 1; j < favTrack.length; j++)
    //             {
    //                 if (temp == favTrack[j])
    //                     tempCount++;
    //             }
    //             if (tempCount > count)
    //             {
    //                 fav = temp;
    //                 favIndex = i;
    //                 count = tempCount;
    //             }
    //         }
    // 
    //         return favIndex;
    //     }
    // 
    //     public static Character getFavSymbol()
    //     {
    //         int count = 1, tempCount;
    //         Character fav = favSymbol.get(0);
    //         Character temp = 0;
    //         for (int i = 0; i < (favSymbol.size() - 1); i++)
    //         {
    //             temp =  favSymbol.get(i);
    //             tempCount = 0;
    //             for (int j = 1; j < favSymbol.size(); j++)
    //             {
    //                 if (temp == favSymbol.get(j))
    //                     tempCount++;
    //             }
    //             if (tempCount > count)
    //             {
    //                 fav = temp;
    //                 count = tempCount;
    //             }
    //         }
    // 
    //         return fav;
    //     }
    // 
    //     public static void printArr() {
    // 
    //         for(long a : favTrack) 
    //             System.out.println(a);
    //     }
    //      public static void printSym() {
    // 
    //         for(Character a : favSymbol) 
    //             System.out.println(a);
    //     }
}
