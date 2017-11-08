package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.Data;

import java.math.BigDecimal;

/**
 {
 "e": "aggTrade",		// event type
 "E": 1499405254326,	// event time
 "s": "ETHBTC",			// symbol
 "a": 70232,			// aggregated tradeid
 "p": "0.10281118",		// price
 "q": "8.15632997",		// quantity
 "f": 77489,			// first breakdown trade id
 "l": 77489,			// last breakdown trade id
 "T": 1499405254324,	// trade time
 "m": false,			// whehter buyer is a maker
 "M": true				// can be ignored
 }
 */
@Data
public class BinanceEventAggTrade {
    public Long eventTime;
    public BinanceSymbol symbol;
    public Long aggregatedTradeId;
    public BigDecimal price;
    public BigDecimal quantity;
    public Long firstBreakdownTradeId;
    public Long lastBreakdownTradeId;
    public Long tradeTime;
    public boolean isMaker;

    public BinanceEventAggTrade(JsonObject event) throws BinanceApiException {
        eventTime = event.get("E").getAsLong();
        symbol = BinanceSymbol.valueOf(event.get("s").getAsString());
        aggregatedTradeId = event.get("a").getAsLong();
        price = event.get("p").getAsBigDecimal();
        quantity = event.get("q").getAsBigDecimal();
        firstBreakdownTradeId = event.get("f").getAsLong();
        lastBreakdownTradeId = event.get("l").getAsLong();
        tradeTime = event.get("T").getAsLong();
        isMaker = event.get("m").getAsBoolean();
    }
}
