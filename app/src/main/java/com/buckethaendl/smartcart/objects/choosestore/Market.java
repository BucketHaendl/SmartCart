package com.buckethaendl.smartcart.objects.choosestore;

import com.buckethaendl.smartcart.R;

import java.io.Serializable;
import java.util.Locale;

public class Market implements Serializable {

    public static final int DEFAULT_DRAWABLE = R.drawable.kaufland_market;

    private String country;
    private Opening[] specialOpeningHours;
    private String[] serviceCounters;
    private String city;
    private Double latitude;
    private String[] services;
    private String storeId;
    private String type;
    private String zipcode;
    private String phone;
    private String street;
    private Company company;
    private Opening[] openingHours;
    private Double longitude;
    private String updatedAt;
    private int drawableId;

    public Market(String country, Opening[] specialOpeningHours, String[] serviceCounters, String city, Double latitude, String[] services,
                  String storeId, String type, String zipcode, String phone, String street, Company company, Opening[] openingHours,
                  Double longitude, String updatedAt) {
        this.country = country;
        this.specialOpeningHours = specialOpeningHours;
        this.serviceCounters = serviceCounters;
        this.city = city;
        this.latitude = latitude;
        this.services = services;
        this.storeId = storeId;
        this.type = type;
        this.zipcode = zipcode;
        this.phone = phone;
        this.street = street;
        this.company = company;
        this.openingHours = openingHours;
        this.longitude = longitude;
        this.updatedAt = updatedAt;
    }

    public String getCountry() {
        return country;
    }

    public Opening[] getSpecialOpeningHours() {
        return specialOpeningHours;
    }

    public String[] getServiceCounters() {
        return serviceCounters;
    }

    public String getCity() {
        return city;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String[] getServices() {
        return services;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getType() {
        return type;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public String getStreet() {
        return street;
    }

    public Company getCompany() {
        return company;
    }

    public Opening[] getOpeningHours() {
        return openingHours;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the drawable id of the logo of this market
     * NOTE: Will always return Kaufland logo by now
     * @return The int id of the drawable
     */
    public int getDrawableId() {

        if(drawableId != 0) return drawableId;
        else return DEFAULT_DRAWABLE;
    }

    @Override
    public String toString() {

        return String.format(Locale.GERMANY, "[MARKET %s] Country: %s (Street: %s, Zip: %s, City: %s) Coordinates: (%.2f°/%.2f°)", this.storeId, this.country, this.street, this.zipcode, this.city, this.longitude, this.latitude);

    }

}