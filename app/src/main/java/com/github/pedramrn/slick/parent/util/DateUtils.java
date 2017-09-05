package com.github.pedramrn.slick.parent.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-25
 */

public class DateUtils {

    public static int age(Date birthDate) {
        return age(birthDate, Calendar.getInstance().getTime());
    }

    public static int age(Date birthDate, Date toDate) {
        Calendar bornDate = getInstance();
        Calendar toDateCal = getInstance();
        bornDate.setTime(birthDate);
        toDateCal.setTime(toDate);

        int age = toDateCal.get(YEAR) - bornDate.get(YEAR);
        if (toDateCal.get(DAY_OF_YEAR) <= bornDate.get(DAY_OF_YEAR)) age--;
        return age;
    }

    public static Date toDate(String date) throws ParseException, NullPointerException {
        SimpleDateFormat dateFormatNum = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormatNum.parse(date);
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat dateFormatName = new SimpleDateFormat(pattern, Locale.getDefault());
        return dateFormatName.format(date);
    }

    public static String format_MMM_dd_yyyy(Date date) {
        SimpleDateFormat dateFormatName = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormatName.format(date);
    }

    public static String format_yyyy(Date date) {
        SimpleDateFormat dateFormatName = new SimpleDateFormat("yyyy", Locale.getDefault());
        return dateFormatName.format(date);
    }
}
