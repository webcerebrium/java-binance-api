package com.webcerebrium.binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// This class contains READ-only tests for account

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.datatype.BinanceOrder;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import com.webcerebrium.binance.datatype.BinanceTrade;
import com.webcerebrium.binance.datatype.BinanceWalletAsset;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@Slf4j
public class AccountInfoTest {

    private BinanceApi binanceApi = null;
    private BinanceSymbol symbol = null;

    @Before
    public void setUp() throws Exception, BinanceApiException {
        binanceApi = new BinanceApi();
        symbol = BinanceSymbol.valueOf("BNBBTC");
    }

    @Test
    public void testAccountInformation() throws Exception, BinanceApiException {
        JsonObject account = binanceApi.account();
        assertTrue("account info should contain makerCommission", account.has("makerCommission"));
        assertTrue("account info should contain takerCommission", account.has("takerCommission"));
        assertTrue("account info should contain buyerCommission", account.has("buyerCommission"));
        assertTrue("account info should contain sellerCommission", account.has("sellerCommission"));
        assertTrue("account info should contain canTrade", account.has("canTrade"));
        assertTrue("account info should contain canWithdraw", account.has("canWithdraw"));
        assertTrue("account info should contain canDeposit", account.has("canDeposit"));
        assertTrue("account info should contain balances", account.has("balances"));
    }

    @Test
    public void testBalances() throws Exception, BinanceApiException {
        JsonArray balances = binanceApi.balances();
        assertTrue("Balances as JSON array should not be empty", balances.size() > 0);
    }

    @Test
    public void testBalancesMap() throws Exception, BinanceApiException {
        Map<String, BinanceWalletAsset> mapWallets = binanceApi.balancesMap();
        assertTrue("BinanceWalletAsset map should not be empty", mapWallets.size() > 0);
        log.info("Wallets={}", mapWallets.toString());

        BinanceWalletAsset ethWallet = mapWallets.get("ETH");
        log.info("ETH Wallet={}", ethWallet.toString());
    }

    @Test
    public void testOpenOrders() throws Exception, BinanceApiException {
        List<BinanceOrder> openOrders = binanceApi.openOrders(symbol);
        if (openOrders.size() > 0) {
            BinanceOrder lastOrder = openOrders.get(0);
            log.info("last Open Order={}", lastOrder.toString());
        }
    }

    @Test
    public void testAllOrders() throws Exception, BinanceApiException {
        List<BinanceOrder> allOrders = binanceApi.allOrders(symbol);
        if (allOrders.size() > 0) {
            BinanceOrder lastOrder = allOrders.get(0);
            log.info("lastOrder={}", lastOrder.toString());
        }
    }

    @Test
    public void testMyTrades() throws Exception, BinanceApiException {
        List<BinanceTrade> binanceTrades = binanceApi.myTrades(symbol);
        if (binanceTrades.size() > 0) {
            BinanceTrade lastTrade = binanceTrades.get(0);
            log.info("lastTrade={}", lastTrade.toString());
            assertTrue("My Trades list should not be empty", binanceTrades.size() > 0);
        }
    }}
