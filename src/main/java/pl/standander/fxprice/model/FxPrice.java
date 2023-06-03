package pl.standander.fxprice.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FxPrice {
    private long id;
    private String name;
    private double bid;
    private double ask;
    private Date date;

    public void setBid(double bid) {
        this.bid = bid * 0.999;
    }
    public void setAsk(double ask) {
        this.ask = ask * 1.001;
    }

    public void setName(String name) {
        this.name = name.trim();
    }

    @SneakyThrows
    public void setDate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
        this.date = formatter.parse(date);
    }
}
