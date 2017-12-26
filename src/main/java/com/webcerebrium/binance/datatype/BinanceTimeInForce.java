package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

public enum BinanceTimeInForce {
    GTC("GTC"),
    GOOD_TILL_CANCELLED("GTC"),
    IMMEDIATE_OR_CANCEL("IOC"),
    IOC("IOC"),
    FILL_OR_KILL("FOK"),
    FOK("FOK")
    ;

    private String value;
    BinanceTimeInForce(final String value) {
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
