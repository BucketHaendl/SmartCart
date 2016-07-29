package com.buckethaendl.smartcart.objects.shoppinglist;

import com.buckethaendl.smartcart.R;

/**
 * Created by Cedric on 27.07.16.
 */
public class Icons {

    private static final int[] ICONS = {
            R.drawable.apple_icon,
            R.drawable.carrot_icon,
            R.drawable.garlic_icon,
            R.drawable.icecream_icon,
            R.drawable.sweet_icon,
            //Ab hier wiederholt es sich (nur zum test)
            R.drawable.apple_icon,
            R.drawable.carrot_icon,
            R.drawable.garlic_icon,
            R.drawable.icecream_icon,
            R.drawable.sweet_icon,
            R.drawable.apple_icon,
            R.drawable.carrot_icon,
            R.drawable.garlic_icon,
            R.drawable.icecream_icon,
            R.drawable.sweet_icon,
            R.drawable.apple_icon,
            R.drawable.carrot_icon,
            R.drawable.garlic_icon,
            R.drawable.icecream_icon,
            R.drawable.sweet_icon,
            R.drawable.apple_icon,
            R.drawable.carrot_icon,
            R.drawable.garlic_icon,
            R.drawable.icecream_icon,
            R.drawable.sweet_icon,
    };

    private Icons() {

    }

    public static int[] getIcons() {

        return Icons.ICONS;

    }

    public static int getRandomIcon() {

        int rand = (int) (Math.random() * ICONS.length);
        return ICONS[rand];

    }

}
