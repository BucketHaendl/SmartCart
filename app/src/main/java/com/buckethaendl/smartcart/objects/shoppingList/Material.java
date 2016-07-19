package com.buckethaendl.smartcart.objects.shoppingList;

import com.buckethaendl.smartcart.R;

/**
 * Materials are the overall categories of SmartCart and should NOT be confused with the main item synonyme
 * Example: Milka [Material = SWEETS, MainItemSynonyme = Chocolate]
 * Each material defines it's own small icon
 *
 * Created by Cedric aka. BucketHaendl on 18.03.2016.
 */
public enum Material {

    //TODO All supported materials are yet to be imported, this is just a quick example
    //TODO set the names via string resources

    BAKED_GOODS("baked goods", android.R.drawable.sym_def_app_icon),
    FRESH_FRUITS("fresh fruits", android.R.drawable.presence_busy),
    FRESH_VEGETABLES("vegetables", android.R.drawable.ic_input_add),
    UNDEFINIED("further", android.R.drawable.btn_plus);

    private String name;
    private int iconResourceId;

    private Material(String name, int iconResourceId) {

        this.name = name;
        this.iconResourceId = iconResourceId;

    }

    public String getName() {
        return name;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

}
