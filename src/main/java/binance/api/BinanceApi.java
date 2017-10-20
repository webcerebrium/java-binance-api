package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public class BinanceApi {

    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    /**
     * API Base URL
     */
    public String baseUrl = "https://www.binance.com/api/";
    /**
     * Base URL for websockets
     */
    public String websocketBaseUrl = "wss://stream.binance.com:9443/ws/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();

    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey
     * @param secretKey
     * @throws BinanceApiException
     */
    public BinanceApi(String apiKey, String secretKey) throws BinanceApiException {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws BinanceApiException
     */
    public BinanceApi() throws BinanceApiException {
        BinanceConfig config = new BinanceConfig();
        this.apiKey = config.getVariable("BINANCE_API_KEY");
        this.secretKey = config.getVariable("BINANCE_SECRET_KEY");
        validateCredentials();
    }

    /**
     * Validation we have API keys set up
     * @throws BinanceApiException
     */
    protected void validateCredentials() throws BinanceApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new BinanceApiException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new BinanceApiException("Missing BINANCE_SECRET_KEY. " + humanMessage);
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // GENERAL ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Checking connectivity,
     * @return empty object
     */
    public JsonObject ping() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ping"))
                .read().asJsonObject();
    }

    /**
     * Checking server time,
     * @return JsonObject, expected { serverTime: 00000 }
     * @throws BinanceApiException
     */
    public JsonObject time() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/time"))
                .read().asJsonObject();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // MARKET ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * 24hr ticker price change statistics
     * @param symbol
     * @return
     * @throws BinanceApiException
     */
    public JsonObject depth(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/depth?symbol=" + symbol.get()))
                .read().asJsonObject();
    }

    /**
     * 24hr ticker price change statistics, with limit explicitly set
     * @param symbol
     * @param limit
     * @return
     * @throws BinanceApiException
     */
    public JsonObject depth(BinanceSymbol symbol, int limit) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/depth?symbol=" + symbol.get() + "&limit=" + limit))
                .read().asJsonObject();
    }

    /**
     * Get compressed, aggregate trades and map result into BinanceAggregatedTrades type for better readability
     * Trades that fill at the time, from the same order, with the same price will have the quantity aggregated.
     * Allowed options - fromId, startTime, endTime.
     * If both startTime and endTime are sent, limit should not be sent AND the distance between startTime and endTime must be less than 24 hours.
     * If fromId, startTime, and endTime are not sent, the most recent aggregate trades will be returned.
     * @param symbol
     * @param limit
     * @param options
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol, int limit, Map<String, Long> options) throws BinanceApiException {
        String u = baseUrl + "v1/aggTrades?symbol=" + symbol.get() + "&limit=" + limit;
        if (options != null) {
            for (String optionKey : options.keySet()) {
                if (!optionKey.equals("fromId") ||
                        !optionKey.equals("startTime") ||
                        !optionKey.equals("endTime")) {
                    throw new BinanceApiException("Invalid aggTrades option, only fromId, startTime, endTime are allowed");
                }
                u += "&" + optionKey + "=" + options.get(optionKey);
            }
        }
        String lastResponse = (new BinanceRequest(u)).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceAggregatedTrades>>() {
        }.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of aggTrades with less parameters
     * @param symbol
     * @param options
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol, Map<String, Long> options) throws BinanceApiException {
        return this.aggTrades(symbol, 500, options);
    }

    /**
     * short version of aggTrades with less parameters
     * @param symbol
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol) throws BinanceApiException {
        return this.aggTrades(symbol, 500, null);
    }

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     * if startTime and endTime are not sent, the most recent klines are returned.
     * @param symbol
     * @param interval
     * @param limit
     * @param options
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceCandlestick> klines(BinanceSymbol symbol, BinanceInterval interval, int limit, Map<String, Long> options) throws BinanceApiException {
        String u = baseUrl + "v1/klines?symbol=" + symbol.get() + "&interval=" + interval.toString() + "&limit=" + limit;
        if (options != null) {
            for (String optionKey : options.keySet()) {
                if (!optionKey.equals("startTime") || !optionKey.equals("endTime")) {
                    throw new BinanceApiException("Invalid klines option, only startTime, endTime are allowed");
                }
                u += "&" + optionKey + "=" + options.get(optionKey);
            }
        }
        JsonArray jsonElements = (new BinanceRequest(u)).read().asJsonArray();
        List<BinanceCandlestick> list = new LinkedList<>();
        for (JsonElement e : jsonElements) list.add(new BinanceCandlestick(e.getAsJsonArray()));
        return list;
    }

    /**
     * short version of klines() with less parameters
     * @param symbol
     * @param interval
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceCandlestick> klines(BinanceSymbol symbol, BinanceInterval interval) throws BinanceApiException {
        return klines(symbol, interval, 500, null);
    }

    /**
     * 24hr ticker price change statistics
     * @param symbol
     * @return JsonObject
     * @throws BinanceApiException
     */
    public JsonObject ticker24hr(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/24hr?symbol=" + symbol.get()))
                .read().asJsonObject();
    }

    /**
     * Latest price for all symbols
     *
     * @return raw JSON Array
     * @throws BinanceApiException
     */
    public JsonArray allPrices() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allPrices"))
                .read().asJsonArray();
    }

    /**
     * Latest price for all symbols -
     *
     * @return Map of big decimals
     * @throws BinanceApiException
     */
    public Map<String, BigDecimal> pricesMap() throws BinanceApiException {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (JsonElement elem : allPrices()) {
            JsonObject obj = elem.getAsJsonObject();
            map.put(obj.get("symbol").getAsString(), obj.get("price").getAsBigDecimal());
        }
        return map;
    }

    /**
     * Get best price/qty on the order book for all symbols.
     *
     * @return JsonArray
     * @throws BinanceApiException
     */
    public JsonArray allBookTickers() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allBookTickers"))
                .read().asJsonArray();
    }

    /**
     * Get best price/qty on the order book for all symbols.
     *
     * @return map of BinanceTicker
     * @throws BinanceApiException
     */
    public Map<String, BinanceTicker> allBookTickersMap() throws BinanceApiException {
        String lastResponse = (new BinanceRequest(baseUrl + "v1/ticker/allBookTickers")).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceTicker>>() {
        }.getType();

        Map<String, BinanceTicker> mapTickers = new HashMap<>();
        List<BinanceTicker> ticker = new Gson().fromJson(lastResponse, listType);
        for (BinanceTicker t : ticker) mapTickers.put(t.getSymbol(), t);
        return mapTickers;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // ACCOUNT READ-ONLY ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Getting account information
     * @return JsonObject
     * @throws BinanceApiException
     */
    public JsonObject account() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v3/account"))
                .sign(apiKey, secretKey, null).read().asJsonObject();
    }

    /**
     * Getting balances - part of account information
     * @return JsonArray
     * @throws BinanceApiException
     */
    public JsonArray balances() throws BinanceApiException {
        return account().get("balances").getAsJsonArray();
    }

    /**
     * Getting balances - part of account information
     * @return map of wallet assets structure
     * @throws BinanceApiException
     */
    public Map<String, BinanceWalletAsset> balancesMap() throws BinanceApiException {
        Map<String, BinanceWalletAsset> mapAssets = new HashMap<>();
        for (JsonElement el : balances()) {
            BinanceWalletAsset w = new BinanceWalletAsset(el.getAsJsonObject());
            mapAssets.put(w.getAsset(), w);
        }
        return mapAssets;
    }

    /**
     * Get all open orders on a symbol.
     * @param symbol
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceOrder> openOrders(BinanceSymbol symbol) throws BinanceApiException {
        String u = baseUrl + "v3/openOrders?symbol=" + symbol.toString();
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceOrder>>() {
        }.getType();
        return new Gson().fromJson(lastResponse, listType);
    }
    /**
     * Get all orders on a symbol; active, canceled, or filled.
     * If orderId is set (not null and > 0), it will get orders >= that orderId.
     * Otherwise most recent orders are returned.
     * @param symbol
     * @param orderId
     * @param limit
     * @return
     * @throws BinanceApiException
     */
    public List<BinanceOrder> allOrders(BinanceSymbol symbol, Long orderId, int limit) throws BinanceApiException {
        String u = baseUrl + "v3/allOrders?symbol=" + symbol.toString() + "&limit=" + limit;
        if (orderId != null && orderId > 0) u += "&orderId=" + orderId;

        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceOrder>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of allOrders
     * @param symbol
     * @return list of orders
     * @throws BinanceApiException
     */
    public List<BinanceOrder> allOrders(BinanceSymbol symbol) throws BinanceApiException {
        return allOrders(symbol, 0L, 500);
    }
    /**
     * Get trades for a specific account and symbol.
     * @param symbol
     * @param orderId
     * @param limit
     * @return list of trades
     * @throws BinanceApiException
     */
    public List<BinanceTrade> myTrades(BinanceSymbol symbol, Long orderId, int limit) throws BinanceApiException {
        String u = baseUrl + "v3/myTrades?symbol=" + symbol.toString() + "&limit=" + limit;
        if (orderId != null && orderId > 0) u += "&orderId=" + orderId;
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        Type listType = new TypeToken<List<BinanceTrade>>() {}.getType();
        return new Gson().fromJson(lastResponse, listType);
    }

    /**
     * short version of myTrades(symbol, orderId, limit)
     * @param symbol
     * @throws BinanceApiException
     */
    public List<BinanceTrade> myTrades(BinanceSymbol symbol) throws BinanceApiException {
        return myTrades(symbol, 0L, 500);
    }

    /**
     * Get Order Status
     * @param symbol
     * @param orderId
     * @return BinanceOrder object if successfull
     * @throws BinanceApiException
     */
    public BinanceOrder getOrderById(BinanceSymbol symbol, Long orderId ) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&orderId=" + orderId;
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        return (new Gson()).fromJson(lastResponse, BinanceOrder.class);
    }

    /**
     * @param symbol
     * @param origClientOrderId
     * @return BinanceOrder object if successfull
     * @throws BinanceApiException
     */
    public BinanceOrder getOrderByOrigClientId(BinanceSymbol symbol, String origClientOrderId)  throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&origClientOrderId=" + esc.escape(origClientOrderId);
        String lastResponse = (new BinanceRequest(u)).sign(apiKey, secretKey, null).read().getLastResponse();
        return (new Gson()).fromJson(lastResponse, BinanceOrder.class);
    }

    /**
     * Getting order from order object. A wrapper for getOrderById(symbol, orderId)
     * @param order
     * @return
     * @throws BinanceApiException
     */
    public BinanceOrder getOrder(BinanceOrder order)  throws BinanceApiException {
        return getOrderById(BinanceSymbol.valueOf(order.getSymbol()), order.getOrderId() );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // TRADING ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * @param orderPlacement
     * @return
     * @throws BinanceApiException
     */
    public JsonObject createOrder(BinanceOrderPlacement orderPlacement)  throws BinanceApiException {
        String u = baseUrl + "v3/order?" + orderPlacement.getAsQuery();
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).post().read().asJsonObject();
    }

    /**
     * @param orderPlacement
     * @return
     * @throws BinanceApiException
     */
    public JsonObject testOrder(BinanceOrderPlacement orderPlacement)  throws BinanceApiException {
        String u = baseUrl + "v3/order/test?" + orderPlacement.getAsQuery();
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).post().read().asJsonObject();
    }


    /**
     * Deletes order by order ID
     * @param symbol
     * @param orderId
     * @return
     * @throws BinanceApiException
     */
    public JsonObject deleteOrderById(BinanceSymbol symbol, Long orderId) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&orderId=" + orderId;
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }
    /**
     * Deletes order by original client ID
     * @param symbol
     * @param origClientOrderId
     * @return
     * @throws BinanceApiException
     */
    public JsonObject deleteOrderByOrigClientId(BinanceSymbol symbol, String origClientOrderId) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&origClientOrderId=" + esc.escape(origClientOrderId);
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }

    /**
     * Deletes order by new client ID
     * @param symbol
     * @param newClientOrderId
     * @return
     * @throws BinanceApiException
     */
    public JsonObject deleteOrderByNewClientId(BinanceSymbol symbol, String newClientOrderId ) throws BinanceApiException {
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&newClientOrderId=" + esc.escape(newClientOrderId);
        return (new BinanceRequest(u)).sign(apiKey, secretKey, null).delete().read().asJsonObject();
    }

    /**`
     * Deletes order by BinanceOrder object
     * @param order
     * @return
     * @throws BinanceApiException
     */
    public JsonObject deleteOrder(BinanceOrder order) throws BinanceApiException {
        BinanceSymbol symbol = BinanceSymbol.valueOf(order.getSymbol());
        if (!Strings.isNullOrEmpty(order.getClientOrderId())) {
            return deleteOrderByOrigClientId(symbol, order.getClientOrderId());
        }
        return deleteOrderById(symbol, order.getOrderId());
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // USER DATA STREAM
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Start user data stream, get a key for websocket
     * @return
     * @throws BinanceApiException
     */
    public String startUserDataStream() throws BinanceApiException {
        JsonObject jsonObject = (new BinanceRequest(baseUrl + "v1/userDataStream"))
                .sign(apiKey).post().read().asJsonObject();
        return jsonObject.get("listenKey").getAsString();
    }

    /**
     * Keep user data stream alive
     * @param listenKey
     * @return
     * @throws BinanceApiException
     */
    public JsonObject keepUserDataStream(String listenKey) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/userDataStream?listenKey=" + esc.escape(listenKey)))
                .sign(apiKey).put().read().asJsonObject();
    }

    /**
     * Close user data stream
     * @param listenKey
     * @return
     * @throws BinanceApiException
     */
    public JsonObject deleteUserDataStream(String listenKey) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/userDataStream?listenKey=" + esc.escape(listenKey)))
                .sign(apiKey).delete().read().asJsonObject();
    }

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // WEBSOCKET ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Base method for all websockets streams
     * @param url
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session getWebsocketSession(String url, WebSocketAdapter adapter) throws BinanceApiException {
        try {
            URI uri = new URI(websocketBaseUrl + url);
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setTrustAll(true); // The magic
            WebSocketClient client = new WebSocketClient(sslContextFactory);
            client.start();
            return client.connect(adapter, uri).get();
        } catch (URISyntaxException e) {
            throw new BinanceApiException("URL Syntax error: " + e.getMessage());
        } catch (Throwable e) {
            throw new BinanceApiException("Websocket error: " + e.getMessage());
        }
    }

    /**
     * Depth Websocket Stream Listener
     * @param symbol
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketDepth(BinanceSymbol symbol, BinanceWebSocketAdapterDepth adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@depth", adapter);
    }

    /**
     * Depth Websocket Stream Listener - best 20 levels
     * @param symbol
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketDepth20(BinanceSymbol symbol, BinanceWebSocketAdapterDepth adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@depth20", adapter);
    }

    /**
     * Depth Websocket Stream Listener - best 10 levels
     * @param symbol
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketDepth10(BinanceSymbol symbol, BinanceWebSocketAdapterDepth adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@depth10", adapter);
    }

    /**
     * Depth Websocket Stream Listener - best 5 lavels
     * @param symbol
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketDepth5(BinanceSymbol symbol, BinanceWebSocketAdapterDepth adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@depth5", adapter);
    }



    /**
     * Klines Websocket Stream Listener
     * @param symbol
     * @param interval
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketKlines(BinanceSymbol symbol, BinanceInterval interval, BinanceWebSocketAdapterKline adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@kline_" + interval.toString(), adapter);
    }

    /**
     * Trades Websocket Stream Listener
     * @param symbol
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocketTrades(BinanceSymbol symbol, BinanceWebSocketAdapterAggTrades adapter) throws BinanceApiException {
        return getWebsocketSession(symbol.toString().toLowerCase() + "@aggTrade", adapter);
    }

    /**
     * User Data Websocket Stream Listener
     * @param listenKey
     * @param adapter
     * @return
     * @throws BinanceApiException
     */
    public Session websocket(String listenKey, BinanceWebSocketAdapterUserData adapter) throws BinanceApiException {
        return getWebsocketSession(listenKey, adapter);
    }

    /**
     * Withdrawal APIs
     * @param asset
     * @param address
     * @param amount
     * @param name label of destination address, can be left empty
     * @return
     * @throws BinanceApiException
     */
    public JsonObject withdraw(String asset, String address, long amount, String name) throws BinanceApiException {
        String u = baseUrl + "wapi/v1/withdraw.html?asset=" + esc.escape(asset) +
                "&address=" + esc.escape(address) +
                "&amount=" + amount;
        if (!Strings.isNullOrEmpty(name)) {
            u += "name=" + esc.escape(name);
        }
        return (new BinanceRequest(u))
                .sign(apiKey).post().read().asJsonObject();
    }
}


