package com.buckethaendl.smartcart.objects.instore;


import com.buckethaendl.smartcart.objects.shoppinglist.Icons;

/**
 * Created by Cedric on 29.07.16.
 */
public enum Material {

    FRISCHE(1, Icons.getRandomIcon()),
    NAHRUNG_UNGEK(2, Icons.getRandomIcon()),
    TK_U_FLEISCH(3, Icons.getRandomIcon()),
    MOPRO(4, Icons.getRandomIcon()),
    HAUSHALTSW(5, Icons.getRandomIcon()),
    ALKOHOL(6, Icons.getRandomIcon()),
    NON_FOOD(7, Icons.getRandomIcon()),
    SNACKS(8, Icons.getRandomIcon()),
    GETRAENKE(9, Icons.getRandomIcon()),
    WERBUNG(10, Icons.getRandomIcon()),
    HAUSHALTSM(11, Icons.getRandomIcon()),
    TIERFUTTER_KASSE(12, Icons.getRandomIcon());

    private int id;
    private final int iconId;

    private Material(int id, int iconId) {

        this.id = id;
        this.iconId = iconId;

    }

    public static Material getMaterial(int id) {

        for(Material material : Material.values()) {

            if(material.getId() == id) return material;

        }

        return null;

    }

    public static int getMaterialId(int priority) {

        return (priority / 100);

    }

    public int getId() {

        return id;

    }

    public int getIconId() {
        return iconId;
    }

}
