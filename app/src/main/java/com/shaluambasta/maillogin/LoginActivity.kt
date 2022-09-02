package com.shaluambasta.maillogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser


    private lateinit var etEmailIdLogin: TextInputLayout
    private lateinit var etPasswordLogin: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private lateinit var textviewReferSignUp: MaterialTextView

    private val TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        auth.currentUser.also {
            if (it != null) {
                user = it
            }
        }


        etEmailIdLogin = findViewById(R.id.et_email_id_login)
        etPasswordLogin = findViewById(R.id.et_password_login)
        btnLogin = findViewById(R.id.btn_login)
        textviewReferSignUp = findViewById(R.id.textview_refer_sign_up)


        btnLogin.setOnClickListener {
            logInUser()
        }


        textviewReferSignUp.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun logInUser() {

        if (etEmailIdLogin.editText?.text.toString().isEmpty()) {
            etEmailIdLogin.error = "Please enter your Email id"
            etEmailIdLogin.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmailIdLogin.editText?.text.toString()).matches()) {
            etEmailIdLogin.error = "Please enter valid Email"
            etEmailIdLogin.requestFocus()
            return
        }

        if (etPasswordLogin.editText?.text.toString().isEmpty()) {
            etPasswordLogin.error = "Please enter your Password"
            etPasswordLogin.requestFocus()
            return
        }

        if (etPasswordLogin.editText?.text.toString().length < 6) {
            etPasswordLogin.error = "Password is too short\nMust be length between 6-12."
            etPasswordLogin.requestFocus()
            return
        }

        if (etPasswordLogin.editText?.text.toString().length > 12) {
            etPasswordLogin.error = "Password is much long\nMust be length between 6-12."
            etPasswordLogin.requestFocus()
            return
        }

        if (notEmpty()) {

            auth.signInWithEmailAndPassword(
                etEmailIdLogin.editText?.text.toString(),
                etPasswordLogin.editText?.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        user = auth.currentUser!!

                        Toast.makeText(this, "Login Successfully!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        Toast.makeText(this, "Welcome Back!", Toast.LENGTH_SHORT).show()
        finish()
    }


    private fun notEmpty(): Boolean {
        return (etEmailIdLogin.editText?.text.toString().isNotEmpty()
                && etPasswordLogin.editText?.text.toString().isNotEmpty())
    }

}