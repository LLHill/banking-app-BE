package com.bankin.app.utils;

public class CardGenerator {
    private static final long FST_SEQ = 1456;
    private static final long SND_SEQ = 4523;
    private static final long TRD_SEQ = 1256;
    private static final long FTH_SEQ = 7894;

    private static String calcSubNum(String base, long added){
        long calcResult = Math.abs(Long.parseLong(base) - added);
        return String.format("%04d", calcResult);
    }


    public static String generateCardNumber(){
        long curMils = System.currentTimeMillis();
        String curMilsStr = Long.toString(curMils);
        String fstSeq = calcSubNum(curMilsStr.substring(0, 4), FST_SEQ);
        String sndSeq = calcSubNum(curMilsStr.substring(4, 8), SND_SEQ);
        String trdSeq = calcSubNum(curMilsStr.substring(8, 12), TRD_SEQ);
        String fthSeq = calcSubNum(curMilsStr.substring(9), FTH_SEQ);
        return fstSeq + "-" + sndSeq  + "-" + trdSeq  + "-" + fthSeq;
    }

    public static String generateCVV(){
        return String.format("%03d", System.currentTimeMillis() % 1000);
    }
}
