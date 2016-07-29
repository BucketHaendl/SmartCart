package com.buckethaendl.smartcart.activities.shoppinglist.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.objects.shoppinglist.Icons;

/**
 * Created by Cedric on 03.04.2016.
 */
public class IconChooseAdapter extends RecyclerView.Adapter<IconChooseAdapter.ColorItemViewHolder> {

    private IconChooseListener listener;
    private final int[] colors;

    public IconChooseAdapter() {

        this(Icons.getIcons(), null);

    }

    public IconChooseAdapter(int[] colors, IconChooseListener listener) {

        this.colors = colors;
        this.listener = listener;

    }

    public IconChooseListener getListener() {
        return  this.listener;
    }

    public void setListener(IconChooseListener listener) {
        this.listener = listener;
    }

    @Override
    public ColorItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ImageView view = (ImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.color_chooser_item, parent, false);
        return new ColorItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ColorItemViewHolder holder, int position) {

        final ImageView imageView = holder.getView();
        final int color = this.colors[position];

        imageView.setImageResource(color);
        //TODO muss natürlich noch verbessert werden, damit es rund ist usw., content description

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Animation animation = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.color_choose_anim);
                imageView.startAnimation(animation);

                if(IconChooseAdapter.this.listener != null) {

                    IconChooseAdapter.this.listener.onIconChosen(color);

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

    public interface IconChooseListener {

        void onIconChosen(int color);

    }

}
