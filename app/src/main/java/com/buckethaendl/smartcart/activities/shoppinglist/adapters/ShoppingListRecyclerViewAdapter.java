package com.buckethaendl.smartcart.activities.shoppinglist.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.shoppinglist.listeners.ShoppingItemCheckListener;
import com.buckethaendl.smartcart.activities.shoppinglist.listeners.ShoppingItemClickListener;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingListItem;
import com.buckethaendl.smartcart.util.DialogBuildingSite;

/**
 * Created by Cedric on 31.03.2016.
 */
public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ShoppingItemViewHolder> {

    protected ShoppingList list;

    protected ShoppingItemClickListener shoppingItemClickListener;
    protected ShoppingItemCheckListener shoppingItemCheckListener;

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
    public void onBindViewHolder(final ShoppingItemViewHolder holder, final int position) {

        final ShoppingListItem item = this.list.get(position);
        final Context context = holder.getView().getContext();

        //Set the data of this shopping list item to the views in the holder
        View rootView = holder.getView();

        //Set up content listener
        rootView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                ShoppingListRecyclerViewAdapter.this.list.remove(item);
                ShoppingListRecyclerViewAdapter.this.notifyDataSetChanged();

                return true;

            }

        });

        //Set up content listener todo remove long - just for deletion now!
        rootView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(getShoppingItemClickListener() != null) {

                    getShoppingItemClickListener().onClickShoppingItem(item, holder.getAdapterPosition());

                }

            }

        });

        final CheckBox checkBox = holder.getCheckBox();
        checkBox.setChecked(item.isChecked());

        //Set up check listener
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                item.setChecked(checkBox.isChecked());
                if(getShoppingItemCheckedListener() != null) {

                    getShoppingItemCheckedListener().onCheckShoppingItem(item, holder.getAdapterPosition(), checkBox.isChecked());

                }

            }

        });

        TextView textView = holder.getTextView();
        textView.setText(item.getDisplayName());

        final ImageButton error = holder.getImageButton();

        if(item.isUnknown()) {

            error.setVisibility(View.VISIBLE);
            error.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.stat_notify_error));

        }

        else error.setVisibility(View.INVISIBLE);

        //Set up error checker
        error.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                //show error dialog
                final AlertDialog errorDialog = DialogBuildingSite.buildErrorDialog(context, context.getString(R.string.item_not_recognized, item.getDisplayName()));
                errorDialog.setCancelable(true);

                //ok button
                errorDialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.error_dialog_ok), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        errorDialog.dismiss();

                    }

                });

                //no ignore button
                errorDialog.setButton(DialogInterface.BUTTON_NEGATIVE, null, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }

                });

                errorDialog.show();

            }

        });

    }

    @Override
    public int getItemCount() {

        return this.list.size();

    }

    public ShoppingItemClickListener getShoppingItemClickListener() {
        return this.shoppingItemClickListener;
    }

    public void setShoppingItemClickListener(ShoppingItemClickListener shoppingItemClickListener) {
        this.shoppingItemClickListener = shoppingItemClickListener;
    }

    public ShoppingItemCheckListener getShoppingItemCheckedListener() {
        return shoppingItemCheckListener;
    }

    public void setShoppingItemCheckedListener(ShoppingItemCheckListener shoppingItemCheckListener) {
        this.shoppingItemCheckListener = shoppingItemCheckListener;
    }

    public static class ShoppingItemViewHolder extends RecyclerView.ViewHolder {

        private View view;

        private CheckBox checkBox;
        private TextView textView;
        private ImageButton imageButton;

        public ShoppingItemViewHolder(View view) { //View is in this case a RelativeLayout or whatever root view was chosen for the listitems or a FrameLayout or so

            super(view);
            this.view = view;

            this.checkBox = (CheckBox) view.findViewById(R.id.shopping_list_item_checkbox);
            this.textView = (TextView) view.findViewById(R.id.shopping_list_item_textview);
            this.imageButton = (ImageButton) view.findViewById(R.id.shopping_list_item_icon_imagebutton);

        }

        public View getView() {
            return view;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public TextView getTextView() {
            return textView;
        }

        public ImageButton getImageButton() {
            return imageButton;
        }

    }

}
