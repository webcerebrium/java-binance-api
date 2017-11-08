package com.webcerebrium.binance.datatype;
/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

public enum BinanceRejectReason {
    NONE,
    UNKNOWN_INSTRUMENT,
    MARKET_CLOSED,
    PRICE_QTY_EXCEED_HARD_LIMITS,
    UNKNOWN_ORDER,
    DUPLICATE_ORDER,
    UNKNOWN_ACCOUNT,
    INSUFFICIENT_BALANCE,
    ACCOUNT_INACTIVE,
    ACCOUNT_CANNOT_SETTLE
}
