package com.andinazn.sensordetectionv2.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by taweechai on 4/25/15 AD.
 */
public class MonitorDataModel {
    private int id;
    private String sensorId;
    private String userId;
    private String sensorVal;
    private String sensorUpdateTime;
    private String measurementTime;
    private String accuracy;

    public MonitorDataModel () {

    }

    public  MonitorDataModel (int id, String sensorId, String userId, String sensorVal, String sensorUpdateTime, String accuracy, String measurementTime) {
        this.id = id;
        this.sensorId = sensorId;
        this.userId = userId;
        this.sensorVal = sensorVal;
        this.sensorUpdateTime = sensorUpdateTime;
        this.accuracy = accuracy;
        this.measurementTime = measurementTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSensorVal() {
        return sensorVal;
    }

    public void setSensorVal(String sensorVal) {
        this.sensorVal = sensorVal;
    }

    public String getSensorUpdateTime() {
        return sensorUpdateTime;
    }

    public void setSensorUpdateTime(String sensorUpdateTime) {
        this.sensorUpdateTime = sensorUpdateTime;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public String getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(String measurementTime) {
        this.measurementTime = measurementTime;
    }

    @Override
    public String toString() {
        return "MonitorData [id=" + id + ", userId=" + userId + ", sensorId=" + sensorId+ ", sensorVal=" + sensorVal + ", sensorUpdateTime=" + getDate(Long.getLong(sensorUpdateTime, 1)/1000000L) + ", accuracy=" + accuracy+ ", measurementTime=" + measurementTime
                + "]";
    }

    private String getDate(long timestamp){
        try{
            DateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
            Date netDate = (new Date(timestamp));
            return sdf.format(netDate);
        }
        catch(Exception ex){
            return "00:00";
        }
    }
}
