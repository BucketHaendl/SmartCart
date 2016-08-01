package com.buckethaendl.smartcart.objects.choosestore;

public abstract class DistanceCalculator {

    public static double calculateDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {

        double earthRadius = 6371.0d;
        double dLatitude = Math.toRadians(latitudeB-latitudeA);
        double dLongitude = Math.toRadians(longitudeB-longitudeA);
        double sinDLatitude = Math.sin(dLatitude / 2);
        double sinDLongitude = Math.sin(dLongitude / 2);
        double a = Math.pow(sinDLatitude, 2) + Math.pow(sinDLongitude, 2)
                * Math.cos(Math.toRadians(latitudeA)) * Math.cos(Math.toRadians(latitudeB));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }
}
