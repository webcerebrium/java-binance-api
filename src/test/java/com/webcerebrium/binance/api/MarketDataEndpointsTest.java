package com.webcerebrium.binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.datatype.BinanceAggregatedTrades;
import com.webcerebrium.binance.datatype.BinanceCandlestick;
import com.webcerebrium.binance.datatype.BinanceInterval;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import com.webcerebrium.binance.datatype.BinanceTicker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Slf4j
public class MarketDataEndpointsTest {

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testDepthEndpoint() throws Exception, BinanceApiException {
        JsonObject jsonObject = binanceApi.depth(symbol);

        assertTrue("depth response should contain lastUpdateId", jsonObject.has("lastUpdateId"));
        assertTrue("depth response should contain bids", jsonObject.has("bids"));
        JsonArray bids = jsonObject.get("bids").getAsJsonArray();
        assertTrue("depth response should contain non-empty array bids", bids.size() > 0);
        assertTrue(bids.get(0).getAsJsonArray().size() > 2);
        assertTrue("depth response should contain asks", jsonObject.has("asks"));
        JsonArray asks = jsonObject.get("asks").getAsJsonArray();
        assertTrue("depth response should contain non-empty array asks", asks.size() > 0);
        assertTrue(asks.get(0).getAsJsonArray().size() > 2);
    }

    @Test
    public void testAggTradesEndpoint() throws Exception, BinanceApiException {
        List<BinanceAggregatedTrades> binanceAggregatedTrades = binanceApi.aggTrades(symbol, 5, null);

        assertTrue("Aggregated trades array should be received", binanceAggregatedTrades.size() > 0);
        // check human-looking getters for the first picked trade
        BinanceAggregatedTrades trade = binanceAggregatedTrades.get(0);

        assertTrue("First Trade should contain tradeId", trade.getTradeId() > 0);
        assertTrue("First Trade should contain price", trade.getPrice().compareTo(BigDecimal.ZERO) > 0);
        assertTrue("First Trade should contain quantity", trade.getQuantity().compareTo(BigDecimal.ZERO) > 0);
        assertTrue("First Trade should contain firstTradeId", trade.getFirstTradeId() > 0);
        assertTrue("First Trade should contain lastTradeId", trade.getLastTradeId() > 0);
        assertTrue("First Trade should contain timestamp", trade.getTimestamp() > 0);
    }

    @Test
    public void testIntervalsAreConvertedToStrings() throws Exception {
        assertTrue("15min check", BinanceInterval.FIFTEEN_MIN.toString().equals("15m"));
        assertTrue("1 hour check", BinanceInterval.ONE_HOUR.toString().equals("1h"));
        assertTrue("1 month check", BinanceInterval.ONE_MONTH.toString().equals("1M"));
    }

    @Test
    public void testKlinesEndpoint() throws Exception, BinanceApiException {
        // checking intervals
        List<BinanceCandlestick> klines = binanceApi.klines(symbol, BinanceInterval.FIFTEEN_MIN, 5, null);
        assertTrue("Klines should return non-empty array of candlesticks", klines.size() > 0);

        BinanceCandlestick firstCandlestick = klines.get(0);
        log.info(firstCandlestick.toString());
        assertNotNull("Candlestick should contain open", firstCandlestick.getOpen());
        assertNotNull("Candlestick should contain high", firstCandlestick.getHigh());
        assertNotNull("Candlestick should contain low", firstCandlestick.getLow());
        assertNotNull("Candlestick should contain close", firstCandlestick.getClose());
        assertNotNull("Candlestick should contain openTime", firstCandlestick.getOpenTime());
        assertNotNull("Candlestick should contain closeTime", firstCandlestick.getCloseTime());
        assertNotNull("Candlestick should contain numberOfTrades", firstCandlestick.getNumberOfTrades());
        assertNotNull("Candlestick should contain volume", firstCandlestick.getVolume());
        assertNotNull("Candlestick should contain quoteAssetVolume", firstCandlestick.getQuoteAssetVolume());
        assertNotNull("Candlestick should contain takerBuyBaseAssetVolume", firstCandlestick.getTakerBuyBaseAssetVolume());
        assertNotNull("Candlestick should contain takerBuyQuoteAssetVolume", firstCandlestick.getTakerBuyQuoteAssetVolume());
    }

