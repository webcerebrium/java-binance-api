package binance.api;

import com.google.common.base.Strings;
import lombok.Data;

@Data
public class BinanceSymbol {

    String symbol = "";

    public BinanceSymbol(String symbol) {
        this.symbol = symbol.replace("_", "").toUpperCase();
    }

    public String get() throws BinanceApiException {
        if (Strings.isNullOrEmpty(symbol)) {
            throw new BinanceApiException("Symbol cannot be empty. Example: BQXBTC");
        }
        return this.getSymbol();
    }
}
