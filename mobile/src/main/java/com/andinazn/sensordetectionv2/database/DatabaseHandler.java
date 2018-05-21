package com.andinazn.sensordetectionv2.database;

/**
 * Created by taweechai on 4/24/15 AD.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.andinazn.sensordetectionv2.model.MonitorDataModel;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "monitorDB";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MONITOR_DATA_TABLE = "CREATE TABLE monitorData ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sensorUpdateTime TEXT, "+
                "sensorId TEXT, "+
                "userId TEXT, "+
                "accuracy TEXT, "+
                "measurementTime TEXT, "+
                "sensorVal TEXT )";

        db.execSQL(CREATE_MONITOR_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS monitorDB");
        this.onCreate(db);
    }

    public void addMonitorData(MonitorDataModel monitorData){
        Log.d("addMonitorData", monitorData.toString());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sensorUpdateTime", monitorData.getSensorUpdateTime());
        values.put("sensorId", monitorData.getSensorId());
        values.put("userId", monitorData.getUserId());
        values.put("sensorVal", monitorData.getSensorVal());
        values.put("accuracy", monitorData.getAccuracy());
        values.put("measurementTime", monitorData.getMeasurementTime());
        db.insert("monitorData", null, values);
        db.close();
    }

    public List<MonitorDataModel> getAllUserMonitorData() {
        List<MonitorDataModel> monitorDatas = new LinkedList<MonitorDataModel>();
        String query = "SELECT  * FROM " + "monitorData";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MonitorDataModel monitorData = null;
        if (cursor.moveToFirst()) {
            do {
                monitorData = new MonitorDataModel();
                monitorData.setId(Integer.parseInt(cursor.getString(0)));
                monitorData.setSensorUpdateTime(cursor.getString(1));
                monitorData.setSensorId(cursor.getString(2));
                monitorData.setUserId(cursor.getString(3));
                monitorData.setAccuracy(cursor.getString(4));
                monitorData.setMeasurementTime(cursor.getString(5));
                monitorData.setSensorVal(cursor.getString(6));
                monitorDatas.add(monitorData);
            } while (cursor.moveToNext());
        }
        Log.d("getAllMonitorData()", monitorDatas.toString());
        return monitorDatas;
    }

    public List<MonitorDataModel> getAllUserMonitorDataByLastMeasurementTime(long measurementTime) {
        List<MonitorDataModel> monitorDatas = new LinkedList<MonitorDataModel>();
        String query = "SELECT  * FROM " + "monitorData"+ " where measurementTime='" + String.valueOf(measurementTime) +"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        MonitorDataModel monitorData = null;
        if (cursor.moveToFirst()) {
            do {
                monitorData = new MonitorDataModel();
                monitorData.setId(Integer.parseInt(cursor.getString(0)));
                monitorData.setSensorUpdateTime(cursor.getString(1));
                monitorData.setSensorId(cursor.getString(2));
                monitorData.setUserId(cursor.getString(3));
                monitorData.setAccuracy(cursor.getString(4));
                monitorData.setMeasurementTime(cursor.getString(5));
                monitorData.setSensorVal(cursor.getString(6));
                monitorDatas.add(monitorData);
            } while (cursor.moveToNext());
        }
        Log.d("getLastMonitorData()", monitorDatas.toString());
        return monitorDatas;
    }

    public void deleteMeasurementDataByMeasurementTime(long measurementTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("monitorData", //table name
                "measurementTime"+" = ?",  // selections
                new String[] { String.valueOf(measurementTime) }); //selections args
        db.close();
        Log.d("db", "deleted");
    }
}