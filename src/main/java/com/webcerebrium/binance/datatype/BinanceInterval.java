package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// valid
// 1m,3m,5m,15m,30m,
// 1h,2h,4h,6h,8h,12h,
// 1d,3d,1w,1M

public enum BinanceInterval {

    ONE_MIN("1m"),
    THREE_MIN("3m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),

    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    SIX_HOURS("6h"),
    EIGHT_HOURS("8h"),
    TWELVE_HOURS("12h"),

    ONE_DAY("1d"),
    THREE_DAYS("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1M")
    ;
    private String value;

    BinanceInterval(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    static public BinanceInterval lookup(String val) {
        if (val.equals(ONE_MIN.toString())) return ONE_MIN;
        if (val.equals(THREE_MIN.toString())) return THREE_MIN;
        if (val.equals(FIVE_MIN.toString())) return FIVE_MIN;
        if (val.equals(FIFTEEN_MIN.toString())) return FIFTEEN_MIN;
        if (val.equals(THIRTY_MIN.toString())) return THIRTY_MIN;

        if (val.equals(ONE_HOUR.toString())) return ONE_HOUR;
        if (val.equals(TWO_HOURS.toString())) return TWO_HOURS;
        if (val.equals(FOUR_HOURS.toString())) return FOUR_HOURS;
        if (val.equals(SIX_HOURS.toString())) return SIX_HOURS;
        if (val.equals(EIGHT_HOURS.toString())) return EIGHT_HOURS;
        if (val.equals(TWELVE_HOURS.toString())) return TWELVE_HOURS;

        if (val.equals(ONE_DAY.toString())) return ONE_DAY;
        if (val.equals(THREE_DAYS.toString())) return THREE_DAYS;
        if (val.equals(ONE_WEEK.toString())) return ONE_WEEK;
        return ONE_MONTH;
    }

}
