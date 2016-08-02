package com.buckethaendl.smartcart.objects.choosestore;

import java.io.Serializable;

public class Opening implements Serializable {

    private String weekday;
    private Integer close;
    private Integer open;

    public Opening(String weekday, Integer close, Integer open) {
        this.weekday = weekday;
        this.close = close;
        this.open = open;
    }

    public String getWeekday() {
        return weekday;
    }

    public Integer getClose() {
        return close;
    }

    public Integer getOpen() {
        return open;
    }
}