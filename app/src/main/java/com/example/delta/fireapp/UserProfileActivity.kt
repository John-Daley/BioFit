package com.example.delta.fireapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.delta.fireapp.DataModel.UserData

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.text.ParseException
import java.text.SimpleDateFormat

class UserProfileActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var userReference: DatabaseReference

    private var isEditable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        //Get current signed in user
        mAuth = FirebaseAuth.getInstance()
        val currentUserUID = mAuth.currentUser?.uid

        //Initialize database based on current user
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(currentUserUID)

        updateUI()



    }

    //Toggles button between a save and edit functionality and updates profile accordingly
    private fun updateUI(){

        if(isEditable){

            editEnable()

            btn_edit_save_profile.text = getString(R.string.profile_save_button)

            btn_edit_save_profile.setOnClickListener { view ->

                onClickSave(view)

            }




        }else{

            readUserData()

            editDisable()

            btn_edit_save_profile.text = getString(R.string.profile_edit_button)

            btn_edit_save_profile.setOnClickListener { view ->

                onClickEdit(view)

            }
        }



    }

    //Read from database, store in a UserData object, update fields in profile by calling updateUserData()
    private fun readUserData() {

        //To read values from database
        //We create an anon inner class for the interface ValueEventListener
        val userListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot?) {

                //get user info and store it in a UserData object
                val userDataSnapshot = dataSnapshot!!.getValue(UserData::class.java)

                val data = userDataSnapshot!!

                updateUserData(data)

            }

            override fun onCancelled(databaseError: DatabaseError?) {

                Log.w(MainActivity.TAG, "loadUser: Cancelled", databaseError?.toException())
            }

        }

        //constantly listen for changes in DB
        userReference.addValueEventListener(userListener)

    }

    //updates fields on profile with the data given in parameter
    private fun updateUserData(userData:UserData) {

        profile_email.setText(userData.email)
        profile_first_name.setText(userData.firstName)
        profile_last_name.setText(userData.lastName)
        spinner_gender.prompt = userData.gender
        profile_height.setText(userData.height.toString())
        profile_weight.setText(userData.weight.toString())
        profile_DOB.setText(userData.dateOfBirth)

    }

    private fun onClickEdit(view: View){

        isEditable = true

        updateUI()

    }

    private fun onClickSave(view: View){


        if(passValidation()){

            isEditable = false

            writeUserData()


        }

        updateUI()

    }

    private fun writeUserData(){

        val email = profile_email.text.toString()
        val firstName = profile_first_name.text.toString().trim()
        val lastName = profile_last_name.text.toString().trim()
        val gender = spinner_gender.selectedItem.toString()
        val height = Integer.parseInt(profile_height.text.toString())
        val weight = profile_weight.text.toString().toFloat()
        val dob = profile_DOB.text.toString()

        val newUserData = UserData(email,firstName,lastName,dob,gender,height,weight)

        userReference.setValue(newUserData)



    }

    private fun passValidation():Boolean{

        profile_first_name.error = null
        profile_last_name.error = null
        profile_height.error= null
        profile_weight.error = null
        profile_DOB.error = null

        var isValid = true
        var focusView: View? =null


        val firstName = profile_first_name.text.toString().trim()
        val lastName = profile_last_name.text.toString().trim()
        val heightStr = profile_height.text.toString()
        val weightStr = profile_weight.text.toString()
        val dateOfBirthStr = profile_DOB.text.toString()

        if (TextUtils.isEmpty(firstName)){
            profile_first_name.error = getString(R.string.invalid_empty_string)
            focusView = profile_first_name
            isValid = false

        }

        if (TextUtils.isEmpty(lastName)){
            profile_last_name.error = getString(R.string.invalid_empty_string)
            focusView = profile_last_name
            isValid = false

        }

        if(TextUtils.isEmpty(heightStr)){
            profile_height.error = getString(R.string.invalid_empty_string)
            focusView = profile_height
            isValid = false
        }

        if(TextUtils.isEmpty(weightStr)){
            profile_weight.error =getString(R.string.invalid_empty_string)
            focusView = profile_weight
            isValid = false
        }


        if(TextUtils.isEmpty(dateOfBirthStr)){
            profile_DOB.error =getString(R.string.invalid_empty_string)
            focusView = profile_DOB
            isValid = false
        }

        if(!isValidDate(dateOfBirthStr)){

            profile_DOB.error =getString(R.string.date_format_dd_mm_yyyy)
            focusView = profile_DOB
            isValid = false

        }


            if(!isValid){
                focusView?.requestFocus()
            }

            return isValid


    }

    private fun isValidDate(dateStr :String):Boolean{

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        dateFormat.isLenient = false
        try{
            dateFormat.parse(dateStr)

        }catch(e: ParseException){
            return false
        }
        return true
    }



    private fun editDisable(){
        profile_email.isEnabled =false
        profile_first_name.isEnabled =false
        profile_last_name.isEnabled =false
        spinner_gender.isEnabled =false
        profile_height.isEnabled =false
        profile_weight.isEnabled =false
        profile_DOB.isEnabled = false

    }

    private fun editEnable(){

        profile_first_name.isEnabled =true
        profile_last_name.isEnabled =true
        spinner_gender.isEnabled =true
        profile_height.isEnabled =true
        profile_weight.isEnabled =true
        profile_DOB.isEnabled = true
    }


}
