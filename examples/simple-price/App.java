package binance.example;

/* ============================================================
 * java-binance-api example
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata
 * Released under the MIT License
 * ============================================================ */

import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args) {

       try {
            BinanceApi api = new BinanceApi();
            System.out.println("ETH-BTC PRICE=" + api.pricesMap().get("ETHBTC") );


        } catch (BinanceApiException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
