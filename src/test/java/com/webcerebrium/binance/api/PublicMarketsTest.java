package com.webcerebrium.binance.api;
/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.datatype.BinanceExchangeStats;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j

public class PublicMarketsTest {
    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testPublicMarkets() throws Exception, BinanceApiException {
        BinanceExchangeStats binanceExchangeStats = binanceApi.publicStats();
        log.info("Exchange Stats: {}", binanceExchangeStats.toString());
    }
}
