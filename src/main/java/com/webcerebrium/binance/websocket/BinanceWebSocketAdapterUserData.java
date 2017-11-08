package com.webcerebrium.binance.websocket;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceEventExecutionReport;
import com.webcerebrium.binance.datatype.BinanceEventOutboundAccountInfo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

@Slf4j
public abstract class BinanceWebSocketAdapterUserData  extends WebSocketAdapter {
    @Override
    public void onWebSocketConnect(Session sess) {
        log.debug("onWebSocketConnect: {}", sess);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        log.error("onWebSocketError: {}", cause);
    }

    @Override
    public void onWebSocketText(String message) {
        log.debug("onWebSocketText message={}", message);
        JsonObject operation = (new Gson()).fromJson(message, JsonObject.class);

        try {
            String eventType = operation.get("e").getAsString();
            if (eventType.equals("outboundAccountInfo")) {
                onOutboundAccountInfo(new BinanceEventOutboundAccountInfo(operation));
            } else if (eventType.equals("executionReport")) {
                onExecutionReport(new BinanceEventExecutionReport(operation));
            } else {
                log.error("Error in websocket message - unknown event Type");
            }
        } catch (BinanceApiException e) {
            log.error("Error in websocket message {}", e.getMessage());
        }
    }

    public abstract void onOutboundAccountInfo(BinanceEventOutboundAccountInfo event) throws BinanceApiException;
    public abstract void onExecutionReport(BinanceEventExecutionReport event) throws BinanceApiException;
}
