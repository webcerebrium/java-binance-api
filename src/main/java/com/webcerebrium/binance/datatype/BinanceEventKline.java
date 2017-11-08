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
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
/*
    {
        "e": "kline",							// event type
        "E": 1499404907056,						// event time
        "s": "ETHBTC",							// symbol
        "k": {
            "t": 1499404860000, 				// start time of this bar
            "T": 1499404919999, 				// end time of this bar
            "s": "ETHBTC",						// symbol
            "i": "1m",							// interval
            "f": 77462,							// first trade id
            "L": 77465,							// last trade id
            "o": "0.10278577",					// open
            "c": "0.10278645",					// close
            "h": "0.10278712",					// high
            "l": "0.10278518",					// low
            "v": "17.47929838",					// volume
            "n": 4,								// number of trades
            "x": false,							// whether this bar is final
            "q": "1.79662878",					// quote volume
            "V": "2.34879839",					// volume of active buy
            "Q": "0.24142166",					// quote volume of active buy
            "B": "13279784.01349473"			// can be ignored
            }
        }
    }
*/

@Data
@Slf4j
public class BinanceEventKline {
    public Long eventTime;
    public BinanceSymbol symbol;
    public BinanceInterval interval;

    public Long startTime;
    public Long endTime;

    public Long firstTradeId;
    public Long lastTradeId;

    public BigDecimal open;
    public BigDecimal close;
    public BigDecimal high;
    public BigDecimal low;
    public BigDecimal volume;

    public Long numberOfTrades;
    public boolean isFinal;

    public BigDecimal quoteVolume;
    public BigDecimal volumeOfActiveBuy;
    public BigDecimal quoteVolumeOfActiveBuy;

    public BinanceEventKline(JsonObject event) throws BinanceApiException {
        eventTime = event.get("E").getAsLong();
        symbol = BinanceSymbol.valueOf(event.get("s").getAsString());

        JsonObject k = event.get("k").getAsJsonObject();
        log.info(k.get("i").getAsString());
        interval = BinanceInterval.lookup(k.get("i").getAsString());

        startTime = k.get("t").getAsLong();
        endTime  = k.get("T").getAsLong();

        firstTradeId  = k.get("f").getAsLong();
        lastTradeId  = k.get("L").getAsLong();

        open = k.get("o").getAsBigDecimal();
        close = k.get("c").getAsBigDecimal();
        high = k.get("h").getAsBigDecimal();
        low = k.get("l").getAsBigDecimal();
        volume = k.get("v").getAsBigDecimal();

        numberOfTrades  = k.get("n").getAsLong();
        isFinal  = k.get("x").getAsBoolean();

        quoteVolume = k.get("q").getAsBigDecimal();
        volumeOfActiveBuy = k.get("V").getAsBigDecimal();
        quoteVolumeOfActiveBuy = k.get("Q").getAsBigDecimal();
    }
}
