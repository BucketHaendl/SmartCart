package com.buckethaendl.smartcart.objects.choosestore;

import java.io.Serializable;

public class Company implements Serializable {

    private String zip;
    private String managingDirectors;
    private String city;
    private String jurisdiction;
    private String vat;
    private String office;
    private String companyIdent;
    private String commercialRegister;
    private String street;
    private String name;

    public Company(String zip, String managingDirectors, String city, String jurisdiction, String vat, String office, String companyIdent,
                   String commercialRegister, String street, String name) {
        this.zip = zip;
        this.managingDirectors = managingDirectors;
        this.city = city;
        this.jurisdiction = jurisdiction;
        this.vat = vat;
        this.office = office;
        this.companyIdent = companyIdent;
        this.commercialRegister = commercialRegister;
        this.street = street;
        this.name = name;
    }

    public String getZip() {
        return zip;
    }

    public String getManagingDirectors() {
        return managingDirectors;
    }

    public String getCity() {
        return city;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public String getVat() {
        return vat;
    }

    public String getOffice() {
        return office;
    }

    public String getCompanyIdent() {
        return companyIdent;
    }

    public String getCommercialRegister() {
        return commercialRegister;
    }

    public String getStreet() {
        return street;
    }

    public String getName() {
        return name;
    }
}