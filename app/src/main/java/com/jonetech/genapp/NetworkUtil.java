package com.jonetech.genapp;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

    // Method to get the webData

    static String getWebData(String url) {

        HttpURLConnection urlConnection = null;
        BufferedReader bfReader = null;
        String data = null;

        try {

            Uri builtUri = Uri.parse(url);

            URL urlText = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) urlText.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputData = urlConnection.getInputStream();
            bfReader = new BufferedReader(new InputStreamReader(inputData));

            String line;
            StringBuilder mBuilder = new StringBuilder();

            while ((line = bfReader.readLine()) != null) {
                mBuilder.append(line);
            }

            if (mBuilder.length() == 0) {
                return null;
            }

            data = mBuilder.toString();


        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (bfReader != null) {
                try {
                    bfReader.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }

        System.out.println(data);
        return data;
    }
}
