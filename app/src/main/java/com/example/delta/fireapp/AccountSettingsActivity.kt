package com.example.delta.fireapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.delta.fireapp.DataModel.UserData
import com.example.delta.fireapp.SleepTrack.SleepTrackActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_account_settings.*

class AccountSettingsActivity : AppCompatActivity() {

    // --DB Ops
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private lateinit var sleepDataDbRef: DatabaseReference
    private lateinit var currentUserUID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)

        //Get current signed in user
        mAuth = FirebaseAuth.getInstance()
        currentUserUID = mAuth.currentUser?.uid!!


        //general db initialization
        dbRef = FirebaseDatabase.getInstance().reference
        sleepDataDbRef = FirebaseDatabase.getInstance().getReference("SleepData")

        initSettings()

    }


    fun deleteAllSleepData(view: View) {

        val query = dbRef.child("SleepData").orderByChild("userId").equalTo(currentUserUID)

        //the query should return a list of SleepData Objects that have the current user's ID
        //loop through results with for loop
        query.addChildEventListener(object : ChildEventListener {


            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key!!)

                //delete node
                dataSnapshot.ref.setValue(null)


            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException())
                Toast.makeText(this@AccountSettingsActivity, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show()
            }


        })


        Toast.makeText(this, "All Sleep Data has been deleted", Toast.LENGTH_SHORT).show()
    }

    fun deleteAccount(view: View) {
        Toast.makeText(this, "Just Kiddin', this has not yet been implemented", Toast.LENGTH_LONG).show()
        //TODO this

    }

    private fun initSettings() {

        val userListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                //get user info and store it in a UserData object
                val userData = dataSnapshot!!.getValue(UserData::class.java)

                //Update UI
                settings_email.text = userData!!.email

            }

            override fun onCancelled(databaseError: DatabaseError?) {

                Log.w(MainActivity.TAG, "loadUser: Cancelled", databaseError?.toException())
            }

        }

        //Listen for changes only once (when this method is called)
        val userReference = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUID)
        userReference.addListenerForSingleValueEvent(userListener)
    }


    companion object {
        const val TAG = "SettingsActivity"
    }
}
