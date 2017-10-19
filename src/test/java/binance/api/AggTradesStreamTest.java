package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class AggTradesStreamTest {

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testTradesStreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.websocketTrades(symbol, new BinanceWebSocketAdapterAggTrades() {
            @Override
            public void onMessage(BinanceEventAggTrade message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(5000);
        session.close();
    }
}
