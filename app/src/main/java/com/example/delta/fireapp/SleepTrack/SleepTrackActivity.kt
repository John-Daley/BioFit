package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.example.delta.fireapp.R
import com.hsalf.smilerating.SmileRating
import java.util.*
import java.util.concurrent.TimeUnit

class SleepTrackActivity : AppCompatActivity() {

    lateinit var startTime: Date
    lateinit var stopTime: Date
    lateinit var myDialog: Dialog
    lateinit var rating: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_track)
        myDialog = Dialog(this)
    }

    fun startButton(view: View){
    startTime = Calendar.getInstance().time

}

    fun stopButton(view: View) {
        stopTime = Calendar.getInstance().time
        var timeSlept = stopTime.time - startTime.time
        var outPut = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeSlept),
                TimeUnit.MILLISECONDS.toMinutes(timeSlept) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeSlept)),
                TimeUnit.MILLISECONDS.toSeconds(timeSlept) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSlept)))
        myDialog.setContentView(R.layout.popup_smiley_rating)
        var tvTimeSlept = myDialog.findViewById<TextView>(R.id.textView4)
        var smileRating = myDialog.findViewById<SmileRating>(R.id.smile_rating)

        smileRating.setOnRatingSelectedListener { level, reselected ->
            when (level) {
                1 -> rating = "TERRIBLE"
                2 -> rating = "BAD"
                3 -> rating = "OKAY"
                4 -> rating = "GOOD"
                5 -> rating = "GREAT"
            }
            myDialog.dismiss()
            val intent = Intent(this, SleepDetailActivity::class.java)
            intent.putExtra("stuff", outPut)
            intent.putExtra("rating", rating)
            startActivity(intent)
        }
        tvTimeSlept.text = outPut
        myDialog.show()

    }
}
