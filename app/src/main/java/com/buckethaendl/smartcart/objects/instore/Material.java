package com.buckethaendl.smartcart.objects.instore;


import com.buckethaendl.smartcart.objects.shoppinglist.Icons;

/**
 * Created by Cedric on 29.07.16.
 */
public enum Material {

    FRISCHE(1, "Fresh", Icons.getRandomIcon()),
    NAHRUNG_UNGEK(2, "Uncooled", Icons.getRandomIcon()),
    TK_U_FLEISCH(3, "Meat", Icons.getRandomIcon()),
    MOPRO(4, "Milk", Icons.getRandomIcon()),
    HAUSHALTSW(5, "House", Icons.getRandomIcon()),
    ALKOHOL(6, "Alcohol", Icons.getRandomIcon()),
    NON_FOOD(7, "Non-Food", Icons.getRandomIcon()),
    SNACKS(8, "Snacks", Icons.getRandomIcon()),
    GETRAENKE(9, "Beverages", Icons.getRandomIcon()),
    WERBUNG(10, "Ads", Icons.getRandomIcon()),
    HAUSHALTSM(11, "Daily", Icons.getRandomIcon()),
    TIERFUTTER_KASSE(12, "Pets", Icons.getRandomIcon());

    private int id;
    private String name;
    private final int iconId;

    private Material(int id, String name, int iconId) {

        this.id = id;
        this.name = name;
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

    public Material getMaterialBelow() {

        return Material.getMaterial(this.id - 1);

    }

    public Material getMaterialAbove() {

        return Material.getMaterial(this.id + 1);

    }

    public int getId() {

        return id;

    }

    public int getIconId() {
        return iconId;
    }


    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
