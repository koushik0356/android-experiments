package com.example.cinesearch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val API_KEY = "83639fed"
    private val BASE_URL = "https://www.omdbapi.com/"

    private lateinit var etMovieSearch: EditText
    private lateinit var btnSearch: Button
    private lateinit var rvMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Title at the top bar
        supportActionBar?.title = "CineSearch"

        etMovieSearch = findViewById(R.id.etMovieSearch)
        btnSearch = findViewById(R.id.btnSearch)
        rvMovies = findViewById(R.id.rvMovies)

        // Single column vertical layout
        rvMovies.layoutManager = LinearLayoutManager(this)

        movieAdapter = MovieAdapter(emptyList()) { selectedMovie ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("IMDB_ID", selectedMovie.imdbID)
            startActivity(intent)
        }
        rvMovies.adapter = movieAdapter

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(OmdbApiService::class.java)

        btnSearch.setOnClickListener {
            val query = etMovieSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                apiService.searchMovies(query, API_KEY).enqueue(object : Callback<MovieSearchResponse> {
                    override fun onResponse(call: Call<MovieSearchResponse>, response: Response<MovieSearchResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            val body = response.body()!!
                            if (body.response == "True" && body.searchResults != null) {
                                movieAdapter.updateData(body.searchResults)
                            } else {
                                Toast.makeText(this@MainActivity, body.error ?: "No results found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<MovieSearchResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Network Error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a movie name", Toast.LENGTH_SHORT).show()
            }
        }
    }
}