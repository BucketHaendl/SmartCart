package com.buckethaendl.smartcart.data.service;

import java.util.List;

/**
 * A more specific implementation of a real WaSaFBBShelf, retrieved from the WaSa Service
 *
 * Created by Cedric on 04.08.16.
 */
public class WaSaFBBShelf extends FBBShelf {

    private String regaltyp;
    private List<WaSaArticle> artikel;

    public WaSaFBBShelf(String fbbNr, String regaltyp, List<WaSaArticle> artikel) {

        super(fbbNr);

        this.regaltyp = regaltyp;
        this.artikel = artikel;
    }

    public String getRegaltyp() {
        return regaltyp;
    }

    public List<WaSaArticle> getArtikel() {
        return artikel;
    }

}
