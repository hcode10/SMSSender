package com.example.gsvo.smssender;

import android.util.Log;


public class SMSFormat {


    private String num, body, id;

    public SMSFormat(String SMSTbl){
        if(SMSTbl.length() < 5){
            return;
        }
        Log.d("hsms", "SMSTbl : " + SMSTbl);
        String[] SMSInfos = SMSTbl.split("@");

        id = SMSInfos[0];
        num = SMSInfos[1];
        body = SMSInfos[2];

        Log.d("hsms", "num = " + num);
        Log.d("hsms", "body = " + body);
        Log.d("hsms", "id = " + id);
    }

    public String getNum(){
        return num;
    }

    public String getBody(){
        return body;
    }

    public String getId(){
        return id;
    }
}
