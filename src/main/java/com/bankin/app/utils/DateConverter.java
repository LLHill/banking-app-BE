package com.bankin.app.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    public static String strConverter(String format, Date date){
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        return fmt.format(date);
    }
}
