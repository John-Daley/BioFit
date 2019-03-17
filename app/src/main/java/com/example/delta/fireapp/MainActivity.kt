package com.example.delta.fireapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.delta.fireapp.DataModel.UserData
import com.example.delta.fireapp.SleepTrack.SleepTrackActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //Get current signed in user
        mAuth = FirebaseAuth.getInstance()
        val currentUserUID = mAuth.currentUser?.uid

        //Initialize database based on current user
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUID)

        //Get user info
        initUserProfile()


    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    /*
    Initialize user profile by loading the user data from database
     */
    private fun initUserProfile(){

        //To read values from database
        //We create an anon inner class for the interface ValueEventListener
        val userListener = object: ValueEventListener{

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                //get user info and store it in a UserData object
                val userData = dataSnapshot!!.getValue(UserData::class.java)

                //format full name string
                val firstName = userData!!.firstName
                val lastName = userData.lastName
                val fullName = "$firstName $lastName"

                //Update UI
                nav_user_name.text = fullName
                nav_user_email.text = userData.email

            }

            override fun onCancelled(databaseError: DatabaseError?) {

                Log.w(TAG, "loadUser: Cancelled", databaseError?.toException())
            }

        }

        //Listen for changes only once (when this method is called)
        userReference.addListenerForSingleValueEvent(userListener)
    }

    fun goToHeartRateView(view : View){
        val intent = Intent(this,HeartRateActivity::class.java)
        startActivity(intent)
    }

    fun goToRunSettingsView(view: View){
        val intent = Intent(this,RunSettingsActivity::class.java)
        startActivity(intent)
    }

    fun goToSleepTrack(view: View){
        val intent = Intent(this, SleepTrackActivity::class.java)
        startActivity(intent)
    }



    companion object {

        const val TAG = "MainActivity"

    }
}
