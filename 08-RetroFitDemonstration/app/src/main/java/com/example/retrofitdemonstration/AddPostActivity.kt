package com.example.retrofitdemonstration

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linearLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
        }

        val heading = TextView(this).apply {
            text = "ADD NEW POST"
            textSize = 24f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 30)
            setTextColor(Color.BLACK)
        }

        val userIdEditText = EditText(this).apply {
            hint = "User ID"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val titleEditText = EditText(this).apply { hint = "Title" }
        val bodyEditText = EditText(this).apply {
            hint = "Body"
            minLines = 4
            gravity = Gravity.TOP
        }

        val submitButton = Button(this).apply { text = "SUBMIT" }
        val cancelButton = Button(this).apply { text = "CANCEL" }

        linearLayout.addView(heading)
        linearLayout.addView(userIdEditText)
        linearLayout.addView(titleEditText)
        linearLayout.addView(bodyEditText)
        linearLayout.addView(submitButton)
        linearLayout.addView(cancelButton)

        setContentView(linearLayout)

        cancelButton.setOnClickListener { finish() }

        submitButton.setOnClickListener {
            val userIdText = userIdEditText.text.toString()
            val title = titleEditText.text.toString()
            val body = bodyEditText.text.toString()

            if (userIdText.isBlank() || title.isBlank() || body.isBlank()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newPost = CreatePostObject(userIdText.toInt(), title, body)

            RetrofitClient.api.createPost(newPost).enqueue(object : Callback<PostObject> {
                override fun onResponse(call: Call<PostObject>, response: Response<PostObject>) {
                    if (response.isSuccessful) {
                        Log.d("POST", "Created ID: ${response.body()?.id}")
                        Toast.makeText(this@AddPostActivity, "Data submitted successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@AddPostActivity, "POST failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostObject>, t: Throwable) {
                    Toast.makeText(this@AddPostActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}