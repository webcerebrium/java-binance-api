package com.webcerebrium.binance.api;

import com.google.common.base.Strings;

public class BinanceSymbol {

    String symbol = "";

    public BinanceSymbol(String symbol)  throws BinanceApiException  {
        // sanitizing symbol, preventing from common user-input errors
        if (Strings.isNullOrEmpty(symbol)) {
            throw new BinanceApiException("Symbol cannot be empty. Example: BQXBTC");
        }
        if (symbol.contains(" ")) {
            throw new BinanceApiException("Symbol cannot contain spaces. Example: BQXBTC");
        }
        if (!symbol.endsWith("BTC") && !symbol.endsWith("ETH") && !symbol.endsWith("USDT")) {
            throw new BinanceApiException("Market Symbol should be ending with BTC, ETH oir USDT. Example: BQXBTC");
        }
        this.symbol = symbol.replace("_", "").replace("-", "").toUpperCase();
    }

    public String get(){ return this.symbol; }

    public String getSymbol(){ return this.symbol; }

    public String toString() { return this.get(); }

    public static BinanceSymbol valueOf(String s) throws BinanceApiException {
        return new BinanceSymbol(s);
    }

    public static BinanceSymbol BTC(String pair) throws BinanceApiException {
       return BinanceSymbol.valueOf(pair.toUpperCase() + "BTC");
    }

    public static BinanceSymbol ETH(String pair) throws BinanceApiException {
       return BinanceSymbol.valueOf(pair.toUpperCase() + "ETH");
    }

    public static BinanceSymbol USDT(String pair) throws BinanceApiException {
       return BinanceSymbol.valueOf(pair.toUpperCase() + "USDT");
    }

}
