package com.buckethaendl.smartcart.objects.instore;

import com.buckethaendl.smartcart.data.service.WaSaArticle;

import java.util.List;

public class Shelf {

    public static final Shelf UNKNOWN = new Shelf(-1, -100, 0, 0, null);

    private Integer shelfId;
    private Integer priority;
    private Integer x;
    private Integer y;
    private List<WaSaArticle> articles;

    public Shelf(Integer shelfId, Integer priority, Integer x, Integer y, List<WaSaArticle> articles) {

        this.shelfId = shelfId;
        this.priority = priority;
        this.x = x;
        this.y = y;

        this.articles = articles;
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

    public List<WaSaArticle> getArticles() {
        return articles;
    }

    //todo funktioniert das überhaupt?

    //Problem: Geht so nicht! Denn sonst werden keine gleichen Regale mehr hinzugefügt!
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (shelfId != null ? !shelfId.equals(shelf.shelfId) : shelf.shelfId != null) return false;
        if (priority != null ? !priority.equals(shelf.priority) : shelf.priority != null)
            return false;
        if (x != null ? !x.equals(shelf.x) : shelf.x != null) return false;
        return y != null ? y.equals(shelf.y) : shelf.y == null;

    }

    @Override
    public int hashCode() {
        int result = shelfId != null ? shelfId.hashCode() : 0;
        result = 31 * result + (priority != null ? priority.hashCode() : 0);
        result = 31 * result + (x != null ? x.hashCode() : 0);
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }

}
