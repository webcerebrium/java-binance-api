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
            System.out.println("ETH-BTC PRICE=" + binanceApi.pricesMap().get("ETHBTC") );
            System.out.println("Please see usage cases in unit tests" );

        } catch (BinanceApiException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
