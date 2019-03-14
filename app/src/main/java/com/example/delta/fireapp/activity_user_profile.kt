package com.example.delta.fireapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.delta.fireapp.SleepTrack.SleepTrackActivity

class activity_user_profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }
public fun goToHeartRateView(view : View){
    val intent = Intent(this,HeartRateActivity::class.java)
    startActivity(intent)
}
    fun goToSleepTrack(view: View){
        val intent = Intent(this, SleepTrackActivity::class.java)
        startActivity(intent)
    }


}
