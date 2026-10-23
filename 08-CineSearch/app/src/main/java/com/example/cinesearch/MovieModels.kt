package com.example.cinesearch

import com.google.gson.annotations.SerializedName

// Search Result Items
data class MovieSearchResponse(
    @SerializedName("Search") val searchResults: List<MovieItem>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response") val response: String,
    @SerializedName("Error") val error: String?
)

data class MovieItem(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)

// Detailed Movie Response
data class MovieDetailResponse(
    @SerializedName("Title") val title: String?,
    @SerializedName("Year") val year: String?,
    @SerializedName("Runtime") val runtime: String?,
    @SerializedName("imdbRating") val imdbRating: String?,
    @SerializedName("Director") val director: String?,
    @SerializedName("Writer") val writer: String?,
    @SerializedName("Actors") val actors: String?,
    @SerializedName("Plot") val plot: String?,
    @SerializedName("Poster") val poster: String?,
    @SerializedName("Type") val type: String?
)