package com.example.cinesearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {
    // Search movies by title keyword (returns list)
    @GET("/")
    fun searchMovies(
        @Query("s") title: String,
        @Query("apikey") apiKey: String
    ): Call<MovieSearchResponse>

    // Get exact movie details by IMDb ID
    @GET("/")
    fun getMovieDetails(
        @Query("i") imdbId: String,
        @Query("apikey") apiKey: String
    ): Call<MovieDetailResponse>
}