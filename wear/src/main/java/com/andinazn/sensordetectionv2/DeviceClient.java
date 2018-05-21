/*
** Created by Andina Zahra Nabilla on 10 April 2018
*
* Activity berfungsi untuk:
* 1. Membuat GoogleApiClient instance untuk terhubung pada service Wearable Api
* 2. Menghubungkan client dengan Google Play Services
* 3. Pemetaan Map Data Untul Sinkronisasi Antara Wear dan Handheld
* 4. Flag data urgency
 */

package com.andinazn.sensordetectionv2;

import android.content.Context;
import android.util.Log;
import android.util.SparseLongArray;

import com.github.pocmo.sensordashboard.shared.DataMapKeys;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Arrays;
import java.util.Set;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeviceClient {
    private static final String TAG = "SensorDashboard/DeviceClient";
    private static final int CLIENT_CONNECTION_TIMEOUT = 15000;

    private static final String CAPABILITY_SHOW_FALL_STATUS = "receive_fall_status";

    public static final String MESSAGE_PATH_SHOW_FALL_STATUS = "/receive_fall_status";

    public static DeviceClient instance;

    public static DeviceClient getInstance(Context context) {
        if (instance == null) {
            instance = new DeviceClient(context.getApplicationContext());
        }

        return instance;
    }

    private Context context;
    private GoogleApiClient googleApiClient;
    private ExecutorService executorService;
    private int filterId;

    private SparseLongArray lastSensorData;

    private DeviceClient(Context context) {
        this.context = context;

        //1. Membuat GoogleApiClient instance untuk terhubung pada service wearable
        googleApiClient = new GoogleApiClient.Builder(context).addApi(Wearable.API).build();

        Wearable.CapabilityApi.getCapability(googleApiClient,
                CAPABILITY_SHOW_FALL_STATUS,
                CapabilityApi.FILTER_REACHABLE).setResultCallback(new ResultCallbacks<CapabilityApi.GetCapabilityResult>() {
            @Override
            public void onSuccess(CapabilityApi.GetCapabilityResult getCapabilityResult) {
                updateStateCapability(getCapabilityResult.getCapability());
            }

            @Override
            public void onFailure(Status status) {
                updateStateCapability(null);
            }
        });

        CapabilityApi.CapabilityListener stateCapabilityListener =
                new CapabilityApi.CapabilityListener() {
                    @Override
                    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
                        updateStateCapability(capabilityInfo);
                    }
                };

        Wearable.CapabilityApi.addCapabilityListener(
                googleApiClient,
                stateCapabilityListener,
                CAPABILITY_SHOW_FALL_STATUS);

        executorService = Executors.newCachedThreadPool();
        lastSensorData = new SparseLongArray();

    }

        private String hostNodeId = null;

        private CapabilityInfo stateCapabilityInfo = null;
        private CapabilityInfo dataCapabilityInfo = null;

    private void updateStateCapability(CapabilityInfo capabilityInfo) {
        stateCapabilityInfo = capabilityInfo;

        updateHost();
    }

    private void updateHost() {
        if ((stateCapabilityInfo != null) && (dataCapabilityInfo != null)) {
            hostNodeId = pickBestNodeId(stateCapabilityInfo.getNodes(), dataCapabilityInfo.getNodes());
        }
    }

    private String pickBestNodeId(Set< Node > stateCapableNodes, Set<Node> dataCapableNodes) {
        String bestNodeId = null;
        // Find a nearby node or pick one arbitrarily
        for (Node stateCapableNode : stateCapableNodes) {
            if (stateCapableNode.isNearby()) {
                for (Node dataCapableNode : dataCapableNodes) {
                    if ((dataCapableNode.isNearby()) && (dataCapableNode.getId().equals(stateCapableNode.getId())))
                        return dataCapableNode.getId();
                }
            } else if (bestNodeId == null) {
                for (Node dataCapableNode : dataCapableNodes) {
                    if (dataCapableNode.getId().equals(stateCapableNode.getId()))
                        bestNodeId = dataCapableNode.getId();
                }
            }
        }
        return bestNodeId;
    }

    public void sendFallStatus(boolean data) {
        if (hostNodeId != null) {
            Log.d("Send","Status: "+data);
            ByteBuffer b = ByteBuffer.allocate(1);
            b.put((byte) ((data)?1:0));
            Wearable.MessageApi.sendMessage(googleApiClient, hostNodeId,
                    MESSAGE_PATH_SHOW_FALL_STATUS, b.array());
        }
    }

    public void setSensorFilter(int filterId) {
        Log.d(TAG, "Now filtering by sensor: " + filterId);

        this.filterId = filterId;
    }

    public void sendSensorData(final int sensorType, final int accuracy, final long timestamp, final float[] values) {
        long t = System.currentTimeMillis();

        long lastTimestamp = lastSensorData.get(sensorType);
        long timeAgo = t - lastTimestamp;

        if (lastTimestamp != 0) {
            if (filterId == sensorType && timeAgo < 100) {
                return;
            }

            if (filterId != sensorType && timeAgo < 3000) {
                return;
            }
        }

        lastSensorData.put(sensorType, t);

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                sendSensorDataInBackground(sensorType, accuracy, timestamp, values);
            }
        });
    }

    private void sendSensorDataInBackground(int sensorType, int accuracy, long timestamp, float[] values) {
        if (sensorType == filterId) {
            Log.i(TAG, "Sensor " + sensorType + " = " + Arrays.toString(values));
        } else {
            Log.d(TAG, "Sensor " + sensorType + " = " + Arrays.toString(values));
        }

        //3. Pemetaan Map Data Untul Sinkronisasi Antara Wear dan Handheld
        PutDataMapRequest dataMap = PutDataMapRequest.create("/sensors/" + sensorType);

        dataMap.getDataMap().putInt(DataMapKeys.ACCURACY, accuracy);
        dataMap.getDataMap().putLong(DataMapKeys.TIMESTAMP, timestamp);
        dataMap.getDataMap().putFloatArray(DataMapKeys.VALUES, values);

        //4. Flag data urgency
        PutDataRequest putDataRequest = dataMap.asPutDataRequest().setUrgent();
        send(putDataRequest);
    }

    private boolean validateConnection() {
        if (googleApiClient.isConnected()) {
            return true;
        }

        //2. Menghubungkan client dengan Google Play Services
        ConnectionResult result = googleApiClient.blockingConnect(CLIENT_CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);

        return result.isSuccess();
    }

    private void send(PutDataRequest putDataRequest) {
        if (validateConnection()) {
            Wearable.DataApi.putDataItem(googleApiClient, putDataRequest).setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                @Override
                public void onResult(DataApi.DataItemResult dataItemResult) {
                    Log.v(TAG, "Sending sensor data: " + dataItemResult.getStatus().isSuccess());
                }
            });
        }
    }
}
