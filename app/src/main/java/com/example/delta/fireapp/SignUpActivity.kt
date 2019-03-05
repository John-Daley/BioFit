package com.example.delta.fireapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()

        btn_signup.setOnClickListener{ view ->

            attemptSignUp(view)

        }
    }


    private fun attemptSignUp(view:View){

        sign_up_email.error = null
        sign_up_password.error = null
        first_name.error = null
        last_name.error = null
        //TODO: DOB field

        val emailStr = sign_up_email.text.toString()
        val passwordStr = sign_up_password.text.toString()
        val firstNameStr = first_name.text.toString()
        val lastNameStr = last_name.text.toString()


        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            sign_up_password.error = getString(R.string.error_invalid_password)
            focusView = sign_up_password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            sign_up_email.error = getString(R.string.error_field_required)
            focusView = sign_up_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            sign_up_email.error = getString(R.string.error_invalid_email)
            focusView = sign_up_email
            cancel = true
        }


        //Detect error if name field are empty
        if (TextUtils.isEmpty(firstNameStr)) {
            first_name.error = "first name cant be empty"
            focusView = first_name
            cancel = true
        }
        if (TextUtils.isEmpty(lastNameStr)) {
            last_name.error = "last name cant be empty"
            focusView = last_name
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt sign up and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            createAccount(view, emailStr, passwordStr)


        }

    }

    private fun isEmailValid(email: String): Boolean {

        //TODO: More e-mail validation here
        return email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {

        //TODO: A lot more password validation here
        return password.length > 4
    }

        fun createAccount(view:View, email: String, password: String) {

            showMessage(view, "Creating Account...")

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            finish() //prevents new user from going back to registration activity
                            val intent = Intent(this, MainActivity::class.java)
                            //TODO: send current user info to MainActivity
                            //intent.putExtra("id", mAuth.currentUser?.email)
                            startActivity(intent)


                } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            showMessage(view, "Error: account couldn't be created")


                }


            }

        }

    //to display success or error
    private fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }


    companion object {

        const val TAG = "SignUpActivity"
    }
}
