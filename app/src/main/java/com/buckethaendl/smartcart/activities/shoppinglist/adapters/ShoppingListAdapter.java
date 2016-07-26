package com.buckethaendl.smartcart.activities.shoppinglist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.data.library.ShoppingListLibrary;
import com.buckethaendl.smartcart.objects.shoppinglist.ShoppingList;

import java.util.List;

/**
 * Created by Cedric on 20.07.16.
 */
public class ShoppingListAdapter extends BaseAdapter {

    private Context context;
    private int resource;
    private List<ShoppingList> lists;

    public ShoppingListAdapter(Context context, int resource) {

        super();

        this.context = context;
        this.lists = ShoppingListLibrary.getInstance().getShoppingLists();
        this.resource = resource;

    }

    @Override
    public int getCount() {

        return this.lists.size();

    }

    @Override
    public Object getItem(int i) {

        return this.lists.get(i);

    }

    @Override
    public long getItemId(int i) {

        return i;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) throws IndexOutOfBoundsException {

        ShoppingList selectedList = (ShoppingList) this.getItem(position);
        ShoppingListViewHolder viewHolder;

        if(selectedList != null) {

            if(convertView == null) { //inflate the view

                LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(this.resource, parent, false);

                viewHolder = new ShoppingListViewHolder(convertView);
                convertView.setTag(R.string.shopping_list_view_holder_tag, viewHolder);

            }

            else {

                viewHolder = (ShoppingListViewHolder) convertView.getTag(R.string.shopping_list_view_holder_tag);

            }

            //Set shopping_list_overview_listitem_circle_imageview

            //circleImageView.setBackgroundResource(selectedList.getIconId());
            viewHolder.getCircleImageView().setBackgroundResource(selectedList.getIconId());
            //circleImageView.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.banner_shopping_go, null));

            //Set shopping_list_overview_listitem_title_textview
            viewHolder.getTitleTextView().setText(selectedList.getName());

            //Set shopping_list_overview_listitem_date_textview
            viewHolder.getDateTextView().setText(selectedList.getDateFormatted());

            //Set shopping_list_overview_listitem_info_textview
            String itemCount = selectedList.size() + " " + this.context.getString(R.string.shopping_list_overview_listitem_info_product);
            viewHolder.getInfoTextView().setText(itemCount);

            convertView.setTag(R.string.shopping_list_object_tag, selectedList);

            return convertView;

        }

        else throw new IndexOutOfBoundsException("Error: Null view, selected list at position " + position + " is null");

    }

    public class ShoppingListViewHolder {

        private ImageView circleImageView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView infoTextView;

        public ShoppingListViewHolder(View base) {

            this.circleImageView = (ImageView) base.findViewById(R.id.shopping_list_overview_listitem_circle_imageview);
            this.titleTextView = (TextView) base.findViewById(R.id.shopping_list_overview_listitem_title_textview);
            this.dateTextView = (TextView) base.findViewById(R.id.shopping_list_overview_listitem_date_textview);
            this.infoTextView = (TextView) base.findViewById(R.id.shopping_list_overview_listitem_info_textview);

        }

        public ImageView getCircleImageView() {
            return circleImageView;
        }

        public TextView getTitleTextView() {
            return titleTextView;
        }

        public TextView getDateTextView() {
            return dateTextView;
        }

        public TextView getInfoTextView() {
            return infoTextView;
        }
    }

}
