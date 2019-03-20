package com.example.delta.fireapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class Pedometer : AppCompatActivity() {
    var running = false
    var sensorManager: SensorManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedometer)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        override fun onResume() {
            super.onResume()
            running = true
            var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

            if(stepsSensor == null){
                Toast.makeText(this, "No step Counter !", Toast.LENGTH_SHORT).show()
            }else {
                sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
            }
        }

        override fun onPause() {
            super.onPause()
            running = false
            sensorManager?.unregisterListener(this)
        }

        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if(running){
                textView2.setText(" "  + event.values[0])

            }
    }
}
