package com.buckethaendl.smartcart.objects.shoppingList;

import java.io.Serializable;

/**
 * An object representing a single item from a shoppinglist.
 * The private field checked determines if the item was already bought and is thus ticked in the shopping list
 *
 * Created by Cedric on 18.03.2016.
 */
public class ShoppingListItem implements Serializable {

    public static final long SERIALIZATION_ID = 0L;

    private String name;
    private Material material;
    private boolean checked;

    /**
     * Create a new item for a shopping list
     * @param name of the item (e.g. bread)
     * @param material of the item (e.g. Material.BAKED_GOODS)
     */
    public ShoppingListItem(String name, Material material) {

        this(name, material, false);

    }

    /**
     * Create a new item for a shopping list
     * @param name of the item (e.g. bread)
     * @param material of the item (e.g. Material.BAKED_GOODS)
     * @param checked true if the item was already bought and should appear ticked
     */
    public ShoppingListItem(String name, Material material, boolean checked) {

        this.name = name;
        this.material = material;
        this.checked = checked;

    }

    //Getters & Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {

        return this.getName();

    }

}
