package com.webcerebrium.binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.datatype.BinanceEventExecutionReport;
import com.webcerebrium.binance.datatype.BinanceEventOutboundAccountInfo;
import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterUserData;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class UserDataStreamTest {

    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testUserDataStreamIsCreatedAndClosed() throws Exception, BinanceApiException {
        String listenKey = binanceApi.startUserDataStream();
        log.info("LISTEN KEY=" + listenKey);
        Session session = binanceApi.websocket(listenKey, new BinanceWebSocketAdapterUserData() {
            @Override
            public void onOutboundAccountInfo(BinanceEventOutboundAccountInfo event) throws BinanceApiException {
                log.info(event.toString());
            }
            @Override
            public void onExecutionReport(BinanceEventExecutionReport event) throws BinanceApiException {
                log.info(event.toString());
            }
        });
        Thread.sleep(2000);
        log.info("KEEPING ALIVE=" + binanceApi.keepUserDataStream(listenKey));
        Thread.sleep(2000);
        session.close();
        log.info("DELETED=" + binanceApi.deleteUserDataStream(listenKey));
    }
}

