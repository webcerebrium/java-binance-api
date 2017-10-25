package com.webcerebrium.binance.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

@Slf4j
public class AccountHistoryTest {

    private BinanceApi binanceApi = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
    }

    @Test
    public void testAccountDepositHistory() throws Exception, BinanceApiException {
        BinanceHistoryFilter historyFilter = new BinanceHistoryFilter("ETH");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        historyFilter.setStartTime(cal.getTime());
        log.info("DEPOSIT={}", binanceApi.getDepositHistory(historyFilter));

    }

    @Test
    public void testAccountWithdrawalHistory() throws Exception, BinanceApiException {
        BinanceHistoryFilter historyFilter = new BinanceHistoryFilter();
        log.info("WITHDRAWALS={}", binanceApi.getWithdrawHistory(historyFilter));
    }
}
