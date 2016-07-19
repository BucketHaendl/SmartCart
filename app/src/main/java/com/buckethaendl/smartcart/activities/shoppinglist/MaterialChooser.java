package com.buckethaendl.smartcart.activities.shoppinglist;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.objects.shoppingList.Material;

import java.util.List;

/**
 * Created by Cedric on 18.04.2016.
 */
public class MaterialChooser extends ListPopupWindow { //using the appcompat version

    private MaterialChooserListAdapter adapter;

    private Context context;
    private List<Material> materials;

    private MaterialChosenListener listener;

    //ACHTUNG: Nie WRAP_CONTENT verwenden, da sonst die width/height des anchors verwendet wird!

    public MaterialChooser(Context context, View anchor, List<Material> materials) {

        this(context, anchor, materials, null);

    }

    public MaterialChooser(Context context, View anchor, List<Material> materials, MaterialChosenListener listener) {

        super(context);

        this.setAnchorView(anchor);

        this.context = context;
        this.materials = materials;

        this.listener = listener;

        this.initialize();
        this.setContentWidth(this.measureContentWidth(this.adapter));

        this.setDropDownGravity(Gravity.END); //makes the list drop towards bottom

    }

    private ListAdapter initialize() {

        this.adapter = new MaterialChooserListAdapter();
        this.setAdapter(this.adapter);

        MaterialChooserListener listener = new MaterialChooserListener();
        this.setOnItemClickListener(listener);

        return this.adapter;

    }

    /**
     * Measures the width required to display the drop-down list correctly
     * @param adapter The ListAdapter of with the respective views
     * @return The pixels needed to display all content properly
     */
    private int measureContentWidth(final ListAdapter adapter) {

        ViewGroup mMeasureParent = null;
        int maxWidth = 0;

        View itemView = null;
        int itemType = 0;

        final int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        final int count = adapter.getCount();

        for (int i = 0; i < count; i++) {

            final int positionType = adapter.getItemViewType(i);

            //Some checking, that the itemType is really a content item
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }

            if (mMeasureParent == null) {
                mMeasureParent = new FrameLayout(context);
            }

            //Get the views width
            itemView = adapter.getView(i, itemView, mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);

            final int itemWidth = itemView.getMeasuredWidth();

            //Save the width if it's the widest one
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }

        }

        return maxWidth;
    }

    public MaterialChosenListener getListener() {
        return listener;
    }

    public void setListener(MaterialChosenListener listener) {
        this.listener = listener;
    }

    private class MaterialChooserListAdapter implements ListAdapter {

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
        }

        @Override
        public int getCount() {
            return MaterialChooser.this.materials.size();
        }

        @Override
        public Object getItem(int position) {
            return MaterialChooser.this.materials.get(position);
        }

        @Override
        public long getItemId(int position) {

            //TODO does this really work?

            Material material = (Material) this.getItem(position);
            return materials.indexOf(material);
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            if(view == null) {

                view = LayoutInflater.from(MaterialChooser.this.context).inflate(R.layout.shopping_list_material_chooser_item, parent, false);

            }

            //Set the values
            Material suggestedMaterial = (Material) this.getItem(position); //no type params available, that's why we have to cast

            TextView textView = (TextView) view.findViewById(R.id.shopping_list_material_chooser_textview);
            textView.setText(suggestedMaterial.getName());

            ImageView imageView = (ImageView) view.findViewById(R.id.shopping_list_material_chooser_icon_imageview);
            imageView.setImageResource(suggestedMaterial.getIconResourceId());
            imageView.setContentDescription(suggestedMaterial.getName());

            return view;

        }

        @Override
        public int getItemViewType(int position) {
            return IGNORE_ITEM_VIEW_TYPE;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return (getCount() <= 0);
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

    }

    private class MaterialChooserListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Log.v("MaterialChooser", "You selected a material");

            Material material = MaterialChooser.this.materials.get((int) id);

            MaterialChooser.this.dismiss();

            if(getListener() != null) getListener().onMaterialChosen(material);

        }

    }

    public interface MaterialChosenListener {

        void onMaterialChosen(Material material);

    }

}
