package com.buckethaendl.smartcart.objects.instore;


import com.buckethaendl.smartcart.R;

/**
 * Created by Cedric on 29.07.16.
 */
public enum Material {

    UNKNOWN(-1, "Unknown", R.drawable.category_unknown), //unknwon category

    FRISCHE(1, "Fresh", R.drawable.category_fresh),
    NAHRUNG_UNGEK(2, "Uncooled", R.drawable.category_uncooled),
    TK_U_FLEISCH(3, "Meat", R.drawable.category_meat),
    MOPRO(4, "Milk", R.drawable.category_milk),
    HAUSHALTSW(5, "House", R.drawable.category_household),
    ALKOHOL(6, "Alcohol", R.drawable.category_alcohol),
    NON_FOOD(7, "Non-Food", R.drawable.category_nonfood),
    SNACKS(8, "Snacks", R.drawable.category_snacks),
    GETRAENKE(9, "Beverages", R.drawable.category_beverages),
    WERBUNG(10, "Ads", R.drawable.category_ads),
    HAUSHALTSM(11, "Daily", R.drawable.category_daily),
    TIERFUTTER_KASSE(12, "Pets", R.drawable.category_icecream);

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
