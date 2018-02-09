package com.webcerebrium.binance.api;


import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class SystemStatusTest {
    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testSystemStatus() throws Exception, BinanceApiException {
        JsonObject status = binanceApi.getSystemStatus();
        log.info("Status {}", status.toString() );
    }
}
