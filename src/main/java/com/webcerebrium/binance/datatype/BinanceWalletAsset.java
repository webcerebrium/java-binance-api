package com.webcerebrium.binance.datatype;

import com.google.gson.JsonObject;
import lombok.Data;

import java.math.BigDecimal;


// Structure from account balance

@Data
public class BinanceWalletAsset {
    public String asset;
    public BigDecimal free;
    public BigDecimal locked;

    public BinanceWalletAsset() {
    }

    public BinanceWalletAsset(JsonObject obj) {
        if (obj.has("a")) {
            this.asset = obj.get("a").getAsString();
        } else {
            this.asset = obj.get("asset").getAsString();
        }
        if (obj.has("f")) {
            this.free = obj.get("f").getAsBigDecimal();
        } else {
            this.free = obj.get("free").getAsBigDecimal();
        }
        if (obj.has("l")) {
            this.locked = obj.get("l").getAsBigDecimal();
        } else {
            this.locked = obj.get("locked").getAsBigDecimal();
        }
    }
}
