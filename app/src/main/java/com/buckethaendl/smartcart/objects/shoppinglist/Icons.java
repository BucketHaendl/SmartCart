package com.buckethaendl.smartcart.objects.shoppinglist;

import com.buckethaendl.smartcart.R;

/**
 * Created by Cedric on 27.07.16.
 */
public class Icons {

    private static final int[] ICONS = {

            R.drawable.list_apple_icon,
            R.drawable.list_carrot_icon,
            R.drawable.list_cherry_icon,
            R.drawable.list_waterbottle_icon,
            R.drawable.list_pizza_icon,
            R.drawable.list_coffee_beam_icon,
            R.drawable.list_chicken_icon,
            R.drawable.list_conev_icon,
            R.drawable.list_egg_icon,
            R.drawable.list_food_icon,
            R.drawable.list_garlic_icon,
            R.drawable.list_guava_icon,
            R.drawable.list_icecream_icon,
            R.drawable.list_meat_icon,
            R.drawable.list_strawberry_icon,
            R.drawable.list_sweet_icon,
            R.drawable.list_toffee_icon,
            R.drawable.list_tomato_icon

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
