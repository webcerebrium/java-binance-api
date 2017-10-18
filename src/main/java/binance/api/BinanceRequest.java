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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
public class BinanceRequest {

    public String userAgent = "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0";
    public HttpsURLConnection conn = null;
    public String requestUrl = "";
    public String method = "GET";
    public String lastResponse = "";

    public String apiKey = "";
    public String secretKey = "";

    public Map<String, String> headers = new HashMap<String, String>();

    // Internal JSON parser
    private JsonParser jsonParser = new JsonParser();
    private String requestBody = "";

    // Creating public request
    public BinanceRequest(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    // HMAC encoding
    public static String encode(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    }

    public BinanceRequest sign(String apiKey, String secretKey, Map<String, String> options) throws BinanceApiException {
        if (!Strings.isNullOrEmpty(secretKey)) {
            List<String> list = new LinkedList<>();
            if (options != null) {
                for (String key : options.keySet()) {
                    list.add(key + "=" + options.get(key));
                }
            }
            list.add("recvWindow=" + 6500);
            list.add("timestamp=" + String.valueOf(new Date().getTime()));
            String query = String.join("&", list);
            try {
                String signature = encode(secretKey, query); // set the HMAC hash header
                this.requestUrl += "?" + query + "&signature=" + signature;
            } catch (Exception e ) {
                throw new BinanceApiException("Encryption error " + e.getMessage());
            }
        }
        headers.put("X-MBX-APIKEY", apiKey);
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return this;
    }

    /**
     * Settings method as post, keeping interface fluid
     * @return
     */
    public BinanceRequest post() {
        this.setMethod("POST");
        return this;
    }

    /**
     * Settings method as PUT, keeping interface fluid
     * @return
     */
    public BinanceRequest put() {
        this.setMethod("PUT");
        return this;
    }


    /**
     * Settings method as DELETE, keeping interface fluid
     * @return
     */
    public BinanceRequest delete() {
        this.setMethod("DELETE");
        return this;
    }

    /*
      Opens HTTPS connection and save connection Handler
     */
    public BinanceRequest connect() throws BinanceApiException {

        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        URL url = null;
        try {
            url = new URL(requestUrl);
            log.info("Requesting {}", url);
        } catch (MalformedURLException e) {
            throw new BinanceApiException("Mailformed URL " + e.getMessage());
        }
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            throw new BinanceApiException("SSL Error " + e.getMessage() );
        } catch (KeyManagementException e) {
            throw new BinanceApiException("Key Management Error " + e.getMessage() );
        }

        try {
            conn = (HttpsURLConnection)url.openConnection();
        } catch (IOException e) {
            throw new BinanceApiException("HTTPS Connection error " + e.getMessage());
        }

        try {
            conn.setRequestMethod(method);
        } catch (ProtocolException e) {
            throw new BinanceApiException("HTTP method error " + e.getMessage());
        }
        conn.setRequestProperty("User-Agent", getUserAgent());
        for(String header: headers.keySet()) {
            conn.setRequestProperty(header, headers.get(header));
        }
        return this;
    }

    // Saving response into local string variable
    public BinanceRequest read() throws BinanceApiException {
        if (conn == null) {
            connect();
        }
        int code = 0;
        try {

            // posting payload it we do not have it yet
            if (!Strings.isNullOrEmpty(getRequestBody())) {
                log.info("Payload: {}", getRequestBody());
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(getRequestBody());
                writer.close();
            }

            BufferedReader br = new BufferedReader( new InputStreamReader(conn.getInputStream()));
            lastResponse = IOUtils.toString(br);
            // code = conn.getResponseCode();
            // lastResponse = conn.getResponseMessage();
            log.info("Response: {}", lastResponse);
        } catch (IOException e) {
            throw new BinanceApiException("Error in reading response " + e.getMessage() + " code: " + code);
        }
        return this;
    }

    public BinanceRequest payload(JsonObject payload) {
        if (payload == null) return this; // this is a valid case
        // according to documentation we need to have this header if we have preload
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        this.requestBody = payload.toString();
        return this;
    }

    /**
     * Getting last response as google GAON JsonObject
     */
    public JsonObject asJsonObject() {
        return (JsonObject)jsonParser.parse(getLastResponse());
    }
    /**
     * Getting last response as google GAON JsonArray
     */
    public JsonArray asJsonArray() {
        return (JsonArray)jsonParser.parse(getLastResponse());
    }

}
