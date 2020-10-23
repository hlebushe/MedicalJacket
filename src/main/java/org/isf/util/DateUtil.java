package org.isf.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String format(String source) {
        SimpleDateFormat simpleDateFormatSource = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        SimpleDateFormat simpleDateFormatTarget = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        try {
            return simpleDateFormatTarget.format(simpleDateFormatSource.parse(source));
        } catch (ParseException e) {
            return source;
        }
    }

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormatTarget = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.S", Locale.ENGLISH);
        return simpleDateFormatTarget.format(date);
    }
}
