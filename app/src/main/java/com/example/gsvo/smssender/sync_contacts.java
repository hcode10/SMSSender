package com.example.gsvo.smssender;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by hcode on 25/05/2017.
 */

public class sync_contacts extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String result = "";
        Log.e("web", "Lancement de la requete (Contacts)!");
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            //add reuqest header
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
            urlConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String contact = URLEncoder.encode(MainActivity.getContacts(), "UTF-8");

            String urlParameters = "str=" + contact;

            Log.d("hsms", "str=" + urlParameters);
            urlConnection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int code = urlConnection.getResponseCode();

            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;
                }
                in.close();
            }

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("web", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("web", e.getMessage());
        }

        finally {
            urlConnection.disconnect();
        }
        return result;

    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("hsms", "Adhérents : " + result);



        super.onPostExecute(result);
    }
}
