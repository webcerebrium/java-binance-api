@Grapes([ 
  @Grab(group = 'com.webcerebrium', module = 'binance-api', version = '1.0.9')
])
import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import com.webcerebrium.binance.datatype.BinanceEventDepthUpdate;
import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterDepth;

try {
  def symbol = new BinanceSymbol("ETHBTC");
  def session = (new BinanceApi()).websocketDepth(symbol, new BinanceWebSocketAdapterDepth() {
    @Override
    public void onMessage(BinanceEventDepthUpdate message) { 
        println(message.toString());
    }
  });
  // do nothing - user should terminate watcher by pressing Ctrl+C

} catch (BinanceApiException e) {
  println( "ERROR: " + e.getMessage());
}
