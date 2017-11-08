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
   "e": "outboundAccountInfo",		// event type
   "E": 1499405658849,				// event time
   "m": 0,
   "t": 0,
   "b": 0,
   "s": 0,
   "T": true,
   "W": true,
   "D": true,
   "B": [  							// balances
       {
           "a": "LTC",				// asset
           "f": "17366.18538083",	// available balance
           "l": "0.00000000"		// locked by open orders
       },
       {
           "a": "BTC",
           "f": "10537.85314051",
           "l": "2.19464093"
       },
       {
           "a": "ETH",
           "f": "17902.35190619",
           "l": "0.00000000"
       },
       {
           "a": "BNC",
           "f": "1114503.29769312",
           "l": "0.00000000"
       },
       {
           "a": "NEO",
           "f": "0.00000000",
           "l": "0.00000000"
       }
   ]
}
 */

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class BinanceEventOutboundAccountInfo {
    public Long eventTime;
    public Long makerCommission;
    public Long takerCommission;
    public Long buyerCommission;
    public Long sellerCommission;
    public boolean canTrade;
    public boolean canWithdraw;
    public boolean canDeposit;
    public List<BinanceWalletAsset> balances;

    public BinanceEventOutboundAccountInfo(JsonObject event) {
        eventTime = event.get("E").getAsLong();

        makerCommission = event.get("m").getAsLong();
        takerCommission = event.get("t").getAsLong();
        buyerCommission = event.get("b").getAsLong();
        sellerCommission = event.get("s").getAsLong();
        canTrade = event.get("T").getAsBoolean();
        canWithdraw = event.get("W").getAsBoolean();
        canDeposit = event.get("D").getAsBoolean();

        balances = new LinkedList<>();
        JsonArray b = event.get("B").getAsJsonArray();
        for (JsonElement asset : b) {
            balances.add(new BinanceWalletAsset(asset.getAsJsonObject()));
        }
    }
}
