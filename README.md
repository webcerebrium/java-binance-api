# Java Binance API Client

This project is designed to help you make your own projects that interact with the [Binance API](https://www.binance.com/restapipub.html). 
This project seeks to have complete API coverage including User Data Streams and WebSockets.

## Getting Started

For compiling and launching the project from source code, you need
- `Java 1.8`
- [`gradle`](https://gradle.org/releases/) dependency manager to be installed
- File `src/main/resources/application.properties` can be set with Binance API keys:

```
BINANCE_API_KEY=<key>
BINANCE_API_SECRET=<secret>
```
As alternative, you can set an environment variables or java Virtual Machine properties `BINANCE_API_KEY`, `BINANCE_API_SECRET`
and they will be accepted in higher priority comparing to resource file.

#### Running tests

Tests with API coverage could be launched with `gradle test`. Before running, make sure API keys are set as described above.

#### Running from command line

Compile the project with `gradle distZip`. Under `build\distributions\java-binance-api-X.X.X\bin` you will have have batch file with command line client.

#### Running in Eclipse
This project uses Lombok. You will need to [install lombok per guidelines](https://projectlombok.org/download.html) to make the project compile in Eclipse. It will compile via gradle just fine without this additional installation step.

#### Running in Intellij IDEA
This project uses Lombok. You will need to [install corresponding plugin](https://plugins.jetbrains.com/plugin/6317) and enable `Annotation processing` in your `Compiler` settings. It will compile via gradle just fine without this additional installation step.
To start, import current directory as Gradle project.

### Logging Configuration

Logging configuration can be tuned in `src/main/resources/logback.xml`.
To learn more, you can read about [Logback](https://logback.qos.ch/manual/index.html).

Logging for requests and responses is disabled by default in the application runner, but enabled in tests.

## General Endpoints

#### Checking Server Responsiveness
```java
System.out.println((new BinanceApi()).ping() );
```
<details><summary>View Output</summary><pre>{}</pre></details>

#### Getting Server Time
```java
System.out.println((new BinanceApi()).time().get("serverTime").getAsString());
```
<details><summary>View Output</summary><pre>1508364584572</pre></details>

## Getting Account Information

#### Getting Account Information

```java
JsonObject account = (new BinanceApi()).account();
System.out.println("Maker Commission: " + account.get("makerCommission").getAsBigDecimal());
System.out.println("Taker Commission: " + account.get("takerCommission").getAsBigDecimal());
System.out.println("Buyer Commission: " + account.get("buyerCommission").getAsBigDecimal());
System.out.println("Seller Commission: " + account.get("sellerCommission").getAsBigDecimal());
System.out.println("Can Trade: " +  account.get("canTrade").getAsBoolean());
System.out.println("Can Withdraw: " + account.get("canWithdraw").getAsBoolean());
System.out.println("Can Deposit: " + account.get("canDeposit").getAsBoolean());
```
<details><summary>View Output</summary>
<pre>
Maker Commission: 10
Taker Commission: 10
Buyer Commission: 0
Seller Commission: 0
Can Trade: true
Can Withdraw: true
Can Deposit: true
</pre>
</details>

#### Getting All Balances

```java
System.out.println((new BinanceApi()).balances());
```
<details><summary>View Output</summary>
<pre>[{"asset":"BTC","free":"0.00001161","locked":"0.00000000"},{"asset":"LTC","free":"0.00000000","locked":"0.00000000"},...,{"asset":"ZEC","free":"0.00000000","locked":"0.00000000"}]</pre>
</details>

#### Getting Asset Balance
```java
System.out.println((new BinanceApi()).balancesMap().get("ETH"));
```
<details><summary>View Output</summary>
<pre>BinanceWalletAsset(asset=ETH, free=0.00859005, locked=0E-8)</pre>
</details>

#### Getting Open Orders
```java
System.out.println((new BinanceApi()).openOrders(BinanceSymbol.valueOf("ETHBTC")));
```
<details><summary>View Output</summary>
<pre>[BinanceOrder(symbol=ETHBTC, orderId=11111, clientOrderId=hNch6HItMQp2m9VuQZaA6L, price=0.18200000, origQty=1.00000000, executedQty=0E-8, status=NEW, timeInForce=GTC, type=LIMIT, side=SELL, stopPrice=0E-8, icebergQty=0E-8, time=1508361363677)]</pre>
</details>

#### Checking Order Status
```java
BinanceApi api = new BinanceApi();
BinanceSymbol symbol = BinanceSymbol.valueOf("ETHBTC");
System.out.println(api.getOrderById(symbol, 333821L));
System.out.println(api.getOrderByOrigClientId(symbol, "2m9VuQZaA6LhNch6HItMQp"));
```
<details><summary>View Output</summary>
<pre>BinanceOrder(symbol=ETHBTC, orderId=333821, clientOrderId=2m9VuQZaA6LhNch6HItMQp, price=0.18200000, origQty=1.00000000, executedQty=0E-8, status=NEW, timeInForce=GTC, type=LIMIT, side=SELL, stopPrice=0E-8, icebergQty=0E-8, time=1508361363677)</pre>
</details>

#### Getting All Orders
```java
System.out.println((new BinanceApi()).allOrders(BinanceSymbol.valueOf("BQXBTC")));
```
<details><summary>View Output</summary>
<pre>[BinanceOrder(symbol=BQXBTC, orderId=11111, clientOrderId=D0qeCsEBxKaXSRwxKUZkBZ, price=0.00018602, origQty=309.00000000, executedQty=309.00000000, status=FILLED, timeInForce=GTC, type=LIMIT, side=BUY, stopPrice=0E-8, icebergQty=0E-8, time=1506498038089), ... , BinanceOrder(symbol=BQXBTC, orderId=222222, clientOrderId=p2m9VuQZaA6LhNch6HItMQ, price=0.18200000, origQty=1.00000000, executedQty=0E-8, status=NEW, timeInForce=GTC, type=LIMIT, side=SELL, stopPrice=0E-8, icebergQty=0E-8, time=1508361363677)]</pre>
</details>


#### Getting My Trades
```java
System.out.println((new BinanceApi()).myTrades(BinanceSymbol.valueOf("BQXBTC")));
```
<details><summary>View Output</summary>
<pre>[BinanceTrade(id=1321, commissionAsset=BQX, price=0.00018602, qty=38.00000000, commission=0.03800000, time=1506499148055, isBuyer=true, isMaker=true, isBestMatch=true), ... , BinanceTrade(id=6315, commissionAsset=BNB, price=0.00015216, qty=449.00000000, commission=0.10018983, time=1507144715249, isBuyer=true, isMaker=true, isBestMatch=true)]</pre>
</details>


## Getting Market Data

#### Getting Bids and Asks on Symbol
```java
BinanceSymbol symbol = BinanceSymbol.valueOf("ETHBTC");
JsonObject depth = (new BinanceApi()).depth(symbol);
System.out.println("BIDS=" + depth.get("bids").getAsJsonArray());
System.out.println("ASKS=" + depth.get("asks").getAsJsonArray());
```
<details><summary>View Output</summary>
<pre>
```
BIDS=[["0.05577600","2.30000000",[]],["0.05577500","1.10000000",[]],["0.05577400","2.10000000",[]],...,["0.00000100","21000.00000000",[]]]
ASKS=[["0.05590600","10.90900000",[]],["0.05590700","9.96000000",[]],["0.05590800","9.44200000",[]],...,["0.06102400","0.50000000",[]]]
```
</pre></details>

#### Getting latest prices - as list of JsonObjects
```java
System.out.println((new BinanceApi()).prices());
```
<details><summary>View Output</summary>
<pre>[{"symbol":"ETHBTC","price":"0.05602000"},{"symbol":"LTCBTC","price":"0.01107300"},{"symbol":"BNBBTC","price":"0.00023561"},...,{"symbol":"BNTBTC","price":"0.00038649"}]</pre>
</details>

#### Getting latest prices - as map of decimals
```java
System.out.println((new BinanceApi()).pricesMap());
```
<details><summary>View Output</summary>
<pre>{ZECETH=0E-8, SALTETH=0.00940300, WTCETH=0.02207900, NEOETH=0.09463500, ... , MTHETH=0.00028000, FUNETH=0.00007538}</pre>
</details>

#### Getting latest price of a symbol
```java
System.out.println((new BinanceApi()).pricesMap().get("ETHBTC"));
```
<details><summary>View Output</summary><pre>0.05628800</pre></details>

#### Getting Aggregated Trades
```java
BinanceSymbol symbol = new BinanceSymbol("ETHBTC");
List<BinanceAggregatedTrades> binanceAggregatedTrades = (new BinanceApi()).aggTrades(symbol, 5, null);
BinanceAggregatedTrades trade = binanceAggregatedTrades.get(0);
System.out.println("TRADE=" + trade.toString() );
```
<details><summary>View Output</summary>
<pre>TRADE=BinanceAggregatedTrades{tradeId=2084592, price=0.05476100, quantity=6.87000000, firstTradeId=2192143, lastTradeId=2192143, timestamp=1508393572350, maker=true, bestPrice=true}</pre>
</details>

#### Getting All Book Tickers
```java
System.out.println((new BinanceApi()).allBookTickers());
```
<details><summary>View Output</summary>
<pre>[{"symbol":"ETHBTC","bidPrice":"0.05461000","bidQty":"29.73000000","askPrice":"0.05486300","askQty":"19.96000000"}, ... ,{"symbol":"ASTETH","bidPrice":"0.00102010","bidQty":"4030.00000000","askPrice":"0.00102920","askQty":"187.00000000"}]</pre>
</details>

#### Getting All Available Symbols
```java
System.out.println((new BinanceApi()).allBookTickersMap().keySet());
```
<details><summary>View Output</summary>
<pre>[ZECETH, SALTETH, WTCETH, NEOETH, IOTABTC, CTRBTC, SNMBTC, LRCETH, ... , MTLETH, SALTBTC, SUBBTC, MTHETH, FUNETH]</pre>
</details>

#### Getting 24hr Tickers for Symbol
```java
BinanceSymbol symbol = BinanceSymbol.valueOf("ETHBTC");
System.out.println((new BinanceApi()).ticker24hr(symbol));
```
<details><summary>View Output</summary>
<pre>{"priceChange":"-0.00180600","priceChangePercent":"-3.195","weightedAvgPrice":"0.05619267","prevClosePrice":"0.05652600","lastPrice":"0.05472000","lastQty":"0.26100000","bidPrice":"0.05472000","bidQty":"26.53900000","askPrice":"0.05486100","askQty":"0.53100000","openPrice":"0.05652600","highPrice":"0.05734100","lowPrice":"0.05460000","volume":"28260.45000000","quoteVolume":"1588.03005689","openTime":1508308200417,"closeTime":1508394600417,"firstId":2167326,"lastId":2192389,"count":25063}</pre>
</details>

#### Getting Klines / Candlesticks
```java
BinanceSymbol symbol = new BinanceSymbol("ETHBTC");
List<BinanceCandlestick> klines = (new BinanceApi()).klines(symbol, BinanceInterval.ONE_HOUR, 5, null);
BinanceCandlestick binanceCandlestick = klines.get(0);
System.out.println("KLINE=" + binanceCandlestick.toString() );
```
<details><summary>View Output</summary>
<pre>KLINE=BinanceCandlestick(openTime=1508378400000, open=0.05598000, high=0.05622000, low=0.05569100, close=0.05570500, volume=1514.33900000, closeTime=1508381999999, quoteAssetVolume=84.65979632, numberOfTrades=1683, takerBuyBaseAssetVolume=716.56500000, takerBuyQuoteAssetVolume=40.07877823)</pre>
</details>

## Placing Orders

#### Placing a LIMIT order
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>


#### Placing a MARKET order
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>


#### Placing a STOP LOSS order
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### Placing an ICEBERG order
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>


#### Cancel an order
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

## Using User Data Streams

#### Start User Data Stream
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### Keep User Data Stream Alive
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### Close User Data Stream
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### User Data Web Socket Watcher
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>


## Connecting to other Web Sockets

#### Depth Web Socket Watcher
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### Kline Web Socket Watcher
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>

#### Trades Web Socket Watcher
```java
```
<details><summary>View Output</summary>
<pre></pre>
</details>


# License
MIT. Anyone can copy, change, derive further work from this repository without any restrictions.
