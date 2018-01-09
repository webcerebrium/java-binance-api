package com.webcerebrium.binance.datatype;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

// Example of JSON Data
// {"rateLimitType":"REQUESTS","interval":"MINUTE","limit":1200}


@Data
@Slf4j
public class BinanceRateLimit {

    String rateLimitType;
    String interval;
    Long limit;

    public BinanceRateLimit(JsonObject obj) {
        if (obj.has("rateLimitType") && obj.get("rateLimitType").isJsonPrimitive()) {
            rateLimitType = obj.get("rateLimitType").getAsString();
        }
        if (obj.has("interval") && obj.get("interval").isJsonPrimitive()) {
            interval = obj.get("interval").getAsString();
        }
        if (obj.has("limit") && obj.get("limit").isJsonPrimitive()) {
            limit = obj.get("limit").getAsLong();
        }
    }

}
