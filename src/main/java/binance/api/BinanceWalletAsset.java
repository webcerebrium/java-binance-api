package binance.api;

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
        this.asset = obj.get("asset").getAsString();
        this.free = obj.get("free").getAsBigDecimal();
        this.locked = obj.get("locked").getAsBigDecimal();
    }
}
