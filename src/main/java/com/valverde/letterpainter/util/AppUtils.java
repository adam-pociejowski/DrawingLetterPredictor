package com.valverde.letterpainter.util;

import com.valverde.letterpainter.enums.Letter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AppUtils {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNotNull(Object o) {
        return o != null;
    }

    public static Date getDate(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar().getTime();
    }

    public static int getLetterPositionInAlphabet(final Letter letter) {
        String name = letter.name();
        char letterChar = name.toLowerCase().charAt(0);
        return letterChar - 'a' + 1;
    }

    public static Letter getLetterByNumber(final int number) {
        List<Letter> letters = Arrays.asList(Letter.values());
        for (Letter letter : letters) {
            if (letter.getNumber() == number)
                return letter;
        }
        throw new IllegalArgumentException("No letter found for number "+number);
    }
}
