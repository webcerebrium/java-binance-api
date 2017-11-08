package com.webcerebrium.binance.datatype;


import com.google.common.base.Strings;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import lombok.Data;

import java.util.Date;

@Data
public class BinanceHistoryFilter {

    public String asset = "";
    public Date startTime = null;
    public Date endTime = null;

    public BinanceHistoryFilter() {
    }

    public BinanceHistoryFilter(String asset) {
        this.asset = asset;
    }

    public String getAsQuery() {
        StringBuffer sb = new StringBuffer();
        Escaper esc = UrlEscapers.urlFormParameterEscaper();

        if (!Strings.isNullOrEmpty(asset)) {
            sb.append("&asset=").append(esc.escape(asset));
        }
        if (startTime != null) {
            sb.append("&startTime=").append(startTime.getTime());
        }
        if (endTime != null) {
            sb.append("&endTime=").append(endTime.getTime());
        }
        String s = sb.toString();
        return s.length() > 1 ? s.substring(1) : s; // skipping the first &
    }
}
