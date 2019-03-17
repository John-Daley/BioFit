package com.example.delta.fireapp.RunTrack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.delta.fireapp.R
import kotlinx.android.synthetic.main.activity_run_settings.*

class RunSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_settings)
    }
fun getTheRunDuration(view: View){
    val hours: Int = hoursText.text.toString().toInt().times(60)
    val mins: Int= minutesText.text.toString().toInt()
    val TotalTime =  hours + mins
    val intent = Intent(this,MapRunningActivity::class.java)
   intent.putExtra("time",TotalTime)
startActivity(intent)

}

}
