package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.DataModel.SleepData
import com.example.delta.fireapp.DataModel.SleepDataCallBack
import com.example.delta.fireapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.hsalf.smilerating.SmileRating
import kotlinx.android.synthetic.main.activity_sleep_track.*
import kotlinx.android.synthetic.main.popup_smiley_rating.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback

class SleepTrackActivity : AppCompatActivity(), SleepDataCallBack {

    //These variables can be stored in a SleepData object
    //substitute with: lateinit var sleepData: SleepData
    lateinit var startTime: Date
    lateinit var stopTime: Date
    lateinit var myDialog: Dialog
    lateinit var rating: String

    // --DB Ops
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var sleepDataDbRef: DatabaseReference
    private lateinit var currentUserUID: String
    private var currentKey = "0"
    var aux = 1

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
        sleepDataDbRef = FirebaseDatabase.getInstance().getReference("SleepData")

        updateArray()
        test()
    }

    fun startButton(view: View){
        val intent = Intent(this, SleepDetailActivity::class.java)
        var bundle = Bundle()
        bundle.putSerializable("sleepData", userSleepDataArray)
        intent.putExtras(bundle)
        startActivity(intent)

}

    fun testText(){
        var start: Long = userSleepDataArray[0].start
        var end: Long = userSleepDataArray[0].end
        var timeSlept = end-start
        var outPut = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(timeSlept),
                TimeUnit.MILLISECONDS.toMinutes(timeSlept) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeSlept)),
                TimeUnit.MILLISECONDS.toSeconds(timeSlept) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeSlept)))
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


    private fun saveSleepData(){

        currentKey = sleepDataDbRef.push().key

        dbRef.child("SleepData").child(currentKey).child("start").setValue(ServerValue.TIMESTAMP)
        dbRef.child("SleepData").child(currentKey).child("userId").setValue(currentUserUID)

        //Toast.makeText(this, "Sleep Sesh Started", Toast.LENGTH_SHORT).show()

    }

    private fun updateSleepData(newRating:String){


        dbRef.child("SleepData").child(currentKey).child("end").setValue(ServerValue.TIMESTAMP)
        dbRef.child("SleepData").child(currentKey).child("rating").setValue(newRating)

        //Toast.makeText(this, "Sleep Sesh Stopped", Toast.LENGTH_SHORT).show()

    }


    private fun updateArray(){

        //find all object with this user's ID

        val query = dbRef.child("SleepData").orderByChild("userId").equalTo(currentUserUID)

        //the query should return a list of SleepData Objects that have the current user's ID
        //loop through results with for loop
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userSleepDataArray.clear()
                for (sleepDataSnapshot in dataSnapshot.children) {
                    val data = sleepDataSnapshot.getValue(SleepData::class.java)

                    println("--------------------------->retrieved child has this ID "+data!!.userId)

                    if (data.userId.equals(currentUserUID)) {

                        println("-------------------------matches")
                        println("---------------object: " + data.start)

                        userSleepDataArray.add(data)
                        println("-----------------This is the size"+ userSleepDataArray.size)

                    }
                }

                //onCallBack(userSleepDataArray)

                println("_-------------- array size from within updateArray() " + userSleepDataArray.size.toString())
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

                saveSleepData()
                aux++

                //Toast.makeText(this, "WORKING BOI", Toast.LENGTH_SHORT).show()



            }else{
                //woke up

                myDialog.setContentView(R.layout.popup_smiley_rating)
                var smileRating = myDialog.findViewById<SmileRating>(R.id.smile_rating)
                smileRating.setOnRatingSelectedListener { level, reselected ->
                    when (level) {
                        1 -> updateSleepData("TERRIBLE")
                        2 -> updateSleepData("BAD")
                        3 -> updateSleepData("OKAY")
                        4 -> updateSleepData("GOOD")
                        5 -> updateSleepData("GREAT")
                    }
                    myDialog.dismiss()
                }
                updateArray()
                println("_-------------- array size after update " + userSleepDataArray.size.toString())
                testText()
                myDialog.show()

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
