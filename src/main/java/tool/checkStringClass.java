package tool;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class checkStringClass implements checkString {
    @Override
    public boolean checkString(String string) {
        Pattern p = Pattern.compile("[^a-z0-9\\x{0E01}-\\x{0E3A}\\x{0E40}-\\x{0E4C} ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        boolean b = m.find();
        if (!b && StringUtils.hasText(string)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkString(String string, String exceptionString) {
        Pattern p = Pattern.compile("[^a-z0-9\\x{0E01}-\\x{0E3A}\\x{0E40}-\\x{0E4C} "+ exceptionString+"]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        boolean b = m.find();
        if (!b && StringUtils.hasText(string)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean checkNum(String string) {
        try {
            if (StringUtils.containsWhitespace(string)){
                return false;
            }
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
