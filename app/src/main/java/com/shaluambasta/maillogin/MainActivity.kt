package com.shaluambasta.maillogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.shaluambasta.maillogin.utils.Name

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    private lateinit var textviewWelcome: MaterialTextView
    private lateinit var btnLogout: MaterialButton

    private var name: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        auth.currentUser.also {
            if (it != null) {
                user = it
            }
        }

        textviewWelcome = findViewById(R.id.textview_welcome)
        btnLogout = findViewById(R.id.btn_logout)


        user.let {
            // Name, email address, and profile photo Url
            name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }

        name = if (intent != null) {
            intent.getStringExtra(Name)
        } else {
            "!"
        }

        "Welcome\n $name !".also { textviewWelcome.text = it }


        btnLogout.setOnClickListener {

            val dialog = MaterialAlertDialogBuilder(this)
            dialog.setTitle("LogOut")
            dialog.setMessage("Do you really want to Logout?")
            dialog.setIcon(R.drawable.ic_baseline_login_24)

            dialog.setPositiveButton("Yeah") { _, _ ->

                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                finish()
            }

            dialog.setNeutralButton("Cancel") { _, _ ->
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()

            }

            dialog.create()
            dialog.setCancelable(false)
            dialog.show()

        }

    }
}