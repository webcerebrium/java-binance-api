@Grapes([ 
  @Grab(group = 'com.webcerebrium', module = 'binance-api', version = '1.0.9')
])
import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;

try {
  def api = new BinanceApi();
  println( "ETH-BTC PRICE=" + api.pricesMap().get("ETHBTC"));
} catch (BinanceApiException e) {
  println( "ERROR: " + e.getMessage());
}
