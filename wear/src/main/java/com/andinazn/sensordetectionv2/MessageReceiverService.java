/*
** Created by Andina Zahra Nabilla on 10 April 2018
*
* Activity berfungsi untuk:
* 1. Deklarasi pemanggilan Device Client
* 2. Pemanggilan Data Item Berdasarkan Uri yang terdiri dari creator dan path
* 3. Mengembalikan Path Message Yang Dikirimkan
 */

package com.andinazn.sensordetectionv2;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.github.pocmo.sensordashboard.shared.ClientPaths;
import com.github.pocmo.sensordashboard.shared.DataMapKeys;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MessageReceiverService extends WearableListenerService {
    private static final String TAG = "SensorDashboard/MessageReceiverService";

    private DeviceClient deviceClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //1. Deklarasi pemanggilan Device Client
        deviceClient = DeviceClient.getInstance(this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);

        for (DataEvent dataEvent : dataEvents) {
            //Indicates that the enclosing DataEvent was triggered by a data item being added or changed.
            if (dataEvent.getType() == DataEvent.TYPE_CHANGED) {
                //2. Pemanggilan Data Item Berdasarkan Uri yang terdiri dari creator dan path
                DataItem dataItem = dataEvent.getDataItem();
                Uri uri = dataItem.getUri();
                String path = uri.getPath();

                if (path.startsWith("/filter")) {
                    DataMap dataMap = DataMapItem.fromDataItem(dataItem).getDataMap();
                    int filterById = dataMap.getInt(DataMapKeys.FILTER);
                    deviceClient.setSensorFilter(filterById);
                }
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "Received message: " + messageEvent.getPath());

        //3. Mengembalikan Path Message Yang Dikirimkan
        if (messageEvent.getPath().equals(ClientPaths.START_MEASUREMENT)) {
            startService(new Intent(this, SensorService.class));
        }

        if (messageEvent.getPath().equals(ClientPaths.STOP_MEASUREMENT)) {
            stopService(new Intent(this, SensorService.class));
        }
    }
}
