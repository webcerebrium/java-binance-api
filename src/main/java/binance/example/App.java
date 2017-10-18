package binance.example;


/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata
 * Released under the MIT License
 * ============================================================ */

import binance.api.BinanceApi;
import binance.api.BinanceApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {
        try {
            BinanceApi binanceApi = new BinanceApi();
            // System.out.println("API KEY = " + binanceApi.getApiKey());
            // System.out.println("SECRET KEY = " + binanceApi.getSecretKey());

            System.out.println("BALANCES=" + binanceApi.balances() );
            System.out.println("ACCOUNT=" + binanceApi.account() );

//            System.out.println("TICKER24hr=" + binanceApi.ticker24hr("BQXBTC"));
//            System.out.println("PRICES LIST=" + binanceApi.prices() );
//            System.out.println("PRICES AS MAP=" + binanceApi.pricesMap() );
//            System.out.println("PING=" + binanceApi.ping() );
//            System.out.println("SERVER TIME=" + binanceApi.time().get("serverTime").getAsString() + ", LOCAL TIME=" + new Date().getTime());

        } catch (BinanceApiException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
