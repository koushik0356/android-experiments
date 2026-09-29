package com.example.intentdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class MainActivity : AppCompatActivity() {

    // Pre-registered credentials for local validation
    private val VALID_USER_ID = "Koushik"
    private val VALID_PASSWORD = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Setup edge-to-edge padding to prevent content from being obscured by system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout_root)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        val btnLoginAction = findViewById<Button>(R.id.btn_login)
        val btnClearInputs = findViewById<Button>(R.id.btn_clear)
        val etUsernameField = findViewById<EditText>(R.id.et_username)
        val etPasswordField = findViewById<EditText>(R.id.et_password)

        // Initialize the common bottom navigation bar
        val bottomNavView = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navigation)
        NavHelper.setupBottomNav(this, bottomNavView, R.id.nav_login)

        btnLoginAction.setOnClickListener {
            val inputUsername = etUsernameField.text.toString().trim()
            val inputPassword = etPasswordField.text.toString().trim()

            // Validate inputs are not empty before checking credentials
            if (inputUsername.isEmpty()) {
                etUsernameField.error = "Username is required"
                etUsernameField.requestFocus()
            } else if (inputPassword.isEmpty()) {
                etPasswordField.error = "Password is required"
                etPasswordField.requestFocus()
            } else {
                processLoginAttempt(inputUsername, inputPassword)
            }
        }

        btnClearInputs.setOnClickListener {
            etUsernameField.text.clear()
            etPasswordField.text.clear()
            etUsernameField.error = null
            etPasswordField.error = null
        }
    }

    private fun processLoginAttempt(username: String, password: String) {
        val loginIsSuccessful = (username == VALID_USER_ID && password == VALID_PASSWORD)
        
        if (loginIsSuccessful) {
            // Update global session state
            LoginState.isLoggedIn = true
            LoginState.userName = username
            
            // Navigate to the Success screen
            val successIntent = Intent(this, SecondActivity::class.java).apply {
                putExtra("USER_NAME", username)
                putExtra("IS_SUCCESS", true)
            }
            startActivity(successIntent)
            
            // Finish this activity so the user cannot navigate back to login after success
            finish()
        } else {
            Toast.makeText(this, "Login Failed: Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }
}