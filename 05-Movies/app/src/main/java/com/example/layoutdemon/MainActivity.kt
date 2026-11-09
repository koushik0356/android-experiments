package com.example.layoutdemon

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkBoxList: List<CheckBox>
    private lateinit var radioButtonIds: List<Int>
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        movies = listOf(
            Movie("Solo Leveling", "2024", R.drawable.solo_leveling),
            Movie("X-Men Origins: Wolverine", "2009", R.drawable.wolverine),
            Movie("John Wick: Chapter 4", "2023", R.drawable.john_wick),
            Movie("One Piece", "1999", R.drawable.one_piece),
            Movie("Ready Player One", "2018", R.drawable.ready_player_one),
            Movie("Shang-Chi and the Legend of the Ten Rings", "2021", R.drawable.shang_chi)
        )

        val spinnerData = listOf("Table Layout", "Grid Layout", "Scroll View & Grid Layout", "Recycle View")
        spinner = findViewById(R.id.spinner)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
        spinner.adapter = spinnerAdapter

        radioGroup = findViewById(R.id.radioGroup)
        radioButtonIds = listOf(R.id.radioButton5, R.id.radioButton6, R.id.radioButton7, R.id.radioButton8)

        checkBoxList = listOf(
            findViewById(R.id.checkBox8),
            findViewById(R.id.checkBox9),
            findViewById(R.id.checkBox10),
            findViewById(R.id.checkBox11)
        )

        recyclerView = findViewById(R.id.recyclerView)
        movieAdapter = MovieAdapter(movies, R.layout.item_movie_table)
        recyclerView.adapter = movieAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val index = radioButtonIds.indexOf(checkedId)
            if (index != -1) {
                updateSelection(index, fromRadio = true)
            }
        }

        // Set up CheckBox listeners
        checkBoxList.forEachIndexed { index, checkBox ->
            checkBox.setOnClickListener {
                updateSelection(index, fromCheckBox = true)
            }
        }

        // Set up Spinner listener
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSelection(position, fromSpinner = true)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        radioGroup.check(R.id.radioButton5)
    }

    private fun updateSelection(index: Int, fromRadio: Boolean = false, fromCheckBox: Boolean = false, fromSpinner: Boolean = false) {
        if (!fromRadio) {
            radioGroup.check(radioButtonIds[index])
        }

        if (!fromSpinner) {
            spinner.setSelection(index)
        }

        checkBoxList.forEachIndexed { i, checkBox ->
            checkBox.isChecked = (i == index)
        }

        when (index) {
            0 -> {
                recyclerView.layoutManager = LinearLayoutManager(this)
                movieAdapter.setLayoutResId(R.layout.item_movie_table, hybrid = false)
            }
            1 -> {
                recyclerView.layoutManager = GridLayoutManager(this, 2)
                movieAdapter.setLayoutResId(R.layout.item_movie_grid, hybrid = false)
            }
            2 -> {
                val gridLayoutManager = GridLayoutManager(this, 2)
                gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position < 3) 2 else 1
                    }
                }
                recyclerView.layoutManager = gridLayoutManager
                movieAdapter.setLayoutResId(R.layout.item_movie_list, hybrid = true)
            }
            3 -> { // Recycle View
                recyclerView.layoutManager = LinearLayoutManager(this)
                movieAdapter.setLayoutResId(R.layout.item_movie_list, hybrid = false)
            }
        }
    }
}