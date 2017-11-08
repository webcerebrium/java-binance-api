package com.webcerebrium.binance.websocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceEventKline;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;


@Slf4j
public abstract class BinanceWebSocketAdapterKline extends WebSocketAdapter {
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
            onMessage(new BinanceEventKline(operation));
        } catch (BinanceApiException e) {
            log.error("Error in websocket message {}", e.getMessage());
        }
    }

    public abstract void onMessage(BinanceEventKline event) throws BinanceApiException;

}
