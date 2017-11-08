package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

 /*
 {
     "symbol": "LTCBTC",
     "orderId": 1,
     "clientOrderId": "myOrder1",
     "price": "0.1",
     "origQty": "1.0",
     "executedQty": "0.0",
     "status": "NEW",
     "timeInForce": "GTC",
     "type": "LIMIT",
     "side": "BUY",
     "stopPrice": "0.0",
     "icebergQty": "0.0",
     "time": 1499827319559
 }
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BinanceOrder {
    public String symbol;
    public Long orderId;
    public String clientOrderId;
    public BigDecimal price;
    public BigDecimal origQty;
    public BigDecimal executedQty;
    public BinanceOrderStatus status;
    public BinanceTimeInForce timeInForce;
    public BinanceOrderType type;
    public BinanceOrderSide side;
    public BigDecimal stopPrice;
    public BigDecimal icebergQty;
    public Long time;
}
