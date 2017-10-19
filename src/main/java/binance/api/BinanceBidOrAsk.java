package binance.api;
/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonArray;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceBidOrAsk {

    public BigDecimal price = null;
    public BigDecimal quantity = null;

    public BinanceBidOrAsk() {}

    public BinanceBidOrAsk(JsonArray arr) {
        price = arr.get(0).getAsBigDecimal();
        quantity = arr.get(1).getAsBigDecimal();
    }
}
