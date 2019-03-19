package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.DataModel.SleepData
import com.example.delta.fireapp.DataModel.UserData
import com.example.delta.fireapp.MainActivity
import com.example.delta.fireapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.hsalf.smilerating.SmileRating
import kotlinx.android.synthetic.main.activity_sleep_track.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SleepTrackActivity : AppCompatActivity() {

    //These variables can be stored in a SleepData object
    //substitute with: lateinit var sleepData: SleepData
    lateinit var startTime: Date
    lateinit var stopTime: Date
    lateinit var myDialog: Dialog
    lateinit var rating: String

    // --DB Ops
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var currentUserUID: String

    var userSleepDataArray = arrayListOf<SleepData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleep_track)
        myDialog = Dialog(this)

        //Get current signed in user
        mAuth = FirebaseAuth.getInstance()
        currentUserUID = mAuth.currentUser?.uid!!


        //general db initialization
        dbRef = FirebaseDatabase.getInstance().reference


        test()
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


    private fun saveSleepData(sleepData: SleepData){

        var sleepDbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("SleepData")
        val key = sleepDbRef.push().key
        dbRef.child("SleepData").child(key).setValue(sleepData)

        Toast.makeText(this, "Sleep Data saved", Toast.LENGTH_SHORT).show()

    }

    private fun readSleepData(){

        val now = System.currentTimeMillis()

        val query = dbRef.child("SleepData")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (sleepData in dataSnapshot.children) {
                    // TODO: handle the post


                    var data = sleepData.getValue(SleepData::class.java)

                    if (data!!.userId.equals(currentUserUID)) {

                        println(data.toString())

                        if (!data.end.equals(0)) {

                            var sleepReference = FirebaseDatabase.getInstance().reference.child("SleepData").child(sleepData.key)

                            data.end = now
                            data.rating = "Good"

                            println(data.toString())

                            sleepReference.setValue(data)

                        }


                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }

    private fun updateArray(){

        val query = dbRef.child("SleepData")

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (sleepData in dataSnapshot.children) {

                    var data = sleepData.getValue(SleepData::class.java)

                    if (data!!.userId.equals(currentUserUID)) {

                        userSleepDataArray.add(data)
                        textView_test.text = userSleepDataArray.size.toString()

                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }

    private fun test(){
        toggleButton_test.setOnCheckedChangeListener{_, isChecked ->
            if(isChecked){
                //started sleeping

                val data = SleepData(System.currentTimeMillis(),0,"", currentUserUID)

                saveSleepData(data)

                userSleepDataArray.clear()

            }else{
                //woke up
                readSleepData()
                updateArray()



            }
        }
    }

    private fun updateSleepUI(sleepData:SleepData){

    }


    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }

}
