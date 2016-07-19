package com.buckethaendl.smartcart.activities.shoppinglist.adapters;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.MaterialChooser;
import com.buckethaendl.smartcart.activities.shoppinglist.ShoppingListAddActivity;
import com.buckethaendl.smartcart.objects.shoppingList.Material;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppingList.ShoppingListItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Cedric on 31.03.2016.
 */
public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ShoppingItemViewHolder> {

    private ShoppingList list;

    private ShoppingItemClickListener shoppingItemClickListener;
    private ShoppingItemCheckedListener shoppingItemCheckedListener; //TODO register a listener if needed?

    public ShoppingListRecyclerViewAdapter(ShoppingList list) {

        this.list = list;

    }

    @Override
    public ShoppingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the content views and return a new holder for each instanciated object
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        ViewGroup rootLayout = (ViewGroup) inflater.inflate(R.layout.shopping_list_item, parent, false);
        return new ShoppingItemViewHolder(rootLayout);

    }

    @Override
    public void onBindViewHolder(final ShoppingItemViewHolder holder, int position) {

        final ShoppingListItem item = this.list.getItem(position);

        //Set up check listener
        CheckBox box = (CheckBox) holder.getView().findViewById(R.id.shopping_list_item_checkbox);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                item.setChecked(isChecked);

                if(getShoppingItemCheckedListener() != null) {

                    getShoppingItemCheckedListener().onCheckShoppingItem(item, holder.getAdapterPosition(), isChecked);

                }

            }

        });

        //Set up content listener
        holder.getView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(getShoppingItemClickListener() != null) {

                    getShoppingItemClickListener().onClickShoppingItem(item, holder.getAdapterPosition());

                }

            }

        });

        //Set up material chooser listener
        holder.getView().findViewById(R.id.shopping_list_item_category_imagebutton).setOnClickListener(new ChooseMaterialClickListener(holder, item));

        //Set the data of this shopping list item to the views in the holder
        View rootView = holder.getView();

        CheckBox checked = (CheckBox) rootView.findViewById(R.id.shopping_list_item_checkbox);
        checked.setChecked(item.isChecked());

        TextView name = (TextView) rootView.findViewById(R.id.shopping_list_item_textview);
        name.setText(item.getName());

        ImageButton category = (ImageButton) rootView.findViewById(R.id.shopping_list_item_category_imagebutton);
        category.setImageResource(item.getMaterial().getIconResourceId());
        category.setContentDescription(item.getMaterial().getName());

        //TODO implement a property animation to reveal the item


            /*
            //initialize variables
            this.itemNameText = (EditText) holder.getView().findViewById(R.id.shopping_list_new_item_edittext);
            this.materialIconImage = (ImageButton) holder.getView().findViewById(R.id.shopping_list_new_item_category_imagebutton);

            //set the keylistener for the edittext
            itemNameText.setKeyListener(new KeyListener() {

                @Override
                public int getInputType() {
                    return itemNameText.getInputType();
                }

                @Override
                public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {

                    boolean isEnterKey = (keyCode == KeyEvent.KEYCODE_ENTER);

                    if (isEnterKey) {

                        if(getAddItemListener() != null) {

                            String name = getAddItemName();
                            int materialIconResourceId = getAddItemCategoryId();

                            getAddItemListener().onAddItemListener(name, materialIconResourceId);

                            Log.v("ShoppingListAddActivity", "Enter-Key pressed: Added new item " + name);

                        }

                    }

                    return isEnterKey;

                }

                @Override
                public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
                    return false;
                }

                @Override
                public boolean onKeyOther(View view, Editable text, KeyEvent event) {
                    return false;
                }

                @Override
                public void clearMetaKeyState(View view, Editable content, int states) {
                }

            });

            //set the onClickListener for the add button
            final ImageButton itemAddButton = (ImageButton) holder.getView().findViewById(R.id.shopping_list_new_item_add_button);
            itemAddButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if(getAddItemListener() != null) {

                        String name = getAddItemName();
                        int materialIconResourceId = getAddItemCategoryId();

                        getAddItemListener().onAddItemListener(name, materialIconResourceId);

                        Log.v("ShoppingListAddActivity", "Add button pressed: Added new item " + name);

                    }

                }

            });

            //maybe set a listener for the keyboard-close event as well

            */

    }

    @Override
    public int getItemCount() {

        return this.list.getItemsCount();

    }

    public ShoppingItemClickListener getShoppingItemClickListener() {
        return this.shoppingItemClickListener;
    }

    public void setShoppingItemClickListener(ShoppingItemClickListener shoppingItemClickListener) {
        this.shoppingItemClickListener = shoppingItemClickListener;
    }

    public ShoppingItemCheckedListener getShoppingItemCheckedListener() {
        return shoppingItemCheckedListener;
    }

    public void setShoppingItemCheckedListener(ShoppingItemCheckedListener shoppingItemCheckedListener) {
        this.shoppingItemCheckedListener = shoppingItemCheckedListener;
    }

    public static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {

        private View view;

        public ShoppingItemViewHolder(View view) { //View is in this case a RelativeLayout or whatever root view was chosen for the listitems or a FrameLayout or so

            super(view);
            this.view = view;

        }

        public View getView() {

            return this.view;

        }

    }

    //für den material / category chooser
    private class ChooseMaterialClickListener implements AdapterView.OnClickListener, MaterialChooser.MaterialChosenListener {

        private ShoppingItemViewHolder holder;
        private ShoppingListItem item;

        public ChooseMaterialClickListener(ShoppingItemViewHolder holder, ShoppingListItem item) {

            this.holder = holder;
            this.item = item;


        }

        @Override
        public void onClick(View view) {

            ImageView button = (ImageView) view; //maybe set the drawable resource to something else

            List<Material> materialList = Arrays.asList(Material.values());
            MaterialChooser chooser = new MaterialChooser(view.getContext(), view, materialList, this);

            chooser.show();

        }

        @Override
        public void onMaterialChosen(Material material) {

            Log.v("ShoppingListRecyclerAd", "Material " + material.getName() + " chosen - now changing");

            //change the material of the shoppinglistitem
            this.item.setMaterial(material);
            ShoppingListRecyclerViewAdapter.this.notifyItemChanged(holder.getAdapterPosition());

        }

    }

    public interface ShoppingItemClickListener {

        public void onClickShoppingItem(ShoppingListItem item, int position);

    }

    public interface ShoppingItemCheckedListener {

        public void onCheckShoppingItem(ShoppingListItem item, int position, boolean checked);

    }

}
