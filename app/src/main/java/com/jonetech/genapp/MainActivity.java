package com.jonetech.genapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner resource;
    private TextView resultText;
    private EditText inputText;

    // for tha async-task
    private WeakReference<TextView> rText;
    private String fItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resource = (Spinner) findViewById(R.id.spinner);
        resultText = (TextView) findViewById(R.id.page_result);
        inputText = (EditText) findViewById(R.id.urlText);

        resource.setOnItemSelectedListener(this);

        // Populate our spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.security, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Apply the adapter
        resource.setAdapter(adapter);

    }

    public void viewPageResult(View view) {

        String editText = inputText.getText().toString();

        StringBuilder builder = new StringBuilder();
        builder.append(fItem);
        builder.append(editText);

        String genText = builder.toString();



        // Get the keyboard running on the app
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        // Check if its not null
        if (inputManager != null) {

            // Hide the keyboard on the click of the button
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Check the phone connection state
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the network info of the phone
        NetworkInfo mNetworkInfo = null;

        // if the phone connection is not null
        if (connManager != null) {

            // Get the active connection info of mobile app
            mNetworkInfo = connManager.getActiveNetworkInfo();
        }

        // if the network info is not null
        // and the the phone is connected to the internet
        // and the text in the input is not null

        if (mNetworkInfo != null && mNetworkInfo.isConnected() && editText.length() != 0) {

            // perform a query on the api and fetch our result

            new FetchDataSite(resultText).execute(genText);

            // Show that the app is loading in the background for results
            resultText.setText(R.string.loading);

        } else {


            // if the input text is 0
            if (editText.length() == 0) {

                // notify the user that the EditText can't be empty
                resultText.setText(R.string.no_search_term);
            } else {

                // notify the user that there is not network available on the app
                resultText.setText(R.string.no_network);
            }
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int ids = parent.getSelectedItemPosition();

        String[] arrays = getResources().getStringArray(R.array.security);
        fItem = arrays[ids];

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class FetchDataSite extends AsyncTask<String, Void, String>{

        public FetchDataSite(TextView textView){

            rText = new WeakReference<>(textView);

        }

        @Override
        protected String doInBackground(String... strings) {
            return new NetworkUtil().getWebData(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{
                System.out.println(s);
                rText.get().setText(s);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
