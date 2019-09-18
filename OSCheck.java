public class OSCheck
{
    private static String OS = null;

    public static String getOSName()
    {
        if(OS == null) {
            OS = System.getProperty("os.name"); 
        }
        return OS;
    }

    public static boolean isWindows() {
        return getOSName().startsWith("Windows");
    }

    public static boolean isMac() {
        return getOSName().startsWith("Mac");
    }
}