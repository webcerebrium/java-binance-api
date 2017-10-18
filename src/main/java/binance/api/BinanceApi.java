package binance.api;

/* ============================================================
 * java-binance-api
 * https://github.com/webcerebrium/java-binance-api
 * ============================================================
 * Copyright 2017-, Viktor Lopata, Web Cerebrium OÜ
 * Released under the MIT License
 * ============================================================ */

import com.google.common.base.Strings;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class BinanceApi {

    // Actual API key and Secret Key that will be used
    public String apiKey;
    public String secretKey;

    // Base URLs
    public String baseUrl = "https://www.binance.com/api/";
    public String websocketBaseUrl = "wss://stream.binance.com:9443/ws/";

    // Constructor of API when you exactly know the keys
    public BinanceApi(String apiKey, String secretKey) throws BinanceApiException {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    // Constructor of API - keys are loaded from VM options, environment variables, resource files
    public BinanceApi() throws BinanceApiException {
        BinanceConfig config = new BinanceConfig();
        this.apiKey = config.getVariable("BINANCE_API_KEY");
        this.secretKey = config.getVariable("BINANCE_SECRET_KEY");
        validateCredentials();
    }

    // Validation we have API keys set up
    protected void validateCredentials() throws BinanceApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new BinanceApiException("Missing BINANCE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new BinanceApiException("Missing BINANCE_SECRET_KEY. " + humanMessage);
    }

    // Checking connectivity, expecting empty object in response
    public JsonObject ping() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ping"))
                .read().asJsonObject();
    }

    // Checking server time, expected { serverTime: 00000 } in response
    public JsonObject time() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/time"))
                .read().asJsonObject();
    }

    // Latest price for all symbols - as array
    public JsonArray prices() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v1/ticker/allPrices"))
                .read().asJsonArray();
    }
    // Latest price for all symbols - as map of big decimals
    public Map<String, BigDecimal> pricesMap() throws BinanceApiException {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        for (JsonElement elem : prices()) {
            JsonObject obj = elem.getAsJsonObject();
            map.put(obj.get("symbol").getAsString(), obj.get("price").getAsBigDecimal());
        }
        return map;
    }

    // 24hr ticker price change statistics
    public JsonObject ticker24hr(String symbol) throws BinanceApiException {
        String s = new BinanceSymbol(symbol).get();
        return (new BinanceRequest(baseUrl + "v1/ticker/24hr?symbol=" + s))
                .read().asJsonObject();
    }

    // Getting account information
    public JsonObject account() throws BinanceApiException {
        return (new BinanceRequest(baseUrl + "v3/account"))
                .sign(apiKey, secretKey, null).read().asJsonObject();
    }

    // Getting balances - part of account information
    public JsonArray balances() throws BinanceApiException {
        return account().get("balances").getAsJsonArray();
    }
}
