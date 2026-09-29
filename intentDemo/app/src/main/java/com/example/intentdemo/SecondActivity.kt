package com.example.intentdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {

    private val LOG_TAG = "SecondActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        // Adjust layout padding to accommodate status and navigation bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.second_main_root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        val tvStatusMessage = findViewById<TextView>(R.id.tv_result)
        val btnContinueAction = findViewById<Button>(R.id.btn_ok)
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)

        // Link the bottom navigation bar using our helper
        NavHelper.setupBottomNav(this, bottomNavView, R.id.nav_success)

        // Retrieve data passed from the Login screen
        val displayName = intent.getStringExtra("USER_NAME") ?: "User"
        val loginWasSuccessful = intent.getBooleanExtra("IS_SUCCESS", false)

        // Explicitly ensure the action button is visible
        btnContinueAction.visibility = View.VISIBLE

        if (loginWasSuccessful) {
            displaySuccess(tvStatusMessage, displayName, btnContinueAction)
        } else {
            displayFailure(tvStatusMessage, displayName, btnContinueAction)
        }
    }

    private fun displaySuccess(textView: TextView, name: String, actionButton: Button) {
        textView.text = getString(R.string.login_success, name)
        
        actionButton.setOnClickListener {
            Log.d(LOG_TAG, "Login acknowledged. Navigating to Dashboard.")
            val dashboardIntent = Intent(this, ThirdActivity::class.java)
            startActivity(dashboardIntent)
        }
    }

    private fun displayFailure(textView: TextView, name: String, actionButton: Button) {
        textView.text = getString(R.string.login_failed, name)
        
        actionButton.setOnClickListener {
            Log.d(LOG_TAG, "Failure acknowledged. Returning to Login.")
            // Close this activity to return to the previous screen (if still in stack)
            finish()
        }
    }
}