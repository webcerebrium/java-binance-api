@Grapes([ 
  @Grab(group = 'com.webcerebrium', module = 'binance-api', version = '1.0.5')
])
import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceSymbol;

try {
  def api = new BinanceApi();

  println(api.pricesMap());


} catch (BinanceApiException e) {
  println( "ERROR: " + e.getMessage());
}



