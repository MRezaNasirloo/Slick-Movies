package com.github.pedramrn.slick.parent.util;

import java.util.Calendar;
import java.util.Date;

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
}
