package laiwei.mydagger2.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laiwei on 2018/3/30 0030.
 */
public class Ticker {
    public String id;
    public String name;
    public String symbol;
    public String price;
    @SerializedName("percent_change_24h")
    public String percentChange24h;
}
