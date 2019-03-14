package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.R
import com.hsalf.smilerating.SmileRating
import java.util.*
import java.util.concurrent.TimeUnit

class SleepDetailActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_detail)

        val stuff:String = intent.getStringExtra("stuff")
        val rating: String = intent.getStringExtra("rating")
        var tv = findViewById<TextView>(R.id.sleepTime)
        var tvRating = findViewById<TextView>(R.id.textView10)
        tv.text = stuff
        tvRating.text = rating

    }



}
