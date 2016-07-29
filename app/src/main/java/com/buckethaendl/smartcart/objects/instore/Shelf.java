package com.buckethaendl.smartcart.objects.instore;

public class Shelf {

    private Integer shelfId;
    private Integer priority;
    private Integer x;
    private Integer y;

    public Shelf(Integer shelfId, Integer priority, Integer x, Integer y) {

        this.shelfId = shelfId;
        this.priority = priority;
        this.x = x;
        this.y = y;

    }

    public Integer getShelfId() {
        return shelfId;
    }

    public Integer getPriority() {
        return priority;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

}
