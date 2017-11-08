package com.webcerebrium.binance.datatype;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class BinanceExchangeStats {
    List<BinanceExchangeProduct> products = new LinkedList<>();

    public BinanceExchangeStats() {
    }

    public BinanceExchangeStats(JsonObject response) throws BinanceApiException {
        if (!response.has("data")) {
            throw new BinanceApiException("Missing restrictions in response object while trying to get restrictions");
        }
        JsonArray pairs = response.get("data").getAsJsonArray();
        products.clear();
        for (JsonElement entry: pairs) {
            BinanceExchangeProduct symbol = new BinanceExchangeProduct(entry.getAsJsonObject());
            products.add(symbol);
        }
    }

    public List<BinanceExchangeProduct> getMarketsOf(String coin) {
        List<BinanceExchangeProduct> result = new LinkedList<>();
        for (int i = 0; i < products.size(); i++ ) {
            BinanceExchangeProduct tradingSymbol = products.get(i);
            if (!tradingSymbol.isActive()) continue;
            if (tradingSymbol.getSymbol().contains(coin)) {
                result.add(tradingSymbol);
            }
        }
        return result;
    }

    public Set<BinanceSymbol> getSymbolsOf(String coin) throws BinanceApiException {
        List<BinanceExchangeProduct> coins = getMarketsOf(coin);
        Set<BinanceSymbol> result = new TreeSet<>();
        for (BinanceExchangeProduct sym: coins) {
            result.add(sym.getSymbol());
        }
        return result;
    }

    public Set<String> getCoinsOf(String coin) throws BinanceApiException {
        List<BinanceExchangeProduct> coins = getMarketsOf(coin);
        Set<String> result = new TreeSet<>();
        for (BinanceExchangeProduct sym: coins) {
            result.add(sym.getSymbol().getOpposite(coin));
        }
        return result;
    }
}
