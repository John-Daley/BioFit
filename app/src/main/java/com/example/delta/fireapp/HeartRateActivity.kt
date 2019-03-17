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
import android.widget.Toast
import com.example.delta.fireapp.DataModel.HeartRateData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_heart_rate.*
import java.text.SimpleDateFormat

class HeartRateActivity : AppCompatActivity(), SensorEventListener {
    val sensorManager: SensorManager? = null
    val heartRateSensor: Sensor? = null

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


    /*
    Save the calculated BPM on database, based on server's time rather than "subjective" device time
    The ServerValue.TIMESTAMP saves a MutableMap (key,value) when saving
    Note: This value is calculated after WRITE operation and is actually retrieved as data type Long
     */
    private fun saveHeartRateData(view:View, bpm: Int) {

        val timestamp = ServerValue.TIMESTAMP

        //store in a data object
        val heartRateData: HeartRateData = HeartRateData(bpm, timestamp , currentUserUID)

        //perform write operation on database
        var HeartRateDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("HeartRateData")
        //generate unique id
        val key = HeartRateDbRef.push().getKey()
        dbRef.child("HeartRateData").child(key).setValue(heartRateData)


        Toast.makeText(this, "Heart Rate saved", Toast.LENGTH_SHORT).show()

    }

}
