package com.example.cinesearch

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieDetailActivity : AppCompatActivity() {

    private val API_KEY = "83639fed"
    private val BASE_URL = "https://www.omdbapi.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Action Bar Navigation Arrow
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Movie Details"

        // On-screen Back Button Handler
        val btnBack = findViewById<Button>(R.id.btnBackToResults)
        btnBack.setOnClickListener {
            finish() // Closes this Activity and returns to search results
        }

        val imdbId = intent.getStringExtra("IMDB_ID") ?: return

        val ivPoster = findViewById<ImageView>(R.id.detailPoster)
        val tvTitle = findViewById<TextView>(R.id.detailTitle)
        val tvMeta = findViewById<TextView>(R.id.detailMeta)
        val tvDirector = findViewById<TextView>(R.id.detailDirector)
        val tvCast = findViewById<TextView>(R.id.detailCast)
        val tvPlot = findViewById<TextView>(R.id.detailPlot)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(OmdbApiService::class.java)

        apiService.getMovieDetails(imdbId, API_KEY).enqueue(object : Callback<MovieDetailResponse> {
            override fun onResponse(call: Call<MovieDetailResponse>, response: Response<MovieDetailResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val movie = response.body()!!

                    tvTitle.text = movie.title ?: "N/A"
                    tvMeta.text = "${movie.year ?: ""} | ${movie.runtime ?: ""} | Rating: ${movie.imdbRating ?: "N/A"}"
                    tvDirector.text = "Director: ${movie.director ?: "N/A"}"
                    tvCast.text = "Cast: ${movie.actors ?: "N/A"}"
                    tvPlot.text = "Plot: ${movie.plot ?: "N/A"}"

                    if (!movie.poster.isNullOrEmpty() && movie.poster != "N/A") {
                        Glide.with(this@MovieDetailActivity).load(movie.poster).into(ivPoster)
                    }
                }
            }

            override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                Toast.makeText(this@MovieDetailActivity, "Error loading details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}