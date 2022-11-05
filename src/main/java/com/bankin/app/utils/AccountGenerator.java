package com.bankin.app.utils;

import java.util.Date;

public class AccountGenerator {
    private static final long INIT_DATE = 948589200;


    public static String generateSequence (){
        long curMils = System.currentTimeMillis();
        return Long.toString(curMils - INIT_DATE);
    }
}
