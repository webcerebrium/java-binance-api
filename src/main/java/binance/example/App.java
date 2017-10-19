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
            System.out.println("This is just an API wrapper. Please see usage cases in unit tests or README.md" );

            BinanceApi api = new BinanceApi();
            // System.out.println("ETH-BTC PRICE=" + api.pricesMap().get("ETHBTC") );

            String listenKey = api.startUserDataStream();
            System.out.println("LISTEN KEY=" + listenKey);
            try { Thread.sleep(500); } catch (InterruptedException ie) {}
            System.out.println("KEEPING ALIVE=" + api.keepUserDataStream(listenKey));
            System.out.println("DELETED=" + api.deleteUserDataStream(listenKey));

        } catch (BinanceApiException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
