package com.example.gsvo.smssender;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SmsListener extends BroadcastReceiver{

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            if (bundle != null){
                //---retrieve the SMS message received---
                try{

                    SMS.notif("Synchronisation en cours ...", context);
                    Log.d("hsms", "Synchronisation en cours ...");
                    sync_messages msg = new sync_messages(context);
                    msg.execute("https://urla.fr/hcode_api/ajax/get_sms.php");
                }catch(Exception e){
                          Log.d("hsms",e.getMessage());
                }
            }
        }
    }
}
