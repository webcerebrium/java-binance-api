package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 {
     "symbol":"ETHBTC",
     "status":"TRADING",
     "baseAsset":"ETH",
     "baseAssetPrecision":8,
     "quoteAsset":"BTC",
     "quotePrecision":8,
     "orderTypes":["LIMIT","LIMIT_MAKER","MARKET","STOP_LOSS_LIMIT","TAKE_PROFIT_LIMIT"],
     "icebergAllowed":true,
     "filters":
     [
        {"filterType":"PRICE_FILTER","minPrice":"0.00000100","maxPrice":"100000.00000000","tickSize":"0.00000100"},
        {"filterType":"LOT_SIZE","minQty":"0.00100000","maxQty":"100000.00000000","stepSize":"0.00100000"},
        {"filterType":"MIN_NOTIONAL","minNotional":"0.00100000"}
     ]
 }
 */
@Slf4j
@Data
public class BinanceExchangeSymbol {

    BinanceSymbol symbol;
    String status;
    String baseAsset;
    Long baseAssetPrecision;
    String quoteAsset;
    Long quotePrecision;
    List<BinanceOrderType> orderTypes = new LinkedList<>();
    boolean icebergAllowed;
    HashMap<String, JsonObject> filters = new HashMap<>();

    public BinanceExchangeSymbol(JsonObject obj) throws BinanceApiException {
        // log.debug("Reading Symbol {}, {}", obj.get("symbol").getAsString(), obj.toString());

        symbol = BinanceSymbol.valueOf(obj.get("symbol").getAsString());
        status = obj.get("status").getAsString();

        baseAsset = obj.get("baseAsset").getAsString();
        baseAssetPrecision = obj.get("baseAssetPrecision").getAsLong();
        quoteAsset = obj.get("quoteAsset").getAsString();
        quotePrecision = obj.get("quotePrecision").getAsLong();
        icebergAllowed = obj.get("icebergAllowed").getAsBoolean();

        if (obj.has("orderTypes") && obj.get("orderTypes").isJsonArray()) {
            JsonArray arrOrderTypes = obj.get("orderTypes").getAsJsonArray();
            orderTypes.clear();
            for (JsonElement entry: arrOrderTypes) {
                orderTypes.add(BinanceOrderType.valueOf(entry.getAsString()));
            }
        }

        if (obj.has("filters") && obj.get("filters").isJsonArray()) {
            JsonArray arrFilters = obj.get("filters").getAsJsonArray();
            filters.clear();
            for (JsonElement entry: arrFilters) {
                JsonObject item = entry.getAsJsonObject();
                String key = item.get("filterType").getAsString();
                filters.put(key, item);
            }
        }
    }

    public JsonObject getPriceFilter() {
        return filters.get("PRICE_FILTER");
    }

    public JsonObject getLotSize() {
        return filters.get("LOT_SIZE");
    }

    public JsonObject getMinNotional() {
        return filters.get("LOT_SIZE");
    }
}
