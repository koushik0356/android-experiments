package com.example.retrofitdemonstration

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var allPosts: List<PostObject> = emptyList()
    private lateinit var postsContainer: LinearLayout
    private lateinit var userIdSpinner: Spinner
    private lateinit var headerLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mainLayout = LinearLayout(this)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.setPadding(30, 30, 30, 30)

        val addButton = Button(this)
        addButton.text = "ADD DATA"
        addButton.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }
        mainLayout.addView(addButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        val listButton = Button(this)
        listButton.text = "LIST DATA"
        listButton.setOnClickListener {
            loadData()
        }
        mainLayout.addView(listButton, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        userIdSpinner = Spinner(this)
        userIdSpinner.setPadding(10, 20, 10, 20)
        userIdSpinner.visibility = View.GONE

        val spinnerOptions = arrayListOf("All")
        for (i in 1..10) {
            spinnerOptions.add(i.toString())
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userIdSpinner.adapter = adapter

        mainLayout.addView(userIdSpinner, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ))

        headerLayout = LinearLayout(this)
        headerLayout.orientation = LinearLayout.HORIZONTAL
        headerLayout.setPadding(0, 15, 0, 15)
        headerLayout.visibility = View.GONE

        val idHeader = TextView(this)
        idHeader.text = "ID"
        idHeader.typeface = Typeface.DEFAULT_BOLD

        val userIdHeader = TextView(this)
        userIdHeader.text = "USER ID"
        userIdHeader.typeface = Typeface.DEFAULT_BOLD

        val titleHeader = TextView(this)
        titleHeader.text = "TITLE"
        titleHeader.typeface = Typeface.DEFAULT_BOLD

        val bodyHeader = TextView(this)
        bodyHeader.text = "BODY"
        bodyHeader.typeface = Typeface.DEFAULT_BOLD

        headerLayout.addView(idHeader, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        headerLayout.addView(userIdHeader, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f))
        headerLayout.addView(titleHeader, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f))
        headerLayout.addView(bodyHeader, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f))

        mainLayout.addView(headerLayout)

        val scrollView = ScrollView(this)
        postsContainer = LinearLayout(this)
        postsContainer.orientation = LinearLayout.VERTICAL
        scrollView.addView(postsContainer)

        mainLayout.addView(scrollView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        ))

        setContentView(mainLayout)

        userIdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (allPosts.isNotEmpty()) {
                    filterAndDisplayPosts()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadData() {
        RetrofitClient.api.getPosts().enqueue(object : Callback<List<PostObject>> {
            override fun onResponse(call: Call<List<PostObject>>, response: Response<List<PostObject>>) {
                if (response.isSuccessful) {
                    allPosts = response.body() ?: emptyList()
                    Log.d("RETROFIT", "Loaded ${allPosts.size} posts")
                    Toast.makeText(this@MainActivity, "Data Loaded", Toast.LENGTH_SHORT).show()

                    userIdSpinner.visibility = View.VISIBLE
                    headerLayout.visibility = View.VISIBLE

                    filterAndDisplayPosts()
                } else {
                    Log.e("RETROFIT", "HTTP Error: ${response.code()}")
                    Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<PostObject>>, t: Throwable) {
                Log.e("RETROFIT", "GET request failed", t)
                Toast.makeText(this@MainActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterAndDisplayPosts() {
        postsContainer.removeAllViews()

        val selectedOption = userIdSpinner.selectedItem?.toString() ?: "All"

        val filteredPosts = if (selectedOption == "All") {
            allPosts
        } else {
            val selectedUserId = selectedOption.toIntOrNull()
            if (selectedUserId != null) {
                allPosts.filter { it.userId == selectedUserId }
            } else {
                allPosts
            }
        }

        filteredPosts.forEach { post ->
            val rowLayout = LinearLayout(this)
            rowLayout.orientation = LinearLayout.HORIZONTAL
            rowLayout.setPadding(0, 15, 0, 15)
            rowLayout.isClickable = true
            rowLayout.isFocusable = true

            val outValue = TypedValue()
            theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            rowLayout.setBackgroundResource(outValue.resourceId)

            val idTextView = TextView(this)
            idTextView.text = post.id.toString()

            val userIdTextView = TextView(this)
            userIdTextView.text = post.userId.toString()

            val titleTextView = TextView(this)
            titleTextView.text = post.title

            val bodyTextView = TextView(this)
            bodyTextView.text = post.body

            rowLayout.addView(idTextView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            rowLayout.addView(userIdTextView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.5f))
            rowLayout.addView(titleTextView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f))
            rowLayout.addView(bodyTextView, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 3f))

            rowLayout.setOnClickListener {
                showPostDetailsDialog(post)
            }

            postsContainer.addView(rowLayout)
        }
    }

    private fun showPostDetailsDialog(post: PostObject) {
        val detailsLayout = LinearLayout(this)
        detailsLayout.orientation = LinearLayout.VERTICAL
        detailsLayout.setPadding(50, 40, 50, 40)

        val idText = TextView(this)
        idText.text = "ID: ${post.id}"
        idText.textSize = 16f
        idText.setTextColor(Color.BLACK)
        idText.setPadding(0, 0, 0, 10)

        val userIdText = TextView(this)
        userIdText.text = "USER ID: ${post.userId}"
        userIdText.textSize = 16f
        userIdText.setTextColor(Color.BLACK)
        userIdText.setPadding(0, 0, 0, 30)

        val titleLabel = TextView(this)
        titleLabel.text = "TITLE:"
        titleLabel.typeface = Typeface.DEFAULT_BOLD
        titleLabel.textSize = 16f
        titleLabel.setTextColor(Color.BLACK)

        val titleContent = TextView(this)
        titleContent.text = post.title
        titleContent.textSize = 16f
        titleContent.setTextColor(Color.BLACK)
        titleContent.setPadding(0, 5, 0, 30)

        val bodyLabel = TextView(this)
        bodyLabel.text = "BODY:"
        bodyLabel.typeface = Typeface.DEFAULT_BOLD
        bodyLabel.textSize = 16f
        bodyLabel.setTextColor(Color.BLACK)

        val bodyContent = TextView(this)
        bodyContent.text = post.body
        bodyContent.textSize = 16f
        bodyContent.setTextColor(Color.BLACK)
        bodyContent.setPadding(0, 5, 0, 0)

        detailsLayout.addView(idText)
        detailsLayout.addView(userIdText)
        detailsLayout.addView(titleLabel)
        detailsLayout.addView(titleContent)
        detailsLayout.addView(bodyLabel)
        detailsLayout.addView(bodyContent)

        val scrollView = ScrollView(this)
        scrollView.addView(detailsLayout)

        AlertDialog.Builder(this)
            .setTitle("Post Details")
            .setView(scrollView)
            .setPositiveButton("OK", null)
            .show()
    }
}