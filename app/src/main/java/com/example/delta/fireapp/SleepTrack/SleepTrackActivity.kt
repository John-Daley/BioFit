package com.example.delta.fireapp.SleepTrack

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.delta.fireapp.DataModel.SleepData
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
    var isStarted: Boolean = false

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
        //test()
    }

    fun recordingsButton(view: View){
        val intent = Intent(this, SleepDetailActivity::class.java)
        var bundle = Bundle()
        bundle.putSerializable("sleepData", userSleepDataArray)
        intent.putExtras(bundle)
        startActivity(intent)

}


    private fun saveSleepData(){


        currentKey = sleepDataDbRef.push().key

        dbRef.child("SleepData").child(currentKey).child("start").setValue(ServerValue.TIMESTAMP)
        dbRef.child("SleepData").child(currentKey).child("userId").setValue(currentUserUID)

        Toast.makeText(this, "Sleep Sesh Started", Toast.LENGTH_SHORT).show()

    }

    private fun updateSleepData(newRating:String){


        dbRef.child("SleepData").child(currentKey).child("end").setValue(ServerValue.TIMESTAMP)
        dbRef.child("SleepData").child(currentKey).child("rating").setValue(newRating)

        Toast.makeText(this, "Sleep Sesh Stopped", Toast.LENGTH_SHORT).show()
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
                      
                    }
                }

                println("_-------------- array size from within updateArray() " + userSleepDataArray.size.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        })

    }

    private fun updateUI(){

        if(isStarted) {
            textView_toggle.text = "Stop"

        toggleCard.setOnClickListener{view ->
            onClickStop(view)
        }

        }else{
            textView_toggle.text = "Start"

            toggleCard.setOnClickListener{view ->
                onClickStart(view)
            }
        }


    }

    fun onClickStart(view: View){
        isStarted = true
        saveSleepData()
        updateUI()


    }

    fun onClickStop(view: View){
        isStarted = false
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
        updateUI()
        myDialog.show()
    }

   /*fun test(view: View){

        if(isStarted){
            saveSleepData()

            isStarted = true

            textView_toggle.text = "stop"

            }else{
                //woke up

            textView_toggle.text = "start"

            toggleCard.setOnClickListener({
                onClickStart(view)
            })


        }
    }*/

}
