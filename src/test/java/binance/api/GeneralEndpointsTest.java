package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class GeneralEndpointsTest {

    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testPingReturnsEmptyObject() throws Exception, BinanceApiException {
        assertEquals("Ping should respond with empty object", binanceApi.ping().entrySet().size(), 0);
    }

    @Test
    public void testServerTimeIsAlmostSameAsLocal() throws Exception, BinanceApiException {
        JsonObject time = binanceApi.time();
        assertTrue("serverTime should be received", time.has("serverTime"));
        long serverTime = time.get("serverTime").getAsLong();
        long localTime = (new Date()).getTime();
        assertTrue("serverTime should not differ much from local", Math.abs(serverTime - localTime) < 5000);
    }
}
