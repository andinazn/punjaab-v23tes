/*
** Created by Andina Zahra Nabilla on 10 April 2018
*
* Activity berfungsi untuk:
* 1. Deklarasi dalam mendapatkan data sensor baru dari Smartwatch
*/

package com.andinazn.sensordetectionv2;


import com.andinazn.sensordetectionv2.data.Sensor;

//1. Deklarasi dalam mendapatkan data sensor baru dari Smartwatch
public class NewSensorEvent {
    private Sensor sensor;

    public NewSensorEvent(Sensor sensor) {
        this.sensor = sensor;
    }

    public Sensor getSensor() {
        return sensor;
    }
}
