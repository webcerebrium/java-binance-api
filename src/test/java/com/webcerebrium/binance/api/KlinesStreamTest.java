package com.webcerebrium.binance.api;

import com.webcerebrium.binance.datatype.BinanceEventKline;
import com.webcerebrium.binance.datatype.BinanceInterval;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterKline;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class KlinesStreamTest {

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("ETHBTC");
    }

    @Test
    public void testKlinesStreamWatcher() throws Exception, BinanceApiException {
        Session session = binanceApi.websocketKlines(symbol, BinanceInterval.ONE_MIN, new BinanceWebSocketAdapterKline() {
            @Override
            public void onMessage(BinanceEventKline message) {
                log.info(message.toString());
            }
        });
        Thread.sleep(3000);
        session.close();
    }

}
