package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

/*
"symbol": "LTCBTC",
"bidPrice": "4.00000000",
"bidQty": "431.00000000",
"askPrice": "4.00000200",
"askQty": "9.00000000"
*/

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceTicker {
    public String symbol = null;
    public BigDecimal bidPrice = null;
    public BigDecimal bidQty = null;
    public BigDecimal askPrice = null;
    public BigDecimal askQty = null;
}
