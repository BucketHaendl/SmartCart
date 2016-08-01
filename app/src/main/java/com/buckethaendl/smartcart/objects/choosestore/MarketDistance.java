package com.buckethaendl.smartcart.objects.choosestore;

import android.support.annotation.NonNull;

/**
 * A wrapper object for each market with the distance to the current location
 * NOTE: The distance is always based on the location, the object was created on. If this has changed, the distance might not be valid anymore
 *
 * Created by Cedric on 01.08.16.
 */
public class MarketDistance implements Comparable<MarketDistance> {

    private Market market;
    private double distance;

    public MarketDistance(Market market, double distance) {
        this.market = market;
        this.distance = distance;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public int compareTo(@NonNull  MarketDistance marketDistance) {

        if(this.getDistance() < marketDistance.getDistance()) return -1;
        else if(this.getDistance() == marketDistance.getDistance()) return 0;
        else return 2;

    }

}
