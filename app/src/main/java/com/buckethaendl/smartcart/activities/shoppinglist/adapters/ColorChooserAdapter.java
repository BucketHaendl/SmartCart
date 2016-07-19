package com.buckethaendl.smartcart.activities.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.buckethaendl.smartcart.R;

import java.util.List;

/**
 * Created by Cedric on 03.04.2016.
 */
public class ColorChooserAdapter extends RecyclerView.Adapter<ColorChooserAdapter.ColorItemViewHolder> {

    private ColorChooserListener listener;
    private final int[] colors;

    public ColorChooserAdapter(int[] colors) {

        this(colors, null);

    }

    public ColorChooserAdapter(int[] colors, ColorChooserListener listener) {

        this.colors = colors;
        this.listener = listener;

    }

    public ColorChooserListener getListener() {
        return  this.listener;
    }

    public void setListener(ColorChooserListener listener) {
        this.listener = listener;
    }

    @Override
    public ColorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.color_chooser_item, parent, false);
        return new ColorItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ColorItemViewHolder holder, int position) {

        ImageView imageView = holder.getView();
        final int color = this.colors[position];

        imageView.setImageResource(color);
        //TODO muss natürlich noch verbessert werden, damit es rund ist usw., content description

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(ColorChooserAdapter.this.listener != null) {

                    ColorChooserAdapter.this.listener.onColorChosen(color);

                }

            }

        });

    }

    @Override
    public int getItemCount() {

        return this.colors.length;

    }

    public static class ColorItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView colorView;

        public ColorItemViewHolder(ImageView colorView) {

            super(colorView);
            this.colorView = colorView;

        }

        public ImageView getView() {

            return this.colorView;

        }

    }

    public interface ColorChooserListener {

        void onColorChosen(int color);

    }

}
