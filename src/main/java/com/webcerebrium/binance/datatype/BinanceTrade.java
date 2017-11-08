package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import lombok.Data;

import java.math.BigDecimal;

/**
 {
 "id": 28457,
 "price": "4.00000100",
 "qty": "12.00000000",
 "commission": "10.10000000",
 "commissionAsset": "BNB",
 "time": 1499865549590,
 "isBuyer": true,
 "isMaker": false,
 "isBestMatch": true
 }
 */

@Data
public class BinanceTrade {
    public Long id;
    public String commissionAsset;
    public BigDecimal price;
    public BigDecimal qty;
    public BigDecimal commission;
    public Long time;
    public boolean isBuyer;
    public boolean isMaker;
    public boolean isBestMatch;
}
