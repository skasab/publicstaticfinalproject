
/**
 *Adapted from Lokesh Gupta
 */

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class StringAlignUtils extends Format {

    private static final long serialVersionUID = 1L;

    /** Current max length in a line */
    private int maxChars = 180;

    public StringBuffer format(Object input, StringBuffer where, FieldPosition ignore)
    {
        String s = input.toString();
        List<String> strings = splitInputString(s);
        ListIterator<String> listItr = strings.listIterator();

        while (listItr.hasNext()) 
        {
            String wanted = listItr.next();

            //Get the spaces in the right place.

            int toAdd = maxChars - wanted.length();
            pad(where, toAdd / 2);
            where.append(wanted);
            pad(where, toAdd - toAdd / 2);

        }
        where.append("\n");
        return where;
    }
   

    protected final void pad(StringBuffer to, int howMany) {
        for (int i = 0; i < howMany; i++)
            to.append(' ');
    }

    String format(String s) {
        return format(s, new StringBuffer(), null).toString();
    }

    /** ParseObject is required, but not useful here. */
    public Object parseObject(String source, ParsePosition pos) {
        return source;
    }

    private List<String> splitInputString(String str) {
        List<String> list = new ArrayList<String>();
        if (str == null)
            return list;
        for (int i = 0; i < str.length(); i = i + maxChars) 
        {
            int endindex = Math.min(i + maxChars, str.length());
            list.add(str.substring(i, endindex));
        }
        return list;
    }

    public static String trim(String str) {
        int len = str.length();
        int st = 0;

        char[] val = str.toCharArray();

        while ((st < len) && (val[len - 1] <= ' ')) {
            len--;
        }
        return str.substring(st, len);
    }
}