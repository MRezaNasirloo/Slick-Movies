package com.github.pedramrn.slick.parent.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2017-08-25
 */
public class DateUtilsTest {
    @Test
    public void age() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1991, 1, 20);
        Date birthDate = calendar.getTime();
        calendar.set(2017, 8, 25);
        Date now = calendar.getTime();

        assertEquals(26, DateUtils.age(birthDate, now));
    }

}