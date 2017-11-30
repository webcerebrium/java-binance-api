package com.webcerebrium.binance.datatype;

import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class BinanceExchangeProduct {
    boolean active;
    BigDecimal activeBuy;
    BigDecimal activeSell;
    String baseAsset;
    String baseAssetUnit;
    BigDecimal close;
    Long decimalPlaces;
    BigDecimal high;
    Long lastAggTradeId;
    BigDecimal low;
    String matchingUnitType;
    BigDecimal minQty;
    BigDecimal minTrade;
    BigDecimal open;
    BigDecimal prevClose;
    String quoteAsset;
    String quoteAssetUnit;
    String status;
    BinanceSymbol symbol;
    BigDecimal tickSize;
    BigDecimal tradedMoney;
    BigDecimal volume;
    BigDecimal withdrawFee;

    public BinanceExchangeProduct() {
    }

    private void jsonExpect(JsonObject obj, Set<String> fields) throws BinanceApiException {
        Set<String> missing = new HashSet<>();
        for (String f: fields) { if (!obj.has(f) || obj.get(f).isJsonNull()) missing.add(f); }
        if (missing.size() > 0) {
            log.warn("Missing fields {} in {}", missing.toString(), obj.toString());
            throw new BinanceApiException("Missing fields " + missing.toString());
        }
    }

    private BigDecimal safeDecimal(JsonObject obj, String field) {
        if (obj.has(field) && obj.get(field).isJsonPrimitive() && obj.get(field) != null) {
            try {
                return obj.get(field).getAsBigDecimal();
            } catch (java.lang.NumberFormatException nfe) {
                log.info("Number format exception in field={} value={} trade={}", field, obj.get(field), obj.toString());
            }
        }
        return null;
    }


    public BinanceExchangeProduct(JsonObject obj) throws BinanceApiException {

        symbol = BinanceSymbol.valueOf(obj.get("symbol").getAsString());
        active = obj.get("active").getAsBoolean();

        quoteAsset = obj.get("quoteAsset").getAsString();
        quoteAssetUnit = obj.get("quoteAssetUnit").getAsString();
        status = obj.get("status").getAsString();
        baseAsset = obj.get("baseAsset").getAsString();
        baseAssetUnit = obj.get("baseAssetUnit").getAsString();
        matchingUnitType = obj.get("matchingUnitType").getAsString();

        decimalPlaces  = obj.get("decimalPlaces").getAsLong();
        lastAggTradeId  = obj.get("lastAggTradeId").getAsLong();

        activeBuy = safeDecimal(obj, "activeBuy");
        activeSell = safeDecimal(obj, "activeSell");
        close = safeDecimal(obj, "close");
        high = safeDecimal(obj, "high");
        low = safeDecimal(obj, "low");
        minQty = safeDecimal(obj, "minQty");
        minTrade = safeDecimal(obj, "minTrade");
        open = safeDecimal(obj, "open");
        prevClose = safeDecimal(obj, "prevClose");

        tickSize = safeDecimal(obj, "tickSize");
        tradedMoney = safeDecimal(obj, "tradedMoney");
        volume = safeDecimal(obj, "volume");
        withdrawFee = safeDecimal(obj, "withdrawFee");
    }
}
