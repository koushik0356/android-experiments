package com.example.intentdemo

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView

object NavHelper {
    fun setupBottomNav(activity: Activity, navView: BottomNavigationView, currentItemId: Int) {
        navView.selectedItemId = currentItemId
        navView.setOnItemSelectedListener { item ->
            if (item.itemId == currentItemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.nav_login -> {
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    if (activity !is MainActivity) activity.finish()
                    true
                }
                R.id.nav_success -> {
                    if (LoginState.isLoggedIn) {
                        val intent = Intent(activity, SecondActivity::class.java).apply {
                            putExtra("USER_NAME", LoginState.userName)
                            putExtra("IS_SUCCESS", true)
                        }
                        activity.startActivity(intent)
                        if (activity !is SecondActivity) activity.finish()
                    } else {
                        Toast.makeText(activity, "Please login first", Toast.LENGTH_SHORT).show()
                    }
                    LoginState.isLoggedIn
                }
                R.id.nav_dashboard -> {
                    if (LoginState.isLoggedIn) {
                        activity.startActivity(Intent(activity, ThirdActivity::class.java))
                        if (activity !is ThirdActivity) activity.finish()
                    } else {
                        Toast.makeText(activity, "Please login first", Toast.LENGTH_SHORT).show()
                    }
                    LoginState.isLoggedIn
                }
                else -> false
            }
        }
    }
}