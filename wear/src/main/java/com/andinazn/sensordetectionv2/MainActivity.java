/*
*** Created by Andina Zahra Nabilla on 10 April 2018
*** Created by Kalista Umari on 30 April 2018
*
* Active Sensor: Heartrate Monitor
* Build Gradle Version: 2.3.0
* Mobile SDK Version: 25.0.3
* Wear SDK Version: 25.0.0
*
* Activity berfungsi untuk:
* 1. Pengaturan layout wear
* 2. Pemanggilan hasil data sensor Accelerometer dan Heartrate yang terdeteksi di SensorService menggunakan Broadcast Receiver
* 3. Menampilkan data sensor heartrate sebagai TextView Wear
* 4. Menggunakan fungsi GetDate untuk mendapatkan timestamp dari data sensor yang didapat dan menampilkannya sebagai TextView
*
* Done right:
* + Heartrate sensor kedetect
* + Flow berpikir harusnya udah bener
* + DeviceClient.java fixx
* + MessageReceiverService fixx
* + Data HR dan ACC muncul di logcat
* + Nama app sudah falldetection
* + Nama app sudah com.andinazn.sensordetectionv2
* + Data accelerometer & HR muncul di textview
* + Accelerometer berhasil dikirim dan ditampilkan di mobile
* + Processing accelerometer di wear
* + Pengiriman fall detected ke mobile bisa
* + Layout wear
* + Timestamp
* + Activity Recognition masuk
* 
* Need fixing:
* - Menjadikan Activity Recognition dalam bentuk value dan memprosesnya untuk pengiriman
* - Di SensorService gimana caranya not [0, 0, 0] ga ke skip -> fall detected
* - Max 590 records (20 menit)
* - Sensor HR lebih lama dari ACC
 */

package com.andinazn.sensordetectionv2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends Activity {

    private TextView timeStampTxt, hrTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //1. Pengaturan layout wear
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                //* 3. Menampilkan data sensor heartrate sebagai TextView Wear
                timeStampTxt = (TextView) stub.findViewById(R.id.timeStampTxt);
                hrTxt = (TextView) stub.findViewById(R.id.hrTxt);
            }
        });

        this.registerReceiver(mMessageReceiver, new IntentFilter("com.example.Broadcast"));

    }

    // * 2. Pemanggilan hasil data sensor Accelerometer dan Heartrate yang terdeteksi di SensorService menggunakan Broadcast Receiver
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Get timestamp
            long timeStamp = intent.getLongExtra("TIME", 0)/1000000L;
            timeStampTxt.setText(String.valueOf(getDate()));

            //Get accuracy
            int message2 = intent.getIntExtra("ACCR", 0);

            // Extract data included in the Intent
            //Receiving heartrate data from broadcast
            float[] message1 = intent.getFloatArrayExtra("HR");
            Log.d("Receiver", "Got message1: " + message1[0] + "Got message2: " + message2);
            int tmpHr = (int)Math.ceil(message1[0] - 0.5f);
            hrTxt.setText(String.valueOf(tmpHr));

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        // Register mMessageReceiver to receive messages.
        this.registerReceiver(mMessageReceiver, new IntentFilter("com.example.Broadcast"));

    }

    //4. Menggunakan fungsi GetDate untuk mendapatkan timestamp dari data sensor yang didapat dan menampilkannya sebagai TextView
    private String getDate(){

        try{
            DateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            Date netDate = (new Date());
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "7:00";
        }
    }

}
