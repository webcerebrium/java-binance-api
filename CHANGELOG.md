# Changelog

## 1.0.3 (2017-10-26)

- Initial version

## 1.0.5 (2017-11-30)

- Separating data classes into `datatype` package.
- Added public `publicStats()` endpoint

## 1.0.6 (2017-12-11)

- Converting `HashMap` instances into `ConcurrentHashMap` to resolve concurrency issues

## 1.0.7 (2017-12-30)

- Added fill-or-kill (FOK) BinanceTimeInForce
- Fixed NullPointerException while setting assetOfCommission for orders
- Fixes in BinanceEventExecutionReport

## 1.0.8 (2018-01-13)

- Fixes on placing MARKET orders
- Added exchangeInfo() method to keep track of public markets information
- Fixes of klines() and aggTrades() calls with options