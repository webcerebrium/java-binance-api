package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

// Internal variables are not human readable. So this class contains better readable getters

//  {
// "a": 26129,         // Aggregate tradeId
// "p": "0.01633102",  // Price
// "q": "4.70443515",  // Quantity
// "f": 27781,         // First tradeId
// "l": 27781,         // Last tradeId
// "T": 1498793709153, // Timestamp
// "m": true,          // Was the buyer the maker?
// "M": true           // Was the trade the best price match?
// }

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceAggregatedTrades {

    //
    public long a;
    public BigDecimal p;
    public BigDecimal q;
    public long f;
    public long l;
    public long T;
    public boolean m;
    public boolean M;

    public long getTradeId() { return a; }
    public BigDecimal getPrice() { return p; }
    public BigDecimal getQuantity() { return q; }
    public long getFirstTradeId() { return f; }
    public long getLastTradeId() { return l; }
    public long getTimestamp() { return T; }
    public boolean wasMaker() { return m; }
    public boolean wasBestPrice() { return M; }

    @Override
    public String toString() {
        return "BinanceAggregatedTrades{" +
            "tradeId=" + getTradeId() +
            ", price=" + getPrice() +
            ", quantity=" + getQuantity() +
            ", firstTradeId=" + getFirstTradeId() +
            ", lastTradeId=" + getLastTradeId() +
            ", timestamp=" + getTimestamp() +
            ", maker=" + wasMaker() +
            ", bestPrice=" + wasBestPrice() +
            '}';
    }
}
