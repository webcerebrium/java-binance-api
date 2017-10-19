package binance.api;

import lombok.extern.slf4j.Slf4j;
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
        try { Thread.sleep(50); } catch (InterruptedException ie) {}
        log.info("KEEPING ALIVE=" + binanceApi.keepUserDataStream(listenKey));
        log.info("DELETED=" + binanceApi.deleteUserDataStream(listenKey));
    }
}

