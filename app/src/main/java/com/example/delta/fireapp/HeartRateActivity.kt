package com.example.delta.fireapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView

class HeartRateActivity : AppCompatActivity(), SensorEventListener {
    val sensorManager: SensorManager? = null
    val heartRateSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)
    }

    fun onClickBPMstartButton(view: View){

        //Manager enables sensor data as fast as possible
        if(sensorManager != null && heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST)

        }

    }

    fun onClickStopButton(view: View){
        //Disables sensor
        sensorManager?.unregisterListener(this, heartRateSensor)

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        Log.d("acc", "onAccuracyChanged:" + p1)
        println(p1)

    }

    //When sensor is activated returns float[] data then cast to Int to be displayed
    override fun onSensorChanged(p0: SensorEvent?) {
        var heartRateValue = p0!!.values[0]
        var heartRate = Math.round(heartRateValue)
        val beatsPerMinuteDisplay = findViewById<TextView>(R.id.beatsPerMinuteDisplay)

        beatsPerMinuteDisplay.setText(Integer.toString(heartRate))
    }

    //Disables sensor when paused
    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)

    }
}
