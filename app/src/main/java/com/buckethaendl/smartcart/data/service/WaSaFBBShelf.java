package com.buckethaendl.smartcart.data.service;

import java.util.List;

public class WaSaFBBShelf {

    private String fbbNr;
    private String regaltyp;
    private List<WaSaArticle> artikel;

    public WaSaFBBShelf(String fbbNr, String regaltyp, List<WaSaArticle> artikel) {
        this.fbbNr = fbbNr;
        this.regaltyp = regaltyp;
        this.artikel = artikel;
    }

    public String getFbbNr() {
        return fbbNr;
    }

    public String getRegaltyp() {
        return regaltyp;
    }

    public List<WaSaArticle> getArtikel() {
        return artikel;
    }
}
