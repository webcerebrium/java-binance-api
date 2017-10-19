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
import binance.api.BinanceOrderPlacement;
import binance.api.BinanceOrderSide;
import binance.api.BinanceSymbol;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public class App {

    public static void main(String[] args) {

        try {
            System.out.println("This is just an API wrapper. Please see usage cases in unit tests or README.md" );

            // BinanceApi binanceApi = new BinanceApi();
            // System.out.println("ETH-BTC PRICE=" + binanceApi.pricesMap().get("ETHBTC") );


            BinanceSymbol symbol = BinanceSymbol.valueOf("ETHBTC");
            BinanceOrderPlacement placement = new BinanceOrderPlacement(symbol, BinanceOrderSide.BUY);
            placement.setPrice(BigDecimal.valueOf(0.00001));
            placement.setQuantity(BigDecimal.valueOf(10000)); // buy 10000 ether for 0.00001 BTC
            System.out.println(new BinanceApi().testOrder(placement));


        } catch (BinanceApiException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
