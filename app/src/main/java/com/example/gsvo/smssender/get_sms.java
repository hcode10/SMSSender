package com.example.gsvo.smssender;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class get_sms extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        String result = "";
        Log.e("web", "Lancement de la requete !");
        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int code = urlConnection.getResponseCode();

            if(code==200){
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    String line = "";

                    while ((line = bufferedReader.readLine()) != null)
                        result += line;

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

        if(result.equals("OK")){
            Log.d("hsms", "Activation du timer !");
            SMS.enableTimer();
            return;
        }

        if(!result.equals("") && !result.equals("OK")){
            Log.d("hsms", "Envoi du message en cours ...");
            SMSFormat inf_sms = new SMSFormat(result);

            SMS.send_sms(inf_sms.getId(), inf_sms.getNum(), inf_sms.getBody());

        }

        if(result.equals("")){
            SMS.enableTimer();
            return;
        }



        super.onPostExecute(result);
    }
}
