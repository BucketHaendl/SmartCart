package com.buckethaendl.smartcart.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.HttpsURLConnection;

public abstract class JsonUtil {

    public static final String TAG = JsonUtil.class.getName();

    public static String getJsonContent(HttpsURLConnection connection) throws IOException {

        if(connection!=null){

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

                throw new IOException("No answer from URL: " + e.getMessage());
            }
        }

        Log.e(TAG, "The connection is closed. No data could be read.");
        return "";
    }
}
