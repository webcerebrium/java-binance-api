package binance.api;

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

    ONE_MIN("1m"), M1("1m"),
    THREE_MIN("3m"), M3("3m"),
    FIVE_MIN("5m"), M5("5m"),
    FIFTEEN_MIN("15m"), M15("15m"),
    THIRTY_MIN("30m"), M30("30m"),

    ONE_HOUR("1h"), H1("1h"),
    TWO_HOURS("2h"), H2("2h"),
    FOUR_HOURS("4h"), H4("4h"),
    SIX_HOURS("6h"), H6("6h"),
    EIGHT_HOURS("8h"), H8("8h"),
    TWELVE_HOURS("12h"), H12("12h"),

    ONE_DAY("1d"), D1("1d"),
    THREE_DAYS("3d"), D3("3d"),
    ONE_WEEK("1w"), W1("1w"),
    ONE_MONTH("1M"), MONTH("1M")

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
}
