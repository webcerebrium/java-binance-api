# Java Binance API
This project is designed to help you make your own projects that interact with the [Binance API](https://www.binance.com/restapipub.html). 
You can stream candlestick chart data, market depth, or use other advanced features such as setting stop losses and iceberg orders. 
This project seeks to have complete API coverage including WebSockets.

## Getting Started

For compiling and launching the project from source code, you need
- `Java 1.8`
- [`gradle`](https://gradle.org/releases/) dependency manager to be installed
- file `src/main/resources/application.properties` should contain Binance API keys: 

```
BINANCE_API_KEY=<key>
BINANCE_API_SECRET=<secret>
```
As alternative, you can set an environment variables or java properties BINANCE_API_KEY, BINANCE_API_SECRET

To have Java 8 on clean Ubuntu installation, just run the following
```
apt-get -y install software-properties-common
add-apt-repository -y ppa:webupd8team/java && apt-get -y update
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
apt-get -y install oracle-java8-installer
```

#### Running from command line

Compile the project with `gradle distZip`. Under `build\distributions` you will have

#### Running in Eclipse
==
This project uses Lombok. You will need to install lombok per guidelines (https://projectlombok.org/download.html) to make the project compile in Eclipse. It will compile via gradle just fine without this additional installation step.

#### Running in Intellij IDEA
==
This project uses Lombok. You will need to install corresponding plugin (https://plugins.jetbrains.com/plugin/6317) and enable `Annotation processing` in your `Compiler` settings. It will compile via gradle just fine without this additional installation step.
To start, import current directory as Gradle project.

### Logging Configuration

Logging configuration can be tuned `src/main/resources/logback.xml` (https://logback.qos.ch/manual/index.html)
Extensive logging is enabled by default.

#### Getting latest price of a symbol
```java
Map<String, BigDecimal> prices = (new BinanceApi()).getPrices('BTC');
System.out.println("Prices={}", prices.toString());
```
<details>
 <summary>View Response</summary>
```js
{ ETHBTC: '0.07003500',
  LTCBTC: '0.01176700',
  BNBBTC: '0.00035735'
}
</details>

### License
MIT
