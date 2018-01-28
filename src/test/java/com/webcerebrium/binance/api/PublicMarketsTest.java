package com.webcerebrium.binance.api;
/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.datatype.BinanceExchangeInfo;
import com.webcerebrium.binance.datatype.BinanceExchangeStats;
import com.webcerebrium.binance.datatype.BinanceExchangeSymbol;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        log.info("Public Exchange Stats (not documented): {}", binanceExchangeStats.toString());
    }

    @Test
    public void testExchangeInfo() throws Exception, BinanceApiException {
        BinanceExchangeInfo binanceExchangeInfo = binanceApi.exchangeInfo();
        List<BinanceExchangeSymbol> symbols = binanceExchangeInfo.getSymbols();
        // BinanceExchangeSymbol BNB = symbols.stream().filter(a -> a.getQuoteAsset().equals("BNB")).findFirst().get();
        // log.info("BNB Lot Size: {}", BNB.getLotSize().toString());
        symbols
        .stream()
        .filter(b -> (b.getBaseAsset().equals("BNB") || b.getQuoteAsset().equals("BNB")))
        .forEach(a -> {
             log.info("Base: {} Quote: {} Lot Size: {}", a.getBaseAsset(), a.getQuoteAsset(), a.getLotSize().toString());
        });
    }
}
