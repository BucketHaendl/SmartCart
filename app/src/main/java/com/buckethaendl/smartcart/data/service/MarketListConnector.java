package com.buckethaendl.smartcart.data.service;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.R;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.buckethaendl.smartcart.objects.choosestore.DistanceCalculator;
import com.buckethaendl.smartcart.objects.choosestore.Market;
import com.buckethaendl.smartcart.objects.choosestore.MarketDistance;
import com.buckethaendl.smartcart.util.ConnectionUtil;
import com.buckethaendl.smartcart.util.JsonUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class MarketListConnector {

    public static final String TAG = MarketListConnector.class.getName();
    private static final String FILIALLISTE_URL = "https://app-at.kaufland.net/data/api/v1/stores";

    private static MarketListConnector instance;

    static {

        instance = new MarketListConnector();

    }

    private MarketListConnector() {

    }

    public static MarketListConnector getInstance() {

        return MarketListConnector.instance;

    }

    /**
     * Returns the markets in a sync task to directly retrieve them (hackaround)
     */
    public List<MarketDistance> loadMarketDistancesSync(double userLongitude, double userLatitude, int maxResults) {

        LoadMarketListAsyncTask task = new LoadMarketListAsyncTask(userLongitude, userLatitude, maxResults);
        return task.doInBackground();

    }

    /**
     * Returns the markets in an async task to retrieve them
     * Loads ALL markets in the world in the background but only returns the number (maxResults) we limit the list to
     */
    public void loadMarketDistancesAsync(double userLongitude, double userLatitude, int maxResults, LibraryListener<List<MarketDistance>> listener) {

        LoadMarketListAsyncTask task = new LoadMarketListAsyncTask(userLongitude, userLatitude, maxResults, listener);
        task.execute();

    }

    private class LoadMarketListAsyncTask extends AsyncTask<Void, Void, List<MarketDistance>> {

        private double userLongitude;
        private double userLatitude;
        private int maxResults;

        private LibraryListener<List<MarketDistance>> listener;

        public LoadMarketListAsyncTask(double userLongitude, double userLatitude, int maxResults) {

            this(userLongitude, userLatitude, maxResults, null);

        }

        public LoadMarketListAsyncTask(double userLongitude, double userLatitude, int maxResults, LibraryListener<List<MarketDistance>> listener) {

            this.userLongitude = userLongitude;
            this.userLatitude = userLatitude;
            this.maxResults = maxResults;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            if (this.listener != null) this.listener.onOperationStarted();

        }

        @Override
        protected List<MarketDistance> doInBackground(Void... voids) {

            List<MarketDistance> markets = this.loadMarkets(userLongitude, userLatitude, maxResults);

            if (this.listener != null) this.listener.onLoadResult(markets);
            return markets;

        }

        @Override
        protected void onPostExecute(List<MarketDistance> loadedMarkets) {

            if(loadedMarkets == null) return; //do not proceed if nothing loaded

            if (this.listener != null) {

                this.listener.onOperationFinished();

            }

        }

        private List<MarketDistance> loadMarkets(final double userLongitude, final double userLatitude, final int maxResults) {

            final Context context = App.getGlobalContext();

            ConnectionUtil.prepareUntrustedCertificateHackaround();
            this.setDefaultAuthenticator();

            try {
                URL url = new URL(FILIALLISTE_URL);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                List<Market> allMaerkte = parseJsonToMarkets(connection);
                return getMaxNumberWithDistance(userLongitude, userLatitude, maxResults, allMaerkte);
            }

            catch (MalformedURLException me) {

                Looper.prepare();
                Toast toast = Toast.makeText(context, R.string.error_04, Toast.LENGTH_SHORT);
                toast.show();

                Log.e(TAG, "Broken URL: " + FILIALLISTE_URL);
                me.printStackTrace();

            }

            catch (IOException e) {

                Looper.prepare();
                Toast toast = Toast.makeText(context, R.string.error_05, Toast.LENGTH_SHORT);
                toast.show();

                Log.e(TAG, "Couldn't establish connection to WASA-restservice with URL: " + FILIALLISTE_URL);
                e.printStackTrace();

                Toast toast2 = Toast.makeText(context, R.string.error_08, Toast.LENGTH_SHORT);
                toast2.show();

                Log.v(TAG, "IOException occurred while trying to read Json from Content");
                e.printStackTrace();

            }

            return null;

        }

        private List<Market> parseJsonToMarkets(HttpsURLConnection connection) throws IOException {

            String json = JsonUtil.getJsonContent(connection);

            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Market>>(){}.getType();
            List<Market> markets = gson.fromJson(json, listType);

            return markets;

        }

        private List<MarketDistance> getMaxNumberWithDistance(double userLongitude, double userLatitude, int maxResults, List<Market> allMarkets){

            List<MarketDistance> marketsWithDistance = new ArrayList<MarketDistance>();

            for(Market market : allMarkets){

                MarketDistance distance = new MarketDistance(market, DistanceCalculator.calculateDistance(userLatitude, userLongitude, market.getLatitude(), market.getLongitude()));
                marketsWithDistance.add(distance);

            }

            Collections.sort(marketsWithDistance);

            //cut-off too many results
            return marketsWithDistance.subList(0, maxResults);

        }

        private void setDefaultAuthenticator(){

            Authenticator myAuth = new KISAuthenticator();
            Authenticator.setDefault(myAuth);

        }


    }

}
