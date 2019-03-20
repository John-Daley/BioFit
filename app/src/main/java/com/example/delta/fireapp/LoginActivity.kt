package com.example.delta.fireapp

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Set up the login form.

        mAuth = FirebaseAuth.getInstance()

        //detects whether enter was pressed
        sign_in_password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                //attemptLogin(view)
                //TODO: log in when enter pressed
                return@OnEditorActionListener true
            }
            false
        })

        //button functionality
        btn_email_log_in.setOnClickListener { view ->

            attemptLogin(view)

        }

        btn_email_sign_up.setOnClickListener{

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        tv_forgot_password.setOnClickListener {

            startActivity(Intent(this, RetrievePasswordActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()

        //TODO: Verify that a user is not already logged in
        // (because once logged in, user can still press back button and end up here)
        var currentUser = mAuth.currentUser
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin(view:View) {
        /*if (mAuth != null) {
            return
        }*/

        // Reset errors.
        sign_in_email.error = null
        sign_in_password.error = null

        // Store values at the time of the login attempt.
        val emailStr = sign_in_email.text.toString()
        val passwordStr = sign_in_password.text.toString()

        var cancel = false
        var focusView: View? = null

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordStr) && !isPasswordValid(passwordStr)) {
            sign_in_password.error = getString(R.string.error_invalid_password)
            focusView = sign_in_password
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            sign_in_email.error = getString(R.string.error_field_required)
            focusView = sign_in_email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            sign_in_email.error = getString(R.string.error_invalid_email)
            focusView = sign_in_email
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {

            // perform the user login attempt.
            signIn(view,emailStr, passwordStr)


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



    /**
     * Firebase Log-In Authentication
     * (Already handles background task, so no need to implement a thread handler)
     */
    private fun signIn(view: View,email: String, password: String){
        showMessage(view,"Authenticating...")

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.d(TAG, "Login successful")
                finish() //this prevents the user going back to log in page once logged in
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("CURRENT_USER", mAuth.currentUser?.email)
                startActivity(intent)

            }else{
                Log.d(TAG, "Login Unsuccessful")
                showMessage(view,"Error: ${task.exception?.message}")
            }
        }

    }

    /**
     * Shows that an authentication is in progress or unsuccessful
     */
    private fun showMessage(view:View, message: String){
        Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
    }

    companion object {


        const val TAG = "LoginActivity"

    }
}

