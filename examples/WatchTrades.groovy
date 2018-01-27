@Grapes([ 
  @Grab(group = 'com.webcerebrium', module = 'binance-api', version = '1.0.8')
])
import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;
import com.webcerebrium.binance.datatype.BinanceSymbol;
import com.webcerebrium.binance.datatype.BinanceEventAggTrade;
import com.webcerebrium.binance.websocket.BinanceWebSocketAdapterAggTrades;

try {
  def symbol = new BinanceSymbol("ETHBTC");
  def session = (new BinanceApi()).websocketTrades(symbol, new BinanceWebSocketAdapterAggTrades() {
    @Override
    public void onMessage(BinanceEventAggTrade message) { 
        println(message.toString());
    }
  });
  // do nothing - user should terminate watcher by pressing Ctrl+C

} catch (BinanceApiException e) {
  println( "ERROR: " + e.getMessage());
}
