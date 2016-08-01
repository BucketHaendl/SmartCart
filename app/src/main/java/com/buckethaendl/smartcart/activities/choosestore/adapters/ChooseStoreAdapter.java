package com.buckethaendl.smartcart.activities.choosestore.adapters;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.activities.choosestore.listeners.ChooseStoreClickListener;
import com.buckethaendl.smartcart.objects.choosestore.Market;
import com.buckethaendl.smartcart.objects.choosestore.MarketDistance;

import java.util.List;

/**
 * Created by Cedric on 01.08.16.
 */
public class ChooseStoreAdapter extends RecyclerView.Adapter<ChooseStoreAdapter.ChooseStoreViewHolder> {

    private List<MarketDistance> markets;

    protected ChooseStoreClickListener clickListener;

    public ChooseStoreAdapter(List<MarketDistance> markets) {

        this(markets, null);

    }

    public ChooseStoreAdapter(List<MarketDistance> markets, ChooseStoreClickListener listener) {

        this.markets = markets;
        this.clickListener = listener;

    }

    @Override
    public ChooseStoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.choose_store_item, parent, false);

        return new ChooseStoreViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final ChooseStoreViewHolder holder, int position) {

        final MarketDistance distance = this.markets.get(position);
        final Market market = distance.getMarket();

        Resources res = App.getGlobalResources();

        holder.getNameTextView().setText(res.getString(R.string.choose_store_activity_store_name_format, market.getStoreId()));
        holder.getAddressTextView().setText(res.getString(R.string.choose_store_activity_store_address_format, market.getStreet(), market.getZipcode(), market.getCity()));
        holder.getDistanceTextView().setText(res.getString(R.string.choose_store_activity_store_distance_format, distance.getDistance()));

        holder.getView().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(ChooseStoreAdapter.this.clickListener != null) ChooseStoreAdapter.this.clickListener.onClick(market, holder.getAdapterPosition());

            }

        });

    }

    @Override
    public int getItemCount() {

        return markets.size();

    }

    public ChooseStoreClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(ChooseStoreClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public static class ChooseStoreViewHolder extends RecyclerView.ViewHolder {

        private View view;

        private TextView nameTextView;
        private TextView addressTextView;
        private TextView distanceTextView;

        public ChooseStoreViewHolder(View view) {

            super(view);

            this.view = view;
            this.nameTextView = (TextView) view.findViewById(R.id.choose_store_item_store_name);
            this.addressTextView = (TextView) view.findViewById(R.id.choose_store_item_store_address);
            this.distanceTextView = (TextView) view.findViewById(R.id.choose_store_item_store_distance);

        }

        public View getView() {
            return view;
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getAddressTextView() {
            return addressTextView;
        }

        public TextView getDistanceTextView() {
            return distanceTextView;
        }
    }

}
