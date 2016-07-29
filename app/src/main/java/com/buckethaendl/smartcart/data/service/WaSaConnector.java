package com.buckethaendl.smartcart.data.service;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.data.local.LibraryListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * todo noch in AsyncTask und gescheit handlen
 */
public class WaSaConnector {

    public static final String TAG = WaSaConnector.class.getName();
    private static final boolean USE_LOCAL_FILE = true;

    private static final String COUNTRY_PH = "{country}";
    private static final String MARKT_PH = "{number}";
    private static final String QUERY_PH = "{query}";
    private static final String WASA_REST_URL = "https://iawasa01.dc.hn.de.kaufland/wasa-smart-cart-service/rest/smartcart/"+COUNTRY_PH+"/"+MARKT_PH+"/regale?artikelname="+QUERY_PH;

    private static WaSaConnector instance;

    static {

        instance = new WaSaConnector();

    }

    private WaSaConnector() {

    }

    public static WaSaConnector getInstance() {

        return WaSaConnector.instance;

    }

    /**
     * Returns the shelves in a sync task to directly retrieve them (hackaround)
     */
    public List<WaSaFBBShelf> loadShelvesSync(String country, int market, String query) {

        LoadWaSaFBBShelvesAsyncTask task = new LoadWaSaFBBShelvesAsyncTask(country, market, query);
        return task.doInBackground();

    }

    public void loadShelvesAsync(String country, int market, String query, LibraryListener<List<WaSaFBBShelf>> listener) {

        LoadWaSaFBBShelvesAsyncTask task = new LoadWaSaFBBShelvesAsyncTask(country, market, query, listener);
        task.execute();

    }

    private class LoadWaSaFBBShelvesAsyncTask extends AsyncTask<Void, Void, List<WaSaFBBShelf>> {

        private String country;
        private int market;
        private String query;

        private Handler uiHandler;
        private LibraryListener<List<WaSaFBBShelf>> listener;

        public LoadWaSaFBBShelvesAsyncTask(String country, int market, String query) {

            this(country, market, query, null);

        }

        public LoadWaSaFBBShelvesAsyncTask(String country, int market, String query, LibraryListener<List<WaSaFBBShelf>> listener) {

            this.country = country;
            this.market = market;
            this.query = query;
            this.listener = listener;

        }

        @Override
        protected void onPreExecute() {

            this.uiHandler = new Handler(Looper.getMainLooper());
            if(this.listener != null) this.listener.onOperationStarted();

        }

        @Override
        protected List<WaSaFBBShelf> doInBackground(Void...voids) {

            List<WaSaFBBShelf> resultShelf = this.queryShelves(country, market, query);

            if(this.listener != null) this.listener.onLoadResult(resultShelf);
            return resultShelf;

        }

        @Override
        protected void onPostExecute(List<WaSaFBBShelf> loadedShelf) {

            if(this.listener != null) {

                this.listener.onOperationFinished();

            }

        }

        /**
         * Queries the given data and gives back all shelves, that contain the given product and market details
         * @param country 2 letter ISO code of the country to search in ("DE")
         * @param market 4 digit market number of the market to search in (6250)
         * @param query a search query term of the product to search the shelves for ("honey")
         * @return all shelves that contain products with the ware description query term in this specific store (here: all honey shelves)
         */
        public List<WaSaFBBShelf> queryShelves(String country, int market, String query){

            this.prepareUntrustedCertificateHackaround();

            String queryUrl = WASA_REST_URL
                    .replace(COUNTRY_PH, country)
                    .replace(MARKT_PH, String.valueOf(market))
                    .replace(QUERY_PH, query);

            try {

                URL url = new URL(queryUrl);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

                String rawResult = readResultString(connection);
                return parseJsonShelves(rawResult);

            } catch (MalformedURLException me){

                Log.e(TAG, "Broken URL: "+ queryUrl);

            } catch (IOException e) {

                Log.e(TAG, "Couldn't establish connection to WASA RestService with URL: " + queryUrl);

            }

            return null;

        }

        /**
         * Reads out the result string containing the JSON from the given connection
         * @param connection The connection to the JSON
         * @return A string containing the resulting JSON
         */
        private String readResultString(HttpsURLConnection connection){

            if(USE_LOCAL_FILE){ //todo remove

                File testFile = new File(App.EXTERNAL_DIRECTORY, "smartcart.json");

                try {

                    InputStreamReader sReader = new InputStreamReader(new FileInputStream(testFile));
                    BufferedReader bReader = new BufferedReader(sReader);

                    String line = "";
                    StringBuilder json = new StringBuilder();

                    while((line = bReader.readLine()) !=null) {
                        json.append(line);
                    }

                    return json.toString();

                }

                catch (FileNotFoundException e) {

                    e.printStackTrace();

                }

                catch (IOException e) {

                    e.printStackTrace();

                }

            }

            else {

                if(connection != null){

                    try {

                        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                        BufferedReader bufferedReader = new BufferedReader(streamReader);

                        String json = "";
                        String line;

                        while((line = bufferedReader.readLine())!=null){
                            json += line;
                        }

                        return json;

                    }

                    catch (IOException e){
                        Log.e(TAG, "No Answer from URL: " + connection.getURL().toString());
                    }

                }

            }

            Log.v(TAG, "The connection is closed. No data could be read.");
            return "";

        }

        /**
         * Parses the given JSON to a list of WaSaFBBShelf objects
         * @param json the JSON to be parsed
         * @return a list of shelves contained in the JSON
         */
        private List<WaSaFBBShelf> parseJsonShelves(String json) {

            //Create the JSON
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<WaSaFBBShelf>>(){}.getType();

            //Create List<WaSaFBBShelf> from the JSON
            List<WaSaFBBShelf> regale = gson.fromJson(json, listType);

            return regale;

        }

        /**
         * A workaround for the missing SSL connection certificate
         */
        private void prepareUntrustedCertificateHackaround(){

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };

            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                Log.e(TAG, "SSQ-Hackaround failed");
            }

        }

    }


}
