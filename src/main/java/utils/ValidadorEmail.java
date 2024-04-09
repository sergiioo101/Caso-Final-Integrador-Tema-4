package utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidadorEmail {

    private static final Pattern patronEmail = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                    "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    public static boolean esEmailValido(String email) {
        if (email == null) return false;
        Matcher matcher = patronEmail.matcher(email);
        return matcher.matches();
    }
}