    @Test
    public void testTicker24hrEndpoint() throws Exception, BinanceApiException {
        JsonObject jsonObject = binanceApi.ticker24hr(symbol);

        assertTrue("24hr ticker should contain priceChange", jsonObject.has("priceChange"));
        assertTrue("24hr ticker should contain priceChangePercent", jsonObject.has("priceChangePercent"));
        assertTrue("24hr ticker should contain weightedAvgPrice", jsonObject.has("weightedAvgPrice"));
        assertTrue("24hr ticker should contain prevClosePrice", jsonObject.has("prevClosePrice"));
        assertTrue("24hr ticker should contain lastPrice", jsonObject.has("lastPrice"));
        assertTrue("24hr ticker should contain lastQty", jsonObject.has("lastQty"));
        assertTrue("24hr ticker should contain bidPrice", jsonObject.has("bidPrice"));
        assertTrue("24hr ticker should contain bidQty", jsonObject.has("bidQty"));
        assertTrue("24hr ticker should contain askQty", jsonObject.has("askQty"));
        assertTrue("24hr ticker should contain askQty", jsonObject.has("askQty"));
        assertTrue("24hr ticker should contain openPrice", jsonObject.has("openPrice"));
        assertTrue("24hr ticker should contain highPrice", jsonObject.has("highPrice"));
        assertTrue("24hr ticker should contain lowPrice", jsonObject.has("lowPrice"));
        assertTrue("24hr ticker should contain volume", jsonObject.has("volume"));
        assertTrue("24hr ticker should contain quoteVolume", jsonObject.has("quoteVolume"));
        assertTrue("24hr ticker should contain openTime", jsonObject.has("openTime"));
        assertTrue("24hr ticker should contain closeTime", jsonObject.has("closeTime"));
        assertTrue("24hr ticker should contain firstId", jsonObject.has("firstId"));
        assertTrue("24hr ticker should contain lastId", jsonObject.has("lastId"));
        assertTrue("24hr ticker should contain count", jsonObject.has("count"));
    }

    @Test
    public void testAllPricesEndpoint() throws Exception, BinanceApiException {
        BigDecimal ethbtc = binanceApi.pricesMap().get(symbol.toString());
        assertTrue("There should be price for " + symbol.toString(), ethbtc.compareTo(BigDecimal.valueOf(0)) > 0);
    }

    @Test
    public void testAllBookTickersEndpoint() throws Exception, BinanceApiException {
        JsonArray tickers = binanceApi.allBookTickers();
        assertTrue("There should be some tickers", tickers.size() > 0);
    }

    @Test
    public void testAllBookTickersMapping() throws Exception, BinanceApiException {
        Map<String, BinanceTicker> mapTickers = binanceApi.allBookTickersMap();
        assertTrue("Tickers should be mapped", mapTickers.size() > 0);
        assertTrue("There should be ticker for symbol", mapTickers.containsKey(symbol.toString()));

        String s = symbol.toString();
        BinanceTicker binanceTicker = mapTickers.get(s);
        assertTrue(s + " ticker should have symbol", binanceTicker.getSymbol().equals(s));
        assertNotNull(s + " ticker should have bidQty", binanceTicker.getBidPrice());
        assertNotNull(s + " ticker should have askQty", binanceTicker.getAskPrice());
        assertNotNull(s + " ticker should have bidPrice", binanceTicker.getBidPrice());
        assertNotNull(s + " ticker should have askPrice", binanceTicker.getAskPrice());
    }
}

