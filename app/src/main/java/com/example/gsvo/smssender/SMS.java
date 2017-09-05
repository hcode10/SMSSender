package com.example.gsvo.smssender;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class SMS extends Service {
    private static Handler myHandler;
    private static NotificationManager nMN;
     private static final int NOTIFICATION_ID = 54675;
    private static Notification n;
    private static Context myContext;

    private static Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            // Code à éxécuter de façon périodique
            //Toast.makeText(getApplicationContext(), "Maintenant", Toast.LENGTH_SHORT).show();
            get_sms e = new get_sms();

            e.execute("https://urla.fr/hcode_api/ajax/get_sms.php");
        }
    };

    @Override
    public void onCreate(){
        myContext = this;
        nMN = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notif("Service Actif!", myContext);
    }

    public static void notif(String content, Context c){
        n  = new Notification.Builder(c)
                .setContentTitle("SMSSender")
                .setContentText(content)
                .setSmallIcon(R.drawable.if_go_sms_70315)
                .setOngoing(true)
                .build();
        nMN.notify(NOTIFICATION_ID, n);
    }

    public static boolean send_sms(String id, String num, String body){
        notif("Envoi en cours ...", myContext);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(num, null, body, null, null);

        get_sms e = new get_sms();

        e.execute("https://urla.fr/hcode_api/ajax/get_sms.php?id=" + id);


        Log.d("hsms", "Envoi effectuée avec succées !");
        notif("Envoi effectuée avec succées !", myContext);
        return true;
    }

    public static  void enableTimer(){
        myHandler.postDelayed(myRunnable,25000);
    }



    public SMS() {
        myHandler = new Handler();
        myHandler.postDelayed(myRunnable,5000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy(){
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
        nMN.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}
