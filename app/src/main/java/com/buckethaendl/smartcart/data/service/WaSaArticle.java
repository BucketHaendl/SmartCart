package com.buckethaendl.smartcart.data.service;

import java.util.Locale;

public class WaSaArticle {

    private Long id;
    private String bezeichnung;
    private String kategorie;

    public WaSaArticle(Long id, String bezeichnung, String kategorie) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.kategorie = kategorie;
    }

    private Long getId() {
        return id;
    }

    private String getBezeichnung() {
        return bezeichnung;
    }

    private String getKategorie() {
        return kategorie;
    }

    @Override
    public String toString() {

        return String.format(Locale.GERMANY, "[Article] " + getBezeichnung().replace("%", "%%"));

    }

}