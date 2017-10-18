package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// This class contains tests for trading. Take it wisely

import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class TradingTest {
    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;
    private BinanceOrder order = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("BNBBTC");
        order = null;
    }

    @After
    public void tearDown() throws Exception {
        if (order != null) {
            // binanceApi.cancelOrder(BinanceOrder);
        }
    }

    @Test
    public void testOrderWithoutPlacing() throws Exception {
    }

    @Test
    public void testPlacingCheckingAndCancelingLimitOrder() throws Exception {
    }

    @Test
    public void testPlacingCheckingAndCancelingMarketOrder() throws Exception {
    }


}
