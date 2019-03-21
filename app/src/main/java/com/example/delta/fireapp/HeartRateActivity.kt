package com.example.delta.fireapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.DataModel.HeartRateData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_heart_rate.*
import java.text.SimpleDateFormat

class HeartRateActivity : AppCompatActivity(), SensorEventListener {
    var sensorManager: SensorManager? = null
    var heartRateSensor: Sensor? = null

    // --DB Ops
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var currentUserUID: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heart_rate)

        //Get current signed in user
        mAuth = FirebaseAuth.getInstance()
        currentUserUID = mAuth.currentUser?.uid!!


        //general db initialization
        dbRef = FirebaseDatabase.getInstance().reference

    }

    fun onClickBPMstartButton(view: View){
        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        heartRateSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        //Manager enables sensor data as fast as possible
        if(heartRateSensor == null)
            println("Sensor not working")
        else
            sensorManager?.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_FASTEST)
            println("Sensor......")


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
        println(" BPM: " + heartRate)
    }

    //Disables sensor when paused
    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)

    }



    private fun saveHeartRateData(view:View, bpm: Int) {


        val timestamp =  System.currentTimeMillis()


        //store in a data object
        val heartRateData = HeartRateData(bpm, timestamp , currentUserUID)

        //perform write operation on database
        var heartRateDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("HeartRateData")
        //generate unique id
        val key = heartRateDbRef.push().key
        dbRef.child("HeartRateData").child(key).setValue(heartRateData)


        Toast.makeText(this, "Heart Rate saved", Toast.LENGTH_SHORT).show()

    }

}
