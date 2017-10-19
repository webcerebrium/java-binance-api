package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.math.BigDecimal;
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

    /* Base URLs */
    public String baseUrl = "https://www.binance.com/api/";
    public String websocketBaseUrl = "wss://stream.binance.com:9443/ws/";

    /* Constructor of API when you exactly know the keys */
    public BinanceApi(String apiKey, String secretKey) throws BinanceApiException {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /*
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     */
    public BinanceApi() throws BinanceApiException {
        BinanceConfig config = new BinanceConfig();
        this.apiKey = config.getVariable("BINANCE_API_KEY");
        this.secretKey = config.getVariable("BINANCE_SECRET_KEY");
        validateCredentials();
    }

    /*
     * Validation we have API keys set up
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
     *
     * @return empty object
     */
    public JsonObject ping() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ping"))
                .read().asJsonObject();
    }

    /**
     * Checking server time,
     *
     * @return JsonObject, expected { serverTime: 00000 }
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
     */
    public JsonObject depth(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/depth?symbol=" + symbol.get()))
                .read().asJsonObject();
    }

    /**
     * 24hr ticker price change statistics, with limit explicitly set
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
     * short version with less parameters
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol, Map<String, Long> options) throws BinanceApiException {
        return this.aggTrades(symbol, 500, options);
    }

    /**
     * short version with less parameters
     */
    public List<BinanceAggregatedTrades> aggTrades(BinanceSymbol symbol) throws BinanceApiException {
        return this.aggTrades(symbol, 500, null);
    }

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     * If startTime and endTime are not sent, the most recent klines are returned.
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
     */
    public List<BinanceCandlestick> klines(BinanceSymbol symbol, BinanceInterval interval) throws BinanceApiException {
        return klines(symbol, interval, 500, null);
    }

    /**
     * 24hr ticker price change statistics
     *
     * @return JsonObject
     */
    public JsonObject ticker24hr(BinanceSymbol symbol) throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/24hr?symbol=" + symbol.get()))
                .read().asJsonObject();
    }

    /**
     * Latest price for all symbols
     *
     * @return raw JSON Array
     */
    public JsonArray allPrices() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allPrices"))
                .read().asJsonArray();
    }

    /**
     * Latest price for all symbols -
     *
     * @return Map of big decimals
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
        String u = baseUrl + "v3/order?symbol=" + symbol.toString() + "&origClientOrderId=" + origClientOrderId;
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
    // testOrder
    // newOrder
    // BinanceOrder getOrder
    // deleteOrder

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // USER DATA STREAM
    // - - - - - - - - - - - - - - - - - - - - - - - -
    // startStream
    // keepStream
    // deleteStream

    // - - - - - - - - - - - - - - - - - - - - - - - -
    // WEBCOCKET ENDPOINTS
    // - - - - - - - - - - - - - - - - - - - - - - - -
    // Depth Websocket Endpoint
    // Kline Websocket Endpoint
    // Trades Websocket Endpoint
    // User Data Websocket Endpoint

}


