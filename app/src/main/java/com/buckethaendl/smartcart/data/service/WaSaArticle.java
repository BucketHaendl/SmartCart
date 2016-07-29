package com.buckethaendl.smartcart.data.service;

public class WaSaArticle {

    private Long id;
    private String bezeichnung;
    private String kategorie;

    public WaSaArticle(Long id, String bezeichnung, String kategorie) {
        this.id = id;
        this.bezeichnung = bezeichnung;
        this.kategorie = kategorie;
    }

    public Long getId() {
        return id;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public String getKategorie() {
        return kategorie;
    }
}